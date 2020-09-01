package escape.com.todomvp.addedittask

import escape.com.todomvp.data.Task
import escape.com.todomvp.data.source.TasksDataSource
import escape.com.todomvp.data.source.TasksRepository
import java.lang.RuntimeException

class AddEditTaskPresenter(
    private val taskId: String?,
    val tasksRepository: TasksRepository,
    val addTaskView: AddEditTaskContract.View,
    override var isDataMissing: Boolean
): AddEditTaskContract.Presenter, TasksDataSource.GetTaskCallback {

    init {
        addTaskView.presenter = this
    }


    override fun start() {
        if( taskId != null && isDataMissing) {
            populateTaske()
        }
    }

    override fun saveTask(title: String, description: String) {
        if( taskId == null ) {
            createTask(title, description)
        } else {
            updateTask(title, description)
        }
    }

    override fun populateTaske() {
        if( taskId == null ) {
            throw RuntimeException("populateTask() was called but task is new.")
        }
        tasksRepository.getTask(taskId, this)
    }

    override fun onTaskLoaded(task: Task) {
        if(addTaskView.isActive) {
            addTaskView.setTitle(task.title)
            addTaskView.setDescription(task.description)
        }
        isDataMissing = false
    }

    override fun onDataNotAvailable() {
        if(addTaskView.isActive) {
            addTaskView.showEmptyTaskError()
        }
    }

    private fun createTask(title: String, description: String) {
        val newTask = Task(title, description)
        if(newTask.isEmpty) {
            addTaskView.showEmptyTaskError()
        } else {
            tasksRepository.saveTask(newTask)
            addTaskView.showTasksList()
        }
    }

    private fun updateTask(title: String, description: String) {
        if(taskId == null) {
            throw RuntimeException("updateTask() was called but task is new.")
        }

        tasksRepository.saveTask(Task(title, description, taskId))
        addTaskView.showTasksList()
    }
}