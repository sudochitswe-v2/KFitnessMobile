package com.kmd.kfitness.unauthorized.register.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.kmd.kfitness.R
import com.kmd.kfitness.databinding.FragmentRegisterBinding
import com.kmd.kfitness.general.api.KFitnessUrl
import com.kmd.kfitness.general.common.KTextWatcher
import com.kmd.kfitness.general.helper.ApiErrorHandler
import com.kmd.kfitness.general.helper.MessageHelper
import com.kmd.kfitness.unauthorized.register.data.UserRegisterModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var requestQueue: RequestQueue
    private lateinit var messageHelper: MessageHelper
    private var gson = Gson()

    private lateinit var apiErrorHandler: ApiErrorHandler

    private lateinit var fullNameEditText : TextInputEditText
    private lateinit var emailEditText : TextInputEditText
    private lateinit var passwordEditText : TextInputEditText
    private lateinit var confirmPasswordTextEdit : TextInputEditText
    private lateinit var registerButton : Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        requestQueue = Volley.newRequestQueue(requireContext())
        messageHelper = MessageHelper(requireContext())
        apiErrorHandler = ApiErrorHandler(messageHelper,gson)

        fullNameEditText = binding.fullNameTextInput
        emailEditText = binding.emailTextInput
        passwordEditText = binding.passwordTextInput
        confirmPasswordTextEdit = binding.confirmPasswordTextInput
        registerButton = binding.btnRegister
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBackToLogin.setOnClickListener {
            goToLogin()
        }
        fullNameEditText.addTextChangedListener(KTextWatcher.createAfterTextChangedListener { validateForm() })
        emailEditText.addTextChangedListener(KTextWatcher.createAfterTextChangedListener { validateForm() })
        passwordEditText.addTextChangedListener(KTextWatcher.createAfterTextChangedListener { validateForm() })
        confirmPasswordTextEdit.addTextChangedListener(KTextWatcher.createAfterTextChangedListener { validateForm() })
        registerButton.setOnClickListener{
            register()
        }
    }

    private fun validateForm() {
        fullNameEditText.error = checkFullNameError()
        emailEditText.error = checkEmailError()
        passwordEditText.error = checkPasswordError()
        confirmPasswordTextEdit.error = checkConfirmPasswordError()
        checkFormIsValid()
    }
    private fun checkFullNameError () : String? {
       return if (fullNameEditText.text.toString().isBlank()) "Full name is required" else null
    }
    private fun checkEmailError() : String?{
        val value = emailEditText.text.toString()
        return if(value.isBlank()){
            "Email is required"
        }else if (!value.contains("@")){
            "Invalid email"
        }else{
            null
        }
    }
    private fun checkPasswordError() : String?{
        val value = passwordEditText.text.toString()
        return if(value.isBlank()){
            "Password is required"
        }else if (value.length < 6){
            "Password should have at least 6 character long"
        }
        else{
            null
        }
    }
    private fun checkConfirmPasswordError() : String?{
        val value = confirmPasswordTextEdit.text.toString()
        val password = passwordEditText.text.toString()
        return if(value.isBlank()){
            "Confirm password is require"
        } else if (value != password){
            "Confirm password doesn't match with password"
        }else{
            null
        }
    }
    private fun checkFormIsValid(){
        val isEnabled =
                fullNameEditText.error == null &&
                emailEditText.error == null &&
                passwordEditText.error == null &&
                confirmPasswordTextEdit.error == null
                registerButton.isEnabled = isEnabled
    }
    private fun register(){
        try {
            val registerModel = UserRegisterModel(
                fullNameEditText.text.toString(),
                emailEditText.text.toString(),
                passwordEditText.text.toString(),
            )
            val request = object: StringRequest(
                Method.POST,
                KFitnessUrl.REGISTER,
                {
                        _ -> onRegisterSuccess() // Status 200 with no other response
                },
                {
                        error -> apiErrorHandler.onVolleyErrorShowSimpleDialog(error)
                }
            ){
                override fun getBody(): ByteArray {
                    return gson.toJson(registerModel).toByteArray()
                }
            }
            requestQueue.add(request)
        }catch (e : Exception){
            messageHelper.showShortToast(e.message.toString())
        }
    }
    private fun onRegisterSuccess(){
        messageHelper.showPositiveDialog("Success","Account created. Please login.","Go to Login") {
            goToLogin()
        }
    }
    private fun goToLogin(){
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment,null,
            NavOptions.Builder()
                .setPopUpTo(R.id.unauthorized_navigation, true) // clear login and register fragments
                .setLaunchSingleTop(true)
                .build())
    }
}