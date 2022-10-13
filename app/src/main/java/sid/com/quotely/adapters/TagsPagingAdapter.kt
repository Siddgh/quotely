package sid.com.quotely.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter
import com.firebase.ui.database.paging.LoadingState
import kotlinx.android.synthetic.main.layout_tags_list.view.*
import sid.com.quotely.R
import sid.com.quotely.models.data.TagsListMeta
import sid.com.quotely.utils.NavigationUtils

class TagsPagingAdapter(
    options: DatabasePagingOptions<TagsListMeta>,
    private val activity: Activity,
    private val type: String
) :
    FirebaseRecyclerPagingAdapter<TagsListMeta, TagsPagingAdapter.ViewHolder>(options) {

    class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_tags_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: TagsListMeta) {
        holder.itemView.apply {
            tv_layout_tags_list.text = model.tag
        }
        holder.itemView.setOnClickListener {
            NavigationUtils.AddToTagsList(activity, type, model.tag ?: "")
        }
    }

    override fun onLoadingStateChanged(state: LoadingState) {

    }
}