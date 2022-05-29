package com.example.carsapp.features.presentation.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.carsapp.R
import com.example.carsapp.databinding.ItemCarBinding
import com.example.carsapp.features.domain.model.CarsModel

class CarsAdapter(private val carsList: ArrayList<CarsModel.Data>) :
    RecyclerView.Adapter<CarsAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ItemCarBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, carsList[position])
    }

    override fun getItemCount(): Int {
        return carsList.size
    }

    class ViewHolder(private val itemViewBinding: ItemCarBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        fun bind(context: Context, data: CarsModel.Data) {
            itemViewBinding.run {
                Glide.with(context).load(data.imageUrl).error(R.drawable.car)
                    .placeholder(R.drawable.loading).into(ivCarImage)
                tvTitle.text = data.brand
                if(data.isUsed){
                    isUsed.visibility = View.GONE
                    tvCondition.text = "Used"
                    tvCondition.setTextColor(context.getColor(R.color.red))
                }else{
                    isUsed.visibility = View.VISIBLE
                    tvCondition.text = "New"
                    tvCondition.setTextColor(context.getColor(R.color.green))
                }
                tvModel.text = data.constractionYear ?: "Unknown"
            }
        }
    }
}