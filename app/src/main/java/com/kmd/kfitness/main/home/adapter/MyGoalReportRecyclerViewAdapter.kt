package com.kmd.kfitness.main.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kmd.kfitness.databinding.FragmentGoalsBinding
import com.kmd.kfitness.databinding.GoalReportListItemBinding
import com.kmd.kfitness.databinding.WorkoutReportListItemBinding
import com.kmd.kfitness.general.helper.GeneralHelper
import com.kmd.kfitness.main.goals.adapter.MyGoalRecyclerViewAdapter
import com.kmd.kfitness.main.home.data.GoalReportModel

class MyGoalReportRecyclerViewAdapter(
   private val items : List<GoalReportModel>
) :
    RecyclerView.Adapter<MyGoalReportRecyclerViewAdapter.ViewHolder>()
{
    inner class ViewHolder(binding: GoalReportListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val targetTextView = binding.targetTextView
        val endDateTextView = binding.endDateTextView
        val descriptionTextView = binding.descriptionTextView

    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyGoalReportRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.targetTextView.text = item.target.toString()
        holder.endDateTextView.text = GeneralHelper.kDateFormat.format(item.endDate)
        holder.descriptionTextView.text = item.description
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            GoalReportListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun getItemCount() = items.size

}