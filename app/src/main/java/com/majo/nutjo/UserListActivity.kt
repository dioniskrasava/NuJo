package com.majo.nutjo

import android.content.DialogInterface
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

    private fun initViews() {
        usersListView = findViewById(R.id.usersListView)
        backButton = findViewById(R.id.backButton)
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            finish() // Закрываем эту Activity и возвращаемся к предыдущей
        }

        usersListView.setOnItemClickListener { parent, view, position, id ->
            val user = usersList[position]
            Toast.makeText(this, "Выбран: ${user.name}", Toast.LENGTH_SHORT).show()
        }

        usersListView.setOnItemLongClickListener { parent, view, position, id ->
            val user = usersList[position]
            showDeleteConfirmationDialog(user, position)
            true // Возвращаем true, чтобы показать, что событие обработано
        }
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