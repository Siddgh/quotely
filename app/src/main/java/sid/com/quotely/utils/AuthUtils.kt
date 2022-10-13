package sid.com.quotely.utils

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import sid.com.quotely.R
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.models.data.UserMeta
import java.text.SimpleDateFormat
import java.util.*

object AuthUtils {

    fun getUserMeta(): UserMeta {
        val user = FirebaseAuth.getInstance().currentUser
        return UserMeta(
            user?.displayName,
            user?.email,
            user?.photoUrl.toString(),
            user?.uid,
            SimpleDateFormat(
                GenericConstants.dateFormat,
                Locale.ENGLISH
            ).format(Calendar.getInstance().time)
        )
    }

    fun setUserProfile(
        userName: TextView,
        userEmail: TextView,
        userProfile: ImageView,
        context: Context
    ) {
        val userMeta = getUserMeta()
        userName.text = userMeta.displayName
        userEmail.text = userMeta.email
        Glide.with(context).load(userMeta.photoUrl).placeholder(R.drawable.ic_user)
            .into(userProfile)
    }

}