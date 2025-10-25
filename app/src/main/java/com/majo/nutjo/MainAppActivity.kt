package com.majo.nutjo

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.appbar.MaterialToolbar

class MainAppActivity : AppCompatActivity() {

    private lateinit var welcomeTextView: TextView
    private lateinit var dateTextView: TextView

    private val prefsName = "NutritionAppPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_app)

        // Устанавливаем Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        initViews()
        updateWelcomeText()
        updateDate()
    }

    private fun initViews() {
        welcomeTextView = findViewById(R.id.welcomeTextView)
        dateTextView = findViewById(R.id.dateTextView)

        // Оставляем только основные карточки, остальное убираем
        val addProductCard = findViewById<android.view.View>(R.id.addProductCard)
        val addMealCard = findViewById<android.view.View>(R.id.addMealCard)
        val nutritionSummaryCard = findViewById<android.view.View>(R.id.nutritionSummaryCard)

        addProductCard.setOnClickListener {
            showToast("Добавление продукта - в разработке")
        }
        addMealCard.setOnClickListener {
            showToast("Добавление приема пищи - в разработке")
        }
        nutritionSummaryCard.setOnClickListener {
            showToast("Итоги за день - в разработке")
        }
    }

    // 1. Создаем меню (три точки)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // 2. Обрабатываем нажатия в меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_user -> {
                switchUser()
                true
            }
            R.id.action_products_list -> {
                showToast("Список продуктов - в разработке")
                true
            }
            R.id.action_meals_list -> {
                showToast("Список приемов пищи - в разработке")
                true
            }
            R.id.action_create_dish -> {
                showToast("Создание блюда - в разработке")
                true
            }
            R.id.action_nutrition_summary -> {
                showToast("Итоги за день - в разработке")
                true
            }
            R.id.action_settings -> {
                showToast("Настройки - в разработке")
                true
            }
            else -> super.onOptionsItemSelected(item)
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