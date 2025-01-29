package com.kmd.kfitness.main.home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import com.kmd.kfitness.databinding.FragmentHomeBinding
import com.kmd.kfitness.general.api.KFitnessUrl
import com.kmd.kfitness.general.helper.ApiErrorHandler
import com.kmd.kfitness.general.helper.GeneralHelper
import com.kmd.kfitness.general.helper.MessageHelper
import com.kmd.kfitness.general.identity.CustomHttpStack
import com.kmd.kfitness.general.identity.UserIdentity
import com.kmd.kfitness.main.home.adapter.MyGoalReportRecyclerViewAdapter
import com.kmd.kfitness.main.home.adapter.MyWorkoutReportRecyclerViewAdapter
import com.kmd.kfitness.main.home.data.ReportModel
import java.util.Date

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val gson = GsonBuilder().setDateFormat(GeneralHelper.K_DATE_FORMAT).create()
    private lateinit var requestQueue: RequestQueue
    private lateinit var messageHelper: MessageHelper
    private lateinit var apiErrorHandler: ApiErrorHandler
    private val today = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestQueue = Volley.newRequestQueue(requireContext(), CustomHttpStack())
        messageHelper = MessageHelper(requireContext())
        apiErrorHandler = ApiErrorHandler(messageHelper,gson)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        binding.todayDateTextView.text= GeneralHelper.kDateFormat.format(today)
        getReport()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    @SuppressLint("SetTextI18n")
    private fun getReport(){
        val date = GeneralHelper.kDateFormat.format(today)
        val url = "${KFitnessUrl.REPORT}?user_id=${UserIdentity.instance.id}&date=$date"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    // Use Gson to parse the JSON array into a List<T>
                    val report = gson.fromJson(response,ReportModel::class.java)
                    binding.caloriesTextView.text = report.calories.toString()
                    val workoutReportAdapter = MyWorkoutReportRecyclerViewAdapter(report.workouts)
                    binding.recyclerTodayWorkouts.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerGoalsInProgress.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerTodayWorkouts.adapter = workoutReportAdapter
                    val goalReportAdapter = MyGoalReportRecyclerViewAdapter(report.goals)
                    binding.recyclerGoalsInProgress.adapter = goalReportAdapter
                    checkEmptyState()

                    // Notify the adapter that the data has changed
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("HomeFragment", "Error parsing JSON: ${e.message}")
                }
            },
            { error ->
                messageHelper.showPositiveDialog("Error",error.toString())
                Log.e("HomeFragment", "Volley error: ${error.message}")
            }
        )

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest)
    }
    private fun checkEmptyState() {
        // Today Workouts
        if (binding.recyclerTodayWorkouts.adapter?.itemCount == 0) {
            binding.noTodayWorkouts.visibility = View.VISIBLE
            binding.recyclerTodayWorkouts.visibility = View.GONE
        } else {
            binding.noTodayWorkouts.visibility = View.GONE
            binding.recyclerTodayWorkouts.visibility = View.VISIBLE
        }

        // Goals In Progress
        if (binding.recyclerGoalsInProgress.adapter?.itemCount == 0 ) {
            binding.noGoals.visibility = View.VISIBLE
            binding.recyclerGoalsInProgress.visibility = View.GONE
        } else {
            binding.noGoals.visibility = View.GONE
            binding.recyclerGoalsInProgress.visibility = View.VISIBLE
        }
    }

}