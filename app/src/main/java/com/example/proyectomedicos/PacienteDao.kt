package com.example.proyectomedicos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PacienteDao {

    @Query("SELECT * FROM PacienteEntity")
    fun getPaciente(): MutableList<PacienteEntity>

    @Query("SELECT * FROM PacienteEntity WHERE name LIKE :searchQuery")
    fun searchPacientesByName(searchQuery: String): List<PacienteEntity>

    @Query("SELECT * FROM PacienteEntity WHERE id = :id")
    fun getPacienteById(id: Long): PacienteEntity?

    @Insert
    fun addPaciente(pacienteEntity: PacienteEntity)

    @Update
    fun updatePaciente(pacienteEntity: PacienteEntity)

    @Delete
    fun deletePaciente(pacienteEntity: PacienteEntity)
}