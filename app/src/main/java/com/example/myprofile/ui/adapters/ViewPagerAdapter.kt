package com.example.myprofile.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myprofile.ui.fragments.contacts.MyContactsFragment
import com.example.myprofile.ui.fragments.MyProfileFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = ViewPagerFragments.values().size // Number of fragments

    override fun createFragment(position: Int): Fragment =
        when (ViewPagerFragments.values()[position]) {
            ViewPagerFragments.PROFILE_FRAGMENT -> MyProfileFragment()
            ViewPagerFragments.CONTACTS_FRAGMENT -> MyContactsFragment()
        }
}
