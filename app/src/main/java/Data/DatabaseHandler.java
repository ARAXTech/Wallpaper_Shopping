package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Model.ListItem;
import Util.util;

/**
 * Created by QHS on 15/07/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(Context context) {
        super(context, util.DATABASE_NAME, null, util.DARABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_GL_TABLE = "CREATE TABLE " + util.TABLE_NAME + "("
                + util.KEY_ID + " TEXT PRIMARY KEY, "
                + util.KEY_NAME + " TEXT, "
                + util.KEY_DESCRIPTION + " TEXT, "
                + util.KEY_IMG_SRC + " TEXT, "
                + util.KEY_FAVORITE + " TEXT DEFAULT 'false', "
                + util.KEY_NUM_LINK + " INTEGER DEFAULT 0, "
                + util.KEY_PRICE + " INTEGER DEFAULT 1, "
                + util.KEY_COUNT + " INTEGER DEFAULT 1 " +")";
        sqLiteDatabase.execSQL(CREATE_GL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Dropping is deleting Table!
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + util.TABLE_NAME);
        //CREATE TABLE AGAIN
        onCreate(sqLiteDatabase);
    }


    public void addListItem(ListItem listItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(util.KEY_ID, listItem.getId());
        value.put(util.KEY_NAME, listItem.getName());
        value.put(util.KEY_DESCRIPTION, listItem.getDescription());
        value.put(util.KEY_IMG_SRC, listItem.getImgLink());
        value.put(util.KEY_FAVORITE, listItem.getFavorite());
        value.put(util.KEY_NUM_LINK,listItem.getNum_link());
        value.put(util.KEY_PRICE,listItem.getPrice());
        value.put(util.KEY_COUNT,listItem.getCount());

        //Insert to row
        db.insert(util.TABLE_NAME, null, value);
        db.close();//Close db connection

    }

    //Get A contact
    public ListItem getListItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(util.TABLE_NAME, new String[]{util.KEY_ID,
                        util.KEY_NAME, util.KEY_DESCRIPTION, util.KEY_IMG_SRC, util.KEY_FAVORITE, util.KEY_NUM_LINK.toString(),
                        util.KEY_PRICE.toString(), util.KEY_COUNT.toString()}, util.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            ListItem item = new ListItem(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getInt(5),cursor.getInt(6),cursor.getInt(7));
            return item;
        }
        return null;
    }

    public List <ListItem> getAllListItem() {
        SQLiteDatabase db = this.getReadableDatabase();
        //  List<Contact> contactList = new ArrayList<>();
        List <ListItem> ItemList = new ArrayList <>();
        //select all contacts
        String selectAll = "SELECT * FROM " + util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);
        if (cursor.moveToFirst()) {
            do {
                ListItem item = new ListItem();
                item.setId(cursor.getString(0));
                Log.d("id in db", ""+item.getId());
                item.setName(cursor.getString(1));
                item.setDescription(cursor.getString(2));
                item.setImgLink(cursor.getString(3));
                item.setFavorite(cursor.getString(4));
                item.setNum_link(cursor.getInt(5));
                item.setPrice(cursor.getInt(6));
                item.setCount(cursor.getInt(7));
                //add contact object to our contact list
                ItemList.add(item);
                //  ListItem item=new ListItem(cursor.getString(1),cursor.getString(2));
                //ItemList.add(item);
            }
            while (cursor.moveToNext());
        }
        return ItemList;
    }

    // Get a Favorite
    public String get_favorite(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(util.TABLE_NAME, new String[]{util.KEY_ID,
                        util.KEY_NAME, util.KEY_DESCRIPTION, util.KEY_IMG_SRC, util.KEY_FAVORITE}, util.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();

        ListItem item = new ListItem(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)
                ,cursor.getInt(5),cursor.getInt(6),cursor.getInt(7));
        return item.getFavorite();
    }
    //Update contact

    public int updateListItem(ListItem listItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(util.KEY_NAME, listItem.getName());
        values.put(util.KEY_DESCRIPTION, listItem.getDescription());
        values.put(util.KEY_IMG_SRC, listItem.getImgLink());
        values.put(util.KEY_FAVORITE, listItem.getFavorite());
        values.put(String.valueOf(util.KEY_NUM_LINK), listItem.getNum_link());
        values.put(String.valueOf(util.KEY_PRICE),listItem.getPrice());
        values.put(String.valueOf(util.KEY_COUNT),listItem.getCount());
        //Update row
        return db.update(util.TABLE_NAME, values, util.KEY_ID + "=?", new String[]{String.valueOf(listItem.getId())});

    }

    // delete single contact
    public void deleteListItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(util.TABLE_NAME, util.KEY_ID + "=?",
                new String[]{id});
        db.close();
    }


//    public int getID(String name) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(util.TABLE_NAME, new String[]{util.KEY_ID,
//                        util.KEY_NAME, util.KEY_QUANTIFY}, util.KEY_NAME + "=?",
//                new String[]{name}, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2));
//        return contact.getId();
//
//    }

    //get contacts count
    public int getListItemCount() {
        String countQuery = "SELECT * FROM " + util.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        // int count=cursor.getCount();
        //  cursor.close();
        //return  count;
        return cursor.getCount();

    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + util.TABLE_NAME);
    }

    public Boolean Exists(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(util.TABLE_NAME, new String[]{util.KEY_ID,
//                        util.KEY_NAME, util.KEY_DESCRIPTION, util.KEY_FAVORITE}, util.KEY_ID + "=?",
//                new String[]{id}, null, null, null, null);
//        boolean exists = (cursor.getCount() > 0);
//        Log.d("exist1",String.valueOf(cursor.getCount()));
//        cursor.close();
//        return exists;
        Cursor cursor = null;
        String sql ="SELECT * FROM "+util.TABLE_NAME+" WHERE "+util.KEY_ID+"="+id;
        cursor= db.rawQuery(sql,null);
        Log.d("Cursor Count : " , String.valueOf(cursor.getCount()));
        cursor.close();
        if(cursor.getCount()>0){
            return true;
        }else{
            return false;
        }


    }
}


