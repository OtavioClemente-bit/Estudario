Nota do aluno: 8/10

ERROS DE CONTEÚDO (corrija todos):
- Cita RFC 9846 (julho de 2026) como substituta da RFC 8446; não consegui confirmar o número e a data, é preciso conferir

O QUE FALTA (acrescente na teoria, dentro dos capítulos existentes):
- Modos de cifra ECB/CBC/CFB/OFB/CTR; cifra de bloco x fluxo
- Algoritmos DES, RC4/RC5/RC6, Blowfish, Twofish, IDEA, ECC, SHA-3, WHIRLPOOL
- VPN: PPTP, L2TP, ISAKMP, OpenVPN, VPN SSL
- e-CPF/e-CNPJ, EV/SAN/wildcard, PIN/PUK, tipos ICP além de A1/A3, ACT
- Instalação de certificados, CSR, OpenSSL, code signing
- Salt/bcrypt, KMS, TDE, envelope digital, gestão de segredos
- WAF/NGFW/EDR, DLP

ITENS DE EDITAL QUE A MATÉRIA COBRE SÓ EM PARTE:
- "Certificação digital: conceitos fundamentais de certificado digital de pessoa física e jurídica; identificação de validade e outros atributos de um certificado digital; ICP-Brasil, autoridades certificadora e de registro; token e outras mídias de certificado digital; conceitos, uso e cuidado de PIN e PUK; assinatura digital.": falta Não explica PIN e PUK (bloqueio/desbloqueio do token), nem e-CPF/e-CNPJ (certificado de pessoa física x jurídica) e como ver os atributos de um certificado no sistema.
- "Assinatura digital e tecnologias de autenticação (gov.br).": falta Não trata da conta gov.br (níveis bronze/prata/ouro) nem da assinatura eletrônica avançada pelo gov.br.
- "Modos de operação de cifras.": falta Só diz que o modo importa e cita GCM; não explica ECB, CBC, CFB, OFB e CTR.
- "Modos de operação de cifradores. Funções digestoras (hashes).": falta Faltam os modos ECB, CBC, CFB, OFB e CTR.
- "Criptografia: algoritmos simétricos e assimétricos, hashes, cifras de bloco e de fluxo e modos ECB, CBC, CBF, OBF e CTR.": falta Não diferencia cifra de bloco e de fluxo nem explica os modos ECB/CBC/CFB/OFB/CTR.
- "Criptografia simétrica e assimétrica, algoritmos de hash, criptografia de bloco e de fluxo.": falta Falta cifra de fluxo x cifra de bloco (RC4, ChaCha20).
- "Algoritmos RSA, DES, AES e RC4, RC5, RC6, MD5, SHA-1, SHA-256, SHA-512.": falta DES (chave de 56 bits), RC4, RC5, RC6 e SHA-512 não são descritos.
- "Criptografia... Algoritmos RSA, AES, ECC, IDEA, Twofish, Blowfish, 3DES e RC4. Protocolo Diffie–Hellman. Hashes MD5, SHA-1, SHA-2, SHA-3, colisões. Técnicas: força bruta, criptoanálise, canal lateral, ataques de texto conhecido/escolhido, MITM.": falta Faltam IDEA, Twofish, Blowfish, RC4, ECC em detalhe, SHA-3, ataque de canal lateral e de texto escolhido, e os modos de cifra.
- "Algoritmos RSA, AES, ECC, IDEA, Twofish, Blowfish, 3DES e RC4.": falta IDEA, Twofish, Blowfish, RC4 e ECC não são caracterizados (tamanho de bloco/chave, tipo de cifra).
- "Algoritmos AES, 3DES, Blowfish, IDEA, RC4, RC6, Diffie-Hellman, RSA, ECC, MD4, MD5, SHA-1, SHA-2 e WHIRLPOOL.": falta Faltam Blowfish, IDEA, RC4, RC6, ECC, MD4 e WHIRLPOOL.
- "Protocolos criptográficos e algoritmos RSA, DES e AES.": falta O DES não é caracterizado (bloco de 64 bits, chave efetiva de 56 bits, quebra por força bruta).
- "Algoritmos RSA, DES, AES, MD5 e SHA-1.": falta Faltam as características do DES.
- "Criptografia: sistemas simétricos e assimétricos, certificação digital, algoritmos RSA, Diffie-Hellman, AES e RC4, hashes criptográficos e colisões.": falta RC4 (cifra de fluxo, hoje proibida no TLS) não é tratado.
- "Dados criptografados: ... discos virtuais criptografados; detecção; ... recuperação direta; pré-computado; força bruta; dicionário probabilístico; híbrido ...": falta Faltam a visão pericial: detectar dados cifrados, ataques de dicionário, tabelas pré-computadas (rainbow), ataques híbridos e recuperação de chaves; RC4.
- "Segurança da Informação e Criptografia: ... (DES, AES, cifras de fluxo), teoria dos números, controle de acesso, autenticação e segurança em aplicações Web.": falta Faltam teoria dos números (aritmética modular, primos para o RSA), DES, cifras de fluxo e segurança de aplicações web.
- "Conceitos e aplicações de VPN nas modalidades: Host-To-Gateway, Gateway-To-Gateway e protocolos IPSec (ISAKMP, AH, ESP), PPTP, L2TP.": falta Não trata de PPTP, L2TP nem ISAKMP, nem das modalidades host-to-gateway e gateway-to-gateway pelo nome (nenhuma outra matéria do catálogo cobre PPTP/L2TP).
- "VPN Host-to-Gateway e Gateway-to-Gateway e protocolos IPSec (ISAKMP, AH, ESP), PPTP e L2TP.": falta Faltam PPTP, L2TP e ISAKMP.
- "VPN: encapsulamento, autenticação, criptografia, topologias, PPTP, L2TP, IPSEC e OpenVPN.": falta Faltam PPTP, L2TP e OpenVPN.
- "VPN e VPN-SSL.": falta Não há VPN SSL/TLS (VPN por navegador ou OpenVPN), só IPsec.
- "Garantindo o sigilo e a autenticidade de um e-mail através de criptografia PGP, chaves públicas e privadas.": falta O PGP só é citado; falta mostrar na prática como cifrar e assinar um e-mail (quais chaves usar) e o envelope digital.
- "Criptografia PGP: chaves públicas e privadas; consulta e envio de chaves públicas a um servidor de chaves.": falta Não trata dos servidores de chaves PGP (publicar e consultar chaves), da impressão digital da chave nem da revogação.
- "... Certificados Digitais para pessoas físicas, jurídicas e servidores de rede (e-CPF, e-CNPJ, SSL, EV SSL, SAN e WILDCARD).": falta Faltam e-CPF, e-CNPJ, certificados DV/OV/EV, SAN e wildcard.
- "Certificados digitais para pessoas físicas, jurídicas e servidores de rede: e-CPF, e-CNPJ, SSL, EV SSL, SAN e wildcard.": falta Faltam e-CPF, e-CNPJ, EV, SAN e wildcard.
- "Certificado digital: tipos de certificados digitais; padrões X.509 e ICP-Brasil.": falta Só trata de A1 e A3; faltam os demais tipos da ICP-Brasil (S, T, A4 etc.) e os campos/extensões da v3 do X.509.
- "Certificado digital: tipos de certificados digitais; padrões X.509 e ICPBrasil.": falta Faltam os tipos de certificado além de A1/A3 e as extensões do X.509 v3.
- "ACT; principais informações de um certificado digital (tipos de certificados, prazo de validade, certificado autoassinado).": falta Não explica a ACT (Autoridade de Carimbo do Tempo) como entidade da ICP-Brasil; o carimbo do tempo só aparece como conceito.
- "Segurança Defensiva: ICP-Brasil: Autoridade Certificadora, Autoridade de Carimbo do Tempo, Organização, Normas e Padrões.": falta Faltam a ACT, o Comitê Gestor e as normas (DOC-ICP, políticas de certificado).
- "Certificados digitais: solicitação, instalação, configuração e revogação.": falta Falta o passo a passo: CSR, instalação no navegador ou servidor e configuração; só a revogação é coberta.
- "Configuração de certificados digitais autoassinados e de autoridade certificadora.": falta Não mostra como gerar um certificado autoassinado ou uma AC própria (CSR, OpenSSL).
- "Conceitos básicos de segurança da informação: instalação de certificados digitais, criptografias, assinaturas.": falta Falta a instalação de certificados.
- "Hardening e criptografia: endurecimento de Windows Server e Linux; gerenciamento de certificados SSL/TLS; PKI; criptografia (simétrica, assimétrica, hash) e assinatura digital.": falta Falta o hardening de Windows Server e Linux e a gestão prática de certificados TLS (renovação, CSR).
- "OpenSSL": falta O OpenSSL não é citado (comandos para gerar chaves, CSR e certificados).
- "OpenSSL; criptografia: conceitos básicos e aplicações, protocolos criptográficos, criptografia simétrica e assimétrica, principais algoritmos; assinatura e certificação digital.": falta O OpenSSL não é tratado.
- "Criptografia: hash, salt, TLS/SSL.": falta Não explica salt nem o armazenamento de senhas com hash.
- "... hashes (SHA, bcrypt, HMAC).": falta Faltam bcrypt e salt (hash lento para senhas); o ECC só é citado.
- "Vulnerabilidades de codesigning.": falta Não trata de assinatura de código (code signing) nem de suas falhas (chave roubada, certificado revogado, assinatura de malware).
- "Vulnerabilidades de code signing.": falta Code signing não é tratado.
- "... Resumo de Mensagem, Cifragem de Dados, Assinatura Digital, Envelope Digital, Certificado Digital (ICP-Brasil), Autenticação Multifator (MFA), Single Sign-On e Técnicas de Redundância e Tolerância a Falhas.": falta Faltam envelope digital, Single Sign-On e redundância/tolerância a falhas.
- "Segurança Defensiva: ... Envelope Digital, Certificado Digital, Autenticação Multifator, Carimbo do Tempo e Técnicas de Redundância e Tolerância a Falhas.": falta Faltam envelope digital e redundância/tolerância a falhas.
- "Segurança para dados inativos e dados em trânsito (Transparent Data Encryption, criptografia em nível de objeto, Always Encrypted).": falta Não trata de TDE, Always Encrypted nem da cifragem em nível de objeto do SGBD.
