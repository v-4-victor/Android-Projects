package com.example.fgc2.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fgc2.DB.Setting
import com.example.fgc2.R
import com.example.fgc2.viewmodel.SettingModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AddSettingActivity() : FragmentActivity() {

    var statesList = mutableListOf<String>()
    private val viewModel: SettingModel by lazy {
        ViewModelProvider(this, SettingModel.Factory(application))
            .get(SettingModel::class.java)
    }
    lateinit var setsList: LiveData<List<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var typeObject = false
        if (intent.extras != null) {
            typeObject = intent.extras!!.get("F") as Boolean
            //Toast.makeText(this,"Hi $typeObject",Toast.LENGTH_SHORT).show()
        }
        val v = setContentView(R.layout.add_setting)
        val recycler = findViewById<RecyclerView>(R.id.recyclerView_states)
        val addStateButton = findViewById<FloatingActionButton>(R.id.add_new_state_button)
        val name = findViewById<EditText>(R.id.editTextTextPersonName)
        val check = findViewById<CheckBox>(R.id.checkBox)
        var type = false
        check.setOnCheckedChangeListener { buttonView, isChecked ->
            addStateButton.isEnabled = !isChecked
            type = isChecked
        }
        addStateButton.setOnClickListener {
            val text = EditText(this)
            val builder = AlertDialog.Builder(this)
            builder
                .setTitle("Введите состояние:")
                .setView(text)
                .setPositiveButton("OK") { _, _ ->
                    statesList.add(text.text.toString())
                    recycler.adapter = customAdapter(statesList)
                }
            builder.create()
            builder.show()

        }
        val button = findViewById<Button>(R.id.save_button)
        button.setOnClickListener {
//            viewModel.insertAllSettings(statesList.map { Setting(0, name.text.toString(), it, type, typeObject)})
//            if (type)
//                viewModel.insert(
//                    Setting(0, name.text.toString(), "", type, typeObject)
//                )

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        recycler.layoutManager = LinearLayoutManager(this)
        return v
    }

    inner class customAdapter(private val setList: List<String>) :
        RecyclerView.Adapter<customAdapter.controlHolder>() {

        inner class controlHolder(view: View) : RecyclerView.ViewHolder(view) {
            var TitleText = view.findViewById<TextView>(R.id.TitleText)
            var DescribeText = view.findViewById<TextView>(R.id.DescribeText)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): controlHolder {
            val inflater = LayoutInflater.from(parent.context)
            return controlHolder(inflater.inflate(R.layout.deletethis, parent, false))
        }

        override fun onBindViewHolder(holder: controlHolder, position: Int) {
            holder.TitleText.text = setList[position]
            holder.DescribeText.text = ""
        }

        override fun getItemCount() = setList.size
    }

}