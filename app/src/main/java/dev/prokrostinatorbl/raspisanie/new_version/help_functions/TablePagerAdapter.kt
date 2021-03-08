package dev.prokrostinatorbl.raspisanie.new_version.help_functions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import moxy.MvpAppCompatFragment

class TablePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val mFragmentList: ArrayList<MvpAppCompatFragment> = ArrayList()



    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFrag(frag: MvpAppCompatFragment){
        mFragmentList.add(frag)
    }

}