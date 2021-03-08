package dev.prokrostinatorbl.raspisanie.new_version.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dev.prokrostinatorbl.raspisanie.R
import dev.prokrostinatorbl.raspisanie.new_version.adapters.TimeTableAdapter
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.Saver
import dev.prokrostinatorbl.raspisanie.new_version.models.TimeTableModel
import dev.prokrostinatorbl.raspisanie.new_version.presenters.TimeTablePresenter
import dev.prokrostinatorbl.raspisanie.new_version.views.TimeTableView
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_TABLE = "table"
/**
 * A simple [Fragment] subclass.
 * Use the [TimeTableFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimeTablePage : MvpAppCompatFragment(), TimeTableView {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    private var table: String? = null

    private lateinit var mRvTable: RecyclerView
    private lateinit var mProgress: ProgressBar
    private lateinit var mAdapter: TimeTableAdapter

    lateinit var APP_PREFERENCES_THEME: String
    private var mSettingMap: MutableMap<String, String> = HashMap<String, String>()
    lateinit var context_: Context

    @InjectPresenter
    lateinit var tablePresenter: TimeTablePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)
            table = it.getString(ARG_TABLE)
        }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_time_table, container, false)

        var context = context
        if (context != null) {
            Saver.init(context)
        }
        mSettingMap.clear()
        mSettingMap.putAll(Saver.load_main())
        mSettingMap.putAll(Saver.load_group())
        APP_PREFERENCES_THEME = mSettingMap.getValue("theme")


        mRvTable = view.findViewById(R.id.RvPage1)
        mProgress = view.findViewById(R.id.progress_page_1)

        mAdapter = TimeTableAdapter()

        mRvTable.adapter = mAdapter
        mRvTable.layoutManager = LinearLayoutManager(
                activity,
                OrientationHelper.VERTICAL,
                false
        )

        when(table){
            "stud" -> if (context != null){
                tablePresenter.loadTable(context = context, href = param3!!, name = param1!!, group = param2!!, page = 0)
            }

            "teach" -> if (context != null){
                tablePresenter.loadTeach(context = context, href = param3!!, name = param1!!, group = param2!!, page = 0)
            }
        }

        return view
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TimeTableFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String, table: String) =
                TimeTablePage().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                        putString(ARG_PARAM3, param3)
                        putString(ARG_TABLE, table)
                    }
                }
    }

    private val mTableList: ArrayList<TimeTableModel> = ArrayList()

    override fun startLoading() {
        mProgress.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
    }

    override fun endLoading() {
        mProgress.visibility = View.GONE
    }
    override fun setupList(tableList: ArrayList<TimeTableModel>) {
        mRvTable.visibility = View.VISIBLE
        mAdapter.setupList(tableList = tableList, context = requireActivity(), theme = APP_PREFERENCES_THEME)
        mTableList.clear()
        mTableList.addAll(tableList)
        if (param1 == mSettingMap["link"]
                && param3 == mSettingMap["group"]
                && param2 == mSettingMap["start_uni"]){
            saveData(mTableList)
        }
    }

    fun saveData(mTableList: ArrayList<TimeTableModel>){
        val gson = Gson()
        val string_gson = gson.toJson(mTableList)
        Saver.save_timetable_1(string_gson)
    }
}