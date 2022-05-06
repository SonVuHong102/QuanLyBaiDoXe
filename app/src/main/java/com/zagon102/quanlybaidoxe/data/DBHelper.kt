package com.zagon102.quanlybaidoxe.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zagon102.quanlybaidoxe.presentation.model.User
import com.zagon102.quanlybaidoxe.presentation.model.VehicleCheck
import com.zagon102.quanlybaidoxe.ultis.Constants
import com.zagon102.quanlybaidoxe.ultis.toDateFormat

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        var query = ("CREATE TABLE " + USER_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                USERNAME_COl + " TEXT," +
                PASSWORD_COL + " TEXT," +
                ROLE_COL + " TEXT," +
                NAME_COL + " TEXT," +
                DOB_COL + " TEXT," +
                PHONE_COL + " TEXT," +
                EMAIL_COL + " TEXT" + ")")
        db.execSQL(query)
        query = ("CREATE TABLE " + CHECK_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                BRAND_COL + " TEXT," +
                SEATS_COL + " INTEGER," +
                COLOR_COL + " TEXT," +
                PLATE_COL + " TEXT," +
                CHECKIN_COL + " TEXT," +
                CHECKOUT_COL + " TEXT," +
                NAME_COL + " TEXT," +
                PHONE_COL + " TEXT," +
                CASH_COL + " TEXT," +
                DONE_COL + " TEXT" + ")")
        db.execSQL(query)
        query =
            ("INSERT INTO $USER_TABLE_NAME ($USERNAME_COl,$PASSWORD_COL,$NAME_COL,$DOB_COL,$PHONE_COL,$EMAIL_COL) " +
                    "VALUE ('admin','admin','admin',2000-01-02,'0912345678','anonymousemail@gmail.com'")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $USER_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $CHECK_TABLE_NAME")
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put(USERNAME_COl, user.username)
        values.put(PASSWORD_COL, user.password)
        values.put(NAME_COL, user.name)
        values.put(ROLE_COL, user.role)
        values.put(DOB_COL, user.dob.toDateFormat())
        values.put(PHONE_COL, user.phone)
        values.put(EMAIL_COL, user.email)
        val db = this.writableDatabase
        db.insert(USER_TABLE_NAME, null, values)
        db.close()
    }

    fun addCheck(vehicleCheck: VehicleCheck) {
        val values = ContentValues()
        values.put(BRAND_COL, vehicleCheck.brand)
        values.put(SEATS_COL, vehicleCheck.seats)
        values.put(COLOR_COL, vehicleCheck.color)
        values.put(PLATE_COL, vehicleCheck.plate)
        values.put(CHECKIN_COL, vehicleCheck.checkInDate.toDateFormat())
        values.put(NAME_COL, vehicleCheck.name)
        values.put(PHONE_COL, vehicleCheck.phone)
        values.put(DONE_COL, Constants.PENDING)
        val db = this.writableDatabase
        db.insert(CHECK_TABLE_NAME, null, values)
        db.close()
    }

    fun getUsers(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $USER_TABLE_NAME", null)
    }

    fun getChecks(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $CHECK_TABLE_NAME", null)
    }

    fun getPendingChecks(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM $CHECK_TABLE_NAME WHERE done = '${Constants.PENDING}'",
            null
        )
    }

    fun authUser(username: String, password: String): Cursor? {
        val db = this.writableDatabase
        return db.rawQuery(
            "SELECT * FROM $USER_TABLE_NAME WHERE username = ? AND password = ?",
            arrayOf(username, password)
        )
    }

    fun getUser(username: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $USER_TABLE_NAME WHERE username = ?", arrayOf(username))
    }

    fun updateUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(PASSWORD_COL, user.password)
        values.put(NAME_COL, user.name)
        values.put(DOB_COL, user.dob.toDateFormat())
        values.put(PHONE_COL, user.phone)
        values.put(EMAIL_COL, user.email)
        db.update(USER_TABLE_NAME, values, "id = ?", arrayOf(user.id.toString()))
        db.close()
    }

    fun updateCheck(vehicleCheck: VehicleCheck) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(CHECKOUT_COL, vehicleCheck.checkOutDate!!.toDateFormat())
        values.put(CASH_COL, vehicleCheck.cash)
        values.put(DONE_COL, Constants.DONE)
        db.update(CHECK_TABLE_NAME, values, "id = ?", arrayOf(vehicleCheck.id.toString()))
        db.close()
    }

    companion object {
        private val DATABASE_NAME = "MyDatabase"
        private val DATABASE_VERSION = 1
        val USER_TABLE_NAME = "user_table"
        val CHECK_TABLE_NAME = "check_table"
        val ID_COL = "id"
        val USERNAME_COl = "username"
        val PASSWORD_COL = "password"
        val ROLE_COL = "role"
        val NAME_COL = "name"
        val DOB_COL = "dob"
        val PHONE_COL = "phone"
        val EMAIL_COL = "email"
        val BRAND_COL = "brand"
        val SEATS_COL = "seats"
        val COLOR_COL = "color"
        val PLATE_COL = "plate"
        val CHECKIN_COL = "checkin"
        val CHECKOUT_COL = "checkout"
        val CASH_COL = "cash"
        val DONE_COL = "done"
    }
}