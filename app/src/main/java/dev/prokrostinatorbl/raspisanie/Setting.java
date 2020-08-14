package dev.prokrostinatorbl.raspisanie;

import android.app.Activity;

import android.app.Dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;



import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;


public class Setting extends AppCompatActivity {


    BillingClient   mBillingClient;
    Activity _this;

    List<SkuDetails>skuListData;


    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема
    public static String APP_PREFERENCES_PREMIUM;

    private Map<String, SkuDetails> mSkuDetailsMap = new HashMap<>();
    private final static String mSkuId = "premium";

    private final static String GPLAY_LICENSE = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlHaWVObSvicGziWi06Rp1o9mRVR/ATXGDuJDif1Z6qw00fM/WgyMRwh/JOtoJYAdQbcN49Xa0Pb5lUWtoqTYFvYlROjU9D74OVLIcSJT+PROooM3F62TJrnGOI4dBeBqcwag5aEYT0e6mqCIBOVj2kZnr0XAstOcrQYZDbeDvfj3vqFCem5xJ7LDr1nm6KJbcIsuvarxkQMo1uu5pi0RVsDARL+zBNAhkqVqlP0sIi0hovuvn+5sG9qpfvX6pHTE3hWwkMHBpGEOM1DPmT+nTgfPvQhqNCmtQ7f3BhMuCmaWDC27IFMcGz0YcnEfOQHuZPWYu43T3XcugAlRaMlBXwIDAQAB";

    SharedPreferences mSettings;

    RadioGroup radioTheme;

    private Toolbar toolbar;
    Dialog theme_swith;

    Dialog donate_dialog;
    ClipboardManager clipboardManager;
    ClipData clipData;

    BlurView blurView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;



        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if(mSettings.contains(APP_PREFERENCES_THEME)) {

            String mCounter = mSettings.getString(APP_PREFERENCES_THEME, "auto");

            if(!mCounter.equals("auto") && !mCounter.equals("white") && !mCounter.equals("black")){
                mCounter = "auto";
            }

            switch(mCounter){
                case "white":
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        setTheme(R.style.Light_statusbar);
                    } else {
                        setTheme(R.style.Light);
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                    break;
                case "black":
                    setTheme(R.style.Dark);
                    break;
                case "pink":
                    break;
                case "auto":
                    switch (currentNightMode) {
                        case Configuration.UI_MODE_NIGHT_NO:
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                setTheme(R.style.Light_statusbar);
                            } else {
                                setTheme(R.style.Light);
                                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                            }
                            break;
                        case Configuration.UI_MODE_NIGHT_YES:
                            setTheme(R.style.Dark);
                            break;
                        default:
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                setTheme(R.style.Light_statusbar);
                            } else {
                                setTheme(R.style.Light);
                                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                            }
                            break;
                        // We don't know what mode we're in, assume notnight
                    }
                    break;
            }
        }


        setContentView(R.layout.setting);


        //---------------------------------------------------------------------------------------

        openConnection();
        _this=this;

        //---------------------------------------------------------------------------------------

        String versionName = BuildConfig.VERSION_NAME;

        TextView version = (TextView)findViewById(R.id.info_sub);
        version.setText(versionName);

        //---------------------------------------------------------------------------------------

        theme_swith = new Dialog(this);
        theme_swith.requestWindowFeature(Window.FEATURE_NO_TITLE);
        theme_swith.setContentView(R.layout.theme_swich);
        theme_swith.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //---------------------------------------------------------------------------------------

        TextView current_theme = (TextView)findViewById(R.id.current_theme);

        toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText("Настройки");

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("back", "true");
                startActivity(intent);
            }
        });

        RadioButton whiteRadioButton = (RadioButton)theme_swith.findViewById(R.id.radio_white);
        whiteRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton blackRadioButton = (RadioButton)theme_swith.findViewById(R.id.radio_black);
        blackRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton autoRadioButton = (RadioButton)theme_swith.findViewById(R.id.radio_auto);
        autoRadioButton.setOnClickListener(radioButtonClickListener);

        //---------------------------------------------------------------------------------------

        RelativeLayout them = (RelativeLayout) findViewById(R.id.Theme_swicher);
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme_swith.show();

                blurView.setVisibility(View.VISIBLE);
            }
        });

        RelativeLayout info = (RelativeLayout)findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent app_info = new Intent(Setting.this, app_info.class);
                startActivity(app_info);
            }
        });


        RelativeLayout premium = (RelativeLayout) findViewById(R.id.premium);
        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    premium_cheker();
            }
        });

        //---------------------------------------------------------------------------------------

        String mPremium = mSettings.getString(APP_PREFERENCES_PREMIUM, "false");

        if (!mPremium.equals("true") && !mPremium.equals("false")){
            mPremium = "false";
        }

        TextView premium_subtext = (TextView) findViewById(R.id.premium_subtext);
        premium_subtext.setText(mPremium);

        //---------------------------------------------------------------------------------------


        //---------------------------------------------------------------------------------------

        if(mSettings.contains(APP_PREFERENCES_THEME)) {

            String mCounter = mSettings.getString(APP_PREFERENCES_THEME, "auto");



            if(!mCounter.equals("auto") && !mCounter.equals("white") && !mCounter.equals("black")){
                mCounter = "auto";
            }

            switch(mCounter){
                case "white":
                    Log.i("!!!!", mCounter);
                    whiteRadioButton.setChecked(true);
                    current_theme.setText("Текущая тема: светлая");
                    break;
                case "black":
                    Log.i("!!!!", mCounter);
                    blackRadioButton.setChecked(true);
                    current_theme.setText("Текущая тема: тёмная");
                    break;
                case "auto":
                    Log.i("!!!!", mCounter);
                    autoRadioButton.setChecked(true);
                    current_theme.setText("Текущая тема: авто");
                    break;
            }


        }



    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("back", "true");
        startActivity(intent);
    }

    public void premium_cheker(){

        String mPremium = mSettings.getString(APP_PREFERENCES_PREMIUM, "false");

        if (!mPremium.equals("true") && !mPremium.equals("false")){
            mPremium = "false";
        }

        Toast.makeText(this, "Состаяние премиум: " + mPremium, Toast.LENGTH_SHORT).show();
        TextView premium_subtext = (TextView) findViewById(R.id.premium_subtext);
        premium_subtext.setText(mPremium);

        donate_dialog = new Dialog(this);
        donate_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        donate_dialog.setContentView(R.layout.donate);
        donate_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        if(mPremium.equals("false")){
            donate_dialog.show();

            blurView.setVisibility(View.VISIBLE);

            clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

            donate_dialog.findViewById(R.id.vk).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //СОВЕРШЕНИЕ ПОКУПКИ
                    launchBilling(mSkuId);
                }
            });
        }

    }


    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSettings.edit();


            switch (rb.getId()) {
                case R.id.radio_white:
                    theme_swith.dismiss();
                    editor.putString(APP_PREFERENCES_THEME, "white");
                    break;
                case R.id.radio_black:
                    theme_swith.dismiss();
                    editor.putString(APP_PREFERENCES_THEME, "black");
                    break;
                case R.id.radio_auto:
                    theme_swith.dismiss();
                    editor.putString(APP_PREFERENCES_THEME, "auto");
                    break;

                default:
                    break;
            }
            editor.apply();
            recreate();








//              УСПЕШНАЯ ПОКУПКА
//            mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = mSettings.edit();
//            editor.putString(APP_PREFERENCES_PREMIUM, "true");
//            editor.apply();

        }
    };


    public void launchBilling(String skuId) {
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(mSkuDetailsMap.get(skuId))
                .build();
        mBillingClient.launchBillingFlow(this, billingFlowParams);
    }

    private void openConnection()
    {
        mBillingClient = BillingClient.newBuilder(this).setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {

                Log.i("dfdsf","gggggg "+billingResult.getResponseCode());
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && purchases != null) {

                                Log.i("ОПЛАТА", "ПРОШЛА");

                                mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putString(APP_PREFERENCES_PREMIUM, "true");
                                editor.apply();

                    String mPremium = mSettings.getString(APP_PREFERENCES_PREMIUM, "true");
//                    Toast.makeText(_this, mPremium, Toast.LENGTH_SHORT).show();

                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                    // Handle an error caused by a user cancelling the purchase flow.
                } else {
                    // Handle any other error codes.
                }
            }


        }).enablePendingPurchases()
                .build();

        mBillingClient.startConnection(new BillingClientStateListener() {


            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResponseCode) {
                Log.i("ResposneCode","ResposneCode "+billingResponseCode.getResponseCode());
                if (billingResponseCode.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    showList();

                    List<Purchase> purchasesList = queryPurchases(); //запрос о покупках

                    //если товар уже куплен, предоставить его полователю
                    for (int i = 0; i < purchasesList.size(); i++) {
                        String purchaseId = purchasesList.get(i).getSku();
                        if(TextUtils.equals(mSkuId, purchaseId)) {
                            mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putString(APP_PREFERENCES_PREMIUM, "true");
                            editor.apply();
                        }
                    }

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    private void showList()
    {


/**
 * To purchase an Subscription
 */

        final List<String> skuList = new ArrayList<>();

        skuList.add("premium"); // SKU Id

        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP)
                .build();
        mBillingClient.querySkuDetailsAsync(params,
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList)
                    {
                        Log.i("ResposneCode","ResposneCode11 "+billingResult.getResponseCode());
                        Log.i("ResposneCode","ResposneCode12 "+skuDetailsList);
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
//                            Log.i("ResposneCode","ResposneCode13 "+skuDetailsList.size());
//                            Product product;
//                            skuListData=skuDetailsList;
//                            subTypeList=new String[skuListData.size()];
//                            for (int k=0;k<skuDetailsList.size();k++)
//                                subTypeList[k]=(skuDetailsList.get(k).getTitle()+"\n Price "+skuDetailsList.get(k).getPrice()+"\n").replaceAll("(InAppSubscription)"," Sub ");
                            for (SkuDetails skuDetails : skuDetailsList) {
                                mSkuDetailsMap.put(skuDetails.getSku(), skuDetails);
                            }
                        }
                    }
                });


    }

    private List<Purchase> queryPurchases() {
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
        return purchasesResult.getPurchasesList();
    }


}