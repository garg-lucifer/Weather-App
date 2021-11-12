package com.namangarg.weather_api_app;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    private static final String STRING_URL = "https://www.metaweather.com/api/location/search/?query=";
    private static final String STRING_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";
    private String id = "";
    private Context context;

    public WeatherDataService(Context context){
        this.context = context;
    }

    public interface VolleyResponseListener{
        void onError(String message);
        void onResponse(String cityID);
    }

    public void getCityID(String cityName, final VolleyResponseListener volleyResponseListener){
        String url = STRING_URL + cityName;
        // Request a string response fro the provided url
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    id = jsonObject.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                volleyResponseListener.onResponse(id);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error !! ", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something wrong.");
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }


    public interface VolleyResponseListenerWeatherById{
        void onError(String message);
        void onResponse(List<WeatherReportModel> cityID);
    }

    public void getCityForecastByID(String cityID, final VolleyResponseListenerWeatherById volleyResponseListenerWeatherById){
        String url = STRING_WEATHER_BY_ID + cityID;
        final List<WeatherReportModel> weatherReportModelList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("consolidated_weather");
                    for (int i = 0; i <6; i++){
                        WeatherReportModel weatherReportModel = new WeatherReportModel(jsonArray.getJSONObject(i).getInt("id"),
                                jsonArray.getJSONObject(i).getInt("air_pressure"), jsonArray.getJSONObject(i).getInt("humidity"),
                                jsonArray.getJSONObject(i).getInt("predictability"),jsonArray.getJSONObject(i).getString("weather_state_name"),
                                jsonArray.getJSONObject(i).getString("weather_state_abbr"), jsonArray.getJSONObject(i).getString("wind_direction_compass"),
                                jsonArray.getJSONObject(i).getString("created"), jsonArray.getJSONObject(i).getString("applicable_date"),
                                jsonArray.getJSONObject(i).getLong("min_temp"), jsonArray.getJSONObject(i).getLong("max_temp"),
                                jsonArray.getJSONObject(i).getLong("the_temp"), jsonArray.getJSONObject(i).getLong("wind_speed"),
                                jsonArray.getJSONObject(i).getLong("wind_direction"), jsonArray.getJSONObject(i).getLong("visibility"));
                        weatherReportModelList.add(weatherReportModel);
                    }
                    volleyResponseListenerWeatherById.onResponse(weatherReportModelList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListenerWeatherById.onError("Error");
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public void getCityForecastByName(String cityName, final VolleyResponseListenerWeatherById volleyResponseListenerWeatherById){
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                volleyResponseListenerWeatherById.onError(message);
            }

            @Override
            public void onResponse(String cityID) {
                getCityForecastByID(cityID, new VolleyResponseListenerWeatherById() {
                    @Override
                    public void onError(String message) {
                        volleyResponseListenerWeatherById.onError(message);
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> cityID) {
                            volleyResponseListenerWeatherById.onResponse(cityID);
                    }
                });
            }
        });
    }
}
