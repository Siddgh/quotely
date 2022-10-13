package sid.com.quotely.models.groupie

import android.view.View
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.layout_tags_chip.view.*
import sid.com.quotely.R
import sid.com.quotely.contents.Favourites
import sid.com.quotely.utils.StringUtils

class FavouritesTypeChipsGroupieModel(private val type: String, private val activity: Favourites) :
    Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_layout_tags.text = type
        viewHolder.itemView.iv_layout_tags.visibility = View.VISIBLE
        viewHolder.itemView.iv_layout_tags.setImageResource(StringUtils.getTypeIcon(type))
        viewHolder.itemView.setOnClickListener {
            activity.setUpFavouritesRecyclerList(type)
        }
    }

    override fun getLayout() = R.layout.layout_tags_chip

}