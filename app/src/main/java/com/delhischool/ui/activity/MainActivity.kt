package com.delhischool.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.delhischool.R
import com.delhischool.databinding.ActivityMainBinding
import com.delhischool.ui.fragments.StudentDataFragment
import com.delhischool.ui.fragments.TeacherDataFragment
import com.delhischool.ui.viewModel.MainActivityViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mContext: Context
    private lateinit var viewPageAdapter: ViewPagerFragmentAdapter
    private var isTeacher = false
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mContext = this
        mainViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        setUpViewPager()

        mainViewModel.isTeacher.observe(this, Observer {
            isTeacher = it
        })

        binding.fabIcon.setOnClickListener {
            startActivity(Intent(mContext, AddUpdateUserDetailsActivity::class.java)
                    .putExtra("for", isTeacher))
        }
    }

    private fun setUpViewPager() {
        viewPageAdapter = ViewPagerFragmentAdapter()
        binding.viewPagerMain.adapter = viewPageAdapter
        binding.viewPagerMain.offscreenPageLimit = 2

        binding.viewPagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    mainViewModel.isTeacher.postValue(false)
                } else {
                    mainViewModel.isTeacher.postValue(true)
                }
            }
        })

        TabLayoutMediator(binding.tabLayout, binding.viewPagerMain,
                TabLayoutMediator.TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                    if (position == 0) {
                        tab.text = getString(R.string.student)
                    } else {
                        tab.text = getString(R.string.teachers)
                    }
                }
        ).attach()
    }

    inner class ViewPagerFragmentAdapter : FragmentStateAdapter(this) {
        private val fragment: Array<Fragment> = arrayOf(
                StudentDataFragment(),
                TeacherDataFragment()
        )

        override fun createFragment(position: Int): Fragment {
            return fragment[position]
        }

        override fun getItemCount(): Int {
            return 2
        }
    }
}