package forecast.iak.ihsan.forecast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forecast.iak.ihsan.forecast.adapter.WeatherAdapter;
import forecast.iak.ihsan.forecast.model.Weather;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private RecyclerView rvWeatherData;
    private ProgressDialog progressDialog;
    private WeatherAdapter weatherAdapter;
    private List<Weather> weatherList = new ArrayList<Weather>();
    private RecyclerView.LayoutManager layoutManager;
    private String strLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvWeatherData = (RecyclerView) findViewById(R.id.rv_weather_data);
        rvWeatherData.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        rvWeatherData.setLayoutManager(layoutManager);
        weatherAdapter = new WeatherAdapter(MainActivity.this, weatherList);
        rvWeatherData.setAdapter(weatherAdapter);
        new FetchWeatherData().execute();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        strLocation = sharedPreferences.getString("pref_location", "Bandung");
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        setTitle(strLocation);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        strLocation = sharedPreferences.getString(key, "Bandung");
        setTitle(strLocation);
    }


    public class FetchWeatherData extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String strJSONWeather = null;
            try {
                // memanggil url
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q="+ strLocation +"&APPID=2939b4f9a70e7dd25e181b06ab14bc5d&mode=json&units=metric&cnt=5");
                // membuat koneksi
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                }
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                    Log.d("cek buffer-->", line.toString());
                }
                if (stringBuffer.length() == 0) {
                    return null;
                } else {
                    strJSONWeather = stringBuffer.toString();
                    return strJSONWeather;
                }
            } catch (IOException e) {
                Log.e("Error response-->", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            fetchJSONWeather(s);
            Log.d("cek response", s);
        }

        private void fetchJSONWeather(String response) {
            try {
                JSONObject jsonObjectForecast = new JSONObject(response);
                JSONArray jsonArrayList = jsonObjectForecast.getJSONArray("list");

                for (int i = 0; i < jsonArrayList.length(); i++) {
                    JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                    Weather weather = new Weather();
                    weather.setDt(String.valueOf(convertDateTime(jsonObjectList.getLong("dt"))));
                    JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                    weather.setDay(String.valueOf(jsonObjectTemp.getInt("day")));
                    JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                    for (int j = 0; j < jsonArrayWeather.length(); j++) {
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(j);
                        weather.setMain(jsonObjectWeather.getString("main"));
                    }

                    weatherList.add(weather);
                }

                weatherAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e("Error Parsing JSON", e.toString());
            }
        }

        private String convertDateTime(long dateTime) {
            Date date = new Date(dateTime * 1000);
            Format dateTimeFormat = new SimpleDateFormat("EEE, dd MMM");
            return dateTimeFormat.format(date);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_setting) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
