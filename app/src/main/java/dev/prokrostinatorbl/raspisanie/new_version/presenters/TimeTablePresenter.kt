package dev.prokrostinatorbl.raspisanie.new_version.presenters

import android.content.Context
import dev.prokrostinatorbl.raspisanie.new_version.models.TimeTableModel
import dev.prokrostinatorbl.raspisanie.new_version.providers.TimeTableProvider
import dev.prokrostinatorbl.raspisanie.new_version.views.TimeTableView
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class TimeTablePresenter: MvpPresenter<TimeTableView>() {
    fun loadTable(context: Context, href: String, name:String, group: String, page: Int){
        viewState.startLoading()
        TimeTableProvider(presenter = this).loadTable(context = context, href = href, name = name, group = group, page = page)
    }

    fun loadTeach(context: Context, href: String, name:String, group: String, page: Int){
        viewState.startLoading()
        TimeTableProvider(presenter = this).loadTeach(context = context, href = href, name = name, group = group, page = page)
    }

    fun TableLoaded(tableList: ArrayList<TimeTableModel>){
        viewState.endLoading()
        viewState.setupList(tableList = tableList)
    }
}