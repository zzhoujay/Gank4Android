package zhou.gank.io.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper;
import groovy.transform.CompileStatic
import zhou.gank.io.model.Bookmark
import zhou.gank.io.model.Gank

@CompileStatic
public class DatabaseManager {

    public static final String TABLE_NAME = "bookmark"
    public static final String URL = "url"
    public static final String TITLE = "title"
    public static final String TIME = "time"

    private static DatabaseManager databaseManager

    public static DatabaseManager getInstance() {
        return databaseManager
    }

    public static void init(Context context) {
        databaseManager = new DatabaseManager(context)
    }


    private DatabaseHelper databasehelper
    private SQLiteDatabase database

    private DatabaseManager(Context context) {
        databasehelper = new DatabaseHelper(context)
    }

    public void insert(Bookmark bookmark) {
        start()
        ContentValues cv = new ContentValues(3)
        cv.put(URL, bookmark.url)
        cv.put(TITLE, bookmark.title)
        cv.put(TIME, bookmark.time.getTime())
        database.insert(TABLE_NAME, null, cv)
        end()
    }

    public void delete(String url) {
        start()
        database.delete(TABLE_NAME, "url=?", url)
        end()
    }

    public List<Bookmark> select() {
        start()
        String sql = "select * from $TABLE_NAME order by $TIME desc;"
        Cursor cursor = database.rawQuery(sql)
        int count = cursor?.getCount()
        List<Bookmark> bookmarks = new ArrayList<>(count)
        if (cursor) {
            cursor.moveToFirst()
            for (int i = 0; i < count; i++) {
                String url = cursor.getString(0)
                String title = cursor.getString(1)
                long time = cursor.getLong(2)
                bookmarks.add(new Bookmark(url, title, new Date(time)))
                cursor.moveToNext()
            }
        }
        end()
        return bookmarks
    }

    public List<Gank> selectToGank() {
        start()
        String sql = "select * from $TABLE_NAME order by $TIME desc;"
        Cursor cursor = database.rawQuery(sql)
        int count = cursor?.getCount()
        List<Gank> gs = new ArrayList<>(count)
        if (cursor) {
            cursor.moveToFirst()
            for (int i = 0; i < count; i++) {
                String url = cursor.getString(0)
                String title = cursor.getString(1)
                long time = cursor.getLong(2)
                gs.add(new Gank(title, url, new Date(time)))
                cursor.moveToNext()
            }
        }
        end()
        return gs
    }

    public boolean isExist(String url) {
        start()
        String sql = "select * from $TABLE_NAME where $URL=?"
        Cursor cursor = database.rawQuery(sql, url)
        boolean flag = false
        if (cursor && cursor.getCount() > 0) {
            flag = true
        }
        end()
        return flag
    }


    private void start() {
        database = databasehelper.getWritableDatabase()
    }

    private end() {
        database?.close()
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        private static final int VERSION = 1
        private static final String DATABASE_NAME = "ganke"

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION)
        }

        @Override
        void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql = "create table $TABLE_NAME (${DatabaseManager.URL} text primary key,${TITLE} text not null,$TIME long not null);"
            println(sql)
            sqLiteDatabase.execSQL(sql)
        }

        @Override
        void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}