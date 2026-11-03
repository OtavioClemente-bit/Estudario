// ==UserScript==
// @name         Estudário – fila automática do Gemini
// @namespace    estudario
// @version      1.9
// @updateURL    http://127.0.0.1:8788/estudario-gemini.meta.js
// @downloadURL  http://127.0.0.1:8788/estudario-gemini.user.js
// @description  Envia os pedidos da fila do Estudário ao Gemini (modo Argon) e salva no PC o JSON de cada resposta (arquivos .chatgpt-80N).
// @match        https://gemini.google.com/*
// @grant        GM_xmlhttpRequest
// @grant        GM_getValue
// @grant        GM_setValue
// @connect      127.0.0.1
// @run-at       document-idle
// ==/UserScript==
// Precisa do servidor local aberto: scripts\chatgpt\INICIAR-SERVIDOR-FILA.cmd (o mesmo do ChatGPT).
// Um pedido por conversa nova: cola as regras + o pedido, espera a resposta terminar, lê o bloco ```json da página
// e manda ao servidor. O nome do arquivo termina em .chatgpt-80N (rodada N + 800) para distinguir do ChatGPT.
(function () {
  "use strict";
  const SERV = "http://127.0.0.1:8788";
  const MODELO = "Argon";
  const POR_CICLO = 10;
  const PAUSA = () => (2 + Math.random()) * 60_000; // 2 a 3 min entre um pedido e o próximo
  const PREFIXO = `Você é autor de questões inéditas de concurso público no Brasil, no estilo exato da banca pedida. Responda SOMENTE com UM bloco de código \`\`\`json contendo um objeto JSON válido e completo, sem nenhum texto antes ou depois do bloco.
FORMATO: {"secoes":[{"numero":1,"titulo":"..."}],"questoes":[{"secao":1,"enunciado":"...","alternativas":["...","...","...","...","..."],"gabarito":"C","explicacao":"..."}]}
REGRAS: use exatamente as seções e a quantidade pedidas, distribuídas por igual; múltipla escolha tem SEMPRE 5 alternativas (sem a letra no texto) e só UMA defensável como correta; no formato Certo/Errado omita "alternativas" e use gabarito "Certo" ou "Errado", com equilíbrio entre os dois; cada explicação tem 150 a 300 caracteres, diz por que a resposta está certa e o erro das outras, e nunca cita letra; não cite número de lei, artigo, súmula ou prazo de que não tenha certeza (explique o conteúdo); não use normas, decisões ou fatos posteriores a 2024; evite pontos controvertidos; varie dificuldade e não repita enunciados.
PEDIDO:
`;

  const w = (ms) => new Promise((r) => setTimeout(r, ms));
  const ABA = Math.random().toString(36).slice(2);
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
  painel.style.cssText = "position:fixed;bottom:12px;left:12px;z-index:99999;background:#1e293b;color:#fff;font:12px sans-serif;padding:8px 10px;border-radius:8px;max-width:340px;opacity:.92";
  const status = document.createElement("div");
  const botao = document.createElement("button");
  botao.style.cssText = "margin-top:6px;padding:2px 8px;border-radius:4px;border:0;cursor:pointer";
  // Ligar zera esperas pendentes (limite, pausa entre pedidos, envio não confirmado) e assume esta aba.
  botao.onclick = () => {
    set("ativo", !get("ativo", false));
    if (get("ativo", false)) { set("proximo", 0); set("atual", null); set("dono", { id: ABA, t: Date.now() }); msg = ""; }
    atualizar(); if (get("ativo", false)) passo();
  };
  painel.append(status, botao);
  document.body.append(painel);
  let msg = "";
  function atualizar(m) {
    if (m) msg = m;
    const prox = get("proximo", 0);
    const espera = prox > Date.now() ? ` · próximo envio às ${new Date(prox).toLocaleTimeString("pt-BR").slice(0, 5)}` : "";
    status.textContent = `Estudário Gemini: ${get("ativo", false) ? "LIGADO" : "pausado"} · fila ${get("fila", []).length} · salvos ${get("salvos", 0)} · falhas ${get("falhas", 0)}${espera}` + (msg ? ` · ${msg}` : "");
    botao.textContent = get("ativo", false) ? "Pausar" : "Ligar";
  }
  atualizar();

  // ---------------------------------------------------------------- modelo
  async function garantirModelo() {
    // A tela inicial tem "Chat" e "Tarefas": o pedido tem que ir pelo Chat.
    const chat = [...document.querySelectorAll(".gem-segmented-button-action")].find((e) => e.innerText?.trim() === "Chat");
    if (chat && !chat.parentElement?.classList.contains("gem-segmented-button-selected")) { chat.click(); await w(1200); }
    const seletor = () => document.querySelector("button[aria-label^='Abrir seletor de modo']");
    const itens = () => [...document.querySelectorAll("[role='menuitem']")];
    const fechar = () => document.body.dispatchEvent(new KeyboardEvent("keydown", { key: "Escape", bubbles: true }));
    if (!seletor()) return false;
    seletor().click();
    await w(1200);
    // Modelo (Argon)
    const modelo = itens().find((e) => e.innerText.includes(MODELO));
    if (!modelo) { fechar(); return false; }
    if (!modelo.classList.contains("selected")) { modelo.click(); await w(1500); seletor().click(); await w(1200); }
    // Nível de raciocínio: Médio (o Alto levava 20 a 40 min por resposta)
    const nivel = itens().find((e) => /Nível de raciocínio/.test(e.innerText));
    if (nivel) {
      nivel.click();
      await w(1000);
      const medio = itens().find((e) => /^Médio\b/.test(e.innerText.trim()));
      if (medio && !medio.classList.contains("selected")) { medio.click(); await w(1200); } else fechar();
    } else fechar();
    await w(500);
    fechar();
    return (seletor()?.getAttribute("aria-label") || "").includes(MODELO);
  }

  // ---------------------------------------------------------------- envio + leitura
  // Rodada N do pedido vira chatgpt-80N (ex.: -1 → -801), para não colidir com os arquivos do ChatGPT.
  const nomeDe = (pedido) => (pedido.match(/nome=([a-z0-9.-]+)/)?.[1] || "").replace(/\.chatgpt-(\d+)$/, (_, n) => `.chatgpt-${800 + Number(n)}`);

  async function esperarResposta() {
    // Termina quando há bloco de código, a barra de ações da resposta aparece e o texto parou de mudar.
    // Prazo pelo relógio, não por número de voltas: com a aba em segundo plano o Chrome atrasa os timers e
    // "240 voltas de 5 s" virava horas; a aba ficava presa em "esperando a resposta" até alguém recarregar (03/11).
    let antes = -1, iguais = 0;
    const fim = Date.now() + 20 * 60_000;
    while (Date.now() < fim) {
      await w(5000);
      set("dono", { id: ABA, t: Date.now() });
      const r = [...document.querySelectorAll("model-response")].pop();
      if (!r) continue;
      if (/unusual activity|atividade incomum|tente novamente mais tarde|limite/i.test(r.innerText) && !r.querySelector("code")) return { erro: "Gemini recusou (limite?)" };
      const code = r.querySelector("code-block code, code");
      const len = (r.innerText || "").length;
      const pronto = code && r.querySelector("message-actions") && !document.querySelector("button[aria-label*='Parar'], button[aria-label*='Interromper']");
      iguais = len === antes ? iguais + 1 : 0;
      antes = len;
      if (pronto && iguais >= 2) return { texto: code.innerText };
    }
    return { erro: "resposta não terminou em 20 min" };
  }

  let enviando = false;
  async function passo() {
    if (enviando || !get("ativo", false)) return;
    // Só uma aba trabalha: outra aba do Gemini aberta fica parada enquanto a dona der sinal de vida.
    const dono = get("dono", null);
    if (dono && dono.id !== ABA && Date.now() - dono.t < 90_000) { atualizar("outra aba está trabalhando"); return; }
    set("dono", { id: ABA, t: Date.now() });
    if (Date.now() < get("proximo", 0)) return;
    enviando = true;
    try {
      // Pedido em andamento (a página recarregou no meio): lê a resposta desta conversa.
      const atual = get("atual", null);
      const naConversa = () => /^\/app\/[0-9a-f]+/.test(location.pathname) || !!document.querySelector("model-response");
      if (atual && naConversa()) {
        atualizar("esperando a resposta…");
        const r = await esperarResposta();
        set("atual", null);
        set("vivo", Date.now());
        set("proximo", Date.now() + PAUSA());
        if (r.erro) { set("falhas", get("falhas", 0) + 1); atualizar(r.erro); if (/recusou/.test(r.erro)) set("proximo", Date.now() + 30 * 60_000); return; }
        let ok = false;
        try { JSON.parse(r.texto); ok = true; } catch { /* inválido */ }
        if (!ok) { set("falhas", get("falhas", 0) + 1); atualizar("JSON inválido; pulei"); return; }
        const res = await local("POST", "/arquivo", { nome: atual.nome, conteudo: r.texto });
        if (res.ok && !res.ja) set("salvos", get("salvos", 0) + 1);
        atualizar(res.ok ? `salvo ${atual.nome}` : `servidor recusou: ${res.erro}`);
        return;
      }
      // Pedido em andamento, mas a conversa ainda não apareceu: o Argon pode demorar para abrir a
      // conversa. Espera até 5 min desde o envio; só depois esquece e segue (antes recarregava sem parar).
      if (atual) {
        if (Date.now() - (atual.t ?? 0) < 5 * 60_000) { atualizar("enviado; esperando a conversa abrir…"); return; }
        set("atual", null); atualizar(`envio de ${atual.nome} não confirmado em 5 min; sigo para o próximo`);
      }

      let fila = get("fila", []);
      if (!fila.length) {
        fila = (await local("GET", `/proximos?n=${POR_CICLO}&tipo=QUESTOES`)).filter((p) => /^QUESTÕES:/.test(p));
        set("fila", fila);
        atualizar(`ciclo novo: ${fila.length} pedidos`);
        if (!fila.length) { set("proximo", Date.now() + 15 * 60_000); return; }
      }
      if (location.pathname !== "/app") { location.href = "/app"; return; }
      let ed;
      for (let i = 0; i < 40 && !(ed = document.querySelector("rich-textarea .ql-editor")); i++) await w(500);
      if (!ed) return;
      // Limite do plano ("Seu limite será redefinido neste horário: 6:56 PM"): espera até lá, sem cair para um
      // modelo mais fraco nem ficar recarregando.
      const aviso = document.body.innerText.match(/limite será redefinido[^\d]*(\d{1,2}):(\d{2})\s*(AM|PM)?/i);
      if (aviso) {
        let h = +aviso[1] % 12 + (/PM/i.test(aviso[3] || "") ? 12 : 0);
        if (!aviso[3]) h = +aviso[1];
        const volta = new Date(); volta.setHours(h, +aviso[2] + 2, 0, 0);
        if (volta.getTime() < Date.now()) volta.setDate(volta.getDate() + 1);
        set("proximo", volta.getTime());
        atualizar(`limite do Gemini; volto às ${volta.toLocaleTimeString("pt-BR").slice(0, 5)}`);
        return;
      }
      if (!(await garantirModelo())) {
        set("proximo", Date.now() + 10 * 60_000);
        atualizar(`não consegui o ${MODELO} com raciocínio Médio; tento em 10 min`);
        return;
      }
      const pedido = fila[0];
      ed.focus();
      document.execCommand("selectAll");
      document.execCommand("insertText", false, PREFIXO + pedido);
      await w(1500);
      const b = document.querySelector("button.send-button, button[aria-label='Enviar mensagem']");
      if (!b) { atualizar("botão de enviar não apareceu"); await w(5000); location.href = "/app"; return; }
      set("atual", { nome: nomeDe(pedido), t: Date.now() });
      fila.shift(); set("fila", fila);
      b.click();
      for (const fim = Date.now() + 5 * 60_000; Date.now() < fim && !naConversa();) await w(500); // até 5 min
      enviando = false;
      return passo(); // já na conversa: espera a resposta
    } catch (e) {
      atualizar(String(e.message || e));
    } finally { enviando = false; }
  }

  // Vigia: se esta aba é a dona e nada foi salvo nem falhou em 35 min, recarrega a página (o mesmo que o dono do
  // projeto fazia à mão quando a aba "travava" com a mensagem parada).
  set("vivo", Date.now());
  setInterval(() => {
    if (!get("ativo", false) || get("dono", {}).id !== ABA || Date.now() < get("proximo", 0)) return;
    if (Date.now() - get("vivo", Date.now()) > 35 * 60_000) { set("vivo", Date.now()); location.href = "/app"; }
  }, 60_000);
  setTimeout(passo, 4000);
  setInterval(passo, 20_000);
  setInterval(atualizar, 30_000);
})();
