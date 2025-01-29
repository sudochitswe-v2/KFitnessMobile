package com.kmd.kfitness.main.goals.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.kmd.kfitness.databinding.FragmentEditableProgressBinding
import com.kmd.kfitness.general.api.KFitnessUrl
import com.kmd.kfitness.general.helper.ApiErrorHandler
import com.kmd.kfitness.general.helper.GeneralHelper
import com.kmd.kfitness.general.helper.MessageHelper
import com.kmd.kfitness.general.identity.CustomHttpStack
import com.kmd.kfitness.general.identity.UserIdentity
import com.kmd.kfitness.main.goals.data.GoalModel
import com.kmd.kfitness.main.goals.data.ProgressModel
import java.util.Calendar


class EditableProgressFragment : Fragment() {

    private var _binding: FragmentEditableProgressBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()

    private lateinit var requestQueue: RequestQueue
    private lateinit var messageHelper: MessageHelper
    private var gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd") // Set the desired date format
        .create()
    private lateinit var apiErrorHandler: ApiErrorHandler

    private var progressModel: ProgressModel? = null
    private  var goalId : Int = 0

    private fun isEditMode(): Boolean {
        return progressModel != null
    }
    private fun setAppBarTitle() {
        // Set the title dynamically based on edit mode
        val title = if (isEditMode()) "Edit Progress" else "Add Progress"
//        findNavController().currentDestination?.label = title
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = title

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressModel = arguments?.getSerializable("progressModel")  as? ProgressModel
        goalId = arguments?.getInt("goal_id") as Int
        requestQueue = Volley.newRequestQueue(requireContext(), CustomHttpStack())
        messageHelper = MessageHelper(requireContext())
        apiErrorHandler = ApiErrorHandler(messageHelper,gson)
        setAppBarTitle()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditableProgressBinding.inflate(layoutInflater,container,false)

        if(isEditMode()){
            progressModel?.let { populateForm(it) }
        }
        binding.saveButton.setOnClickListener {
            saveChanges()
        }
        binding.dateInput.setOnClickListener{
            showDatePicker(binding.dateInput)
        }
       return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun populateForm(progressModel: ProgressModel){
        binding.notesTextInput.setText(progressModel.notes)
        binding.dateInput.setText(GeneralHelper.kDateFormat.format(progressModel.date))
        binding.currentValueTextInput.setText(progressModel.currentValue.toString())
    }
    private fun collectFormData() : ProgressModel{

        val date  = GeneralHelper.kDateFormat.parse(binding.dateInput.text.toString())

        return ProgressModel(
          progressModel?.id?:0,
            goalId,
            date,
            binding.currentValueTextInput.text.toString().toFloat(),
            binding.notesTextInput.text.toString()
        )
    }

    private fun showDatePicker(textInput: TextInputEditText){
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
    private fun saveChanges(){
        try {
            val method = if(isEditMode()) Request.Method.PUT else Request.Method.POST
            val msg = if(isEditMode()) "Successfully updated" else "Successfully saved"
            val data = collectFormData()
            val request = object: StringRequest(
                method,
                KFitnessUrl.PROGRESSES,
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