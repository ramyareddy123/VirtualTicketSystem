package com.example.iiitb.vts;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iiitb.vts.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewUserScreen extends Activity implements OnClickListener{

    private EditText firstname,lastname,username,password;
    private Button bSignup, bGoBack;    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    private static final String REGISTER_URL = "http://vts.herobo.com/register.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_screen);
        firstname = (EditText)findViewById(R.id.FirstName);
        lastname = (EditText)findViewById(R.id.LastName);
        username = (EditText)findViewById(R.id.UsernameNew);
        password = (EditText)findViewById(R.id.PasswordNew);
        bSignup = (Button)findViewById(R.id.SignupNew);
        bSignup.setOnClickListener(this);

        final Button Already_Login = (Button) findViewById(R.id.AlreadyRegistered);
        Already_Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create an explicit Intent for starting the LoginScreen
                // Activity
                Intent loginIntent = new Intent(NewUserScreen.this,
                        MainScreen.class);
                // Use the Intent to start the LoginScreen Activity
                startActivity(loginIntent);

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_user_screen, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.SignupNew:
                new AttemptRegister().execute(REGISTER_URL);
                // here we have used, switch case, because on login activity you may //also want to show registration button, so if the user is new ! we can go the //registration activity , other than this we could also do this without switch //case.
            default:
                break;
        }
    }

    class AttemptRegister extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewUserScreen.this);
            pDialog.setMessage("Registering...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args)
        {
            // TODO Auto-generated method stub
            // here Check for success tag
            int success;
            String fname = firstname.getText().toString();
            String lname = lastname.getText().toString();
            String uname = username.getText().toString();
            String pass = password.getText().toString();
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", uname));
                params.add(new BasicNameValuePair("password", pass));
                params.add(new BasicNameValuePair("firstname", fname));
                params.add(new BasicNameValuePair("lastname", lname));
                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);
                System.out.println("Came till here1");
                // checking  log for json response
                Log.d("Register attempt", json.toString());

                System.out.println("Came till here2");
                // success tag for json
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Successfully register!", json.toString());

                    Intent ii = new Intent(NewUserScreen.this, MainScreen.class);
                    finish();
                    // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
                    startActivity(ii);
                    return json.getString(TAG_MESSAGE);
                }else{

                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String message)
        {

            pDialog.dismiss();
            if (message != null){
                Toast.makeText(NewUserScreen.this, message, Toast.LENGTH_LONG).show();

            }
        }

    }
}
