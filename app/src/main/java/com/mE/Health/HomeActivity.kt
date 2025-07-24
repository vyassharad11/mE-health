package com.mE.Health

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import com.mE.Health.databinding.ActivityHomeNewBinding
import com.mE.Health.feature.ContactUsFragment
import com.mE.Health.feature.HomeFragment
import com.mE.Health.feature.MyPersonaFragment
import com.mE.Health.feature.SettingFragment
import com.mE.Health.feature.adapter.HomeMenuAdapter
import com.mE.Health.feature.adapter.RadioButtonListAdapter
import com.mE.Health.models.NavMenuDTO
import com.mE.Health.viewmodels.mockData.MockDataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeNewBinding
    private var isNavActive = false
    private var navMenuAdapter: HomeMenuAdapter? = null
    private val mockDataViewModel: MockDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isNavActive = false

        binding = ActivityHomeNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (this.applicationContext as MyApplication).setCurrentActivity(this)
        observeResponse()
        mockDataViewModel.insertPatients()
        mockDataViewModel.getProviderList()

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
                isNavActive = true
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
                isNavActive = false
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
                if (supportFragmentManager.backStackEntryCount == 1 && isNavActive) {
                    updateNavMenuVisibility(View.VISIBLE)
                    navMenuAdapter?.selectedItem = -1
                    navMenuAdapter?.notifyDataSetChanged()
                }
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

    fun activeDashboardMenu() {
        binding.ivDashboard.setImageResource(R.drawable.ic_nav_dashboard_selected)
        binding.tvDashboard.setTextColor(ContextCompat.getColor(this, R.color.color_FF6605))
    }

    fun activeHomeMenu() {
        binding.ivMenu.setImageResource(R.drawable.ic_nav_menu_selected)
        binding.tvMenu.setTextColor(ContextCompat.getColor(this, R.color.color_FF6605))
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

    fun refreshMenu() {
        binding.ivMenu.setImageResource(R.drawable.ic_nav_menu)
        binding.ivDashboard.setImageResource(R.drawable.ic_nav_dashboard)
        binding.tvMenu.setTextColor(ContextCompat.getColor(this, R.color.color_333333))
        binding.tvVoice.setTextColor(ContextCompat.getColor(this, R.color.color_333333))
        binding.tvDashboard.setTextColor(ContextCompat.getColor(this, R.color.color_333333))
    }

    private fun initNavMenu() {
        binding.rvNavMenu.layoutManager = LinearLayoutManager(this)
        navMenuAdapter = HomeMenuAdapter(this, getNavMenuItem())
        binding.rvNavMenu.adapter = navMenuAdapter
        navMenuAdapter?.apply {
            onItemClickListener = object : HomeMenuAdapter.OnClickCallback,
                RadioButtonListAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    selectedItem = position
                    notifyDataSetChanged()
                    when (position) {
                        0 -> { // Dashboard clicked in side nav
                            updateNavMenuVisibility(View.GONE)
                            isNavActive = false
                            refreshMenu()
                            activeDashboardMenu()
                            clearBackStack()
                            openFragment(HomeFragment())
                        }

                        1 -> {
                            updateNavMenuVisibility(View.GONE)
                            homeNavClickAction(MyPersonaFragment())
                        }

                        2 -> {
                            updateNavMenuVisibility(View.GONE)
                            homeNavClickAction(SettingFragment())
                        }

                        3 -> {
                            updateNavMenuVisibility(View.GONE)
                            homeNavClickAction(ContactUsFragment())
                        }

                        4 -> {
                            val intent = Intent(this@HomeActivity, MainActivity::class.java)
                            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                    Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                                    or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            overridePendingTransition(
                                R.anim.enter_from_bottom,
                                android.R.anim.fade_out
                            )
                            finish()
                        }
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

    private fun homeNavClickAction(fragment: Fragment) {
        clearBackStack()
        addFragment(fragment)
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

    fun updateSideNavStatus() {
        isNavActive = false
    }

    fun getSideNavStatus(): Boolean {
        return isNavActive
    }

    fun openSetting() {
        addFragment(SettingFragment())
    }

    private fun observeResponse() {
        mockDataViewModel.providerList.observe(this) { providerList ->
            if (providerList.isEmpty()) {
                mockDataViewModel.fetchRemoteData()
            }
        }
    }
}