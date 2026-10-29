// ==UserScript==
// @name         Estudário – fila automática do ChatGPT
// @namespace    estudario
// @version      1.0
// @description  Envia os pedidos da fila do Estudário ao projeto "Gerador Estudário" e salva no PC os arquivos entregues.
// @match        https://chatgpt.com/*
// @grant        GM_xmlhttpRequest
// @grant        GM_getValue
// @grant        GM_setValue
// @connect      127.0.0.1
// @run-at       document-idle
// ==/UserScript==
// Precisa do servidor local aberto: scripts\chatgpt\INICIAR-SERVIDOR-FILA.cmd
(function () {
  "use strict";
  const PROJ = "/g/g-p-6ae29815eb9c81918f823336b21516cf/project";
  const GIZMO = "g-p-6ae29815eb9c81918f823336b21516cf";
  const SERV = "http://127.0.0.1:8788";
  const POR_CICLO = 30;
  const INTERVALO = 15 * 60_000;
  const PREFIXO = "ENTREGA OBRIGATÓRIA EM ARQUIVO: use a ferramenta de código (Python) para gravar o JSON em /mnt/data/<nome>.json e responda apenas com o link de download. NÃO escreva as questões nem o gabarito no chat; resposta em texto sem arquivo não serve. Pedido: ";

  const w = (ms) => new Promise((r) => setTimeout(r, ms));
  const get = (k, d) => GM_getValue(k, d);
  const set = (k, v) => GM_setValue(k, v);
  const local = (method, path, body) => new Promise((ok, erro) => GM_xmlhttpRequest({
    method, url: SERV + path, data: body ? JSON.stringify(body) : undefined,
    headers: { "Content-Type": "application/json" },
    onload: (r) => { try { ok(JSON.parse(r.responseText)); } catch { erro(new Error(r.responseText)); } },
    onerror: () => erro(new Error("servidor da fila fechado (abra INICIAR-SERVIDOR-FILA.cmd)")),
  }));

  // ---------------------------------------------------------------- painel
  const painel = document.createElement("div");
  painel.style.cssText = "position:fixed;bottom:12px;left:12px;z-index:99999;background:#1e293b;color:#fff;font:12px sans-serif;padding:8px 10px;border-radius:8px;max-width:320px;opacity:.92";
  const status = document.createElement("div");
  const botao = document.createElement("button");
  botao.style.cssText = "margin-top:6px;padding:2px 8px;border-radius:4px;border:0;cursor:pointer";
  botao.onclick = () => { set("ativo", !get("ativo", false)); atualizar(); if (get("ativo", false)) passo(); };
  painel.append(status, botao);
  document.body.append(painel);
  let msg = "";
  function atualizar(m) {
    if (m) msg = m;
    const fila = get("fila", []);
    const prox = get("proximoCiclo", 0);
    const falta = Math.max(0, Math.round((prox - Date.now()) / 60000));
    status.textContent = `Estudário: ${get("ativo", false) ? "LIGADO" : "pausado"} · fila ${fila.length} · salvos ${get("salvos", 0)}` +
      (fila.length ? "" : ` · próximo ciclo em ${falta} min`) + (msg ? ` · ${msg}` : "");
    botao.textContent = get("ativo", false) ? "Pausar" : "Ligar";
  }
  atualizar();

  // ---------------------------------------------------------------- envio
  let enviando = false;
  async function passo() {
    if (enviando || !get("ativo", false)) return;
    enviando = true;
    try {
      const naProjeto = location.pathname === PROJ;
      let fila = get("fila", []);
      if (!fila.length && Date.now() >= get("proximoCiclo", 0)) {
        fila = await local("GET", `/proximos?n=${POR_CICLO}`);
        set("fila", fila); set("proximoCiclo", Date.now() + INTERVALO);
        atualizar(`ciclo novo: ${fila.length} pedidos`);
      }
      if (!fila.length) return;
      if (!naProjeto) { await w(4000); location.href = PROJ; return; }
      let ed;
      for (let i = 0; i < 40 && !(ed = document.querySelector("#prompt-textarea, [contenteditable='true']")); i++) await w(500);
      if (!ed) return;
      await w(1500);
      ed.focus();
      document.execCommand("selectAll");
      document.execCommand("insertText", false, PREFIXO + fila[0]);
      let b;
      for (let i = 0; i < 40 && !(b = document.querySelector("[data-testid='send-button'], button[aria-label='Enviar']")); i++) await w(500);
      if (!b) { atualizar("botão de enviar não apareceu; tento de novo"); await w(5000); location.href = PROJ; return; }
      b.click();
      for (let i = 0; i < 60 && !/\/c\/[0-9a-f]{8}-/.test(location.pathname); i++) await w(500);
      if (!/\/c\//.test(location.pathname)) { atualizar("envio não confirmado; tento de novo"); await w(3000); location.href = PROJ; return; }
      fila.shift(); set("fila", fila);
      set("enviados", get("enviados", 0) + 1);
      atualizar(`enviado; faltam ${fila.length}`);
      await w(3000);
      location.href = PROJ; // recarrega a página do projeto para o próximo (evita a caixa travada)
    } catch (e) {
      atualizar(String(e.message || e));
    } finally { enviando = false; }
  }

  // ---------------------------------------------------------------- coleta dos arquivos
  let coletando = false;
  async function coletar() {
    if (coletando) return;
    coletando = true;
    try {
      const s = await (await fetch("/api/auth/session")).json();
      const h = { Authorization: "Bearer " + s.accessToken };
      const l = await (await fetch(`/backend-api/gizmos/${GIZMO}/conversations?cursor=0`, { headers: h })).json();
      const feitos = new Set(get("coletados", []));
      for (const c of (l.items || []).slice(0, 40)) {
        if (feitos.has(c.id)) continue;
        const d = await (await fetch("/backend-api/conversation/" + c.id, { headers: h })).json();
        let mid, path;
        for (const m of Object.values(d.mapping || {})) {
          if (m.message?.author?.role !== "assistant") continue;
          const f = JSON.stringify(m.message.content).match(/sandbox:(\/mnt\/data\/[^)\s"\\]+\.json)/);
          if (f) { mid = m.message.id; path = f[1]; }
        }
        if (!mid) continue; // ainda escrevendo (ou respondeu sem arquivo): olha de novo depois
        const r = await (await fetch(`/backend-api/conversation/${c.id}/interpreter/download?message_id=${mid}&sandbox_path=${encodeURIComponent(path)}`, { headers: h })).json();
        if (!r.download_url) continue;
        const conteudo = await (await fetch(r.download_url)).text();
        const nome = path.split("/").pop().replace(/\.json$/, "");
        const res = await local("POST", "/arquivo", { nome, conteudo });
        if (res.ok) {
          feitos.add(c.id); set("coletados", [...feitos].slice(-3000));
          if (!res.ja) set("salvos", get("salvos", 0) + 1);
          atualizar();
        }
      }
    } catch (e) {
      atualizar("coleta: " + String(e.message || e));
    } finally { coletando = false; }
  }

  setTimeout(passo, 3000);
  setInterval(passo, 30_000);
  setInterval(atualizar, 30_000);
  setTimeout(coletar, 20_000);
  setInterval(coletar, 3 * 60_000);
})();
