package pl.jitsolutions.agile.presentation.projects.managing

import android.content.ContextWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pl.jitsolutions.agile.R

@BindingAdapter("bindViewPagerToTabs")
fun bindViewPagerToTabs(viewPager: ViewPager, tabLayout: TabLayout) {
    val activity = viewPager.context as? AppCompatActivity
    activity?.apply {
        val titles = arrayListOf(
            getString(R.string.project_adding_screen_tab_create_text),
            getString(R.string.project_adding_screen_tab_join_text)
        )
        supportFragmentManager?.apply {
            viewPager.adapter = ProjectAddingPagerAdapter(titles, this)
            tabLayout.setupWithViewPager(viewPager)
        }
    }
}

@BindingAdapter("bindProjectAddingBackArrowVisibility")
fun bindProjectAddingBackArrowVisibility(view: View, bind: Boolean) {
    if (bind) {
        val wrapper = view.context as? ContextWrapper
        val activity = wrapper?.baseContext as? AppCompatActivity
        activity?.apply {
            setSupportActionBar(view as? Toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}
