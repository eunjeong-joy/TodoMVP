package escape.com.todomvp.statistics

import escape.com.todomvp.data.Task
import escape.com.todomvp.data.source.TasksDataSource
import escape.com.todomvp.data.source.TasksRepository
import escape.com.todomvp.util.EspressoIdlingResource

class StatisticsPresenter(
    val tasksRepository: TasksRepository,
    val statisticsView: StatisticsContract.View
): StatisticsContract.Presenter {

    init {
        statisticsView.presenter = this
    }

    override fun start() {
        loadStatistics()
    }

    private fun loadStatistics() {
        statisticsView.setProgressIndicator(true)

        EspressoIdlingResource.increment()

        tasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback{
            override fun onTasksLoaded(tasks: List<Task>) {
                val completedTasks = tasks.filter { it.isCompleted }.size
                val activeTasks = tasks.size - completedTasks

                if(!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }

                if(!statisticsView.isActive) {
                    return
                }

                statisticsView.setProgressIndicator(false)
                statisticsView.showStatistics(activeTasks, completedTasks)
            }

            override fun onDataNotAvailable() {
                if(!statisticsView.isActive) {
                    return
                }
                statisticsView.showLoadingStatisticsError()
            }
        })
    }
}