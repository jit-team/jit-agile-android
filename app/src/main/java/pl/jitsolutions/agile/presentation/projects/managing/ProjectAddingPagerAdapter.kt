package pl.jitsolutions.agile.presentation.projects.managing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ProjectAddingPagerAdapter(
    private val titles: ArrayList<String>,
    fm: FragmentManager
) :
    FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int = titles.size

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ProjectCreationFragment()
            1 -> ProjectJoiningFragment()
            else -> throw Exception()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}