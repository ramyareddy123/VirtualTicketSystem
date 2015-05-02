package com.example.iiitb.vts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyTotalScreen extends ActionBarActivity {


    private TextView Mytotal;
    SessionManager session;
    private ProgressDialog pDialog;
    String name = new String();
    public static final String KEY_USERNAME = "username";
    JSONParser jsonParser = new JSONParser();
    private static final String TOTAL_URL = "http://vts.herobo.com/total.php";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_total_screen);
        session = new SessionManager(getApplicationContext());


        if (savedInstanceState == null) {
            if(getIntent().getExtras()!=null)
            {
                name = getIntent().getExtras().getString("name");
            }
            else
            {
                new Total().execute();
            }
        }
        else
        {
            name= (String) savedInstanceState.getSerializable("name");
        }
        name=session.pref.getString("username", null);
        String[] parts = name.split("@");
        name = parts[0];
        new Total().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_total_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
        }
        return true;
    }

    private void logout() {
        Intent logoutIntent = new Intent(MyTotalScreen.this,
                MainScreen.class);
        finish();
        session.logoutUser();
        startActivity(logoutIntent);
    }

    class Total extends AsyncTask<String,JSONObject,JSONObject> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyTotalScreen.this);
            pDialog.setMessage("loading....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args) {
            int success;
            JSONObject json;
            //String uname = session.pref.getString(KEY_USERNAME, null);
            //System.out.println(uname);
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", name));
                json = jsonParser.makeHttpRequest(
                        TOTAL_URL, "POST", params);
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    return json;
                }
                else
                {
                    return json;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            try {
                final TextView amount = (TextView)findViewById(R.id.TotalAmount);
                String TotalAmount = json.getString("total");
                System.out.print("dfgds");
                System.out.println(TotalAmount);
                amount.setText(TotalAmount);

                pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pDialog.dismiss();

        }
    }
}
