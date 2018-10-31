package pl.jitsolutions.agile.repository.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

inline fun <reified T> DocumentSnapshot.toFirebaseObject() = this.toObject(T::class.java)

inline fun <reified T> QuerySnapshot?.toFirebaseObjects() =
    this?.toObjects(T::class.java) ?: emptyList()