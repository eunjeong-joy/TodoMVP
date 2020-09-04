package escape.com.todomvp.taskDetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import escape.com.todomvp.Injection
import escape.com.todomvp.R
import escape.com.todomvp.util.replaceFragmentInActivity
import escape.com.todomvp.util.setupActionBar

class TaskDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_task_detail)

        setupActionBar(R.id.toolbar) {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val taskId = intent.getStringExtra(EXTRA_TASK_ID)

        val taskDetailFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as TaskDetailFragment? ?:
                TaskDetailFragment.newInstance(taskId).also {
                    replaceFragmentInActivity(it, R.id.contentFrame)
                }

        TaskDetailPresenter(taskId, Injection.provideTasksRepository(applicationContext), taskDetailFragment)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_TASK_ID = "TASK_ID"
    }
}