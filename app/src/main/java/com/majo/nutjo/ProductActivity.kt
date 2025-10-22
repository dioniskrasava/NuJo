package com.majo.nutjo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        // Здесь будет логика работы с продуктами
        // Пока просто покажем приветствие

        val prefs = getSharedPreferences("NutritionAppPrefs", MODE_PRIVATE)
        val selectedUserName = prefs.getString("selected_user_name", "")

        Toast.makeText(this, "Добро пожаловать, $selectedUserName!", Toast.LENGTH_SHORT).show()
    }
}
