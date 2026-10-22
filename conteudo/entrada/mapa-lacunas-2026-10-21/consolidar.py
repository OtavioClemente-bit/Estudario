import json,csv
from pathlib import Path
P=Path(__file__).parent; groups=json.loads((P/'grupos.json').read_text()); summary=json.loads((P/'resumo.json').read_text())
# Revisão editorial: decisões baseadas nos escopos e capítulos lidos, sem publicar vínculos.
review=[
(0,'compor_materias_existentes',['portugues.coordenacao','portugues.subordinacao','portugues.conjuncoes','portugues.pronomes-relativos-funcoes'],'O enunciado combina coordenação, subordinação e conectivos; os escopos de coordenação e subordinação são complementares. Não resolver com uma única matéria parcial.'),
(3,'compor_materias_existentes',['informatica.microsoft-word-2016','informatica.microsoft-excel-2016','informatica.microsoft-powerpoint-2016'],'Há materiais para os três aplicativos. Conferir a versão exigida no edital: os escopos são da edição 2016 e excluem recursos exclusivos posteriores.'),
(8,'compor_materias_existentes',['portugues.classes-de-palavras','portugues.formacao-de-palavras'],'O tópico agrega classes e formação; usar cobertura conjunta, sem criar outra matéria geral duplicada.'),
(31,'compor_materias_existentes',['portugues.classes-de-palavras','portugues.formacao-de-palavras','portugues.vozes-verbais'],'Inclui formação, flexões e vozes verbais; reaproveitar os módulos, conferindo a abrangência e abordagem dos autores exigidos.'),
(1,'avaliar_complementacao',['informatica.windows-10-11','informatica.arquivos-digitais-backup'],'Windows cobre utilitários, mas o tópico também exige chat, clientes de e-mail e multimídia. Revisar as seções específicas antes de considerar cobertura suficiente.'),
(5,'avaliar_complementacao',['arquivologia.gestao-documentos','informatica.arquivos-digitais-backup'],'Gestão documental cobre temporalidade e autenticidade; backup cobre retenção. A rastreabilidade de comunicações institucionais precisa de correspondência explícita e contextual.'),
(7,'avaliar_complementacao',['saude-publica.sus-legislacao'],'O capítulo aborda Resolução CNS 453/2012 e composição dos conselhos. Não demonstra sozinho cobertura integral da resolução; revisar diretrizes, funcionamento e demais exigências antes de vincular como completo.'),
(6,'criar_modulo_especifico',['saude-publica.sus-legislacao','direito-constitucional.ordem-social'],'O escopo de SUS exclui expressamente percentuais e blocos de financiamento da LC 141/2012; Ordem Social apenas menciona mínimos. Criar módulo específico de financiamento/LC 141, após conferir texto oficial vigente.'),
(4,'criar_modulo_local',[],'Não foi encontrado módulo municipal de Campina Grande nos títulos, escopos e busca textual. História geral e materiais do Rio Grande do Norte não atendem ao recorte municipal.'),
(14,'criar_modulo_local',['rondonia.geografia-historia'],'Rondônia fornece contexto estadual e menciona Ji-Paraná; exclui catálogo exaustivo municipal. Agrupar os 12 tópicos municipais de Ji-Paraná num projeto editorial próprio, com fontes locais.'),
(30,'criar_modulo_norma_local',[],'Não foi identificado módulo da LC 4.055/2019 de Santa Luzia; normas de outros municípios/estados não substituem. Conferir município, diploma e vigência antes de produzir.'),
(27,'avaliar_modulo_especifico',['radiologia.tecnicas-radiologicas-radioprotecao','radiologia.tc-rm-mamografia-nuclear'],'Radioproteção clínica não demonstra cobertura institucional e regulatória de ANSN/CNEN e segurança operacional nuclear. Delimitar escopo próprio e conferir atos oficiais vigentes.'),
(28,'reaproveitar_com_conferencia',['administracao-geral.projetos-qualidade','administracao-publica.gestao-processos'],'Escopo de projetos/qualidade inclui autores, ferramentas, PDCA e processos. Verificar se o edital pede normas industriais específicas: certificação ISO detalhada está excluída.'),
(29,'compor_materias_existentes',['direito-constitucional.principios-fundamentais','direito-constitucional.direitos-fundamentais','direito-constitucional.organizacao-estado','direito-constitucional.administracao-publica'],'O tópico enumera blocos diferentes da Constituição. Montar cobertura por artigos; não vincular toda a faixa a princípios fundamentais.'),
]
# Apenas ids realmente existentes são apresentados como materiais.
ROOT=P.parents[2]; existing=set()
for f in (ROOT/'conteudo/materias').glob('*/*.json'):existing.add(json.loads(f.read_text())['id'])
result=[]
for idx,decision,ids,note in review:
 g=groups[idx]
 result.append({'disciplina':g['disciplina'],'texto':g['texto'],'quantidade_editais':g['quantidade_editais'],'decisao':decision,'materias': [i for i in ids if i in existing],'justificativa':note,'editais':g['editais'],'contagem_compativel_banco':g['banco_contagem_compativel']})
(P/'prioridades-revisadas.json').write_text(json.dumps(result,ensure_ascii=False,indent=2))
with (P/'prioridades-revisadas.csv').open('w') as f:
 w=csv.writer(f);w.writerow(['disciplina','topico','editais','acao','materias','justificativa'])
 for r in result:w.writerow([r['disciplina'],r['texto'],r['quantidade_editais'],r['decisao'],' | '.join(r['materias']),r['justificativa']])
md=['# Mapa de cobertura das matérias — 21/10/2026','',
'## Resultado e base de comparação','',
'Inventário dos tópicos sem vínculo nos editais publicáveis, com agrupamento e sugestões de materiais. Nenhum conteúdo ou vínculo foi alterado. Análise executada sem agentes adicionais e sem chamadas a modelos externos.','',
'- Banco consultado: 4.048 editais publicados, 466.466 tópicos e 48.022 sem vínculo segundo os contadores do catálogo.',
'- Repositório correspondente: 48.212 ocorrências sem vínculo, agrupadas em **24.884 pares de disciplina + tópico normalizados**. Esta é a base detalhada do CSV; não são 24.884 matérias novas.',
'- Em **342 editais**, as contagens locais diferem das contagens do banco; 3.706 têm contagens compatíveis. Igualdade de contagens não prova identidade dos textos ou vínculos.',
'- O catálogo publicado guarda textos e contador de cobertura, mas não o id do material de cada tópico. A classificação por tópico usa os arquivos locais; não é uma reconstituição individual dos vínculos em produção.',
'- Prioridade significa quantidade de editais com a ocorrência, não procura dos usuários nem número de candidatos. Editais de cargos da mesma instituição podem repetir a mesma prova.',
'',
'## Primeiras decisões editoriais revisadas','',
'As 14 entradas abaixo foram revistas quanto ao escopo. Complementação e criação são propostas editoriais: dependem de delimitação e fontes, especialmente legislação local. Não se aplicam automaticamente a todos os demais grupos.','',
'| Tópico | Editais na base local | Ação recomendada |','|---|---:|---|']
for r in result:md.append('| '+r['texto'].replace('|','/').replace('\n',' ')[:150]+' | '+str(r['quantidade_editais'])+' | '+r['decisao'].replace('_',' ')+' |')
md+=['','### Fundamentação das decisões','']
for r in result:md+=['**'+r['disciplina']+' — '+r['texto']+'**','',r['justificativa'],'','Materiais a conferir: '+(', '.join('`'+i+'`' for i in r['materias']) or 'nenhum módulo específico identificado')+'.','']
md+=['## Repetição municipal: exemplo de como agrupar o trabalho','',
'Ji-Paraná possui 12 tópicos sem vínculo repetidos em 56 editais: **672 ocorrências**, mas apenas um conjunto municipal a planejar. Recomenda-se delimitar história, geografia e cultura local, possivelmente em mais de um módulo conforme a extensão. Campina Grande aparece em 65 editais com um tópico geral de história municipal. As contagens não devem ser somadas como editais únicos entre projetos.','',
'## Concentração por nome de disciplina','',
'Os nomes abaixo são os rótulos dos editais, preservados para rastreabilidade; não equivalem às 179 disciplinas canônicas da biblioteca. “Conhecimentos Específicos” reúne profissões distintas e precisa ser separado por cargo antes de criar módulos.','',
'| Disciplina do edital | Ocorrências sem vínculo | Grupos normalizados |','|---|---:|---:|']
for name,v in list(summary['disciplinas'].items())[:25]:md.append(f"| {name} | {v['ocorrencias']} | {v['grupos']} |")
md+=['','## Como interpretar a triagem automática','',
'O CSV completo usa reaproveitamento de vínculos locais, aliases exatos e similaridade lexical TF-IDF de títulos, escopos, capítulos e aliases. As pontuações são apenas ordenação de candidatos; não medem cobertura e não determinam criação. O limiar 0,24 é heurístico, não calibrado. Não há revisão humana de todos os 24.884 grupos.','',
'- 76 grupos têm texto/disciplina já associados a material em outro edital; 7 têm alias exato. Ambos exigem revisão de contexto.',
'- 2.313 grupos têm candidatos lexicais com pontuação a partir do limiar; a cobertura pode exigir composição ou complementação.',
'- 22.488 grupos seguem para investigação. **Isso não confirma ausência de matéria.** A busca textual falha com sinônimos, leis por número e tópicos compostos.',
'- Aliases genéricos podem produzir associações erradas: “Hidrografia” e “Relevo” de Ji-Paraná encontraram matérias de Minas Gerais. Não reaproveitar esses vínculos.',
'- Até a evidência de outro edital pode estar incorreta: “Classificação.” em Direito Civil apareceu ligado a ato administrativo. Tópicos curtos e rótulos genéricos precisam do pai, contexto e cargo.',
'- Normalização elimina acentos, pontuação e diferenças de caixa; não une tópicos semanticamente semelhantes com palavras diferentes. O texto original e os editais ficam preservados.',
'',
'## Ordem de execução recomendada','',
'1. Conciliar os 342 editais divergentes com a publicação e confirmar os vínculos efetivos antes de editar.',
'2. Revisar os grupos de maior recorrência que dependem de composição de matérias existentes; conferir suporte a múltiplos materiais no resolvedor.',
'3. Preparar os módulos locais e normativos delimitados neste relatório, começando pelos que atendem mais editais.',
'4. Separar “Conhecimentos Específicos” por profissão/cargo e revisar os demais grupos por disciplina. Criar material somente após checar escopos e capítulos candidatos.',
'',
'## Arquivos','',
'- `prioridades-revisadas.csv` / `.json`: 14 decisões com justificativa e materiais candidatos.',
'- `prioridades.csv` / `grupos.json`: mapa completo com recorrência, exemplos de editais e candidatos; triagem não aprovada para publicação.',
'- `resumo.json`: distribuição completa por disciplina e categoria.',
'- `divergencias-banco-repositorio.json`: 342 diferenças para conciliação.',
'- `catalogo-banco.json`: fotografia dos ids e contagens do catálogo consultado.',
'- `mapear.py` / `consolidar.py`: reprodução da análise, sem alterar arquivos canônicos.']
(P/'MAPA-MATERIAS.md').write_text('\n'.join(md)+'\n')
print('revisados',len(result),'materias_existentes',len(existing),'editais_unicos_prioridades',len(set(e for r in result for e in r['editais'])))
