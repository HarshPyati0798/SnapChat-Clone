package com.example.android.snapchatclone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.net.HttpURLConnection
import java.net.URL

class ViewSnapsActivity : AppCompatActivity() {
    var snap: ImageView? = null
    var message: TextView? = null
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snaps)

        snap = findViewById(R.id.snapView)

        message = findViewById(R.id.messageTextView)
        message?.text = intent.getStringExtra("message")
        Log.i("ImageURL", intent.getStringExtra("imageURL"))
        val url: String = intent.getStringExtra("imageURL")
        val myTask = DownloadImageTask()
        val myImage: Bitmap
        try {
            myImage = myTask.execute(url).get()
            snap?.setImageBitmap(myImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    inner class DownloadImageTask : AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg urls: String): Bitmap? {
            val url: URL
            val httpURLConnection: HttpURLConnection
            try {
                url = URL(urls[0])
                httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.connect()
                val stream = httpURLConnection.inputStream
                return BitmapFactory.decodeStream(stream)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser!!.uid).child("snaps").child(intent.getStringExtra("snapKey")).removeValue()
        FirebaseStorage.getInstance().getReference().child("images").child(intent.getStringExtra("imageName")).delete()
    }


}
