package dev.prokrostinatorbl.raspisanie.new_version

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.prokrostinatorbl.raspisanie.BuildConfig
import dev.prokrostinatorbl.raspisanie.R
import dev.prokrostinatorbl.raspisanie.new_version.fragments.*
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.*
import dev.prokrostinatorbl.raspisanie.new_version.service.TimeTableChecker
import moxy.MvpAppCompatActivity
import moxy.MvpAppCompatFragment
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.coroutineContext


class parrent : MvpAppCompatActivity() {

    private val sharedPrefs by lazy {  getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    private lateinit var APP_PREFERENCES_THEME: String
    private var mSettingMap: MutableMap<String, String> = HashMap<String, String>()

    private lateinit var start_frame: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())


        val currentNightMode = (resources.configuration.uiMode
                and Configuration.UI_MODE_NIGHT_MASK)


        when (getSavedTheme()) {
            THEME_LIGHT -> {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            THEME_DARK -> {
                window.decorView.systemUiVisibility = 0
            }
            THEME_SYSTEM -> {
                when (currentNightMode) {
                    Configuration.UI_MODE_NIGHT_NO -> window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    Configuration.UI_MODE_NIGHT_YES -> window.decorView.systemUiVisibility = 0
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

                }
            }
            THEME_UNDEFINED -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    Configuration.UI_MODE_NIGHT_YES -> window.decorView.systemUiVisibility = 0
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> when (currentNightMode) {
                        Configuration.UI_MODE_NIGHT_NO -> window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        Configuration.UI_MODE_NIGHT_YES -> window.decorView.systemUiVisibility = 0
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

                    }
                }
            }
        }

        Saver.init(context = applicationContext)
        mSettingMap.putAll(Saver.load_main())
        mSettingMap.putAll(Saver.load_service())
        mSettingMap.putAll(Saver.load_group())
        APP_PREFERENCES_THEME = mSettingMap.getValue("theme")

        setContentView(R.layout.activity_parrent)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        start_frame = intent.getStringExtra("frame")

        if (start_frame == "main"){
            if (savedInstanceState?.get("stack") == null) {
                createStack()
                val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                pushFragments(tag = TAB_STUDENTS, fragment = main(), shouldAdd = true, fTrans = fragmentTransaction)
            }
        } else {
            if (savedInstanceState?.get("stack") == null) {
                createStack()
                val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                val array: Array<String> = arrayOf(mSettingMap["start_uni"].toString(), mSettingMap["group"].toString(), mSettingMap["link"].toString(), mSettingMap["table"].toString(), "from_start")
                pushFragments(tag = TAB_STUDENTS, fragment = TimeTableFragment.newInstance(array), shouldAdd = true, fTrans = fragmentTransaction)
            }
        }


        Thread.setDefaultUncaughtExceptionHandler { thread, ex -> handleUncaughtException(thread, ex) }

        if (mSettingMap["service"]=="true"){
            val serviceClass = TimeTableChecker::class.java
            val intent = Intent(this, serviceClass)
            startService(intent)
        }
    }

    fun handleUncaughtException(thread: Thread?, e: Throwable) {
        val versionName: String = BuildConfig.VERSION_NAME
        val toast = Toast.makeText(applicationContext,
                "Приложение крашнулось! Выберите способ отправки логов", Toast.LENGTH_SHORT)
        toast.show()
        val stackTrace = Log.getStackTraceString(e)
        val message = e.message
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("gubchenko.vadim@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "MyApp Crash log file. version:$versionName")
        intent.putExtra(Intent.EXTRA_TEXT, stackTrace)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // required when starting from Application
        startActivity(intent)
    }


    // сохранение состояния
    override fun onSaveInstanceState(outState: Bundle) {
        val arr = ArrayList<HashMap<String, Stack<MvpAppCompatFragment>>>()
        outState.putParcelableArrayList("stack", arr as ArrayList<out Parcelable?>)
//        outState.putSerializable("stack", mStacks)

        super.onSaveInstanceState(outState)
    }

    // получение ранее сохраненного состояния
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val a = savedInstanceState["stack"] as ArrayList<HashMap<String, Stack<MvpAppCompatFragment>>>?
        if (a!!.size != 0)
            mStacks = a[0]

        Log.e("stack", "aaa")
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment: MvpAppCompatFragment = mStacks[mCurrentTab]!!.elementAt(mStacks[mCurrentTab]!!.size - 1)
        gotoFragment(fragment, fragmentTransaction)
    }

    private var currentPageId = -1

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if(currentPageId == item.itemId) {
            false
        } else {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            currentPageId = item.itemId
            when (item.itemId) {
                R.id.setting_button -> {
                    selectedTab(tabId = TAB_SETTING, fTrans = fragmentTransaction)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.students -> {
                    selectedTab(tabId = TAB_STUDENTS, fTrans = fragmentTransaction)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.teach -> {
                    selectedTab(tabId = TAB_TEACH, fTrans = fragmentTransaction)
                    return@OnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {

        Log.e("ItemClick", "back ${mStacks[mCurrentTab]!!.size}")

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        if (start_frame == "main"){

            if (mStacks[mCurrentTab]!!.size == 1){
                super.onBackPressed()
                finishAffinity()
            }
            popFragments(fTrans = fragmentTransaction)
        }
        if (start_frame != "main"){
            if (mStacks[mCurrentTab]!!.size == 1){
                replaceFragment(tag = TAB_STUDENTS, fragment = main(), shouldAdd = true, fTrans = fragmentTransaction)
            } else {
                if (mStacks[mCurrentTab]!!.size < 1){
                    super.onBackPressed()
                    finishAffinity()
                } else {
                    popFragments(fTrans = fragmentTransaction)
                }
            }
        }
    }

    private fun getSavedTheme() = sharedPrefs.getInt(KEY_THEME, THEME_UNDEFINED)

    companion object{
        private lateinit var mStacks: HashMap<String, Stack<MvpAppCompatFragment>>
        var TAB_STUDENTS = "tab_students"
        var TAB_TEACH = "tab_teach"
        var TAB_SETTING = "tab_setting"

        private var mCurrentTab: String = ""

        fun createStack(){
            Log.e("stack", "bbb")
            mCurrentTab = TAB_STUDENTS
            mStacks = HashMap<String, Stack<MvpAppCompatFragment>>()
            mStacks[TAB_STUDENTS] = Stack<MvpAppCompatFragment>()
            mStacks[TAB_TEACH] = Stack<MvpAppCompatFragment>()
            mStacks[TAB_SETTING] = Stack<MvpAppCompatFragment>()
        }

        fun selectedTab(tabId: String, fTrans: FragmentTransaction){
            mCurrentTab = tabId

            if (mStacks[tabId]!!.size == 0){
                when (tabId) {
                    TAB_STUDENTS -> {
                        pushFragments(tag = tabId, fragment = main(), shouldAdd = true, fTrans = fTrans)
                    }
                    TAB_TEACH -> {
                        pushFragments(tag = tabId, fragment = teacher(), shouldAdd = true, fTrans = fTrans)
                    }
                    TAB_SETTING -> {
                        pushFragments(tag = tabId, fragment = Setting(), shouldAdd = true, fTrans = fTrans)
                    }
                }
            } else {
                val bundle: Bundle = Bundle()
                pushFragments(tag = tabId, mStacks[tabId]!!.lastElement(), shouldAdd = false, fTrans = fTrans)
            }
        }


        fun gotoFragment(fragment: MvpAppCompatFragment, fTrans: FragmentTransaction){
            fTrans.replace(R.id.container, fragment)
            fTrans.commit()
        }

        fun pushFragments(tag: String, fragment: MvpAppCompatFragment, shouldAdd: Boolean, fTrans: FragmentTransaction){
            if (shouldAdd){
                mStacks[tag]?.push(fragment)
                Log.e("ItemClick", "push")
            }

            fTrans.replace(R.id.container, fragment)
            fTrans.commit()
        }

        fun replaceFragment(tag: String, fragment: MvpAppCompatFragment, shouldAdd: Boolean, fTrans: FragmentTransaction){
            mStacks[mCurrentTab]!!.pop()
            pushFragments(tag = tag, fragment = fragment, shouldAdd = shouldAdd, fTrans = fTrans)
        }

        fun popFragments(fTrans: FragmentTransaction){

            val fragment: MvpAppCompatFragment = mStacks[mCurrentTab]!!.elementAt(mStacks[mCurrentTab]!!.size - 2)

            mStacks[mCurrentTab]!!.pop()

            fTrans.replace(R.id.container, fragment)
            fTrans.commit()

        }

    }
}