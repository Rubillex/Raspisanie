package dev.prokrostinatorbl.raspisanie.new_version.presenters

import android.content.Context
import android.util.Log
import dev.prokrostinatorbl.raspisanie.new_version.models.MainModel
import dev.prokrostinatorbl.raspisanie.new_version.providers.MainProvider
import dev.prokrostinatorbl.raspisanie.new_version.views.MainView
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {
    fun loadInstitut(context: Context){
        viewState.startLoading()
        MainProvider(presenter = this).loadInstitut(context = context)
    }

    fun InstitutLoaded(mainList: ArrayList<MainModel>){
        Log.e("MyLog", "presenter")
        viewState.endLoading()
        viewState.setupList(mainList =  mainList)
    }
}