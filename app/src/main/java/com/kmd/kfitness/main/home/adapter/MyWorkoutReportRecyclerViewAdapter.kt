package com.kmd.kfitness.main.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kmd.kfitness.databinding.WorkoutReportListItemBinding
import com.kmd.kfitness.main.home.data.WorkoutReportModel

class MyWorkoutReportRecyclerViewAdapter(
    private val items : List<WorkoutReportModel>
) :
    RecyclerView.Adapter<MyWorkoutReportRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(binding: WorkoutReportListItemBinding ) : RecyclerView.ViewHolder(binding.root) {
        val activityNameTextView = binding.activityTextView
        val durationTextView = binding.durationTextView
        val statusTextView = binding.statusTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            WorkoutReportListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun getItemCount() = items.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.activityNameTextView.text = item.activity
        holder.statusTextView.text = item.status
        holder.durationTextView.text = item.duration.toString()

    }
}