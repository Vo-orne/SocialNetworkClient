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

@AndroidEntryPoint
class PagerFragment : BaseFragment<FragmentPagerBinding>(FragmentPagerBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.viewPager.offscreenPageLimit = 1

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (ViewPagerFragments.values()[position]) {
                ViewPagerFragments.PROFILE_FRAGMENT -> tab.text = getString(R.string.tab1)
                ViewPagerFragments.CONTACTS_FRAGMENT -> tab.text = getString(R.string.tab2)
            }
        }.attach()
    }

    fun getViewPager(): ViewPager2 = binding.viewPager

    override fun setListeners() {}
}