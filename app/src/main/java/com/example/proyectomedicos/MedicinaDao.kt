package com.example.proyectomedicos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MedicinaDao {

    @Query("SELECT * FROM MedicinaEntity")
    fun getMedicinas(): MutableList<MedicinaEntity>

    @Query("SELECT * FROM MedicinaEntity WHERE name LIKE :searchQuery")
    fun searchMedicinasByName(searchQuery: String): List<MedicinaEntity>

    @Query("SELECT * FROM MedicinaEntity WHERE id = :id")
    fun getMedicinaById(id: Long): MedicinaEntity?

    @Insert
    fun addMedicina(medicinaEntity: MedicinaEntity)

    @Update
    fun updateMedicina(medicinaEntity: MedicinaEntity)

    @Delete
    fun deleteMedicina(medicinaEntity: MedicinaEntity)
}