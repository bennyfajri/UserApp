package com.example.userapp.fragments.update

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
import androidx.navigation.fragment.navArgs
import com.example.userapp.R
import com.example.userapp.databinding.FragmentUpdateBinding
import com.example.userapp.model.User
import com.example.userapp.viewmodel.UserViewModel

class UpdateFragment : Fragment() {

    lateinit var binding: FragmentUpdateBinding
    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.etFirstName.setText(args.currentUser.firstName)
        binding.etLastName.setText(args.currentUser.lastName)
        binding.etAge.setText(args.currentUser.age.toString())

        binding.btnUpdate.setOnClickListener {
            updateItem()
        }

        return binding.root
    }

    private fun updateItem() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val age = binding.etAge.text

        if(inputCheck(firstName, lastName, age)){
            // Create user object
            val updateUser = User(args.currentUser.id,firstName, lastName, age.toString().toInt())
            // Update current user
            userViewModel.updateUser(updateUser)
            Toast.makeText(context, "Updated successfully!", Toast.LENGTH_SHORT).show()
            //Navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }

}