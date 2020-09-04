package escape.com.todomvp.tasks

import android.app.Activity
import escape.com.todomvp.addedittask.AddEditTaskActivity
import escape.com.todomvp.data.Task
import escape.com.todomvp.data.source.TasksDataSource
import escape.com.todomvp.data.source.TasksRepository
import escape.com.todomvp.util.EspressoIdlingResource

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
        if(AddEditTaskActivity.REQUEST_ADD_TASK == requestCode
            && Activity.RESULT_OK == resultCode) {
            tasksView.showSuccessfullySavedMessage()
        }
    }

    override fun loadTasks(forceUpdate: Boolean) {
        loadTasks(forceUpdate || firstLoad, true)
    }

    private fun loadTasks(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if(showLoadingUI) {
            tasksView.setLoadingIndicator(true)
        }
        if(forceUpdate) {
            tasksRepository.refreshTasks()
        }

        EspressoIdlingResource.increment()

        tasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                val tasksToShow = ArrayList<Task>()
                if(EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }

                for(task in tasks) {
                    when(currentFiltering) {
                        TasksFilterType.ALL_TASKS -> tasksToShow.add(task)
                        TasksFilterType.ACTIVE_TASKS -> if(task.isActive) {
                            tasksToShow.add(task)
                        }
                        TasksFilterType.COMPLETED_TASKS -> if(task.isCompleted) {
                            tasksToShow.add(task)
                        }
                    }
                }

                if(!tasksView.isActive) {
                    return
                }
                if(showLoadingUI) {
                    tasksView.setLoadingIndicator(false)
                }

                processTasks(tasksToShow)
            }

            override fun onDataNotAvailable() {
                if(!tasksView.isActive) {
                    return
                }
                tasksView.showLoadingTasksError()
            }
        })
    }

    private fun processTasks(tasks: List<Task>) {
        if(tasks.isEmpty()) {
            processEmptyTasks()
        } else {
            tasksView.showTasks(tasks)
            showFilterLabel()
        }
    }

    private fun processEmptyTasks() {
        when(currentFiltering) {
            TasksFilterType.ACTIVE_TASKS -> tasksView.showActiveFilterLabel()
            TasksFilterType.COMPLETED_TASKS -> tasksView.showCompletedFilterLabel()
            else -> tasksView.showAllFilterLabel()
        }
    }

    private fun showFilterLabel() {
        when(currentFiltering) {
            TasksFilterType.ACTIVE_TASKS -> tasksView.showActiveFilterLabel()
            TasksFilterType.COMPLETED_TASKS -> tasksView.showCompletedFilterLabel()
            else -> tasksView.showAllFilterLabel()
        }
    }

    override fun addNewTask() {
        tasksView.showAddTask()
    }

    override fun openTaskDetails(requestedTask: Task) {
        tasksView.showTaskDetailUi(requestedTask.id)
    }

    override fun completedTask(completedTask: Task) {
        tasksRepository.completeTask(completedTask)
        tasksView.showTaskMarkedComplete()
        loadTasks(false, false)
    }

    override fun activateTask(activateTask: Task) {
        tasksRepository.activateTask(activateTask)
        tasksView.showTaskMarkedActive()
        loadTasks(false, false)
    }

    override fun clearCompletedTasks() {
        tasksRepository.clearCompletedTasks()
        tasksView.showCompletedTasksClear()
        loadTasks(false, false)
    }
}