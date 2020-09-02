package escape.com.todomvp.taskDetail

import escape.com.todomvp.BasePresenter
import escape.com.todomvp.BaseView

interface TaskDetailContract {

    interface View : BaseView<Presenter> {
        val isActive: Boolean

        fun setLoadingIndication(active: Boolean)

        fun showMissingTask()

        fun hideTitle()

        fun showTitle(title: String)

        fun hideDescription()

        fun showDescription(description: String)

        fun showCompletionStatus(complete: Boolean)

        fun showEditTask(taskId: String)

        fun showTaskDeleted()

        fun showTaskMarkedComplete()

        fun showTaskMarkedActive()
    }

    interface Presenter : BasePresenter {
        fun editTask()

        fun deleteTask()

        fun completeTask()

        fun activateTask()
    }
}