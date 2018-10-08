package example.tql.indi.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class pointCount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_point_count);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_point, menu);
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

    public void addPoint(int id, int point) {
        TextView points = (TextView) findViewById(id);
        int current = Integer.parseInt(points.getText().toString());
        points.setText(String.valueOf(current + point));
    }

    public void addPoint(View btn){
        int id = btn.getId();
        switch(id){
            case R.id.a1:
                addPoint(R.id.pointA, 1);
                break;
            case R.id.a2:
                addPoint(R.id.pointA, 2);
                break;
            case R.id.a3:
                addPoint(R.id.pointA, 3);
                break;
            case R.id.b1:
                addPoint(R.id.pointB, 1);
                break;
            case R.id.b2:
                addPoint(R.id.pointB, 2);
                break;
            case R.id.b3:
                addPoint(R.id.pointB, 3);
                break;
        }
    }

    public void clearPoint(View btn) {
        TextView pointA = (TextView) findViewById(R.id.pointA);
        TextView pointB = (TextView) findViewById(R.id.pointB);
        pointA.setText("0");
        pointB.setText("0");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String scoreA = ((TextView)findViewById(R.id.pointA)).getText().toString();
        String scoreB = ((TextView)findViewById(R.id.pointB)).getText().toString();

        outState.putString("scoreA", scoreA);
        outState.putString("scoreB", scoreB);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ((TextView)findViewById(R.id.pointA))
                .setText(savedInstanceState.getString("scoreA"));
        ((TextView)findViewById(R.id.pointB))
                .setText(savedInstanceState.getString("scoreB"));
    }
}
