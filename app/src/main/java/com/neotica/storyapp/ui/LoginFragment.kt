package com.neotica.storyapp.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.neotica.storyapp.databinding.FragmentLoginBinding
import com.neotica.storyapp.design.PasswordCustomView
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.models.LoginPreferences
import com.neotica.storyapp.ui.viewmodel.LoginViewModel
import com.neotica.storyapp.models.UserLogin
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var etEmail: EditText
    private lateinit var etPassword: PasswordCustomView
    private lateinit var btLogin: Button
    private lateinit var btRegister: Button
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: LoginViewModel by viewModel()

    private fun isValidEmail(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    private fun isValidPassword(str: String): Boolean {
        return str.length >= 6
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            etEmail = edLoginEmail
            etPassword = edLoginPassword
            btLogin = btnLogin
            btRegister = btnRegister
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        }
        btRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginToRegisterFragment()
            findNavController().navigate(action)
        }
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { showButton() }
            override fun afterTextChanged(s: Editable?) {}
        })
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { showButton() }
            override fun afterTextChanged(s: Editable?) {}
        })
        login()
    }

    private fun showButton() {
        binding.apply {
            btLogin.isEnabled =
                etPassword.text != null && isValidPassword(etPassword.text.toString()) &&
                        etEmail.text != null && isValidEmail(etEmail.text.toString())
        }
    }

    private fun navigateToMain(){
        val action = LoginFragmentDirections.actionLoginToMainFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun saveToken(loginResult: UserLogin) {
        Log.d("neotica","token saved.")
        val prefLogin = LoginPreferences(requireContext())
        prefLogin.setToken(loginResult.token)
        navigateToMain()
    }
    private fun showDialogError() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Login Error!")
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
        }
        builder.show()
    }

    private fun login() {
        binding.btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            viewModel.login(email, password)
        }
        viewModel.responseLogin.observe(viewLifecycleOwner) { login ->
            when (login) {
                is ApiResult.Success -> {
                    val loginResult = login.data
                    saveToken(loginResult)
                    Toast.makeText(context, "success login", Toast.LENGTH_SHORT).show()
                    //showLoading(false)
                    Log.d("Neotica", "Success login $etEmail")
                }
                is ApiResult.Error -> {
                    showDialogError()
                    // showLoading(false)
                    Log.d("Neotica", "error login ${etEmail.text} ${etPassword.text}")
                }
                is ApiResult.Loading -> {
                    // showLoading(true)
                    Log.d("Neotica", "loading login")
                }
                else -> {
                    Log.d("Neotica", "doesnt even go inside")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
