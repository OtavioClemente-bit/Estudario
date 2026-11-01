// Abertura do site: a turma do Folha em órbita ao redor do mascote. Só enfeita o #splash do
// index.html (que já aparece sem este arquivo); o app o dissolve quando os estudos ficam prontos.
// Depois do login (a conta Google escolhida), o app chama __showSplash e a mesma abertura volta.
(function () {
  window.__splashStart = Date.now();
  var first = document.getElementById("splash");
  var template = first ? first.outerHTML : null;
  function crew() {
    var orbit = document.getElementById("sp-orbit");
    if (!orbit || !window.estudarioGlyph) return;
    var names = ["home", "calendar_month", "gps_fixed", "bookmarks", "local_fire_department", "emoji_events", "timer", "lightbulb"];
    var n = names.length;
    names.forEach(function (name, i) {
      var svg = window.estudarioGlyph(name, false);
      if (!svg) return;
      var slot = document.createElement("span");
      slot.className = "sp-sat";
      slot.style.setProperty("--a", (360 / n * i) + "deg");
      slot.style.setProperty("--d", (0.25 + i * 0.09) + "s");
      slot.innerHTML = '<span class="sp-pop"><span class="sp-up">' + svg + "</span></span>";
      orbit.appendChild(slot);
    });
  }
  crew();
  window.__showSplash = function () {
    if (!template || document.getElementById("splash")) return;
    document.body.insertAdjacentHTML("afterbegin", template);
    window.__splashStart = Date.now();
    crew();
  };
})();
