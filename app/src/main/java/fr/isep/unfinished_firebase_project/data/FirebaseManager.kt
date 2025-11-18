package fr.isep.unfinished_firebase_project.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    /* PARTIE 1 : AUTHENTIFICATION */

    fun signUp(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Erreur inconnue") }
    }

    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Erreur inconnue") }
    }

    fun logout() {
        auth.signOut()
    }

    fun isUserConnected(): Boolean {
        return auth.currentUser != null
    }

    /* PARTIE 2 : FIRESTORE */

    fun addTask(taskTitle: String) {
        val task = hashMapOf(
            "title" to taskTitle,
            "completed" to false,
            "userId" to auth.currentUser?.uid // Bonne pratique : lier la donnée à l'user
        )

        db.collection("tasks")
            .add(task)
            .addOnSuccessListener {
                // Succès (optionnel de gérer le callback ici pour un tuto simple)
            }
    }

    // Exemple avec écoute en temps réel (SnapshotListener)
    fun listenToTasks(onTasksReceived: (List<String>) -> Unit) {
        db.collection("tasks")
            // Optionnel : filtrer par utilisateur
            // .whereEqualTo("userId", auth.currentUser?.uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val tasks = ArrayList<String>()
                for (doc in value!!) {
                    doc.getString("title")?.let { tasks.add(it) }
                }
                onTasksReceived(tasks)
            }
    }
}