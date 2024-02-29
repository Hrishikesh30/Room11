package com.example.roomtest11

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val usersText = remember { mutableStateOf("") } // State to hold user data

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Display(usersText.value) // Pass the user data to Exam composable
            }

            // Establish database connection
            connectToDatabase(usersText)
        }
    }

    private fun connectToDatabase(usersText: MutableState<String>) {
        lifecycleScope.launch(Dispatchers.IO) {
            val context: Context = applicationContext ?: return@launch
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "dataTest"
            ).build()

            val userDao = db.userDao()

            val users: List<User> = userDao.getAll()

            val usersString = buildString {
                for (user in users) {
                    append("User ID: ${user.id}, Name: ${user.firstName} ${user.lastName}\n")
                }
            }

            // Update UI with the retrieved user data
            usersText.value = usersString
        }
    }
}

@Composable
fun Display(usersText: String) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(usersText) // Display the user data

        }
    }

