package com.example.miniproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    lateinit var signupName : EditText
    lateinit var signupMail : EditText
    lateinit var signupPass : EditText
    lateinit var signupCpass : EditText
    lateinit var signupContact : EditText
    lateinit var signupAge : EditText
    lateinit var rgrp : RadioGroup
    lateinit var check : CheckBox
    lateinit var signup : Button
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signupName = findViewById(R.id.signupName)
        signupMail = findViewById(R.id.signupMail)
        signupPass = findViewById(R.id.signupPass)
        signupCpass = findViewById(R.id.signupCpass)
        signupContact = findViewById(R.id.signupContact)
        signupAge = findViewById(R.id.signupAge)
        rgrp = findViewById(R.id.rgrp)
        check = findViewById(R.id.check)
        signup = findViewById(R.id.signup)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        signup.setOnClickListener(){
            if (signupName.text.toString().isEmpty() || signupMail.text.toString().isEmpty() || signupPass.text.toString().isEmpty() ||
                    signupCpass.text.toString().isEmpty() || signupContact.text.toString().isEmpty() || signupAge.text.toString().isEmpty()){
                Toast.makeText(this, "Enter all the fields", Toast.LENGTH_SHORT).show()
            }
            else if (signupPass.text.toString() != signupCpass.text.toString()){
                Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show()
            }
            else if (rgrp.checkedRadioButtonId == -1){
                Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show()
            }
            else if (!check.isChecked){
                Toast.makeText(this, "Agree to the checkbox", Toast.LENGTH_SHORT).show()
            }
            else{
                var gender = (rgrp.findViewById<RadioButton>(rgrp.checkedRadioButtonId)).text
                auth.createUserWithEmailAndPassword(signupMail.text.toString(), signupPass.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                        if (signupMail.text.toString() == "pranavtez123@gmail.com"){
                            val  userId = auth.currentUser?.uid?:""
                            val userRef = db.getReference("users").child(userId)
                            userRef.child("Name").setValue(signupName.text.toString())
                            userRef.child("Email").setValue(signupMail.text.toString())
                            userRef.child("Contact").setValue("+91-" + signupContact.text.toString())
                            userRef.child("Age").setValue(signupAge.text.toString())
                            userRef.child("Gender").setValue(gender)
                            userRef.child("Role").setValue("admin")
                        }
                        else{
                            val userId = auth.currentUser?.uid?:""
                            val userRef = db.getReference("users").child(userId)
                            userRef.child("Name").setValue(signupName.text.toString())
                            userRef.child("Email").setValue(signupMail.text.toString())
                            userRef.child("Contact").setValue("+91-" + signupContact.text.toString())
                            userRef.child("Age").setValue(signupAge.text.toString())
                            userRef.child("Gender").setValue(gender)
                            userRef.child("Account Verification Status").setValue("No")
                            userRef.child("Application Details")
                            userRef.child("Role").setValue("user")
                        }
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Account already exists", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}