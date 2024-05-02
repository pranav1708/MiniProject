package com.example.miniproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ApprovedAd(private val context: Context, approvedArrayList: ArrayList<ApprovedInfo>) : RecyclerView.Adapter<ApprovedAd.Viewholder>() {
    private val approvedArrayList : ArrayList<ApprovedInfo>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApprovedAd.Viewholder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.approved_card_view, parent, false)
        return ApprovedAd.Viewholder(view)
    }

    override fun onBindViewHolder(holder: ApprovedAd.Viewholder, position: Int) {
        val model : ApprovedInfo = approvedArrayList[position]
        holder.nameStudent.setText(model.getStudentName())
        holder.nameFather.setText(model.getFatherName())
    }

    override fun getItemCount(): Int {
        return approvedArrayList.size
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
        this.approvedArrayList = approvedArrayList
    }
}