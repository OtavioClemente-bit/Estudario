Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Informática: Linux: conceitos, diretórios, permissões e comandos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
O que é o Linux (kernel, GNU/Linux, software livre e licença GPL), características (multiusuário, multitarefa, diferencia maiúsculas de minúsculas), distribuições e famílias (Debian/Ubuntu, Red Hat/Fedora), ambientes gráficos e shell; estrutura de diretórios (/, /bin, /etc, /home, /root, /tmp, /var, /usr, /dev, /proc, /boot, /mnt e /media), caminhos absolutos e relativos, arquivos ocultos; comandos básicos (pwd, ls, cd, mkdir, rmdir, rm, cp, mv, touch, cat, less, head, tail, grep, find, man, ln), redirecionamento e pipe; usuários e grupos, root e sudo, /etc/passwd e /etc/shadow, useradd, passwd, chown; permissões rwx, notação octal, chmod e umask; pacotes .deb e .rpm, apt, dnf/yum e rpm; processos com ps, top e kill.

Fica de fora (outras matérias tratam): Shell script e programação Bash, administração de servidores e serviços de rede (Samba, LDAP, DNS, Apache), sistemas de arquivos e LVM em profundidade, systemd em detalhe, hardening e segurança avançada, Windows e pacotes de escritório.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Noções de sistema operacional (ambientes Linux e Windows).
- Sistema operacional e ambiente Linux.
- Software livre, código aberto, licenças, projetos, modelos de negócio e padrões abertos.
- Conhecimentos de sistemas operacionais Linux.
- Sistemas operacionais Windows 10 (32-64 bits) e ambiente Linux (SUSE SLES 15 SP2).
- Sistemas de arquivos e ambientes Windows 10, Linux SUSE SLES 15 SP2 e IBM z/OS.
- Linux e Ubuntu Linux.
- Fundamentos, operação e configuração de Sistemas Operacionais: Linux.
- Fundamentos, operação e configuração de Sistemas Operacionais: Linux RedHat.
- Sistemas operacionais Windows e conceitos básicos de Linux e software livre.
- Noções de administração de sistemas operacionais: z/OS, Linux, MS-Windows.
- Conceitos e configurações básicas de Linux (Sistema de arquivos EXT4, BTRFS e XFS, Conceitos de LVM, Gerenciamento de processos).
- Noções de Linux: estrutura de diretórios, comandos básicos, permissões de arquivos, gerenciamento de usuários.
- Sistema Operacional Linux.
- Características do sistema operacional Linux.
- Gerenciamento de usuários e permissões de acesso no Linux.
- Shell e comandos.
- Sistemas operacionais Windows e Linux: conceitos básicos.
- Linux: fundamentos, instalação, comandos básicos e administração.
- Linux e Ubuntu Linux 6.06.
- Ambiente Linux (RedHat Enterprise Linux).
- Linux: pacotes RPM e DEB, systemd e scripts Bash e Python.
- Noções básicas de UNIX, Linux, Windows XP e Windows.
- Sistemas operacionais: Ambiente Linux (CentOS, Red Hat e Oracle Linux).
- Ambiente Linux (CentOS, Red Hat e Oracle Linux): Utilitários e comandos‐padrão.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Seção sobre software livre e código aberto: as quatro liberdades, licenças (GPL, LGPL, BSD, MIT, Apache), copyleft, modelos de negócio, padrões abertos e projetos, com 10 questões
- Capítulo de operação e configuração do Linux (serviços e systemd básico, configuração de rede, discos e particionamento, montagem, logs, agendamento com cron) com 12 questões
- Capítulo de operação e configuração do Linux (serviços e systemd básico, configuração de rede, discos e particionamento, montagem, logs, agendamento com cron) com 12 questões, específico de Red Hat
- Seção sobre sistemas de arquivos (ext4, Btrfs, XFS: journaling, limites), conceitos de LVM (PV, VG, LV) e gerenciamento de processos (prioridade, nice, jobs), com 10 questões
- Seção sobre instalação do Linux (particionamento, boot, gerenciador GRUB) e administração básica (usuários, serviços, atualização), com 10 questões
- Seção sobre o Red Hat Enterprise Linux: subscription, yum/dnf e repositórios, SELinux, firewalld e serviços, com 10 questões
- Seção sobre systemd (unidades, systemctl, targets, journalctl) e scripts em Bash e Python, com 10 questões
- Seção sobre CentOS, Red Hat e Oracle Linux: diferenças entre as distribuições da família Red Hat, gerenciador de pacotes, SELinux e ciclo de suporte, com 8 questões

## Regras de qualidade (as mais importantes)
1. **Regra precisa, não vaga.** Dê a regra exata com exemplos concretos do tipo que cai (frases, casos, contas), as exceções e os casos de dúvida real que a prova explora. Quando houver norma oficial (ex.: Acordo Ortográfico, VOLP, Manual de Redação da Presidência), cite-a com precisão. Nunca invente regra.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa → exemplo concreto do tipo que cai → exceção que a prova usa → como reconhecer. Tabelas para comparar institutos ou casos parecidos. O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. Enunciados com caso concreto, como nas provas.
4. **Tamanho das alternativas:** a certa NÃO pode ser a mais longa nem a mais curta em mais de 10 das 25 questões de múltipla escolha. Escreva as cinco alternativas com tamanho parecido; a certa nunca é uma frase seca de 3 palavras ao lado de distratores longos.
5. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto ou conceito vizinho trocado, número, prazo, autor ou súmula trocados, regra certa aplicada ao caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria: absurdo ("toda remoção é impossível", "depende de autorização judicial prévia"), de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos ("sempre", "nunca", "exclusivamente") só para marcar o errado.
6. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo ou a regra, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
7. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a prova escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; **de 4 a 6** "conceitos que geram erro" (chaves e1 a e6); fontes reais que você abriu.
8. Texto neutro: não escreva o nome de nenhuma banca, órgão ou cargo, nem a palavra "banca" (use "a prova").

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "informatica.linux",
  "subject": "Informática",
  "title": "Linux: conceitos, diretórios, permissões e comandos",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Linux: conceitos, diretórios, permissões e comandos"],
  "scope": { "covers": "o que a matéria cobre", "excludes": "o que fica de fora" },
  "theoryTitle": "título completo da teoria",
  "chapters": [ { "title": "1. ...", "markdown": "texto em Markdown (## e ###, listas, **negrito**, tabelas | a | b |, alertas com >)" } ],
  "summary": "resumo em Markdown",
  "flashcards": [ { "front": "pergunta curta", "back": "resposta curta" } ],
  "tips": ["dica 1"],
  "traps": ["pegadinha 1"],
  "activeRecall": [ { "question": "pergunta", "answer": "resposta" } ],
  "errorConcepts": [ { "key": "e1", "title": "nome do erro", "summary": "explicação corretiva" } ],
  "questions": [
    {
      "statement": "enunciado (pergunta ou comando)",
      "format": "MULTIPLE_CHOICE",
      "difficulty": "FACIL",
      "options": [
        { "key": "A", "text": "...", "correct": false },
        { "key": "B", "text": "...", "correct": true },
        { "key": "C", "text": "...", "correct": false },
        { "key": "D", "text": "...", "correct": false },
        { "key": "E", "text": "...", "correct": false }
      ],
      "explanation": "Gabarito B. ...",
      "section": "título EXATO de um dos capítulos",
      "errorConceptKey": "e1",
      "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null
    },
    {
      "statement": "afirmação para julgar",
      "format": "TRUE_FALSE",
      "difficulty": "MEDIA",
      "options": [ { "key": "C", "text": "Certo", "correct": false }, { "key": "E", "text": "Errado", "correct": true } ],
      "explanation": "Errado. ...",
      "section": "título EXATO de um dos capítulos",
      "errorConceptKey": null,
      "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null
    }
  ],
  "sources": [ { "kind": "OFICIAL", "title": "...", "publisher": "...", "reference": "...", "url": "https://...", "accessedAt": "AAAA-MM-DD" } ]
}
```
- difficulty: "FACIL", "MEDIA" ou "DIFICIL". format: "MULTIPLE_CHOICE" ou "TRUE_FALSE". kind das fontes: "OFICIAL" ou "COMPLEMENTAR".
- IMPORTANTE: escreva cada explicação de forma própria, à mão. Proibido usar frases-molde repetidas entre questões (ex.: "não descreve o critério aplicável", "A análise deve distinguir..."). Em múltipla escolha, explique por que CADA alternativa errada está errada com o conceito específico dela. Não estique alternativas com enfeites como ", segundo o caso apresentado".
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome informatica.linux.json.
