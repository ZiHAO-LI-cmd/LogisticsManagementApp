package com.example.logisticsmanagementapp2

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var PersonNameText: EditText
    lateinit var PasswordText : EditText
    lateinit var LoginButton : Button
    lateinit var ExitButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityBox.addActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PersonNameText = findViewById(R.id.editTextTextPersonName2)
        PasswordText = findViewById(R.id.editTextTextPassword2)
        LoginButton = findViewById(R.id.button1)
        ExitButton = findViewById(R.id.button2)

        val dbHelper = MyDatabaseHelper(this, "UserStore", 2)


//        登录按钮
        LoginButton.setOnClickListener {
            val intent = Intent(this, LoggedActivity::class.java)
            val nameData = PersonNameText.getText().toString()
            val PasswordData = PasswordText.getText().toString()

            var flag = false
            val db = dbHelper.readableDatabase
            val cursor = db.query("User", null, null, null, null, null, null)
            @SuppressLint("Range")
            if (cursor.moveToFirst()) {
                do {
                    val user_id = cursor.getString(cursor.getColumnIndex("user_id"))
                    val user_name = cursor.getString(cursor.getColumnIndex("user_name"))
                    val user_password = cursor.getString(cursor.getColumnIndex("user_password"))

//                    校验用户名，密码
                    if (user_name == nameData && user_password == PasswordData) {
                        flag = true
                    }

                } while (cursor.moveToNext())
            }
            cursor.close()

            if (flag == true) {        //用户名，密码正确
                Log.d("MainActivity", "nameData is $nameData")
                intent.putExtra("name", nameData)
                intent.putExtra("password", PasswordData)
                startActivity(intent)
            }else {
                Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG).show()
            }

        }

//        退出按钮
        ExitButton.setOnClickListener {
            finish()
        }


//                创建用户数据库
//        val createDatabase: Button = findViewById(R.id.createDatabase)
//        createDatabase.setOnClickListener{
//            dbHelper.writableDatabase
//        }

//        val db = dbHelper.writableDatabase
//        val value1 = ContentValues().apply {
//            put("user_name","李子豪")
//            put("user_password", "123456")
//        }
//        val value2 = ContentValues().apply {
//            put("user_name","张三")
//            put("user_password", "654321")
//        }
//        db.insert("User", null, value1)
//        db.insert("User", null, value2)

        //        查询调试
//        val queryData: Button = findViewById(R.id.queryData)
//        queryData.setOnClickListener {
//            val db = dbHelper.writableDatabase
//            val cursor = db.query("User", null, null, null, null, null, null)
//            @SuppressLint("Range")
//            if (cursor.moveToFirst()) {
//                do {
//                    val user_id = cursor.getString(cursor.getColumnIndex("user_id"))
//                    val user_name = cursor.getString(cursor.getColumnIndex("user_name"))
//                    val user_password = cursor.getString(cursor.getColumnIndex("user_password"))
//                    Log.d("MainActivity", "user_id is $user_id")
//                    Log.d("MainActivity", "user_name is $user_name")
//                    Log.d("MainActivity", "user_password is $user_password")
//                } while (cursor.moveToNext())
//            }
//            cursor.close()
//        }

    }

    override fun onRestart() {
        super.onRestart()
//        返回到MainActivity,清空用户名密码输入框
        PersonNameText = findViewById(R.id.editTextTextPersonName2)
        PasswordText = findViewById(R.id.editTextTextPassword2)
        PersonNameText.setText("")
        PasswordText.setText("")
    }

    class MyDatabaseHelper(val context: Context, name: String, version: Int) :
        SQLiteOpenHelper(context, name, null, version) {
        private val createUser = "create table User (" +
                " user_id integer primary key autoincrement," +
                "user_name text," +
                "user_password text)"

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(createUser)
            Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
            db.execSQL("drop table if exists User")
            onCreate(db)
        }
    }
}

