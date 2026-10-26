// Worker clássico: o bundle reconhece WorkerGlobalScope antes de inicializar o app visual.
// A query mantém worker e motor na mesma versão da página que pediu a previsão.
importScripts(new URL("estudario.js" + self.location.search, self.location.href).href);
