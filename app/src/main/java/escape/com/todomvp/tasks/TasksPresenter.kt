package escape.com.todomvp.tasks

import escape.com.todomvp.data.source.TasksRepository

class TasksPresenter(val tasksRepository: TasksRepository, val tasksView: TasksContract.View) : TasksContract.Presenter {
    override var currentFiltering = TasksFilterType.ALL_TASKS
}