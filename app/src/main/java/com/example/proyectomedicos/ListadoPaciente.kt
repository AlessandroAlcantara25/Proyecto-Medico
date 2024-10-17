package com.example.proyectomedicos

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectomedicos.databinding.ListadoPacienteBinding
import kotlinx.coroutines.*
import java.util.concurrent.LinkedBlockingQueue

class ListadoPaciente : AppCompatActivity(), OnClickListener {

    private lateinit var mBinding: ListadoPacienteBinding

    private lateinit var mAdapter: PacienteAdapter

    companion object {
        private const val EDIT_PACIENTE_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ListadoPacienteBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupBottomNav()

        mBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = PacienteAdapter(mutableListOf(), this)
        mBinding.recyclerView.adapter = mAdapter

        mBinding.btnBuscar.setOnClickListener {
            val query = mBinding.etName.text.toString()
            if (query.isNotEmpty()) {
                searchPacientes(query)
            } else {
                Toast.makeText(this, "Ingrese un nombre para buscar", Toast.LENGTH_SHORT).show()
            }
        }

        mBinding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    loadPacientes()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        loadPacientes()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PACIENTE_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.getLongExtra("updatedPacienteId", -1L)?.let { id ->
                if (id != -1L) {
                    updatePacienteInList(id)
                }
            }
        }
    }

    private fun updatePacienteInList(id: Long) {
        GlobalScope.launch(Dispatchers.Main) {
            val paciente = withContext(Dispatchers.IO) {
                PacienteApplication.database.pacienteDao().getPacienteById(id)
            }
            paciente?.let {
                mAdapter.update(it)
            }
        }
    }

    private fun loadPacientes() {
        GlobalScope.launch(Dispatchers.Main) {
            val pacientes = withContext(Dispatchers.IO) {
                getPacientes()
            }
            if (pacientes != null) {
                mAdapter.setPacientes(pacientes)
            } else {
                Toast.makeText(
                    this@ListadoPaciente,
                    "Error al cargar los pacientes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun searchPacientes(query: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val pacientes = withContext(Dispatchers.IO) {
                PacienteApplication.database.pacienteDao().searchPacientesByName("%$query%")
            }
            if (pacientes != null) {
                mAdapter.setPacientes(pacientes)
            } else {
                Toast.makeText(
                    this@ListadoPaciente,
                    "No se encontraron pacientes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getPacientes(): MutableList<PacienteEntity> {
        return PacienteApplication.database.pacienteDao().getPaciente()
    }

    override fun onClick(pacienteEntity: PacienteEntity) {
        val intent = Intent(this, EditarPaciente::class.java)
        intent.putExtra("pacienteId", pacienteEntity.id)
        startActivityForResult(intent, EDIT_PACIENTE_REQUEST_CODE)
    }

    override fun onFavoritePaciente(pacienteEntity: PacienteEntity) {
        pacienteEntity.isfav = !pacienteEntity.isfav
        val queue = LinkedBlockingQueue<PacienteEntity>()
        Thread {
            PacienteApplication.database.pacienteDao().updatePaciente(pacienteEntity)
            queue.add(pacienteEntity)
        }.start()
        mAdapter.update(queue.take())
    }

    override fun onDeletePaciente(pacienteEntity: PacienteEntity) {
        val queue = LinkedBlockingQueue<PacienteEntity>()
        Thread {
            PacienteApplication.database.pacienteDao().deletePaciente(pacienteEntity)
            queue.add(pacienteEntity)
        }.start()
        mAdapter.delete(queue.take())
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