package com.example.proyectomedicos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomedicos.databinding.ItemRecetaBinding

class MedicinaAdapter(
    private var medicinas: MutableList<MedicinaEntity>,
    private var listener: OnClickListenerMedicina
) : RecyclerView.Adapter<MedicinaAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_receta, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = medicinas.size

    fun add(medicinaEntity: MedicinaEntity) {
        medicinas.add(medicinaEntity)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicina = medicinas[position]
        holder.bind(medicina)
    }

    fun setMedicinas(medicinas: List<MedicinaEntity>) {
        this.medicinas.clear()
        this.medicinas.addAll(medicinas)
        notifyDataSetChanged()
    }

    fun update(medicina: MedicinaEntity) {
        val index = medicinas.indexOfFirst { it.id == medicina.id }
        if (index != -1) {
            medicinas[index] = medicina
            notifyItemChanged(index)
        }
    }


    fun delete(medicinaEntity: MedicinaEntity) {
        val index = medicinas.indexOfFirst { it.id == medicinaEntity.id }
        if (index != -1) {
            medicinas.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecetaBinding.bind(view)

        fun bind(medicina: MedicinaEntity) {
            with(binding) {
                tvNameDoctor.text = medicina.namedoc
                tvName.text = medicina.name
                tvMedicina.text = medicina.tipoMedi
                tvNameMedicina.text = medicina.nomMedicina
                cbFavorite.isChecked = medicina.isfav

                root.setOnClickListener { listener.onClick(medicina) }

                root.setOnLongClickListener {
                    listener.onDeleteMedicina(medicina)
                    true
                }

                cbFavorite.setOnClickListener {
                    listener.onFavoriteMedicina(medicina)
                }
            }
        }
    }
}