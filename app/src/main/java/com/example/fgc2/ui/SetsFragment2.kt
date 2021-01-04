package com.example.fgc2.ui

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fgc2.DB.MyDatabaseHelper
import com.example.fgc2.DB.Object
import com.example.fgc2.DB.Result
import com.example.fgc2.DB.Setting
import com.example.fgc2.R
import com.example.fgc2.activities.MainActivity
import com.example.fgc2.viewmodel.SettingModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SetsFragment2(val title: String, val page: Int) : Fragment() {
    var setsList = mutableListOf<Object>()
    var normList = mutableListOf<SettingView>()
    var x = false
    var list = mutableListOf<Setting>()
    var result_list = mutableListOf<Setting>()
    lateinit var recycler: RecyclerView

    private val viewModel: SettingModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, SettingModel.Factory(activity.application))
            .get(SettingModel::class.java)
    }

    data class SettingView(
        val name: String,
        val states: List<String>,
        val id_list: List<Int>,
        val grades: List<Int>,
        val number: Boolean
    )

    override fun onResume() {
        super.onResume()
        x = (activity as MainActivity).x
        if (list.isNotEmpty()) {
            recycler.adapter =
                customAdapter(list.filter { (it.object_type == x) and (it.page == page) })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_sets2, container, false)
        v.findViewById<FloatingActionButton>(R.id.makeResult).setOnClickListener {
            calcResults()
        }
        recycler = v.findViewById(R.id.RecyclerView)
        v.findViewById<TextView>(R.id.titlePage).text = title
        viewModel.getAllObjects().observe(viewLifecycleOwner, Observer<List<Object>> { sets ->
        })
        viewModel.getAllSettings().observe(viewLifecycleOwner, Observer<List<Setting>> { sets ->
            val editSet =
                sets.filter { (it.object_type == viewModel.type.value) and (it.page == page) }
            val tmp = editSet.groupBy { it.title }
            result_list = editSet.filter { it.grade == 4 }.toMutableList()
            list = sets.toMutableList()
            if (sets.isNotEmpty()) {
                recycler.adapter = customAdapter(editSet)
            }
        })
        recycler.layoutManager = LinearLayoutManager(context)
        return v
    }

    fun calcResults() {
        Toast.makeText(
            context,
            (result_list.minOf { it.grade } * 0.184 * 100).toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun makeResults(id: Int) {
        if (id > 0)
            viewModel.insertAllResults(result_list.filter { it.object_type == x && it.page == page }
                .map { Result(id,0, it, "", "") })
        else
            Toast.makeText(context, "Объект не выбран", Toast.LENGTH_SHORT).show()
    }

    inner class customAdapter(private val setList: List<Setting>) :
        RecyclerView.Adapter<customAdapter.controlHolder>() {
        val names = setList.distinctBy { it.title }.sortedBy { it.title }.map { it.title }
        inner class controlHolder(view: View) : RecyclerView.ViewHolder(view) {
            var TitleText = view.findViewById<TextView>(R.id.TitleText)
            var DescribeText = view.findViewById<TextView>(R.id.DescribeText)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): controlHolder {
            val inflater = LayoutInflater.from(parent.context)
            return controlHolder(inflater.inflate(R.layout.deletethis, parent, false))
        }


        override fun onBindViewHolder(holder: controlHolder, position: Int) {
            val oneSet = setList.filter { it.title == names[position] }.sortedByDescending { it.grade }
            holder.TitleText.text = oneSet[0].title
            holder.DescribeText.text = oneSet[0].state
            holder.itemView.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder
                    .setItems(
                        oneSet.map { it.state }.toTypedArray()
                    ) { _, which ->
                        holder.DescribeText.text = oneSet[which].state
                        result_list.removeIf { it.title == oneSet[which].title}
                        result_list.add(
                            oneSet[which]
                        )

                    }
                builder.create()
                builder.show()
            }


        }

        override fun getItemCount() = names.size
    }
}