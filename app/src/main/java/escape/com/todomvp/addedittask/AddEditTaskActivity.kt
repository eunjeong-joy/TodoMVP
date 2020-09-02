package escape.com.todomvp.addedittask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import escape.com.todomvp.Injection
import escape.com.todomvp.R
import escape.com.todomvp.util.replaceFragmentInActivity
import escape.com.todomvp.util.setupActionBar

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var addEditTaskPresenter: AddEditTaskPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        val taskId = intent.getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle( if (taskId == null) R.string.add_task else R.string.edit_task)
        }

        val addEditTaskFragment =
            supportFragmentManager.findFragmentById(R.id.contentFrame) as AddEditTaskFragment?
                ?: AddEditTaskFragment.newInstance(taskId).also {
                    replaceFragmentInActivity(it, R.id.contentFrame)
                }

        val shouldLoadDataFromRepo =
            savedInstanceState?.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY) ?: true

        addEditTaskPresenter = AddEditTaskPresenter(taskId,
                                Injection.provideTasksRepository(applicationContext), addEditTaskFragment,
                                shouldLoadDataFromRepo)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, addEditTaskPresenter.isDataMissing)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY"
        const val REQUEST_ADD_TASK = 1
    }
}