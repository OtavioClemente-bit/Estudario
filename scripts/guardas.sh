#!/usr/bin/env bash
# Guardas de lançamento: rodar antes de todo envio para a Play e de todo deploy do site.
#
#   bash scripts/guardas.sh
#
# Roda os testes do plano de estudo (alunos simulados por 60 a 120 dias: teoria repetida, tarefa
# duplicada, dia acima das horas, questão antes da teoria, atrasadas, plano estável) e da sincronização
# (celular e site sem rede, juntando em ordens diferentes), mais todos os outros testes de shared, web e app.
# Sai com erro se alguma falhar. O relatório do plano fica em shared/build/plan-guard.txt.
set -u
cd "$(dirname "$0")/.."

if [ -d "${USERPROFILE:-$HOME}/.jdks/jbr-17.0.14" ]; then
  export JAVA_HOME="${USERPROFILE:-$HOME}/.jdks/jbr-17.0.14"
fi
GRADLE=./gradlew
[ -f ./gradlew.bat ] && case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) GRADLE=./gradlew.bat ;; esac

mkdir -p build
echo "Rodando as guardas (leva alguns minutos)..."
# clean<Tarefa> força os testes a rodarem de novo, mesmo sem mudança no código.
"$GRADLE" :shared:cleanJvmTest :web:cleanJsNodeTest :app:cleanTestDebugUnitTest \
  :shared:jvmTest :web:jsNodeTest :app:testDebugUnitTest -q --console=plain --continue -Pkotlin.compiler.execution.strategy=in-process > build/guardas.log 2>&1
status=$?

conta() { # pasta de resultados -> "testes falhas"
  local dir="$1" t=0 f=0 x
  [ -d "$dir" ] || { echo "0 0"; return; }
  for x in "$dir"/*.xml; do
    [ -f "$x" ] || continue
    t=$((t + $(sed -n 's/.*<testsuite[^>]* tests="\([0-9]*\)".*/\1/p' "$x" | head -1)))
    f=$((f + $(sed -n 's/.*<testsuite[^>]* failures="\([0-9]*\)".*/\1/p' "$x" | head -1) + $(sed -n 's/.*<testsuite[^>]* errors="\([0-9]*\)".*/\1/p' "$x" | head -1)))
  done
  echo "$t $f"
}

echo
echo "Resumo das guardas"
for par in "Regras e plano (shared):shared/build/test-results/jvmTest" "Site (web):web/build/test-results/jsNodeTest" "App Android (app):app/build/test-results/testDebugUnitTest"; do
  nome="${par%%:*}"; dir="${par#*:}"
  read -r t f < <(conta "$dir")
  if [ "$t" = "0" ]; then echo "  $nome: não rodou (veja build/guardas.log)"
  elif [ "$f" = "0" ]; then echo "  $nome: $t testes, todos passaram"
  else echo "  $nome: $t testes, $f FALHARAM"; grep -l "<failure" "$dir"/*.xml 2>/dev/null | sed 's/.*TEST-/      falhou: /; s/\.xml$//'
  fi
done
if [ -f shared/build/plan-guard.txt ]; then
  falhas=$(sed -n '/^falhas:/,$p' shared/build/plan-guard.txt | tail -n +2 | grep -c . || true)
  echo "  Alunos simulados (60 a 120 dias): $(grep -c '^=== ' shared/build/plan-guard.txt) perfis, $falhas com problema (relatório: shared/build/plan-guard.txt)"
fi
echo
if [ $status -eq 0 ]; then
  echo "OK: todas as guardas passaram. Pode publicar."
else
  echo "PARE: alguma guarda falhou. Não publique. Detalhes em build/guardas.log."
fi
exit $status
