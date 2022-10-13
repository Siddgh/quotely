package sid.com.quotely

import com.google.firebase.database.FirebaseDatabase

class Offline : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}

