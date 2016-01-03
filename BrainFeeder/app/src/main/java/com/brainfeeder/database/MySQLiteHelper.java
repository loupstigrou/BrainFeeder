
package com.brainfeeder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.brainfeeder.user.User;

import java.util.ArrayList;
import java.util.List;


public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AppDatabase";
    private static final String KEY_USER_ID = "remoteIdUser";

    private SQLiteDatabase _database;
    private User _currentUser;

    public MySQLiteHelper(Context context, User user) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _currentUser = user;
    }

    public void open() throws SQLException {
        _database = getWritableDatabase();
    }

    public void close() {
        _database.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL("DROP TABLE IF EXISTS friends");

        String CREATE_FRIENDS_TABLE = "CREATE TABLE friends ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_USER_ID+" TEXT, " +
                "name TEXT, " +
                "score INT )";
        db.execSQL(CREATE_FRIENDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS friends");

        // create fresh books table
        this.onCreate(db);
    }


    public String remoteUserClause() {
        return KEY_USER_ID+" = "+_currentUser.getRemoteId();
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) friend
     */

    private static final String TABLE_FRIENDS= "friends";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";

    private static final String[] COLUMNS = {KEY_ID, KEY_NAME, KEY_SCORE};

    public Friend addFriend(Friend friend) {

        Log.d("addFriend", friend.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, _currentUser.getRemoteId());
        values.put(KEY_NAME, friend.name);
        values.put(KEY_SCORE, friend.score);

        long insertId = db.insert(TABLE_FRIENDS,
                null, //nullColumnHack
                values);
        friend.id = (int) insertId;
        db.close();
        return friend;
    }

    public Friend getFriend(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(TABLE_FRIENDS, // a. table
                        COLUMNS, // b. column names
                        " id = ? AND "+remoteUserClause(), // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        Friend friend = new Friend();
        friend.id = (Integer.parseInt(cursor.getString(0)));
        friend.name = (cursor.getString(1));
        friend.score = (cursor.getInt(2));

        Log.d("getFriend(" + id + ")", friend.toString());

        return friend;
    }

    public List<Friend> getAllFriends() {
        List<Friend> friends = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_FRIENDS + " WHERE "+remoteUserClause();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Friend friend = null;
        if (cursor.moveToFirst()) {
            do {
                friend = new Friend();
                friend.id = (Integer.parseInt(cursor.getString(0)));
                friend.name = (cursor.getString(1));
                friend.score = (cursor.getInt(2));

                friends.add(friend);
            } while (cursor.moveToNext());
        }

        Log.d("getAllFriends()", friends.toString());

        return friends;
    }

    public int updateFriend(Friend friend) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", friend.name);
        values.put("score", friend.score);

        int i = db.update(TABLE_FRIENDS, //table
                values, // column/value
                KEY_ID + " = ? AND "+remoteUserClause(), // selections
                new String[]{String.valueOf(friend.id)}); //selection args
        db.close();
        return i;
    }

    public void deleteFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIENDS,
                KEY_ID + " = ? AND "+remoteUserClause(),
                new String[]{String.valueOf(friend.id)});
        db.close();
        Log.d("deleteFriend", friend.toString());
    }

    public void deleteAllFriends() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FRIENDS + " WHERE "+remoteUserClause()+";");
    }
}