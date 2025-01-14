package com.kmd.kfitness.unauthorized.login.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.kmd.kfitness.MainActivity
import com.kmd.kfitness.R
import com.kmd.kfitness.databinding.FragmentLoginBinding
import com.kmd.kfitness.general.api.KFitnessUrl
import com.kmd.kfitness.general.constant.ConstantKey
import com.kmd.kfitness.general.error.ApiError
import com.kmd.kfitness.general.identity.UserIdentity
import com.kmd.kfitness.general.helper.MessageHelper
import com.kmd.kfitness.general.helper.SharedPrefHelper
import com.kmd.kfitness.unauthorized.login.data.model.LoggedInUser
import com.kmd.kfitness.unauthorized.login.data.model.LoginRequest
import java.nio.charset.Charset


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!
    private val gson = Gson()

    private lateinit var _requestQueue: RequestQueue
    private lateinit var _messageHelper: MessageHelper
    private lateinit var _sharedPref: SharedPrefHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        _requestQueue = Volley.newRequestQueue(requireContext())
        _messageHelper = MessageHelper(requireContext())
        _sharedPref = SharedPrefHelper(requireContext())
        checkUserAuthAndAutoLogin()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnGoRegister.setOnClickListener {
            goToRegister()
        }
        super.onViewCreated(view, savedInstanceState)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login


        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        loginButton.setOnClickListener {
            login()
        }
    }
   private val afterTextChangedListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // ignore
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // ignore
        }

        override fun afterTextChanged(s: Editable) {
            validateForm()
        }
    }
    private fun login() {
        val loginRequest = LoginRequest(
            binding.username.text.toString(),
            binding.password.text.toString()
        )
        val request = object: StringRequest(
            Method.POST,
            KFitnessUrl.login,
            {
                response -> onLoginOk(response)
            },
            {
                error -> onLoginError(error)
            }
        ){
            override fun getBody(): ByteArray {
               return gson.toJson(loginRequest).toByteArray()
            }
        }
        _requestQueue.add(request)

    }
    private fun checkUserAuthAndAutoLogin(){
      val userData = _sharedPref.getData(ConstantKey.loggedInUser,"")
        if(userData!= ""){
            setIdentity(userData)
            replaceWithMainActivity()
        }
    }
    private fun replaceWithMainActivity(){
        val intent = Intent(requireActivity(),MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()

    }
    private fun onLoginOk(response : String){
        saveLoggedIn(response)
        setIdentity(response)
        replaceWithMainActivity()
    }
    private fun saveLoggedIn(response: String){
        _sharedPref.saveData(ConstantKey.loggedInUser,response)
    }
    private fun setIdentity(response: String){
        val claim = gson.fromJson(response,LoggedInUser::class.java)
        UserIdentity.instance.init(claim)
    }

    private fun onLoginError(error: VolleyError){
        val errMsg = error.networkResponse.data.toString(Charset.defaultCharset())
       val data =  gson.fromJson(errMsg,ApiError::class.java)
       _messageHelper.showPositiveDialog("Error",data.error);
    }

    private fun validateForm() {
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()

        // Perform validation
        val usernameError = if (username.isBlank()) "invalid email" else null
        val passwordError = if (password.isBlank()) "invalid password" else null

        // Update UI based on validation
        binding.username.error = usernameError
        binding.password.error = passwordError

        // Enable/disable login button based on form validity
        binding.login.isEnabled = usernameError == null && passwordError == null
    }

    private fun goToRegister(){
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment,null,
             NavOptions.Builder()
                .setPopUpTo(R.id.unauthorized_navigation, true) // clear login and register fragments
                .setLaunchSingleTop(true)
                .build())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}