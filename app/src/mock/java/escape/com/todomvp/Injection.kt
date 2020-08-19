package escape.com.todomvp

import android.content.Context
import escape.com.todomvp.data.source.TasksRepository
import escape.com.todomvp.data.source.local.TasksLocalDataSource
import escape.com.todomvp.data.source.local.ToDoDatabase
import escape.com.todomvp.data.source.remote.TasksRemoteDataSource
import escape.com.todomvp.util.AppExecutors

object Injection {
    fun provideTasksRepository(context: Context): TasksRepository {
        val database = ToDoDatabase.getInstance(context)
        return TasksRepository.getInstance(TasksRemoteDataSource,
            TasksLocalDataSource.getInstance(AppExecutors(), database.taskDao()))
    }
}