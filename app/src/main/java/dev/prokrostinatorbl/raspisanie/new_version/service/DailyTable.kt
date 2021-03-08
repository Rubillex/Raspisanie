package dev.prokrostinatorbl.raspisanie.new_version.service

import android.R
import android.app.ActionBar
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RelativeLayout


class DailyTable : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(WINDOW_SERVICE) as WindowManager
        val params = WindowManager.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,  // Ширина экрана
                FrameLayout.LayoutParams.WRAP_CONTENT,  // Высота экрана
                WindowManager.LayoutParams.TYPE_PHONE,  // Говорим, что приложение будет поверх других. В поздних API > 26, данный флаг перенесен на TYPE_APPLICATION_OVERLAY
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  // Необходимо для того чтобы TouchEvent'ы в пустой области передавались на другие приложения
                PixelFormat.TRANSLUCENT) // Само окно прозрачное


        // Задаем позиции для нашего Layout

        // Задаем позиции для нашего Layout
        params.gravity = Gravity.TOP or Gravity.RIGHT
        params.x = 0
        params.y = 0

        // Отображаем наш Layout

        // Отображаем наш Layout
        val rootView = LayoutInflater.from(this).inflate(R.layout.activity_list_item, null) as RelativeLayout

        manager.addView(rootView, params)
    }

}