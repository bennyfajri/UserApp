package com.example.userapp.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.R
import com.example.userapp.data.User
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