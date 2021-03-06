package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlayerDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME_PLAYERS = "player";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_NAME="name";
    public static final String COLUMN_NAME_SEX="sex";

    public static final String COLUMN_NAME_COUNT = "number";
    public static final String COLUMN_NAME_GRADE = "grade";


    public PlayerDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


       String CREATE_BOOK = "create table book ("

                + "id integer primary key autoincrement, "

                + "author text, "

                + "price real, "

                + "pages integer, "

                + "name text)";

        String sql = "CREATE TABLE " + TABLE_NAME_PLAYERS + "(" + COLUMN_NAME_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME_COUNT + " TEXT NOT NULL DEFAULT\"\","
                + COLUMN_NAME_SEX + " TEXT NOT NULL DEFAULT\"\","
                + COLUMN_NAME_NAME + " TEXT NOT NULL DEFAULT\"\","
                + COLUMN_NAME_GRADE + " TEXT NOT NULL DEFAULT\"\"" + ")";

       // db.execSQL(sql);
        db.execSQL(CREATE_BOOK);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
