package com.kmd.kfitness.main.goals.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.kmd.kfitness.databinding.FragmentGoalsBinding
import com.kmd.kfitness.main.goals.data.GoalModel

class MyGoalRecyclerViewAdapter(
    private val values: List<GoalModel>,
    private val onEditClick: (GoalModel) -> Unit,
    private val onDetailClick : (Int)->Unit,
    private val onDeleteClick : (Int)->Unit
) : RecyclerView.Adapter<MyGoalRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentGoalsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.descriptionTextView.text = item.description
        holder.statusTextView.text = item.status
        holder.targetValueTextView.text = item.target.toString()

        holder.deleteButton.setOnClickListener{
            onDeleteClick(item.id)
        }
        holder.detailsButton.setOnClickListener{
            onDetailClick(item.id)
        }
        holder.editButton.setOnClickListener {
            onEditClick(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentGoalsBinding) : RecyclerView.ViewHolder(binding.root) {
        val descriptionTextView = binding.description
        val statusTextView = binding.statusText
        val targetValueTextView = binding.targetText
        val editButton = binding.editButton
        val detailsButton = binding.viewDetailsButton
        val deleteButton = binding.deleteButton
    }

}