package com.kmd.kfitness.main.workouts.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kmd.kfitness.R
import com.kmd.kfitness.databinding.FragmentEditableWorkoutBinding
import com.kmd.kfitness.databinding.FragmentWorkoutsBinding
import com.kmd.kfitness.general.api.KFitnessUrl
import com.kmd.kfitness.general.helper.ApiErrorHandler
import com.kmd.kfitness.general.helper.GeneralHelper
import com.kmd.kfitness.general.helper.MessageHelper
import com.kmd.kfitness.general.identity.CustomHttpStack
import com.kmd.kfitness.general.identity.UserIdentity
import com.kmd.kfitness.main.workouts.adapter.ActivityAdapter
import com.kmd.kfitness.main.workouts.adapter.MyWorkoutRecyclerViewAdapter
import com.kmd.kfitness.main.workouts.data.ActivityModel
import com.kmd.kfitness.main.workouts.data.EditableWorkoutModel
import com.kmd.kfitness.main.workouts.data.WorkoutModel
import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 * Use the [EditableWorkoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditableWorkoutFragment : Fragment() {

    private var _binding: FragmentEditableWorkoutBinding? = null

    private val binding get() = _binding!!

    private val gson = GsonBuilder()
        .serializeNulls()
        .setDateFormat(GeneralHelper.K_DATE_FORMAT).create()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var goalStatus: Array<String>
    private lateinit var requestQueue: RequestQueue
    private lateinit var messageHelper: MessageHelper

    private lateinit var apiErrorHandler: ApiErrorHandler
    private lateinit var activities : List<ActivityModel>
    private var workoutModel :EditableWorkoutModel? = null
    private var selectedActivity: ActivityModel? = null
    private val calendar = Calendar.getInstance()

    private var isStartLocationRequest = true  // Default to start location

    private fun isEditMode() :Boolean {
        return workoutModel != null
    }

    private fun setAppBarTitle() {
        // Set the title dynamically based on edit mode
        val title = if (isEditMode()) "Edit Workout" else "Add Workout"
//        findNavController().currentDestination?.label = title
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = title

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestQueue = Volley.newRequestQueue(requireContext(), CustomHttpStack())
        messageHelper = MessageHelper(requireContext())
        apiErrorHandler = ApiErrorHandler(messageHelper,gson)
        goalStatus = resources.getStringArray(R.array.goalStatuses)
        val data = arguments?.getString("workoutModel")
        workoutModel =  gson.fromJson(data,EditableWorkoutModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        setAppBarTitle()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation(isStartLocationRequest)
            } else {
                messageHelper
                    .showPositiveDialog("Permission denied","Permission is required to use this feature.")
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditableWorkoutBinding.inflate(layoutInflater,container,false)
        binding.saveButton.setOnClickListener { saveChanges() }
        binding.activityAutoText.setOnItemClickListener { _, _, position, _ ->
            selectedActivity = activities[position]
        }
        if(isEditMode()){
            binding.startButton.isEnabled =true
            binding.finishButton.isEnabled =true
            binding.startButton.setOnClickListener {
                isStartLocationRequest = true
                getCurrentLocation(isStartLocationRequest)
            }
            binding.finishButton.setOnClickListener {
                isStartLocationRequest =false
                getCurrentLocation(isStartLocationRequest)
            }
        }
        binding.dateInputText.setOnClickListener{ showDatePicker(binding.dateInputText) }
        binding.statusAutoTextView.setAdapter(ArrayAdapter(requireContext(),R.layout.dropdown_item,goalStatus))
        fetchActivities().apply {
            if(isEditMode()){
                workoutModel?.let { populateForm(it) }
            }
        }
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    private fun getCurrentLocation(isStart : Boolean) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                if(isStart){
                    binding.startLatitudeTextView.text = latitude.toString()
                    binding.startLongitudeTextView.text = longitude.toString()
                }else{
                    binding.endLatitudeTextView.text = latitude.toString()
                    binding.endLongitudeTextView.text = latitude.toString()
                }
            } else {
                messageHelper.showPositiveDialog("Failed to retrieve location","")
            }
        }.addOnFailureListener {
            messageHelper.showPositiveDialog("Error getting location",it.message)
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
    private fun populateForm(workoutModel: EditableWorkoutModel){
        binding.dateInputText.setText(GeneralHelper.kDateFormat.format(workoutModel.date))
        binding.setsTextInput.setText(workoutModel.sets?.toString() ?:"")
        binding.repsTextInput.setText(workoutModel.repetitions?.toString() ?: "")
        binding.durationTextInput.setText(workoutModel.duration.toString())
        binding.weightTextInput.setText(workoutModel.weight?.toString()?: "")
        binding.distanceTextInput.setText(workoutModel.distance?.toString() ?:"")
        val index = goalStatus.indexOf(workoutModel.status)
        if(index != -1){
            binding.statusAutoTextView.setText(goalStatus[index],false)
        }
        binding.startLatitudeTextView.text = workoutModel.startLatitude?: LAT
        binding.startLongitudeTextView.text = workoutModel.startLatitude?: LONG
        binding.endLatitudeTextView.text = workoutModel.endLatitude?: LAT
        binding.endLongitudeTextView.text = workoutModel.endLongitude?: LONG

    }
    private fun collectFormData() : EditableWorkoutModel {
        return EditableWorkoutModel(
            workoutModel?.id?:0,
            UserIdentity.instance.id as Int,
            selectedActivity?.id?:0,
            GeneralHelper.kDateFormat.parse(binding.dateInputText.text.toString()),
            binding.durationTextInput.text.toString().toInt(),
            binding.distanceTextInput.text.toString().toFloatOrNull(),
            binding.weightTextInput.text.toString().toFloatOrNull(),
            binding.repsTextInput.text.toString().toIntOrNull(),
            binding.setsTextInput.text.toString().toIntOrNull(),
            binding.statusAutoTextView.text.toString(),
            binding.startLatitudeTextView.text.toString().replace(LAT,""),
            binding.startLongitudeTextView.text.toString().replace(LONG,""),
            binding.endLatitudeTextView.text.toString().replace(LAT,""),
            binding.endLongitudeTextView.text.toString().replace(LONG,""),
        )
    }
    private fun saveChanges(){
        try {
            val method = if(isEditMode()) Request.Method.PUT else Request.Method.POST
            val msg = if(isEditMode()) "Successfully updated" else "Successfully saved"
            val data = collectFormData()
            val request = object: StringRequest(
                method,
                KFitnessUrl.WORKOUTS,
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
                    var json = gson.toJson(data)
                    Log.e("Test",json)
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
    private fun fetchActivities(){
        val url = KFitnessUrl.ACTIVITIES

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    // Use Gson to parse the JSON array into a List<T>
                    val activitiesListType = object : TypeToken<List<ActivityModel>>() {}.type
                    activities = gson.fromJson<List<ActivityModel>>(response, activitiesListType)

                    val adapter = ActivityAdapter(requireContext(),activities)
                    binding.activityAutoText.setAdapter(adapter)

                    if(isEditMode()){
                        selectedActivity = activities.find { it.id == workoutModel?.activityId }
                        if(selectedActivity != null){
                            binding.activityAutoText.setText(selectedActivity?.name, false)
                        }
                    }

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
    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 1001
        private const val LAT = "Latitude: -"
        private const val LONG = "Longitude: -"
    }
}