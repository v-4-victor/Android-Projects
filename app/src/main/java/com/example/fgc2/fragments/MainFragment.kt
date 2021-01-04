package com.example.fgc2.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.fgc2.DB.Object
import com.example.fgc2.Excel.ReadExcel
import com.example.fgc2.R
import com.example.fgc2.ui.SetsControl
import com.example.fgc2.activities.AddSettingActivity
import com.example.fgc2.activities.MainActivity

import com.example.fgc2.viewmodel.SettingModel
import kotlinx.android.synthetic.main.deletethis.view.*
import kotlinx.android.synthetic.main.setting.view.*
import kotlinx.android.synthetic.main.setting.view.DescribeText
import kotlinx.android.synthetic.main.setting.view.TitleText

class MainFragment : Fragment() {
    private lateinit var viewFrag: View
    private lateinit var chains:SetsControl
    private lateinit var chain_name :SetsControl
    private lateinit var object_type :SetsControl
    private lateinit var number:SetsControl
    private var chainsData = ""
    private var namesData = ""
    private var typeData = ""
    private var numberData = ""
    fun getData() = listOf(chainsData, namesData, typeData, numberData)
    private val viewModel: SettingModel by lazy {
        ViewModelProvider(this, SettingModel.Factory(requireActivity().application))
            .get(SettingModel::class.java)
    }
    var objects = listOf<Object>()
    var myId = 0
    fun Boolean.toInt() = if (this) 1 else 0
    companion object{
        fun createMe(id:Int):MainFragment{
            val f = MainFragment()
            val band = Bundle()
            band.putInt("id", id)
            f.arguments = band
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewFrag = inflater.inflate(R.layout.fragment_main, container, false)
        this.arguments?.let {
            myId = it.getInt("id")
        }
        var chainList = listOf("6кВ", "10кВ", "220кВ", "330кВ", "500кВ", "750кВ")

        val kv6 = listOf("ВЛ ПС Белый городок ф.№2 Хотилово")
        val kv10 = listOf(
            "ВЛ ПС Юрье-Девичье ф. №8 Глинки",
            "ВЛ ПС Микрорайонная ф. №5 Ваулино",
            "ВЛ пС Овсище фидер Починок"
        )
        val typeObject = listOf("Опора", "Пролет")
        val button_characters = viewFrag.findViewById<Button>(R.id.button_characters)
        val button_defects = viewFrag.findViewById<Button>(R.id.button_defects)
        chains = viewFrag.findViewById<SetsControl>(R.id.chains)
        chain_name = viewFrag.findViewById<SetsControl>(R.id.chain_name)
        object_type = viewFrag.findViewById<SetsControl>(R.id.object_type)
        number = viewFrag.findViewById<SetsControl>(R.id.number)
        var name_array = arrayOf<CharSequence>()
        val textView = viewFrag.findViewById<TextView>(R.id.textView8)
        val textView2 = viewFrag.findViewById<TextView>(R.id.textView9)
        val textView3 = viewFrag.findViewById<TextView>(R.id.textView10)
        val button_add_Setting = viewFrag.findViewById<Button>(R.id.button_add_Setting)
        val buttonDeleteResults = viewFrag.findViewById<Button>(R.id.delete_results)
        val buttonDeleteAll = viewFrag.findViewById<Button>(R.id.deleteAll)
        val buttonReadExcel = viewFrag.findViewById<Button>(R.id.insertExcel)
        viewModel.getAllObjects().observe(viewLifecycleOwner) {
            textView.text = it.map { it.toString() + "\n"}.toString()
            objects = it
            setDefault()
        }
        viewModel.getAllResults().observe(viewLifecycleOwner) {
            textView2.text = it.map {that ->  that.toString()+ "\n"}.toString()
        }
        viewModel.getAllSettings().observe(viewLifecycleOwner) {
            textView3.text = it.map{it.toString() + "\n"}.toString()
        }
        viewModel.chain.observe(viewLifecycleOwner) { th ->
            chains.setValueText(th)
            check()

        }
        viewModel.chain_name.observe(viewLifecycleOwner) {
            chain_name.setValueText(it)
            check()
        }
        viewModel.type.observe(viewLifecycleOwner) {
            object_type.setValueText(typeObject[it.toInt()])
            check()
            //object_type.setValueText(typeObject[it.toInt()])
        }
        viewModel.number.observe(viewLifecycleOwner) {
            check()
            number.setValueText(it)
        }
        button_defects.setOnClickListener {
            val int = Intent(context, AddSettingActivity::class.java)
            startActivity(int)
        }
        button_characters.setOnClickListener {
            //val int = Intent(context, CharactersActivity::class.java)
            //startActivity(int)
        }
        button_add_Setting.setOnClickListener{
            if(viewModel.type.value != null)
            {
                val int = Intent(context, AddSettingActivity::class.java)
                int.putExtra("F", viewModel.type.value)
                startActivity(int)
            }
            else{
                Toast.makeText(context,"Не выбран тип объекта",Toast.LENGTH_SHORT).show()
            }
        }
        buttonDeleteResults.setOnClickListener{
            viewModel.deleteAllResults()
        }
        buttonDeleteAll.setOnClickListener{
            viewModel.deleteAll()
        }
        buttonReadExcel.setOnClickListener{
            viewModel.updateAllSets(ReadExcel(resources).Read())

        }
        chains.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            chainList = objects.groupBy { it.chain }.map { (key, value) -> key }
            builder
                .setItems(
                    chainList.toTypedArray()
                ) { _, which ->
                    //chains.setValueText(chainList[which])
                    viewModel.chain.value = chainList[which]
                    viewModel.chain_name.value = ""
                    viewModel.number.value = ""
                    chainsData = chains.TitleText.text.toString() + ":" + chainList[which]

                }
            builder.create()
            builder.show()
        }
        chain_name.setOnClickListener {
            name_array =
                objects.filter { it.chain == viewModel.chain.value }.groupBy { it.name_chain }
                    .map { (key, value) -> key ?: "" }.toTypedArray()
            val builder = AlertDialog.Builder(context)
            builder
                .setItems(
                    name_array
                ) { _, which ->
                    //chain_name.setValueText(name_array[which].toString())
                    viewModel.chain_name.value = name_array[which].toString()
                    namesData =
                        chain_name.TitleText.text.toString() + ":" + name_array[which].toString()
                }
            builder.create()
            builder.show()
        }
        object_type.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder
                .setItems(
                    typeObject.toTypedArray()
                ) { _, which ->
                    object_type.setValueText(typeObject[which])
                    viewModel.type.value = (which + 1) % 2 == 0
                    (activity as MainActivity).x = (which + 1) % 2 == 0
                    typeData = object_type.TitleText.text.toString() + ":" + typeObject[which]
                }
            builder.create()
            builder.show()
        }
        number.setOnClickListener {
            val view = inflater.inflate(R.layout.edit_dialog, container, false)
            val builder = AlertDialog.Builder(context)
            val numbers =
                objects.filter { (it.chain == viewModel.chain.value) && (it.name_chain == viewModel.chain_name.value) && (it.type == viewModel.type.value) }
                    .map { it.number.toString() }
            builder.setTitle("Номер")
                .setItems(
                    numbers.toTypedArray()
                ) { _, which ->
                    number.setValueText(numbers[which])
                    viewModel.number.value = numbers[which]
                    numberData = number.TitleText.text.toString() + ":" + number.DescribeText
                }
            builder.create()
            builder.show()
        }
        return viewFrag
    }

    private fun setDefault(
    ) {
        if(objects.isNotEmpty()) {
            viewModel.chain.value = objects[0].chain
            viewModel.chain_name.value = objects[0].name_chain
            viewModel.type.value = false
            viewModel.number.value =  objects[0].number
        }
    }

    fun check() {
        with(viewModel)
        {
            val tmp = activity as MainActivity
            tmp.id_object =
                objects.firstOrNull { it.chain == chain.value && it.name_chain == chain_name.value && it.type == type.value && it.number == number.value }?.id
                    ?: 0
            if (tmp.id_object > 0){
                tmp.chain = chain.value?:""
                tmp.name_chain = chain_name.value?:""
            }
        }

    }
}