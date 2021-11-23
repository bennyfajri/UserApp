package com.example.userapp.fragments.add

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.databinding.FragmentAddBinding
import com.example.userapp.model.User
import com.example.userapp.viewmodel.UserViewModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class Addfragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    lateinit var bitmap: Bitmap
    lateinit var decoded: Bitmap

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

        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                if(Build.VERSION.SDK_INT < 28 ){
                    bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver, it
                    )
                    setToImageView(getResizedBitmap(bitmap, 512)!!)
                }else {
                    val source = ImageDecoder.createSource(requireContext().contentResolver, it)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    setToImageView(getResizedBitmap(bitmap, 512)!!)
                }
            }
        )

        binding.btnChoosePhoto.setOnClickListener {
            getImage.launch("image/*")
        }

        return binding.root
    }

    private fun insertDataToDatabase() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val birthday = binding.etBirthday.text.toString()
        val address = binding.etAddress.text.toString()

        if (inputCheck(firstName, lastName, birthday, address, binding.imgProfile)) {
            // Create user object
            val user = User(0, firstName, lastName, birthday, address, decoded)
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
        birthday: String,
        address: String,
        image: ImageView
    ): Boolean {
        return !(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(birthday) || TextUtils.isEmpty(address) || image.drawable == null)
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    private fun setToImageView(bmp: Bitmap) {
        val bytes = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 75, bytes)
        decoded = BitmapFactory.decodeStream(ByteArrayInputStream(bytes.toByteArray()))

        binding.imgProfile.setImageBitmap(decoded)
    }

}