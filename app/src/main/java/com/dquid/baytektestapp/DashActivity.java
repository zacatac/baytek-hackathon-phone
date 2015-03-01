package com.dquid.baytektestapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DashActivity extends Activity {
    static Context context;
    private Button addCreditsButton;
    private Button playButton;
    private TextView creditsAmt;
    private TextView userText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        initUI();
    }

    void initUI(){
        addCreditsButton = (Button) findViewById(R.id.addCreditsButton);
        playButton = (Button) findViewById(R.id.playButton);
        creditsAmt = (TextView) findViewById(R.id.creditsViewAmt);
        userText = (TextView) findViewById(R.id.userText);

        userText.setText("field.zackery@gmail.com");
        creditsAmt.setText("25");


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addCreditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
