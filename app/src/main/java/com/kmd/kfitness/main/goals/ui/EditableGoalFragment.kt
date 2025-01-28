package com.kmd.kfitness.main.goals.ui

import android.R
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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kmd.kfitness.databinding.FragmentEditableGoalBinding
import com.kmd.kfitness.general.api.KFitnessUrl
import com.kmd.kfitness.general.helper.ApiErrorHandler
import com.kmd.kfitness.general.helper.GeneralHelper
import com.kmd.kfitness.general.helper.MessageHelper
import com.kmd.kfitness.general.identity.CustomHttpStack
import com.kmd.kfitness.general.identity.UserIdentity
import com.kmd.kfitness.main.goals.data.GoalModel
import com.kmd.kfitness.unauthorized.register.data.UserRegisterModel
import java.time.LocalDate
import java.util.Calendar
import java.util.Date


/**
 * A simple [Fragment] subclass.
 * Use the [EditableGoalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditableGoalFragment : Fragment() {

    private var _binding: FragmentEditableGoalBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()

    private lateinit var requestQueue: RequestQueue
    private lateinit var messageHelper: MessageHelper
    private var gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd") // Set the desired date format
        .create()


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
        requestQueue = Volley.newRequestQueue(requireContext(),CustomHttpStack())
        messageHelper = MessageHelper(requireContext())
        apiErrorHandler = ApiErrorHandler(messageHelper,gson)
        goalModel = arguments?.getSerializable("goalModel") as? GoalModel
        setAppBarTitle()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditableGoalBinding.inflate(inflater,container,false)
        descriptionInput = binding.descriptionTextInput
        targetInput = binding.targetTextInput
        startDateInput = binding.startDateInput
        endDateInput = binding.endDateInput
        statusSpinner = binding.statusSpinner

        // Set up status spinner

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, GeneralHelper.statusValues())
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = adapter

        if(isEditMode()){
            goalModel?.let { populateForm(it) }
        }

        startDateInput.setOnClickListener { showDatePicker(startDateInput) }
        endDateInput.setOnClickListener { showDatePicker(endDateInput) }

        binding.saveButton.setOnClickListener{
            saveGoal()
        }

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
    private fun  collectFormData() : GoalModel{

        val start  = GeneralHelper.kDateFormat.parse(startDateInput.text.toString())
        val end  = GeneralHelper.kDateFormat.parse(endDateInput.text.toString())

        return GoalModel(
            goalModel?.id ?: 0,
            UserIdentity.instance.id ?: 0,
            descriptionInput.text.toString(),
            targetInput.text.toString().toFloat(),
            start,
            end,
            statusSpinner.selectedItem.toString()
        )
    }
    private fun saveGoal(){
        try {
            val method = if(isEditMode()) Request.Method.PUT else Request.Method.POST
            val msg = if(isEditMode()) "Successfully updated" else "Successfully saved"
            val data = collectFormData()
            val request = object: StringRequest(
                method,
                KFitnessUrl.GOALS,
                {
                    _ ->  messageHelper
                    .showPositiveDialog("Success",msg, onPositiveButtonClick = {
                        findNavController().popBackStack()
                    }) // Status 200 with no other response
                },
                {
                        error -> apiErrorHandler.onVolleyErrorShowSimpleDialog(error)
                }
            ){
                override fun getBody(): ByteArray {
                    return gson.toJson(data).toByteArray()
                }
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            requestQueue.add(request)
        }catch (e : Exception){
            messageHelper.showShortToast(e.message.toString())
        }
    }


}