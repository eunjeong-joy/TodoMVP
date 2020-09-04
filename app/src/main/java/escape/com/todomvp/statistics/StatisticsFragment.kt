package escape.com.todomvp.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import escape.com.todomvp.R

class StatisticsFragment: Fragment(), StatisticsContract.View {

    private lateinit var statisticsTV: TextView

    override lateinit var presenter: StatisticsContract.Presenter

    override val isActive: Boolean
        get() = isAdded

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_statistics, container, false)
        statisticsTV = root.findViewById(R.id.statistics)
        return root
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun setProgressIndicator(active: Boolean) {
        if(active) {
            statisticsTV.text = getString(R.string.loading)
        } else {
            statisticsTV.text = ""
        }
    }

    override fun showStatistics(numberOfIncompleteTasks: Int, numberOfCompleteTasks: Int) {
        if(numberOfCompleteTasks == 0 && numberOfIncompleteTasks == 0) {
            statisticsTV.text = getString(R.string.statistics_no_tasks)
        } else {
            val displayString = "${getString(R.string.statistics_active_tasks)}" +
                    "${numberOfIncompleteTasks}\n" +
                    "${getString(R.string.statistics_completed_tasks)}" +
                    "${numberOfCompleteTasks}"
            statisticsTV.text = displayString
        }
    }

    override fun showLoadingStatisticsError() {
        statisticsTV.text = getString(R.string.statistics_error)
    }

    companion object {
        fun newInstance(): StatisticsFragment {
            return StatisticsFragment()
        }
    }
}