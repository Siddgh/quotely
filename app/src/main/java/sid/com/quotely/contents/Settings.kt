package sid.com.quotely.contents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.merge_recyclerview_with_header.view.*
import sid.com.quotely.R
import sid.com.quotely.models.groupie.SettingsGroupieModel

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        include_settings.tv_recyclerview_with_header.text = getString(R.string.settings_header)
        include_settings.tv_recyclerview_with_header.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32F)
        val items = mutableListOf<SettingsGroupieModel>()
        for (title in resources.getStringArray(R.array.settings_titles)) {
            items.add(SettingsGroupieModel(this, title))
        }
        val section = Section(items)
        include_settings.rv_recyclerview_with_header.apply {
            layoutManager =
                LinearLayoutManager(
                    baseContext,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(section)
            }
            isNestedScrollingEnabled = false
        }
    }
}
