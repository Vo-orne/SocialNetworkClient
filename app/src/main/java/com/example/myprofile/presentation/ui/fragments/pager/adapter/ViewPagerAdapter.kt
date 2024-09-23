package com.example.myprofile.presentation.ui.fragments.pager.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myprofile.presentation.ui.fragments.pager.adapter.utils.ViewPagerFragments
import com.example.myprofile.presentation.ui.fragments.contacts.MyContactsFragment
import com.example.myprofile.presentation.ui.fragments.my_profile.MyProfileFragment

/**
 * Adapter for managing fragments in the ViewPager2.
 *
 * @param fragment The fragment used to manage the ViewPager2.
 */
class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * Returns the number of fragments in the ViewPager2.
     */
    override fun getItemCount(): Int = ViewPagerFragments.entries.size

    /**
     * Creates and returns the fragment for the given position.
     *
     * @param position The position of the fragment in the ViewPager2.
     * @return The fragment for the specified position.
     */
    override fun createFragment(position: Int): Fragment =
        when (ViewPagerFragments.entries[position]) {
            ViewPagerFragments.PROFILE_FRAGMENT -> MyProfileFragment()
            ViewPagerFragments.CONTACTS_FRAGMENT -> MyContactsFragment()
        }
}