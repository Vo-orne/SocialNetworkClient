package com.example.myprofile.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myprofile.ui.fragments.MyContactsFragment
import com.example.myprofile.ui.fragments.MyProfileFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // Number of fragments

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyProfileFragment()
            1 -> MyContactsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
