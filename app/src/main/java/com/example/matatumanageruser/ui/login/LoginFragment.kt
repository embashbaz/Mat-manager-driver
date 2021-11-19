package com.example.matatumanageruser.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.matatumanageruser.MatManagerUserApp
import com.example.matatumanageruser.R
import com.example.matatumanageruser.databinding.FragmentLoginBinding
import com.example.matatumanageruser.ui.other.showLongToast
import com.example.matatumanageruser.ui.other.stringFromTl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = loginBinding.root

        login()
        observeLoginStatus()

        return view
    }

    fun login(){
        loginBinding.loginButton.setOnClickListener {
            loginViewModel.loginMethod(
                stringFromTl(loginBinding.emailLogin),
                stringFromTl(loginBinding.passwordLogin)
            )
        }
        }

    fun observeLoginStatus(){
        loginViewModel.loginStatus.observe(viewLifecycleOwner, {
            when(it){
                is LoginViewModel.LoginStatus.Failed -> {
                    showLongToast(it.errorText)
                }
                is LoginViewModel.LoginStatus.Success -> {
                    (activity?.application as MatManagerUserApp).driverObject = it.driver
                    moveToDashboard()
                }
            }
        })
    }

    fun moveToDashboard(){
        this.findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
    }
}