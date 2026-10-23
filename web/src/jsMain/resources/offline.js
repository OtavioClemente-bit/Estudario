// A cópia dos dados da conta neste navegador (IndexedDB), para o site abrir sem internet. O que for
// feito sem rede fica marcado como "a enviar" e sobe sozinho quando a conexão volta.
(function () {
  "use strict";
  const DB = "estudario-offline", STORE = "snapshot";
  function open() {
    return new Promise((resolve, reject) => {
      const req = indexedDB.open(DB, 1);
      req.onupgradeneeded = () => req.result.createObjectStore(STORE);
      req.onsuccess = () => resolve(req.result);
      req.onerror = () => reject(req.error);
    });
  }
  window.estudarioCache = {
    // { userId, text, revision, dirty, savedAt } ou null
    get: function (userId) {
      return open().then((db) => new Promise((resolve) => {
        const req = db.transaction(STORE, "readonly").objectStore(STORE).get("current");
        req.onsuccess = () => { const v = req.result; resolve(v && v.userId === userId ? v : null); };
        req.onerror = () => resolve(null);
      })).catch(() => null);
    },
    set: function (value) {
      return open().then((db) => new Promise((resolve) => {
        const tx = db.transaction(STORE, "readwrite");
        tx.objectStore(STORE).put(value, "current");
        tx.oncomplete = () => resolve(true);
        tx.onerror = () => resolve(false);
      })).catch(() => false);
    },
    clear: function () {
      return open().then((db) => new Promise((resolve) => {
        const tx = db.transaction(STORE, "readwrite");
        tx.objectStore(STORE).delete("current");
        tx.oncomplete = () => resolve(true);
        tx.onerror = () => resolve(false);
      })).catch(() => false);
    },
  };
  if ("serviceWorker" in navigator && location.protocol === "https:") {
    window.addEventListener("load", () => navigator.serviceWorker.register("sw.js").catch(() => null));
  }
})();
