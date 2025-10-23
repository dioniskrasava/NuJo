package com.majo.nutjo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/*Главная активность. Проверяющая факт уже имеющихся пользователей*/
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("NutritionAppPrefs", MODE_PRIVATE)
        val usersJson = prefs.getString("key_users", "[]")
        val gson = Gson()
        val type = object : TypeToken<List<User>>() {}.type
        val users = gson.fromJson<List<User>>(usersJson, type) ?: emptyList()

        val selectedUserId = prefs.getInt("selected_user_id", -1)

        val intent = when {
            users.isEmpty() -> {
                // Нет пользователей - идем к регистрации
                Intent(this, MainActivity::class.java)
            }
            selectedUserId == -1 -> {
                // Есть пользователи, но никто не выбран - идем к выбору
                Intent(this, UserListActivity::class.java)
            }
            else -> {
                // Есть выбранный пользователь - идем к ГЛАВНОМУ ЭКРАНУ
                Intent(this, MainAppActivity::class.java)
            }
        }

        startActivity(intent)
        finish()
    }
}