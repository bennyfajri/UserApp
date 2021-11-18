package com.example.userapp.fragments.update

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.userapp.R
import com.example.userapp.databinding.FragmentUpdateBinding
import com.example.userapp.model.Name
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

        binding.etFirstName.setText(args.currentUser.name.firstName)
        binding.etLastName.setText(args.currentUser.name.lastName)
        binding.etAge.setText(args.currentUser.age.toString())

        binding.btnUpdate.setOnClickListener {
            updateItem()
        }

        // Add menu
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun updateItem() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val age = binding.etAge.text
        val address = binding.etAddress.text.toString()

        if(inputCheck(firstName, lastName, age)){
            // Create user object
                val name = Name(firstName, lastName)
            val updateUser = User(args.currentUser.id,name, age.toString().toInt(), address)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete){
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        AlertDialog.Builder(context)
            .setTitle("Delete ${args.currentUser.name.firstName}")
            .setMessage("Are you sure to delete ${args.currentUser.name.firstName}?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                userViewModel.deleteUser(args.currentUser)
                Toast.makeText(
                    context,
                    "Sucessfully removed: ${args.currentUser.name.firstName}",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            .show()
    }

}