package escape.com.todomvp.tasks

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import escape.com.todomvp.R
import escape.com.todomvp.addedittask.AddEditTaskActivity
import escape.com.todomvp.data.Task
import escape.com.todomvp.taskDetail.TaskDetailActivity
import escape.com.todomvp.util.showSnackBar
import kotlinx.android.synthetic.main.fragment_tasks.*

class TasksFragment : Fragment(), TasksContract.View {

    override lateinit var presenter: TasksContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    private lateinit var noTasksView: View
    private lateinit var noTaskIcon: ImageView
    private lateinit var noTaskMainView: TextView
    private lateinit var noTaskAddView: TextView
    private lateinit var tasksView: LinearLayout
    private lateinit var filteringLabelView: TextView

    companion object {

        fun newInstance() = TasksFragment()

    }

    internal var itemListener: TaskItemListener = object : TaskItemListener {
        override fun onTaskClick(clickedTask: Task) {
            presenter.openTaskDetails(clickedTask)
        }

        override fun onCompleteTaskClick(completedTask: Task) {
            presenter.completedTask(completedTask)
        }

        override fun onActivateTaskClick(activatedTask: Task) {
            presenter.activateTask(activatedTask)
        }
    }

    private val listAdapter = TasksAdapter(ArrayList(0), itemListener)

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.result(requestCode, resultCode)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tasks, container, false)

        with(root) {
            val listView = findViewById<ListView>(R.id.tasks_list).apply { adapter = listAdapter }

            findViewById<ScrollChildSwipeRefreshLayout>(R.id.refresh_layout).apply {
                setColorSchemeColors(
                    ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                    ContextCompat.getColor(requireContext(), R.color.colorAccent),
                    ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
                )
                scrollUpChild = listView
                setOnRefreshListener { presenter.loadTasks(false) }
            }
            filteringLabelView = findViewById(R.id.filteringLabel)
            tasksView = findViewById(R.id.tasksLL)

            noTasksView = findViewById(R.id.noTasks)
            noTaskIcon = findViewById(R.id.noTaskIcon)
            noTaskMainView = findViewById(R.id.noTaskMain)
            noTaskAddView = (findViewById<TextView>(R.id.noTasksAdd)).also {
                it.setOnClickListener{ showAddTask() }
            }
        }

        requireActivity().findViewById<FloatingActionButton>(R.id.fad_add_task).apply {
            setImageResource(R.drawable.ic_add)
            setOnClickListener{
                presenter.addNewTask()
            }
            setHasOptionsMenu(true)
        }

        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_clear -> presenter.clearCompletedTasks()
            R.id.menu_filter -> showFilteringPopUpMenu()
            R.id.menu_refresh -> presenter.loadTasks(true)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_tasks_fregment, menu)
    }

    override fun showFilteringPopUpMenu() {
        val activity = activity ?: return
        val context = context ?: return
        PopupMenu(context, activity.findViewById(R.id.menu_filter)).apply {
            menuInflater.inflate(R.menu.filter_tasks, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.active -> presenter.currentFiltering = TasksFilterType.ACTIVE_TASKS
                    R.id.completed -> presenter.currentFiltering = TasksFilterType.COMPLETED_TASKS
                    else -> presenter.currentFiltering = TasksFilterType.ALL_TASKS
                }
                presenter.loadTasks(false)
                true
            }
            show()
        }
    }

    override fun setLoadingIndicator(active: Boolean) {
        val root = view ?: return
        with(root.findViewById<ScrollChildSwipeRefreshLayout>(R.id.refresh_layout)) {
            post { isRefreshing = active }
        }
    }

    override fun showTasks(tasks: List<Task>) {
        listAdapter.tasks = tasks
        tasksView.visibility = View.VISIBLE
        noTaskAddView.visibility = View.GONE
    }

    private fun showNoTasksView(mainText: String, iconRes: Int, showAddView: Boolean) {
        tasksView.visibility = View.GONE
        noTasksView.visibility = View.VISIBLE

        noTaskMain.text = mainText
        noTaskIcon.setImageResource(iconRes)
        noTaskAddView.visibility = if(showAddView) View.VISIBLE else View.GONE
    }

    override fun showNoActiveTasks() {
        showNoTasksView(resources.getString(R.string.no_tasks_active), R.drawable.ic_check_circle_24dp, false)
    }

    override fun showNoTasks() {
        showNoTasksView(resources.getString(R.string.no_tasks_all), R.drawable.ic_assignment_turned_in_24dp, false)
    }

    override fun showNoCompletedTasks() {
        showNoTasksView(resources.getString(R.string.no_tasks_completed), R.drawable.ic_verified_user_24dp, false)
    }

    private fun showMessage(message: String) {
        view?.showSnackBar(message, Snackbar.LENGTH_LONG)
    }

    override fun showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message))
    }

    override fun showActiveFilterLabel() {
        filteringLabelView.text = resources.getString(R.string.label_active)
    }

    override fun showCompletedFilterLabel() {
        filteringLabelView.text = resources.getString(R.string.label_completed)
    }

    override fun showAllFilterLabel() {
        filteringLabelView.text = resources.getString(R.string.label_all)
    }

    override fun showAddTask() {
        val intent = Intent(context, AddEditTaskActivity::class.java)
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK)
    }

    override fun showTaskDetailUi(taskId: String) {
        val intent = Intent(context, TaskDetailActivity::class.java).apply {
            putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId)
        }
        startActivity(intent)
    }

    override fun showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete))
    }

    override fun showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active))
    }

    override fun showCompletedTasksClear() {
        showMessage(getString(R.string.completed_tasks_cleared))
    }

    override fun showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error))
    }

    private class TasksAdapter(tasks: List<Task>, private val itemListener: TaskItemListener) : BaseAdapter() {

        var tasks: List<Task>  = tasks
            set(tasks) {
                field = tasks
                notifyDataSetChanged()
            }

        override fun getItem(i: Int) = tasks[i]

        override fun getItemId(i: Int) = i.toLong()

        override fun getCount() = tasks.size

        override fun getView(i: Int, view: View?, viewgroup: ViewGroup): View {
            val task = getItem(i)
            val rowView = view ?: LayoutInflater.from(viewgroup.context)
                .inflate(R.layout.task_item, viewgroup, false)

            with(rowView.findViewById<TextView>(R.id.title)) {
                text = task.titleForList
            }

            with(rowView.findViewById<CheckBox>(R.id.complete)) {
                isChecked = task.isCompleted
                val rowViewBackground = if(task.isCompleted) R.drawable.list_completed_touch_feedback
                                        else R.drawable.touch_feedback
                rowView.setBackgroundResource(rowViewBackground)
                setOnClickListener {
                    if(!task.isCompleted) {
                        itemListener.onCompleteTaskClick(task)
                    } else {
                        itemListener.onActivateTaskClick(task)
                    }
                }
            }
            rowView.setOnClickListener {itemListener.onTaskClick(task)}
            return rowView
        }
    }

    interface TaskItemListener{

        fun onTaskClick(clickedTask: Task)

        fun onCompleteTaskClick(completeTask: Task)

        fun onActivateTaskClick(activatedTask: Task)
    }
}