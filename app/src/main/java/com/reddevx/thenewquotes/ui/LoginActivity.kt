package com.reddevx.thenewquotes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.reddevx.thenewquotes.R
import com.reddevx.thenewquotes.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private val fAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginBtn.setOnClickListener {
            submit()
        }
        binding.signInDontHaveAccTv.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


    private fun signInUser(){
        val email = binding.loginEmailEd.text.toString().trim()
        val password = binding.loginPasswordEd.text.toString().trim()

        fAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "Error : ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun submit(){
        if (!passwordValidation() or !emailValidation() ){
            return
        }

        signInUser()
    }

    private fun emailValidation(): Boolean {
        val email = binding.loginEmailEd.text.toString().trim()
        if (email.isEmpty()){
            binding.loginEmailContainer.error = "Email is required"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.loginEmailContainer.error = "Invalid email address"
            return false
        }
        binding.loginEmailContainer.error = null
        return true
    }



    private fun passwordValidation(): Boolean {
        val password = binding.loginPasswordEd.text.toString()
        if (password.isEmpty()){
            binding.loginPasswordContainer.error = "Password is required"
            return false
        }

        binding.loginPasswordContainer.error = null
        return true
    }
}