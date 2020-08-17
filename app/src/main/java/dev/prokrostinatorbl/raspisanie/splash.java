package dev.prokrostinatorbl.raspisanie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_STARTFRAME;
    public static String APP_PREFERENCES_START_UNI;
    public static String APP_PREFERENCES_START_GROUP;
    public static String APP_PREFERENCES_LINK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Saved.init(getApplicationContext());
        new Saved().load_splash();



        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
//                starter();

                if(APP_PREFERENCES_STARTFRAME.equals("main")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("back", "false");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), FUCKTABLE.class);
                    intent.putExtra("link", APP_PREFERENCES_LINK);
                    intent.putExtra("instit", APP_PREFERENCES_START_UNI);
                    intent.putExtra("key", APP_PREFERENCES_START_GROUP);
                    startActivity(intent);

                }

            }
        }, SPLASH_DISPLAY_LENGTH);


    }

    public void starter() {

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("back", "false");
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, auth.class);
            startActivity(intent);
        }

    }
}


