package com.example.fgc2.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fgc2.DB.Object
import com.example.fgc2.DB.Result
import com.example.fgc2.DB.Setting
import com.example.fgc2.R
import com.example.fgc2.activities.MainActivity
import com.example.fgc2.viewmodel.FragmentModel
import com.example.fgc2.viewmodel.SettingModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SetsPageWork : Fragment() {
    var x = false
    var editSet = mutableListOf<Setting>()
    var list = mutableListOf<Setting>()
    var result_list = mutableListOf<Setting>()
    var page = 0
    var chosen = mutableMapOf<Int, Int>()

    lateinit var title: String
    lateinit var mainSets: List<String>
    lateinit var mainTitle: String
    var pageStart = 0
    var k = 0.0

    lateinit var recycler: RecyclerView
    lateinit var mainSetting: SetsControl
    var myId = 0
    private val newModel: FragmentModel by activityViewModels()
    companion object{
        fun createMe(id:Int): SetsPageWork {
            val f = SetsPageWork()
            val band = Bundle()
            band.putInt("id", id)
            f.arguments = band
            return f
        }
    }

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
        newModel.chosenMap[myId - 1].clear()
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
        this.arguments?.let {
            myId = it.getInt("id")
        }
        chosen = newModel.chosenMap[myId - 1]
        newModel.pagesData[myId - 1].also {
            title = it.title
            mainSets = it.mainSettingStates
            mainTitle = it.mainTitle
            pageStart = it.startPage
            page = pageStart
            k = it.k
        }

        mainSetting = v.findViewById<SetsControl>(R.id.page1_mainSetting)
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
        newModel.getAllSettings.observe(viewLifecycleOwner, { sets ->
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
        newModel.chosenMap[myId - 1].clear()
        resetAdapter(editSet)
    }

    private fun resetAdapter(sets: List<Setting>) {
        result_list.clear()
        val adaptSet =
            sets.filter { (it.page == page) }
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
                        newModel.chosenMap[myId - 1][position] = which
                        newModel.resultList[myId - 1].removeIf { it.title == oneSet[which].title }
                        if (oneSet[which].grade < 4) {
                            newModel.resultList[myId - 1].add(
                                oneSet[which]
                            )
                        }
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