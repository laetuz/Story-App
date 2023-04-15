package com.neotica.storyapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.neotica.storyapp.databinding.FragmentRegisterBinding
import com.neotica.storyapp.design.PasswordCustomView
import com.neotica.storyapp.ui.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var etName : EditText
    private lateinit var etEmail : EditText
    private lateinit var etPassword : PasswordCustomView
    private lateinit var registerButton : Button
    private val viewModel: RegisterViewModel by viewModel()

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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            etName = edRegisterName
            etEmail = edRegisterEmail
            etPassword = edRegisterPassword
            registerButton = btnRegister
        }
        etName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        etEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        etPassword.addTextChangedListener(object  : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        registerButton.setOnClickListener {
            binding.apply {
                val etName = edRegisterName.text.toString()
                val etEmail = edRegisterEmail.text.toString()
                val etPass = edRegisterPassword.text.toString()
                viewModel.signup(etName, etEmail, etPass)
                viewModel.success.observe(viewLifecycleOwner){
                    if (it){
                        Toast.makeText(context, "User Registered.", Toast.LENGTH_SHORT).show()
                        onDestroy()
                    } else {
                        Toast.makeText(context, "Failed to register user.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showButton(){
        binding.apply {
            registerButton.isEnabled =
                etPassword.text !=null && isValidPassword(etPassword.text.toString()) &&
                        etEmail.text != null && isValidEmail(etEmail.text.toString()) &&
                        etName.text.isNotEmpty()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}