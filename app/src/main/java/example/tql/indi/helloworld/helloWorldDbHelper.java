package example.tql.indi.helloworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class helloWorldDbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "hello.db";
    public static final String TB_RATE = "rate";
    private SQLiteDatabase db = null;

    public helloWorldDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public helloWorldDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table "+TB_RATE+"(id int primary key autoincrement, name varchar(8), rating varchar(10))";
        sqLiteDatabase.execSQL(sql);
        Log.i("debugDatabase","creating database.....");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("debugDatabase","updating database.....");
    }


    private SQLiteDatabase getDb() {
        if(db == null) {
            db = getWritableDatabase();
        }
        return db;
    }

    public void closeDb() {
        if(db != null)
            db.close();
        db = null;
    }

    private boolean hasTable(String tableName) {
        SQLiteDatabase wdb = getDb();
        Cursor cur = wdb.query("sqlite_master",new String []{"name"}, "name=?",
                new String[]{tableName},null,null,null);
        if(cur.getColumnCount() >0)
            return true;
        else
            return false;
    }

    public boolean deleteAll(String tableName) {
        if(hasTable(tableName)) {
            SQLiteDatabase wdb = getDb();
            wdb.execSQL("delete from"+tableName);
            wdb.execSQL("update sqlite_sequence SET seq = 0 where name ='"+tableName+"'");
            return true;
        }
        return false;
    }

    public boolean insert(String tableName, DbVal val) {
        if(tableName.equals(val.getTableName()) && hasTable(tableName)) {
            SQLiteDatabase wdb = getDb();
            wdb.insert(tableName, null ,val.toValues());
            return false;
        }
        return true;
    }

    public List<?> selectAll(String tableName) {
         if(hasTable(tableName)) {
             if(tableName.equals(TB_RATE)) {
                 List<RateDbVal> rate = new ArrayList<RateDbVal>();
                 SQLiteDatabase wdb = getDb();
                 Cursor cur = wdb.query(tableName, new String[]{"name","rating"}, null, null,
                         null,null,null);
                 if(cur == null) {
                     while(cur.moveToNext()) {
                         RateDbVal val = new RateDbVal();
                         val.setRating(cur.getString(cur.getColumnIndex("rating")));
                         val.setName(cur.getString(cur.getColumnIndex("name")));
                         rate.add(val);
                     }
                 }
                 cur.close();
                 return rate;
             }
         }
        return null;
    }
}
