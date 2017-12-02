package com.definedoutcomes.eedatawidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EeUsageWidgetProvider extends AppWidgetProvider {

    private String url = "http://ee-monitor.definedoutcomes.com:5002/eedata";  // This is the API base URL (GitHub API)

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {

            ComponentName thisWidget = new ComponentName(context,
                    EeUsageWidgetProvider.class);

            for (int widgetId : appWidgetIds) {
                final int widgetIdToUse = widgetId;

                final RemoteViews remoteViews = new RemoteViews(context
                        .getPackageName(), R.layout.ee_usage_appwidget);

                // Get the data from the rest service
                RequestQueue queue = Volley.newRequestQueue(context);


                JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    Log.e("Volley", "Received response " + response.toString());
                                    // Check the length of our response
                                    JSONObject jsonObj = response.getJSONObject(0);
                                    String phoneData = jsonObj.get("phone_data_remaining").toString() + " GB";
                                    String phoneDays = "Phone " + jsonObj.get("phone_days_remaining").toString() + "d";

                                    String mifiData = jsonObj.get("mifi_data_remaining").toString() + " GB";
                                    String mifiDays = "MiFi " + jsonObj.get("mifi_days_remaining").toString() + "d";
                                    String refreshTime = jsonObj.get("time").toString();


                                    remoteViews.setTextViewText(R.id.tv_phone_data,
                                            phoneData);
                                    remoteViews.setTextViewText(R.id.tv_phone_days,
                                            phoneDays);
                                    remoteViews.setTextViewText(R.id.tv_mifi_data,
                                            mifiData);
                                    remoteViews.setTextViewText(R.id.tv_mifi_days,
                                            mifiDays);

                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                                    LocalDateTime dateTime = LocalDateTime.parse(refreshTime.substring(0, refreshTime.indexOf('.')), formatter);
                                    remoteViews.setTextViewText(R.id.tv_status_time,
                                                                dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                                        .replaceAll("T", " ")
                                    );

                                    appWidgetManager.updateAppWidget(widgetIdToUse, remoteViews);
                                } catch (JSONException e) {
                                    //// If there is an error then output this to the logs.
                                    Log.e("Volley", "Invalid JSON Object.");
                                }



                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley", error.toString());
                            }
                        }
                );

                queue.add(arrReq);

                // Register an onClickListener to allow refreshing the widget by clicking.
                Intent clickIntent = new Intent(context,
                        EeUsageWidgetProvider.class);

                clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                        appWidgetIds);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context, 0, clickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.tv_mifi_data, pendingIntent);
                remoteViews.setOnClickPendingIntent(R.id.tv_phone_data, pendingIntent);
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
}