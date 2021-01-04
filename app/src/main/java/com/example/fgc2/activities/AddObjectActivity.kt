package com.example.fgc2.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.fgc2.DB.Object
import com.example.fgc2.R
import com.example.fgc2.viewmodel.SettingModel
import kotlinx.android.synthetic.main.activity_add_object.*
import kotlinx.android.synthetic.main.deletethis.view.*
import kotlinx.android.synthetic.main.setting.view.TitleText

class AddObjectActivity : AppCompatActivity() {
    private val viewModel: SettingModel by lazy {
        ViewModelProvider(this, SettingModel.Factory(this.application))
            .get(SettingModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_object)
        var chainList = listOf<Object>()
        var chain = ""
        var chainName = ""
        var number = ""
        var type = false
        val typeObject = listOf("Опора", "Пролет")

        val chainText = MutableLiveData<String>()
        val nameText = MutableLiveData<String>()
        val numberText = MutableLiveData<String>()
        nameText.observe(this) {
            add_chain_name.DescribeText.text = it
            chainName = it
        }
        numberText.observe(this) {
            add_number.DescribeText.text = it
            number = it
        }
        chainText.observe(this) {
            add_chain.DescribeText.text = it
            chain = it
        }
        viewModel.getAllObjects().observe(this)
        {
            chainList = it
        }
        add_chain.setOnClickListener {
            chain_function(chainList.groupBy { it.chain }.map { (key, value) -> key }, chainText)
        }
        add_chain_name.setOnClickListener {
            chain_function(chainList.filter { it.chain == chain }.groupBy { it.name_chain }
                .map { (key, value) -> key ?: "" }, nameText)
        }
        add_number.setOnClickListener {
            chain_function(chainList.filter { (it.chain == chain) && (it.name_chain == chainName) }
                .map { it.number ?: "" }, numberText)
        }
        add_object_type.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder
                .setItems(
                    typeObject.toTypedArray()
                ) { _, which ->
                    add_object_type.setValueText(typeObject[which])
                    type = (which + 1) % 2 == 0
                    Toast.makeText(this,"$type",Toast.LENGTH_LONG).show()
                }
            builder.create()
            builder.show()
        }
        button_add.setOnClickListener {
            if (chainList.none { (it.chain == chain) && (it.name_chain == chainName) && (it.number == number) && (it.type == type) })
                viewModel.insert(
                    Object(
                        0,
                        add_chain.getValueText(),
                        add_chain_name.getValueText(),
                        type,
                        add_number.getValueText()
                    )
                )
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        button_delete.setOnClickListener {
            viewModel.deleteObject(chainList.filter { (it.chain == chain) && (it.name_chain == chainName) && (it.number == number) }[0])
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun chain_function(list: List<String>, set: MutableLiveData<String>) {
        var chain = ""
        val chainList = list
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle("Элементы:")
            .setItems(
                chainList.toTypedArray()
            ) { _, which ->
                chain = chainList[which]
                set.value = chain
            }
            .setNeutralButton("Добавить новый") { _, _ ->
                val builder = AlertDialog.Builder(this)
                val view = EditText(this)
                builder
                    .setTitle("Название")
                    .setView(view)
                    .setPositiveButton("OK") { _, _ ->
                        chain = view.text.toString()
                        set.value = chain
                    }
                builder.create()
                builder.show()
            }
        builder.create()
        builder.show()
    }
}