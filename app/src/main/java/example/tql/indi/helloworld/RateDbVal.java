package example.tql.indi.helloworld;

import android.content.ContentValues;

public class RateDbVal implements DbVal {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ContentValues toValues() {
        ContentValues val = new ContentValues();
        val.put("name", name);
        val.put("rating", rating);
        return val;
    }

    public String getTableName() {
        return helloWorldDbHelper.TB_RATE;
    }

    private String name;
    private String rating;

}
