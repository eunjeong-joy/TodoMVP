package escape.com.todomvp.taskDetail

import escape.com.todomvp.data.Task
import escape.com.todomvp.data.source.TasksDataSource
import escape.com.todomvp.data.source.TasksRepository

class TaskDetailPresenter(
    private val taskId: String,
    private val tasksRepository: TasksRepository,
    private val taskDetailView: TaskDetailContract.View
) : TaskDetailContract.Presenter {

    init {
        taskDetailView.presenter = this
    }

    override fun start() {
        openTask()
    }

    private fun openTask() {
        if(taskId.isEmpty()) {
            taskDetailView.showMissingTask()
            return
        }

        taskDetailView.setLoadingIndicator(true)
        tasksRepository.getTask(taskId, object: TasksDataSource.GetTaskCallback {
            override fun onTaskLoaded(task: Task) {
                with(taskDetailView) {
                    if(!isActive) {
                        return
                    }
                    setLoadingIndicator(false)
                }
                showTask(task)
            }

            override fun onDataNotAvailable() {
                with(taskDetailView) {
                    if(!isActive) {
                        return
                    }
                    showMissingTask()
                }
            }
        })
    }

    private fun showTask(task: Task) {
        with(taskDetailView) {
            if(taskId.isEmpty()) {
                hideTitle()
                hideDescription()
            } else {
                showTitle(task.title)
                showDescription(task.description)
            }
            showCompletionStatus(task.isCompleted)
        }
    }

    override fun editTask() {
        if(taskId.isEmpty()) {
            taskDetailView.showMissingTask()
            return
        }
        taskDetailView.showEditTask(taskId)
    }

    override fun deleteTask() {
        if(taskId.isEmpty()) {
            taskDetailView.showMissingTask()
            return
        }
        tasksRepository.deleteTask(taskId)
        taskDetailView.showTaskDeleted()
    }

    override fun completeTask() {
        if(taskId.isEmpty()) {
            taskDetailView.showMissingTask()
            return
        }
        tasksRepository.completeTask(taskId)
        taskDetailView.showTaskMarkedComplete()
    }

    override fun activateTask() {
        if(taskId.isEmpty()) {
            taskDetailView.showMissingTask()
            return
        }
        tasksRepository.activateTask(taskId)
        taskDetailView.showTaskMarkedActive()
    }
}