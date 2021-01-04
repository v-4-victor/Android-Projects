package com.example.fgc2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fgc2.DB.Result
import com.example.fgc2.DB.Setting
import com.example.fgc2.R
import com.example.fgc2.activities.MainActivity
import com.example.fgc2.viewmodel.FragmentModel

class EndPage : Fragment() {
    private lateinit var textview: TextView
    private val listOfK = mapOf(
        0 to 0.184,
        2 to 0.135,
        16 to 0.033,
        17 to 0.033,
        18 to 0.003,
        19 to 0.003,
        20 to 0.0
    )
    val newModel: FragmentModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_end, container, false)
        v.findViewById<Button>(R.id.totalObjectResult).setOnClickListener {
            val list = mutableListOf<Setting>()
            newModel.resultList.forEach { list.addAll(it) }
            newModel.resultList.forEach { it.clear() }
            newModel.chosenMap.forEach { it.clear() }
            val action = (activity as MainActivity)
            action.calc(list.map { Result(action.id_object, action.id_number, it, "", "") })
            action.id_number++
            action.clearList()
        }
        val sets = (activity as MainActivity).getAllSets()
        textview = v.findViewById<TextView>(R.id.ITS_text)
        textview.text = calc(sets).toString()
        return v
    }

    override fun onResume() {
        super.onResume()
        val sets = (activity as MainActivity).getAllSets()
        textview.text = calc(sets).toString()
    }

    fun calc(sets: Map<Int, Int>): String {
        var ITS = 0.0
        val keys = listOfK.keys.toList().sorted()
        val newSet = sets.toSortedMap()
        for (i in 0..keys.size - 2) {
            val tmp = newSet.filterKeys { (it >= keys[i]) and (it < keys[i + 1]) }
            ITS += if (tmp.isEmpty())
                (listOfK[keys[i]] ?: 0.0) * 4.0
            else
                (listOfK[keys[i]] ?: 0.0) * tmp.minOf { it.value }
        }
        if ((newSet[18] ?: 4 < 4) or (newSet[19] ?: 4 < 4))
            ITS = 0.26 * 4 * 0.391
        return (ITS * 100 / 4.0 / 0.391).toString()
        //return keys.toString()
        //return ITS.toString()
    }
}