package com.example.myprofile.presentation.ui.fragments.pager

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.myprofile.R
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.databinding.FragmentPagerBinding
import com.example.myprofile.presentation.ui.fragments.pager.adapter.ViewPagerAdapter
import com.example.myprofile.presentation.ui.fragments.pager.adapter.utils.ViewPagerFragments
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for displaying a ViewPager with tabs.
 */
@AndroidEntryPoint
class PagerFragment : BaseFragment<FragmentPagerBinding>(FragmentPagerBinding::inflate) {

    /**
     * Called when the fragment's view is created.
     *
     * @param view The fragment's view.
     * @param savedInstanceState The saved state of the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the ViewPager with the adapter
        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.viewPager.offscreenPageLimit = 1  // Limit to one page offscreen

        // Set up the TabLayoutMediator to link TabLayout with ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (ViewPagerFragments.entries[position]) {
                ViewPagerFragments.PROFILE_FRAGMENT -> tab.text = getString(R.string.tab1)
                ViewPagerFragments.CONTACTS_FRAGMENT -> tab.text = getString(R.string.tab2)
            }
        }.attach()
    }

    /**
     * Returns the ViewPager2 instance.
     *
     * @return The ViewPager2 instance.
     */
    fun getViewPager(): ViewPager2 = binding.viewPager

    /**
     * Method to set event listeners.
     * Currently not implemented.
     */
    override fun setListeners() {}
}