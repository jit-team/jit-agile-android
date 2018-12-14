package pl.jitsolutions.agile.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val firebaseModule = Kodein.Module(name = "FirebaseModule") {
    bind<FirebaseFirestore>() with singleton { FirebaseFirestore.getInstance() }
    bind<FirebaseFunctions>() with singleton { FirebaseFunctions.getInstance() }
    bind<FirebaseAuth>() with singleton { FirebaseAuth.getInstance() }
}