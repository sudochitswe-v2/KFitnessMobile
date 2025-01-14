package com.kmd.kfitness.unauthorized.register

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kmd.kfitness.R
import com.kmd.kfitness.databinding.FragmentLoginBinding
import com.kmd.kfitness.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private lateinit var navController : NavController
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = RegisterFragment()
    }

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBackToLogin.setOnClickListener {
            goToRegister();
        }
    }
    private fun goToRegister(){
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment,null,
            NavOptions.Builder()
                .setPopUpTo(R.id.unauthorized_navigation, true) // clear login and register fragments
                .setLaunchSingleTop(true)
                .build())
    }
}