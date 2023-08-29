package com.sunnyweather.android.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.databinding.PlaceItemBinding
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.ui.weather.WeatherActivity

class PlaceAdapter(private val placeList: List<Place>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    private var listener: ItemClickListener<Place>? = null

    inner class ViewHolder(binding: PlaceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val placeName = binding.placeName
        val placeAddress = binding.placeAddress
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(PlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        holder.itemView.setOnClickListener {
            listener?.let {
                val index = holder.adapterPosition
                if (index >= 0) {
                    listener?.onItemClicked(index, placeList[index])
                }
            }
        }
        return holder
    }

    override fun getItemCount() = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    fun setItemClickListener(listener: ItemClickListener<Place>?) {
        this.listener = listener
    }

    interface ItemClickListener<K> {

        fun onItemClicked(position: Int, k: K)
    }
}