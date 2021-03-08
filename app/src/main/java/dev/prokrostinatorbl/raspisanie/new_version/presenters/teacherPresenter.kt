package dev.prokrostinatorbl.raspisanie.new_version.presenters

import android.content.Context
import dev.prokrostinatorbl.raspisanie.new_version.models.MainModel
import dev.prokrostinatorbl.raspisanie.new_version.providers.TeacherProvider
import dev.prokrostinatorbl.raspisanie.new_version.views.TeacherView
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class teacherPresenter: MvpPresenter<TeacherView>() {
    fun loadTeach(context: Context, teach: ArrayList<String>){
        viewState.startLoading()
        TeacherProvider(presenter = this).loadTeach(context = context, teach = teach)
    }

    fun TeachLoaded(teachList: ArrayList<MainModel>){
        viewState.endLoading()
        viewState.setupList(teachList = teachList)
    }
}