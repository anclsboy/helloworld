package example.tql.indi.helloworld;

import android.content.ContentValues;

interface DbVal {
    public ContentValues toValues();
    public String getTableName() ;
}
