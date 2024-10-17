package com.example.proyectomedicos

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "MedicinaEntity")
data class MedicinaEntity(@PrimaryKey(autoGenerate = true) var id:Long= 0,
                          var name:String,
                          var namedoc: String="",
                          var telefono: String="",
                          var fecha: String="",
                          var email: String="",
                          var tipoMedi: String="",
                          var nomMedicina: String="",
                          var isfav: Boolean=false)
