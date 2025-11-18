package fr.isep.unfinished_firebase_project

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isep.unfinished_firebase_project.data.FirebaseManager
import fr.isep.unfinished_firebase_project.ui.theme.UnfinishedFirebaseProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firebaseManager = FirebaseManager()
        setContent {
            UnfinishedFirebaseProjectTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    FirebaseTutorialScreen(firebaseManager)
                }
            }
        }
    }
}

@Composable
fun FirebaseTutorialScreen(firebaseManager: FirebaseManager) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var taskTitle by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf<String>()) }
    var isConnected by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Observer les changements d'état de connexion (Optionnel pour l'instant)
    // Pour l'exercice, on vérifie manuellement au chargement ou après action
    LaunchedEffect(Unit) {
        isConnected = firebaseManager.isUserConnected()
        if (isConnected) {
            firebaseManager.getAllTasks { updatedList -> tasks = updatedList }
        }
    }

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Firebase Tutorial", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (!isConnected) {
            // Login / Signup UI
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = {
                    firebaseManager.signUp(email, password,
                        onSuccess = {
                            Toast.makeText(context, "Compte créé !", Toast.LENGTH_SHORT).show()
                            isConnected = true
                        },
                        onError = { error -> Toast.makeText(context, "Erreur: $error", Toast.LENGTH_SHORT).show() }
                    )
                }) { Text("S'inscrire") }

                Button(onClick = {
                    firebaseManager.login(email, password,
                        onSuccess = {
                            Toast.makeText(context, "Connecté !", Toast.LENGTH_SHORT).show()
                            isConnected = true
                            // Charger les tâches après connexion
                            firebaseManager.getAllTasks { updatedList -> tasks = updatedList }
                        },
                        onError = { error -> Toast.makeText(context, "Erreur: $error", Toast.LENGTH_SHORT).show() }
                    )
                }) { Text("Se connecter") }
            }
        } else {
            // Task List UI
            Text("Bienvenue ! Vous êtes connecté.")
            Button(onClick = {
                firebaseManager.logout()
                isConnected = false
                tasks = emptyList()
            }) { Text("Se déconnecter") }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                TextField(value = taskTitle, onValueChange = { taskTitle = it }, label = { Text("Nouvelle tâche") }, modifier = Modifier.weight(1f))
                Button(onClick = {
                    firebaseManager.addTask(taskTitle)
                    taskTitle = ""
                }) { Text("Ajouter") }
            }

            LazyColumn(modifier = Modifier.fillMaxHeight().padding(top = 16.dp)) {
                items(tasks) { task ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text(text = task, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}