package com.example.miniproject

class ApprovedInfo(private var studentName : String, private var fatherName : String){
    fun getStudentName() : String {
        return studentName
    }
    fun setStudentName(studentName: String){
        this.studentName = studentName
    }
    fun getFatherName() : String{
        return fatherName
    }
    fun setFatherName(fatherName: String){
        this.fatherName = fatherName
    }
}