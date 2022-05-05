package com.example.logisticsmanagementapp2

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

class LocalWaybillActivity : AppCompatActivity() {
    lateinit var backBtn : Button
    val billList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityBox.addActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_waybill)

//        返回按钮
        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        val dbHelper = MyDatabaseHelper(this, "WaybillStore", 11)
        val db = dbHelper.readableDatabase
        val cursor = db.query("Waybill", null, null, null, null, null, null)
        @SuppressLint("Range")
        if (cursor.moveToFirst()) {
            do {
                val waybillNo = cursor.getString(cursor.getColumnIndex("waybillNo"))
                val ArrivalStation = cursor.getString(cursor.getColumnIndex("transportationArrivalStation"))
                val consignor = cursor.getString(cursor.getColumnIndex("consignor"))
                val consignorPhoneNumber = cursor.getString(cursor.getColumnIndex("consignorPhoneNumber"))
                val consignee = cursor.getString(cursor.getColumnIndex("consignee"))
                val consigneePhoneNumber = cursor.getString(cursor.getColumnIndex("consigneePhoneNumber"))
                val goodsName = cursor.getString(cursor.getColumnIndex("goodsName"))
                val numberOfPackages = cursor.getString(cursor.getColumnIndex("numberOfPackages"))
                val freightPaidByConsignor = cursor.getString(cursor.getColumnIndex("freightPaidByConsignor"))
                val freightPaidByTheReceivingParty = cursor.getString(cursor.getColumnIndex("freightPaidByTheReceivingParty"))
                Log.d("MainActivity", "waybillNo is $waybillNo")
                Log.d("MainActivity", "consignor is $consignor")
                Log.d("MainActivity", "consignorPhoneNumber is $consignorPhoneNumber")
                Log.d("MainActivity", "consignee is $consignee")
                val billNo = "No: ${waybillNo.toString().trim()}"   //No: X2000001
                val billTrace =                                     //乌鲁木齐 - 丹东  汽车配件 3件  到付298元
                    "${
                        ArrivalStation.toString().trim()
                    } - 沈阳  ${
                        goodsName.toString().trim()
                    } ${
                        numberOfPackages.toString().trim()
                    }件  到付${freightPaidByTheReceivingParty.toString().trim()}元"
                val billName =                                      //收货人：洛天賜(13044784582)
                    "收货人：${consignee.toString().trim()}(${consigneePhoneNumber.toString().trim()})"
                billList.add(billNo + "\n" + billTrace + "\n" + billName)
            } while (cursor.moveToNext())
        }
        cursor.close()

        val listView: ListView = findViewById(R.id.listView)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, billList)
        listView.adapter = adapter
    }

    class MyDatabaseHelper(val context: Context, name: String, version: Int) :
        SQLiteOpenHelper(context, name, null, version) {
        override fun onCreate(db: SQLiteDatabase){
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
        }
    }

}

