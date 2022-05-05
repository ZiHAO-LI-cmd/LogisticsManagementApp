package com.example.logisticsmanagementapp2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EnterWaybillActivity : AppCompatActivity() {
    lateinit var ArrivalStation: EditText
    lateinit var consignor: EditText
    lateinit var consignorPhoneNumber: EditText
    lateinit var consignee: EditText
    lateinit var consigneePhoneNumber: EditText
    lateinit var goodsName: EditText
    lateinit var numberOfPackages: EditText
    lateinit var freightPaidByConsignor: EditText
    lateinit var freightPaidByTheReceivingParty: EditText
    lateinit var Savebutton: Button
    lateinit var Backbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityBox.addActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_waybill)

        ArrivalStation = (findViewById(R.id.ArrivalStation))
        consignor = findViewById(R.id.consignor)
        consignorPhoneNumber = findViewById(R.id.consignorPhoneNumber)
        consignee = findViewById(R.id.consignee)
        consigneePhoneNumber = findViewById(R.id.consigneePhoneNumber)
        goodsName = findViewById(R.id.goodsName)
        numberOfPackages = findViewById(R.id.numberOfPackages)
        freightPaidByConsignor = findViewById(R.id.freightPaidByConsignor)
        freightPaidByTheReceivingParty = findViewById(R.id.freightPaidByTheReceivingParty)
        Savebutton = findViewById(R.id.Savebutton)
        Backbutton = findViewById(R.id.Backbutton)






        val dbHelper = MyDatabaseHelper(this, "WaybillStore", 11)

//        创建数据库
//        val createDatabase: Button = findViewById(R.id.createDatabase)
//        createDatabase.setOnClickListener{
//            dbHelper.writableDatabase
//        }

//        保存按钮
        Savebutton.setOnClickListener {
            val ArrivalStationData = ArrivalStation.getText().toString()
            val consignorData = consignor.getText().toString()
            val consignorPhoneNumberData = consignorPhoneNumber.getText().toString()
            val consigneeData = consignee.getText().toString()
            val consigneePhoneNumberData = consigneePhoneNumber.getText().toString()
            val goodsNameData = goodsName.getText().toString()
            val numberOfPackagesData = numberOfPackages.getText().toString()
            val freightPaidByConsignorData = freightPaidByConsignor.getText().toString()
            val freightPaidByTheReceivingPartyData = freightPaidByTheReceivingParty.getText().toString()
            Log.d("EnterWaybillActivity", "consignorData is $consignorData")

            val db = dbHelper.writableDatabase
            val values1 = ContentValues().apply {
//                put("waybillNo", "J2000000")
                put("consignor",consignorData)
                put("consignorPhoneNumber", consignorPhoneNumberData)
                put("consignee",consigneeData )
                put("consigneePhoneNumber", consigneePhoneNumberData)
                put("transportationDepartureStation","沈阳" )
                put("transportationArrivalStation",ArrivalStationData )
                put("goodsDistributionAddress"," " )
                put("goodsName",goodsNameData )
                put("numberOfPackages",numberOfPackagesData )
                put("freightPaidByTheReceivingParty",freightPaidByTheReceivingPartyData )
                put("freightPaidByConsignor", freightPaidByConsignorData )
            }
//            保证到站信息，货物名称，件数不为空，若为空提示失败
            if (ArrivalStationData != "" && goodsNameData != "" && numberOfPackagesData != "") {
                db.insert("Waybill", null, values1)
//                清空界面录入项
                ArrivalStation.setText("")
                consignor.setText("")
                consignorPhoneNumber.setText("")
                consignee.setText("")
                consigneePhoneNumber.setText("")
                goodsName.setText("")
                numberOfPackages.setText("")
                freightPaidByConsignor.setText("")
                freightPaidByTheReceivingParty.setText("")
                Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(getApplicationContext(), "保存失败,有必填项为空", Toast.LENGTH_LONG).show()
            }
        }

//        查询调试
//        val queryData: Button = findViewById(R.id.queryData)
//        queryData.setOnClickListener {
//            val db = dbHelper.writableDatabase
//            val cursor = db.query("Waybill", null, null, null, null, null, null)
//            @SuppressLint("Range")
//            if (cursor.moveToFirst()) {
//                do {
//                    val waybillNo = cursor.getString(cursor.getColumnIndex("waybillNo"))
//                    val consignor = cursor.getString(cursor.getColumnIndex("consignor"))
//                    val consignorPhoneNumber = cursor.getString(cursor.getColumnIndex("consignorPhoneNumber"))
//                    val consignee = cursor.getString(cursor.getColumnIndex("consignee"))
//                    Log.d("MainActivity", "waybillNo is $waybillNo")
//                    Log.d("MainActivity", "consignor is $consignor")
//                    Log.d("MainActivity", "consignorPhoneNumber is $consignorPhoneNumber")
//                    Log.d("MainActivity", "consignee is $consignee")
//                } while (cursor.moveToNext())
//            }
//            cursor.close()
//        }

//        返回按钮
        Backbutton.setOnClickListener {
            finish()
        }
    }

    class MyDatabaseHelper(val context: Context, name: String, version: Int) :
        SQLiteOpenHelper(context, name, null, version) {
        private val createWaybill = "create table Waybill (" +
                " waybillNo integer primary key autoincrement," +
                "consignor text," +
                "consignorPhoneNumber text," +
                "consignee text," +
                "consigneePhoneNumber text," +
                "transportationDepartureStation text," +
                "transportationArrivalStation text," +
                "goodsDistributionAddress text," +
                "goodsName text," +
                "numberOfPackages," +
                "freightPaidByTheReceivingParty,"+
                "freightPaidByConsignor text)"

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(createWaybill)
            Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
            db.execSQL("drop table if exists Waybill")
            onCreate(db)
        }
    }


}

