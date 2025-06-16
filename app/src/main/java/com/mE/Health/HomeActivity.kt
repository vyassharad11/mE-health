package com.mE.Health

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.databinding.ActivityHomeBinding
import com.mE.Health.feature.HomeFragment
import com.mE.Health.feature.adapter.HomeMenuAdapter
import com.mE.Health.feature.adapter.RadioButtonListAdapter
import com.mE.Health.models.AdviceDTO
import com.mE.Health.models.NavMenuDTO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        openFragment()
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

    private fun openFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, HomeFragment())
            .commit()
    }

    override fun onClick(v: View?) {
        refreshMenu()
        when (v?.id) {
            R.id.llMenu -> {
                updateNavMenuVisibility(View.VISIBLE)
                binding.ivMenu.setImageResource(R.drawable.ic_nav_menu_selected)
                binding.tvMenu.setTextColor(ContextCompat.getColor(this, R.color.color_FF6605))
            }

            R.id.llVoice, R.id.rllVoice -> {
                updateNavMenuVisibility(View.GONE)
                openDialog()
            }

            R.id.llDashboard -> {
                updateNavMenuVisibility(View.GONE)
                activeDashboardMenu()
            }
        }
    }


    override fun onBackPressed() {
        try {
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
        if (supportFragmentManager.backStackEntryCount == 0) {
            binding.rlBottomMenu.visibility = View.VISIBLE
        } else {
            binding.rlBottomMenu.visibility = View.GONE
        }
    }

    private fun activeDashboardMenu() {
        binding.ivDashboard.setImageResource(R.drawable.ic_nav_dashboard_selected)
        binding.tvDashboard.setTextColor(ContextCompat.getColor(this, R.color.color_FF6605))
    }

    fun updateNavMenuVisibility(visibility: Int){
        binding.rvNavMenu   .visibility = visibility
        binding.llMenuView.visibility = visibility
        binding.fragmentContainer.setPadding(0,0,if (visibility==View.VISIBLE)-100 else 0,0)
    }

    private fun refreshMenu() {
        binding.ivMenu.setImageResource(R.drawable.ic_nav_menu)
        binding.ivDashboard.setImageResource(R.drawable.ic_nav_dashboard)
        binding.tvMenu.setTextColor(ContextCompat.getColor(this, R.color.color_333333))
        binding.tvVoice.setTextColor(ContextCompat.getColor(this, R.color.color_333333))
        binding.tvDashboard.setTextColor(ContextCompat.getColor(this, R.color.color_333333))
    }

    private fun initNavMenu(){
        binding.rvNavMenu.layoutManager = LinearLayoutManager(this)
        val adapter = HomeMenuAdapter(this,getNavMenuItem())
        binding.rvNavMenu.adapter = adapter
        adapter.apply {
            onItemClickListener =  object : HomeMenuAdapter.OnClickCallback,
                RadioButtonListAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    selectedItem = position
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun getNavMenuItem():ArrayList<NavMenuDTO> {
        return ArrayList<NavMenuDTO>().apply {
            add(NavMenuDTO(getString(R.string.dashboard), R.drawable.nav_menu_dashboard))
            add(NavMenuDTO(getString(R.string.my_persona), R.drawable.nav_menu_persona))
            add(NavMenuDTO(getString(R.string.settings), R.drawable.nav_menu_settings))
            add(NavMenuDTO(getString(R.string.contact_us), R.drawable.nav_menu_settings))
            add(NavMenuDTO(getString(R.string.logout), R.drawable.nav_menu_settings))
        }
    }

    private fun openDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_ok)
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
//        dialog.window!!.attributes.windowAnimations = R.style.animation
        val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = getString(R.string.dialog_description)
        val tvOk = dialog.findViewById<View>(R.id.tvOk)
        tvOk.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
    }
}