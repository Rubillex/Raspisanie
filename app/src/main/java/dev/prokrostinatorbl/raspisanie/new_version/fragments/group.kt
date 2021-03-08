package dev.prokrostinatorbl.raspisanie.new_version.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.RecoverySystem
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.prokrostinatorbl.raspisanie.R
import dev.prokrostinatorbl.raspisanie.new_version.adapters.GroupAdapter
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.Saver
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.Themer
import dev.prokrostinatorbl.raspisanie.new_version.models.GroupModel
import dev.prokrostinatorbl.raspisanie.new_version.models.GroupToTableModel
import dev.prokrostinatorbl.raspisanie.new_version.parrent
import dev.prokrostinatorbl.raspisanie.new_version.presenters.GroupPresenter
import dev.prokrostinatorbl.raspisanie.new_version.views.GroupView
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private var group_name: String = ""
private var href: String = ""
/**
 * A simple [Fragment] subclass.
 * Use the [group.newInstance] factory method to
 * create an instance of this fragment.
 */
class group : MvpAppCompatFragment(), GroupView, GroupAdapter.OnItemClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var APP_PREFERENCES_THEME: String
    private var mSettingMap: MutableMap<String, String> = HashMap<String, String>()
    lateinit var context_: Context

    private lateinit var mRvGroup: RecyclerView
    private lateinit var mAdapter: GroupAdapter
    private lateinit var mProgress: ProgressBar

    private lateinit var href: String
    private lateinit var name: String

    @InjectPresenter
    lateinit var groupPresenter: GroupPresenter

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
        val view = inflater.inflate(R.layout.fragment_group, container, false)

        var context = context
        if (context != null) {
            Saver.init(context)
        }

        mSettingMap.putAll(Saver.load_main())
        APP_PREFERENCES_THEME = mSettingMap.getValue("theme")


        group_name = param1.toString()
        href = param2.toString()

        mProgress = view.findViewById(R.id.progress_group)
        mRvGroup = view.findViewById(R.id.RvGroup)

        if (context != null){
            Log.e("GroupBind", "param1: $param1 param2: $param2")
            groupPresenter.loadGroup(context = context, href = param2.toString(), name = param1.toString())
        }

        mAdapter = GroupAdapter(this)

        mRvGroup.adapter = mAdapter
        mRvGroup.layoutManager = LinearLayoutManager(
                activity,
                OrientationHelper.VERTICAL,
                false
        )

        val mAuth: FirebaseUser? =  FirebaseAuth.getInstance().currentUser
        val mail: String
        mail = mAuth?.email?.toString() ?: ""


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment group.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                group().apply {
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

    override fun setupList(groupList: ArrayList<GroupModel>) {
        mRvGroup.visibility = View.VISIBLE
        mAdapter.setupList(groupList = groupList)
    }

    override fun onItemClick(item: GroupModel, position: Int, button: Int) {
        val fr = fragmentManager?.beginTransaction()
        when(button){
            1 -> {
                val array: Array<String> = arrayOf(group_name, item.name_1, "$href${item.href_1}", "stud", "from_group")
                parrent.pushFragments(tag = parrent.TAB_STUDENTS, fragment = TimeTableFragment.newInstance(array), shouldAdd = true, fTrans = fr!!)
            }
            2 -> {
                val array: Array<String> = arrayOf(group_name, item.name_2, "$href${item.href_2}", "stud", "from_group")
                parrent.pushFragments(tag = parrent.TAB_STUDENTS, fragment = TimeTableFragment.newInstance(array), shouldAdd = true, fTrans = fr!!)
            }
            3 -> {
                val array: Array<String> = arrayOf(group_name, item.name_3, "$href${item.href_3}", "stud", "from_group")
                parrent.pushFragments(tag = parrent.TAB_STUDENTS, fragment = TimeTableFragment.newInstance(array), shouldAdd = true, fTrans = fr!!)
            }
        }
    }
}