package com.example.miniproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener

class AdminAd(private val context: ValueEventListener, adminArrayList: ArrayList<AdminInfo>, private val onButtonClickListener: (position : Int, buttonId : Int) -> Unit) : RecyclerView.Adapter<AdminAd.Viewholder>() {
    private val adminArrayList : ArrayList<AdminInfo>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAd.Viewholder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.admin_card_view, parent, false)
        return AdminAd.Viewholder(view)
    }

    override fun onBindViewHolder(holder: AdminAd.Viewholder, position: Int) {
        val model : AdminInfo = adminArrayList[position]
        holder.nameStudent.setText(model.getStudentName())
        holder.nameFather.setText(model.getFatherName())
        holder.approve.setOnClickListener(){
            onButtonClickListener(position, R.id.approve)
        }
        holder.reject.setOnClickListener(){
            onButtonClickListener(position, R.id.reject)
        }
    }

    override fun getItemCount(): Int {
        return adminArrayList.size
    }
    class Viewholder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nameStudent : TextView
        val nameFather : TextView
        val approve : Button
        val reject : Button
        init {
            nameStudent = itemView.findViewById(R.id.name)
            nameFather = itemView.findViewById(R.id.father)
            approve = itemView.findViewById(R.id.approve)
            reject = itemView.findViewById(R.id.reject)
        }
    }
    init {
        this.adminArrayList = adminArrayList
    }
}