package com.example.proyectomedicos

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomedicos.databinding.AgregarPacienteBinding

class InicioPaciente : AppCompatActivity() {

    private lateinit var mBinding: AgregarPacienteBinding
    private lateinit var mAdapter: PacienteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = AgregarPacienteBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupBottomNav()

        val spinnerGenero: Spinner = findViewById(R.id.spinnergenero)

        ArrayAdapter.createFromResource(
            this,
            R.array.opciones_genero,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGenero.adapter = adapter
        }

        // Inicializa mAdapter con una lista vacía de pacientes y un listener vacío ya que no se utilizará en esta actividad
        mAdapter = PacienteAdapter(mutableListOf(), object : OnClickListener {
            override fun onClick(pacienteEntity: PacienteEntity) {}
            override fun onDeletePaciente(pacienteEntity: PacienteEntity) {}
            override fun getPacientes(): MutableList<PacienteEntity> {
                return PacienteApplication.database.pacienteDao().getPaciente()
            }

            override fun onFavoritePaciente(pacienteEntity: PacienteEntity) {}
        })

        mBinding.btnAgregar.setOnClickListener {
            val nombre = mBinding.etName.text.toString().trim()
            val telefono = mBinding.etTelefono.text.toString().trim()
            val dni = mBinding.etDni.text.toString().trim()
            val email = mBinding.etEmail.text.toString().trim()
            val sexo = mBinding.spinnergenero.selectedItem.toString().trim()
            val peso = mBinding.etPeso.text.toString().trim()
            val talla = mBinding.etTalla.text.toString().trim()
            val direccion = mBinding.etDirec.text.toString().trim()

            // Verificar campos vacíos
            if (nombre.isEmpty() || telefono.isEmpty() || dni.isEmpty() || email.isEmpty() || sexo.isEmpty() || peso.isEmpty() || talla.isEmpty() || direccion.isEmpty()) {
                Toast.makeText(this, "No se permiten campos vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Si todos los campos están llenos, proceder con la adición del paciente
            val paciente = PacienteEntity(
                name = nombre,
                telefono = telefono,
                dni = dni,
                email = email,
                sexo = sexo,
                peso = peso,
                talla = talla,
                direccion = direccion
            )

            // Continuar con la lógica de agregar el paciente en un hilo separado
            Thread {
                try {
                    PacienteApplication.database.pacienteDao().addPaciente(paciente)

                    // Volver al hilo principal para mostrar el Toast y actualizar el adaptador
                    runOnUiThread {
                        Toast.makeText(this, "Paciente agregado correctamente", Toast.LENGTH_SHORT)
                            .show()
                        mAdapter.add(paciente)
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Error al agregar paciente: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.start()
        }
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
                    // Cerrar sesión y volver a MainActivity
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



