package escape.com.todomvp.data.source.local

import androidx.room.*
import escape.com.todomvp.data.Task

@Dao interface TasksDao {
    @Query("SELECT * FROM Tasks")
    fun getTasks() : List<Task>

    @Query("SELECT * FROM Tasks WHERE entryId = :taskId")
    fun getTaskById(taskId: String) : Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task): Int

    @Query("UPDATE tasks SET completed = :completed WHERE entryId = :taskId")
    fun updateCompleted(taskId: String, completed: Boolean)

    @Query("DELETE FROM tasks WHERE entryId = :taskId")
    fun deleteTaskById(taskId: String): Int

    @Query("DELETE FROM tasks")
    fun deleteTasks()

    @Query("DELETE FROM tasks WHERE completed = 1")
    fun deleteCompletedTasks(): Int
}