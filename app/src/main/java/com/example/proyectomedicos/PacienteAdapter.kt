package com.example.proyectomedicos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomedicos.databinding.ItemPacienteBinding

class PacienteAdapter(
    private var pacientes: MutableList<PacienteEntity>,
    private var listener: OnClickListener
) : RecyclerView.Adapter<PacienteAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_paciente, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = pacientes.size

    fun add(pacienteEntity: PacienteEntity) {
        pacientes.add(pacienteEntity)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val paciente = pacientes[position]
        holder.bind(paciente)
    }

    fun setPacientes(pacientes: List<PacienteEntity>) {
        this.pacientes.clear()
        this.pacientes.addAll(pacientes)
        notifyDataSetChanged()
    }

    fun update(paciente: PacienteEntity) {
        val index = pacientes.indexOfFirst { it.id == paciente.id }
        if (index != -1) {
            pacientes[index] = paciente
            notifyItemChanged(index)
        }
    }

    fun delete(pacienteEntity: PacienteEntity) {
        val index = pacientes.indexOfFirst { it.id == pacienteEntity.id }
        if (index != -1) {
            pacientes.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemPacienteBinding.bind(view)

        fun bind(paciente: PacienteEntity) {
            with(binding) {
                tvName.text = paciente.name
                tvTlf.text = paciente.telefono
                tvDni.text = paciente.dni
                tvDireccion.text = paciente.direccion
                cbFavorite.isChecked = paciente.isfav

                root.setOnClickListener { listener.onClick(paciente) }

                root.setOnLongClickListener {
                    listener.onDeletePaciente(paciente)
                    true
                }

                cbFavorite.setOnClickListener {
                    listener.onFavoritePaciente(paciente)
                }
            }
        }
    }
}