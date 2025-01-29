package com.kmd.kfitness.main.goals.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.kmd.kfitness.R
import com.kmd.kfitness.databinding.FragmentGoalDetailsBinding
import com.kmd.kfitness.general.api.KFitnessUrl
import com.kmd.kfitness.general.helper.ApiErrorHandler
import com.kmd.kfitness.general.helper.MessageHelper
import com.kmd.kfitness.general.identity.CustomHttpStack
import com.kmd.kfitness.main.goals.adapter.MyProgressRecyclerViewAdapter
import com.kmd.kfitness.main.goals.data.GoalDetailModel

/**
 * A simple [Fragment] subclass.
 * Use the [GoalDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalDetailsFragment : Fragment() {

    private var _binding: FragmentGoalDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestQueue: RequestQueue
    private lateinit var messageHelper: MessageHelper
    private var gson = Gson()

    private  var goalId : Int = 0
    private lateinit var goalDetailModel: GoalDetailModel
    private lateinit var apiErrorHandler: ApiErrorHandler

    private lateinit var adapter : MyProgressRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goalId = arguments?.getInt("id") as Int
        requestQueue = Volley.newRequestQueue(requireContext(), CustomHttpStack())
        messageHelper = MessageHelper(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGoalDetailsBinding.inflate(layoutInflater,container,false)
        binding.progressList.layoutManager = LinearLayoutManager(requireContext())
        binding.fabAddProgress.setOnClickListener{
            addNewProgress()
        }

        fetchGoalDetails()
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    private fun setDetailModel(goalDetailModel: GoalDetailModel){
        binding.descriptionTextView.text = goalDetailModel.description
        binding.targetValueTextView.text = goalDetailModel.target.toString()
        binding.statusTextView.text = goalDetailModel.status
    }
    private fun addNewProgress(){
        val bundle = Bundle().apply {
            putInt("goal_id",goalId)
        }
       findNavController().navigate(R.id.nav_goal_details_to_nav_edit_progress,bundle)
    }
    private fun fetchGoalDetails() {
        val url = "${KFitnessUrl.GOALS}?id=${goalId}"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    // Use Gson to parse the JSON array into a List<T>
                    goalDetailModel = gson.fromJson(response,GoalDetailModel::class.java)
                    // Update the adapter with the new data
                    setDetailModel(goalDetailModel)
                    adapter = MyProgressRecyclerViewAdapter(goalDetailModel.progresses)
                    { progressModel ->
                        val bundle = Bundle().apply {
                            putSerializable(
                                "progressModel",
                                progressModel
                            ) // `item` is the GoalModel object
                            putInt("goal_id",goalId)
                        }
                        findNavController().navigate(
                            R.id.nav_goal_details_to_nav_edit_progress,
                            bundle
                        )
                    }
                    binding.progressList.adapter = adapter

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("UserFragment", "Error parsing JSON: ${e.message}")
                }
            },
            { error ->
                messageHelper.showPositiveDialog("Error",error.toString())
                Log.e("UserFragment", "Volley error: ${error.message}")
            }
        )

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest)
    }
}