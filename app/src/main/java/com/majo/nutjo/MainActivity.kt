package com.majo.nutjo

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var nameEditText: TextInputEditText
    private lateinit var heightEditText: TextInputEditText
    private lateinit var weightEditText: TextInputEditText
    private lateinit var ageEditText: TextInputEditText
    private lateinit var saveButton: Button
    private lateinit var userListButton: Button

    private val prefsName = "NutritionAppPrefs"
    private val keyUsers = "key_users"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        nameEditText = findViewById(R.id.nameEditText)
        heightEditText = findViewById(R.id.heightEditText)
        weightEditText = findViewById(R.id.weightEditText)
        ageEditText = findViewById(R.id.ageEditText)
        saveButton = findViewById(R.id.saveProfileButton)
        userListButton = findViewById(R.id.userListButton)
    }

    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            saveUser()
        }

        userListButton.setOnClickListener {
            // Переход на экран списка пользователей
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveUser() {
        val name = nameEditText.text.toString().trim()
        val heightText = heightEditText.text.toString().trim()
        val weightText = weightEditText.text.toString().trim()
        val ageText = ageEditText.text.toString().trim()

        if (name.isEmpty() || heightText.isEmpty() || weightText.isEmpty() || ageText.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val height = heightText.toIntOrNull()
        val weight = weightText.toFloatOrNull()
        val age = ageText.toIntOrNull()

        if (height == null || weight == null || age == null) {
            Toast.makeText(this, "Введите корректные числа", Toast.LENGTH_SHORT).show()
            return
        }

        val newUser = User(name, height, weight, age)
        saveUserToSharedPreferences(newUser)

        Toast.makeText(this, "Пользователь $name сохранен!", Toast.LENGTH_SHORT).show()
        clearFields()
    }

    private fun saveUserToSharedPreferences(newUser: User) {
        val prefs: SharedPreferences = getSharedPreferences(prefsName, MODE_PRIVATE)
        val usersJson = prefs.getString(keyUsers, "[]")

        val gson = Gson()
        val type = object : TypeToken<MutableList<User>>() {}.type
        val usersList = gson.fromJson<MutableList<User>>(usersJson, type) ?: mutableListOf()

        // Проверяем, нет ли уже пользователя с таким именем
        val existingUserIndex = usersList.indexOfFirst { it.name == newUser.name }
        if (existingUserIndex != -1) {
            usersList[existingUserIndex] = newUser // Обновляем существующего
        } else {
            usersList.add(newUser) // Добавляем нового
        }

        val editor = prefs.edit()
        editor.putString(keyUsers, gson.toJson(usersList))
        editor.apply()
    }

    private fun clearFields() {
        nameEditText.text?.clear()
        heightEditText.text?.clear()
        weightEditText.text?.clear()
        ageEditText.text?.clear()
    }
}