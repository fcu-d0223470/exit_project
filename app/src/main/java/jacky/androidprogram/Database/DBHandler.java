package jacky.androidprogram.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by Jacky on 7/13/2016.
 */
public class DBHandler extends SQLiteOpenHelper{

    // Logcat tag
    private static final String LOG = DBHandler.class.getName();
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "DataManager";

    // Table Names
    private static final String TABLE_HAVE = "Have";
    private static final String TABLE_STORY = "Story";
    private static final String TABLE_TASK = "Task";
    private static final String TABLE_USER = "User";
    private static final String TABLE_PHOTO = "Photo";

    // Common column names
    //private static final String KEY_ACCOUNT = "username";
    // HAVE Table - column names
    private static final String KEY_HAVE_ID = "have_id";
    private static final String KEY_HAVE_NUMBER = "have_number";
    private static final String KEY_HAVE_STORY_PART = "have_story_part";
    private static final String KEY_HAVE_FLAG = "have_flag";
    private static final String KEY_HAVE_STORY_FLAG = "have_story_flag";
    private static final String KEY_HAVE_TASK_FLAG = "have_task_flag";
    private static final String KEY_HAVE_DATE = "have_date";
    // STORY Table - column names
    private static final String KEY_STORY_SERIAL = "story_serial_number";
    private static final String KEY_STORY_NUMBER = "story_number";
    private static final String KEY_STORY_NAME = "story_name";
    private static final String KEY_STORY_ADDRESS = "story_address";
    private static final String KEY_STORY_LONGITUDE = "story_longitude";
    private static final String KEY_STORY_LATITUDE = "story_latitude";
    private static final String KEY_STORY_PICTURE = "story_picture";
    private static final String KEY_STORY_PICTURE_SD = "story_picture_sd";
    private static final String KEY_STORY_DATA = "story_data";
    private static final String KEY_STORY_PART = "story_part";
    private static final String KEY_STORY_SHOW = "story_show";
    private static final String KEY_STORY_DATE = "story_date";
    // TASK Table - column names
    private static final String KEY_TASK_NUMBER = "task_number";
    private static final String KEY_TASK_NAME = "task_name";
    private static final String KEY_TASK_BLOCK = "task_block";
    private static final String KEY_TASK_TYPE = "task_type";
    private static final String KEY_TASK_PICTURE = "task_picture";
    private static final String KEY_TASK_PICTURE_SD = "task_picture_sd";
    private static final String KEY_TASK_INTRODUCTION = "task_introduction";
    private static final String KEY_TASK_PART = "task_part";
    private static final String KEY_TASK_HOT = "task_hot";
    private static final String KEY_TASK_DEVELOPER = "task_developer";
    private static final String KEY_TASK_SHOW = "task_show";
    private static final String KEY_TASK_DATE = "task_date";
    // USER Table - column names
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ACCOUNT = "username";
    private static final String KEY_USER_PASS = "userpass";
    private static final String KEY_USER_FNAME = "fname";
    private static final String KEY_USER_LNAME = "lname";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PHONE = "phone";
    private static final String KEY_USER_SEX = "sex";
    private static final String KEY_USER_ADDRESS = "address";
    private static final String KEY_USER_PHONE_ID = "user_phone_id";
    private static final String KEY_USER_CREATEDATE = "createdate";
    // Photo Table - column names
    private static final String KEY_PHOTO_SERIAL = "photo_serial";
    private static final String KEY_PHOTO_DIR = "photo_dir";
    // Table Create Statements
    // STORY table create statement
    private static final String CREATE_TABLE_HAVE =
            "CREATE TABLE " + TABLE_HAVE + "("
                    + KEY_HAVE_ID + " TEXT,"
                    + KEY_HAVE_NUMBER + " TEXT,"
                    + KEY_HAVE_STORY_PART + " TEXT,"
                    + KEY_HAVE_FLAG + " TEXT,"
                    + KEY_HAVE_TASK_FLAG + " TEXT,"
                    + KEY_HAVE_STORY_FLAG + " TEXT,"
                    + KEY_HAVE_DATE + " TEXT,"
                    + " FOREIGN KEY"+"("+KEY_HAVE_ID+")"+" REFERENCES "+TABLE_USER+"("+KEY_ID+")"+"ON UPDATE CASCADE,"
                    + " FOREIGN KEY"+"("+KEY_HAVE_ID+")"+" REFERENCES "+TABLE_USER+"("+KEY_ID+")"+"ON DELETE CASCADE,"
                    + " FOREIGN KEY"+"("+KEY_HAVE_NUMBER+")"+" REFERENCES "+TABLE_TASK+"("+KEY_TASK_NUMBER+")ON UPDATE CASCADE,"
                    + " FOREIGN KEY"+"("+KEY_HAVE_NUMBER+")"+" REFERENCES "+TABLE_TASK+"("+KEY_TASK_NUMBER+")ON DELETE CASCADE"
                    //+ " FOREIGN KEY"+"("+KEY_HAVE_STORY_PART+")"+" REFERENCES "+TABLE_STORY+"("+KEY_STORY_PART+")"
                    +")";

    // STORY table create statement
    private static final String CREATE_TABLE_STORY =
            "CREATE TABLE " + TABLE_STORY+ "("
                    + KEY_STORY_SERIAL + " INTERGER PRIMARY KEY,"
                    + KEY_STORY_NUMBER + " TEXT,"
                    + KEY_STORY_NAME + " TEXT,"
                    + KEY_STORY_ADDRESS + " TEXT,"
                    + KEY_STORY_LONGITUDE + " TEXT,"
                    + KEY_STORY_LATITUDE + " TEXT,"
                    + KEY_STORY_PICTURE + " TEXT,"
                    + KEY_STORY_PICTURE_SD + " TEXT,"
                    + KEY_STORY_DATA + " TEXT,"
                    + KEY_STORY_PART + " TEXT,"
                    + KEY_STORY_SHOW + " TEXT,"
                    + KEY_STORY_DATE + " TEXT" + ");" +
            "CREATE INDEX "+"index_user "+"ON "+TABLE_STORY+"("+KEY_STORY_PART+");";

    // TASK table create statement
    private static final String CREATE_TABLE_TASK =
            "CREATE TABLE " + TABLE_TASK + "("
                    + KEY_TASK_NUMBER + " INTERGER PRIMARY KEY,"
                    + KEY_TASK_NAME + " TEXT,"
                    + KEY_TASK_BLOCK + " TEXT,"
                    + KEY_TASK_TYPE + " TEXT,"
                    + KEY_TASK_PICTURE + " TEXT,"
                    + KEY_TASK_PICTURE_SD + " TEXT,"
                    + KEY_TASK_INTRODUCTION + " TEXT,"
                    + KEY_TASK_PART + " TEXT,"
                    + KEY_TASK_HOT + " TEXT,"
                    + KEY_TASK_DEVELOPER + " TEXT,"
                    + KEY_TASK_SHOW + " TEXT,"
                    + KEY_TASK_DATE + " TEXT"+ ")";

    // USER table create statement
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_USER_ACCOUNT + " TEXT,"
                    + KEY_USER_PASS + " TEXT,"
                    + KEY_USER_FNAME + " TEXT,"
                    + KEY_USER_LNAME + " TEXT,"
                    + KEY_USER_EMAIL + " TEXT,"
                    + KEY_USER_PHONE + " TEXT,"
                    + KEY_USER_SEX + " TEXT,"
                    + KEY_USER_ADDRESS + " TEXT,"
                    + KEY_USER_PHONE_ID + " TEXT,"
                    + KEY_USER_CREATEDATE + " TEXT"+ ")";

    // PHOTO table create statement
    private static final String CREATE_TABLE_PHOTO =
            "CREATE TABLE " + TABLE_PHOTO + "("
                    + KEY_PHOTO_SERIAL + " TEXT,"
                    + KEY_PHOTO_DIR + " TEXT,"
                    + " FOREIGN KEY"+"("+KEY_PHOTO_SERIAL+")"+" REFERENCES "+TABLE_STORY+"("+KEY_STORY_SERIAL+")"+"ON UPDATE CASCADE"+ ")";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_TASK);
        db.execSQL(CREATE_TABLE_STORY);
        db.execSQL(CREATE_TABLE_HAVE);
        db.execSQL(CREATE_TABLE_PHOTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HAVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "USER" table methods ----------------//
    // Adding new user
    public void createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,user.getId());//id
        values.put(KEY_USER_ACCOUNT, user.getUsername()); // Name
        values.put(KEY_USER_PASS, user.getUserpass()); // Pass
        values.put(KEY_USER_FNAME, user.getFname()); // Fname
        values.put(KEY_USER_LNAME, user.getLname()); // Lname
        values.put(KEY_USER_EMAIL, user.getEmail()); // Email
        values.put(KEY_USER_PHONE, user.getPhone()); // Phone
        values.put(KEY_USER_SEX, user.getSex()); // Sex
        values.put(KEY_USER_ADDRESS, user.getAddress()); // Address
        values.put(KEY_USER_PHONE_ID, user.getUser_phone_id()); // Phoneid
        values.put(KEY_USER_CREATEDATE, user.getCreatedate()); // cDate

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }
    // Getting one user
    public User getUserbyidori(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_ID,
                        KEY_USER_ACCOUNT, KEY_USER_PASS}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User contact = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return shop
        return contact;
    }
    public User getUserbyid(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_ID,
                        KEY_USER_ACCOUNT, KEY_USER_PASS, KEY_USER_FNAME, KEY_USER_LNAME,KEY_USER_EMAIL,
                        KEY_USER_PHONE,KEY_USER_SEX,KEY_USER_ADDRESS,KEY_USER_PHONE_ID,KEY_USER_CREATEDATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User contact = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9),cursor.getString(10));
        // return shop
        return contact;
    }
    public User getUserbyName(String UserName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_ID,
                        KEY_USER_ACCOUNT, KEY_USER_PASS, KEY_USER_FNAME, KEY_USER_LNAME, KEY_USER_EMAIL,
                        KEY_USER_PHONE, KEY_USER_SEX, KEY_USER_ADDRESS, KEY_USER_PHONE_ID, KEY_USER_CREATEDATE}, KEY_USER_ACCOUNT + "=?",
                new String[]{UserName}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        User contact = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10));
        // return shop
        return contact;
    }
    public boolean hasUsers(String UserName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_ID,
                        KEY_USER_ACCOUNT, KEY_USER_PASS, KEY_USER_FNAME, KEY_USER_LNAME,KEY_USER_EMAIL,
                        KEY_USER_PHONE,KEY_USER_SEX,KEY_USER_ADDRESS,KEY_USER_PHONE_ID,KEY_USER_CREATEDATE}, KEY_USER_ACCOUNT + "=?",
                new String[]{UserName}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        if(cursor==null)return false;
        else return true;
    }
    public boolean hasUser(String UserName) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_USER_ACCOUNT + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {UserName});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d(TAG, String.format("%d records found", count));

            //endregion
        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }

    public int getIdbyName(String UserName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_ID,
                        KEY_USER_ACCOUNT, KEY_USER_PASS, KEY_USER_FNAME, KEY_USER_LNAME,KEY_USER_EMAIL,
                        KEY_USER_PHONE,KEY_USER_SEX,KEY_USER_ADDRESS,KEY_USER_PHONE_ID,KEY_USER_CREATEDATE}, KEY_USER_ACCOUNT + "=?",
                new String[]{UserName}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User contact = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9),cursor.getString(10));
        // return shop

        return contact.getId();
    }
    // Getting All User
    public List<User> getAllUser() {
        List<User> usersList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setUserpass(cursor.getString(2));
                // Adding contact to list
                usersList.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        return usersList;
    }
    public List<User> getAllUserFull() {
        List<User> usersList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setUserpass(cursor.getString(2));
                user.setFname(cursor.getString(3));
                user.setLname(cursor.getString(4));
                user.setEmail(cursor.getString(5));
                user.setPhone(cursor.getString(6));
                user.setSex(cursor.getString(7));
                user.setAddress(cursor.getString(8));
                user.setUser_phone_id(cursor.getString(9));
                user.setCreatedate(cursor.getString(10));
                // Adding contact to list
                usersList.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        return usersList;
    }
    // Getting users Count
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Updating a user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ACCOUNT, user.getUsername());
        values.put(KEY_USER_PASS, user.getUserpass());
        values.put(KEY_USER_FNAME, user.getFname());
        values.put(KEY_USER_LNAME, user.getLname());
        values.put(KEY_USER_EMAIL, user.getEmail());
        values.put(KEY_USER_PHONE, user.getPhone());
        values.put(KEY_USER_SEX, user.getSex());
        values.put(KEY_USER_ADDRESS, user.getAddress());
        values.put(KEY_USER_PHONE_ID, user.getUser_phone_id());
        values.put(KEY_USER_CREATEDATE, user.getCreatedate());

        // updating row
        return db.update(TABLE_USER, values, KEY_ID + " = ?", new String[]{String.valueOf(user.getId())});
    }
    public int updateUserwithId(int id,User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ACCOUNT, user.getUsername());
        values.put(KEY_USER_PASS, user.getUserpass());
        values.put(KEY_USER_FNAME, user.getFname());
        values.put(KEY_USER_LNAME, user.getLname());
        values.put(KEY_USER_EMAIL, user.getEmail());
        values.put(KEY_USER_PHONE, user.getPhone());
        values.put(KEY_USER_SEX, user.getSex());
        values.put(KEY_USER_ADDRESS, user.getAddress());
        values.put(KEY_USER_PHONE_ID, user.getUser_phone_id());
        values.put(KEY_USER_CREATEDATE, user.getCreatedate());

        // updating row
        return db.update(TABLE_USER, values, KEY_ID + " = ?", new String[] { String.valueOf(id) });
    }
    // Deleting a user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
        db.close();
    }
    // Delete all
    public void deleteAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+  TABLE_USER);
        db.close();
    }
    //Get count
    public boolean isUserempty(){
        boolean flag;
        String quString = "select exists(select 1 from " + TABLE_USER  + ");";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(quString, null);
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        if (count ==1) {
            flag =  false;
        } else {
            flag = true;
        }
        cursor.close();
        db.close();

        return flag;
    }

    //get 1st data
    public User getFirstRow(){
        User get_user = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            get_user = new User();
            get_user.setId(Integer.parseInt(cursor.getString(0)));
            get_user.setUsername(cursor.getString(1));
            get_user.setUserpass(cursor.getString(2));
            get_user.setFname(cursor.getString(3));
            get_user.setLname(cursor.getString(4));
            get_user.setEmail(cursor.getString(5));
            get_user.setPhone(cursor.getString(6));
            get_user.setSex(cursor.getString(7));
            get_user.setAddress(cursor.getString(8));
            get_user.setUser_phone_id(cursor.getString(9));
            get_user.setCreatedate(cursor.getString(10));
        }

        // return contact list
        return get_user;
    }


    // ------------------------ "TASK" table methods ----------------//
    // Adding new user
    public void createTaskWeb(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NUMBER, task.getTask_number()); // Shop Name
        values.put(KEY_TASK_NAME, task.getTask_name()); // Shop Phone Number
        values.put(KEY_TASK_BLOCK, task.getTask_block()); // Shop Phone Number
        values.put(KEY_TASK_TYPE, task.getTask_type()); // Shop Phone Number
        values.put(KEY_TASK_PICTURE, task.getTask_picture()); // Shop Phone Number
        values.put(KEY_TASK_PICTURE_SD, task.getTask_picture_sd()); // Shop Phone Number
        values.put(KEY_TASK_INTRODUCTION, task.getTask_introduction()); // Shop Phone Number
        values.put(KEY_TASK_PART, task.getTask_part()); // Shop Phone Number
        values.put(KEY_TASK_HOT, task.getTask_hot()); // Shop Phone Number
        values.put(KEY_TASK_DEVELOPER, task.getTask_developer()); // Shop Phone Number
        values.put(KEY_TASK_SHOW, task.getTask_show()); // Shop Phone Number
        values.put(KEY_TASK_DATE, task.getTask_date()); // Shop Phone Number

        // Inserting Row
        db.insert(TABLE_TASK, null, values);
        db.close(); // Closing database connection
    }
    // Adding new user
    public void createTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NUMBER, task.getTask_number()); // Shop Name
        values.put(KEY_TASK_NAME, task.getTask_name()); // Shop Phone Number
        values.put(KEY_TASK_BLOCK, task.getTask_block()); // Shop Phone Number
        values.put(KEY_TASK_TYPE, task.getTask_type()); // Shop Phone Number
        values.put(KEY_TASK_PICTURE, task.getTask_picture()); // Shop Phone Number
        values.put(KEY_TASK_INTRODUCTION, task.getTask_introduction()); // Shop Phone Number
        values.put(KEY_TASK_PART, task.getTask_part()); // Shop Phone Number
        values.put(KEY_TASK_HOT, task.getTask_hot()); // Shop Phone Number
        values.put(KEY_TASK_DEVELOPER, task.getTask_developer()); // Shop Phone Number
        values.put(KEY_TASK_SHOW, task.getTask_show()); // Shop Phone Number
        values.put(KEY_TASK_DATE, task.getTask_date()); // Shop Phone Number

        // Inserting Row
        db.insert(TABLE_TASK, null, values);
        db.close(); // Closing database connection
    }
    // Getting one user
    public Task getTaskbyid(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASK, new String[]{KEY_TASK_NUMBER,
                        KEY_TASK_NAME, KEY_TASK_BLOCK}, KEY_TASK_NUMBER + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task contact = new Task(
                Integer.parseInt(cursor.getString(0)),//num
                cursor.getString(1),//name
                cursor.getString(2),//block
                Integer.parseInt(cursor.getString(3)),//type
                cursor.getString(4),//block
                cursor.getString(5),//pic
                cursor.getString(6),//intro
                Integer.parseInt(cursor.getString(7)),//type
                Integer.parseInt(cursor.getString(8)),//dev
                cursor.getString(9),//dev
                Integer.parseInt(cursor.getString(10)),//show
                cursor.getString(11));//show
        // return shop
        return contact;
    }
    public Task getTaskbyId(String Id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_TASK
                + " WHERE "+KEY_TASK_NUMBER+" = "+Id, null);
        //Cursor cursor = db.query(true, new String[]{TABLE_HAVE, TABLE_TASK}, new String[]{KEY_TASK_NUMBER,KEY_TASK_NAME, KEY_USER_PASS}, KEY_TASK_NUMBER + "=?", new String[]{String.valueOf(Account)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task contact = new Task(
                Integer.parseInt(cursor.getString(0)),//num
                cursor.getString(1),//name
                cursor.getString(2),//block
                Integer.parseInt(cursor.getString(3)),//type
                cursor.getString(4),//pic
                cursor.getString(5),//pic
                cursor.getString(6),//intro
                Integer.parseInt(cursor.getString(7)),//part
                Integer.parseInt(cursor.getString(8)),//hot
                cursor.getString(9),//dev
                Integer.parseInt(cursor.getString(10)),//show
                cursor.getString(11));//date
        // return shop
        return contact;
    }

    public boolean hasTask(String TaskName) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_TASK + " WHERE " + KEY_TASK_NAME + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {TaskName});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d(TAG, String.format("%d records found", count));

            //endregion
        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }
    // Getting All User
    public List<Task> getAllTaskbyId(String id) {
        List<Task> tasksList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASK+","+TABLE_HAVE +" WHERE "+KEY_HAVE_NUMBER+" = "+ KEY_TASK_NUMBER + " AND " + KEY_HAVE_ID + " =?"+" GROUP BY "+ KEY_HAVE_NUMBER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {id});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setTask_number(Integer.parseInt(cursor.getString(0)));
                task.setTask_name(cursor.getString(1));
                task.setTask_block(cursor.getString(2));
                task.setTask_type(Integer.parseInt(cursor.getString(3)));
                task.setTask_picture(cursor.getString(4));
                task.setTask_picture_sd(cursor.getString(5));
                task.setTask_introduction(cursor.getString(6));
                task.setTask_part(Integer.parseInt(cursor.getString(7)));
                task.setTask_hot(Integer.parseInt(cursor.getString(8)));
                task.setTask_developer(cursor.getString(9));
                task.setTask_show(Integer.parseInt(cursor.getString(10)));
                task.setTask_date(cursor.getString(11));
                // Adding contact to list
                tasksList.add(task);
            } while (cursor.moveToNext());
        }

        // return contact list
        return tasksList;
    }

    public List<User> getAllUserFullcpy() {
        List<User> usersList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setUserpass(cursor.getString(2));
                user.setFname(cursor.getString(3));
                user.setLname(cursor.getString(4));
                user.setEmail(cursor.getString(5));
                user.setPhone(cursor.getString(6));
                user.setSex(cursor.getString(7));
                user.setAddress(cursor.getString(8));
                user.setUser_phone_id(cursor.getString(9));
                user.setCreatedate(cursor.getString(10));
                // Adding contact to list
                usersList.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        return usersList;
    }
    // Getting users Count
    public int getTasksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Updating a user
    public int updateTaskWeb(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NUMBER, task.getTask_number()); // Shop Name
        values.put(KEY_TASK_NAME, task.getTask_name()); // Shop Phone Number
        values.put(KEY_TASK_BLOCK, task.getTask_block()); // Shop Phone Number
        values.put(KEY_TASK_TYPE, task.getTask_type()); // Shop Phone Number
        values.put(KEY_TASK_PICTURE, task.getTask_picture()); // Shop Phone Number
        values.put(KEY_TASK_PICTURE_SD, task.getTask_picture_sd()); // Shop Phone Number
        values.put(KEY_TASK_INTRODUCTION, task.getTask_introduction()); // Shop Phone Number
        values.put(KEY_TASK_PART, task.getTask_part()); // Shop Phone Number
        values.put(KEY_TASK_HOT, task.getTask_hot()); // Shop Phone Number
        values.put(KEY_TASK_DEVELOPER, task.getTask_developer()); // Shop Phone Number
        values.put(KEY_TASK_SHOW, task.getTask_show()); // Shop Phone Number
        values.put(KEY_TASK_DATE, task.getTask_date()); // Shop Phone Number
        // updating row
        return db.update(TABLE_TASK, values, KEY_TASK_NUMBER + " = ?", new String[]{String.valueOf(task.getTask_number())});
    }
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NUMBER, task.getTask_number()); // Shop Name
        values.put(KEY_TASK_NAME, task.getTask_name()); // Shop Phone Number
        values.put(KEY_TASK_BLOCK, task.getTask_block()); // Shop Phone Number
        values.put(KEY_TASK_TYPE, task.getTask_type()); // Shop Phone Number
        values.put(KEY_TASK_PICTURE, task.getTask_picture()); // Shop Phone Number
        values.put(KEY_TASK_INTRODUCTION, task.getTask_introduction()); // Shop Phone Number
        values.put(KEY_TASK_PART, task.getTask_part()); // Shop Phone Number
        values.put(KEY_TASK_HOT, task.getTask_hot()); // Shop Phone Number
        values.put(KEY_TASK_DEVELOPER, task.getTask_developer()); // Shop Phone Number
        values.put(KEY_TASK_SHOW, task.getTask_show()); // Shop Phone Number
        values.put(KEY_TASK_DATE, task.getTask_date()); // Shop Phone Number
        // updating row
        return db.update(TABLE_TASK, values, KEY_TASK_NUMBER + " = ?", new String[]{String.valueOf(task.getTask_number())});
    }
    // Deleting a user
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASK, KEY_TASK_NUMBER + " = ?",
                new String[] { String.valueOf(task.getTask_number())});
        db.close();
    }
    // Delete all
    public void deleteAllTask() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+  TABLE_TASK);
        db.close();
    }

    // ------------------------ "STORY" table methods ----------------//
    // Adding new user
    public void createStoryWeb(Story story) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STORY_SERIAL, story.getStory_serial_no()); // Story Serial
        values.put(KEY_STORY_NUMBER, story.getStory_number()); //Story Number
        values.put(KEY_STORY_NAME, story.getStory_name()); //Story Name
        values.put(KEY_STORY_ADDRESS, story.getStory_address()); //Story address
        values.put(KEY_STORY_LONGITUDE, story.getStory_longtitute()); //Story longitude
        values.put(KEY_STORY_LATITUDE, story.getStory_latitude()); //Story latitude
        values.put(KEY_STORY_PICTURE, story.getStory_picture()); //Story picture
        values.put(KEY_STORY_PICTURE_SD, story.getStory_picture_sd()); //Story picture sd
        values.put(KEY_STORY_DATA, story.getStory_data()); //Story data
        values.put(KEY_STORY_PART, story.getStory_part()); //Story part
        values.put(KEY_STORY_SHOW, story.getStory_show()); //Story show
        values.put(KEY_STORY_DATE, story.getStory_date()); //Story date

        // Inserting Row
        db.insert(TABLE_STORY, null, values);
        db.close(); // Closing database connection
    }
    // Adding new user
    public void createStory(Story story) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STORY_SERIAL, story.getStory_serial_no()); // Story Serial
        values.put(KEY_STORY_NUMBER, story.getStory_number()); //Story Number
        values.put(KEY_STORY_NAME, story.getStory_name()); //Story Name
        values.put(KEY_STORY_ADDRESS, story.getStory_address()); //Story address
        values.put(KEY_STORY_LONGITUDE, story.getStory_longtitute()); //Story longitude
        values.put(KEY_STORY_LATITUDE, story.getStory_latitude()); //Story latitude
        values.put(KEY_STORY_PICTURE, story.getStory_picture()); //Story picture
        values.put(KEY_STORY_DATA, story.getStory_data()); //Story data
        values.put(KEY_STORY_PART, story.getStory_part()); //Story part
        values.put(KEY_STORY_SHOW, story.getStory_show()); //Story show
        values.put(KEY_STORY_DATE, story.getStory_date()); //Story date

        // Inserting Row
        db.insert(TABLE_STORY, null, values);
        db.close(); // Closing database connection
    }
    // Getting one user
    public Story getStorybyid(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STORY, new String[]{KEY_STORY_SERIAL,
                        KEY_STORY_NAME, KEY_USER_PASS}, KEY_STORY_SERIAL + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Story contact = new Story(
                Integer.parseInt(cursor.getString(0)),//story serial
                Integer.parseInt(cursor.getString(1)),//story number
                cursor.getString(2),//story name
                cursor.getString(3),//story address
                cursor.getString(4),//story longtitude
                cursor.getString(5),//story latitude
                cursor.getString(6),//story pic
                cursor.getString(7),//story pic sd
                cursor.getString(8),//story data
                Integer.parseInt(cursor.getString(9)),//story part
                Integer.parseInt(cursor.getString(10)),//story show
                cursor.getString(11)//date
        );
        // return shop
        return contact;
    }
    public boolean hasStory(String StoryName) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_STORY + " WHERE " + KEY_STORY_NAME + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {StoryName});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d(TAG, String.format("%d records found", count));

            //endregion
        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }

    // Getting All User
    public List<Story> getAllStorybyTaskId(String task_id) {
        List<Story> storiesList = new ArrayList<Story>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STORY+" WHERE "+ KEY_STORY_NUMBER +" =? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,new String[] {task_id});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Story story = new Story();
                story.setStory_serial_no(Integer.parseInt(cursor.getString(0)));//serial
                story.setstory_number(Integer.parseInt(cursor.getString(1)));//number
                story.setStory_name(cursor.getString(2));//name
                story.setStory_address(cursor.getString(3));//addr
                story.setStory_longtitute(cursor.getString(4));//longitude
                story.setStory_latitude(cursor.getString(5));//latitude
                story.setStory_picture(cursor.getString(6));//pic
                story.setStory_picture_sd(cursor.getString(7));//pic sd
                story.setStory_data(cursor.getString(8));//data
                story.setStory_part(Integer.parseInt(cursor.getString(9)));//part
                story.setStory_show(Integer.parseInt(cursor.getString(10)));//show
                story.setStory_date(cursor.getString(11));//date
                // Adding contact to list
                storiesList.add(story);
            } while (cursor.moveToNext());
        }

        // return contact list
        return storiesList;
    }

    // Getting All User
    public List<Story> getAllStory() {
        List<Story> storiesList = new ArrayList<Story>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Story story = new Story();
                story.setStory_serial_no(Integer.parseInt(cursor.getString(0)));
                story.setstory_number(Integer.parseInt(cursor.getString(1)));
                story.setStory_name(cursor.getString(2));
                story.setStory_address(cursor.getString(3));
                story.setStory_data(cursor.getString(4));
                story.setStory_part(Integer.parseInt(cursor.getString(5)));
                story.setStory_date(cursor.getString(6));
                // Adding contact to list
                storiesList.add(story);
            } while (cursor.moveToNext());
        }

        // return contact list
        return storiesList;
    }
    // Getting users Count
    public int getStorysCount(int task_id) {
        String countQuery = "SELECT * FROM " + TABLE_STORY +" WHERE "+ KEY_HAVE_NUMBER+" = ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }
    // Updating a user
    public int updateStoryWeb(Story story) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STORY_SERIAL, story.getStory_serial_no());
        values.put(KEY_STORY_NUMBER, story.getStory_number());
        values.put(KEY_STORY_NAME, story.getStory_name());
        values.put(KEY_STORY_ADDRESS, story.getStory_address());
        values.put(KEY_STORY_LONGITUDE, story.getStory_longtitute());
        values.put(KEY_STORY_LATITUDE, story.getStory_latitude());
        values.put(KEY_STORY_PICTURE, story.getStory_picture());
        values.put(KEY_STORY_PICTURE_SD, story.getStory_picture_sd());
        values.put(KEY_STORY_DATA, story.getStory_data());
        values.put(KEY_STORY_PART, story.getStory_part());
        values.put(KEY_STORY_SHOW, story.getStory_show());
        values.put(KEY_STORY_DATE, story.getStory_date());

        // updating row
        return db.update(TABLE_STORY, values, KEY_STORY_SERIAL + " = ?", new String[]{String.valueOf(story.getStory_serial_no())});
    }
    // Updating a user
    public int updateStory(Story story) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STORY_SERIAL, story.getStory_serial_no());
        values.put(KEY_STORY_NUMBER, story.getStory_number());
        values.put(KEY_STORY_NAME, story.getStory_name());
        values.put(KEY_STORY_ADDRESS, story.getStory_address());
        values.put(KEY_STORY_LONGITUDE, story.getStory_longtitute());
        values.put(KEY_STORY_LATITUDE, story.getStory_latitude());
        values.put(KEY_STORY_PICTURE, story.getStory_picture());
        values.put(KEY_STORY_DATA, story.getStory_data());
        values.put(KEY_STORY_PART, story.getStory_part());
        values.put(KEY_STORY_SHOW, story.getStory_show());
        values.put(KEY_STORY_DATE, story.getStory_date());

        // updating row
        return db.update(TABLE_STORY, values, KEY_STORY_SERIAL + " = ?", new String[]{String.valueOf(story.getStory_serial_no())});
    }
    // Deleting a user
    public void deleteStory(Story story) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STORY, KEY_STORY_SERIAL+ " = ?",
                new String[] { String.valueOf(story.getStory_serial_no()) });
        db.close();
    }
    // Delete all
    public void deleteAllStory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+  TABLE_STORY);
        db.close();
    }
///*
    // ------------------------ "HAVE" table methods ----------------//
    // Adding new Have
    public void createHave(Have have) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HAVE_ID, have.getHave_id()); // have acc
        values.put(KEY_HAVE_NUMBER, have.getHave_number()); // have number
        values.put(KEY_HAVE_STORY_PART, have.getHave_story_part()); // have story part
        values.put(KEY_HAVE_FLAG, have.getHave_flag()); // have flag
        values.put(KEY_HAVE_TASK_FLAG, have.getHave_task_flag()); // have number
        values.put(KEY_HAVE_STORY_FLAG, have.getHave_story_flag()); // have number
        values.put(KEY_HAVE_DATE, have.getHave_date());// have date

        // Inserting Row
        db.insert(TABLE_HAVE, null, values);
        db.close(); // Closing database connection
    }
    // Getting one user
    /*public Have getHavebyid(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HAVE, new String[]{KEY_ID,
                        KEY_HAVE_NUMBER, KEY_HAVE_FLAG}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Have contact = new Have(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return shop
        return contact;
    }*/
    public Have getHavebyId(String Id,String Taskno) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectString = "SELECT DISTINCT * FROM " + TABLE_HAVE + " WHERE " + KEY_HAVE_ID + " = ? AND "+ KEY_HAVE_NUMBER + " = ? ";
        Cursor cursor = db.rawQuery(selectString,new String[] {Id,Taskno});

        if (cursor != null)
            cursor.moveToFirst();

        Have contact = new Have(cursor.getString(0),//have id
                Integer.parseInt(cursor.getString(1)),//have number
                Integer.parseInt(cursor.getString(2)),//have stry part
                Integer.parseInt(cursor.getString(3)),//have flag
                Integer.parseInt(cursor.getString(4)),//have task flag
                Integer.parseInt(cursor.getString(5)),//have stry flag
                cursor.getString(6));//have date
        // return shop
        return contact;
    }
    public Have getHavebyValue(String id,String task, String part) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HAVE, new String[]{KEY_HAVE_ID, KEY_HAVE_NUMBER,KEY_HAVE_STORY_PART, KEY_HAVE_FLAG,KEY_HAVE_TASK_FLAG,KEY_HAVE_STORY_FLAG,KEY_HAVE_DATE},
                KEY_HAVE_ID + " =? "+" AND "+ KEY_HAVE_NUMBER + " =? "+" AND "+ KEY_HAVE_STORY_PART+ " =? ",
                new String[]{id,task,part}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Have contact = new Have(cursor.getString(0),//have id
                Integer.parseInt(cursor.getString(1)),//have number
                Integer.parseInt(cursor.getString(2)),//have stry part
                Integer.parseInt(cursor.getString(3)),//have flag
                Integer.parseInt(cursor.getString(4)),//have task flag
                Integer.parseInt(cursor.getString(5)),//have stry flag
                cursor.getString(6));//have date
        // return shop
        return contact;
    }
    public Have getHavebyData(String id,String task, String part) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectString = "SELECT * FROM " + TABLE_HAVE + " WHERE " + KEY_HAVE_ID + " = ? AND "+ KEY_HAVE_NUMBER + " = ? AND "+ KEY_HAVE_STORY_PART + " = ?";
        Cursor cursor = db.rawQuery(selectString,new String[] {id,task,part});
        if (cursor != null)
            cursor.moveToFirst();

        Have contact = new Have(cursor.getString(0),//have id
                Integer.parseInt(cursor.getString(1)),//have number
                Integer.parseInt(cursor.getString(2)),//have stry part
                Integer.parseInt(cursor.getString(3)),//have flag
                Integer.parseInt(cursor.getString(4)),//have task flag
                Integer.parseInt(cursor.getString(5)),//have stry flag
                cursor.getString(6));//have date
        // return shop
        return contact;
    }
    /*public boolean hasHave(String id,String task, String part) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HAVE, new String[]{KEY_HAVE_ID, KEY_HAVE_NUMBER,KEY_HAVE_STORY_PART, KEY_HAVE_FLAG,KEY_HAVE_TASK_FLAG,KEY_HAVE_STORY_FLAG,KEY_HAVE_DATE},
                KEY_HAVE_ID + "=?"+"AND"+ KEY_HAVE_NUMBER + "=?"+"AND"+ KEY_HAVE_STORY_PART+ "=?",
                new String[]{id,task,part}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // return shop
        if(cursor==null)return false;
        else return true;
    }*/
    public boolean hasHave(String id,String task,String part) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_HAVE + " WHERE " + KEY_HAVE_ID + " = ? AND "+ KEY_HAVE_NUMBER + " = ? AND "+ KEY_HAVE_STORY_PART + " = ?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {id,task,part});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d(TAG, String.format("%d records found", count));

            //endregion
        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }
    public boolean UserhasTask(String id,String task) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT DISTINCT  * FROM " + TABLE_HAVE + " WHERE " + KEY_HAVE_ID + " = ? AND "+ KEY_HAVE_NUMBER + " = ? AND "+ KEY_HAVE_FLAG + " = ?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {id,task,String.valueOf(1)});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d(TAG, String.format("%d records found", count));

            //endregion
        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }
    // Getting All User
    public List<Have> getAllHave() {
        List<Have> haveList = new ArrayList<Have>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HAVE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Have have = new Have();
                have.setHave_id(cursor.getString(0));
                have.setHave_number(Integer.parseInt(cursor.getString(1)));
                have.setHave_flag(Integer.parseInt(cursor.getString(2)));
                have.setHave_date(cursor.getString(3));
                // Adding contact to list
                haveList.add(have);
            } while (cursor.moveToNext());
        }

        // return contact list
        return haveList;
    }
    public List<Have> getAllHavebyId(int Account) {
        List<Have> haveList = new ArrayList<Have>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HAVE +" WHERE "
                + Account +" = " +KEY_HAVE_ID;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Have have = new Have();
                have.setHave_id(cursor.getString(0));//have id
                have.setHave_number(Integer.parseInt(cursor.getString(1)));//have number
                have.setHave_story_part(Integer.parseInt(cursor.getString(2)));//have story part
                have.setHave_flag(Integer.parseInt(cursor.getString(3)));//have flag
                have.setHave_task_flag(Integer.parseInt(cursor.getString(4)));//have task flag
                have.setHave_story_flag(Integer.parseInt(cursor.getString(5)));//have stry flag
                have.setHave_date(cursor.getString(6));
                // Adding contact to list
                haveList.add(have);
            } while (cursor.moveToNext());
        }

        // return contact list
        return haveList;
    }
    public List<Have> getAllHavebyIdOnTask(int Account,int Taskid) {
        List<Have> haveList = new ArrayList<Have>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HAVE  +" WHERE "
                + Account +" = " +KEY_HAVE_ID+" AND "+ Taskid +" = " +KEY_HAVE_NUMBER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Have have = new Have();
                have.setHave_id(cursor.getString(0));//have id
                have.setHave_number(Integer.parseInt(cursor.getString(1)));//have number
                have.setHave_story_part(Integer.parseInt(cursor.getString(2)));//have story part
                have.setHave_flag(Integer.parseInt(cursor.getString(3)));//have flag
                have.setHave_task_flag(Integer.parseInt(cursor.getString(4)));//have task flag
                have.setHave_story_flag(Integer.parseInt(cursor.getString(5)));//have stry flag
                have.setHave_date(cursor.getString(6));
                // Adding contact to list
                haveList.add(have);
            } while (cursor.moveToNext());
        }

        // return contact list
        return haveList;
    }
    // Getting users Count
    public int gethavesCountofAccount(String Account) {
        String countQuery = "SELECT  * FROM " + TABLE_HAVE+" WHERE "+KEY_HAVE_ID+" = "+Account;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Updating a user
    public int updateHaveWeb(Have have) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HAVE_ID, have.getHave_id());
        values.put(KEY_HAVE_NUMBER, have.getHave_number());
        values.put(KEY_HAVE_STORY_PART, have.getHave_story_part());
        values.put(KEY_HAVE_FLAG, have.getHave_flag());
        //values.put(KEY_HAVE_TASK_FLAG, have.getHave_task_flag());
        //values.put(KEY_HAVE_STORY_FLAG, have.getHave_story_flag());
        //values.put(KEY_HAVE_DATE, have.getHave_date());
        // updating row
        return db.update(TABLE_HAVE, values, KEY_HAVE_ID + " = ? " +" AND "+KEY_HAVE_NUMBER + " = ? " +" AND "+KEY_HAVE_STORY_PART + " = ?", new String[]{String.valueOf(have.getHave_id()),String.valueOf(have.getHave_number()),String.valueOf(have.getHave_story_part())});
    }
    public int updateHaveFlag(Have have) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HAVE_ID, have.getHave_id());
        values.put(KEY_HAVE_NUMBER, have.getHave_number());
        values.put(KEY_HAVE_STORY_PART, have.getHave_story_part());
        values.put(KEY_HAVE_FLAG, have.getHave_flag());
        values.put(KEY_HAVE_TASK_FLAG, have.getHave_task_flag());
        values.put(KEY_HAVE_STORY_FLAG, have.getHave_story_flag());
        values.put(KEY_HAVE_DATE, have.getHave_date());
        // updating row
        return db.update(TABLE_HAVE, values, KEY_HAVE_ID + " = ? " +" AND "+KEY_HAVE_NUMBER + " = ? " +" AND "+KEY_HAVE_STORY_PART + " = ?", new String[]{String.valueOf(have.getHave_id()),String.valueOf(have.getHave_number()),String.valueOf(have.getHave_story_part())});
    }
    // Deleting a user
    public void deleteHave(Have have) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HAVE, KEY_HAVE_ID + " = ?",
                new String[] { String.valueOf(have.getHave_id()) });
        db.close();
    }
    // Delete all
    public void deleteAllHave() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+  TABLE_HAVE);
        db.close();
    }

    // ------------------------ "PHOTO" table methods ----------------//
    // Adding new Photo
    public void createPhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PHOTO_SERIAL, photo.getPhoto_serial()); // story serial
        values.put(KEY_PHOTO_DIR, photo.getPhoto_dir()); // story dir

        // Inserting Row
        db.insert(TABLE_PHOTO, null, values);
        db.close(); // Closing database connection
    }
    // Update new Photo
    public int updatePhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PHOTO_SERIAL, photo.getPhoto_serial());
        values.put(KEY_PHOTO_DIR, photo.getPhoto_dir());

        // updating row
        return db.update(TABLE_PHOTO, values, KEY_PHOTO_SERIAL + " = ?", new String[]{String.valueOf(photo.getPhoto_serial())});
    }
    //Get all Photo of serial
    public List<Photo> getAllPhotoBySerial(String Serial) {
        List<Photo> photoList = new ArrayList<Photo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PHOTO  +" WHERE "
                + Serial +" = " +KEY_PHOTO_SERIAL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Photo photo = new Photo();
                photo.setPhoto_serial(cursor.getInt(0));//photo serial
                photo.setPhoto_dir(cursor.getString(1));//photo photo
                // Adding contact to list
                photoList.add(photo);
            } while (cursor.moveToNext());
        }

        // return contact list
        return photoList;
    }
    public boolean hasAlbum(long serial) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_PHOTO + " WHERE " + KEY_PHOTO_SERIAL + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {String.valueOf(serial)});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d(TAG, String.format("%d records found", count));

            //endregion
        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }
    public boolean PhotoBelongsToAlbum(String photoURL,long serial) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_PHOTO + " WHERE " + KEY_PHOTO_SERIAL + " =? AND " + KEY_PHOTO_DIR + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {String.valueOf(serial),photoURL});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d(TAG, String.format("%d records found", count));

            //endregion
        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }
    // Deleting a photo
    public void deletePhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHOTO, KEY_PHOTO_SERIAL + " = ?",
                new String[] { String.valueOf(photo.getPhoto_serial()) });
        db.close();
    }
    // Delete all
    public void deleteAllPhoto() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+  TABLE_PHOTO);
        db.close();
    }

    // ------------------------ Close Database methods ----------------//
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    // ------------------------ Get date time methods ----------------//
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    //*/
}
