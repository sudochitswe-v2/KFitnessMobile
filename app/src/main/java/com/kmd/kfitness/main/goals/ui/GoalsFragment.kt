package com.kmd.kfitness.main.goals.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kmd.kfitness.R
import com.kmd.kfitness.databinding.FragmentGoalsListBinding
import com.kmd.kfitness.general.api.KFitnessUrl
import com.kmd.kfitness.general.helper.MessageHelper
import com.kmd.kfitness.general.identity.CustomHttpStack
import com.kmd.kfitness.general.identity.UserIdentity
import com.kmd.kfitness.main.goals.adapter.MyGoalRecyclerViewAdapter
import com.kmd.kfitness.main.goals.data.GoalModel

/**
 * A fragment representing a list of Items.
 */
class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val gson = Gson()
    private lateinit var _requestQueue: RequestQueue
    private lateinit var _messageHelper: MessageHelper
    private lateinit var _listAdapter: MyGoalRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _requestQueue = Volley.newRequestQueue(requireContext(),CustomHttpStack())
        _messageHelper = MessageHelper(requireContext())

        _binding = FragmentGoalsListBinding.inflate(inflater,container,false)

        binding.goalList.layoutManager = LinearLayoutManager(requireContext())

        fetchGoals()

        binding.addNewFab.setOnClickListener{
            createNew()
        }

        return binding.root;
    }
    private fun createNew() {
        findNavController().navigate(R.id.action_nav_goals_to_nav_edit_goal)
    }
    private fun edit(goalModel:GoalModel){
        val bundle = Bundle().apply {
            putSerializable(
                "goalModel",
                goalModel
            ) // `item` is the GoalModel object
        }
        findNavController().navigate(
            R.id.action_nav_goals_to_nav_edit_goal,
            bundle
        )
    }
    private fun viewDetails(id : Int){
        val bundle = Bundle().apply {
            putInt("id",id)
        }
        findNavController().navigate(R.id.action_nav_goals_to_nav_goal_details,bundle)
    }
    private fun fetchGoals() {
        val url = "${KFitnessUrl.GOALS}?user_id=${UserIdentity.instance.id}"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    // Use Gson to parse the JSON array into a List<T>
                    val goalListType = object : TypeToken<List<GoalModel>>() {}.type
                    val goals = gson.fromJson<List<GoalModel>>(response, goalListType)

                    // Update the adapter with the new data
                    _listAdapter = MyGoalRecyclerViewAdapter(goals,
                    {
                        goalModel -> edit(goalModel)
                    }, {
                        id -> viewDetails(id)
                    },{
                        id-> delete(id)
                        }
                    )
                    binding.goalList.adapter = _listAdapter
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("GoalsFragment", "Error parsing JSON: ${e.message}")
                }
            },
            { error ->
                _messageHelper.showPositiveDialog("Error",error.toString())
                Log.e("GoalsFragment", "Volley error: ${error.message}")
            }
        )

        // Add the request to the RequestQueue
        _requestQueue.add(stringRequest)
    }
    private fun delete(id : Int){
        val url = "${KFitnessUrl.GOALS}?id=$id"

        val deleteRequest = StringRequest(
            Request.Method.DELETE, url,
            { _ -> fetchGoals()
            },
            { error ->
                _messageHelper.showPositiveDialog("Error",error.toString())
                Log.e("GoalsFragment", "Volley error: ${error.message}")
            }
        )
        _requestQueue.add(deleteRequest)
    }

}