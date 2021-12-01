package com.reddevx.thenewquotes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.databinding.ActivityRegisterBinding
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private val fAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            submit()
        }
        binding.registerHaveAccTv.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }

    private fun submit(){
        if (!passwordValidation() or !emailValidation() ){
            return
        }
        registerUser()
    }

    private fun registerUser(){
        val email = binding.registerEmailEd.text.toString().trim()
        val password = binding.registerPasswordEd.text.toString().trim()



        fAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful){
                    val userId = fAuth.currentUser!!.uid
                    val doc = firestore.collection("users").document(userId)
                    val user:HashMap<String, Any> = HashMap()
                    user["email"] = email
                    user.put("password",password)
                    doc.set(user).addOnSuccessListener {
                        Log.i(
                            "USER_CREATION",
                            "OnSuccess : user added to firestore"
                        )
                    }
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "Error : ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }





    private fun emailValidation(): Boolean {
        val email = binding.registerEmailEd.text.toString().trim()
        if (email.isEmpty()){
            binding.registerEmailContainer.error = "Email is required"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.registerEmailContainer.error = "Invalid email address"
            return false
        }
        binding.registerEmailContainer.error = null
        return true
    }



    private fun passwordValidation(): Boolean {
        val password = binding.registerPasswordEd.text.toString()
        if (password.isEmpty()){
            binding.registerPasswordContainer.error = "Password is required"
            return false
        }
        if (password.length < 8){
            binding.registerPasswordContainer.error = "Must contain at least 8 characters"
            return false
        }
        if (!password.matches(".*[A-Z].*".toRegex())){
            binding.registerPasswordContainer.error = "Must contain at least 1 Upper-case character"
            return false
        }
        if (!password.matches(".*[a-z].*".toRegex())){
            binding.registerPasswordContainer.error = "Must contain at least 1 Lower-case character"
            return false
        }
        if (!password.matches(".*[0-9].*".toRegex())){
            binding.registerPasswordContainer.error = "Must contain at least 1 digit"
            return false
        }

        binding.registerPasswordContainer.error = null
        return true
    }
}