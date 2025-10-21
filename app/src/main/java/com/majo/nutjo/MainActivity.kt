package com.majo.nutjo

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    // Объявляем переменные для наших View-элементов
    private lateinit var heightEditText: TextInputEditText
    private lateinit var weightEditText: TextInputEditText
    private lateinit var ageEditText: TextInputEditText
    private lateinit var saveButton: Button

    // Ключи для сохранения в SharedPreferences
    private val prefsName = "UserProfilePrefs"
    private val keyHeight = "key_height"
    private val keyWeight = "key_weight"
    private val keyAge = "key_age"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим наши View-элементы по ID и присваиваем переменным
        heightEditText = findViewById(R.id.heightEditText)
        weightEditText = findViewById(R.id.weightEditText)
        ageEditText = findViewById(R.id.ageEditText)
        saveButton = findViewById(R.id.saveProfileButton)

        // Загружаем сохраненные данные при запуске приложения
        loadProfileData()

        // Вешаем обработчик нажатия на кнопку
        saveButton.setOnClickListener {
            saveProfileData()
        }
    }

    private fun saveProfileData() {
        // Получаем текст из полей ввода и обрезаем пробелы по краям
        val heightText = heightEditText.text.toString().trim()
        val weightText = weightEditText.text.toString().trim()
        val ageText = ageEditText.text.toString().trim()

        // Проверяем, что поля не пустые
        if (heightText.isEmpty() || weightText.isEmpty() || ageText.isEmpty()) {
            // Показываем пользователю маленькое всплывающее сообщение
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return // Выходим из функции, если какое-то поле пустое
        }

        // Преобразуем текст в числа. Используем безопасный вызов (?.)
        val height = heightText.toIntOrNull()
        val weight = weightText.toFloatOrNull()
        val age = ageText.toIntOrNull()

        // Проверяем, что преобразование прошло успешно
        if (height == null || weight == null || age == null) {
            Toast.makeText(this, "Введите корректные числа", Toast.LENGTH_SHORT).show()
            return
        }

        // Сохраняем данные в SharedPreferences
        val prefs: SharedPreferences = getSharedPreferences(prefsName, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()

        editor.putInt(keyHeight, height)
        editor.putFloat(keyWeight, weight)
        editor.putInt(keyAge, age)

        // Применяем изменения
        editor.apply()

        // Сообщаем пользователю об успехе
        Toast.makeText(this, "Профиль сохранен!", Toast.LENGTH_SHORT).show()
    }

    private fun loadProfileData() {
        // Загружаем данные из SharedPreferences
        val prefs: SharedPreferences = getSharedPreferences(prefsName, MODE_PRIVATE)

        val savedHeight = prefs.getInt(keyHeight, 0) // 0 - значение по умолчанию
        val savedWeight = prefs.getFloat(keyWeight, 0f)
        val savedAge = prefs.getInt(keyAge, 0)

        // Если данные есть (не равны 0), то устанавливаем их в поля ввода
        if (savedHeight != 0) {
            heightEditText.setText(savedHeight.toString())
        }
        if (savedWeight != 0f) {
            weightEditText.setText(savedWeight.toString())
        }
        if (savedAge != 0) {
            ageEditText.setText(savedAge.toString())
        }
    }
}