package com.mE.Health

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mE.Health.databinding.ActivityMainBinding
import com.mE.Health.feature.LoginFragment
import com.mE.Health.feature.SplashFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        (this.applicationContext as MyApplication).setCurrentActivity(this)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        openFragment()
    }

    private fun openFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, LoginFragment())
            .addToBackStack(null)
            .commit()
    }
}