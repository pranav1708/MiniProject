package com.example.miniproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var loginMail : EditText
    lateinit var loginPass : EditText
    lateinit var login : Button
    lateinit var signupText: TextView
    lateinit var forgotText : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        loginMail = findViewById(R.id.loginMail)
        loginPass = findViewById(R.id.loginPass)
        login = findViewById(R.id.login)
        signupText = findViewById(R.id.signupText)
        forgotText = findViewById(R.id.forgotText)
        login.setOnClickListener(){
            if (loginMail.text.toString().isEmpty() || loginPass.text.toString().isEmpty()){
                Toast.makeText(this, "Enter all the fields", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(loginMail.text.toString(), loginPass.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                        if (loginMail.text.toString() == "pranavtez123@gmail.com"){
                            val intent = Intent(this, AdminHome::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            val intent = Intent(this, UserHome::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    else{
                        Toast.makeText(this, "Incorrect Credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        signupText.setOnClickListener(){
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }
        forgotText.setOnClickListener(){
            if (loginMail.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter Mail ID for password restoring", Toast.LENGTH_SHORT).show()
            }
            else{
                sendResetLink(loginMail.text.toString())
            }
        }
    }
    private fun sendResetLink(email : String){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(){
            if (it.isSuccessful){
                Toast.makeText(this, "Reset Link sent to your Mail ID", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Failed to send reset link", Toast.LENGTH_SHORT).show()
            }
        }
    }
}