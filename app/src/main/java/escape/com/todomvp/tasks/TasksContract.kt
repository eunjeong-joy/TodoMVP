package escape.com.todomvp.tasks

import escape.com.todomvp.BasePresenter
import escape.com.todomvp.BaseView
import escape.com.todomvp.data.Task

interface TasksContract {

    interface View : BaseView<Presenter> {

        var isActive: Boolean

    }

    interface Presenter : BasePresenter {

        fun openTaskDetails(requestedTask: Task)

        fun completedTask(completedTask: Task)

        fun activateTask(activateTask: Task)
    }
}