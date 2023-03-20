package com.example.myapplication

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.EventRowItemBinding

class CalendarAdapter : ListAdapter<EvenModel, CalendarAdapter.CalendarViewHolder>(CalendarDiffUtils()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
         var binding = EventRowItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
         return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        var events = getItem(position)
        holder.bind(events)
    }


    class CalendarViewHolder(private var binding : EventRowItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(evenModel: EvenModel){
            binding.event = evenModel
        }
    }

    class CalendarDiffUtils : DiffUtil.ItemCallback<EvenModel>() {
        override fun areItemsTheSame(oldItem: EvenModel, newItem: EvenModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EvenModel, newItem: EvenModel): Boolean {
            return oldItem == newItem
        }

    }


}