package com.example.iiitb.vts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class TicketUseCasesScreen extends ActionBarActivity {

    private Button RegisterCode;
    private Button MakeTransaction;
    private Button ViewTransaction;

    SessionManager session;
    private ProgressDialog pDialog;
    String content = new String();
    String username = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_use_cases_screen);
        session = new SessionManager(getApplicationContext());

        RegisterCode = (Button) findViewById(R.id.RegisterBarCode);
        MakeTransaction = (Button) findViewById(R.id.MakeTransaction);
        ViewTransaction = (Button) findViewById(R.id.PassengerTransactions);




        if (savedInstanceState == null) {
            if(getIntent().getExtras()!=null)
            {
                content = getIntent().getExtras().getString("Content");
                username = getIntent().getExtras().getString("username");
            }
        }
        else
        {
            System.out.println("lsdmfbgvcdddddddddddddddddddddccccccccc");

            content = (String) savedInstanceState.getSerializable("Content");
            username = (String) savedInstanceState.getSerializable("username");
        }
        content=session.pref.getString("barcode", null);
        final Button RegisterBarcode = (Button) findViewById(R.id.RegisterBarCode);
        RegisterBarcode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create an explicit Intent for starting the LoginScreen
                // Activity
                Intent RegisterCode = new Intent(TicketUseCasesScreen.this,
                        RegisteringBarcode.class);
                System.out.print("In Usercases: ");
                System.out.println(content);
                RegisterCode.putExtra("content",content);
                System.out.print("In usecases : ");
                System.out.println(username);
                RegisterCode.putExtra("username",username);
                // Use the Intent to start the LoginScreen Activity
                startActivity(RegisterCode);

            }

        });

        final Button MakeTransaction = (Button) findViewById(R.id.MakeTransaction);
        MakeTransaction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create an explicit Intent for starting the LoginScreen
                // Activity
                Intent NewTransaction = new Intent(TicketUseCasesScreen.this,
                        MakingTransactionScreen.class);
                NewTransaction.putExtra("content",content);
                NewTransaction.putExtra("username",username);
                // Use the Intent to start the LoginScreen Activity
                startActivity(NewTransaction);

            }

        });

        final Button Recharge = (Button) findViewById(R.id.RechargeCard);
        Recharge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create an explicit Intent for starting the LoginScreen
                // Activity
                Intent RechargeIntent = new Intent(TicketUseCasesScreen.this,
                        RechargeScreen.class);
                RechargeIntent.putExtra("content",content);
                RechargeIntent.putExtra("username",username);
                // Use the Intent to start the LoginScreen Activity
                startActivity(RechargeIntent);

            }

        });

        final Button RecentTransaction = (Button) findViewById(R.id.PassengerTransactions);
        RecentTransaction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create an explicit Intent for starting the LoginScreen
                // Activity
                Intent RecentIntent = new Intent(TicketUseCasesScreen.this,
                        PassengerTransaction.class);
                RecentIntent.putExtra("content",content);
                RecentIntent.putExtra("username",username);
                // Use the Intent to start the LoginScreen Activity
                startActivity(RecentIntent);

            }

        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ticket_use_cases_screen, menu);
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
        Intent logoutIntent = new Intent(TicketUseCasesScreen.this,
                MainScreen.class);

        finish();
        session.logoutUser();
        startActivity(logoutIntent);
    }

}
