package com.captvelsky.storyapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.captvelsky.storyapp.R
import com.captvelsky.storyapp.databinding.FragmentLoginBinding
import com.captvelsky.storyapp.ui.activity.HomeActivity
import com.captvelsky.storyapp.ui.activity.HomeActivity.Companion.EXTRA_TOKEN
import com.captvelsky.storyapp.ui.model.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnLogin.setOnClickListener {
                loginAuth()
            }
            btnRegister.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun loginAuth() {
        showLoading(true)
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        viewModel.viewModelScope.launch {
            viewModel.userLogin(email, password).collect { result ->
                result.onSuccess { credentials ->
                    credentials.loginResult.token.let { token ->
                        viewModel.saveAuthToken(token)
                        Intent(requireContext(), HomeActivity::class.java).also { intent ->
                            intent.putExtra(EXTRA_TOKEN, token)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    }
                }
                when {
                    email.isEmpty() -> {
                        result.onFailure {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.email_input_warning),
                                Toast.LENGTH_SHORT
                            ).show()
                            showLoading(false)
                        }
                    }
                    password.isEmpty() -> {
                        result.onFailure {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.password_input_warning),
                                Toast.LENGTH_SHORT
                            ).show()
                            showLoading(false)
                        }
                    }
                    else -> {
                        result.onFailure {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.login_input_warning),
                                Toast.LENGTH_SHORT
                            ).show()
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}