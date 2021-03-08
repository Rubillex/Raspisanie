package dev.prokrostinatorbl.raspisanie.new_version.views

import dev.prokrostinatorbl.raspisanie.new_version.models.GroupModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface GroupView: MvpView {
    fun startLoading()
    fun endLoading()

    fun setupList(groupList: ArrayList<GroupModel>)
}