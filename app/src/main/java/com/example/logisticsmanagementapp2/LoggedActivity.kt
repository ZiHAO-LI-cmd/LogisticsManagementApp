package com.example.logisticsmanagementapp2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread

class LoggedActivity : AppCompatActivity() {

    lateinit var timeText : TextView
    lateinit var timeChangeReceiver : TimeChangeReceiver
    lateinit var nameText : TextView
    lateinit var passwordText : TextView
    lateinit var queryXMLbtn : Button
    lateinit var queryJSONbtn : Button
    lateinit var enterWaybillbutton : Button
    lateinit var queryLOCALbtn : Button
    lateinit var switchUSERbtn : Button
    lateinit var Finishbtn : Button

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTime(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分")
        val formatted = current.format(formatter)
        timeText.text = "当前时间是"+formatted.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityBox.addActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged)

//        时间文本
        timeText = findViewById(R.id.timeText)
        getTime()   // 第一次获取时间

//        取出intent中的传递的数据，并显示在两个文本框
        nameText = findViewById(R.id.nameText)
        val name = intent.getStringExtra("name")
        nameText.text = "我是$name"
        passwordText = findViewById(R.id.passwordText)
        val password = intent.getStringExtra("password")
        passwordText.text = "我的密码是$password"

//        监听时间变化
//        创建一个IntentFilter的实例，并给它添加了一个值为android.intent.action.TIME_TICK的action
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.TIME_TICK")
        timeChangeReceiver = TimeChangeReceiver()
        registerReceiver(timeChangeReceiver, intentFilter)


//        查询公司运单XML按钮
        queryXMLbtn = findViewById(R.id.queryXMLbtn)
        queryXMLbtn.setOnClickListener {
            val intent = Intent(this, XmlActivity::class.java)
            startActivity(intent)
        }

//        查询公司运单JSON按钮
        queryJSONbtn = findViewById(R.id.queryJSONbtn)
        queryJSONbtn.setOnClickListener {
            val intent = Intent(this, JsonActivity::class.java)
            startActivity(intent)
        }

//        录入运单按钮
        enterWaybillbutton = findViewById(R.id.enterWaybillbutton)
        enterWaybillbutton.setOnClickListener {
            val intent = Intent(this, EnterWaybillActivity::class.java)
            startActivity(intent)
        }

//        查询本机运单按钮
        queryLOCALbtn = findViewById(R.id.queryLOCALbtn)
        queryLOCALbtn.setOnClickListener {
            val intent = Intent(this, LocalWaybillActivity::class.java)
            startActivity(intent)
        }

//        切换用户按钮
        switchUSERbtn = findViewById(R.id.switchUSERbtn)
        switchUSERbtn.setOnClickListener {
            finish()    //返回上一个activity
        }

//        退出按钮
        Finishbtn = findViewById(R.id.Finishbtn)
        Finishbtn.setOnClickListener {
            ActivityBox.finishAll()
        }
    }

//    时间监听类
    inner class TimeChangeReceiver : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context, intent: Intent) {
            Toast.makeText(context, "Time has changed", Toast.LENGTH_SHORT).show()
            getTime()   //更新时间
        }
    }


}