package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ashish on 10/7/2017.
 */

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    public static ArrayList<Earthquake> fetchEarthquakeData(String urlString) {

        Log.e(LOG_TAG, "fetchEarthquakeData");
        String jsonResponse = "";

        if(TextUtils.isEmpty(urlString)) return null;

        URL url = createUrl(urlString);
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractEarthquakesFromJson(jsonResponse);
    }

    private static ArrayList<Earthquake> extractEarthquakesFromJson (String jsonResponse) {
        if(TextUtils.isEmpty(jsonResponse)) return null;

        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {
            JSONObject network = new JSONObject(jsonResponse);
            JSONArray features = network.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");
                long timeInMilliSeconds = (long) properties.getLong("time");
                double magnitude = properties.getDouble("mag");
//                Log.e("Ashish", properties.getString("place"));
                earthquakes.add(new Earthquake(magnitude, properties.getString("place"), timeInMilliSeconds, properties.getString("url")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return earthquakes;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        if(stringUrl != null) {
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error creating URL "+e);
            }
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if(url == null) return jsonResponse;

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error Response Code "+ httpURLConnection.getResponseCode()+" recieved from server");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(httpURLConnection != null) httpURLConnection.disconnect();

            if(inputStream != null) inputStream.close();
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if(inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
