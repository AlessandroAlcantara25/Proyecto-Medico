package com.example.proyectomedicos

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomedicos.databinding.EditPacienteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditarPaciente : AppCompatActivity() {
    private lateinit var mBinding: EditPacienteBinding
    private var pacienteId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = EditPacienteBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupBottomNav()

        val genderArray = resources.getStringArray(R.array.opciones_genero)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.spinnergenero.adapter = adapter

        mBinding.btnBuscar.setOnClickListener {
            val query = mBinding.etBuscar.text.toString()
            if (query.isNotEmpty()) {
                searchPaciente(query)
            } else {
                Toast.makeText(this, "Ingrese un nombre para buscar", Toast.LENGTH_SHORT).show()
            }
        }

        mBinding.btEditar.setOnClickListener {
            pacienteId?.let {
                val paciente = PacienteEntity(
                    id = it,
                    name = mBinding.etName.text.toString().trim(),
                    telefono = mBinding.etTelefono.text.toString().trim(),
                    dni = mBinding.etDni.text.toString().trim(),
                    email = mBinding.etEmail.text.toString().trim(),
                    sexo = mBinding.spinnergenero.selectedItem.toString().trim(),
                    peso = mBinding.etPeso.text.toString().trim(),
                    talla = mBinding.etTalla.text.toString().trim(),
                    direccion = mBinding.etDirec.text.toString().trim()
                )
                updatePaciente(paciente)
            }
        }
    }

    private fun searchPaciente(query: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val pacientes = withContext(Dispatchers.IO) {
                PacienteApplication.database.pacienteDao().searchPacientesByName("%$query%")
            }
            if (pacientes.isNotEmpty()) {
                pacienteId = pacientes[0].id
                navigateToEditPaciente(pacientes[0])
            } else {
                Toast.makeText(
                    this@EditarPaciente,
                    "No se encontraron pacientes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigateToEditPaciente(paciente: PacienteEntity) {
        mBinding.etName.setText(paciente.name)
        mBinding.etTelefono.setText(paciente.telefono)
        mBinding.etDni.setText(paciente.dni)
        mBinding.etEmail.setText(paciente.email)
        mBinding.spinnergenero.setSelection(getGenderIndex(paciente.sexo))
        mBinding.etPeso.setText(paciente.peso)
        mBinding.etTalla.setText(paciente.talla)
        mBinding.etDirec.setText(paciente.direccion)
    }

    private fun updatePaciente(paciente: PacienteEntity) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    PacienteApplication.database.pacienteDao().updatePaciente(paciente)
                }
                setResult(RESULT_OK, Intent().putExtra("updatedPacienteId", paciente.id))
                finish()
                Toast.makeText(
                    this@EditarPaciente,
                    "Paciente actualizado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    this@EditarPaciente,
                    "Error al actualizar paciente: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getGenderIndex(gender: String): Int {
        val genderArray = resources.getStringArray(R.array.opciones_genero)
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
                    val intent = Intent(this, OpcionesMenu::class.java)
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

