package com.example.miniproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
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

class AdminHome : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var db : DatabaseReference
    lateinit var name : TextView
    lateinit var father : TextView
    lateinit var open : FloatingActionButton
    lateinit var close : FloatingActionButton
    lateinit var logout : FloatingActionButton
    lateinit var logoutText : TextView
    private var isAllVisible = false
    lateinit var recyclerView: RecyclerView
    private var adminArrayList : ArrayList<AdminInfo> = ArrayList<AdminInfo>()
    lateinit var adminAd : AdminAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        recyclerView = findViewById(R.id.admin)
        recyclerView.layoutManager = LinearLayoutManager(this)
        open = findViewById(R.id.open)
        close = findViewById(R.id.close)
        logout = findViewById(R.id.logout)
        logoutText = findViewById(R.id.logout_text)
        open.visibility = View.VISIBLE
        close.visibility = View.GONE
        logout.visibility = View.GONE
        logoutText.visibility = View.GONE
        auth = FirebaseAuth.getInstance()
        db = Firebase.database.reference
        open.setOnClickListener(){
            if (!isAllVisible){
                open.hide()
                close.show()
                logout.show()
                logoutText.visibility = View.VISIBLE
                isAllVisible = true
            }
        }
        close.setOnClickListener(){
            open.show()
            close.hide()
            logout.hide()
            logoutText.visibility = View.GONE
            isAllVisible = false
        }
        logout.setOnClickListener(){
            Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show()
            auth.signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        val ref = db.child("users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children){
                    val userId = userSnapshot.key
                    if(userId != auth.currentUser?.uid){
                        val reference = userSnapshot.child("Application Details")
                        for(childSnapshot in reference.children){
                            val applicationId = childSnapshot.key
                            val status = childSnapshot.child("Application Status").value.toString()
                            if (status == "Submitted, but not approved"){
                                val nm = childSnapshot.child("First Name").value.toString()
                                val fm = childSnapshot.child("Father Name").value.toString()
                                adminArrayList.add(AdminInfo(nm, fm))
                                recyclerView.visibility = View.VISIBLE
                                val applicationRef = childSnapshot.ref
                                adminAd = AdminAd(this, adminArrayList){ position, buttonId ->
                                    when(buttonId){
                                        R.id.approve ->
                                        {
                                            applicationRef.child("Application Status").setValue("Approved").addOnSuccessListener {
                                                Toast.makeText(applicationContext, "Application Approved",Toast.LENGTH_SHORT).show()
                                                adminArrayList.removeAt(position)
                                                adminAd.notifyDataSetChanged()
                                            }
                                                .addOnFailureListener(){
                                                    Toast.makeText(applicationContext, "Failed to approve application", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                        R.id.reject -> {
                                            applicationRef.removeValue().addOnFailureListener(){
                                                Toast.makeText(applicationContext, "Rejected", Toast.LENGTH_SHORT).show()
                                                adminArrayList.removeAt(position)
                                                adminAd.notifyDataSetChanged()
                                            }
                                                .addOnFailureListener(){
                                                    Toast.makeText(applicationContext, "Failed to reject", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                    }
                                }
                                recyclerView.adapter = adminAd
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}