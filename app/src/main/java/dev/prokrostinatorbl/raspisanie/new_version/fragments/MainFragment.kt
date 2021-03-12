package dev.prokrostinatorbl.raspisanie.new_version.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.prokrostinatorbl.raspisanie.R
import dev.prokrostinatorbl.raspisanie.new_version.adapters.MainAdapter
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.*
import dev.prokrostinatorbl.raspisanie.new_version.models.MainModel
import dev.prokrostinatorbl.raspisanie.new_version.parrent
import dev.prokrostinatorbl.raspisanie.new_version.presenters.MainPresenter
import dev.prokrostinatorbl.raspisanie.new_version.service.TimeTableChecker
import dev.prokrostinatorbl.raspisanie.new_version.views.MainView
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class main : MvpAppCompatFragment(), MainView, MainAdapter.OnItemClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var APP_PREFERENCES_THEME: String
    private var mSettingMap: MutableMap<String, String> = HashMap<String, String>()
    lateinit var context_: Context

    private lateinit var mRvInstituts: RecyclerView
    private lateinit var mAdapter: MainAdapter

    private lateinit var mProgress: ProgressBar

    private lateinit var mllSearch: LinearLayout
    private lateinit var mllInput: LinearLayout

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

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

        val root = inflater.inflate(R.layout.fragment_main, container, false)

        var context = context
        if (context != null) {
            Saver.init(context)
        }

        mSettingMap.putAll(Saver.load_main())
        APP_PREFERENCES_THEME = mSettingMap.getValue("theme")

        val mTxtSearch = root.findViewById<TextInputEditText>(R.id.txtSearch)

        mllSearch = root.findViewById(R.id.llSearchText)
        mllInput = root.findViewById(R.id.llSearchInput)
        mllSearch.setOnClickListener {
            mllSearch.visibility = View.GONE
            mllInput.visibility = View.VISIBLE
            mTxtSearch.requestFocus()
            view?.showKeyboard()
        }


        mProgress = root.findViewById(R.id.progress_main)

        mRvInstituts = root.findViewById(R.id.RvMain)

        val mTxtIn = root.findViewById<TextView>(R.id.txtSearchText)

        mTxtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mAdapter.search(s.toString().toUpperCase())
                if (s.toString() == "") mTxtIn.text = "Поиск"
                else mTxtIn.text = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        mTxtSearch.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    search()
                    true
                }
                else -> false
            }
        }


        if (context != null) {
            mainPresenter.loadInstitut(context = context)
        }

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

    private fun search(){
        view?.clearFocus()
        view?.hideKeyboard()
        mllInput.visibility = View.GONE
        mllSearch.visibility = View.VISIBLE
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
    fun View.showKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInputFromWindow(windowToken, InputMethodManager.SHOW_FORCED, 0)
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

    override fun showError(text: String) {
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
    }

    override fun onItemClick(item: MainModel, position: Int) {
//        view?.hideKeyboard()
        val fr = fragmentManager?.beginTransaction()
        parrent.pushFragments(tag = parrent.TAB_STUDENTS, fragment = group.newInstance(item.name, item.href), shouldAdd = true, fTrans = fr!!)
    }

}

