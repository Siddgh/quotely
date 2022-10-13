package sid.com.quotely.models.groupie

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.firebase.ui.auth.AuthUI
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.layout_settings.view.*
import sid.com.quotely.BuildConfig
import sid.com.quotely.R
import sid.com.quotely.SignIn
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.settings.Credits
import sid.com.quotely.settings.Requests
import sid.com.quotely.utils.RealtimeDatabaseUtils

class SettingsGroupieModel(private val context: Context, private val title: String) : Item() {
    private val icons = mutableListOf(
        R.drawable.ic_ratings,
        R.drawable.ic_feedback,
        R.drawable.ic_developer,
        R.drawable.ic_credits,
        R.drawable.ic_requests,
        R.drawable.ic_delete,
        R.drawable.ic_about
    )
    private val desc = context.resources.getStringArray(R.array.settings_titles_desc)

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tv_layout_settings.text = title
            iv_layout_settings.setImageResource(icons[position])
            if (icons[position] == R.drawable.ic_about) {
                tv_layout_settings_desc.text = "${desc[position]} ${BuildConfig.VERSION_NAME}"
            } else {
                tv_layout_settings_desc.text = desc[position]
            }

        }
        viewHolder.itemView.setOnClickListener {
            when {
                icons[position] == R.drawable.ic_about -> {
                    Toast.makeText(
                        context,
                        "Version : ${BuildConfig.VERSION_NAME}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                icons[position] == R.drawable.ic_ratings -> {
                    val uri =
                        Uri.parse("market://details?id=" + context.packageName)
                    val rateit = Intent(Intent.ACTION_VIEW, uri)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rateit.addFlags(
                            Intent.FLAG_ACTIVITY_NO_HISTORY or
                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                        )
                    } else {
                        rateit.addFlags(
                            Intent.FLAG_ACTIVITY_NO_HISTORY or
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                        )
                    }
                    try {
                        context.startActivity(rateit)
                    } catch (e: ActivityNotFoundException) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)
                            )
                        )
                    }
                }
                icons[position] == R.drawable.ic_feedback -> {
                    val emailIntent = Intent(Intent.ACTION_SENDTO)
                    emailIntent.data = Uri.parse("mailto: quotely.in@gmail.com")
                    context.startActivity(Intent.createChooser(emailIntent, "Contact"))
                }
                icons[position] == R.drawable.ic_developer -> {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://in.linkedin.com/in/siddhesh-dighe-a7b274107")
                    )
                    context.startActivity(browserIntent)
                }
                icons[position] == R.drawable.ic_credits -> {
                    context.startActivity(Intent(context, Credits::class.java))
                }
                icons[position] == R.drawable.ic_requests -> {
                    context.startActivity(Intent(context, Requests::class.java))
                }
                icons[position] == R.drawable.ic_delete -> {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle(GenericConstants.eraseYourAccount)
                    builder.setMessage(GenericConstants.eraseYourAccountDesc)
                    builder.setIcon(R.drawable.ic_delete)
                    builder.setPositiveButton(
                        GenericConstants.erasePositive
                    ) { _, _ ->
                        RealtimeDatabaseUtils.individualUserDatabaseReference.setValue(null)
                        AuthUI.getInstance().delete(context).addOnCompleteListener {
                            Toast.makeText(
                                context,
                                GenericConstants.UserAccountDeleted,
                                Toast.LENGTH_LONG
                            )
                                .show()
                            val intent = Intent(context, SignIn::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        }

                    }
                    builder.setNegativeButton(
                        GenericConstants.eraseNegative
                    ) { _, _ ->

                    }
                    builder.show()
                }
            }
        }
    }

    override fun getLayout() = R.layout.layout_settings
}