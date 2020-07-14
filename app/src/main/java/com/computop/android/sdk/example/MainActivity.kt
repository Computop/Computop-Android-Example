package com.computop.android.sdk.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.computop.android.sdk.example.databinding.ActivityMainBinding
import com.google.android.material.appbar.CollapsingToolbarLayout

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        collapsingToolbarLayout = binding.mainCollapsing
    }

    override fun onStart() {
        super.onStart()

        showLandingPage()
    }

    private fun showLandingPage() {
        binding.toolbar.title = getString(R.string.app_name)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, LandingPage(), LandingPage::class.java.canonicalName)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}