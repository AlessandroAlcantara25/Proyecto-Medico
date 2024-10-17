package com.example.proyectomedicos

import android.app.Application
import androidx.room.Room

class PacienteApplication : Application() {

    companion object {
        lateinit var database: PacienteDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this,
            PacienteDatabase::class.java,
            "PacienteDatabase")
            .build()
    }
}