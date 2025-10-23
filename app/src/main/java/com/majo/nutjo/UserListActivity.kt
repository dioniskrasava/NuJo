package com.majo.nutjo

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserListActivity : AppCompatActivity() {

    private lateinit var usersListView: ListView
    private lateinit var backButton: Button
    private var usersList = mutableListOf<User>()
    private lateinit var adapter: ArrayAdapter<User>

    private val prefsName = "NutritionAppPrefs"
    private val keyUsers = "key_users"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        initViews()
        setupClickListeners()
        loadUsers()
        setupListView()
    }

    // инициал. объектов приложения
    private fun initViews() {
        usersListView = findViewById(R.id.usersListView)
        backButton = findViewById(R.id.backButton)
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            // Вместо finish() создаем явный Intent к MainActivity
            val intent = Intent(this, MainActivity::class.java)

            // Очищаем стек навигации, чтобы нельзя было вернуться назад к UserListActivity
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent)
            finish() // Закрываем текущую Activity
        }

        usersListView.setOnItemClickListener { parent, view, position, id ->
            val user = usersList[position]
            showUserActionsDialog(user, position)
        }

        usersListView.setOnItemLongClickListener { parent, view, position, id ->
            val user = usersList[position]
            showDeleteConfirmationDialog(user, position)
            true // Возвращаем true, чтобы показать, что событие обработано
        }
    }


    private fun showUserActionsDialog(user: User, position: Int) {
        val options = arrayOf("Выбрать пользователя", "Редактировать", "Удалить")

        AlertDialog.Builder(this)
            .setTitle("Действия с пользователем: ${user.name}")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> selectUser(user) // Выбрать
                    1 -> editUser(user, position) // Редактировать
                    2 -> showDeleteConfirmationDialog(user, position) // Удалить
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }


    private fun selectUser(user: User) {
        val prefs = getSharedPreferences("NutritionAppPrefs", MODE_PRIVATE)
        val editor = prefs.edit()

        // Сохраняем ID выбранного пользователя (можно использовать имя как идентификатор)
        editor.putString("selected_user_name", user.name)
        editor.putInt("selected_user_id", usersList.indexOfFirst { it.name == user.name })
        editor.apply()

        Toast.makeText(this, "Пользователь ${user.name} выбран", Toast.LENGTH_SHORT).show()

        // Переходим к экрану продуктов
        val intent = Intent(this, ProductActivity::class.java)
        startActivity(intent)
    }


    private fun editUser(user: User, position: Int) {
        // Создаем диалог для редактирования
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_user, null)
        val nameEditText = dialogView.findViewById<TextInputEditText>(R.id.editNameEditText)
        val heightEditText = dialogView.findViewById<TextInputEditText>(R.id.editHeightEditText)
        val weightEditText = dialogView.findViewById<TextInputEditText>(R.id.editWeightEditText)
        val ageEditText = dialogView.findViewById<TextInputEditText>(R.id.editAgeEditText)

        // Заполняем текущими значениями
        nameEditText.setText(user.name)
        heightEditText.setText(user.height.toString())
        weightEditText.setText(user.weight.toString())
        ageEditText.setText(user.age.toString())

        AlertDialog.Builder(this)
            .setTitle("Редактировать пользователя")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { dialog, which ->
                val newName = nameEditText.text.toString().trim()
                val newHeight = heightEditText.text.toString().toIntOrNull() ?: user.height
                val newWeight = weightEditText.text.toString().toFloatOrNull() ?: user.weight
                val newAge = ageEditText.text.toString().toIntOrNull() ?: user.age

                if (newName.isNotEmpty()) {
                    val updatedUser = User(newName, newHeight, newWeight, newAge)
                    usersList[position] = updatedUser
                    saveUsersToSharedPreferences()
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Пользователь обновлен", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }



    private fun loadUsers() {
        val prefs: SharedPreferences = getSharedPreferences(prefsName, MODE_PRIVATE)
        val usersJson = prefs.getString(keyUsers, "[]")

        val gson = Gson()
        val type = object : TypeToken<MutableList<User>>() {}.type
        usersList = gson.fromJson(usersJson, type) ?: mutableListOf()
    }

    private fun setupListView() {
        adapter = object : ArrayAdapter<User>(
            this,
            R.layout.user_item,
            R.id.userNameTextView,
            usersList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: layoutInflater.inflate(R.layout.user_item, parent, false)
                val user = getItem(position) ?: return view

                val userNameTextView = view.findViewById<TextView>(R.id.userNameTextView)
                val userDetailsTextView = view.findViewById<TextView>(R.id.userDetailsTextView)

                userNameTextView.text = user.name
                userDetailsTextView.text = "Рост: ${user.height}см, Вес: ${user.weight}кг, Возраст: ${user.age}"

                return view
            }
        }
        usersListView.adapter = adapter
    }

    private fun showDeleteConfirmationDialog(user: User, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Удаление пользователя")
            .setMessage("Вы уверены, что хотите удалить пользователя \"${user.name}\"?")
            .setPositiveButton("Удалить") { dialog, which ->
                deleteUser(position)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun deleteUser(position: Int) {
        if (position < usersList.size) {
            val deletedUser = usersList[position]
            usersList.removeAt(position)
            saveUsersToSharedPreferences()
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Пользователь \"${deletedUser.name}\" удален", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUsersToSharedPreferences() {
        val prefs: SharedPreferences = getSharedPreferences(prefsName, MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        editor.putString(keyUsers, gson.toJson(usersList))
        editor.apply()
    }
}