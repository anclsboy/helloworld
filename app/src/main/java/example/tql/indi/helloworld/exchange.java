package example.tql.indi.helloworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class exchange extends AppCompatActivity implements Runnable {

    private double euro, dollar, won;
    private Thread getExchangePage = null;
    private long MillSecOfDay = 1000* 60* 60* 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_exchange);
        if(getExchangePage == null)
            getExchangePage = new Thread(this, "exchange");
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        long now = cal.getTimeInMillis();
        SharedPreferences sp = getSharedPreferences("exchange", MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences("exchange",MODE_PRIVATE).edit();
        if(sp == null || (now - sp.getLong("lastOpen",0)) / MillSecOfDay >= 1 ) {
            getExchangePage.start();
            editor.putFloat("won", (float) won);
            editor.putFloat("dollar", (float) dollar);
            editor.putFloat("euro", (float) euro);
        }
        else {
            euro = sp.getFloat("euro", 0);
            dollar = sp.getFloat("dollar", 0);
            won = sp.getFloat("won", 0);

        }
        editor.putLong("lastOpen", now);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exchange, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tohello) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bld = data.getBundleExtra("exchangesRet");
        if(requestCode == 1 && resultCode == 2) {
            euro = Double.parseDouble(bld.getString("euro"));
            won = Double.parseDouble(bld.getString("won"));
            dollar = Double.parseDouble(bld.getString("dollar"));
        }
    }



    public void toEuro(View btn) {
        TextView exchanged = (TextView) findViewById(R.id.exchanged);
        double rmb = Double.parseDouble(((TextView) findViewById(R.id.money)).getText().toString());
        exchanged.setText(String.valueOf(rmb / euro));
    }

    public void toDollar(View btn) {
        TextView exchanged = (TextView) findViewById(R.id   .exchanged);
        double rmb = Double.parseDouble(((TextView) findViewById(R.id.money)).getText().toString());
        exchanged.setText(String.valueOf(rmb / dollar));
    }

    public void toWon(View btn) {
        TextView exchanged = (TextView) findViewById(R.id.exchanged);
        double rmb = Double.parseDouble(((TextView) findViewById(R.id.money)).getText().toString());
        exchanged.setText(String.valueOf(rmb / won));
    }

    public void jump(View btn) {
        Intent intent = new Intent(this, exchangeCfg.class);
        Bundle bld = new Bundle();
        bld.putString("won", String.valueOf(won));
        bld.putString("dollar", String.valueOf(dollar));
        bld.putString("euro", String.valueOf(euro));
        intent.putExtra("exchanges",bld);
        startActivityForResult(intent, 1);
    }

    @Override
    public void run() {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/enindex.html").get();
            Element euroEle = doc.getElementsMatchingOwnText("EUR").first().parent();
            Element wonEle = doc.getElementsMatchingOwnText("KRW").first().parent();
            Element dlrEle = doc.getElementsMatchingOwnText("USD").first().parent();
            euro = Double.parseDouble(euroEle.child(4).text()) / 100;
            dollar = Double.parseDouble(dlrEle.child(4).text()) / 100;
            won = Double.parseDouble(wonEle.child(4).text()) / 100;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
