package com.mE.Health

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mE.Health.databinding.ActivityMainBinding
import com.mE.Health.feature.SplashFragment
import com.mE.Health.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
//        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        mainViewModel =
//            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
//                MainViewModel::class.java
//            )
//        mainViewModel.productsLiveData.observe(this, Observer {
//            val text =  it.joinToString { x -> x.title + "\n\n" }
//            Log.i("=================","===========text: $text")
//        })

        openFragment()
    }

    private fun openFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, SplashFragment())
            .addToBackStack(null)
            .commit()
    }
}