package br.com.estudario.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SimulationDao {
    @Query("SELECT * FROM simulations ORDER BY createdAt DESC") fun simulations(): Flow<List<SimulationEntity>>
    @Query("SELECT * FROM simulations WHERE id = :id") suspend fun simulation(id: Long): SimulationEntity?
    @Query("SELECT * FROM simulations WHERE id = :id") fun simulationFlow(id: Long): Flow<SimulationEntity?>
    @Query("SELECT * FROM simulations WHERE status IN ('GENERATING', 'PARTIAL')") suspend fun generating(): List<SimulationEntity>
    @Query("SELECT id FROM simulations WHERE status != 'FINISHED'") suspend fun openIds(): List<Long>
    @Query("SELECT * FROM simulations ORDER BY createdAt DESC") suspend fun simulationsOnce(): List<SimulationEntity>
    @Insert suspend fun insertSimulation(value: SimulationEntity): Long
    @Update suspend fun updateSimulation(value: SimulationEntity)
    @Query("DELETE FROM simulations WHERE id = :id") suspend fun deleteSimulation(id: Long)

    @Query("SELECT * FROM exam_profiles") fun examProfiles(): Flow<List<ExamProfileEntity>>
    @Query("SELECT * FROM exam_profiles WHERE competitionId = :competitionId") suspend fun examProfile(competitionId: Long): ExamProfileEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun saveExamProfile(value: ExamProfileEntity)

    @Transaction @Query("SELECT * FROM questions WHERE simulationId = :simulationId ORDER BY id") suspend fun questionsFor(simulationId: Long): List<QuestionWithOptions>
    @Transaction @Query("SELECT * FROM questions WHERE simulationId = :simulationId ORDER BY id") fun questionsForFlow(simulationId: Long): Flow<List<QuestionWithOptions>>
    @Query("DELETE FROM questions WHERE simulationId = :simulationId") suspend fun deleteQuestionsFor(simulationId: Long)
}
