import json,glob,re,unicodedata,collections,csv,math
from pathlib import Path
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
ROOT=Path('/workspace/Estudario'); OUT=Path(__file__).parent

def norm(s):
 s=unicodedata.normalize('NFKD',s).encode('ascii','ignore').decode().lower()
 return re.sub(r'\s+',' ',re.sub(r'[^a-z0-9]+',' ',s)).strip()

def dump(n,x): (OUT/n).write_text(json.dumps(x,ensure_ascii=False,indent=2))
mats={}
for p in sorted((ROOT/'conteudo/materias').glob('*/*.json')):
 d=json.loads(p.read_text());mats[d['id']]=d
live={x[0]:x[1:] for x in json.loads((OUT/'catalogo-banco.json').read_text())}
groups={};linked=collections.defaultdict(set);diff=[]; matched=0;missing=0
for p in sorted((ROOT/'conteudo/editais').glob('*.json')):
 if p.stem not in live:continue
 d=json.loads(p.read_text()); topics=[t for s in d['disciplinas'] for t in s['topicos']]
 local=[len(topics),sum(t.get('topico') is not None for t in topics)]
 if local!=live[p.stem]:diff.append({'edital':p.stem,'local_total_ligados':local,'banco_total_ligados':live[p.stem]})
 else:matched+=1
 for s in d['disciplinas']:
  for t in s['topicos']:
   key=(norm(s['nome']),norm(t['texto']))
   if t.get('topico') is not None:
    linked[key].add(t['topico']);continue
   missing+=1
   g=groups.setdefault(key,{'disciplina':s['nome'],'texto':t['texto'],'editais':set(),'ocorrencias':0,'banco_contagem_compativel':True})
   g['editais'].add(p.stem);g['ocorrencias']+=1
   if local!=live[p.stem]:g['banco_contagem_compativel']=False
ids=list(mats);aliases=collections.defaultdict(set);docs=[]
for id in ids:
 m=mats[id]
 for a in [m['title']]+m.get('aliases',[]):aliases[norm(a)].add(id)
 scope=m.get('scope',{});scope=scope if isinstance(scope,dict) else {}
 docs.append(' '.join([m['title']]*3+[m['subject'],scope.get('covers','')]+[c['title'] for c in m.get('chapters',[])]+m.get('aliases',[])))
vec=TfidfVectorizer(strip_accents='unicode',lowercase=True,ngram_range=(1,2),min_df=1,sublinear_tf=True)
D=vec.fit_transform(docs);gs=list(groups.items());rows=[]
for start in range(0,len(gs),300):
 batch=gs[start:start+300]; Q=vec.transform([g['texto'] for k,g in batch]); scores=linear_kernel(Q,D)
 for (key,g),scoresrow in zip(batch,scores):
  evidence=sorted(linked.get(key,set()));exact=sorted(aliases.get(key[1],set())); candidates=list(dict.fromkeys(evidence+exact))
  best=scoresrow.argsort()[-3:][::-1]
  sug=[{'id':ids[i],'titulo':mats[ids[i]]['title'],'similaridade_lexical':round(float(scoresrow[i]),4)} for i in best if scoresrow[i]>0]
  cat='revisar_vinculo_existente' if evidence else ('revisar_alias_exato' if exact else ('avaliar_complementacao_ou_composicao' if scoresrow[best[0]]>=0.24 else 'investigar_lacuna'))
  g.update({'editais':sorted(g['editais']),'quantidade_editais':len(g['editais']),'categoria':cat,'materias_evidencia':candidates,'evidencia_mesma_disciplina':evidence,'candidatos_lexicais':sug})
  rows.append(g)
rows.sort(key=lambda g:(-g['quantidade_editais'],-g['ocorrencias'],g['disciplina'],g['texto']))
dump('grupos.json',rows);dump('divergencias-banco-repositorio.json',diff)
by=collections.defaultdict(lambda:collections.Counter())
for g in rows:
 b=by[g['disciplina']];b['grupos']+=1;b['ocorrencias']+=g['ocorrencias'];b[g['categoria']]+=g['ocorrencias']
summary={'editais_publicados_banco':len(live),'editais_contagem_compativel':matched,'editais_divergentes':len(diff),'topicos_sem_vinculo_banco':sum(t-r for t,r in live.values()),'topicos_sem_vinculo_repositorio_publicavel':missing,'grupos_normalizados':len(rows),'categorias':dict(collections.Counter(g['categoria'] for g in rows)),'ocorrencias_por_categoria':dict(sum((collections.Counter({g['categoria']:g['ocorrencias']}) for g in rows),collections.Counter())),'disciplinas':dict(sorted(by.items(),key=lambda x:-x[1]['ocorrencias']))}
dump('resumo.json',summary)
with (OUT/'prioridades.csv').open('w') as f:
 w=csv.writer(f);w.writerow(['disciplina','topico','editais','ocorrencias','triagem','materias_com_evidencia','candidatos_lexicais','contagem_compativel_banco','exemplos_editais'])
 for g in rows:w.writerow([g['disciplina'],g['texto'],g['quantidade_editais'],g['ocorrencias'],g['categoria'],' | '.join(g['materias_evidencia']),' | '.join(x['id'] for x in g['candidatos_lexicais']),g['banco_contagem_compativel'],' | '.join(g['editais'][:5])])
print(json.dumps({k:v for k,v in summary.items() if k!='disciplinas'},ensure_ascii=False,indent=2))
print('DISCIPLINAS',list(summary['disciplinas'].items())[:20])
for i,g in enumerate(rows[:35]):print(i,g['quantidade_editais'],g['disciplina'],g['texto'][:160],g['categoria'],g['materias_evidencia'],[c['id'] for c in g['candidatos_lexicais']])
