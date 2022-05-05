package com.example.logisticsmanagementapp2

import android.app.Activity

//处理Activity的销毁，用于在某个Activity中直接退出整个程序
object ActivityBox {

    private val activities = ArrayList<Activity>()

    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun finishAll() {
        for (activity in activities) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activities.clear()
    }

}