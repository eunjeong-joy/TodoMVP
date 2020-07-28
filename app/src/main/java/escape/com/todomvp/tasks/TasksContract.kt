package escape.com.todomvp.tasks

import escape.com.todomvp.BasePresenter
import escape.com.todomvp.BaseView

interface TasksContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {

    }
}