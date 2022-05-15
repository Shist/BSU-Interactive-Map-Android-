package com.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.domain.BuildingItem
import com.example.domain.StructuralObjectItem
import com.example.ui.databinding.ActivityMainBinding
import com.example.ui.fragments.HistBuildingDetailsFragment
import com.example.ui.fragments.MapFragment
import com.example.ui.fragments.ModernDepartDetailsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private fun inflateFragment(f: Fragment, holder: Int, needAddBackStackOrNot: Boolean) {
        if (needAddBackStackOrNot) {
            supportFragmentManager.beginTransaction()
                .replace(holder, f)
                .addToBackStack("goBack")
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(holder, f)
                .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null)
        {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MapFragment())
                .commit()
        }

    }

    fun onHistoricalBuildingClick(building: BuildingItem) {
        inflateFragment(HistBuildingDetailsFragment.newInstance(building),
            R.id.fragment_container,true)
    }

    fun onModernBuildingClick(department: StructuralObjectItem) {
        inflateFragment(ModernDepartDetailsFragment.newInstance(department),
            R.id.fragment_container,true)
    }

}