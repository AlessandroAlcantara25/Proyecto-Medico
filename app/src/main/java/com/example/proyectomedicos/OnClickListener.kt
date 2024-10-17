package com.example.proyectomedicos

interface OnClickListener {


    fun onClick(pacienteEntity: PacienteEntity)
    fun onFavoritePaciente(pacienteEntity: PacienteEntity)
    fun onDeletePaciente(pacienteEntity: PacienteEntity)
    fun getPacientes(): MutableList<PacienteEntity>
}