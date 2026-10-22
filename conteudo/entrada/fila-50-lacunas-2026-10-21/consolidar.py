import json,csv,glob
from pathlib import Path
P=Path(__file__).parent;ROOT=P.parents[2]
G=json.loads((ROOT/'conteudo/entrada/mapa-lacunas-2026-10-21/grupos.json').read_text())
S=json.loads((P/'selecao.json').read_text())
assert len(S)==50
seen=set(); rows=[]; editais=set()
for s in S:
 assert not (set(s['grupos'])&seen);seen.update(s['grupos'])
 groups=[G[i] for i in s['grupos']];es=set(e for g in groups for e in g['editais']);editais.update(es)
 rows.append({**s,'ocorrencias_sem_vinculo':sum(g['ocorrencias'] for g in groups),'grupos_normalizados':len(groups),'editais_distintos':len(es),'origens':[{'disciplina':g['disciplina'],'texto':g['texto'],'ocorrencias':g['ocorrencias'],'editais':g['editais'],'contagem_compativel_banco':g['banco_contagem_compativel']} for g in groups]})
rows.sort(key=lambda x:-x['ocorrencias_sem_vinculo'])
local_total=0;local_null=0;catalogs=0
for file in (ROOT/'conteudo/editais').glob('*.json'):
 d=json.loads(file.read_text())
 if d.get('catalogo') is False:continue
 catalogs+=1
 for subject in d.get('disciplinas',[]):
  for topic in subject.get('topicos',[]):
   local_total+=1;local_null+=topic.get('topico') is None
n=sum(r['ocorrencias_sem_vinculo'] for r in rows)
summary={'base':'repositório local; editais catalogo != false','editais_base':catalogs,'topicos_base':local_total,'sem_vinculo_base':local_null,'materias_propostas':len(rows),'grupos_distintos_selecionados':len(seen),'ocorrencias_selecionadas_sem_sobreposicao':n,'editais_distintos_com_ocorrencia':len(editais),'potencial_condicional_reducao_pendentes_percent':round(n/local_null*100,2),'potencial_condicional_queda_cobertura_pontos_percentuais':round(n/local_total*100,2),'sem_vinculo_restante_se_tudo_coberto':local_null-n,'percent_sem_vinculo_atual':round(local_null/local_total*100,2),'percent_sem_vinculo_condicional':round((local_null-n)/local_total*100,2),'cuidado':'Cenário condicionado à cobertura integral de cada tópico selecionado, conferência das fontes e publicação dos vínculos. Não é previsão nem contagem atual de produção.'}
(P/'fila-detalhada.json').write_text(json.dumps({'resumo':summary,'fila':rows},ensure_ascii=False,indent=2))
with (P/'fila.csv').open('w') as f:
 w=csv.writer(f);w.writerow(['prioridade','materia_proposta','ocorrencias_sem_vinculo','editais_distintos','grupos_agrupados','edital_exemplo'])
 for i,r in enumerate(rows,1):w.writerow([i,r['materia'],r['ocorrencias_sem_vinculo'],r['editais_distintos'],r['grupos_normalizados'],r['origens'][0]['editais'][0]])
md=['# Fila de 50 matérias baseada nos tópicos sem vínculo','',
'Esta fila substitui a lista genérica sugerida anteriormente. Cada proposta está associada a tópicos efetivamente sem vínculo nos arquivos locais de editais. Foram excluídos assuntos gerais já disponíveis, casos claros de composição e fragmentos bibliográficos sem conteúdo próprio. Isso não equivale a declarar que toda cobertura específica já foi validada.','',
'## Base e impacto condicional','',
f"Base local: {catalogs:,} editais publicáveis, {local_total:,} tópicos e {local_null:,} ocorrências sem vínculo.",
f"As 50 propostas agrupam {len(seen)} grupos normalizados e {n:,} ocorrências sem sobreposição entre as propostas, presentes em {len(editais):,} editais distintos.",
f"Se cada proposta cobrir integralmente seus tópicos e os vínculos forem aprovados/publicados, a redução seria de {n:,} ocorrências, ou {n/local_null*100:.2f}% dos pendentes locais. Restariam {local_null-n:,}. A proporção sem vínculo passaria de {local_null/local_total*100:.2f}% para {(local_null-n)/local_total*100:.2f}%, queda de {n/local_total*100:.2f} pontos percentuais.",
'',
'Esse é um cenário condicionado, não uma garantia de resultado. As contagens do banco consultado antes eram 48.022 sem vínculo; a base local tem 48.212. Não aplicar o numerador local diretamente ao banco como se fossem os mesmos registros. Há 342 editais com contagens divergentes documentados no mapa anterior.','',
'## Fila ordenada pela quantidade de ocorrências selecionadas','',
'Cada ocorrência é um tópico em um edital; o mesmo assunto repetido em vários editais conta várias vezes. A coluna não mede alunos, inscritos ou demanda real.','',
'| Ordem | Matéria proposta | Ocorrências sem vínculo | Editais distintos |','|---:|---|---:|---:|']
for i,r in enumerate(rows,1):md.append(f"| {i} | {r['materia']} | {r['ocorrencias_sem_vinculo']} | {r['editais_distintos']} |")
md+=['','## Conferência antes de produzir','',
'- Confirmar no conteúdo completo das matérias existentes se há cobertura específica reaproveitável. A triagem consultou títulos, escopos e aliases, sem leitura integral de todos os capítulos.',
'- Exemplo de descarte após conferência: a Lei 12.772/2012 aparece sem vínculo em editais, mas já tem capítulo em `legislacao.servico-publico-federal-normas`; por isso ficou fora das 50 novas propostas.',
'- A LC 840/2011 aparece na matéria da Polícia Civil do DF apenas em contexto específico; isso não demonstra cobertura do regime geral exigido na educação. Confirmar a extensão antes de redigir o módulo.',
'- Na LC 141/2012, a matéria de SUS exclui expressamente parte do financiamento; existe justificativa de escopo para o módulo específico.',
'- Números e descrições de normas foram herdados dos editais. Confirmar ente, ementa, redação exigida e vigência. Não inventar o objeto de leis identificadas apenas por número.',
'- O texto do tópico associado à Lei 3.830/2016 da ALE/RO mistura descrições; precisa de conciliação com o edital e a norma antes de fixar o título.',
'- O Plano Distrital de Educação é citado para 2015–2024; preservar a exigência do edital e conferir se ele cobra versão histórica, prorrogação ou atualização.',
'- Em itens como CASAN, Agenda 2030 do Ministério Público e SUS em desastres, obter exatamente os códigos ou documentos bibliográficos exigidos; um texto genérico não demonstra atendimento.',
'- Os rascunhos de Campina Grande, Ji-Paraná, LC 141 e Santa Luzia já escritos nesta conversa não foram publicados nem considerados cobertura concluída.',
'',
'## Padrão de produção','',
'Situação concreta → conceito explicado → siglas e termos esclarecidos → exemplos → pelo menos 50 questões comentadas. Quantidade de questões não substitui cobertura do escopo e revisão factual.','',
'## Rastreabilidade','',
'`fila-detalhada.json` contém os textos originais, disciplinas, editais de origem e contagens de cada proposta. `fila.csv` permite ordenar e acompanhar a produção. `selecao.json` registra a seleção editorial; `consolidar.py` apenas consolida contagens e relatórios, sem gerar conteúdo ou questões. Nenhum vínculo ou arquivo canônico foi alterado.']
(P/'FILA-50-MATERIAS.md').write_text('\n'.join(md)+'\n')
print(json.dumps(summary,ensure_ascii=False,indent=2))
for i,r in enumerate(rows,1):print(i,r['ocorrencias_sem_vinculo'],r['materia'])
