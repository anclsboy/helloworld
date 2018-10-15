package example.tql.indi.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ExClick extends AppCompatActivity {

    private double exchange = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_click);

        Intent intent = this.getIntent();
        Bundle bld = intent.getBundleExtra("exchange");
        ((TextView) findViewById(R.id.exName)).setText(bld.getString("name"));
        exchange = Double.parseDouble(bld.getString("exchange"));
        ((EditText) findViewById(R.id.listMoney)).addTextChangedListener(editclick);
    }


    private TextWatcher editclick = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable editable) {
            double money = Double.parseDouble(((TextView) findViewById(R.id.listMoney)).getText().toString());
            money = money* 100 / exchange;
            ((TextView) findViewById(R.id.listToMoney)).setText(String.valueOf((double)Math.round(money*100)/100 ));
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    };
}
