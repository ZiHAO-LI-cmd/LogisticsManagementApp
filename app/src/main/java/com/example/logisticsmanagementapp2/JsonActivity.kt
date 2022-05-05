package com.example.logisticsmanagementapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.concurrent.thread

class JsonActivity : AppCompatActivity() {
    val billList = ArrayList<String>()
    lateinit var backBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityBox.addActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json)

        sendRequestWithOkHttp()

//        返回按钮
        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
    }

    fun sendRequestWithOkHttp() {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://60.12.122.142:6080/simulated-Waybills-db.json")
//                    .url("https://www.baidu.com")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null) {
                    parseJSONWithJSONObject(responseData)
                }
                printData()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun printData(){
        Log.d("MainActivity","总数量"+billList.size.toString())
//        使用runOnUiThread来在主进程中进行Ui更新
        runOnUiThread {
            val listView: ListView = findViewById(R.id.listView)
            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, billList)
            listView.adapter = adapter
        }
    }

    private fun parseJSONWithJSONObject(jsonData: String) {
        try {


            val jsonObject = JSONObject(jsonData)
            val jsonArray = jsonObject.getJSONArray("waybillRecord")
//               val jsonArray=JSONArray(jsonData)
            jsonObject.get("waybillRecord")
            for (i in 0 until jsonArray.length()) {
//                val jsonObject = jsonObject.getJSONObject()
                val jsonObject=jsonArray.getJSONObject(i)

                var waybillNo = jsonObject.getString("waybillNo")
                var consignor = jsonObject.getString("consignor")
                var consignorPhoneNumber = jsonObject.getString("consignorPhoneNumber")
                var consignee = jsonObject.getString("consignee")
                var consigneePhoneNumber = jsonObject.getString("consigneePhoneNumber")
                var transportationDepartureStation = jsonObject.getString("transportationDepartureStation")
                var transportationArrivalStation = jsonObject.getString("transportationArrivalStation")
                var goodsDistributionAddress = jsonObject.getString("goodsDistributionAddress")
                var goodsName = jsonObject.getString("goodsName")
                var numberOfPackages = jsonObject.getString("numberOfPackages")
                var freightPaidByTheReceivingParty = jsonObject.getString("freightPaidByTheReceivingParty")
                var freightPaidByConsignor = jsonObject.getString("freightPaidByConsignor")
//                Log.d("MainActivity", "id is $waybillNo")
//                Log.d("MainActivity", "name is $consignee")
//                Log.d("MainActivity", "version is $freightPaidByConsignor")
                val billNo = "No: ${waybillNo.toString().trim()}"   //No: X2000001
                val billTrace =                                     //乌鲁木齐 - 丹东  汽车配件 3件  到付298元
                    "${
                        transportationArrivalStation.toString().trim()
                    } - ${transportationDepartureStation.toString().trim()}  ${
                        goodsName.toString().trim()
                    } ${
                        numberOfPackages.toString().trim()
                    }件  到付${freightPaidByTheReceivingParty.toString().trim()}元"
                val billName =                                      //收货人：洛天賜(13044784582)
                    "收货人：${consignee.toString().trim()}(${consigneePhoneNumber.toString().trim()})"
                Log.d("MainActivity",billNo)
                Log.d("MainActivity",billTrace)
                Log.d("MainActivity",billName)

                billList.add(billNo + "\n" + billTrace + "\n" + billName)

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}