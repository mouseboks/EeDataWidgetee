package com.definedoutcomes.eedatawidget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    Button btnGetEeData;  // This is a reference to the "Get Repos" button.
    TextView tvEePhoneData;  // This will reference our repo list text box.
    TextView tvEeMifiData;
    TextView tvRefreshDate;
    RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.

    String url = "http://ee-monitor.definedoutcomes.com:5002/eedata";  // This is the API base URL (GitHub API)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.btnGetEeData = (Button) findViewById(R.id.btn_get_ee_data);  // Link our clicky button.
        this.tvEePhoneData = (TextView) findViewById(R.id.tv_phoneData);  // Link our repository list text output box.
        this.tvEeMifiData = (TextView) findViewById(R.id.tv_mifiData);
        this.tvRefreshDate = (TextView) findViewById(R.id.tv_refreshDate);
        //this.tvRepoList.setMovementMethod(new ScrollingMovementMethod());  // This makes our text box scrollable, for those big GitHub contributors with lots of repos :)

        requestQueue = Volley.newRequestQueue(this);  // This setups up a new request queue which we will need to make HTTP requests.

    }

    private void updateEeDataView(String phoneData, String mifiData, String refreshTime) {
        this.tvEePhoneData.setText(phoneData);
        this.tvEeMifiData.setText(mifiData);
        this.tvRefreshDate.setText(refreshTime);
    }

    private void getEeUsageData() {


        // Next, we create a new JsonArrayRequest. This will use Volley to make a HTTP request
        // that expects a JSON Array Response.
        // To fully understand this, I'd recommend reading the office docs: https://developer.android.com/training/volley/index.html
        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                        // Check the length of our response
                            JSONObject jsonObj = response.getJSONObject(0);
                            String phoneData = jsonObj.get("phone_data_remaining").toString();
                            String mifiData = jsonObj.get("mifi_data_remaining").toString();
                            String refreshTime = jsonObj.get("time").toString();
                                    //String lastUpdated = jsonObj.get("updated_at").toString();
                            updateEeDataView(phoneData + " of 38", mifiData + " of 64", refreshTime);
                                } catch (JSONException e) {
                                    //// If there is an error then output this to the logs.
                                    Log.e("Volley", "Invalid JSON Object.");
                                    //this.tvRefreshDate.setText(refreshTime);
                                }



                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        //updateEeDataView("Error while calling REST API");
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
