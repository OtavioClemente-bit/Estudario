package br.com.estudario.data.local

// Enums do modelo de dados, comuns ao app Android e ao app web.
enum class TopicStatus { NAO_ESTUDADO, EM_ESTUDO, ESTUDADO, REVISANDO, DOMINADO }
enum class Priority { BAIXA, NORMAL, ALTA }
enum class Difficulty { FACIL, MEDIA, DIFICIL }
enum class SummaryKind { COMPLETO, RAPIDO }
enum class SnippetKind { BIZU, PEGADINHA, RECUPERACAO }
enum class ReviewDifficulty { FACIL, NORMAL, DIFICIL }
enum class ErrorStatus { NOVO, REVISANDO, CORRIGIDO, RECORRENTE }
enum class QuestionSessionType { QUICK, TOPIC, SUBJECT, SMART, ERROR_REVIEW, SIMULATION, DAILY_CHALLENGE, REVIEW }
enum class QueueEventType { ADICIONADO, CONCLUIDO, ADIADO, PAUSADO, RETOMADO }
enum class ContentOriginType { EDITAL, DIDACTIC_SUBDIVISION, AUXILIARY_CONTENT }
enum class QuestionSourceType { REAL, REAL_ADAPTED, AUTHORIAL }
enum class SourceKind { OFICIAL, COMPLEMENTAR }
enum class RemoteSyllabusSyncOperation { UPSERT, DELETE }
enum class RemoteSyllabusSyncState { PENDING, SYNCED, FAILED }
