package com.example.proyectomedicos

interface OnClickListenerMedicina {

    fun onClick(medicinaEntity: MedicinaEntity)
    fun onFavoriteMedicina(medicinaEntity: MedicinaEntity)
    fun onDeleteMedicina(medicinaEntity: MedicinaEntity)
    fun getMedicinas(): MutableList<MedicinaEntity>
}
