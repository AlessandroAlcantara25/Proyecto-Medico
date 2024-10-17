package com.example.proyectomedicos

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomedicos.databinding.AgregarRecetaBinding

class AgregarMedicina : AppCompatActivity() {

    private lateinit var mBinding: AgregarRecetaBinding
    private lateinit var mAdapter: MedicinaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = AgregarRecetaBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupBottomNav()

        val spinnertipoMedi: Spinner = findViewById(R.id.spinnerMedicina)

        ArrayAdapter.createFromResource(
            this,
            R.array.opciones_medicina,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnertipoMedi.adapter = adapter
        }

        // Inicializa mAdapter con una lista vacía de pacientes y un listener vacío ya que no se utilizará en esta actividad
        mAdapter = MedicinaAdapter(mutableListOf(), object : OnClickListenerMedicina {
            override fun onClick(medicinaEntity: MedicinaEntity) {}
            override fun onDeleteMedicina(medicinaEntity: MedicinaEntity) {}
            override fun getMedicinas(): MutableList<MedicinaEntity> {
                return PacienteApplication.database.medicinaDao().getMedicinas()
            }

            override fun onFavoriteMedicina(medicinaEntity: MedicinaEntity) {}
        })

        mBinding.btnAgregarMedicina.setOnClickListener {
            val nombreDoctor = mBinding.etNameDoctor.text.toString().trim()
            val nombrePaciente = mBinding.etName.text.toString().trim()
            val telefono = mBinding.etTelefono.text.toString().trim()
            val fecha = mBinding.etFecha.text.toString().trim()
            val email = mBinding.etEmail.text.toString().trim()
            val tipoMedicina = mBinding.spinnerMedicina.selectedItem.toString().trim()
            val nombreMedicina = mBinding.etNameMedicina.text.toString().trim()

            // Verificar campos vacíos
            if (nombreDoctor.isEmpty() || nombrePaciente.isEmpty() || telefono.isEmpty() || fecha.isEmpty() || email.isEmpty() || tipoMedicina.isEmpty() || nombreMedicina.isEmpty()) {
                Toast.makeText(this, "No se permiten campos vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Si todos los campos están llenos, proceder con la adición de la receta médica
            val medicina = MedicinaEntity(
                namedoc = nombreDoctor,
                name = nombrePaciente,
                telefono = telefono,
                fecha = fecha,
                email = email,
                tipoMedi = tipoMedicina,
                nomMedicina = nombreMedicina
            )

            // Continuar con la lógica de agregar la receta médica en un hilo separado
            Thread {
                try {
                    PacienteApplication.database.medicinaDao().addMedicina(medicina)

                    // Volver al hilo principal para mostrar el Toast
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Receta médica agregada correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        mAdapter.add(medicina)
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Error al agregar receta médica: ${e.message}",
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
                    val intent = Intent(this, OpcionesMenuRecetas::class.java)
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