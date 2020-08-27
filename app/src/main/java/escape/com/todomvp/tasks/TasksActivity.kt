package escape.com.todomvp.tasks

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.test.espresso.IdlingResource
import com.google.android.material.navigation.NavigationView
import escape.com.todomvp.Injection
import escape.com.todomvp.R
import escape.com.todomvp.statistics.StatisticsActivity
import escape.com.todomvp.util.EspressoIdlingResource
import escape.com.todomvp.util.replaceFragmentInActivity
import escape.com.todomvp.util.setupActionBar

class TasksActivity : AppCompatActivity() {

    private var CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY"

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var tasksPresenter: TasksPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        //Set up the toolbar
        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_menu) // 뒤로가기 버튼 설
            setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        }

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout).apply {
            setStatusBarBackground(R.color.colorPrimaryDark)
        }
        setDrawerContent(findViewById(R.id.nav_view))

        val taskFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as TasksFragment? ?: TasksFragment.newInstance().also {
                    replaceFragmentInActivity(it, R.id.contentFrame)
        }

        tasksPresenter = TasksPresenter(Injection.provideTasksRepository(applicationContext),
            taskFragment).apply {
            if(savedInstanceState != null) {
                currentFiltering = savedInstanceState.getSerializable(CURRENT_FILTERING_KEY) as TasksFilterType
            }
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putSerializable(CURRENT_FILTERING_KEY, tasksPresenter.currentFiltering)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            if(menuItem.itemId == R.id.statistics_navigation_menu_item) {
                val intent = Intent(this@TasksActivity, StatisticsActivity::class.java)
                startActivity(intent)
            }
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
    }

    val countingIdlingResource: IdlingResource
        @VisibleForTesting
        get() = EspressoIdlingResource.countingIdlingResource
}