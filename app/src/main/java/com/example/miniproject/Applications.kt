package com.example.miniproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Applications : AppCompatActivity() {
    lateinit var firstName : EditText
    lateinit var lastName: EditText
    lateinit var fatherName : EditText
    lateinit var motherName: EditText
    lateinit var parentsContact : EditText
    lateinit var age : EditText
    lateinit var presentStandard : EditText
    lateinit var rgrp : RadioGroup
    lateinit var nationality : EditText
    lateinit var applicant : EditText
    lateinit var relation : EditText
    lateinit var presentSchool : EditText
    lateinit var address : EditText
    lateinit var check : CheckBox
    lateinit var submit  : Button
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseDatabase
    lateinit var dbref : DatabaseReference
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
        setContentView(R.layout.activity_applications)
        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        fatherName = findViewById(R.id.fatherName)
        motherName = findViewById(R.id.motherName)
        parentsContact = findViewById(R.id.contact)
        age = findViewById(R.id.age)
        presentStandard = findViewById(R.id.grade)
        rgrp = findViewById(R.id.rgrp)
        nationality = findViewById(R.id.nationality)
        applicant = findViewById(R.id.yourname)
        relation = findViewById(R.id.relation)
        presentSchool = findViewById(R.id.presentSchool)
        address = findViewById(R.id.address)
        check = findViewById(R.id.check)
        submit = findViewById(R.id.submit)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbref = Firebase.database.reference
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
        submit.setOnClickListener(){
            if(firstName.text.toString().isEmpty() || lastName.text.toString().isEmpty() || fatherName.text.toString().isEmpty() || motherName.text.toString().isEmpty()
                || parentsContact.text.toString().isEmpty() || age.text.toString().isEmpty() || presentStandard.text.toString().isEmpty() ||
                    nationality.text.toString().isEmpty() || applicant.text.toString().isEmpty() || relation.text.toString().isEmpty() || presentSchool.text.toString().isEmpty() || address.text.toString().isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else if(rgrp.checkedRadioButtonId == -1){
                Toast.makeText(this, "Selent Gender", Toast.LENGTH_SHORT).show()
            }
            else if(!check.isChecked){
                Toast.makeText(this, "Agree to the checkbox", Toast.LENGTH_SHORT).show()
            }
            else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Submit Alert")
                    .setMessage("Are you sure want to submit?")
                    .setCancelable(true)
                    .setIcon(R.drawable.baseline_warning_24)
                builder.setPositiveButton("Yes") { dialogInterface, which ->
                    Toast.makeText(applicationContext, "Submitting....", Toast.LENGTH_LONG).show()
                    var gender = (rgrp.findViewById<RadioButton>(rgrp.checkedRadioButtonId)).text
                    val userId = auth.currentUser?.uid?:""
                    val userRef = db.getReference("users").child(userId).child("Application Details")
                    val uniqueKey = userRef.push().key
                    if (uniqueKey != null) {
                        userRef.child(uniqueKey).child("First Name").setValue(firstName.text.toString())
                        userRef.child(uniqueKey).child("Last Name").setValue(lastName.text.toString())
                        userRef.child(uniqueKey).child("Father Name").setValue(fatherName.text.toString())
                        userRef.child(uniqueKey).child("Mother Name").setValue(motherName.text.toString())
                        userRef.child(uniqueKey).child("Parent's Contact").setValue(parentsContact.text.toString())
                        userRef.child(uniqueKey).child("Age").setValue(age.text.toString())
                        userRef.child(uniqueKey).child("Present Standard").setValue(presentStandard.text.toString())
                        userRef.child(uniqueKey).child("Gender").setValue(gender)
                        userRef.child(uniqueKey).child("Nationality").setValue(nationality.text.toString())
                        userRef.child(uniqueKey).child("Applicant Name").setValue(applicant.text.toString())
                        userRef.child(uniqueKey).child("Relation").setValue(relation.text.toString())
                        userRef.child(uniqueKey).child("Present School of Study").setValue(presentSchool.text.toString())
                        userRef.child(uniqueKey).child("Address").setValue(address.text.toString())
                        userRef.child(uniqueKey).child("Application Status").setValue("Submitted, but not approved").addOnSuccessListener {
                            Toast.makeText(this, "Application Submitted Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, UserHome::class.java)
                            startActivity(intent)
                            finish()
                        }
                            .addOnFailureListener(){
                                Toast.makeText(this, "Application couldn't submit successfully", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                builder.setNeutralButton("Cancel") { dialogInterface, which ->
                    Toast.makeText(applicationContext, "Cancelling....", Toast.LENGTH_LONG).show()
                }
                builder.setNegativeButton("No") { dialogInterface, which ->
                    Toast.makeText(applicationContext, "Not Submitting....", Toast.LENGTH_LONG).show()
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        }
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
                    dbref.child("users").child(it).addListenerForSingleValueEvent(object : ValueEventListener{
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
    }
}