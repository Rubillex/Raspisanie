package dev.prokrostinatorbl.raspisanie.new_version.providers

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import dev.prokrostinatorbl.raspisanie.new_version.models.MainModel
import dev.prokrostinatorbl.raspisanie.new_version.presenters.teacherPresenter
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class TeacherProvider(var presenter: teacherPresenter) {

    class doAsync(val context: Context,
                  val presenter: teacherPresenter,
                  val teach: ArrayList<String>
                  ) : AsyncTask<Void, Void, Void>(){

        private lateinit var doc: org.jsoup.nodes.Document
        private  lateinit var elements: Elements

        private var result: ArrayList<MainModel> = ArrayList()
        private val result_: ArrayList<MainModel> = ArrayList()


        init {
            execute()
        }

        override fun onPostExecute(res: Void?) {
            super.onPostExecute(res)
            presenter.TeachLoaded(teachList = result)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            Load(context = context, teach = teach)
            return null
        }

        fun hasConnection(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNW = cm.activeNetworkInfo
            return if (activeNW != null && activeNW.isConnected) {
                true
            } else false
        }

        fun Load(context: Context, teach: ArrayList<String>){
            val linkList = listOf<String>(
                    "ИББ https://www.asu.ru/timetable/lecturers/10/34/",
                    "ИББ https://www.asu.ru/timetable/lecturers/10/32/",
                    "ИББ https://www.asu.ru/timetable/lecturers/10/1109745079/",
                    "ИББ https://www.asu.ru/timetable/lecturers/10/33/",

                    "ИНГЕО https://www.asu.ru/timetable/lecturers/11/40/",
                    "ИНГЕО https://www.asu.ru/timetable/lecturers/11/38/",
                    "ИНГЕО https://www.asu.ru/timetable/lecturers/11/37/",
                    "ИНГЕО https://www.asu.ru/timetable/lecturers/11/39/",

                    "ИПО https://www.asu.ru/timetable/lecturers/13/1109745054/",

                    "ИП https://www.asu.ru/timetable/lecturers/14/1109745036/",
                    "ИП https://www.asu.ru/timetable/lecturers/14/8/",
                    "ИП https://www.asu.ru/timetable/lecturers/14/108/",

                    "ИСН https://www.asu.ru/timetable/lecturers/16/84/",
                    "ИСН https://www.asu.ru/timetable/lecturers/16/88/",
                    "ИСН https://www.asu.ru/timetable/lecturers/16/83/",
                    "ИСН https://www.asu.ru/timetable/lecturers/16/86/",

                    "ИИД https://www.asu.ru/timetable/lecturers/18/-1844155121/",
                    "ИИД https://www.asu.ru/timetable/lecturers/18/98/",
                    "ИИД https://www.asu.ru/timetable/lecturers/18/78/",

                    "ИИМО https://www.asu.ru/timetable/lecturers/20/59/",
                    "ИИМО https://www.asu.ru/timetable/lecturers/20/62/",
                    "ИИМО https://www.asu.ru/timetable/lecturers/20/63/",
                    "ИИМО https://www.asu.ru/timetable/lecturers/20/61/",
                    "ИИМО https://www.asu.ru/timetable/lecturers/20/64/",
                    "ИИМО https://www.asu.ru/timetable/lecturers/20/60/",

                    "ИХХФТ https://www.asu.ru/timetable/lecturers/22/67/",
                    "ИХХФТ https://www.asu.ru/timetable/lecturers/22/68/",
                    "ИХХФТ https://www.asu.ru/timetable/lecturers/22/66/",

                    "ИЦТЭФ https://www.asu.ru/timetable/lecturers/24/1109745088/",
                    "ИЦТЭФ https://www.asu.ru/timetable/lecturers/24/104/",
                    "ИЦТЭФ https://www.asu.ru/timetable/lecturers/24/103/",
                    "ИЦТЭФ https://www.asu.ru/timetable/lecturers/24/101/",
                    "ИЦТЭФ https://www.asu.ru/timetable/lecturers/24/106/",
                    "ИЦТЭФ https://www.asu.ru/timetable/lecturers/24/102/",
                    "ИЦТЭФ https://www.asu.ru/timetable/lecturers/24/109/",

                    "ИМКФП https://www.asu.ru/timetable/lecturers/26/94/",
                    "ИМКФП https://www.asu.ru/timetable/lecturers/26/91/",
                    "ИМКФП https://www.asu.ru/timetable/lecturers/26/96/",
                    "ИМКФП https://www.asu.ru/timetable/lecturers/26/73/",
                    "ИМКФП https://www.asu.ru/timetable/lecturers/26/1109745015/",
                    "ИМКФП https://www.asu.ru/timetable/lecturers/26/29/",
                    "ИМКФП https://www.asu.ru/timetable/lecturers/26/1109745067/",
                    "ИМКФП https://www.asu.ru/timetable/lecturers/26/26/",
                    "ИМКФП https://www.asu.ru/timetable/lecturers/26/22/",

                    "ИМИИТ https://www.asu.ru/timetable/lecturers/28/7/",
                    "ИМИИТ https://www.asu.ru/timetable/lecturers/28/2/",
                    "ИМИИТ https://www.asu.ru/timetable/lecturers/28/11/",
                    "ИМИИТ https://www.asu.ru/timetable/lecturers/28/5/",
                    "ИМИИТ https://www.asu.ru/timetable/lecturers/28/3/",
                    "ИМИИТ https://www.asu.ru/timetable/lecturers/28/4/",

                    "ЭФ https://www.asu.ru/timetable/lecturers/29/77/",
                    "ЭФ https://www.asu.ru/timetable/lecturers/29/111/",
                    "ЭФ https://www.asu.ru/timetable/lecturers/29/80/",
                    "ЭФ https://www.asu.ru/timetable/lecturers/29/76/",
                    "ЭФ https://www.asu.ru/timetable/lecturers/29/72/",
                    "ЭФ https://www.asu.ru/timetable/lecturers/29/1109745016/",
                    "ЭФ https://www.asu.ru/timetable/lecturers/29/71/",
                    "ЭФ https://www.asu.ru/timetable/lecturers/29/70/",
                    "ЭФ https://www.asu.ru/timetable/lecturers/29/82/",

                    "ОБЩ https://www.asu.ru/timetable/lecturers/32/863071517/",
                    "ОБЩ https://www.asu.ru/timetable/lecturers/32/1109745048/",

                    "ЮИ https://www.asu.ru/timetable/lecturers/34/55/",
                    "ЮИ https://www.asu.ru/timetable/lecturers/34/54/",
                    "ЮИ https://www.asu.ru/timetable/lecturers/34/1109745019/",
                    "ЮИ https://www.asu.ru/timetable/lecturers/34/52/",
                    "ЮИ https://www.asu.ru/timetable/lecturers/34/53/",
                    "ЮИ https://www.asu.ru/timetable/lecturers/34/56/",
                    "ЮИ https://www.asu.ru/timetable/lecturers/34/57/",
                    "ЮИ https://www.asu.ru/timetable/lecturers/34/1109745020/"
            )
            val file_name = "teacher"

            linkList.forEach {
                var name_href = it.split(" ")

                if (name_href[0] in teach){
                    if (hasConnection(context = context)){
                        doc = Jsoup.connect(name_href[1]).get()
                    } else {
                        try {
                            val `in` = File(context.filesDir.toString() + "Android/data/dev.prokrostinatorbl.raspisanie/files/" + file_name + linkList.indexOf(it) + ".html")
                            doc = Jsoup.parse(`in`, null)
                        } catch (e: IOException) {
                            Toast.makeText(context, "Кэш пуст", Toast.LENGTH_SHORT).show()
                        }
                    }

                    elements = doc.getElementsByClass("link_ptr_left margin_bottom")

                    var link: Elements

                    if (elements.size != 0){
                        elements.forEach { it_ ->
                            val temp = it_.text()

                            val name = temp.split(",")

                            link = it_.select("div.link_ptr_left.margin_bottom > a")
                            val url = link.attr("href")

                            val item = MainModel(
                                    name = name[0].toUpperCase(),
                                    href = "${name_href[1]}$url"
                            )
                            if (!item.name.startsWith("Вакансия") && !item.name.startsWith("Преподаватель"))
                                result_.add(item)
                        }
                    }
                }
            }
            result.addAll(result_)
        }

    }



    fun loadTeach(context: Context, teach: ArrayList<String>){
        doAsync(context = context, presenter = presenter, teach = teach)
    }
}