package com.neotica.storyapp.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.neotica.storyapp.databinding.FragmentRegisterBinding
import com.neotica.storyapp.design.PasswordCustomView
import com.neotica.storyapp.ui.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: PasswordCustomView
    private lateinit var registerButton: Button
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        registerButton.isEnabled = false
        etName.addTextChangedListener(object : TextWatcher {
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
        etEmail.addTextChangedListener(object : TextWatcher {
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
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        registerButton.setOnClickListener {
            showLoading(true)
            binding.apply {
                val etName = edRegisterName.text.toString()
                val etEmail = edRegisterEmail.text.toString()
                val etPass = edRegisterPassword.text.toString()
                viewModel.signup(etName, etEmail, etPass)
                viewModel.success.observe(viewLifecycleOwner) {
                    if (it) {
                        showLoading(false)
                        Toast.makeText(context, "User Registered.", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        showLoading(false)
                        Toast.makeText(context, "Failed to register the user.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        showAnimation()
    }

    private fun isValidEmail(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    private fun isValidPassword(str: String): Boolean {
        return str.length >= 8
    }

    private fun showButton() {
        binding.apply {
            registerButton.isEnabled =
                etPassword.text != null && isValidPassword(etPassword.text.toString()) &&
                        etEmail.text != null && isValidEmail(etEmail.text.toString()) &&
                        etName.text.isNotEmpty()
        }
    }

    private fun showAnimation() {
        ObjectAnimator.ofFloat(binding.ivRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}