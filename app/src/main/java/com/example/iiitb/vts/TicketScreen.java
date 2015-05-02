package com.example.iiitb.vts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iiitb.vts.google.zxing.integration.android.IntentIntegrator;
import com.example.iiitb.vts.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TicketScreen extends ActionBarActivity implements View.OnClickListener {

    private Button scanButton;
    private TextView formatText, contentText;
    String name = new String();
    SessionManager session;
    private ProgressDialog pDialog;
    public static final String KEY_USERNAME = "username";
    JSONParser jsonParser = new JSONParser();
    private static final String DETAILS_URL = "http://vts.herobo.com/collector_details.php";
    private static final String TAG_SUCCESS = "success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_screen);
        session = new SessionManager(getApplicationContext());

        scanButton = (Button) findViewById(R.id.ScanTicket);
        formatText = (TextView)findViewById(R.id.scan_format);
        contentText = (TextView)findViewById(R.id.scan_content);

        //name = (TextView) findViewById(R.id.CollectorName);


        if (savedInstanceState == null) {
            if(getIntent().getExtras()!=null)
            {
                name = getIntent().getExtras().getString("name");
                System.out.print("Insideee : ");
                System.out.println(name);
            }
            else
            {
                new UserDetails().execute();
            }
        }
        else
        {
            name= (String) savedInstanceState.getSerializable("name");
        }

        final TextView tname = (TextView)findViewById(R.id.CollectorName);
        tname.setText(name);
        scanButton.setOnClickListener(this);

        final Button Total_Amount = (Button) findViewById(R.id.TodayTotal);
        Total_Amount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create an explicit Intent for starting the LoginScreen
                // Activity
                Intent TotalIntent = new Intent(TicketScreen.this,
                        MyTotalScreen.class);
                TotalIntent.putExtra("name",name);
                // Use the Intent to start the LoginScreen Activity
                startActivity(TotalIntent);

            }

        });


    }

    public void onClick(View v)
    {
        if(v.getId()==R.id.ScanTicket)
        {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null)
        {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            if(scanContent != null) {
                Intent MoveonIntent = new Intent(TicketScreen.this, TicketUseCasesScreen.class);
                // Use the Intent to start the LoginScreen Activity
                session.addNewBarcodeToSession(scanContent);

                MoveonIntent.putExtra("Format", scanFormat);
                MoveonIntent.putExtra("Content", scanContent);
                System.out.print("Actual: ");
                System.out.println(name);
                MoveonIntent.putExtra("username", name);
                startActivity(MoveonIntent);
            /*formatText.setText("FORMAT: " + scanFormat);
            contentText.setText("CONTENT: " + scanContent);*/
            }
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ticket_screen, menu);
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
        Intent logoutIntent = new Intent(TicketScreen.this,
                MainScreen.class);

        finish();
        session.logoutUser();
        startActivity(logoutIntent);
    }

    class UserDetails extends AsyncTask<String,JSONObject,JSONObject> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TicketScreen.this);
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

                final TextView tname = (TextView)findViewById(R.id.CollectorName);
                String name = json.getString("name");
                System.out.println(name);
                tname.setText(name);
                pDialog.dismiss();
             } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    /*
    class UserDetails extends AsyncTask<String,JSONObject,JSONObject> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TicketScreen.this);
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
/*
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

        }*/
    //}
}
