package com.example.iiitb.vts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iiitb.vts.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForgotPasswordScreen extends Activity {
    private static final String FORGOT_PASSWORD_URL = "http://vts.herobo.com/forgotpassword.php";
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);
        final TextView forgotPassword = (TextView) findViewById(R.id.ResetButton);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new SendPassword().execute();
                Intent forgotPasswordIntent = new Intent(ForgotPasswordScreen.this,
                        MainScreen.class);
                startActivity(forgotPasswordIntent);
                Toast.makeText(ForgotPasswordScreen.this, "Password Reset E-mail is sent to your E-mail address", Toast.LENGTH_SHORT).show();

            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password_screen, menu);
        return true;
    }


    class SendPassword extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgotPasswordScreen.this);
            pDialog.setMessage("Sending....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            final TextView user = (TextView)findViewById(R.id.EmailAddress);
            String username = user.getText().toString();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            JSONObject json = jsonParser.makeHttpRequest(
            FORGOT_PASSWORD_URL, "POST", params);
            return null;
        }

        protected void onPostExecute(String message) {

            pDialog.dismiss();
            //if (message != null){
            //    Toast.makeText(ForgotPasswordScreen.this, "Password Reset E-mail is sent to your E-mail address", Toast.LENGTH_SHORT).show();
            //}
        }
    }

}
