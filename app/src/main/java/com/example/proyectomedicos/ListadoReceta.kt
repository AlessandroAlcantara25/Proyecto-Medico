package com.example.proyectomedicos

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectomedicos.databinding.ListadoRecetasBinding
import kotlinx.coroutines.*
import java.util.concurrent.LinkedBlockingQueue

class ListadoReceta : AppCompatActivity(), OnClickListenerMedicina {

    private lateinit var mBinding: ListadoRecetasBinding
    private lateinit var mAdapter: MedicinaAdapter

    companion object {
        private const val EDIT_RECETA_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ListadoRecetasBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupBottomNav()

        mBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = MedicinaAdapter(mutableListOf(), this)
        mBinding.recyclerView.adapter = mAdapter

        mBinding.btnBuscar.setOnClickListener {
            val query = mBinding.etName.text.toString()
            if (query.isNotEmpty()) {
                searchRecetas(query)
            } else {
                Toast.makeText(this, "Ingrese un nombre para buscar", Toast.LENGTH_SHORT).show()
            }
        }

        mBinding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    loadRecetas()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        loadRecetas()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ListadoReceta.EDIT_RECETA_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.getLongExtra("updatedMedicinaId", -1L)?.let { id ->
                if (id != -1L) {
                    updateMedicinaInList(id)
                }
            }
        }
    }

    private fun updateMedicinaInList(id: Long) {
        GlobalScope.launch(Dispatchers.Main) {
            val medicina = withContext(Dispatchers.IO) {
                PacienteApplication.database.medicinaDao().getMedicinaById(id)
            }
            medicina?.let {
                mAdapter.update(it)
            }
        }
    }

    private fun loadRecetas() {
        GlobalScope.launch(Dispatchers.Main) {
            val recetas = withContext(Dispatchers.IO) {
                getMedicinas()
            }

            if (recetas != null) {
                mAdapter.setMedicinas(recetas)
            } else {
                Toast.makeText(this@ListadoReceta, "Error al cargar las recetas ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchRecetas(query: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val medicinas = withContext(Dispatchers.IO) {
                PacienteApplication.database.medicinaDao().searchMedicinasByName("%$query%")
            }

            if (medicinas != null) {
                mAdapter.setMedicinas(medicinas)
            } else {
                Toast.makeText(this@ListadoReceta, "No se encontraron recetas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getMedicinas(): MutableList<MedicinaEntity> {
        return PacienteApplication.database.medicinaDao().getMedicinas()
    }

    override fun onClick(medicinaEntity: MedicinaEntity) {
        val intent = Intent(this, EditarReceta::class.java)
        intent.putExtra("MedicinaId", medicinaEntity.id)
        startActivityForResult(intent, ListadoReceta.EDIT_RECETA_REQUEST_CODE)
    }

    override fun onFavoriteMedicina(medicinaEntity: MedicinaEntity) {
        medicinaEntity.isfav = !medicinaEntity.isfav
        GlobalScope.launch(Dispatchers.IO) {
            PacienteApplication.database.medicinaDao().updateMedicina(medicinaEntity)
        }
    }

    override fun onDeleteMedicina(medicinaEntity: MedicinaEntity) {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                PacienteApplication.database.medicinaDao().deleteMedicina(medicinaEntity)
            }
            mAdapter.delete(medicinaEntity)
            loadRecetas()
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
