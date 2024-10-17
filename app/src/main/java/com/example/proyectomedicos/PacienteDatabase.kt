package com.example.proyectomedicos

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(PacienteEntity::class, MedicinaEntity::class) , version = 1)
abstract class PacienteDatabase : RoomDatabase() {
    abstract fun pacienteDao(): PacienteDao
    abstract fun medicinaDao(): MedicinaDao
}
