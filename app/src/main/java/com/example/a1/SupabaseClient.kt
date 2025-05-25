package com.example.a1

import android.util.Log
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

object SupabaseClient {
    private const val TAG = "SupabaseClient"

    val client = createSupabaseClient(
        supabaseUrl = "https://eqkxnncjjfnaffdosgjw.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVxa3hubmNqamZuYWZmZG9zZ2p3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDgwOTc0NDMsImV4cCI6MjA2MzY3MzQ0M30.mo2i986-HJfo3CZxGzUfNheUDrIA0hKxDp3hFg4kbt0"
    ) {
        install(GoTrue)
        install(Postgrest)
        install(Realtime)
    }.also {
        Log.d(TAG, "Supabase client initialized")
    }

    suspend fun testConnection() {
        try {
            Log.d(TAG, "Начало теста подключения")
            val testQuery = client.postgrest
                .from("Users")
                .select()

            if (testQuery == null) {
                throw Exception("Не удалось подключиться к базе данных")
            }

            Log.d(TAG, "Тест подключения успешен")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при тестировании подключения: ${e.message}", e)
            throw e
        }
    }

    suspend fun insertUser(email: String, password: String, firstName: String, lastName: String) {
        try {
            Log.d(TAG, "Начало добавления пользователя")

            val userData = buildJsonObject {
                put("Email", email)
                put("Password", password)
                put("FirstName", firstName)
                put("LastName", lastName)
                put("Status", "active")
            }

            val result = client.postgrest
                .from("Users")
                .insert(userData)

            if (result == null) {
                throw Exception("Не удалось добавить пользователя")
            }

            Log.d(TAG, "Пользователь успешно добавлен")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при добавлении пользователя: ${e.message}", e)
            throw e
        }
    }
}