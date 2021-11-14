package com.example.matatumanageruser.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.matatumanageruser.R
import com.example.matatumanageruser.databinding.FragmentLoginBinding
import com.example.matatumanageruser.ui.other.stringFromTl


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

                }
                is LoginViewModel.LoginStatus.Success -> {

                }
            }
        })
    }
}