package com.example.a1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a1.databinding.ActivityLoginBinding
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.FilterOperator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.coroutines.withContext

@Serializable
internal data class LoginUser(
    val email: String,
    val status: String
)

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val tag = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val emailStr = binding.etEmail.text.toString().trim()
            val passwordStr = binding.etPassword.text.toString().trim()

            if (emailStr.isEmpty() || passwordStr.isEmpty()) {
                showToast("Пожалуйста, заполните все поля")
                return@setOnClickListener
            }

            binding.btnLogin.isEnabled = false
            loginUser(emailStr, passwordStr)
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    private fun loginUser(emailStr: String, passwordStr: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Проверяем существование пользователя
                val userExists = checkUserExists(emailStr)
                if (!userExists) {
                    showError("Аккаунт не найден. Проверьте email или зарегистрируйтесь")
                    return@launch
                }

                // Пробуем авторизоваться
                try {
                    val response = SupabaseClient.client.postgrest["Users"]
                        .select {
                            eq("email", emailStr)
                            eq("password", passwordStr)
                        }
                        .decodeList<LoginUser>()

                    if (response.isEmpty()) {
                        showError("Неверный email или пароль")
                        return@launch
                    }
                } catch (e: Exception) {
                    val message = when {
                        e.message?.contains("Invalid login credentials") == true ->
                            "Неверный email или пароль"
                        e.message?.contains("Email not confirmed") == true ->
                            "Email не подтвержден"
                        else -> "Ошибка входа: ${e.message}"
                    }
                    showError(message)
                    return@launch
                }

                // Проверяем статус пользователя
                checkUserStatus(emailStr)

            } catch (e: Exception) {
                Log.e(tag, "Ошибка при входе", e)
                showError("Произошла неизвестная ошибка")
            }
        }
    }

    private suspend fun checkUserExists(email: String): Boolean = try {
        val response = SupabaseClient.client.postgrest["Users"]
            .select {
                eq("email", email)
            }
            .decodeList<LoginUser>()
        response.isNotEmpty()
    } catch (e: Exception) {
        Log.e(tag, "Ошибка при проверке пользователя", e)
        false
    }

    private suspend fun checkUserStatus(email: String) {
        try {
            val response = SupabaseClient.client.postgrest["Users"]
                .select {
                    eq("email", email)
                }
                .decodeList<LoginUser>()

            if (response.isNotEmpty()) {
                val user = response.first()

                when (user.status.lowercase()) {
                    "active" -> {
                        withContext(Dispatchers.Main) {
                            showToast("Вход выполнен")
                            startActivity(Intent(this@LoginActivity, MainPageActivity::class.java))
                            finish()
                        }
                    }
                    else -> showError("Аккаунт не активирован")
                }
            } else {
                showError("Пользователь не найден")
            }
        } catch (e: Exception) {
            Log.e(tag, "Ошибка при проверке статуса", e)
            showError("Ошибка при проверке статуса пользователя")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            showToast(message)
            binding.btnLogin.isEnabled = true
        }
    }
}