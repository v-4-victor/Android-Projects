package com.example.fgc2.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fgc2.Excel.FileUtil
import com.example.fgc2.R
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.File

class SettingsActivity : AppCompatActivity() {
    lateinit var file:File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        file = File(filesDir, "path.txt")
        if(file.length() > 0)
            path.setValueText(file.readText())
        val file2 = File(filesDir, "acc.txt")
        if(file2.length() > 0)
        {
            val lines = file2.readLines()
            account.setTitleText(account.getTitleText() + lines[0])
            account.setValueText(account.getValueText() + lines[1])
        }
        path.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(intent, 1)
        }
        account.setOnClickListener {
            file2.delete()
            val int = Intent(this, EnterActivity::class.java)
            startActivity(int)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val url = data?.data
            val f223 =
                if (url != null) File(FileUtil.getFullPathFromTreeUri(url, this) ?: "")
                else File(file.readText())
            file.writeText(f223.absolutePath)
            path.setValueText(f223.absolutePath)
        }
    }
}