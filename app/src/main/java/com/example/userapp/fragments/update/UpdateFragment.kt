package com.example.userapp.fragments.update

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.userapp.R
import com.example.userapp.databinding.FragmentUpdateBinding
import com.example.userapp.model.User
import com.example.userapp.viewmodel.UserViewModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class UpdateFragment : Fragment() {

    lateinit var binding: FragmentUpdateBinding
    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var userViewModel: UserViewModel
    lateinit var bitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.etFirstName.setText(args.currentUser.firstName)
        binding.etLastName.setText(args.currentUser.lastName)
        binding.etAge.setText(args.currentUser.age.toString())
        binding.etAddress.setText(args.currentUser.address)
        Glide.with(requireContext())
            .load(args.currentUser.profilePhoto)
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.darker_gray)
            .into(binding.imgProfile)

        binding.btnUpdate.setOnClickListener {
            updateItem()
        }

        // Add menu
        setHasOptionsMenu(true)

        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, it))
                } else {
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                }
                binding.imgProfile.setImageBitmap(bitmap)
            }
        )

        binding.btnChoosePhoto.setOnClickListener {
            getImage.launch("image/*")
        }

        return binding.root
    }

    private fun updateItem() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val age = binding.etAge.text
        val address = binding.etAddress.text.toString()

        binding.imgProfile.invalidate()
        bitmap = binding.imgProfile.drawable.toBitmap()

        if (inputCheck(firstName, lastName, age, address, binding.imgProfile)) {
            // Create user object
            val updateUser = User(args.currentUser.id, firstName, lastName, age.toString().toInt(), address, bitmap)
            // Update current user
            userViewModel.updateUser(updateUser)
            Toast.makeText(context, "Updated successfully!", Toast.LENGTH_SHORT).show()
            //Navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(
        firstName: String,
        lastName: String,
        age: Editable,
        address: String,
        image: ImageView
    ): Boolean {
        return !(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || age.isEmpty() || TextUtils.isEmpty(address) || image.drawable == null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        AlertDialog.Builder(context)
            .setTitle("Delete ${args.currentUser.firstName}")
            .setMessage("Are you sure to delete ${args.currentUser.firstName}?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                userViewModel.deleteUser(args.currentUser)
                Toast.makeText(
                    context,
                    "Sucessfully removed: ${args.currentUser.firstName}",
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