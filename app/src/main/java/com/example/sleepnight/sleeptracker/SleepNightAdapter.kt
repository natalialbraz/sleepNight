package com.example.sleepnight.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepnight.R
import com.example.sleepnight.convertDurationToFormatted
import com.example.sleepnight.convertNumericQualityToString
import com.example.sleepnight.database.SleepNight
import com.example.sleepnight.databinding.ListItemSleepNightBinding

// ListAdapter has two arguments: the type of the list that it is holding and the view holder
class SleepNightAdapter: ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {
   // with the subclass ListAdapter we don't need to define the field data
    /*var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    // called when the RV needs to know the size of the list
    override fun getItemCount() = data.size
    // Not necessary with listAdapter too
    */

    // called when RV needs a new VH
    // a VH holds a view for the RV as well as providing additional information
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // called when RV needs to show an item , the VH passed may be recycled
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding: ListItemSleepNightBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SleepNight) {
            binding.sleep = item
            binding.executePendingBindings() // it can be slightly faster to size the views
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
    class SleepNightDiffCallback: DiffUtil.ItemCallback<SleepNight>() {
        // used to discover if an item was edit, removed or moved
        override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            TODO("Not yet implemented")
        }


    }



}