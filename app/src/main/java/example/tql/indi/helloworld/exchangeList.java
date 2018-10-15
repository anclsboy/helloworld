package example.tql.indi.helloworld;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.HashMap;

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
        Thread thread = new Thread(et,"exchangelist");
        thread.start();
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




