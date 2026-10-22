import json
from pathlib import Path
P=Path(__file__).parent
queue=json.loads((P.parent/'fila-50-lacunas-2026-10-21/fila-detalhada.json').read_text())['fila']
new={3:'03-servidores-df',4:'04-saude-pessoa-deficiencia',6:'06-financiamento-saude',8:'08-pccr-geral-boa-vista',9:'09-pccr-saude-boa-vista',11:'11-plano-mulheres-df',12:'12-magisterio-df',13:'13-gestao-educacional-df',14:'14-plano-distrital-educacao',15:'15-regimento-escolar-df',16:'16-resolucao-cedf-2020',17:'17-equidade-trabalho-sus',18:'18-carreira-ale-ro',20:'20-atencao-especializada',22:'22-licitacoes-ale-ro',25:'25-ministerio-publico-agenda-2030',30:'30-etica-ministerio-publico',31:'31-proeja',35:'35-etica-belo-horizonte',36:'36-lgpd-belo-horizonte',41:'41-regulacao-educacao-superior',45:'45-servidores-roraima',46:'46-processo-administrativo-roraima',49:'49-carreira-alece'}
new.update({23:'23-carreira-mpes',24:'24-lei-organica-mpes',27:'27-privacidade-mpes',28:'28-governanca-mpes',33:'33-emendas-roraima',34:'34-estrutura-alerr',40:'40-licitacoes-alerr',44:'44-fiplan-roraima'})
new.update({10:'10-curriculo-movimento-df',19:'19-sus-desastres',21:'21-processo-administrativo-ro',26:'26-planejamento-mpes',32:'32-etica-alerr',37:'37-codigos-politicas-casan',39:'39-fundo-saneamento-sc'})
new.update({1:'01-ji-parana',5:'05-historia-campina-grande',50:'50-historia-geografia-chapada'})
new.update({48:'48-saude-belo-horizonte'})
new.update({29:'29-estrutura-mpes'})
new.update({38:'38-conesan-sc'})
new.update({47:'47-estatuto-ji-parana'})
new.update({2:'02-legislacao-chapada'})
new.update({7:'07-santa-luzia',43:'43-cee-al-resolucao-70'})
old={}
partial={2:'Íntegra da LC 62/2014 não lida; legislação alteradora e consolidação das quatro normas pendentes.',7:'Íntegra legível e consolidada da Lei 4.055/2019 pendente. Versão baseada em dispositivos reproduzidos em atos oficiais e aplicações específicas.',43:'Somente disposições iniciais acessíveis e art. 191 na redação de 2025. Restante da resolução-base, anexos e art. 214 pendentes.'}
rows=[]
for n,r in enumerate(queue,1):
 item={'rank':n,'materia':r['materia'],'ocorrencias_potenciais':r['ocorrencias_sem_vinculo'],'estado':'a_iniciar','questoes_escritas':0,'publicado':False}
 if n in new:
  d=new[n];item.update(estado='versao_editorial_com_50_questoes_para_revisao',questoes_escritas=50,teoria=d+'/TEORIA.md',questoes=d+'/QUESTOES.md',fonte=('Reprodução de documento institucional ou normativo em fonte secundária; cotejo integral com a base oficial permanece pendente; alterações oficiais identificadas no texto quando acessadas.' if n in (10,21,23,24,26,27,28,29,38,39) else 'Documento oficial acessado pela busca web; limites de revisão indicados no texto.'))
  if n in partial:
   item.update(estado='versao_editorial_parcial_com_50_questoes',pendencia_bloqueante=partial[n]+' Não é cobertura completa da matéria.')
 elif n in old:
  a,b=old[n];item.update(estado='rascunho_anterior_parcial_requer_ampliacao_e_fontes',questoes_escritas=50,teoria='../cinco-materias-2026-10-21/'+a,questoes='../cinco-materias-2026-10-21/'+b,fonte='Conferência documental específica ainda pendente.')
 if n == 42:
  item.update(estado='bloqueada_por_falta_das_normas_finais',pesquisa='42-cee-al-resolucoes-2014/PENDENCIA-DOCUMENTAL.md',pendencia_bloqueante='Textos finais das Resoluções 6 e 7/2014 do CEE/AL não obtidos; pauta e bibliografia divergem quanto à numeração.')
 rows.append(item)
(P/'controle.json').write_text(json.dumps({'escopo_autorizado':{'materias':50,'questoes_minimas_por_materia':50,'questoes_alvo':2500},'versoes_editoriais_novas':len(new),'versoes_expressamente_parciais':len(partial),'rascunhos_parciais_anteriores':len(old),'a_iniciar':0,'bloqueadas_sem_versao':1,'questoes_novas_nesta_producao':50*len(new),'questoes_anteriores_preservadas_fora_da_contagem':50,'materias_publicadas':0,'meta_concluida':False,'fila':rows},ensure_ascii=False,indent=2))
md=['# Produção das 50 matérias — controle de andamento','',
'Pedido autorizado: as 50 propostas do ranking, com situações concretas, siglas explicadas, teoria e pelo menos 50 questões comentadas por matéria. Sem agentes extras, sem scripts para gerar questões e sem publicação.','',
'## Estado real','',
f'- **{len(new)} versões editoriais novas nesta produção**, com teoria e 50 questões comentadas cada: {50*len(new)} questões.',
f'- **{len(partial)} dessas versões são expressamente parciais:** ranks 2 (Chapada, LC 62/2014), 7 (Santa Luzia) e 43 (CEE/AL, Resolução 70/2022). Versão escrita não significa cobertura integral ou revisão concluída.',
'- O rascunho geral anterior de Santa Luzia permanece preservado fora desta contagem; foi substituído no controle pela nova versão parcial, evitando duplicidade de questões.',
f'- **{50-len(new)-len(old)} proposta sem versão**, bloqueada por falta dos textos normativos: rank 42. [Pesquisa e evidências](42-cee-al-resolucoes-2014/PENDENCIA-DOCUMENTAL.md). O conjunto das 50 matérias e das 2.500 questões não está concluído.',
'- Nenhuma matéria publicada ou pronta para importar em JSON. Não foi reduzida nenhuma pendência do catálogo.',
'',
'## Versões produzidas','',
'| Rank | Matéria | Teoria | Questões |','|---:|---|---|---|']
for r in rows:
 if r['rank'] in new:md.append(f"| {r['rank']} | {r['materia']} | [Ler]({r['teoria']}) | [50 questões]({r['questoes']}) |")
md+=['','## Controle integral','',
'| Rank | Matéria | Estado |','|---:|---|---|']
for r in rows:
 st=('Versão parcial; fonte integral pendente' if r['rank'] in partial else 'Versão escrita, revisão pendente') if r['rank'] in new else ('Rascunho anterior parcial' if r['rank'] in old else 'Bloqueada: textos normativos finais não obtidos')
 md.append(f"| {r['rank']} | {r['materia']} | {st} |")
md+=['','## Fontes e decisões','',
'A busca web tornou-se disponível nesta etapa e conseguiu abrir documentos oficiais que antes estavam inacessíveis pelo terminal. O acesso pelo terminal e pelo navegador de busca tem resultados diferentes; não é correto continuar dizendo que toda fonte oficial está indisponível. Acesso ao original não prova que todas as alterações e regulamentos foram revisados.','',
'- LC 840: consultado texto compilado no SINJ; identificadas revogações dos arts. 12 e 13 anotadas no portal. A matéria cita os dispositivos trabalhados e não afirma cobrir cada artigo da lei.',
'- PROEJA: lidos os 12 artigos no Planalto, incluindo a anotação de revogação do art. 10. Marcos de 2006/2007 foram tratados como históricos.',
'- II PDPM: lidos decreto e anexo no SINJ. A matéria preserva o recorte 2020–2023 e não o anuncia como plano operacional atual.',
'- Pessoa com deficiência: lidas seções da publicação original disponibilizada pelo Ministério da Saúde; revisão das portarias consolidadas e de tabelas não foi concluída.',
'- Atenção especializada: lidas diretrizes, dimensões e dispositivos de organização do cuidado na publicação do Ministério da Saúde; não se declara revisão integral das normas posteriores de financiamento.',
'- Magistério do DF: consultada Lei 5.105 compilada. Explicadas a mudança do percentual de coordenação de 20 horas, a extinção de GAPED/GASE prevista para 2026 e a diferença entre regência e jornada total.',
'- Gestão educacional do DF: consultadas Leis 5.106 e 7.142. Conciliadas as denominações anteriores e posteriores e destacadas as mudanças no art. 1º, os efeitos temporais da GIC, a extinção da GATA e a alteração de recessos.',
'- Equidade no SUS: lida cópia governamental das sete páginas da Portaria GM/MS 230/2023 e anexo. As metas de 2023 foram preservadas como históricas e diretrizes de acolhimento não foram convertidas em benefícios individuais automáticos.',
'- Ética do MP: lidos os 40 artigos da Resolução CNMP 261/2023 na publicação reproduzida pelo MPM. Destinatários membros, competências e limites de comunicação separados de regras gerais para servidores.',
'- Ética de Belo Horizonte: lido PDF municipal atualizado, com as alterações anotadas de 2012/2016. Destacadas a revogação da antiga exceção de presentes, a diferença Conselho/comissões e a distinção entre sugerir exoneração e exonerar.',
'- LGPD de Belo Horizonte: lidos os 18 artigos da publicação original. Composição e competências locais, compartilhamento e documentação trabalhados; revisão das normas complementares posteriores continua pendente.',
'- PDE: consultada Lei 5.499/2015 compilada, com as 21 metas e estratégias selecionadas. Anotada a declaração de inconstitucionalidade do art. 3º e do parágrafo único do art. 9º; não se afirma revisão integral do Anexo II ou confirmação de plano sucessor.',
'- Carreira da ALECE: consultadas a Lei 17.091 compilada e as alterações 19.683/2026 e 19.769/2026. Atualizadas regras de interstício e efeitos, penalidades, cômputo condicionado do estágio e quantitativos legais, sem confundi-los com vagas de concurso.',
'- Boa Vista, plano geral: consultada republicação oficial da Lei 2.474/2023 e alterações 2.708/2025 e 2.834/2026. Separados os eixos da carreira, hipóteses de reabilitação e novo corte da VPNI; não se declara consolidação de todos os decretos e anexos.',
'- Boa Vista, saúde: consultada Lei 2.527/2024 e pontos das Leis 2.746/2025 e 2.834/2026. Diferenciada a coincidência de interstícios em relação ao plano geral; GQ calculada sobre vencimento inicial e GASAMU atualizada no ponto conferido. Tabelas completas e decretos permanecem pendentes.',
'- Licitações na ALE/RO: consultada compilação oficial da Resolução 593/2024 com alteração 677/2026. Trabalhados planejamento, instrução, seleção, registro e execução; os anexos extensos e a compatibilização integral com normas gerais seguem pendentes, incluindo a condição textual de adesão municipal.',
'- Processo administrativo de Roraima: consultado texto da Lei 418/2004 no TJRR e alterações das Leis 1.385/2020 e 2.050/2024 sobre suspensão de prazos e prioridade. Diferenciados recurso e revisão, anulação, revogação e convalidação; não se declara revisão integral de normas especiais posteriores.',
'- Educação superior: consultado Decreto 9.235 compilado no Planalto, com pontos alterados pelo Decreto 12.456/2025. Explicados processo único, formatos de oferta, atos institucionais e de cursos, autonomia, reconhecimento e supervisão; revisão infradecretal e de transição permanece pendente.',
'- MP e Agenda 2030: consultadas páginas dos 17 objetivos na ONU e arts. 127/129 da Constituição. Diferenciado ODS 18 nacional dos 17 globais; acesso integral ao pacto e ao PGA local falhou, sem inventar cláusulas ou resultados. Exemplos e indicadores didáticos identificados como fictícios.',
'- CEDF 2/2020: confirmada revogação pela Resolução 2/2023 no SINJ e na relação oficial da SEEDF. Preservado o recorte histórico pedido na fila, com fundamentos, avaliação, documentação e pontos de regulação; comparação integral com o ato sucessor e seções específicas continuam pendentes.',
'- Servidores de Roraima: conferidos capítulos da LC 53 em publicações do TJRR e alterações de 2025. Distinguidos posse/exercício, formas de retorno, indenizações e regras novas de licença classista; prazos textualmente diferentes da licença compensatória foram sinalizados sem unificação inventada. Previdência, rito e jurisprudência integral permanecem pendentes.',
'- Carreira da ALE/RO: consultada LC 731 compilada e publicação da LC 1.323/2026, cujo art. 1º fixa marco de julho de 2026. Incorporados oito referências, novos interstícios, bases distintas de adicionais, revogação da docência e auxílios específicos. Tabelas completas, transições individuais e resoluções permanecem pendentes.',
'- Financiamento da saúde: substituído no controle o rascunho parcial por nova versão com 50 questões. Lidos os 48 artigos da LC 141 compilada, piso federal na Constituição, LC 234/2026 e resultado da ADI 5.897. Incluídas alterações de hospitais universitários e bombeiros e IBS nas bases, sem afirmar consolidação de toda a transição ou regulamentação. O rascunho anterior fica preservado como histórico e não é contado novamente.',
'- Chapada: encontrada fonte municipal que identifica a LC 62/2014 com o plano de carreira dos procuradores. A interpretação do conjunto de quatro normas da fila exige confirmar textos e escopo, evitando escrever uma matéria genérica apresentada como lei específica.',
'- Ji-Paraná: a busca localizou histórico do IBGE e outras fontes, mas a leitura direta do PDF histórico retornou erro; não foi usada para declarar o rascunho anterior integralmente validado.',
'',
'## Próxima sequência','',
'Regimento Escolar do DF: incluída versão com 50 questões e atenção à revogação pontual da Portaria 301/2020. Governança do MPES: incluída versão com 50 questões a partir da reprodução da Portaria 8.565/2017 com alterações de 2018/2020; a íntegra oficial retornou bloqueio e o cotejo permanece pendente.','',
'Privacidade do MPES: incluída versão com 50 questões, com duas reproduções da Portaria 897/2023 e cotejo de dispositivos da LGPD no Planalto. Indicadas ressalvas de finalidade acadêmica, oposição, conservação e normas complementares locais; original oficial continua pendente.','',
'Carreira do MPES: incluída versão com 50 questões a partir da reprodução compilada da Lei 7.233/2002 e pontos da Lei 11.849/2023 no banco da Unale. Diferenciados jornada, plantão, 25 níveis, promoção, estágio e antigas regras revogadas; cotejo oficial integral, anexos, regulamentação e alterações posteriores pendentes.','',
'Emendas de Roraima: lidos os 19 artigos da LC 373/2026 no TJRR e no DOE 5.116 de 4/3/2026. Incluídas 50 questões sobre publicidade, contas específicas, coletivas, transferências especiais, impedimentos e execução; regulamentação e compatibilização judicial integral permanecem pendentes.','',
'Licitações da ALERR: incluída versão documental com 50 questões sobre a Resolução 001/2024 e pontos dos anexos na edição oficial 4.142. A Resolução 004/2025 e demais complementos não foram consolidados; o material não anuncia o texto-base como compilação atual integral.','',
'Lei Orgânica do MPES: incluída versão com 50 questões sobre estruturas e atribuições selecionadas do Título I, em reprodução compilada com alterações até 2023. Destacados sete eleitos no Conselho Superior, voz sem voto do Diretor-Geral e mudanças de periodicidade; capítulos auxiliares completos, íntegra oficial, alterações e jurisprudência pendentes.','',
'FIPLAN: incluída versão com 50 questões sobre integração, registros, acessos e transparência, baseada no Decreto federal 10.540 compilado e aplicação local no Decreto 40.319-E/2026. Manual local utilizado apenas na distinção pontual das unidades; não houve teste operacional nem auditoria de conformidade. Normas e manuais locais completos permanecem pendentes.','',
'Estrutura da ALERR: incluída versão com 50 questões sobre estrutura geral e unidades selecionadas da Resolução 015/2024. Lido o ato promulgado desde a página 64 do Diário 4.324, distinguindo-o do projeto publicado no mesmo documento. Todos os artigos, tabelas, organogramas e alterações posteriores permanecem pendentes de consolidação.','',
'Atualização: incluídas versões dos ranks 1, 5, 10, 19, 21, 26, 29, 32, 37, 38, 39, 47, 48 e 50. Os limites documentais específicos constam em cada teoria; as notas antigas acima registram etapas anteriores, não o estado atual desses itens. O rank 2 também tem nova versão, expressamente parcial.',
'Ji-Paraná e Campina Grande: incorporadas fontes municipais, legislação de criação e fontes institucionais; divergências de datas e espécies normativas foram mantidas como ressalvas. Chapada, história e geografia: distinguidos proposta de geoparque, parque nacional e município, sem anunciar reconhecimento não confirmado.',
'Currículo do DF e planejamento do MPES: usadas reproduções documentais, com limites de cotejo oficial. SUS e desastres: leitura seletiva da obra, sem declarar exame integral. Rondônia: incorporadas alterações oficiais de 2022/2024, inclusive contagem em dias úteis, sem transplantar prazos federais.',
'Estrutura do MPES: recorte histórico da Resolução 12/2012, com alterações identificadas; não apresentado como organograma consolidado de 2026. Ética da ALERR: ressalvadas incompatibilidades constitucionais posteriores do texto antigo. CASAN: utilizados código de 2026 e políticas selecionadas, com divergências internas sinalizadas.',
'CONESAN e Fundo de Saneamento de SC: textos reproduzidos em fonte secundária, com atualizações específicas quando acessíveis e ressalva de órgãos extintos. Estatuto de Ji-Paraná: texto original de 2005 e alterações selecionadas, incluindo Lei 3.812/2026; não é compilação completa. Saúde de Belo Horizonte: leitura seletiva do PMS 2026–2029 e cartilha, distinguindo metas, dados históricos e regras dos serviços.',
'Legislação de Chapada: identificados os objetos das quatro normas e lidas as Leis 1.447/2011 e 1.570/2014; LC 97/2023 trabalhada somente nos dispositivos acessíveis; LC 62/2014 identificada no catálogo, sem leitura integral. Mantido estado parcial.',
'Santa Luzia: incluída nova versão parcial com 50 questões, baseada nos arts. 2º, 11, 44 a 47 e pontos de imparcialidade reproduzidos em atos oficiais. Diferenciadas regras da lei e aplicações em portaria de concurso e decreto ambiental; delegação histórica de 2024 não anunciada como vigente. Rascunho geral anterior preservado, excluído da contagem para evitar duplicidade. Íntegra da lei continua pendente.',
'CEE/AL, Resolução 70/2022: incluída versão parcial com 50 questões sobre disposições iniciais acessíveis e art. 191 alterado pela Resolução 13/2025, lida integralmente no DOE de 29/7/2025. Distinguidos três critérios cumulativos, possibilidade de dispensa, modalidade presencial e renovação. Cabeçalho inconsistente de 2025 registrado. Restante da norma-base e art. 214 pendentes.',
'Único rank sem versão: 42. A pauta de 2014 diverge da bibliografia institucional quanto à numeração das Resoluções 6 e 7/2014 do CEE/AL; não foi usada como substituta das normas aprovadas. O visualizador antigo não forneceu os textos e o catálogo secundário apresentou apenas trecho inicial. Pesquisa e evidências salvas no diretório 42-cee-al-resolucoes-2014. Não foram inventadas 50 questões para encerrar a meta numericamente.',
'',
'## Verificação e limites','',
f'Cada versão foi escrita com 50 itens e respectivos gabaritos, numerados Q01 a Q50. Total previsto nos arquivos desta produção: {50*len(new)} itens. Os enunciados e comentários foram escritos individualmente. A contagem não substitui revisão factual, pedagógica, jurídica ou cobertura integral do edital. Todas as alterações estão em `conteudo/entrada/`. Sem commit, push, agentes adicionais ou chamada de API externa de geração.']
(P/'ANDAMENTO.md').write_text('\n'.join(md)+'\n')
print(f'Controle: 50 propostas; {len(new)} versões editoriais novas, sendo {len(partial)} expressamente parciais; 1 bloqueada sem versão; {50*len(new)} questões. Meta incompleta.')
