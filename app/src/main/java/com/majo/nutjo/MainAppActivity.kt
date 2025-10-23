package com.majo.nutjo

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class MainAppActivity : AppCompatActivity() {

    private lateinit var welcomeTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var switchUserButton: com.google.android.material.button.MaterialButton

    private val prefsName = "NutritionAppPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_app)

        initViews()
        setupClickListeners()
        updateWelcomeText()
        updateDate()
    }

    private fun initViews() {
        welcomeTextView = findViewById(R.id.welcomeTextView)
        dateTextView = findViewById(R.id.dateTextView)
        switchUserButton = findViewById(R.id.switchUserButton)

        // Находим все карточки
        val addProductCard = findViewById<android.view.View>(R.id.addProductCard)
        val addMealCard = findViewById<android.view.View>(R.id.addMealCard)
        val addDishCard = findViewById<android.view.View>(R.id.addDishCard)
        val productsListCard = findViewById<android.view.View>(R.id.productsListCard)
        val mealsListCard = findViewById<android.view.View>(R.id.mealsListCard)
        val nutritionSummaryCard = findViewById<android.view.View>(R.id.nutritionSummaryCard)

        // Устанавливаем обработчики для карточек
        addProductCard.setOnClickListener { showToast("Добавление продукта - в разработке") }
        addMealCard.setOnClickListener { showToast("Добавление приема пищи - в разработке") }
        addDishCard.setOnClickListener { showToast("Создание блюда - в разработке") }
        productsListCard.setOnClickListener { showToast("Список продуктов - в разработке") }
        mealsListCard.setOnClickListener { showToast("Список приемов пищи - в разработке") }
        nutritionSummaryCard.setOnClickListener { showToast("Итоги за день - в разработке") }
    }

    private fun setupClickListeners() {
        switchUserButton.setOnClickListener {
            switchUser()
        }
    }

    private fun updateWelcomeText() {
        val prefs: SharedPreferences = getSharedPreferences(prefsName, MODE_PRIVATE)
        val userName = prefs.getString("selected_user_name", "Пользователь")
        welcomeTextView.text = "Добро пожаловать, $userName!"
    }

    private fun updateDate() {
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        dateTextView.text = currentDate
    }

    private fun switchUser() {
        val prefs: SharedPreferences = getSharedPreferences(prefsName, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("selected_user_name")
        editor.remove("selected_user_id")
        editor.apply()

        val intent = Intent(this, UserListActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}