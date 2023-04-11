package com.neotica.storyapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.neotica.storyapp.databinding.FragmentLoginBinding
import com.neotica.storyapp.design.PasswordCustomView
import com.neotica.storyapp.ui.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    val TOKEN = "token"
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var etEmail : EditText
    private lateinit var etPassword : PasswordCustomView
    private lateinit var btLogin : Button
    private lateinit var btRegister : Button
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel : LoginViewModel by viewModel()

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
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
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
        etEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        etPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun showButton(){
        binding.apply {
            btLogin.isEnabled =
                etPassword.text != null && isValidPassword(etPassword.text.toString()) &&
                        etEmail.text != null && isValidEmail(etEmail.text.toString())
        }
    }
}