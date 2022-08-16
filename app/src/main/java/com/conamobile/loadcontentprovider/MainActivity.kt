package com.conamobile.loadcontentprovider

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.insertButton).setOnClickListener {
            onClickAddDetails()
        }
        findViewById<Button>(R.id.loadButton).setOnClickListener {
            onClickShowDetails()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        return true
    }

    private fun onClickAddDetails() {
        val values = ContentValues()
        values.put(MyContentProvider.name,
            (findViewById<View>(R.id.textName) as EditText).text.toString())
        contentResolver.insert(MyContentProvider.CONTENT_URI, values)
        Toast.makeText(baseContext, "New Record Inserted", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("Range", "Recycle", "SetTextI18n")
    fun onClickShowDetails() {
        val resultView = findViewById<View>(R.id.res) as TextView
        val cursor = contentResolver.query(Uri.parse("content://com.demo.user.provider/users"),
            null,
            null,
            null,
            null)

        if (cursor!!.moveToFirst()) {
            val strBuild = StringBuilder()
            while (!cursor.isAfterLast) {
                strBuild.append("""
      
    ${cursor.getString(cursor.getColumnIndex("id"))}-${cursor.getString(cursor.getColumnIndex("name"))}
    """.trimIndent())
                cursor.moveToNext()
            }
            resultView.text = strBuild
        } else {
            resultView.text = "No Records Found"
        }
    }
}