package com.example.fgc2.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fgc2.DB.Object
import com.example.fgc2.DB.Result
import com.example.fgc2.DB.Setting
import com.example.fgc2.R
import com.example.fgc2.activities.MainActivity
import com.example.fgc2.fragments.MainFragment
import com.example.fgc2.viewmodel.SettingModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SetsPage1(
    val title: String,
    val mainSets: List<String>,
    val mainTitle: String,
    val pageStart: Int,
    val k: Double
) : Fragment() {
    var setsList = mutableListOf<Object>()
    var normList = mutableListOf<SettingView>()
    var x = false
    var editSet = mutableListOf<Setting>()
    var list = mutableListOf<Setting>()
    var result_list = mutableListOf<Setting>()
    var page = pageStart
    val chosen = mutableMapOf<Int, Int>()
    lateinit var recycler: RecyclerView
    lateinit var mainSetting: SetsControl
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

//    override fun onResume() {
//        super.onResume()
//        x = (activity as MainActivity).x
//        if (list.isNotEmpty()) {
//            recycler.adapter =
//                customAdapter(list.filter { (it.object_type == x) and (it.page == page) })
//        }
//    }
    fun default()
    {
        setPages(0)
        chosen.clear()
        result_list.clear()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_sets_page1, container, false)
        v.findViewById<FloatingActionButton>(R.id.page1_calc).setOnClickListener {
            calcResults()
        }
        mainSetting = v.findViewById(R.id.page1_mainSetting)
        if (mainSets.isNotEmpty())
            mainSetting.setValueText(mainSets.first())
        else
            mainSetting.visibility = View.GONE
        mainSetting.setTitleText(mainTitle)
        mainSetting.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder
                .setItems(
                    mainSets.toTypedArray()
                ) { _, which ->
                    setPages(which)
                }
            builder.create()
            builder.show()
        }
        recycler = v.findViewById(R.id.page1_recycler)
        v.findViewById<TextView>(R.id.page1_title).text = title
        viewModel.getAllSettings().observe(viewLifecycleOwner, { sets ->
            resetAdapter(sets)
            editSet = sets.toMutableList()
        })
        recycler.layoutManager = LinearLayoutManager(context)
        return v
    }

    private fun setPages(which: Int) {
        if(mainSets.isNotEmpty())
        mainSetting.setValueText(mainSets[which])
        (activity as MainActivity).deleteSettingFromPage(page)
        page = which + pageStart
        chosen.clear()
        resetAdapter(editSet)
    }

    private fun resetAdapter(sets: List<Setting>) {
        result_list.clear()
        val adaptSet =
            sets.filter { (it.object_type == viewModel.type.value) and (it.page == page) }
                .toMutableList()
        list = sets.toMutableList()
        if (sets.isNotEmpty()) {
            recycler.adapter = customAdapter(adaptSet)
        }
    }

    fun calcResults() {
        Toast.makeText(
            context,
            ((if (result_list.size > 0) result_list.minOf { it.grade } else 4) * k * 100).toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun makeResults(id: Int) {
        if (id > 0)
            viewModel.insertAllResults(result_list
                .map { Result(id, 0, it, "", "") })
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
            val oneSet =
                setList.filter { it.title == names[position] }.sortedByDescending { it.grade }
            holder.TitleText.text = oneSet[0].title
            holder.DescribeText.text = oneSet[0].state
            if (chosen.containsKey(position))
                holder.DescribeText.text = oneSet[chosen[position]!!].state
            holder.itemView.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder
                    .setItems(
                        oneSet.map { it.state }.toTypedArray()
                    ) { _, which ->
                        holder.DescribeText.text = oneSet[which].state
                        result_list.removeIf { it.title == oneSet[which].title }
                        if (oneSet[which].grade < 4) {
                            result_list.add(
                                oneSet[which]
                            )
                        }
                        chosen[position] = which

                        (activity as MainActivity).addSetting(
                            Pair(
                                page,
                                result_list.minOfOrNull { it.grade }?:4)
                        )
                    }
                builder.create()
                builder.show()
            }


        }

        override fun getItemCount() = names.size
    }
}