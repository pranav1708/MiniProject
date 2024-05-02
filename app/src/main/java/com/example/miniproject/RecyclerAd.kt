package com.example.miniproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAd(private val context: Context, applicationArrayList : ArrayList<ApplicationInfo>) : RecyclerView.Adapter<RecyclerAd.Viewholder>() {
    private val applicationArrayList : ArrayList<ApplicationInfo>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAd.Viewholder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.application_card_view, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAd.Viewholder, position: Int) {
        val model : ApplicationInfo = applicationArrayList[position]
        holder.nameStudent.setText(model.getStudentName())
        holder.nameFather.setText(model.getFatherName())
    }

    override fun getItemCount(): Int {
        return applicationArrayList.size
    }
    class Viewholder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nameStudent : TextView
        val nameFather : TextView
        init {
            nameStudent = itemView.findViewById(R.id.name)
            nameFather = itemView.findViewById(R.id.father)
        }
    }
    init {
        this.applicationArrayList = applicationArrayList
    }
}