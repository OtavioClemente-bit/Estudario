// Abertura do site: a turma do Folha em órbita ao redor do mascote. Só enfeita o #splash do
// index.html (que já aparece sem este arquivo); o app o dissolve quando os estudos ficam prontos.
(function () {
  window.__splashStart = Date.now();
  var orbit = document.getElementById("sp-orbit");
  if (!orbit || !window.estudarioGlyph) return;
  var crew = ["home", "calendar_month", "gps_fixed", "bookmarks", "local_fire_department", "emoji_events", "timer", "lightbulb"];
  var n = crew.length;
  crew.forEach(function (name, i) {
    var svg = window.estudarioGlyph(name, false);
    if (!svg) return;
    var slot = document.createElement("span");
    slot.className = "sp-sat";
    slot.style.setProperty("--a", (360 / n * i) + "deg");
    slot.style.setProperty("--d", (0.25 + i * 0.09) + "s");
    slot.innerHTML = '<span class="sp-pop"><span class="sp-up">' + svg + "</span></span>";
    orbit.appendChild(slot);
  });
})();
