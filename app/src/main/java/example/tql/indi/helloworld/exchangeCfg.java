package example.tql.indi.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class exchangeCfg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_cfg);

        Intent intent = this.getIntent();
        Bundle bld = intent.getBundleExtra("exchanges");
        ((TextView) findViewById(R.id.cfgDollar)).setText(bld.getString("dollar"));
        ((TextView) findViewById(R.id.cfgWon)).setText(bld.getString("won"));
        ((TextView) findViewById(R.id.cfgEuro)).setText(bld.getString("euro"));
    }

    public void save(View btn) {
        Bundle bld = new Bundle();
        bld.putString("dollar",((TextView) findViewById(R.id.cfgDollar)).getText().toString());
        bld.putString("won",((TextView) findViewById(R.id.cfgWon)).getText().toString());
        bld.putString("euro",((TextView) findViewById(R.id.cfgEuro)).getText().toString());
        Intent intent = this.getIntent();
        intent.putExtra("exchangesRet", bld);
        setResult(2, intent);
        finish();
    }


}
