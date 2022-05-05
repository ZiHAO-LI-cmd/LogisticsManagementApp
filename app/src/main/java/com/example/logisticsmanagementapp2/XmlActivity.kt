package com.example.logisticsmanagementapp2

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import kotlin.concurrent.thread

class XmlActivity : AppCompatActivity() {
    val billList = ArrayList<String>()
    lateinit var backBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityBox.addActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xml)
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
                    .url("http://60.12.122.142:6080/simulated-Waybills-db.xml")
//                    .url("https://www.baidu.com")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null) {
                    parseXMLWithPull(responseData)
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


    fun parseXMLWithPull(xmlData: String) {
        try {
            val factory = XmlPullParserFactory.newInstance()
            val xmlPullParser = factory.newPullParser()
            xmlPullParser.setInput(StringReader(xmlData))
            var eventType = xmlPullParser.eventType
            var waybillNo = ""  //订单号
            var consignor = ""  //发货人
            var consignorPhoneNumber = ""   //发货人手机
            var consignee = ""  //收货人
            var consigneePhoneNumber = ""   //收货人手机
            var transportationDepartureStation = "" //出货地
            var transportationArrivalStation = ""   //目的地
            var goodsDistributionAddress = ""   //目的地具体地址
            var goodsName = ""  //商品名
            var numberOfPackages = ""   //数量
            var freightPaidByTheReceivingParty = "" //收货方运费
            var freightPaidByConsignor = "" //寄货方运费



            while (eventType != XmlPullParser.END_DOCUMENT) {
                val nodeName = xmlPullParser.name
                when (eventType) {
//                        开始解析某个节点
                    XmlPullParser.START_TAG -> {
                        when (nodeName) {
                            "waybillNo" -> waybillNo = xmlPullParser.nextText()
                            "consignor" -> consignor = xmlPullParser.nextText()
                            "consignorPhoneNumber" -> consignorPhoneNumber = xmlPullParser.nextText()
                            "consignee" -> consignee = xmlPullParser.nextText()
                            "consigneePhoneNumber" -> consigneePhoneNumber = xmlPullParser.nextText()
                            "transportationDepartureStation" -> transportationDepartureStation = xmlPullParser.nextText()
                            "transportationArrivalStation" -> transportationArrivalStation = xmlPullParser.nextText()
                            "goodsDistributionAddress" -> goodsDistributionAddress = xmlPullParser.nextText()
                            "goodsName" -> goodsName = xmlPullParser.nextText()
                            "numberOfPackages" -> numberOfPackages = xmlPullParser.nextText()
                            "freightPaidByTheReceivingParty" -> freightPaidByTheReceivingParty = xmlPullParser.nextText()
                            "freightPaidByConsignor" -> freightPaidByConsignor = xmlPullParser.nextText()
                        }
                    }
//                        完成解析某个节点
                    XmlPullParser.END_TAG -> {
                        if ("waybillRecord" == nodeName) {
//                            Log.d("MainActivity","waybillNo is $waybillNo")
//                            Log.d("MainActivity","consignor is $consignor")
//                            Log.d("MainActivity","consignorPhoneNumber is $consignorPhoneNumber")
//                            Log.d("MainActivity","consignee is $consignee")
//                            Log.d("MainActivity","consigneePhoneNumber is $consigneePhoneNumber")
//                            Log.d("MainActivity","transportationDepartureStation is $transportationDepartureStation")
//                            Log.d("MainActivity","transportationArrivalStation is $transportationArrivalStation")
//                            Log.d("MainActivity","goodsDistributionAddress is $goodsDistributionAddress")
//                            Log.d("MainActivity","goodsName is $goodsName")
//                            Log.d("MainActivity","numberOfPackages is $numberOfPackages")
//                            Log.d("MainActivity","freightPaidByTheReceivingParty is $freightPaidByTheReceivingParty")
//                            Log.d("MainActivity","freightPaidByConsignor is $freightPaidByConsignor")
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
                            Log.d("MainActivity",billList.size.toString())
                        }
                    }
//                    printData()

                }
                eventType = xmlPullParser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}