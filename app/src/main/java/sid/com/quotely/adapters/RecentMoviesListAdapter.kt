package sid.com.quotely.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.android.synthetic.main.layout_movie_large_card.view.*
import sid.com.quotely.R
import sid.com.quotely.constants.FirebaseConstants
import sid.com.quotely.models.data.MoviesMeta
import sid.com.quotely.utils.FirebaseStorageUtils
import sid.com.quotely.utils.NavigationUtils

class RecentMoviesListAdapter(
    options: FirebaseRecyclerOptions<MoviesMeta>,
    private val activity: Activity
) : FirebaseRecyclerAdapter<MoviesMeta, RecentMoviesListAdapter.ViewHolder>(options) {
    class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_movie_large_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: MoviesMeta) {
        holder.itemView.apply {
            Glide.with(activity.baseContext).load(
                FirebaseStorageUtils.getMoviePosterReferenceById(
                    model.fromId ?: FirebaseConstants.anon, model.type ?: ""
                )
            )
                .into(iv_layout_movie_large_poster)
            tv_layout_movie_large_title.text = model.from
            setOnClickListener {
                NavigationUtils.AddToIndividualTypeSection(activity, model)
            }
        }
    }

}