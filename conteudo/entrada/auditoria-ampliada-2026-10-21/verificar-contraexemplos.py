"""Confere evidências matemáticas e SQL da auditoria, sem alterar matérias."""
import itertools
import json
from pathlib import Path
import sqlite3


def analise(atributos, dependencias):
    universo = set(atributos)
    conjuntos = [set(c) for n in range(len(atributos) + 1)
                 for c in itertools.combinations(atributos, n)]

    def fecho(x):
        atual = set(x)
        while True:
            antes = set(atual)
            for esquerda, direita in dependencias:
                if set(esquerda) <= atual:
                    atual.update(direita)
            if atual == antes:
                return atual

    chaves = []
    for c in conjuntos:
        if fecho(c) == universo and not any(k <= c for k in chaves):
            chaves.append(c)
    primos = set().union(*chaves)
    parciais = [
        {"candidata": sorted(k), "parte": sorted(p), "atributo": a}
        for k in chaves for p in conjuntos if p < k
        for a in fecho(p) - primos
    ]
    violacoes3 = [
        {"determinante": sorted(x), "dependente": a}
        for x in conjuntos if fecho(x) != universo
        for a in fecho(x) - x - primos
    ]
    violacoes_bcnf = [
        {"determinante": sorted(x), "dependente": a}
        for x in conjuntos if fecho(x) != universo
        for a in fecho(x) - x
    ]
    return {"chaves": [sorted(k) for k in chaves],
            "em2FN": not parciais, "em3FN": not violacoes3,
            "emFNBC": not violacoes_bcnf, "dependenciasParciais": parciais,
            "violacoes3FN": violacoes3}


q78 = analise("ABCD", [("A", "BCD"), ("BC", "A"), ("B", "D")])
assert q78["chaves"] == [["A"], ["B", "C"]]
assert not q78["em2FN"]
q90 = analise("ABCD", [("AB", "CD"), ("B", "C")])
assert q90["chaves"] == [["A", "B"]]
assert not q90["em2FN"] and not q90["em3FN"]
q91 = analise("ABC", [("A", "BC")])
assert q91["emFNBC"] and q91["chaves"] == [["A"]]
# AB -> C é não trivial e seu determinante AB é superchave, sem ser candidato mínimo.
db = sqlite3.connect(":memory:")
q104 = db.execute("SELECT 4 NOT IN (4, NULL), 9 NOT IN (4, NULL)").fetchone()
assert q104 == (0, None)
db.execute("CREATE TABLE exemplo (valor INTEGER DEFAULT 0)")
db.execute("INSERT INTO exemplo(valor) VALUES (-1)")
q93 = db.execute("SELECT valor FROM exemplo").fetchone()[0]
assert q93 == -1
out = {"q78": q78, "q90": q90, "q91": q91,
       "q104": {"4_NOT_IN": q104[0], "9_NOT_IN": q104[1]},
       "q93_valorExplicitoAceitoComDefault": q93,
       "runtimeSQL": "SQLite em memoria: evidencia de logica NULL e DEFAULT, nao teste de PostgreSQL. Confrontado com documentacao oficial PostgreSQL 18 arquivada em fontes/."}
Path(__file__).with_name("contraexemplos.json").write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n")
print(json.dumps(out, ensure_ascii=False, indent=2))
