package fr.isep.unfinished_firebase_project.data

import android.util.Log
// Importez les classes Firebase nécessaires ici pour les aider un peu
// import com.google.firebase.auth.FirebaseAuth
// import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager {

    // TODO: Initialiser les instances de FirebaseAuth et FirebaseFirestore ici
    // private val auth: FirebaseAuth = ...
    // private val db: FirebaseFirestore = ...

    /* PARTIE 1 : AUTHENTIFICATION */

    fun signUp(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // TODO 1: Créer un utilisateur avec email et mot de passe via Firebase Auth
        // Indice : utiliser createUserWithEmailAndPassword
        Log.e("FirebaseManager", "TODO 1: Implémenter l'inscription")
        onError("Fonctionnalité non implémentée")
    }

    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // TODO 2: Connecter l'utilisateur
        // Indice : utiliser signInWithEmailAndPassword
        Log.e("FirebaseManager", "TODO 2: Implémenter la connexion")
        onError("Fonctionnalité non implémentée")
    }

    fun logout() {
        // TODO 3: Déconnecter l'utilisateur
    }

    fun isUserConnected(): Boolean {
        // TODO 4: Vérifier si un utilisateur est actuellement connecté (currentUser != null)
        return false
    }

    /* PARTIE 2 : FIRESTORE (BASE DE DONNÉES) */

    fun addTask(taskTitle: String) {
        // TODO 5: Ajouter une tâche dans une collection "tasks"
        // L'objet peut être une simple map : hashMapOf("title" to taskTitle, "done" to false)
        Log.e("FirebaseManager", "TODO 5: Implémenter l'ajout de tâche")
    }

    fun getAllTasks(onTasksReceived: (List<String>) -> Unit) {
        // TODO 6: Récupérer toutes les tâches de la collection "tasks" en temps réel
        // Indice : utiliser addSnapshotListener
    }
}