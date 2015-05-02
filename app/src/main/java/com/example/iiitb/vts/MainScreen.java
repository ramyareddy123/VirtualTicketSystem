package com.example.iiitb.vts;
import java.util.ArrayList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import java.net.URL;
import java.net.HttpURLConnection;
import android.view.View;
import java.net.MalformedURLException;
import java.io.IOException;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.iiitb.vts.R;
import com.example.iiitb.vts.LoginScreen;

public class MainScreen extends Activity implements OnClickListener{
    private EditText user, pass;
    private Button bLogin;    // Progress Dialog
    private ProgressDialog pDialog;
    SessionManager session;
    int status=0;

    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://vts.herobo.com/login.php";
    public static final String KEY_USERNAME = "username";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        user = (EditText)findViewById(R.id.Username);
        pass = (EditText)findViewById(R.id.Password);
        bLogin = (Button)findViewById(R.id.LoginButton);
        bLogin.setOnClickListener(this);

        session = new SessionManager(getApplicationContext());

        final TextView NewUser = (TextView) findViewById(R.id.Signup);
        NewUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create an explicit Intent for starting the LoginScreen
                // Activity
                Intent loginIntent = new Intent(MainScreen.this,
                        NewUserScreen.class);
                // Use the Intent to start the LoginScreen Activity
                startActivity(loginIntent);

            }

        });

        final TextView forgotPassword = (TextView) findViewById(R.id.ForgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create an explicit Intent for starting the LoginScreen
                // Activity
                Intent forgotPasswordIntent = new Intent(MainScreen.this,
                        ForgotPasswordScreen.class);
                // Use the Intent to start the LoginScreen Activity
                startActivity(forgotPasswordIntent);

            }

        });
    }

    protected void onResume() {
        //boolean flag =session.checkLogin();
        //session.checkLogin();
        //System.out.println(session.pref.getString(KEY_USERNAME, null));
        //System.out.println(session.isUserLoggedIn());
        if (session.isUserLoggedIn() == true)
        {

            if(session.pref.getString(KEY_USERNAME, null).endsWith("@vts.com")) {
                Intent i = new Intent(this, com.example.iiitb.vts.TicketScreen.class);
                startActivity(i);
            }
            else
            {
                Intent i = new Intent(this, com.example.iiitb.vts.LoginScreen.class);
                startActivity(i);
            }
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.LoginButton:
                new AttemptLogin().execute();
                // here we have used, switch case, because on login activity you may //also want to show registration button, so if the user is new ! we can go the //registration activity , other than this we could also do this without switch //case.
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    class AttemptLogin extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainScreen.this);
            pDialog.setMessage("Attempting for login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // here Check for success tag
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();


            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // checking  log for json response
                Log.d("Login attempt", json.toString());

                // success tag for json
                success = json.getInt(TAG_SUCCESS);
                if (success == 1)
                {

                    session.createUserLoginSession(username,password);
                    Log.d("Successfully Login!", json.toString());
                    String name = json.getString("fname") + " " + json.getString("lname");
                    System.out.println(name);
                    int balance = json.getInt("balance");
                    String barcode = json.getString("barcode");
                    System.out.print("In Main Screen : ");
                    System.out.println(barcode);
                    session.addNewBarcodeToSession(barcode);
                    String str_bal = Integer.toString(balance);

                        Intent ii = new Intent(getApplicationContext(), LoginScreen.class).putExtra("name", name);
                        ii.putExtra("balance", str_bal);
                        ii.putExtra("content",barcode);
                        ii.putExtra("username",username);
                        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ii);
                        finish();
                        return json.getString(TAG_MESSAGE);

                }
                else if(success == 2)
                {
                    session.createUserLoginSession(username,password);
                    Log.d("Successfully Login!", json.toString());
                    String name = json.getString("name");
                    System.out.println(name);

                    Intent ii = new Intent(getApplicationContext(), TicketScreen.class).putExtra("name", name);
                    ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(ii);
                    finish();
                    return json.getString(TAG_MESSAGE);
                }
                else
                {

                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        /**
         * Once the background process is done we need to  Dismiss the progress dialog asap
         * **/
        protected void onPostExecute(String message) {

            pDialog.dismiss();
            if (message != null){
                Toast.makeText(MainScreen.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}