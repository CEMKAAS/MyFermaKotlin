package com.hfad.myferma.incubator.MenuIncubators

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MenuAdapter constructor(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeIncubatorFragment()
            1 -> ArchiveIncubatorFragment()
            else -> HomeIncubatorFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}