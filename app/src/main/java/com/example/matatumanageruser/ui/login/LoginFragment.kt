package com.example.matatumanageruser.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.matatumanageradmin.ui.resetPassword.ResetPasswordDialog
import com.example.matatumanageruser.MatManagerUserApp
import com.example.matatumanageruser.R
import com.example.matatumanageruser.databinding.FragmentLoginBinding
import com.example.matatumanageruser.ui.dialogs.NoticeDialogFragment
import com.example.matatumanageruser.ui.other.showLongToast
import com.example.matatumanageruser.ui.other.stringFromTl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(), NoticeDialogFragment.NoticeDialogListener,
    ResetPasswordDialog.ResetPasswordDialogListener {

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
        onResetPasswordClicked()
        obsevePasswordReset()

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
                    openNoticeDialog("ok", it.errorText)
                    hideProgressBar()
                }
                is LoginViewModel.LoginStatus.Success -> {
                    (activity?.application as MatManagerUserApp).driverObject = it.driver
                    moveToDashboard()
                    hideProgressBar()
                    loginViewModel.setLoginStatusToEmpty()
                }
                is LoginViewModel.LoginStatus.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar(){
        loginBinding.progressBarLogin.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        loginBinding.progressBarLogin.visibility = View.VISIBLE
    }

    fun moveToDashboard(){
        this.findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
    }

    fun openNoticeDialog(positiveButton: String,  message: String){
        val dialog = NoticeDialogFragment(positiveButton, message)
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Error")

    }


    private fun obsevePasswordReset() {
        loginViewModel.resetPasswordStatus.observe(viewLifecycleOwner, {
            when(it){
                is LoginViewModel.LoginStatus.Failed -> {
                    hideProgressBar()
                    openNoticeDialog("ok", it.errorText)
                }
                is LoginViewModel.LoginStatus.Success -> {
                    hideProgressBar()
                    openNoticeDialog("ok", it.resultText)

                }

                is LoginViewModel.LoginStatus.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun onResetPasswordClicked() {
        loginBinding.forgotPassword.setOnClickListener {
            openResetPasswordDialog()
        }
    }

    fun openResetPasswordDialog(){
        val dialog = ResetPasswordDialog()
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Reset Password")

    }

    override fun onSaveButtonClicked(email: String) {
        loginViewModel.resetPassword(email)
    }
}