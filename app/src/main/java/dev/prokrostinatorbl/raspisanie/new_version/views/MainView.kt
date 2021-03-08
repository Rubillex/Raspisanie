package dev.prokrostinatorbl.raspisanie.new_version.views

import dev.prokrostinatorbl.raspisanie.new_version.models.MainModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun showError(text: String)

    fun startLoading()
    fun endLoading()
    fun setupList(mainList: ArrayList<MainModel>)
}