package dev.prokrostinatorbl.raspisanie.new_version.help_functions

import android.content.Context
import android.util.Log
import dev.prokrostinatorbl.raspisanie.new_version.models.MainModel
import dev.prokrostinatorbl.raspisanie.new_version.models.TableSecond
import dev.prokrostinatorbl.raspisanie.new_version.models.TimeTableModel
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private val result: ArrayList<TimeTableModel> = ArrayList()
private val second: ArrayList<TableSecond> = ArrayList()

private val dateArray: ArrayList<String> = ArrayList()
private val timeArray: ArrayList<String> = ArrayList()
private val locArray: ArrayList<String> = ArrayList()
private val prepodArray: ArrayList<String> = ArrayList()
private val nameArray: ArrayList<String> = ArrayList()

private lateinit var doc: org.jsoup.nodes.Document

private lateinit var base_elements: org.jsoup.nodes.Element
private lateinit var base_child: Elements

private lateinit var child: Elements
private lateinit var children: org.jsoup.nodes.Element
private lateinit var class_child: String

private lateinit var elements: Elements
private lateinit var elements_day: Elements
private lateinit var elements_time: Elements

private lateinit var time_par: String
private lateinit var time_split: Array<String>

private lateinit var second_time: String

private lateinit var name_para: String
private lateinit var prepod_name: String
private lateinit var loc: String


fun LoadStudents(context: Context, href: String, name: String, page: Int, group: String): ArrayList<TimeTableModel> {

    result.clear()

    Log.e("SERVICE_TEST_!", "href: $href name: $name page: $page group: $group")

    val link_asu = "https://www.asu.ru/timetable/students/$name${getTime(page)}"

    Log.e("FUCK", link_asu)

    val `in` = File("${context.filesDir}Android/data/dev.prokrostinatorbl.raspisanie/files/${group}first.html")
    doc = Jsoup.connect(link_asu).get()

    base_elements = doc.getElementsByClass("align_top schedule").first()
    child = base_elements.allElements
    elements = doc.getElementsByClass("schedule-time" + "schedule-time schedule-current")
    elements_day = doc.getElementsByClass("schedule-date")
    elements_time = doc.getElementsByClass("date t_small_x t_gray_light")
    base_child = child.select("tr")

    var time_: String = ""
    var loc_ : String = ""
    var prep_: String = ""
    var date_: String = ""
    var para_: String = ""

    if (base_child.size != 0){
        base_child.forEach {
            when(it.attr("class")){
                "schedule-date" -> {
                    second.add(TableSecond("Day", it.text()))
                    date_ = it.text()
                }
                "schedule-time schedule-current" -> {
                    time_par = it.select("td").get(1).text()
                    time_split = time_par.split(":").toTypedArray()
                    if (time_split.size > 1) {
                        second_time = time_par
                        second.add(TableSecond("Time", second_time))
                        time_ = second_time
                    } else {
                        second.add(TableSecond("Time", second_time))
                        time_ = second_time
                    }

                    name_para = it.select("td").get(2).text()
                    second.add(TableSecond("Para", name_para))
                    para_ = name_para

                    prepod_name = it.select("td").get(3).text()
                    if (prepod_name == "")
                        prepod_name = " "
                    second.add(TableSecond("Prepod", prepod_name))
                    prep_ = prepod_name

                    loc = it.select("td").get(4).text()
                    second.add(TableSecond("Location", loc))
                    loc_ = loc
                }
                "schedule-time" -> {
                    time_par = it.select("td").get(1).text()
                    time_split = time_par.split(":").toTypedArray()
                    if (time_split.size > 1) {
                        second_time = time_par
                        second.add(TableSecond("Time", second_time))
                        time_ = second_time
                    } else {
                        second.add(TableSecond("Time", second_time))
                        time_ = second_time
                    }

                    name_para = it.select("td").get(2).text()
                    second.add(TableSecond("Para", name_para))
                    para_ = name_para

                    prepod_name = it.select("td").get(3).text()
                    if (prepod_name == "")
                        prepod_name = " "
                    second.add(TableSecond("Prepod", prepod_name))
                    prep_ = prepod_name

                    loc = it.select("td").get(4).text()
                    second.add(TableSecond("Location", loc))
                    loc_ = loc
                }
            }
            val item = TimeTableModel(
                    time = time_,
                    prepod = prep_,
                    audit = loc_,
                    date = date_,
                    name = para_
            )
            time_ = ""
            loc_  = ""
            prep_ = ""
            date_ = ""
            para_ = ""
            result.add(item)
        }
    }

    result.forEach {
        Log.e("FUCK", "${it.date} ${it.time} ${it.name} ${it.prepod} ${it.audit}")
    }

    return result
}