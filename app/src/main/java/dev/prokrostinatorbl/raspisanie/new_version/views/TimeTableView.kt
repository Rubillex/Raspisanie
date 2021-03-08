package dev.prokrostinatorbl.raspisanie.new_version.views

import dev.prokrostinatorbl.raspisanie.new_version.models.TimeTableModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface TimeTableView: MvpView {
    fun startLoading()
    fun endLoading()

    fun setupList(tableList: ArrayList<TimeTableModel>)
}