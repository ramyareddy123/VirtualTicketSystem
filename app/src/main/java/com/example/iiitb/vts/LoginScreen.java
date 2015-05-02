package com.example.iiitb.vts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.iiitb.vts.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginScreen extends ActionBarActivity {

    SessionManager session;
    private ProgressDialog pDialog;
    public static final String KEY_USERNAME = "username";
    JSONParser jsonParser = new JSONParser();
    private static final String DETAILS_URL = "http://vts.herobo.com/details.php";
    private static final String TAG_SUCCESS = "success";

    String content = new String();
    String username = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = new String();
        String balance= new String();
        setContentView(R.layout.activity_login_screen);
        session = new SessionManager(getApplicationContext());

        final TextView Profile = (TextView) findViewById(R.id.Profile);
        Profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create an explicit Intent for starting the LoginScreen
                // Activity
                Intent ProfileIntent = new Intent(LoginScreen.this,
                        UserDetailsScreen.class);
                // Use the Intent to start the LoginScreen Activity
                startActivity(ProfileIntent);

            }

        });

        if (savedInstanceState == null) {
            if(getIntent().getExtras()!=null) {
                name = getIntent().getExtras().getString("name");
                balance = getIntent().getExtras().getString("balance");
                content = getIntent().getExtras().getString("content");
                username = getIntent().getExtras().getString("username");
            }
            else
            {
                new UserDetails().execute();
            }
        }
        else {
            name = (String) savedInstanceState.getSerializable("name");
            balance = (String) savedInstanceState.getSerializable("balance");
            content = (String) savedInstanceState.getSerializable("content");
            username = (String) savedInstanceState.getSerializable("username");
        }


        final Button Transaction = (Button) findViewById(R.id.MyTransactions);
        Transaction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create an explicit Intent for starting the LoginScreen
                // Activity
                Intent TransactionIntent = new Intent(LoginScreen.this,
                        PassengerTransaction.class);
                System.out.print("IN LOGIN SCREEN : ");
                System.out.println(content);
                TransactionIntent.putExtra("content",content);
                TransactionIntent.putExtra("username",username);
                // Use the Intent to start the LoginScreen Activity
                startActivity(TransactionIntent);

            }

        });

        final TextView tname = (TextView)findViewById(R.id.Name);
        final TextView tbalance = (TextView)findViewById(R.id.AmountBalance);
        tname.setText(name);
        tbalance.setText(balance);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_screen, menu);
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
        Intent logoutIntent = new Intent(LoginScreen.this,
                MainScreen.class);
        finish();
        session.logoutUser();
        startActivity(logoutIntent);
    }

    class UserDetails extends AsyncTask<String,JSONObject,JSONObject> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginScreen.this);
            pDialog.setMessage("loading....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected JSONObject doInBackground(String... args) {
            int success;
            JSONObject json;
            String uname = session.pref.getString(KEY_USERNAME, null);
            System.out.println(uname);
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", uname));
                json = jsonParser.makeHttpRequest(
                        DETAILS_URL, "POST", params);
                success = json.getInt(TAG_SUCCESS);
                if (success == 1)
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

                final TextView tname = (TextView)findViewById(R.id.Name);
                final TextView tbalance = (TextView)findViewById(R.id.AmountBalance);
                //String u_name = json.getString("username");

                String name = json.getString("fname") + " " + json.getString("lname");
                System.out.println(name);
                int balance = json.getInt("balance");
                String str_bal = Integer.toString(balance);
                tname.setText(name);
                tbalance.setText(str_bal);
                pDialog.dismiss();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}

