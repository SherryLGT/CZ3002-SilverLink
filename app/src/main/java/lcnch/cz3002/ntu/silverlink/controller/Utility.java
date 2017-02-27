package lcnch.cz3002.ntu.silverlink.controller;

import android.app.ProgressDialog;
import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 17/02/2017
 */

public class Utility {
    // Web service url
    final static String API_URL = "https://lcnch-silverlink.azurewebsites.net/";

    public static String accessToken = "";

    // Get request from api (getURL), returns json result
    public static String getRequest(String getURL) {
        try {
            URL url = new URL(API_URL + getURL.replace(" ","%20"));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                urlConnection.setRequestProperty("Authorization ", "Bearer " + accessToken);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                return response.toString();
            } finally {
                urlConnection.disconnect();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }
    // Post request with json parameter (para) to api (postURL), returns json result
    public static String postRequest(String postURL, String para) {
        try {
            URL url = new URL(API_URL + postURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                DataOutputStream dStream = new DataOutputStream(out);
                dStream.writeBytes(para);
                dStream.flush();
                dStream.close();

                InputStream inputStream;
                int status = urlConnection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK)
                    inputStream = urlConnection.getErrorStream();
                else
                    inputStream = urlConnection.getInputStream();

                InputStream in = new BufferedInputStream(inputStream);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                return response.toString();
            } finally {
                urlConnection.disconnect();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ProgressDialog SetupLoadingDialog(Context context, ProgressDialog dialog) {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        return dialog;
    }
}
