package dev.prokrostinatorbl.raspisanie.new_version.providers

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import dev.prokrostinatorbl.raspisanie.new_version.models.MainModel
import dev.prokrostinatorbl.raspisanie.new_version.presenters.MainPresenter
import org.apache.commons.io.FileUtils
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.File
import java.io.IOException

class MainProvider(var presenter: MainPresenter) {


    class doAsync(val context: Context, val presenter: MainPresenter) : AsyncTask<Void, Void, Void>() {

        private lateinit var doc: Document
        private lateinit var elements: Elements

        private val result: ArrayList<MainModel> = ArrayList()

        init {
            execute()
        }

        override fun onPostExecute(res: Void?) {
            super.onPostExecute(res)
            presenter.InstitutLoaded(mainList = result)
        }

        override fun doInBackground(vararg params: Void?): Void? {

            Log.e("MyLog", "doIn")
            Load(context)
            return null
        }

        fun hasConnection(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNW = cm.activeNetworkInfo
            return if (activeNW != null && activeNW.isConnected) {
                true
            } else false
        }


        fun Load(context: Context) {



            val link_asu = "https://www.asu.ru/timetable/students/"
            val file_name = "main"



            if (hasConnection(context = context)){
                doc = Jsoup.connect(link_asu).get()
            } else {
                try {
                    val `in` = File(context.filesDir.toString() + "Android/data/dev.prokrostinatorbl.raspisanie/files/" + file_name + ".html")
                    doc = Jsoup.parse(`in`, null)
                } catch (e: IOException) {
                    Toast.makeText(context, "Кэш пуст", Toast.LENGTH_SHORT).show()
                }
            }

                elements = doc.getElementsByClass("link_ptr_left margin_bottom")

                var link: Elements

                if (elements.size != 0){
                    elements.forEach {
                        val temp = it.text()

                        val name = temp.substring(temp.indexOf('(') + 1, temp.indexOf(')'))

                        link = it.select("div.link_ptr_left.margin_bottom > a")
                        val url = link.attr("href")

                        val item = MainModel(
                                name = name,
                                href = url
                        )

                        result.add(item)
                    }
                }

                Log.e("MyLog", "loadInstitut: end")
        }
    }

    fun loadInstitut(context: Context){
        doAsync(context = context, presenter = presenter)
    }


}