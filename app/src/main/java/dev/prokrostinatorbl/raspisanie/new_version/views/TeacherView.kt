package dev.prokrostinatorbl.raspisanie.new_version.views

import dev.prokrostinatorbl.raspisanie.new_version.models.MainModel
import dev.prokrostinatorbl.raspisanie.new_version.presenters.MainPresenter
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface TeacherView: MvpView{
    fun startLoading()
    fun endLoading()
    fun setupList(teachList: ArrayList<MainModel>)
}