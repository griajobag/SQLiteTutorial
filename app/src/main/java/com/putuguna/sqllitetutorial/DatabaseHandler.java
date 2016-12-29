package com.putuguna.sqllitetutorial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by putuguna on 03/11/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database_profile";
    private static final String TABLE_PROFILE = "myprofile";

    private static final String PROFILE_ID = "id";
    private static final String PROFILE_NAME = "name";
    private static final String PROFILE_PHONE_NUMBER = "phone_number";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_PROFILE + "(" +
                PROFILE_ID + " INTEGER PRIMARY KEY, " + PROFILE_NAME + " TEXT, " +
                PROFILE_PHONE_NUMBER + " TEXT " + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        onCreate(sqLiteDatabase);
    }


    /**
     * this method used to add contact item
     * @param profile
     */
    public void addProfile(ProfileModel profile){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROFILE_NAME, profile.getName());
        values.put(PROFILE_PHONE_NUMBER, profile.getPhoneNumber());

        db.insert(TABLE_PROFILE, null, values);
        db.close();
    }

    /**
     * this method used to get all data contact and store to list
     * @return
     */
    public List<ProfileModel> getAllDataProfile(){
        List<ProfileModel> profileList  = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PROFILE;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do {
                ProfileModel profileModel = new ProfileModel();
                profileModel.setId(Integer.parseInt(cursor.getString(0)));
                profileModel.setName(cursor.getString(1));
                profileModel.setPhoneNumber(cursor.getString(2));

                profileList.add(profileModel);

            }while (cursor.moveToNext());
        }
        return profileList;
    }


    /**
     * this method used to delete item of contact
     * @param value
     */
    public void deleteRow(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PROFILE + " WHERE "+ PROFILE_ID +"='"+value+"'");
        db.close();
    }

    /**
     * this method used to update the data
     * @param id
     * @param name
     * @param noPhone
     */

    public void updatedetails(int id, String name, String noPhone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(PROFILE_ID, id);
        args.put(PROFILE_NAME, name);
        args.put(PROFILE_PHONE_NUMBER, noPhone);
        db.update(TABLE_PROFILE, args, PROFILE_ID + "=" + id, null);
        db.close();
    }
}
