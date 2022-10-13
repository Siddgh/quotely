package sid.com.quotely.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter
import com.firebase.ui.database.paging.LoadingState
import kotlinx.android.synthetic.main.layout_movie_card.view.*
import sid.com.quotely.R
import sid.com.quotely.constants.FirebaseConstants
import sid.com.quotely.models.data.MoviesMeta
import sid.com.quotely.utils.FirebaseStorageUtils
import sid.com.quotely.utils.NavigationUtils

class TypeSectionListAdapter(
    options: DatabasePagingOptions<MoviesMeta>,
    private val activity: Activity
) :
    FirebaseRecyclerPagingAdapter<MoviesMeta, TypeSectionListAdapter.ViewHolder>(options) {

    class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_movie_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: MoviesMeta) {
        holder.itemView.apply {
            Glide.with(context)
                .load(
                    FirebaseStorageUtils.getMoviePosterReferenceById(
                        model.fromId ?: FirebaseConstants.anon, model.type ?: ""
                    )
                )
                .centerCrop()
                .into(iv_layout_movie_poster)
            setOnClickListener {
                NavigationUtils.AddToIndividualTypeSection(activity, model)
            }
            tv_layout_movie_title.text = model.from
        }

    }

    override fun onLoadingStateChanged(state: LoadingState) {

    }
}