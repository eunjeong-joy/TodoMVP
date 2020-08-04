package escape.com.todomvp.tasks

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import escape.com.todomvp.R
import escape.com.todomvp.statistics.StatisticsActivity
import escape.com.todomvp.util.setupActionBar

class TasksActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

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
}