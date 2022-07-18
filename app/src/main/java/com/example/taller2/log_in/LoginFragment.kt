package com.example.taller2.log_in

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.taller2.movie_list.tabs
import com.example.taller2.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class login : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vtv = inflater.inflate(R.layout.fragment_login, container, false)

        val user = vtv.findViewById<EditText>(R.id.main_user)
        val pass = vtv.findViewById<EditText>(R.id.main_password)
        val btn_login = vtv.findViewById<Button>(R.id.main_btn_login)

        viewModel.loginState.observe(viewLifecycleOwner, Observer<LoginState> {
            when(it){
                is LoginState.Error -> {
                    Toast.makeText(requireContext(), "Invalid user or password", Toast.LENGTH_SHORT).show()
                    user.setText("")
                    pass.setText("")
                }
                is LoginState.Success -> {
                    val fragment = tabs()
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.main, fragment)
                    transaction.addToBackStack(tabs::class.java.canonicalName)
                    transaction.commit()
                }
            }
        })

        btn_login.setOnClickListener {
            viewModel.validateUser(user.text.toString(), pass.text.toString())
        }

        return vtv
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            login().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}