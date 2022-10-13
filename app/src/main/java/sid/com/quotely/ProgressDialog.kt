package sid.com.quotely

import android.app.Activity
import androidx.appcompat.app.AlertDialog

class ProgressDialog(val activity: Activity) {

    lateinit var alertDialog: AlertDialog

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.activity_progress_dialog, null))
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun dismissDialog() {
        alertDialog.dismiss()
    }

}