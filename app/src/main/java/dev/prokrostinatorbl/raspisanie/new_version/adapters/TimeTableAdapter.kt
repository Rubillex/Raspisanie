package dev.prokrostinatorbl.raspisanie.new_version.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.prokrostinatorbl.raspisanie.R
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.*
import dev.prokrostinatorbl.raspisanie.new_version.models.TimeTableModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private lateinit var date: Array<String>
private lateinit var c: Context

private lateinit var APP_PREFERENCES_THEME: String

class TimeTableAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mTableList: ArrayList<TimeTableModel> = ArrayList()
    private var mDayList: ArrayList<Int> = ArrayList()

    fun setupList(tableList: ArrayList<TimeTableModel>, context: Context, theme: String){
        c = context
        APP_PREFERENCES_THEME = theme
        mTableList.clear()
        mTableList.addAll(tableList)
        mDayList.clear()
        mDayList.add(0)
        mTableList.forEachIndexed { index, it ->
            if (it.date != ""){
                mDayList.add(index)
            }
        }

        mTableList.forEachIndexed { index, it ->
            Log.e("INDEX", "$index ${it.date}")
        }

        mDayList.forEach {
            Log.e("INDEX", it.toString())
        }

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (this.getItemViewType(position)){
            0 -> {
                if (holder is TimeTableViewHolder)
                    holder.bind(tableModel = mTableList[position], "normal", context = c, APP_PREFERENCES_THEME = APP_PREFERENCES_THEME)
            }
            else -> {
                if (holder is TimeTableViewHolder)
                    holder.bind(tableModel = mTableList[position], "Day", context = c, APP_PREFERENCES_THEME = APP_PREFERENCES_THEME)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            0 -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemView = layoutInflater.inflate(R.layout.cell_para, parent, false)
                TimeTableViewHolder(itemView = itemView)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemView = layoutInflater.inflate(R.layout.fragment_timetable_custom, parent, false)
                TimeTableViewHolder(itemView = itemView)
            }
        }
    }

    override fun getItemCount(): Int {
        return mTableList.count()
    }

    override fun getItemViewType(position: Int): Int {

        mDayList.forEach {
            if (position == it){
                return 0
            }
        }
        return 1
    }

    class TimeTableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val sharedPrefs by lazy {  itemView.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }



        @SuppressLint("ResourceAsColor")
        fun bind(tableModel: TimeTableModel, type: String, context: Context, APP_PREFERENCES_THEME: String){
            when (type){
                "normal" -> { // разделитель
                    val txtRazd = itemView.findViewById<TextView>(R.id.txtRazd)
                    val txtPara = itemView.findViewById<TextView>(R.id.time_para)
                    val txtName = itemView.findViewById<TextView>(R.id.name_of_par)
                    val txtPrep = itemView.findViewById<TextView>(R.id.docent)
                    val txtLoca = itemView.findViewById<TextView>(R.id.auditoria)
                    val mRL = itemView.findViewById<RelativeLayout>(R.id.mRL)

                    date = tableModel.date.split(" ".toRegex()).toTypedArray()
                    val currentDate = Date()
                    val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    val dateText: String = dateFormat.format(currentDate)

                    if (dateText == date[1]){
                        txtRazd.setBackgroundResource(R.color.day)
                        txtRazd.setTextColor(Color.BLACK)
                    } else {
                        txtRazd.setBackgroundResource(R.color.line)
                    }

                    txtRazd.text = tableModel.date

                    Log.e("RAZD", "time ${tableModel.time} loc ${tableModel.audit} prep ${tableModel.prepod} name ${tableModel.name}")

                    if (tableModel.time == "" &&
                            tableModel.audit == "" &&
                            tableModel.prepod == "" &&
                            tableModel.name == "") {
                        mRL.visibility = View.GONE
                    }

                    txtPara.text = tableModel.time
                    txtLoca.text = tableModel.audit
                    txtName.text = tableModel.name
                    txtPrep.text = tableModel.prepod
                }
                "Day" -> { // расписание
                    val txtPara = itemView.findViewById<TextView>(R.id.time_para)
                    val txtName = itemView.findViewById<TextView>(R.id.name_of_par)
                    val txtPrep = itemView.findViewById<TextView>(R.id.docent)
                    val txtLoca = itemView.findViewById<TextView>(R.id.auditoria)
                    val bg_par = itemView.findViewById<LinearLayout>(R.id.bg_par_fragment)

                    val currentDate = Date()


                    val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    val dateText: String = dateFormat.format(currentDate)

//                Log.i("ТЕКУЩАЯ ДАТА",dateText);
                    val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val curr_date = timeFormat.format(currentDate)

//                Log.i("ТЕКУЩЕЕ ВРЕМЯ", curr_date);

                    val start_t: CharArray = tableModel.time.toCharArray() //преобразования слова в массив символов

                    val hh_f = String(start_t, 0, 2) // начиная с нулевого символа(до строки) забираем 4 символа в переменную

                    val mm_f = String(start_t, 3, 2) // начиная с 4 символа забираем два символа в переменную

                    val hh_s = String(start_t, 8, 2) // начиная с нулевого символа(до строки) забираем 4 символа в переменную

                    val mm_s = String(start_t, 11, 2) // начиная с 4 символа забираем два символа в переменную


                    val CURRENT_DATE = curr_date.split(":".toRegex()).toTypedArray()

                    val START_PAR_HH = hh_f.toInt()
                    val START_PAR_mm = mm_f.toInt()

                    val END_PAR_HH = hh_s.toInt()
                    val END_PAR_mm = mm_s.toInt()

                    val CURRENT_DATE_HH = CURRENT_DATE[0].toInt()
                    val CURRENT_DATE_mm = CURRENT_DATE[1].toInt()

                    val curr_dbl = (CURRENT_DATE_HH * 60 + CURRENT_DATE_mm).toDouble()

                    val start_dbl = (START_PAR_HH * 60 + START_PAR_mm).toDouble()
                    val end_dbl = (END_PAR_HH * 60 + END_PAR_mm).toDouble()


                    Log.e("ПАРА", "$curr_dbl $start_dbl $end_dbl $dateText ${date[1]}")

                    if (curr_dbl in start_dbl..end_dbl && dateText == date[1]) {
                        Log.e("ПАРАААА", "")
                        bg_par.setBackgroundResource(R.drawable.study_timetable_bg_on)
                        txtPara.setTextColor(Color.WHITE)
                        txtLoca.setTextColor(Color.WHITE)
                        txtName.setTextColor(Color.WHITE)
                        txtPrep.setTextColor(Color.WHITE)
                    }

                    txtPara.text = tableModel.time
                    txtLoca.text = tableModel.audit
                    txtName.text = tableModel.name
                    txtPrep.text = tableModel.prepod
                }
            }
        }

        private fun getSavedTheme() = sharedPrefs.getInt(KEY_THEME, THEME_UNDEFINED)

    }

}