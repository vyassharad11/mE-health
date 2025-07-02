package com.mE.Health

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import com.mE.Health.databinding.ActivityHomeBinding
import com.mE.Health.databinding.ActivityHomeNewBinding
import com.mE.Health.feature.HomeFragment
import com.mE.Health.feature.MyPersonaFragment
import com.mE.Health.feature.adapter.HomeMenuAdapter
import com.mE.Health.feature.adapter.RadioButtonListAdapter
import com.mE.Health.models.NavMenuDTO
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (this.applicationContext as MyApplication).setCurrentActivity(this)
        openFragment(HomeFragment())
        initView()
        initNavMenu()
        activeDashboardMenu()
    }

    private fun initView() {
        binding.llMenu.setOnClickListener(this)
        binding.llVoice.setOnClickListener(this)
        binding.llDashboard.setOnClickListener(this)
        binding.rllVoice.setOnClickListener(this)
    }

    private fun openFragment(fragment: Fragment) {
        fragment.enterTransition = Slide(Gravity.END)
        fragment.exitTransition = Slide(Gravity.START)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llMenu -> {
                refreshMenu()
                updateNavMenuVisibility(if (binding.rvNavMenu.isVisible) View.GONE else View.VISIBLE)
                binding.ivMenu.setImageResource(R.drawable.ic_nav_menu_selected)
                binding.tvMenu.setTextColor(ContextCompat.getColor(this, R.color.color_FF6605))
            }

            R.id.llVoice, R.id.rllVoice -> {
//                updateNavMenuVisibility(View.GONE)
//                openDialog()
            }

            R.id.llDashboard -> {
                refreshMenu()
                updateNavMenuVisibility(View.GONE)
                activeDashboardMenu()
                clearBackStack()
                if (getBackStackCount()!! > 0)
                    openFragment(HomeFragment())
            }
        }
    }


    override fun onBackPressed() {
        try {
            if (hideSideNavRequired()) return
            closeKeyboard()
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }

            setBottomNavigationVisibility()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            super.onBackPressed()
        }
    }

    private fun closeKeyboard() {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    fun setBottomNavigationVisibility() {
//        if (supportFragmentManager.backStackEntryCount == 0) {
//            binding.rlBottomMenu.visibility = View.VISIBLE
//        } else {
//            binding.rlBottomMenu.visibility = View.GONE
//        }
    }

    private fun activeDashboardMenu() {
        binding.ivDashboard.setImageResource(R.drawable.ic_nav_dashboard_selected)
        binding.tvDashboard.setTextColor(ContextCompat.getColor(this, R.color.color_FF6605))
    }

    fun updateNavMenuVisibility(visibility: Int) {
        binding.rvNavMenu.visibility = visibility
//        binding.llMenuView.visibility = visibility
        binding.fragmentContainer.setPadding(0, 0, if (visibility == View.VISIBLE) -100 else 0, 0)
        if (visibility == View.GONE) {
//            refreshMenu()
//            activeDashboardMenu()
        }
    }

    fun updateMenu(visibility: Int) {
        binding.rvNavMenu.visibility = visibility
        binding.fragmentContainer.setPadding(0, 0, if (visibility == View.VISIBLE) -100 else 0, 0)
        refreshMenu()
        activeDashboardMenu()
    }

    private fun refreshMenu() {
        binding.ivMenu.setImageResource(R.drawable.ic_nav_menu)
        binding.ivDashboard.setImageResource(R.drawable.ic_nav_dashboard)
        binding.tvMenu.setTextColor(ContextCompat.getColor(this, R.color.color_333333))
        binding.tvVoice.setTextColor(ContextCompat.getColor(this, R.color.color_333333))
        binding.tvDashboard.setTextColor(ContextCompat.getColor(this, R.color.color_333333))
    }

    private fun initNavMenu() {
        binding.rvNavMenu.layoutManager = LinearLayoutManager(this)
        val adapter = HomeMenuAdapter(this, getNavMenuItem())
        binding.rvNavMenu.adapter = adapter
        adapter.apply {
            onItemClickListener = object : HomeMenuAdapter.OnClickCallback,
                RadioButtonListAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    selectedItem = position
                    notifyDataSetChanged()
                    if (position == 1) {
                        updateNavMenuVisibility(View.GONE)
                        homeNavClickAction()
//                        openFragment(MyPersonaFragment())
                    }
                }
            }
        }
    }

    private fun getNavMenuItem(): ArrayList<NavMenuDTO> {
        return ArrayList<NavMenuDTO>().apply {
            add(NavMenuDTO(getString(R.string.dashboard), R.drawable.nav_menu_dashboard))
            add(NavMenuDTO(getString(R.string.my_persona), R.drawable.nav_menu_persona))
            add(NavMenuDTO(getString(R.string.settings), R.drawable.nav_menu_settings))
            add(NavMenuDTO(getString(R.string.contact_us), R.drawable.nav_menu_contact_us))
            add(NavMenuDTO(getString(R.string.logout), R.drawable.nav_menu_logout))
        }
    }

    private fun homeNavClickAction() {
//        val fm: FragmentManager = supportFragmentManager
//        val fragment: HomeFragment =
//            fm.findFragmentById(R.id.fragment_container) as HomeFragment
//        fragment.navClickAction()
        clearBackStack()
        addFragment(MyPersonaFragment())
    }

    private fun addFragment(fragment: Fragment) {
        fragment.enterTransition = Slide(Gravity.END)
        fragment.exitTransition = Slide(Gravity.START)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .addToBackStack("HomeFragment")
            .commitAllowingStateLoss()
    }

    private fun clearBackStack() {
        try {
            val manager = supportFragmentManager
            for (i in 0 until manager.backStackEntryCount) {
                val first = manager.getBackStackEntryAt(i)
                manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getBackStackCount(): Int? {
        return supportFragmentManager?.backStackEntryCount
    }

    fun hideSideNavRequired(): Boolean {
        if (binding.rvNavMenu.isVisible) {
            binding.rvNavMenu.visibility = View.GONE
            binding.fragmentContainer.setPadding(0, 0, 0, 0)
            return true
        }
        return false
    }
}