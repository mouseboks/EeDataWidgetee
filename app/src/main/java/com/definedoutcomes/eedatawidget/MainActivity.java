package com.definedoutcomes.eedatawidget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    Button btnGetEeData;
    TextView tvEePhoneData;
    TextView tvEeMifiData;
    TextView tvRefreshDate;
    RequestQueue requestQueue;

    String url = "http://ee-monitor.definedoutcomes.com:5002/eedata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.btnGetEeData = (Button) findViewById(R.id.btn_get_ee_data);
        this.tvEePhoneData = (TextView) findViewById(R.id.tv_phoneData);
        this.tvEeMifiData = (TextView) findViewById(R.id.tv_mifiData);
        this.tvRefreshDate = (TextView) findViewById(R.id.tv_refreshDate);

        requestQueue = Volley.newRequestQueue(this);
    }

    private void updateEeDataView(String phoneData, String mifiData, String refreshTime) {
        this.tvEePhoneData.setText(phoneData);
        this.tvEeMifiData.setText(mifiData);
        this.tvRefreshDate.setText(refreshTime);
    }

    private void getEeUsageData() {

        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObj = response.getJSONObject(0);
                            String phoneData = jsonObj.get("phone_data_remaining").toString();
                            String mifiData = jsonObj.get("mifi_data_remaining").toString();
                            String refreshTime = jsonObj.get("time").toString();
                            updateEeDataView(phoneData + " of 38", mifiData + " of 64", refreshTime);
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
        // Add the request we just defined to our request queue.
        // The request queue will automatically handle the request as soon as it can.
        requestQueue.add(arrReq);
    }

    public void getEeUsageData(View v) {
        getEeUsageData();
    }

}
