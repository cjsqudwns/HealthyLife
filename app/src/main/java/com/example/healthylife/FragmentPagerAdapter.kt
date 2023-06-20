package com.example.healthylife

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    constructor() : this(fragmentActivity = FragmentActivity())
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return CalenderFragment()
            1 -> return HomeFragment()
            2 -> return UserFragment()
            else -> return HomeFragment()
        }
    }
}