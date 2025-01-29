package com.kmd.kfitness.main.workouts.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kmd.kfitness.R
import com.kmd.kfitness.databinding.FragmentWorkoutsBinding
import com.kmd.kfitness.general.api.KFitnessUrl
import com.kmd.kfitness.general.helper.ApiErrorHandler
import com.kmd.kfitness.general.helper.GeneralHelper
import com.kmd.kfitness.general.helper.MessageHelper
import com.kmd.kfitness.general.identity.CustomHttpStack
import com.kmd.kfitness.general.identity.UserIdentity
import com.kmd.kfitness.main.workouts.adapter.MyWorkoutRecyclerViewAdapter
import com.kmd.kfitness.main.workouts.data.EditableWorkoutModel
import com.kmd.kfitness.main.workouts.data.WorkoutModel

class WorkoutsFragment : Fragment() {

    private var _binding: FragmentWorkoutsBinding? = null

    private val binding get() = _binding!!

    private val gson = GsonBuilder().setDateFormat(GeneralHelper.K_DATE_FORMAT).create()
    private lateinit var requestQueue: RequestQueue
    private lateinit var messageHelper: MessageHelper
    private lateinit var adapter : MyWorkoutRecyclerViewAdapter
    private lateinit var apiErrorHandler: ApiErrorHandler

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


        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        binding.addNewFab.setOnClickListener{
            createWorkout()
        }
        fetchWorkouts()

        return binding.root
    }

    private fun fetchWorkouts(){
        val url = "${KFitnessUrl.WORKOUTS}?user_id=${UserIdentity.instance.id}"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    // Use Gson to parse the JSON array into a List<T>
                    val workoutListType = object : TypeToken<List<WorkoutModel>>() {}.type
                    val workouts = gson.fromJson<List<WorkoutModel>>(response, workoutListType)

                    adapter = MyWorkoutRecyclerViewAdapter(workouts,
                        {
                                id -> deleteWorkout(id)
                        },
                        {
                                editableModel -> editWorkout(editableModel)
                        }
                    )
                    binding.goalList.adapter = adapter

                    // Notify the adapter that the data has changed
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("WorkoutsFragment", "Error parsing JSON: ${e.message}")
                }
            },
            { error ->
                messageHelper.showPositiveDialog("Error",error.toString())
                Log.e("WorkoutsFragment", "Volley error: ${error.message}")
            }
        )

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest)
    }
    private fun deleteWorkout(id: Int){
        val url = "${KFitnessUrl.WORKOUTS}?id=$id"

        val deleteRequest = StringRequest(
            Request.Method.DELETE, url,
            { _ -> fetchWorkouts()
            },
            { error ->
                messageHelper.showPositiveDialog("Error",error.toString())
                Log.e("GoalsFragment", "Volley error: ${error.message}")
            }
        )
        requestQueue.add(deleteRequest)
    }
    private fun editWorkout(editableWorkoutModel: EditableWorkoutModel){
        val bundle = Bundle().apply {
            putString("workoutModel",gson.toJson(editableWorkoutModel))
        }
        findNavController().navigate(R.id.action_nav_workouts_to_nav_edit_workout,bundle)
    }
    private fun createWorkout(){
        findNavController().navigate(R.id.action_nav_workouts_to_nav_edit_workout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}