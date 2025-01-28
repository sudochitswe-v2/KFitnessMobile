package com.kmd.kfitness.main.goals.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kmd.kfitness.R
import com.kmd.kfitness.databinding.FragmentProgressListBinding
import com.kmd.kfitness.general.api.KFitnessUrl
import com.kmd.kfitness.general.helper.ApiErrorHandler
import com.kmd.kfitness.general.helper.MessageHelper
import com.kmd.kfitness.general.identity.CustomHttpStack
import com.kmd.kfitness.general.identity.UserIdentity
import com.kmd.kfitness.main.goals.adapter.MyGoalRecyclerViewAdapter
import com.kmd.kfitness.main.goals.adapter.MyProgressRecyclerViewAdapter
import com.kmd.kfitness.main.goals.data.GoalDetailModel
import com.kmd.kfitness.main.goals.data.GoalModel
import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 * Use the [ProgressListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProgressListFragment : Fragment() {

    private var _binding: FragmentProgressListBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestQueue: RequestQueue
    private lateinit var messageHelper: MessageHelper
    private var gson = Gson()

    private  var goalId : Int? = null
    private lateinit var goalDetailModel: GoalDetailModel
    private lateinit var apiErrorHandler: ApiErrorHandler

    private lateinit var adapter : MyProgressRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goalId = arguments?.getSerializable("id") as? Int
        requestQueue = Volley.newRequestQueue(requireContext(), CustomHttpStack())
        messageHelper = MessageHelper(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProgressListBinding.inflate(layoutInflater,container,false)
        binding.progressList.layoutManager = LinearLayoutManager(requireContext())
        fetchGoalDetails()
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    private fun setDetailModel(goalDetailModel: GoalDetailModel){
        binding.descriptionTextView.text = goalDetailModel.description
        binding.targetValueTextView.text = goalDetailModel.target.toString()
        binding.statusTextView.text = goalDetailModel.status
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
                        }
                        findNavController().navigate(
                            R.id.action_nav_goals_to_addGoalFragment,
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