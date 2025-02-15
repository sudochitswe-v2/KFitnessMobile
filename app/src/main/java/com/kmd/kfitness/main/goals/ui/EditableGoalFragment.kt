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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.kmd.kfitness.R
import com.kmd.kfitness.databinding.FragmentEditableGoalBinding
import com.kmd.kfitness.general.api.KFitnessUrl
import com.kmd.kfitness.general.helper.ApiErrorHandler
import com.kmd.kfitness.general.helper.GeneralHelper
import com.kmd.kfitness.general.helper.MessageHelper
import com.kmd.kfitness.general.identity.CustomHttpStack
import com.kmd.kfitness.general.identity.UserIdentity
import com.kmd.kfitness.main.goals.data.GoalModel
import java.util.Calendar


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
    private lateinit var apiErrorHandler: ApiErrorHandler
    private lateinit var goalStatus : Array<String>
    private var gson = GsonBuilder()
        .setDateFormat(GeneralHelper.K_DATE_FORMAT) // Set the desired date format
        .create()


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

         goalStatus =  resources.getStringArray(R.array.goalStatuses)

        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,goalStatus)
        binding.statusAutoTextView.setAdapter(arrayAdapter)

        // Set up status spinner

        if(isEditMode()){
            goalModel?.let { populateForm(it) }
        }

        binding.startDateInput.setOnClickListener { showDatePicker(binding.startDateInput) }
        binding.endDateInput.setOnClickListener { showDatePicker(binding.endDateInput) }

        binding.saveButton.setOnClickListener{
            saveGoal()
        }

        return binding.root;
    }
    @SuppressLint("SetTextI18n")
    private fun populateForm(goal: GoalModel) {
        // Example: Populate the form fields
        binding.descriptionTextInput.setText(goal.description)
        binding.targetTextInput.setText(goal.target.toString())
        binding.startDateInput.setText(GeneralHelper.kDateFormat.format(goal.startDate))
        binding.endDateInput.setText(GeneralHelper.kDateFormat.format(goal.endDate))

        val index = goalStatus.indexOf(goal.status)
        if(index != -1){
            binding.statusAutoTextView.setText(goalStatus[index],false)
        }
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

        val start  = GeneralHelper.kDateFormat.parse(binding.startDateInput.text.toString())
        val end  = GeneralHelper.kDateFormat.parse(binding.endDateInput.text.toString())

        return GoalModel(
            goalModel?.id ?: 0,
            UserIdentity.instance.id ?: 0,
            binding.descriptionTextInput.text.toString(),
            binding.targetTextInput.text.toString().toFloat(),
            start,
            end,
            binding.statusAutoTextView.text.toString()
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