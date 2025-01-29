package com.kmd.kfitness.main.workouts.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kmd.kfitness.databinding.WorkoutListItemBinding
import com.kmd.kfitness.general.helper.GeneralHelper
import com.kmd.kfitness.main.workouts.data.EditableWorkoutModel
import com.kmd.kfitness.main.workouts.data.WorkoutModel

class MyWorkoutRecyclerViewAdapter(
    private val items : List<WorkoutModel>,
    private val onDeleteClick : (Int) -> Unit,
    private val onEditClick : (EditableWorkoutModel) -> Unit,
) :
    RecyclerView.Adapter<MyWorkoutRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(binding: WorkoutListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val dateTextView = binding.dateTextView
        val statusTextView = binding.statusTextView
        val activityNameTextView = binding.activityNameTextView
        val durationTextView = binding.durationTextView
        val editButton = binding.editButton
        val deleteButton = binding.deleteButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            WorkoutListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.dateTextView.text = GeneralHelper.kDateFormat.format(item.date)
        holder.statusTextView.text = item.status
        holder.activityNameTextView.text = item.activity
        holder.durationTextView.text = item.duration.toString()
        holder.deleteButton.setOnClickListener{
            onDeleteClick(item.id)
        }

        holder.editButton.setOnClickListener {
            onEditClick(item.toEditableModel())
        }
    }
}