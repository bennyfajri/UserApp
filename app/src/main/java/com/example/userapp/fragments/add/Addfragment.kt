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
import com.example.userapp.data.User
import com.example.userapp.data.UserViewModel
import com.example.userapp.databinding.FragmentAddBinding

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

        if(inputCheck(firstName, lastName, age)){
            // Create user object
            val user = User(0, firstName, lastName, age.toString().toInt())
            // Add data to database
            userViewModel.addUser(user)
            Toast.makeText(context, "Successfully added!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addfragment_to_listFragment)
        }else{
            Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())


    }

}