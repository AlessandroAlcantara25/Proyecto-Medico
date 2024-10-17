package com.example.proyectomedicos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PacienteEntity")
data class PacienteEntity(@PrimaryKey(autoGenerate = true) var id:Long= 0,
                          var name:String,
                          var telefono: String="",
                          var dni: String="",
                          var email: String="",
                          var sexo: String="",
                          var peso: String="",
                          var talla: String="",
                          var direccion: String="",
                          var isfav: Boolean=false)
