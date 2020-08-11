package escape.com.todomvp.tasks

import escape.com.todomvp.data.Task
import escape.com.todomvp.data.source.TasksRepository

class TasksPresenter(val tasksRepository: TasksRepository, val tasksView: TasksContract.View) : TasksContract.Presenter {

    override var currentFiltering = TasksFilterType.ALL_TASKS

    private var firstLoad = false

    init {
        tasksView.presenter = this
    }

    override fun start() {
        loadTasks(false)
    }

    override fun result(requestCode: Int, resultCode: Int) {
        TODO("Not yet implemented")
    }

    override fun loadTasks(forceUpdate: Boolean) {
        TODO("Not yet implemented")
    }

    override fun addNewTask() {
        TODO("Not yet implemented")
    }

    override fun openTaskDetails(requestedTask: Task) {
        TODO("Not yet implemented")
    }

    override fun completedTask(completedTask: Task) {
        TODO("Not yet implemented")
    }

    override fun activateTask(activateTask: Task) {
        TODO("Not yet implemented")
    }

    override fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }
}