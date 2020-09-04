package escape.com.todomvp.statistics

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import escape.com.todomvp.Injection
import escape.com.todomvp.R
import escape.com.todomvp.util.replaceFragmentInActivity
import escape.com.todomvp.util.setupActionBar

class StatisticsActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        setupActionBar(R.id.toolbar) {
            setTitle(getString(R.string.statistics_title))
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
        }

        drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout)).apply {
            setStatusBarBackground(R.color.colorPrimaryDark)
        }

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        setupDrawerContent(navigationView)

        val statisticsFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as StatisticsFragment?
            ?: StatisticsFragment.newInstance().also {
                replaceFragmentInActivity(it, R.id.contentFrame)
            }

        StatisticsPresenter(Injection.provideTasksRepository(applicationContext), statisticsFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.list_navigation_menu_item) {
                NavUtils.navigateUpFromSameTask(this@StatisticsActivity)
            }
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
    }
}