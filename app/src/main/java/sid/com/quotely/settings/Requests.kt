package sid.com.quotely.settings

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_requests.*
import sid.com.quotely.R
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.utils.RealtimeDatabaseUtils
import sid.com.quotely.utils.StringUtils

class Requests : AppCompatActivity() {

    var typeSection = GenericConstants.MOVIE
    var title = ""
    var comment = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)

        fab_requests_movies.setOnClickListener {
            typeSectionSelected(GenericConstants.MOVIE)
        }
        fab_requests_tvshows.setOnClickListener {
            typeSectionSelected(GenericConstants.TVSHOW)
        }
        fab_requests_animes.setOnClickListener {
            typeSectionSelected(GenericConstants.ANIME)
        }
        fab_requests_books.setOnClickListener {
            typeSectionSelected(GenericConstants.BOOK)
        }
        fab_requests_legends.setOnClickListener {
            typeSectionSelected(GenericConstants.LEGEND)
        }

        fab_requests_submit.setOnClickListener {
            if (ed_requests_name.text?.trim()?.isNotEmpty() == true && ed_requests_name.text?.trim()
                    ?.isNotBlank() == true
            ) {
                title = ed_requests_name.text.toString()
                comment = ed_requests_comment.text.toString()
                RealtimeDatabaseUtils.writeRequestsToFirebase(
                    typeSection, title, comment
                ) {
                    Toast.makeText(
                        baseContext,
                        "Request Sent Successfully, We will notify once $title is available",
                        Toast.LENGTH_LONG
                    ).show()
                    ed_requests_comment.text?.clear()
                    ed_requests_name.text?.clear()
                }
            } else {
                Toast.makeText(
                    baseContext,
                    "Please specify the title of the $typeSection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun typeSectionSelected(type: String) {
        typeSection = type
        tv_requests_name_title.text = "Name of the $typeSection?"
        fab_requests_submit.backgroundTintList = ColorStateList.valueOf(
            StringUtils.getTypeColor(
                baseContext,
                typeSection
            )
        )
    }

}
