package com.example.userapp.fragments.add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.model.User
import com.example.userapp.viewmodel.UserViewModel
import com.example.userapp.databinding.FragmentAddBinding
import com.example.userapp.model.Name

class Addfragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    lateinit var binding: FragmentAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.btnAdd.setOnClickListener {
            insertDataToDatabase()
        }

        return binding.root
    }

    private fun insertDataToDatabase() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val age = binding.etAge.text
        val address = binding.etAddress.text.toString()

        if (inputCheck(firstName, lastName, age, address)) {
            // Create user object
            val name = Name(firstName, lastName)
            val user = User(0, name, age.toString().toInt(), address)
            // Add data to database
            userViewModel.addUser(user)
            Toast.makeText(context, "Successfully added!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addfragment_to_listFragment)
        } else {
            Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(
        firstName: String,
        lastName: String,
        age: Editable,
        address: String
    ): Boolean {
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty() && TextUtils.isEmpty(
            address
        ))
    }

}