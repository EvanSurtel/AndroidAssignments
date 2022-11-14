package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WeatherForecast extends AppCompatActivity {
    ProgressBar pg;
    ImageView img;
    TextView minView,maxView,currentView;
    private String min, max, current, iconName, fileName, picUrl, city = "Ottawa";
    private Bitmap weatherPic;
    Spinner chooseCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        minView = (TextView)findViewById(R.id.minTempTextView);
        maxView = (TextView)findViewById(R.id.maxTempTextView);
        currentView = (TextView)findViewById(R.id.currentWeatherTextView);
        img = (ImageView)findViewById(R.id.weatherImageView);
        chooseCity = (Spinner)findViewById(R.id.citySpinner);

        pg = (ProgressBar)findViewById(R.id.progressBar);
        pg.setVisibility(View.VISIBLE);

        ForecastQuery forecast = new ForecastQuery();
        forecast.execute();
    }



    private class ForecastQuery extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + ",ca&APPID=2fd5625e2ef3d52678cf723ccf2c3ae7&mode=xml&units=metric");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream in = conn.getInputStream();

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(in, null);
                    while((parser.getEventType()) != XmlPullParser.END_DOCUMENT){
                        if(parser.getEventType() == XmlPullParser.START_TAG){
                            if(parser.getName().equals("temperature")) {
                                current = parser.getAttributeValue(null, "value");
                                publishProgress(25);
                                min = parser.getAttributeValue(null, "min");
                                publishProgress(50);
                                max = parser.getAttributeValue(null, "max");
                                publishProgress(75);

                            }
                            else if (parser.getName().equals("weather")){
                                iconName = parser.getAttributeValue(null,"icon");
                                fileName = iconName + ".png";
                                picUrl = "https://openweathermap.org/img/w/" + fileName;

                                if (fileExistance(fileName)){
                                    FileInputStream fileInputS = null;
                                    try{
                                        fileInputS = openFileInput(fileName);
                                    }
                                    catch (FileNotFoundException e){
                                        e.printStackTrace();
                                    }
                                    Log.i("WeatherForecast","File found locally");
                                    weatherPic = BitmapFactory.decodeStream(fileInputS);
                                }
                                else{
                                    weatherPic  = getImage(new URL(picUrl));
                                    FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                                    weatherPic.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                    Log.i("WeatherForecast","File downloaded from the internet");
                                }
                                publishProgress(100);
                            }
                        }
                        parser.next();
                    }
                } finally {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "";
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            pg.setVisibility(View.VISIBLE);
            pg.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String a){
            pg.setVisibility(View.INVISIBLE);
            img.setImageBitmap(weatherPic);
            currentView.setText(current + "C\u00b0");
            minView.setText(min + "C\u00b0");
            maxView.setText(max + "C\u00b0");
        }



    }



}

//using json
//package com.example.androidassignments;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.DownloadManager;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.DecimalFormat;
//
//public class WeatherForecast extends AppCompatActivity {
//
//    ProgressBar wthBar;
//    TextView weather;
//    private final String url = "api.openweathermap.org/data/2.5/weather";
//    private final String apikey = "2fd5625e2ef3d52678cf723ccf2c3ae7";
//    DecimalFormat df = new DecimalFormat("#,##");
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_weather_forecast);
//
//        //wthBar = findViewById(R.id.wthBar);
////        wthBar.setVisibility(View.VISIBLE);
//        weather = findViewById(R.id.weather);
//
//
//        String tempUrl = "http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=2fd5625e2ef3d52678cf723ccf2c3ae7";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                //Log.d("response", response);
//                String output = "";
//                try {
//                    JSONObject jsonResponse = new JSONObject(response);
//                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
//                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
//                    double min = jsonObjectMain.getDouble("temp_min") - 273.15;
//                    double max = jsonObjectMain.getDouble("temp_max") - 273.15;
//                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
//                    String countryName = jsonObjectSys.getString("country");
//                    String cityName = jsonResponse.getString("name");
//
//                    output += "Current weather of " + cityName + " (" + countryName + ")"
//                            + "\n Temp: " + df.format(temp) + " °C"
//                            + "\n Min Temp: " + df.format(min) + " °C"
//                            + "\n Max Temp: " + df.format(max) + " °C";
//
//                    weather.setText(output);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.add(stringRequest);
//
//
//
//
//    }
//
//
////    public void getWeatherDetails(View view) {
////        String tempUrl = "?q=ottawa,ca&APPID=2fd5625e2ef3d52678cf723ccf2c3ae7";
////
////
////
////    }
//
////    abstract class ForecastQuery extends AsyncTask<String,Integer,String> {
////        String min, max, current;
////        Bitmap bMap;
////
////
////    }
//}