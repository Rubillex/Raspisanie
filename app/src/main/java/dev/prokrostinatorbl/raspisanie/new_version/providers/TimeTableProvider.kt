package dev.prokrostinatorbl.raspisanie.new_version.providers

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.util.Log
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.LoadStudents
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.LoadTeacher
import dev.prokrostinatorbl.raspisanie.new_version.models.TableSecond
import dev.prokrostinatorbl.raspisanie.new_version.models.TimeTableModel
import dev.prokrostinatorbl.raspisanie.new_version.presenters.TimeTablePresenter
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimeTableProvider(var presenter: TimeTablePresenter) {
    fun loadTable(context: Context, href: String, name: String, group: String, page: Int){
        doAsync(context = context, presenter = presenter, href = href, name = name, group = group, page = page)
        Log.e("GOTOGOTO", "table")
    }

    fun loadTeach(context: Context, href: String, name: String, group: String, page: Int){
        Async(context = context, presenter = presenter, href = href, name = name, group = group, page = page)
        Log.e("GOTOGOTO", "teach")
    }

    class Async(val context: Context,
                val presenter: TimeTablePresenter,
                val href: String,
                val name: String,
                val group: String,
                val page: Int): AsyncTask<Void, Void, Void>(){

        init {
            execute()
        }

        private val result: ArrayList<TimeTableModel> = ArrayList()

        override fun doInBackground(vararg params: Void?): Void? {
            result.addAll(LoadTeacher(context = context, href = href, name = name, page = page, group = group))
            return null
        }

        override fun onPostExecute(res: Void?) {
            super.onPostExecute(res)
            presenter.TableLoaded(tableList = result)

        }


    }

    class doAsync(val context: Context,
                  val presenter: TimeTablePresenter,
                  val href: String,
                  val name: String,
                  val group: String,
                  val page: Int): AsyncTask<Void, Void, Void>() {
        init{
            execute()
        }

        private val result: ArrayList<TimeTableModel> = ArrayList()

        override fun doInBackground(vararg params: Void?): Void? {
            result.addAll(LoadStudents(context = context, href = href, name = name, page = page, group = group))
            return null
        }

        override fun onPostExecute(res: Void?) {
            super.onPostExecute(res)
            presenter.TableLoaded(tableList = result)

        }

    }
}