package example.tql.indi.helloworld;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Button chkButton = (Button) findViewById(R.id.button);
        chkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText ans = (EditText) findViewById(R.id.ans);
                String ansStr = ans.getText().toString();
                TextView resp = (TextView) findViewById(R.id.resp);
                if ("World!".equals(ansStr) || "world!".equals(ansStr)) {
                    resp.setText("Bingo!");
                    resp.setTextColor(Color.GREEN);
                    resp.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                else {
                    resp.setText("Try again!");
                    resp.setTextColor(Color.RED);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.pointCount) {
            Intent intent = new Intent(this, pointCount.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
