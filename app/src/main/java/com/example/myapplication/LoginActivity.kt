package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button // btnRegister is declared but not initialized

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister) // You need to initialize btnRegister here

        // Set the click listener for login button
        btnLogin.setOnClickListener {
            loginUser()
        }

        // Set the click listener for register button
        btnRegister.setOnClickListener {
            // Redirect to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val loginDto = LoginDto(
            email = etEmail.text.toString(),
            password = etPassword.text.toString()
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.loginUser(loginDto)
                if (response.isSuccessful) {
                    val token = response.data
                    if (token != null) {
                        // Save token to SharedPreferences
                        getSharedPreferences("user_prefs", MODE_PRIVATE).edit().putString("token", token).apply()

                        startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed: No token received", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed: ${response.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
