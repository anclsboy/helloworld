package example.tql.indi.helloworld;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BMI extends AppCompatActivity {

    private static String thin = "偏低";
    private static String normal = "正常";
    private static String hevay = "过重";
    private static String fat = "肥胖";
    private static String toofat = "非常胖";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bmi);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bmi, menu);
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


    public void calculateBMI(View btn) {
        try {
            TextView view  = findViewById(R.id.numHeight);
            double height = Double.parseDouble(view.getText().toString());
            view  = findViewById(R.id.numWeight);
            double weight = Double.parseDouble(view.getText().toString());
            double bmi = weight / (height * height);
            String res = "BMI:"; res += String.valueOf((double)Math.round(bmi*100)/100 );
            String callback;
            if(bmi < 18.5) callback = thin;
            else if(bmi < 24) callback = normal;
            else if(bmi < 28) callback = hevay;
            else if(bmi < 32) callback = fat;
            else callback = toofat;
            res += ", "+callback;
            view = findViewById(R.id.bmitips);
            view.setText(res);
        } catch (Exception e){
            Toast.makeText(this, "请输入内容",Toast.LENGTH_SHORT).show();
        }

    }



}
