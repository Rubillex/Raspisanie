//package dev.prokrostinatorbl.raspisanie;
//
//import android.content.res.Configuration;
//import android.os.Build;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class Themer extends AppCompatActivity {
//
//    String from = "null";
//    public static String APP_PREFERENCES_THEME; // выбранная тема
//
//    public  void pick(){
//
//        String theme = null;
//
//        int currentNightMode = getResources().getConfiguration().uiMode
//                & Configuration.UI_MODE_NIGHT_MASK;
//
//        Saved.init(getApplicationContext());
//        new Saved().load_setting();
//
//        switch(APP_PREFERENCES_THEME){
//            case "white":
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                    theme = "white_status";
//                } else {
//                    theme =  "white";
//                }
//                break;
//            case "black":
//                theme = "black";
//                break;
//            case "pink":
//                break;
//            case "auto":
//                switch (currentNightMode) {
//                    case Configuration.UI_MODE_NIGHT_NO:
//                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                            theme = "white_status";
//                        } else {
//                            theme =  "white";
//                        }
//                        break;
//                    case Configuration.UI_MODE_NIGHT_YES:
//                        theme = "black";
//                        break;
//                    default:
//                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                            theme = "white_status";
//                        } else {
//                            theme =  "white";
//                        }
//                        break;
//                }
//                break;
//        }
//
//        switch (from){
//            case "favorite":
//                Favorite.THEME = theme;
//                break;
//
//
//        }
//
//    }
//
//}
