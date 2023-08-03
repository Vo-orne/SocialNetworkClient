package com.example.myprofile.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.myprofile.R
import com.example.myprofile.base.BaseFragment
import com.example.myprofile.databinding.FragmentPagerBinding
import com.example.myprofile.ui.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class PagerFragment : BaseFragment<FragmentPagerBinding>(FragmentPagerBinding::inflate) {

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        viewPager = binding.viewPager

        val tabLayout = binding.tabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab1)
                else -> tab.text = getString(R.string.tab2)
            }
        }.attach()
    }

    fun getViewPager(): ViewPager2 {
        return viewPager
    }

    override fun setListeners() {}
}