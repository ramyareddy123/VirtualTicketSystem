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


public class UserDetailsScreen extends ActionBarActivity {

    SessionManager session;
    private ProgressDialog pDialog;
    public static final String KEY_USERNAME = "username";
    JSONParser jsonParser = new JSONParser();
    private static final String DETAILS_URL = "http://vts.herobo.com/details.php";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_screen);
        String firstname = new String();
        String lastname = new String();
        String username = new String();
        String balance= new String();

        session = new SessionManager(getApplicationContext());

        if (savedInstanceState == null) {
            if(getIntent().getExtras()!=null) {
                firstname = getIntent().getExtras().getString("firstname");
                lastname = getIntent().getExtras().getString("lastname");
                username = getIntent().getExtras().getString("username");
                balance = getIntent().getExtras().getString("balance");
            }
            else
            {
                new UserDetails().execute();
            }
        }
        else {
            firstname= (String) savedInstanceState.getSerializable("firstname");
            lastname= (String) savedInstanceState.getSerializable("lastname");
            username= (String) savedInstanceState.getSerializable("username");
            balance= (String) savedInstanceState.getSerializable("balance");
        }

        final TextView fname = (TextView)findViewById(R.id.FirstNameValue);
        final TextView lname = (TextView)findViewById(R.id.LastNameValue);
        final TextView uname = (TextView) findViewById(R.id.UserNameValue);
        final TextView fbalance = (TextView)findViewById(R.id.BalanceValue);

        fname.setText(firstname);
        lname.setText(lastname);
        fbalance.setText(balance);
        uname.setText(username);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_details_screen, menu);
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
        Intent logoutIntent = new Intent(UserDetailsScreen.this,
                MainScreen.class);

        session.logoutUser();
        finish();
        startActivity(logoutIntent);
    }

    class UserDetails extends AsyncTask<String,JSONObject,JSONObject> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserDetailsScreen.this);
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

                //final TextView tname = (TextView)findViewById(R.id.Name);
                //final TextView tbalance = (TextView)findViewById(R.id.AmountBalance);
                //String u_name = json.getString("username");

                final TextView fname = (TextView)findViewById(R.id.FirstNameValue);
                final TextView lname = (TextView)findViewById(R.id.LastNameValue);
                final TextView uname = (TextView) findViewById(R.id.UserNameValue);
                final TextView fbalance = (TextView)findViewById(R.id.BalanceValue);

                String firstname = json.getString("fname");
                System.out.println(fname);
                String lastname = json.getString("lname");
                System.out.println(lname);

                int balance = json.getInt("balance");
                String str_bal = Integer.toString(balance);
                String username = json.getString("username");

                fname.setText(firstname);
                lname.setText(lastname);
                uname.setText(username);
                fbalance.setText(str_bal);
                pDialog.dismiss();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
