package dev.prokrostinatorbl.raspisanie.new_version.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import dev.prokrostinatorbl.raspisanie.R
import dev.prokrostinatorbl.raspisanie.new_version.adapters.MainAdapter
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.*
import dev.prokrostinatorbl.raspisanie.new_version.models.MainModel
import dev.prokrostinatorbl.raspisanie.new_version.parrent
import dev.prokrostinatorbl.raspisanie.new_version.presenters.teacherPresenter
import dev.prokrostinatorbl.raspisanie.new_version.views.TeacherView
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class teacher : MvpAppCompatFragment(), TeacherView, MainAdapter.OnItemClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var APP_PREFERENCES_THEME: String
    private var mSettingMap: MutableMap<String, String> = HashMap<String, String>()
    lateinit var context_: Context

    private lateinit var mRvInstituts: RecyclerView
    private lateinit var mAdapter: MainAdapter

    private lateinit var chIbb: CheckBox
    private lateinit var chIngeo: CheckBox
    private lateinit var chIpo: CheckBox
    private lateinit var chIp: CheckBox
    private lateinit var chIsn: CheckBox
    private lateinit var chIid: CheckBox
    private lateinit var chIimo: CheckBox
    private lateinit var chIhhft: CheckBox
    private lateinit var chIctef: CheckBox
    private lateinit var chImkfp: CheckBox
    private lateinit var chImiit: CheckBox
    private lateinit var chEf: CheckBox
    private lateinit var chObs: CheckBox
    private lateinit var chYi: CheckBox

    private lateinit var llTeacher: ScrollView

    private var teach: ArrayList<String> = ArrayList()

    private lateinit var mProgress: ProgressBar
    private lateinit var mInfo: TextView
    private lateinit var mTxtSearch: EditText

    @InjectPresenter
    lateinit var mainPresenter: teacherPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.teacher, container, false)


        var context = context
        if (context != null) {
            Saver.init(context)
        }

        mSettingMap.putAll(Saver.load_main())
        APP_PREFERENCES_THEME = mSettingMap.getValue("theme")


        mTxtSearch = root.findViewById<EditText>(R.id.txtSearch)
        mProgress = root.findViewById(R.id.progress_main)
        mProgress.visibility = View.GONE

        mRvInstituts = root.findViewById(R.id.RvMain)
        mInfo = root.findViewById(R.id.txtInfo)

        chIbb = root.findViewById(R.id.chIbb)
        chIngeo = root.findViewById(R.id.chIng)
        chIpo = root.findViewById(R.id.chIpo)
        chIp = root.findViewById(R.id.chIp)
        chIsn = root.findViewById(R.id.chIsn)
        chIid = root.findViewById(R.id.chIid)
        chIimo = root.findViewById(R.id.chIimo)
        chIhhft = root.findViewById(R.id.chIhft)
        chIctef = root.findViewById(R.id.chIctef)
        chImkfp = root.findViewById(R.id.chImkfp)
        chImiit = root.findViewById(R.id.chImiit)
        chEf = root.findViewById(R.id.chEf)
        chObs = root.findViewById(R.id.chObs)
        chYi = root.findViewById(R.id.chYi)

        llTeacher = root.findViewById(R.id.llTeacher)

        root.viewTreeObserver.addOnGlobalLayoutListener(
                OnGlobalLayoutListener {
                    val r = Rect()
                    root.getWindowVisibleDisplayFrame(r)
                    val screenHeight: Int = root.rootView.height
                    val keypadHeight: Int = screenHeight - r.bottom
                    if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                        mInfo.visibility = View.GONE
                        llTeacher.visibility = View.VISIBLE
                        endLoading()
                        mRvInstituts.visibility = View.GONE
                    } else {
                        llTeacher.visibility = View.GONE
                        mRvInstituts.visibility = View.VISIBLE
                    }
                })

        mTxtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (context != null) {
                    mInfo.visibility = View.GONE
                    teachCreate()
                    mainPresenter.loadTeach(context = context, teach = teach)
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })




        mAdapter = MainAdapter(this)

        mRvInstituts.adapter = mAdapter
        mRvInstituts.layoutManager = LinearLayoutManager(
                activity,
                OrientationHelper.VERTICAL,
                false
        )

        when(Themer.onActivityCreateSetTheme(APP_PREFERENCES_THEME = APP_PREFERENCES_THEME)){
            "Light" -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
            "Dark" -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
            "auto" -> setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, THEME_SYSTEM)
        }

        return root
    }

    private fun teachCreate(){
        teach.clear()
        if (chIbb.isChecked) teach.add("ИББ")
        if (chIngeo.isChecked) teach.add("ИНГЕО")
        if (chIpo.isChecked) teach.add("ИПО")
        if (chIp.isChecked) teach.add("ИП")
        if (chIsn.isChecked) teach.add("ИСН")
        if (chIid.isChecked) teach.add("ИИД")
        if (chIimo.isChecked) teach.add("ИИМО")
        if (chIhhft.isChecked) teach.add("ИХХФТ")
        if (chIctef.isChecked) teach.add("ИЦТЭФ")
        if (chImkfp.isChecked) teach.add("ИМКФП")
        if (chImiit.isChecked) teach.add("ИМИИТ")
        if (chEf.isChecked) teach.add("ЭФ")
        if (chObs.isChecked) teach.add("ОБЩ")
        if (chYi.isChecked) teach.add("ЮИ")
        if (teach.isEmpty()) {
            teach.add("")
            mInfo.visibility = View.VISIBLE
        }
    }

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                main().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun startLoading() {
        mProgress.visibility = View.VISIBLE
    }

    override fun endLoading() {
        mProgress.visibility = View.GONE
    }

    override fun setupList(mainList: ArrayList<MainModel>) {
        mRvInstituts.visibility = View.VISIBLE
        mAdapter.setupList(mainList = mainList)
        val txt = mTxtSearch.text
        mAdapter.search(txt.toString())
    }

    override fun onItemClick(item: MainModel, position: Int) {
        val fr = fragmentManager?.beginTransaction()
        val array: Array<String> = arrayOf(position.toString(), item.name, item.href, "teach")
        parrent.pushFragments(tag = parrent.TAB_TEACH, fragment = TimeTableFragment.newInstance(array), shouldAdd = true, fTrans = fr!!)
    }

}

