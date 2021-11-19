package com.example.userapp.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.userapp.R
import com.example.userapp.model.User
import com.example.userapp.databinding.ItemDataBinding

class ListAdapter: RecyclerView.Adapter<ListAdapter.ViewHolder>(){

    private var userList = emptyList<User>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        with(holder){
            binding.tvName.text = "${user.firstName} ${user.lastName}"
            binding.tvId.text = user.id.toString()
            binding.tvAge.text = user.age.toString()
            binding.itemData.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(user)
                binding.itemData.findNavController().navigate(action)
            }
            binding.tvAddress.text = user.address
            Glide.with(itemView.context)
                .load(user.profilePhoto)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.darker_gray)
                .into(binding.imgPhoto)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemDataBinding.bind(itemView)
    }

    fun setData(newList : List<User>){
        userList = newList
        notifyDataSetChanged()
    }
}