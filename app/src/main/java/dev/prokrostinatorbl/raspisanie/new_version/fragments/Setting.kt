package dev.prokrostinatorbl.raspisanie.new_version.fragments

import android.app.Activity
import android.app.Dialog
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.android.billingclient.api.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.prokrostinatorbl.raspisanie.BuildConfig
import dev.prokrostinatorbl.raspisanie.R
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.*
import moxy.MvpAppCompatFragment
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Setting.newInstance] factory method to
 * create an instance of this fragment.
 */


    private val mSkuDetailsMap: MutableMap<String, SkuDetails> = HashMap<String, SkuDetails>()
    private val mSkuId: String = "premium"



class Setting : MvpAppCompatFragment() {

    private val sharedPrefs by lazy { context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    lateinit var APP_PREFERENCES_THEME: String
    lateinit var APP_PREFERENCES_PREMIUM: String

    lateinit var context_: Context

    lateinit var APP_PREFERENCES_STARTFRAME: String
    lateinit var APP_PREFERENCES_START_UNI: String
    lateinit var APP_PREFERENCES_START_GROUP: String
    lateinit var APP_PREFERENCES_LINK: String

    private var mSettingMap: MutableMap<String, String> = HashMap<String, String>()

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mBillingClient: BillingClient

    private lateinit var skuListData: List<SkuDetails>

    private lateinit var mAuth: FirebaseUser
    private lateinit var mail: String

    private lateinit var theme_swith: Dialog
    private lateinit var donate_dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.setting, container, false)

        val mAuth: FirebaseUser? =  FirebaseAuth.getInstance().currentUser
        mail = mAuth?.email ?: ""

        var context = context
        if (context != null) {
            Saver.init(context)
        }

        if (context != null) {
            context_ = context
        }

        mSettingMap.putAll(Saver.load_setting())

        APP_PREFERENCES_THEME = mSettingMap.getValue("theme")
        APP_PREFERENCES_PREMIUM = mSettingMap.getValue("premium")
        APP_PREFERENCES_LINK = mSettingMap.getValue("link")
        APP_PREFERENCES_STARTFRAME = mSettingMap.getValue("start_frame")
        APP_PREFERENCES_START_GROUP = mSettingMap.getValue("group")
        APP_PREFERENCES_START_UNI = mSettingMap.getValue("start_uni")




        if (context != null) {
            openConnection(context = context)
        }

        val versionName: String = BuildConfig.VERSION_NAME
        val version = view.findViewById<TextView>(R.id.info_sub)
        version.text = versionName

        //--------------------------------

        theme_swith = Dialog(context!!)
        theme_swith.requestWindowFeature(Window.FEATURE_NO_TITLE)
        theme_swith.setContentView(R.layout.theme_swich)
        theme_swith.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //--------------------------------

        val whiteRB: RadioButton = theme_swith.findViewById(R.id.radio_white)
        val blackRB: RadioButton = theme_swith.findViewById(R.id.radio_black)
        val autoRB: RadioButton = theme_swith.findViewById(R.id.radio_auto)

        whiteRB.setOnClickListener(radioButtonClickListener)
        blackRB.setOnClickListener(radioButtonClickListener)
        autoRB.setOnClickListener(radioButtonClickListener)

        //--------------------------------

        view.findViewById<RelativeLayout>(R.id.Theme_swicher).setOnClickListener { theme_swith.show() }
        view.findViewById<RelativeLayout>(R.id.premium).setOnClickListener { premium_cheker() }

        //--------------------------------

        view.findViewById<TextView>(R.id.premium_subtext).text = APP_PREFERENCES_PREMIUM

        if (APP_PREFERENCES_START_GROUP == "standart"){
            view.findViewById<TextView>(R.id.start_page_subtext).text = "Главное меню"
        } else {
            view.findViewById<TextView>(R.id.start_page_subtext).text = APP_PREFERENCES_START_GROUP
        }

        view.findViewById<RelativeLayout>(R.id.start_page).setOnClickListener {
            save()
            Saver.load_setting()
            if (APP_PREFERENCES_START_GROUP == "standart"){
                view.findViewById<TextView>(R.id.start_page_subtext).text = "Главное меню"
            } else {
                view.findViewById<TextView>(R.id.start_page_subtext).text = APP_PREFERENCES_START_GROUP
            }
        }

        //--------------------------------

        val current_theme = view.findViewById<TextView>(R.id.current_theme)


        when (getSavedTheme()) {
            THEME_LIGHT -> {
                whiteRB.isChecked = true
                current_theme?.text = "Текущая тема: светлая"
            }
            THEME_DARK -> {
                blackRB.isChecked = true
                current_theme?.text = "Текущая тема: тёмная"
            }
            THEME_SYSTEM -> {
                autoRB.isChecked = true
                current_theme?.text = "Текущая тема: авто"
            }
            THEME_UNDEFINED -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> current_theme?.text = "Текущая тема: светлая"
                    Configuration.UI_MODE_NIGHT_YES -> current_theme?.text = "Текущая тема: тёмная"
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> current_theme?.text = "Текущая тема: авто"
                }
            }
        }



        return view
    }

    var radioButtonClickListener = View.OnClickListener { v ->
        val rb = v as RadioButton
        when (rb.id) {
            R.id.radio_white -> {
                theme_swith.dismiss()
                setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
                APP_PREFERENCES_THEME = "white"
            }
            R.id.radio_black -> {
                theme_swith.dismiss()
                setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
                APP_PREFERENCES_THEME = "black"
            }
            R.id.radio_auto -> {
                theme_swith.dismiss()
                setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, THEME_SYSTEM)
                APP_PREFERENCES_THEME = "auto"
            }
            else -> {
            }
        }
        val current_theme = view?.findViewById<TextView>(R.id.current_theme)
        when (getSavedTheme()) {
            THEME_LIGHT -> {
                current_theme?.text = "Текущая тема: светлая"
            }
            THEME_DARK -> {
                current_theme?.text = "Текущая тема: тёмная"
            }
            THEME_SYSTEM -> {
                current_theme?.text = "Текущая тема: авто"
            }
            THEME_UNDEFINED -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> current_theme?.text = "Текущая тема: светлая"
                    Configuration.UI_MODE_NIGHT_YES -> current_theme?.text = "Текущая тема: тёмная"
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> current_theme?.text = "Текущая тема: авто"
                }
            }
        }
        save()
    }

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }

    private fun getSavedTheme() = sharedPrefs?.getInt(KEY_THEME, THEME_UNDEFINED)

    private fun saveTheme(theme: Int) = sharedPrefs?.edit()?.putInt(KEY_THEME, theme)?.apply()

    fun premium_cheker(){
        val premium_subtext = view?.findViewById<TextView>(R.id.premium_subtext)
        premium_subtext!!.text = APP_PREFERENCES_PREMIUM

        if (APP_PREFERENCES_PREMIUM == "false"){
            donate_dialog = Dialog(context_)
            donate_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            donate_dialog.setContentView(R.layout.donate)
            donate_dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            donate_dialog.show()

            donate_dialog.findViewById<LinearLayout>(R.id.vk).setOnClickListener {
                launchBilling(mSkuId)
            }

        }
    }

    fun save(){
        Saver.save_setting(
                theme = APP_PREFERENCES_THEME,
                premium = APP_PREFERENCES_PREMIUM,
                group = APP_PREFERENCES_START_GROUP,
                start_frame = APP_PREFERENCES_STARTFRAME,
                start_uni = APP_PREFERENCES_START_UNI,
                link = APP_PREFERENCES_LINK
        )
    }

    private fun openConnection(context: Context) {
        mBillingClient = BillingClient.newBuilder(context).setListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK
                    && purchases != null) {
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
                Log.i("ОПЛАТА", "ПРОШЛА")
                //                    Toast.makeText(_this, "ПОКУПКУ ПОДТВЕРДИЛ, МОЖЕШЬ СТАВИТЬ ПРИЛОЖЕНИЕ ИЗ GOOGLE PLAY", Toast.LENGTH_SHORT).show();
                APP_PREFERENCES_PREMIUM = "true"
                save()
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }.enablePendingPurchases()
                .build()
        mBillingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResponseCode: BillingResult) {
                if (billingResponseCode.responseCode == BillingClient.BillingResponseCode.OK) {
                    showList()
                    val purchasesList: List<Purchase> = queryPurchases() as List<Purchase> //запрос о покупках
                    for (purchase in purchasesList) {
                        handlePurchase(purchase)
                    }

                    //если товар уже куплен, предоставить его полователю
                    for (i in purchasesList.indices) {
                        val purchaseId = purchasesList[i].sku
                        if (TextUtils.equals(mSkuId, purchaseId)) {
                            APP_PREFERENCES_PREMIUM = "true"
                            save()
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    fun handlePurchase(purchase: Purchase) {
        val acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener { }
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener)
            }
        }
        val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
        val listener = ConsumeResponseListener { billingResult, purchaseToken ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // Handle the success of the consume operation.
                Log.i("ОПЛАТА", "ПРОШЛА")
                //                    Toast.makeText(_this, "ПОКУПКУ ПОДТВЕРДИЛ, МОЖЕШЬ СТАВИТЬ ПРИЛОЖЕНИЕ ИЗ GOOGLE PLAY", Toast.LENGTH_SHORT).show();
                APP_PREFERENCES_PREMIUM = "true"
                save()
                Saver.load_setting()
            }
        }
        mBillingClient.consumeAsync(consumeParams, listener)
    }

    private fun showList() {
        /**
         * To purchase an Subscription
         */
        val skuList: MutableList<String> = ArrayList()
        skuList.add("premium") // SKU Id
        val params = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP)
                .build()
        mBillingClient.querySkuDetailsAsync(params
        ) { billingResult, skuDetailsList ->
            Log.i("ResposneCode", "ResposneCode11 " + billingResult.responseCode)
            Log.i("ResposneCode", "ResposneCode12 $skuDetailsList")
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                for (skuDetails in skuDetailsList) {
                    mSkuDetailsMap[skuDetails.sku] = skuDetails
                }
            }
        }
    }

    private fun queryPurchases(): List<Purchase?>? {
        val purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP)
        return purchasesResult.purchasesList
    }

    fun launchBilling(skuId: String?) {
        val billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(mSkuDetailsMap[skuId]!!)
                .build()
        mBillingClient.launchBillingFlow(context_ as Activity, billingFlowParams)

        donate_dialog.dismiss()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Setting.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                Setting().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}