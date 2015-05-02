package com.example.iiitb.vts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RegisteringBarcode extends ActionBarActivity {

    private Button registerButton;
    private EditText username;
    SessionManager session;
    private ProgressDialog pDialog;
    String content = new String();
    JSONParser jsonParser = new JSONParser();
    private static final String REGISTERCODE_URL = "http://vts.herobo.com/register_new_barcode.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering_barcode);

        session = new SessionManager(getApplicationContext());

        registerButton = (Button) findViewById(R.id.RegisterButton);
        username = (EditText)findViewById(R.id.user);



        if (savedInstanceState == null) {
            if(getIntent().getExtras()!=null)
            {
                content = getIntent().getExtras().getString("content");
                System.out.print("content   ");
                System.out.println(content);
            }
            else
            {
                System.out.println("xxdsdssssssssssssssssssssssssssssssxxxxxx");
                new RegisterCode().execute();
            }
        }
        else
        {
            System.out.println("ppppppppppppppppppppppdddddddddddddddddddddddddddddsssssss");

            content = (String) savedInstanceState.getSerializable("content");
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new RegisterCode().execute();
               // Toast.makeText(RegisteringBarcode.this, "Your Barcode has been successfully registered", Toast.LENGTH_SHORT).show();

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registering_barcode, menu);
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
        Intent logoutIntent = new Intent(RegisteringBarcode.this,
                MainScreen.class);

        finish();
        session.logoutUser();

        startActivity(logoutIntent);
    }

    class RegisterCode extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisteringBarcode.this);
            pDialog.setMessage("Sending....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            final TextView user = (TextView)findViewById(R.id.user);
            String username = user.getText().toString();
            try {
                System.out.print("Hereeeeeeeeee: ");
                System.out.println(content);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("barcode", content));
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTERCODE_URL, "POST", params);
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Successfully register!", json.toString());

                    Intent ii = new Intent(RegisteringBarcode.this, TicketUseCasesScreen.class);
                    ii.putExtra("content",content);
                    // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
                    startActivity(ii);
                    return json.getString(TAG_MESSAGE);
                } else {

                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {

            pDialog.dismiss();
            if (message != null){
                Toast.makeText(RegisteringBarcode.this, message, Toast.LENGTH_LONG).show();

            }
        }
    }
}
