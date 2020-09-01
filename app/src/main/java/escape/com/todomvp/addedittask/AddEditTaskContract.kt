package escape.com.todomvp.addedittask

import escape.com.todomvp.BasePresenter
import escape.com.todomvp.BaseView

interface AddEditTaskContract {

    interface View : BaseView<Presenter> {
        var isActive: Boolean

        fun showEmptyTaskError()

        fun showTasksList()

        fun setTitle(title: String)

        fun setDescription(description: String)
    }

    interface Presenter : BasePresenter {
        var isDataMissing: Boolean

        fun saveTask(title: String, description: String)

        fun populateTaske()
    }
}