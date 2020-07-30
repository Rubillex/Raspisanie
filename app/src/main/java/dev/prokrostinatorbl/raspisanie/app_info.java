package dev.prokrostinatorbl.raspisanie;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.android.billingclient.api.BillingClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.List;

public class app_info extends AppCompatActivity {

    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема

    SharedPreferences mSettings;

    private Toolbar toolbar;

    Button logout;
    TextView donate;

    Dialog donate_dialog;

    ClipboardManager clipboardManager;
    ClipData clipData;


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
        setContentView(R.layout.app_info);

        donate_dialog = new Dialog(this);
        donate_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        donate_dialog.setContentView(R.layout.donate);
        donate_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText("О приложении");

        clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        donate_dialog.findViewById(R.id.donate_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", "4276020019417903");
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getApplicationContext(),"Text Copied ", Toast.LENGTH_SHORT).show();
            }
        });

        donate_dialog.findViewById(R.id.donate_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", "2200240879544613");
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getApplicationContext(),"Text Copied ", Toast.LENGTH_SHORT).show();
            }
        });



        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), auth.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.donate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donate_dialog.show();
            }
        });

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
//            name.setText(signInAccount.getDisplayName());
//            mail.setText(signInAccount.getEmail());
        }


    }
}