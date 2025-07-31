package ru.netology.yandexmaps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.yandexmaps.databinding.FieldForPlacemarksBinding
import ru.netology.yandexmaps.room.db.entity.PointEntity


interface OnInteractionListener {
    fun onTap(point: PointEntity) {}
}

typealias ViewHolder = RecyclerView.ViewHolder
typealias Diff = DiffUtil.ItemCallback<PointEntity>

//-------------------- PostAdapter -------------------

class PointAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<PointEntity, PointHolder>(PointDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointHolder {
        val view =
            FieldForPlacemarksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PointHolder(
            view, onInteractionListener
        )
    }

    override fun onBindViewHolder(holder: PointHolder, position: Int) {
        val point = getItem(position)
        holder.bind(point)
    }
}

//--------------------- PostHolder ---------------------

class PointHolder(
    private val view: FieldForPlacemarksBinding,
    private val onInteractionListener: OnInteractionListener,
) : ViewHolder(view.root) {
    fun bind(point: PointEntity) {
        view.apply {

            fieldPlacemarks.text = point.text
            fieldPlacemarks.setOnClickListener {
                onInteractionListener.onTap(point)
            }

        }
    }
}

//---------------------- DiffUtil -----------------------

class PointDiffCallback : Diff() {
    override fun areItemsTheSame(oldItem: PointEntity, newItem: PointEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PointEntity, newItem: PointEntity): Boolean {
        return oldItem == newItem
    }
}

//------------------------- End
