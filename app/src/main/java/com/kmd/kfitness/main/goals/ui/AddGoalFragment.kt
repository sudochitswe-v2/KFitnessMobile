package com.kmd.kfitness.main.goals.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.android.volley.RequestQueue
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.kmd.kfitness.databinding.FragmentAddGoalBinding
import com.kmd.kfitness.general.helper.ApiErrorHandler
import com.kmd.kfitness.general.helper.GeneralHelper
import com.kmd.kfitness.general.helper.MessageHelper
import com.kmd.kfitness.main.goals.data.GoalModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


/**
 * A simple [Fragment] subclass.
 * Use the [AddGoalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddGoalFragment : Fragment() {

    private var _binding: FragmentAddGoalBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()

    private lateinit var requestQueue: RequestQueue
    private lateinit var messageHelper: MessageHelper
    private var gson = Gson()


    private lateinit var descriptionInput: TextInputEditText
    private lateinit var targetInput: TextInputEditText
    private lateinit var startDateInput: TextInputEditText
    private lateinit var endDateInput: TextInputEditText
    private lateinit var statusSpinner: Spinner

    private lateinit var apiErrorHandler: ApiErrorHandler

    private var goalModel: GoalModel? = null

    private fun isEditMode(): Boolean {
        return goalModel != null
    }
    private fun setAppBarTitle() {
        // Set the title dynamically based on edit mode
        val title = if (isEditMode()) "Edit Goal" else "Add Goal"
//        findNavController().currentDestination?.label = title
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = title

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goalModel = arguments?.getSerializable("goalModel") as? GoalModel
        setAppBarTitle()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddGoalBinding.inflate(inflater,container,false)
        descriptionInput = binding.descriptionTextInput
        targetInput = binding.targetTextInput
        startDateInput = binding.startDateInput
        endDateInput = binding.endDateInput
        statusSpinner = binding.statusSpinner

        // Set up status spinner

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, GeneralHelper.statusValues())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = adapter

        if(isEditMode()){
            goalModel?.let { populateForm(it) }
        }

        startDateInput.setOnClickListener { showDatePicker(startDateInput) }
        endDateInput.setOnClickListener { showDatePicker(endDateInput) }

        binding.saveButton.setOnClickListener{ }

        return binding.root;
    }
    @SuppressLint("SetTextI18n")
    private fun populateForm(goal: GoalModel) {
        // Example: Populate the form fields
        descriptionInput.setText(goal.description)
        targetInput.setText(goal.target.toString())
        startDateInput.setText(GeneralHelper.kDateFormat.format(goal.startDate))
        endDateInput.setText(GeneralHelper.kDateFormat.format(goal.endDate))

        // Set the selected status in the spinner
        val statusPosition = when (goal.status) {
            "in_progress" -> 0
            "completed" -> 1
            else -> 0
        }
        statusSpinner.setSelection(statusPosition)
    }
    private fun showDatePicker(textInput: TextInputEditText) {
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                textInput.setText(GeneralHelper.kDateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

}