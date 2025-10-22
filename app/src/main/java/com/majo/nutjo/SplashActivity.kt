package com.majo.nutjo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Проверяем, есть ли сохраненные пользователи
        val prefs = getSharedPreferences("NutritionAppPrefs", MODE_PRIVATE)
        val usersJson = prefs.getString("key_users", "[]")
        val gson = Gson()
        val type = object : TypeToken<List<User>>() {}.type
        val users = gson.fromJson<List<User>>(usersJson, type) ?: emptyList()

        // Проверяем, есть ли выбранный пользователь
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
                // Есть выбранный пользователь - идем к продуктам
                Intent(this, ProductActivity::class.java)
            }
        }

        startActivity(intent)
        finish() // Закрываем SplashActivity чтобы нельзя было вернуться назад
    }
}