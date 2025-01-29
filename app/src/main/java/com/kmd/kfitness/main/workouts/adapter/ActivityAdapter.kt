package com.kmd.kfitness.main.workouts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.kmd.kfitness.R
import com.kmd.kfitness.main.workouts.data.ActivityModel

class ActivityAdapter(context: Context, private val activityList: List<ActivityModel>) :
    ArrayAdapter<ActivityModel>(context, R.layout.dropdown_item, activityList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.dropdown_item_text_view)
        textView.text = activityList[position].name // Display activity name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.dropdown_item_text_view)
        textView.text = activityList[position].name // Ensure dropdown shows name only
        return view
    }
}
