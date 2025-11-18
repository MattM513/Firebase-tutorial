package fr.isep.unfinished_firebase_project.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager {

    // Initialisation des instances
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    /* PARTIE 1 : AUTHENTIFICATION */

    fun signUp(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            onError("Email ou mot de passe vide")
            return
        }

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.localizedMessage ?: "Erreur inconnue")
            }
    }

    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            onError("Email ou mot de passe vide")
            return
        }

        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.localizedMessage ?: "Erreur de connexion")
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun isUserConnected(): Boolean {
        return auth.currentUser != null
    }

    /* PARTIE 2 : FIRESTORE (BASE DE DONNÉES) */

    fun addTask(taskTitle: String) {
        if (taskTitle.isBlank()) return

        // On crée un objet simple pour Firestore
        val task = hashMapOf(
            "title" to taskTitle,
            "timestamp" to System.currentTimeMillis() // Utile pour trier
        )

        db.collection("tasks")
            .add(task)
            .addOnSuccessListener {
                Log.d("FirebaseManager", "Tâche ajoutée avec l'ID: ${it.id}")
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseManager", "Erreur lors de l'ajout", e)
            }
    }

    fun getAllTasks(onTasksReceived: (List<String>) -> Unit) {
        // addSnapshotListener permet d'avoir les mises à jour en temps réel
        db.collection("tasks")
            .orderBy("timestamp") // Optionnel: trier par temps
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("FirebaseManager", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val taskList = ArrayList<String>()
                if (snapshot != null) {
                    for (doc in snapshot) {
                        // On récupère le champ "title" défini dans addTask
                        doc.getString("title")?.let { title ->
                            taskList.add(title)
                        }
                    }
                }
                onTasksReceived(taskList)
            }
    }
}