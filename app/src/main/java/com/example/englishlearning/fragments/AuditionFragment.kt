package com.example.englishlearning.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.englishlearning.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class AuditionFragment : Fragment() {

    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val auditionFragment = inflater.inflate(R.layout.fragment_audition, container, false)
        tabLayout = auditionFragment.findViewById(R.id.audition_tab_layout)
        viewPager = auditionFragment.findViewById(R.id.view_pager_auditions)
        viewPager.adapter = fragmentAdapter(this)
        TabLayoutMediator(tabLayout, viewPager){tab, position ->
            if (position == 0) tab.text = "Доступные"
            if (position == 1) tab.text = "Выполненные"
        }.attach()
        return auditionFragment
    }

    class fragmentAdapter(fragment : Fragment) : FragmentStateAdapter(fragment){
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> AuditionAvaliableFragment().newInstance()
                1 -> AuditionCompleteFragment().newInstance()
                else -> AuditionAvaliableFragment().newInstance()
            }

        }

    }

}