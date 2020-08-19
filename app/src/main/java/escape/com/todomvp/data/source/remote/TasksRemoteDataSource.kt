package escape.com.todomvp.data.source.remote

import escape.com.todomvp.data.Task
import escape.com.todomvp.data.source.TasksDataSource

object TasksRemoteDataSource : TasksDataSource {

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        TODO("Not yet implemented")
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        TODO("Not yet implemented")
    }

    override fun saveTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun completeTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun activateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override fun refreshTasks() {
        TODO("Not yet implemented")
    }

    override fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }
}