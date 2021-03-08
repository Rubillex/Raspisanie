package dev.prokrostinatorbl.raspisanie.new_version.fragments

import android.R.attr.animation
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import dev.prokrostinatorbl.raspisanie.R
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.Saver
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.TablePagerAdapter
import dev.prokrostinatorbl.raspisanie.new_version.parrent
import moxy.MvpAppCompatFragment
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimeTableFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimeTableFragment : MvpAppCompatFragment() {
    // TODO: Rename and change types of parameters
    private var param1: Array<String> = arrayOf("", "", "")

    private var params: ArrayList<String> = ArrayList()

    private var mSettingMap: MutableMap<String, String> = HashMap<String, String>()

    private var name: String = ""
    private var href: String = ""
    private var group_name: String = ""

    private var table: String = ""
    private var from: String = ""

    private lateinit var mAdapter: TablePagerAdapter

    lateinit var viewPager: ViewPager

    lateinit var mSaveGroup: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getStringArray(ARG_PARAM1) as Array<String>
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pager, container, false)

        var context = context
        if (context != null) {
            Saver.init(context)
        }
        mSettingMap.putAll(Saver.load_setting())
        mSettingMap.putAll(Saver.load_group())

        viewPager = view.findViewById(R.id.pager)

        mAdapter = TablePagerAdapter(parentFragmentManager)

        params.addAll(param1)

        val parcelable: Parcelable? = savedInstanceState?.getParcelable("pager")

        if (parcelable == null){
            from = param1[4]
            table = param1[3]
            name = param1[2]
            href = param1[1]
            group_name = param1[0]
            setupViewPager(savedInstanceState, false)
        } else {
            from = param1[4]
            table = param1[3]
            name = param1[2]
            href = param1[1]
            group_name = param1[0]
            setupViewPager(savedInstanceState, true)
        }

        val textView = view.findViewById<TextView>(R.id.textView)
        val textView2 = view.findViewById<TextView>(R.id.textView2)
        textView.text = if(table == "stud"){
            "$group_name: $href"
        } else {
            href
        }

        val mBack = view.findViewById<ImageView>(R.id.imageView)
        val fm: FragmentManager = parentFragmentManager

        val fragmentTransaction: FragmentTransaction = fm.beginTransaction()
        if (from == "from_start") {
            mBack.setBackgroundResource(R.drawable.ic_home)
            mBack.setOnClickListener {
//                parrent.gotoFragment(fragment = main(), fTrans = fragmentTransaction)
                parrent.replaceFragment(tag = parrent.TAB_STUDENTS, fragment = main(), shouldAdd = false, fTrans = fragmentTransaction)
            }
        } else {
            mBack.setBackgroundResource(R.drawable.ic_arrow)
            mBack.setOnClickListener { parrent.popFragments(fragmentTransaction) }
        }

        mSaveGroup = view.findViewById<ImageView>(R.id.saveGroup)
        mSaveGroup.setOnClickListener { saveGroupClick() }

        if (mSettingMap["group"] == href &&
                mSettingMap["start_uni"] == group_name &&
                mSettingMap["link"] == name) {
            if (checkSave()) mSaveGroup.setBackgroundResource(R.drawable.ic_test_2)
                else mSaveGroup.setBackgroundResource(R.drawable.ic_test_1)
        } else {
            mSaveGroup.setBackgroundResource(R.drawable.ic_test_1)
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageSelected(position: Int) {
                val cal: Calendar = Calendar.getInstance()
                val num = cal.get(Calendar.WEEK_OF_YEAR)
                val number = num + position
                textView2.text = (if ((number % 2) == 0) {
                    "синяя неделя"
                } else {
                    "красная неделя"
                }).toString()
            }

        })

        return view
    }

    fun checkSave(): Boolean{
        return mSettingMap["start_frame"] == "timetable"
    }

    fun saveGroupClick(){

        if (mSettingMap["start_frame"] == "timetable"){
            Saver.save_group(
                    _group = "0",
                    _start_uni = "0",
                    _link = "0",
                    _table = "0",
                    _start_frame = "main",
                    _start_link = "0"
            )
            Saver.save_service(
                    bool = "false"
            )

            val animation_1 = AnimationUtils.loadAnimation(activity, R.anim.out_out)
            val animation_2 = AnimationUtils.loadAnimation(activity, R.anim.out_in)
            val animation2 = AnimationUtils.loadAnimation(activity, R.anim.scale_out)

            animation_1.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    mSaveGroup.startAnimation(animation_2)
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })

            animation_2.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    mSaveGroup.setBackgroundResource(R.drawable.ic_test_1)
                    mSaveGroup.startAnimation(animation2)
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
            mSaveGroup.startAnimation(animation_1)
            Log.e("LOOOOOG_1", "$href $group_name $name $table")
        } else {
            Saver.save_group(
                    _group = href,
                    _start_uni = group_name,
                    _link = name,
                    _table = table,
                    _start_frame = "timetable",
                    _start_link = (if (table == "stud") {
                        "https://www.asu.ru/timetable/students/"
                    } else {
                        ""
                    }).toString()
            )
            Saver.save_service(
                    bool = "true"
            )

            val animation_1 = AnimationUtils.loadAnimation(activity, R.anim.out_out)
            val animation_2 = AnimationUtils.loadAnimation(activity, R.anim.out_in)
            val animation2 = AnimationUtils.loadAnimation(activity, R.anim.scale_out)

            animation_1.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    mSaveGroup.startAnimation(animation_2)
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })

            animation_2.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    mSaveGroup.setBackgroundResource(R.drawable.ic_test_2)
                    mSaveGroup.startAnimation(animation2)
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
            mSaveGroup.startAnimation(animation_1)

            Log.e("LOOOOOG_1", "$href $group_name $name $table")
        }
        mSettingMap.clear()
        mSettingMap.putAll(Saver.load_setting())
        mSettingMap.putAll(Saver.load_group())

    }

    // сохранение состояния
    override fun onSaveInstanceState(outState: Bundle) {
        val parcelable: Parcelable? = mAdapter.saveState()
        outState.putParcelable("pager", parcelable)
        super.onSaveInstanceState(outState)
    }

    private fun setupViewPager(savedInstanceState: Bundle?, save: Boolean){


            when(table){
                "teach" -> {
                    mAdapter.addFrag(TimeTablePage.newInstance(name, group_name, href, table))
                    mAdapter.addFrag(TimeTablePage2.newInstance(name, group_name, href, table))
                }
                "stud" -> {
                    mAdapter.addFrag(TimeTablePage.newInstance(name, group_name, href, table))
                    mAdapter.addFrag(TimeTablePage2.newInstance(name, group_name, href, table))
                }
            }


        viewPager.adapter = mAdapter
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
        fun newInstance(param1: Array<String>) =
                TimeTableFragment().apply {
                    arguments = Bundle().apply {
                        putStringArray(ARG_PARAM1, param1)
                    }
                }
    }
}