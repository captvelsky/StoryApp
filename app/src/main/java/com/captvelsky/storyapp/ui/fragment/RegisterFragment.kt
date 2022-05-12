package com.captvelsky.storyapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.captvelsky.storyapp.R
import com.captvelsky.storyapp.databinding.FragmentRegisterBinding
import com.captvelsky.storyapp.ui.model.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnRegister.setOnClickListener {
                registerAuth()
            }
            btnLogin.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_registerFragment_to_loginFragment)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun registerAuth() {
        showLoading(true)
        val name = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        viewModel.viewModelScope.launch {
            viewModel.userRegister(name, email, password).collect { result ->
                result.onSuccess {
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                when {
                    name.isEmpty() -> {
                        result.onFailure {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.username_warning),
                                Toast.LENGTH_SHORT
                            ).show()
                            showLoading(false)
                        }
                    }
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
                                getString(R.string.register_input_warning),
                                Toast.LENGTH_SHORT
                            ).show()
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}