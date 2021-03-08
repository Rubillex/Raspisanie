package dev.prokrostinatorbl.raspisanie.new_version.presenters

import android.content.Context
import dev.prokrostinatorbl.raspisanie.new_version.models.GroupModel
import dev.prokrostinatorbl.raspisanie.new_version.providers.GroupProvider
import dev.prokrostinatorbl.raspisanie.new_version.views.GroupView
import moxy.InjectViewState
import moxy.MvpPresenter
import java.security.acl.Group

@InjectViewState
class GroupPresenter: MvpPresenter<GroupView>() {
    fun loadGroup(context: Context, href: String, name: String){
        viewState.startLoading()
        GroupProvider(presenter = this).loadGroup(context = context, href = href, name = name)
    }

    fun GroupLoaded(groupList: ArrayList<GroupModel>){
        viewState.endLoading()
        viewState.setupList(groupList = groupList)
    }
}