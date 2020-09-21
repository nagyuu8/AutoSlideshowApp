package com.example.autoslideshowapp

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var mTimer: Timer? = null
    private var mHandler = Handler()
    private val PERMISSIONS_REQUEST_CODE = 100
    private var resumuOnOff = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// Android 6.0以降の場合********************
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                val resolver = contentResolver
                val cursor = resolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                        null, // 項目(null = 全項目)
                        null, // フィルタ条件(null = フィルタなし)
                        null, // フィルタ用パラメータ
                        null // ソート (null ソートなし)
                )
                if (cursor!!.moveToFirst()) {
                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id=cursor.getLong(fieldIndex)
                    val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    imageView.setImageURI(imageUri)
                    move_button.setOnClickListener {
                        if (cursor.isLast) {
                            cursor.moveToFirst()
                            val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                            val id=cursor.getLong(fieldIndex)
                            val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                            imageView.setImageURI(imageUri)
                        } else {
                            cursor.moveToNext()
                            val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                            val id=cursor.getLong(fieldIndex)
                            val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                            imageView.setImageURI(imageUri)
                        }
                    }
                    return_button.setOnClickListener {
                        if (cursor.isFirst) {
                            cursor.moveToLast()
                            val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                            val id=cursor.getLong(fieldIndex)
                            val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                            imageView.setImageURI(imageUri)
                        } else {
                            cursor.moveToPrevious()
                            val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                            val id=cursor.getLong(fieldIndex)
                            val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                            imageView.setImageURI(imageUri)
                        }
                    }
                    resume_button.setOnClickListener {
                        if (resumuOnOff){
                            resume_button.text = "再生"
                            resumuOnOff = false
                            move_button.isClickable = true
                            return_button.isClickable = true
                            if(mTimer != null){
                                mTimer!!.cancel()
                                mTimer = null
                            }
                        }else{
                            resume_button.text = "停止"
                            resumuOnOff = true
                            move_button.isClickable = false
                            return_button.isClickable = false
                            if(mTimer == null){
                            //機能2 2秒ごとにスライドを送る。
                            mTimer = Timer()
                            mTimer!!.schedule(object :TimerTask(){
                                override fun run() {

                                    if (cursor!!.isLast) {
                                        cursor!!.moveToFirst()
                                        val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                        val id=cursor.getLong(fieldIndex)
                                        val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                                        mHandler.post{
                                            imageView.setImageURI(imageUri)
                                        }

                                    } else {
                                        cursor!!.moveToNext()
                                        val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                        val id=cursor.getLong(fieldIndex)
                                        val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                                        mHandler.post{
                                            imageView.setImageURI(imageUri)
                                        }

                                    }
                                }

                            },0,2000)
                        }

                        }
                    }
                }
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }

        } else { // Android 5系以下の場合*******************************************************//todo 後でコピーする
            val resolver=contentResolver
            val cursor=resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                    null, // 項目(null = 全項目)
                    null, // フィルタ条件(null = フィルタなし)
                    null, // フィルタ用パラメータ
                    null // ソート (null ソートなし)
            )
            if (cursor!!.moveToFirst()) {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id=cursor.getLong(fieldIndex)
                val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imageView.setImageURI(imageUri)
                move_button.setOnClickListener {
                    if (cursor.isLast) {
                        cursor.moveToFirst()
                        val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                        val id=cursor.getLong(fieldIndex)
                        val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                        imageView.setImageURI(imageUri)
                    } else {
                        cursor.moveToNext()
                        val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                        val id=cursor.getLong(fieldIndex)
                        val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                        imageView.setImageURI(imageUri)
                    }
                }
                return_button.setOnClickListener {
                    if (cursor.isFirst) {
                        cursor.moveToLast()
                        val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                        val id=cursor.getLong(fieldIndex)
                        val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                        imageView.setImageURI(imageUri)
                    } else {
                        cursor.moveToPrevious()
                        val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                        val id=cursor.getLong(fieldIndex)
                        val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                        imageView.setImageURI(imageUri)
                    }
                }
                resume_button.setOnClickListener {
                    if (resumuOnOff) {
                        resume_button.text="再生"
                        resumuOnOff=false
                        move_button.isClickable=true
                        return_button.isClickable=true
                        if (mTimer != null) {
                            mTimer!!.cancel()
                            mTimer=null
                        }
                    } else {
                        resume_button.text="停止"
                        resumuOnOff=true
                        move_button.isClickable=false
                        return_button.isClickable=false
                        if (mTimer == null) {
                            //機能2 2秒ごとにスライドを送る。
                            mTimer=Timer()
                            mTimer!!.schedule(object : TimerTask() {
                                override fun run() {

                                    if (cursor!!.isLast) {
                                        cursor!!.moveToFirst()
                                        val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                        val id=cursor.getLong(fieldIndex)
                                        val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                                        mHandler.post {
                                            imageView.setImageURI(imageUri)
                                        }

                                    } else {
                                        cursor!!.moveToNext()
                                        val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                        val id=cursor.getLong(fieldIndex)
                                        val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                                        mHandler.post {
                                            imageView.setImageURI(imageUri)
                                        }

                                    }
                                }

                            }, 0, 2000)
                        }

                    }
                }
            }
        }// Android 5系以下の場合*******************************************************
    }

    private fun getContentsInfo() {
        // 画像の情報を取得する
        val resolver = contentResolver
        val cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
        )
        if (cursor!!.moveToFirst()) {
            // indexからIDを取得し、そのIDから画像のURIを取得する
            val fieldIndex=cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id=cursor.getLong(fieldIndex)
            val imageUri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            imageView.setImageURI(imageUri)
        }

        cursor.close()



    }

}






