package escape.com.todomvp.statistics

import escape.com.todomvp.BasePresenter
import escape.com.todomvp.BaseView

interface StatisticsContract {

    interface View: BaseView<Presenter> {
        val isActive: Boolean

        fun setProgressIndicator(active: Boolean)

        fun showStatistics(numberOfIncompleteTasks: Int, numberOfCompleteTasks: Int)

        fun showLoadingStatisticsError()
    }

    interface Presenter: BasePresenter
}