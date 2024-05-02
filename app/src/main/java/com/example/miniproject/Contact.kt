package com.example.miniproject

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Contact : AppCompatActivity() {
    lateinit var openMap : Button
    lateinit var address : String
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
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
    lateinit var db : DatabaseReference
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        openMap = findViewById(R.id.openmap)
        address = "23-77, Street, 11, Rallaguda rd, Madhuranagar, Shamshabad, Hyderabad, Telangana, 501218"
        db = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
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
                    db.child("users").child(it).addListenerForSingleValueEvent(object :
                        ValueEventListener {
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
        val geocoder = Geocoder(this)
        val list : List<Address> = geocoder.getFromLocationName(address, 5)!!
        if (list.isNullOrEmpty()){
            return
        }
        latitude = list[0].latitude
        longitude = list[0].longitude
        openMap.setOnClickListener(){
            val uri = Uri.parse("geo:$latitude, $longitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }
}