package dev.prokrostinatorbl.raspisanie.new_version.providers

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import dev.prokrostinatorbl.raspisanie.new_version.models.GroupModel
import dev.prokrostinatorbl.raspisanie.new_version.presenters.GroupPresenter
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File
import java.io.IOException
import kotlin.collections.ArrayList

class GroupProvider(var presenter: GroupPresenter) {
    fun loadGroup(context: Context, href: String, name: String){
        doAsync(context = context, presenter = presenter, href = href, name = name)
    }

    class doAsync(val context: Context, val presenter: GroupPresenter, val href: String, val name: String): AsyncTask<Void, Void, Void>(){

        private val result: ArrayList<GroupModel> = ArrayList()

        private var arr_name: ArrayList<String> = ArrayList()
        private var arr_href: ArrayList<String> = ArrayList()

        private var arr_1_name: String = ""
        private var arr_2_name: String = ""
        private var arr_3_name: String = ""

        private var arr_1_href: String = ""
        private var arr_2_href: String = ""
        private var arr_3_href: String = ""

        private lateinit var doc: Document
        private lateinit var elements: Elements

        fun hasConnection(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNW = cm.activeNetworkInfo
            return if (activeNW != null && activeNW.isConnected) {
                true
            } else false
        }


        init {
            execute()
        }

        override fun onPostExecute(res: Void?) {
            super.onPostExecute(res)
            presenter.GroupLoaded(groupList = result)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            Load(context = context, href = href, name = name)
            return null
        }

        fun Load(context: Context, href: String, name: String){

            Log.e("GroupBind", href)

            val link_asu = "https://www.asu.ru/timetable/students/$href"

            if (hasConnection(context = context)){
                doc = Jsoup.connect(link_asu).get()
            } else {
                try {
                    val `in` = File(context.filesDir.toString() + "Android/data/dev.prokrostinatorbl.raspisanie/files/$name.html")
                    doc = Jsoup.parse(`in`, null)
                } catch (e: IOException){
                    Toast.makeText(context, "Кэш пуст", Toast.LENGTH_SHORT).show()
                }

            }

            elements = doc.getElementsByClass("link_ptr_left margin_bottom")

            var name: String
            var href_full: Elements
            var href: String

            if (elements.size != 0){
                elements.forEach {
                    name = it.text()
                    href_full = it.select("div.link_ptr_left.margin_bottom > a")
                    href = href_full.attr("href")

                    arr_name.add(name)
                    arr_href.add(href)
                }

                for (i in 0 until arr_name.size step 3){
                    if (i < arr_name.size &&
                            arr_1_name != arr_name[i]){
                        arr_1_name = arr_name[i]
                        arr_1_href = arr_href[i]
                    }
                    if (i+1 < arr_name.size &&
                            arr_2_name != arr_name[i+1]){
                        arr_2_name = (arr_name[i+1])
                        arr_2_href = (arr_href[i+1])
                    } else {
                        arr_2_name = ("")
                        arr_2_href = ("")
                    }
                    if (i+2 < arr_name.size &&
                            arr_3_name != arr_name[i+2]){
                        arr_3_name = (arr_name[i+2])
                        arr_3_href = (arr_href[i+2])
                    } else {
                        arr_3_name = ("")
                        arr_3_href = ("")
                    }

                    val item = GroupModel(
                            name_1 = arr_1_name,
                            name_2 = arr_2_name,
                            name_3 = arr_3_name,
                            href_1 = arr_1_href,
                            href_2 = arr_2_href,
                            href_3 = arr_3_href,
                            )
                    result.add(item)
                }

            }

        }

    }
}