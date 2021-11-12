package com.namangarg.weather_api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    Button btn_cityID, btn_getWeatherByID, getBtn_getWeatherByName;
    EditText et_datainput;
    ListView lv_weatherReport;
    WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_cityID = findViewById(R.id.btn_getCItyID);
        btn_getWeatherByID = findViewById(R.id.btn_getWeatherByCityID);
        getBtn_getWeatherByName = findViewById(R.id.getWeatherByCityName);
        et_datainput = findViewById(R.id.et_dataimput);
        lv_weatherReport = findViewById(R.id.lv_weatherReport);


        btn_cityID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weatherDataService.getCityID(et_datainput.getText().toString().trim(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Something Went Wrong! ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String cityID) {
                        Toast.makeText(MainActivity.this, "City ID = " + cityID, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btn_getWeatherByID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weatherDataService.getCityForecastByID(et_datainput.getText().toString().trim(), new WeatherDataService.VolleyResponseListenerWeatherById() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Something Went Wrong! ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> cityID) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, cityID);
                        lv_weatherReport.setAdapter(arrayAdapter);
                    }

                });
            }
        });

        getBtn_getWeatherByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weatherDataService.getCityForecastByName(et_datainput.getText().toString().trim(), new WeatherDataService.VolleyResponseListenerWeatherById() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Something Went Wrong! ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> cityID) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, cityID);
                        lv_weatherReport.setAdapter(arrayAdapter);
                    }

                });
            }
        });

    }
}