package com.example.a1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a1.databinding.ActivityRegistrationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private val tag = "RegistrationActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString()
            val firstName = binding.editTextFirstName.text.toString().trim()
            val lastName = binding.editTextLastName.text.toString().trim()

            if (validateInput(email, password, firstName, lastName)) {
                binding.btnRegister.isEnabled = false
                registerUser(email, password, firstName, lastName)
            }
        }
    }

    private fun validateInput(email: String, password: String, firstName: String, lastName: String): Boolean {
        when {
            email.isEmpty() -> {
                showMessage("Введите email")
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showMessage("Введите корректный email")
                return false
            }
            password.isEmpty() -> {
                showMessage("Введите пароль")
                return false
            }
            password.length < 6 -> {
                showMessage("Пароль должен быть не менее 6 символов")
                return false
            }
            firstName.isEmpty() -> {
                showMessage("Введите имя")
                return false
            }
            lastName.isEmpty() -> {
                showMessage("Введите фамилию")
                return false
            }
        }
        return true
    }

    private fun registerUser(email: String, password: String, firstName: String, lastName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Проверяем подключение
                try {
                    SupabaseClient.testConnection()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        showMessage("Ошибка подключения к серверу: ${e.message}")
                        binding.btnRegister.isEnabled = true
                    }
                    return@launch
                }

                // Добавляем пользователя
                try {
                    SupabaseClient.insertUser(email, password, firstName, lastName)

                    withContext(Dispatchers.Main) {
                        showMessage("Регистрация успешна")
                        val intent = Intent(this@RegistrationActivity, MainPageActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                } catch (e: Exception) {
                    val errorMessage = when {
                        e.message?.contains("duplicate key") == true -> "Этот email уже зарегистрирован"
                        e.message?.contains("permission denied") == true -> "Ошибка доступа к базе данных"
                        else -> "Ошибка при регистрации: ${e.message}"
                    }

                    Log.e(tag, "Ошибка регистрации", e)

                    withContext(Dispatchers.Main) {
                        showMessage(errorMessage)
                        binding.btnRegister.isEnabled = true
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "Критическая ошибка", e)
                withContext(Dispatchers.Main) {
                    showMessage("Произошла непредвиденная ошибка")
                    binding.btnRegister.isEnabled = true
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}