package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAge: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDeactivate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etEmail)
        etAge = findViewById(R.id.etAge)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDeactivate = findViewById(R.id.btnDeactivate)

        loadUserProfile()

        btnUpdate.setOnClickListener {
            updateUserProfile()
        }

        btnDeactivate.setOnClickListener {
            deactivateAccount()
        }
    }

    private fun loadUserProfile() {
        val token = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("token", "") ?: ""

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserProfile("Bearer $token")
                if (response.isSuccessful) {
                    val user = response.data
                    if (user != null) {
                        etFirstName.setText(user.firstName)
                        etLastName.setText(user.lastName)
                        etEmail.setText(user.email)
                        etAge.setText(user.age.toString())
                    }
                } else {
                    Toast.makeText(this@ProfileActivity, "Failed to load profile: ${response.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUserProfile() {
        val token = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("token", "") ?: ""
        val user = UserDto(
            id = null,
            firstName = etFirstName.text.toString(),
            lastName = etLastName.text.toString(),
            email = etEmail.text.toString(),
            age = etAge.text.toString().toIntOrNull() ?: 0,
            password = "",
            role = "",
            status = ""
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.updateUserProfile("Bearer $token", user)
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Profile updated successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@ProfileActivity, "Failed to update profile: ${response.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deactivateAccount() {
        val token = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("token", "") ?: ""
        val userId = "" // You need to store the user ID when logging in or fetching the profile

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.updateUserStatus("Bearer $token", userId, mapOf("status" to "inactive"))
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Account deactivated. Please contact CSR for reactivation.", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this@ProfileActivity, "Failed to deactivate account: ${response.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}