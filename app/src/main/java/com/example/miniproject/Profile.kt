package com.example.miniproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.GrammaticalInflectionManagerCompat.GrammaticalGender
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class Profile : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var db : DatabaseReference
    lateinit var name : TextView
    lateinit var email : TextView
    lateinit var contact : TextView
    lateinit var age : TextView
    lateinit var status : TextView
    lateinit var gender : TextView
    lateinit var verify : Button
    lateinit var delete : Button
    lateinit var open : FloatingActionButton
    lateinit var close : FloatingActionButton
    lateinit var home : FloatingActionButton
    lateinit var profile : FloatingActionButton
    lateinit var apply : FloatingActionButton
    lateinit var locate : FloatingActionButton
    lateinit var logout : FloatingActionButton
    lateinit var homeText : TextView
    lateinit var profileText : TextView
    lateinit var applyText : TextView
    lateinit var locateText : TextView
    lateinit var logoutText : TextView
    private var isAllVisible = false
    lateinit var verificationStatus : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        auth = FirebaseAuth.getInstance()
        db = Firebase.database.reference
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        contact = findViewById(R.id.contact)
        age = findViewById(R.id.age)
        status = findViewById(R.id.status)
        gender = findViewById(R.id.gender)
        verify = findViewById(R.id.verify)
        delete = findViewById(R.id.delete)
        open = findViewById(R.id.open)
        close = findViewById(R.id.close)
        home = findViewById(R.id.home)
        profile = findViewById(R.id.profile)
        apply = findViewById(R.id.apply)
        locate = findViewById(R.id.locate)
        logout = findViewById(R.id.logout)
        homeText = findViewById(R.id.home_text)
        profileText = findViewById(R.id.profile_text)
        applyText = findViewById(R.id.application_text)
        locateText = findViewById(R.id.locate_text)
        logoutText = findViewById(R.id.logout_text)
        open.visibility = View.VISIBLE
        close.visibility = View.GONE
        home.visibility = View.GONE
        apply.visibility = View.GONE
        profile.visibility = View.GONE
        locate.visibility = View.GONE
        logout.visibility = View.GONE
        homeText.visibility = View.GONE
        applyText.visibility = View.GONE
        profileText.visibility = View.GONE
        applyText.visibility = View.GONE
        locateText.visibility = View.GONE
        logoutText.visibility = View.GONE
        open.setOnClickListener(){
            if (!isAllVisible){
                open.hide()
                close.show()
                home.show()
                profile.show()
                apply.show()
                locate.show()
                logout.show()
                homeText.visibility = View.VISIBLE
                profileText.visibility = View.VISIBLE
                applyText.visibility = View.VISIBLE
                locateText.visibility = View.VISIBLE
                logoutText.visibility = View.VISIBLE
                isAllVisible = true
            }
        }
        close.setOnClickListener(){
            open.show()
            close.hide()
            home.hide()
            profile.hide()
            apply.hide()
            locate.hide()
            logout.hide()
            homeText.visibility = View.GONE
            profileText.visibility = View.GONE
            applyText.visibility = View.GONE
            locateText.visibility = View.GONE
            logoutText.visibility = View.GONE
            isAllVisible = false
        }
        home.setOnClickListener(){
            Toast.makeText(this, "Proceeding to home", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, UserHome::class.java)
            startActivity(intent)
            finish()
        }
        profile.setOnClickListener(){
            Toast.makeText(this, "Proceeding to profile", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
            finish()
        }
        apply.setOnClickListener(){
            val userId = FirebaseAuth.getInstance().currentUser?.uid?:""
            userId.let{
                if (it != null){
                    db.child("users").child(it).addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            verificationStatus = snapshot.child("Account Verification Status").value.toString()
                            if (verificationStatus == "No"){
                                Toast.makeText(applicationContext, "Verify your account to fill application", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Toast.makeText(applicationContext, "Proceeding to Application", Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, Applications::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }
            }
        }
        locate.setOnClickListener(){
            Toast.makeText(this, "Proceeding to Location", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Contact::class.java)
            startActivity(intent)
            finish()
        }
        logout.setOnClickListener(){
            Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show()
            auth.signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        verify.setOnClickListener(){
            val currentUser = auth.currentUser
            currentUser?.sendEmailVerification()?.addOnSuccessListener {
                Toast.makeText(this, "Verification Mail sent successfully", Toast.LENGTH_SHORT).show()
                val userRef = db.child("users").child(currentUser.uid)
                val newData = "Yes"
                currentUser.uid.let {
                    userRef.child("Account Verification Status").setValue(newData).addOnSuccessListener {
                        Toast.makeText(this, "Email Id verified Successfully", Toast.LENGTH_SHORT).show()
                    }
                        .addOnFailureListener(){
                            Toast.makeText(this, "Email ID verification failed", Toast.LENGTH_SHORT).show()
                        }
                }
            }
                ?.addOnFailureListener(){
                    Toast.makeText(this, "Failed to send Verification mail", Toast.LENGTH_SHORT).show()
                }
        }
        val userId = auth.currentUser?.uid?:""
        val userRef = db.child("users").child(userId)
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val nm = snapshot.child("Name").value.toString()
                val mail = snapshot.child("Email").value.toString()
                val ph = snapshot.child("Contact").value.toString()
                val ag = snapshot.child("Age").value.toString()
                val stat = snapshot.child("Account Verification Status").value.toString()
                val gen = snapshot.child("Gender").value.toString()
                name.text = nm
                email.text = mail
                contact.text = ph
                age.text = ag
                status.text = stat
                gender.text = gen
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        delete.setOnClickListener(){
            auth.currentUser?.delete()?.addOnSuccessListener {
                Toast.makeText(this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show()
            }
                ?.addOnFailureListener(){
                    Toast.makeText(this, "Account couldn't be deleted", Toast.LENGTH_SHORT).show()
                }
        }
    }
}