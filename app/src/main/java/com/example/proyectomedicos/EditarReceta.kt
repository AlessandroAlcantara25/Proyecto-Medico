package com.example.proyectomedicos

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomedicos.databinding.EditRecetaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditarReceta : AppCompatActivity() {
    private lateinit var mBinding: EditRecetaBinding
    private var medicinaId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = EditRecetaBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupBottomNav()

        val genderArray = resources.getStringArray(R.array.opciones_medicina)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.spinnerMedicina.adapter = adapter

        mBinding.btnBuscar.setOnClickListener {
            val query = mBinding.etBuscar.text.toString()
            if (query.isNotEmpty()) {
                searchMedicinaByName(query)
            } else {
                Toast.makeText(this, "Ingrese un nombre para buscar", Toast.LENGTH_SHORT).show()
            }
        }

        mBinding.btnActualizarRece.setOnClickListener {
            medicinaId?.let {
                val medicina = MedicinaEntity(
                    id = it,
                    namedoc = mBinding.etNameDoctor.text.toString().trim(),
                    name = mBinding.etName.text.toString().trim(),
                    telefono = mBinding.etTelefono.text.toString().trim(),
                    fecha = mBinding.etFecha.text.toString().trim(),
                    email = mBinding.etEmail.text.toString().trim(),
                    tipoMedi = mBinding.spinnerMedicina.selectedItem.toString().trim(),
                    nomMedicina = mBinding.etNameMedicina.text.toString().trim()
                )
                updateMedicina(medicina)
            }
        }
    }

    private fun searchMedicinaByName(query: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val medicinas = withContext(Dispatchers.IO) {
                PacienteApplication.database.medicinaDao().searchMedicinasByName("%$query%")
            }
            if (medicinas.isNotEmpty()) {
                val medicina = medicinas[0]
                // Asigna el ID de la medicina encontrada al medicinaId
                medicinaId = medicina.id
                // Muestra los datos de la medicina en la interfaz de usuario
                mBinding.etNameDoctor.setText(medicina.namedoc)
                mBinding.etName.setText(medicina.name)
                mBinding.etTelefono.setText(medicina.telefono)
                mBinding.etFecha.setText(medicina.fecha)
                mBinding.etEmail.setText(medicina.email)
                mBinding.spinnerMedicina.setSelection(getGenderIndex(medicina.tipoMedi))
                mBinding.etNameMedicina.setText(medicina.nomMedicina)
            } else {
                Toast.makeText(this@EditarReceta, "No se encontraron medicinas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateMedicina(medicina: MedicinaEntity) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    PacienteApplication.database.medicinaDao().updateMedicina(medicina)
                }
                setResult(RESULT_OK, Intent().putExtra("updateMedicinaId", medicina.id))
                finish()
                Toast.makeText(this@EditarReceta, "Receta actualizada correctamente", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@EditarReceta, "Error al actualizar receta medica: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getGenderIndex(gender: String): Int {
        val genderArray = resources.getStringArray(R.array.opciones_medicina)
        return genderArray.indexOfFirst { it.equals(gender, ignoreCase = true) }
    }

    private fun setupBottomNav() {
        mBinding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    val intent = Intent(this, MenuInicio::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_regresar -> {
                    val intent = Intent(this, OpcionesMenuRecetas::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_cerrar -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
