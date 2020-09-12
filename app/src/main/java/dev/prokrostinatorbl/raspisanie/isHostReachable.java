package dev.prokrostinatorbl.raspisanie;

import android.os.StrictMode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class isHostReachable {

    static boolean isHostReachable(String address) {
        try {
            URL url = new URL(address);

            HttpURLConnection urlc =
                    (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("User-Agent", "userAgent");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1000 * 10);
            urlc.connect();
            if (urlc.getResponseCode() == 200) {
                urlc.disconnect();
                return true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
