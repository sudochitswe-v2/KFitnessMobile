package com.kmd.kfitness.main.goals.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kmd.kfitness.databinding.ProgressItemBinding
import com.kmd.kfitness.general.helper.GeneralHelper
import com.kmd.kfitness.main.goals.data.ProgressModel

class MyProgressRecyclerViewAdapter (
 private val items : List<ProgressModel>,
 private val onEditClick: (ProgressModel) -> Unit
) : RecyclerView.Adapter<MyProgressRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ProgressItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val dateTextView = binding.dateTextView
        val valueTextView = binding.valueTextView
        val editButton = binding.editButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProgressItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        );
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.dateTextView.text = GeneralHelper.kDateFormat.format(item.date)
        holder.valueTextView.text = item.currentValue.toString()
        holder.editButton.setOnClickListener {
            onEditClick(item)
        }
    }
}