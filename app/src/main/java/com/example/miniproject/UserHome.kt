package com.example.miniproject

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserHome : AppCompatActivity() {
    lateinit var db : DatabaseReference
    lateinit var txt1 : TextView
    lateinit var auth: FirebaseAuth
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
    lateinit var card2 : CardView
    lateinit var card8 : CardView
    lateinit var card9 : CardView
    lateinit var name : TextView
    lateinit var fatherName : TextView
    lateinit var text3 : TextView
    lateinit var pending : RecyclerView
    lateinit var approved : RecyclerView
    lateinit var recyclerAd: RecyclerAd
    lateinit var approvedAd: ApprovedAd
    private var applicationArrayList : ArrayList<ApplicationInfo> = ArrayList<ApplicationInfo>()
    private var approvedArrayList : ArrayList<ApprovedInfo> = ArrayList<ApprovedInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)
        pending = findViewById(R.id.pending)
        approved = findViewById(R.id.approved)
        card2 = findViewById(R.id.card2)
        db = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        txt1 = findViewById(R.id.txt1)
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
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId.let {
            if (it != null) {
                db.child("users").child(it).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var name = snapshot.child("Name").value.toString()
                        txt1.text = "Hello $name\nWelcome to Meridian"
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
        val id = FirebaseAuth.getInstance().currentUser?.uid?:""
        val ref = db.child("users").child(id).child("Application Details")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val key = childSnapshot.key
                    key.let {
                        if (it != null) {
                            ref.child(it).addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val status = snapshot.child("Application Status").value.toString()
                                    if (status == "Submitted, but not approved"){
                                        val nm = snapshot.child("First Name").value.toString()
                                        val fm = snapshot.child("Father Name").value.toString()
                                        applicationArrayList.add(ApplicationInfo(nm, fm))
                                    }
                                    else if (status == "Approved"){
                                        val nm = snapshot.child("First Name").value.toString()
                                        val fm = snapshot.child("Father Name").value.toString()
                                        approvedArrayList.add(ApprovedInfo(nm, fm))
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                            recyclerAd.notifyDataSetChanged()
                            approvedAd.notifyDataSetChanged()
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        recyclerAd = RecyclerAd(this, applicationArrayList)
        approvedAd = ApprovedAd(this, approvedArrayList)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val layout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        pending.layoutManager = linearLayoutManager
        approved.layoutManager = layout
        pending.adapter = recyclerAd
        approved.adapter = approvedAd
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                applicationArrayList.clear()
                approvedArrayList.clear()
                for (childSnapshot in snapshot.children) {
                    val status = childSnapshot.child("Application Status").value.toString()
                    val nm = childSnapshot.child("First Name").value.toString()
                    val fm = childSnapshot.child("Father Name").value.toString()

                    when (status) {
                        "Submitted, but not approved" -> {
                            applicationArrayList.add(ApplicationInfo(nm, fm))
                        }
                        "Approved" -> {
                            approvedArrayList.add(ApprovedInfo(nm, fm))
                        }
                    }
                }
                recyclerAd.notifyDataSetChanged()
                approvedAd.notifyDataSetChanged()
                if (applicationArrayList.isEmpty()) {
                    card2.visibility = View.VISIBLE
                    pending.visibility = View.GONE
                } else {
                    card2.visibility = View.GONE
                    pending.visibility = View.VISIBLE
                }

                if (approvedArrayList.isEmpty()) {
                    approved.visibility = View.GONE
                } else {
                    approved.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}