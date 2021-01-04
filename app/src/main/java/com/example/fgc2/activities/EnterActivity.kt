package com.example.fgc2.activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.fgc2.R
import kotlinx.android.synthetic.main.activity_enter.*
import java.io.File


class EnterActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)
        val file = File(filesDir, "acc.txt")

        if(file.length() > 0)
        {
            val int = Intent(this, MainActivity::class.java)
            startActivity(int)
        }
        button2.setOnClickListener {
            if (editTextTextPersonName4.text.toString() == "" || editTextTextPersonName5.text.toString() == "")
                Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show()
            else {
                val int = Intent(this, MainActivity::class.java)
                int.putExtra("Name", editTextTextPersonName4.text.toString())
                int.putExtra("Rank", editTextTextPersonName5.text.toString())
                file.writeText(editTextTextPersonName4.text.toString() + "\n" + editTextTextPersonName5.text.toString())
                startActivity(int)
            }
        }
    }

}