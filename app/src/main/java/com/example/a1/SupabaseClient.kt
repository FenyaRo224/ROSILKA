package com.example.a1

import android.util.Log
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.coroutines.runBlocking

object SupabaseClient {
    private const val TAG = "SupabaseClient"

    // Сделано public для доступа из Java
    @JvmField
    val client = createSupabaseClient(
        supabaseUrl = "https://eqkxnncjjfnaffdosgjw.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVxa3hubmNqamZuYWZmZG9zZ2p3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDgwOTc0NDMsImV4cCI6MjA2MzY3MzQ0M30.mo2i986-HJfo3CZxGzUfNheUDrIA0hKxDp3hFg4kbt0"
    ) {
        install(GoTrue)
        install(Postgrest)
        install(Realtime)
    }

    @Serializable
    data class NewsArticle(
        val articleid: Long,
        val title: String,
        val content: String,
        val userid: Long? = null,
        val agencyid: Long? = null,
        val categoryid: Long? = null,
        val createdate: String? = null
    )

    @Serializable
    data class User(
        val userid: Long,
        val firstname: String,
        val lastname: String,
        val email: String,
        val registrationdate: String? = null,
        val password: String? = null,
        val status: String? = null
    )

    @Serializable
    data class Agency(
        val agencyid: Long,
        val agencyname: String,
        val website: String? = null,
        val userid: Long? = null
    )

    @Serializable
    data class Category(
        val categoryid: Long,
        val categoryname: String,
        val userid: Long? = null
    )

    suspend fun fetchNewsArticles(): List<NewsArticle> {
        return try {
            val response = client.postgrest["newsarticles"].select()
            response.decodeList<NewsArticle>()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении новостей: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun fetchNewsArticleById(articleid: Long): NewsArticle? {
        return try {
            val response = client.postgrest["newsarticles?articleid=eq.$articleid&limit=1"].select()
            val list = response.decodeList<NewsArticle>()
            list.firstOrNull()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении новости по id: ${e.message}", e)
            null
        }
    }

    suspend fun fetchUserNameById(userid: Long): String {
        return try {
            val response = client.postgrest["users?userid=eq.$userid&limit=1"].select()
            val list = response.decodeList<User>()
            val user = list.firstOrNull()
            if (user != null) "${user.firstname} ${user.lastname}" else "Неизвестно"
        } catch (e: Exception) {
            "Неизвестно"
        }
    }

    suspend fun fetchAgencyNameById(agencyid: Long): String {
        return try {
            val response = client.postgrest["newsagencies?agencyid=eq.$agencyid&limit=1"].select()
            val list = response.decodeList<Agency>()
            val agency = list.firstOrNull()
            agency?.agencyname ?: "Неизвестно"
        } catch (e: Exception) {
            "Неизвестно"
        }
    }

    suspend fun fetchCategoryNameById(categoryid: Long): String {
        return try {
            val response = client.postgrest["category?categoryid=eq.$categoryid&limit=1"].select()
            val list = response.decodeList<Category>()
            val category = list.firstOrNull()
            category?.categoryname ?: "Неизвестно"
        } catch (e: Exception) {
            "Неизвестно"
        }
    }

    suspend fun fetchNewsTitles(): List<String> {
        return fetchNewsArticles().map { it.title }
    }

    suspend fun fetchAllUserEmails(): List<String> {
        return try {
            val response = client.postgrest["users?select=email"].select()
            response.body?.jsonArray?.mapNotNull { it.jsonObject["email"]?.toString()?.replace("\"", "") } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении email-ов пользователей: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun testConnection() {
        try {
            Log.d(TAG, "Начало теста подключения")
            client.postgrest["users?limit=1"].select()
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
                put("email", email)
                put("password", password)
                put("firstname", firstName)
                put("lastname", lastName)
                put("status", "active")
            }
            client.postgrest["users"].insert(userData)
            Log.d(TAG, "Пользователь успешно добавлен")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при добавлении пользователя: ${e.message}", e)
            throw e
        }
    }

    // Kotlin suspend-функция для публикации новости
    suspend fun publishNewsArticle(article: NewsArticle) {
        client.postgrest["newsarticles"].insert(article)
    }

    // Java-совместимый вызов (вызывать из Java-кода!)
    @JvmStatic
    fun publishNewsArticleSync(article: NewsArticle) {
        runBlocking {
            publishNewsArticle(article)
        }
    }

    // Java-совместимый вызов для получения заголовков новостей
    @JvmStatic
    fun getNewsTitles(): ArrayList<String> {
        return runBlocking {
            ArrayList(fetchNewsTitles())
        }
    }
    @JvmStatic
    fun getUserInfoList(): ArrayList<String> {
        return runBlocking {
            val result = ArrayList<String>()
            try {
                val response = client.postgrest["users"].select()
                val users = response.decodeList<User>()
                for (user in users) {
                    val info = "Email: ${user.email}\n" +
                            "Имя: ${user.firstname}\n" +
                            "Фамилия: ${user.lastname}\n" +
                            "Дата регистрации: ${user.registrationdate ?: "не указано"}\n" +
                            "Статус: ${user.status ?: "не указан"}"
                    result.add(info)
                }
            } catch (e: Exception) {
                // Можно логировать ошибку, если нужно
            }
            result
        }
    }
}