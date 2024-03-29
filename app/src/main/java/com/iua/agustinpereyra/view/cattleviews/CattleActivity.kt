package com.iua.agustinpereyra.view.cattleviews

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.iua.agustinpereyra.R
import com.iua.agustinpereyra.controller.CARAVAN
import com.iua.agustinpereyra.controller.PreferenceUtils
import com.iua.agustinpereyra.view.helpviews.HelpActivity
import com.iua.agustinpereyra.view.settingsviews.SettingsActivity
import com.iua.agustinpereyra.view.userviews.UserAccountActivity
import com.iua.agustinpereyra.controller.VIEW_USER_REQUEST
import com.iua.agustinpereyra.databinding.ActivityCattleBinding
import com.iua.agustinpereyra.view.NotificationGenerator
import com.iua.agustinpereyra.view.base.BaseCattleListFragment
import com.iua.agustinpereyra.view.bovine.SingleBovineActivity
import com.iua.agustinpereyra.view.camera.CameraActivity
import com.iua.agustinpereyra.view.welcomeviews.WelcomeActivity

class CattleActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BaseCattleListFragment.CattleListFragmentListener {

    lateinit var toggle : ActionBarDrawerToggle

    private lateinit var activityCattleBinding: ActivityCattleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCattleBinding = ActivityCattleBinding.inflate(layoutInflater)

        setContentView(activityCattleBinding.root)

        // Set toolbar as support action bar
        val appMainToolbarBinding = activityCattleBinding.appMainToolbar
        setSupportActionBar(appMainToolbarBinding.mainToolbar)

        // Tie together drawer layout and action bar
        toggle = ActionBarDrawerToggle(this, activityCattleBinding.drawerLayout, appMainToolbarBinding.mainToolbar, 0, 0)
        activityCattleBinding.drawerLayout.addDrawerListener(toggle)


        // Set item click listener
        activityCattleBinding.navigationView.setNavigationItemSelectedListener(this)

        // Get and set Tab Swipe manager
        val tabsAdapter = CattleTabsAdapter(supportFragmentManager, lifecycle)
        val viewPager = activityCattleBinding.cattleViewpager
        val cattleTabLayout = activityCattleBinding.cattleTab
        viewPager.adapter = tabsAdapter
        TabLayoutMediator(cattleTabLayout, viewPager) {tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.cattle_all)
                else -> getString(R.string.cattle_monitoring)
            }
        }.attach()

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.base_main_menu, menu)
        return true
    }

    // Navigation logic inside navbar
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        activityCattleBinding.drawerLayout.closeDrawer(GravityCompat.START)
        when(item.itemId){
            R.id.nav_menu_account -> {
                val userAccountIntent = Intent(this, UserAccountActivity::class.java)
                startActivityForResult(userAccountIntent, VIEW_USER_REQUEST)
            }

            R.id.nav_menu_preferences -> {
                val settingsActivityIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsActivityIntent)
            }

            R.id.nav_menu_help -> {
                val helpActivityIntent = Intent(this, HelpActivity::class.java)
                startActivity(helpActivityIntent)
            }

            R.id.nav_menu_signout -> {
                val welcomeActivityIntent = Intent(this, WelcomeActivity::class.java)
                // Sign out user from preferences
                val preferenceUtils = PreferenceUtils(this)
                preferenceUtils.signOut()
                // Clean backstack and go to Welcome page
                finishAffinity()
                startActivity(welcomeActivityIntent)
                finish()
            }
        }
        return true
    }

    // Set the navbar if menu is clicked or handle as appropriate
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.home -> {
                activityCattleBinding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.main_top_app_bar_camera -> {
                val cameraIntent = Intent(this, CameraActivity::class.java)
                startActivity(cameraIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Close navbar if open on back pressed
    override fun onBackPressed() {
        if (activityCattleBinding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            activityCattleBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    // ActionBarModifier functions
    override fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun setActionBarHomeButtonAsUp() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun notifyNoInternet() {
        NotificationGenerator.noInternetConnectionAlert(this)
    }

    // Cattle List needed functions
    override fun navigateToSpecificBovine(caravan: String) {
        val bovineIntent = Intent(this, SingleBovineActivity::class.java)
        bovineIntent.putExtra(CARAVAN, caravan)
        startActivity(bovineIntent)
    }

}