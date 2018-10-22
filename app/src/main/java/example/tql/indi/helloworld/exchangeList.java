package example.tql.indi.helloworld;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class exchangeList extends ListActivity  {

    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, String>> listItems;
    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if(msg.what == 0){
               createAdapter();
           }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExchangesThread et = new ExchangesThread();
        listItems = new ArrayList<HashMap<String, String>>();
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        SharedPreferences sp = getSharedPreferences("exchangelist", MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences("exchangelist",MODE_PRIVATE).edit();
        String today = String.valueOf(cal.get(Calendar.YEAR)) +"-"+
                String.valueOf(cal.get(Calendar.MONTH) + 1) +"-" +
                String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        if(sp != null && sp.getString("dateLastOpen","1970-01-01").equals(today)) {
            //read from database
            helloWorldDbHelper dbHelper = new helloWorldDbHelper(this);
            List<RateDbVal> rate = (List<RateDbVal>) dbHelper.selectAll(helloWorldDbHelper.TB_RATE);
            listItems = new ArrayList<HashMap<String, String>>();
            for(RateDbVal r : rate) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", r.getName()); // 标题文字
                map.put("ItemDetail", r.getRating()); // 详情描述
                listItems.add(map);
            }
        } else {
            editor.putString("dateLastOpen", today);
            Thread thread = new Thread(et,"exchangelist");
            thread.start();
        }
        createAdapter();
        this.getListView().setOnItemLongClickListener(olclistener);
    }

    private void createAdapter() {
        adapter = new SimpleAdapter(this, listItems,
                R.layout.list_item,
                new String[] { "ItemTitle", "ItemDetail" },
                new int[] { R.id.itemTitle, R.id.itemDetail }
        );
        setListAdapter(adapter);
    }

    private OnItemLongClickListener olclistener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int j) {
                    listItems.remove(i);
                    adapter.notifyDataSetChanged();
                    getListView().invalidate();
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(exchangeList.this);
            builder.setTitle("Delete or Not");
            builder.setMessage("Confirm?");
            builder.setPositiveButton("yes",listener);
            builder.setNegativeButton("No",null);
            builder.show();
            return true;
        }
    };

    class ExchangesThread extends Thread {
        @Override
        public void run() {
            Document doc = null;
            helloWorldDbHelper dbHelper = new helloWorldDbHelper(exchangeList.this);
            RateDbVal val = new RateDbVal();
            try {
                doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/enindex.html").get();
                Elements tables = doc.getElementsByTag("table");
                Element tbody = null;
                for(Element table : tables){
                    if(table.attr("bgcolor").equals("#EAEAEA")){
                        tbody = table.child(0);
                        break;
                    }
                }
                for(Element tr : tbody.children()) {
                    if(tr.hasAttr("align")) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("ItemTitle", tr.child(0).text()); // 标题文字
                        map.put("ItemDetail", tr.child(5).text()); // 详情描述
                        val.setName(map.get("ItemTitle"));
                        val.setRating(map.get("ItemDetail"));
                        dbHelper.insert(helloWorldDbHelper.TB_RATE, val);
                        listItems.add(map);
                    }
                }
                Message msg = new Message();
                msg.what = 0;
                uiHandler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, ExClick.class);
        Bundle bld = new Bundle();
        bld.putString("name", String.valueOf(listItems.get(position).get("ItemTitle")));
        bld.putString("exchange", listItems.get(position).get("ItemDetail"));
        intent.putExtra("exchange",bld);
        startActivity(intent);
    }
}

