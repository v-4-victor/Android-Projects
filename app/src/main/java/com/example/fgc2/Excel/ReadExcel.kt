package com.example.fgc2.Excel

import android.content.res.Resources
import android.widget.TextView
import android.widget.Toast
import com.example.fgc2.DB.Setting
import com.example.fgc2.R
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import java.io.IOException
import java.io.InputStream

class ReadExcel(val resources:Resources) {
    fun Read():List<Setting>{
        try {
            val caInput: InputStream = resources.openRawResource(R.raw.params)
            val workbook = HSSFWorkbook(caInput)
            val feuille = workbook.getSheetAt(0)
            val list = mutableListOf<Setting>()
            var text = ""
            var text2 = ""
            var page = 0
            feuille.forEach { row ->
                row.getCell(4)?.let {
                    if ((row.getCell(5)?.toString() == "") or (row.getCell(6)?.toString() == "a"))
                        text2 += it.toString()
                    if (listOf(
                            "арматура линейная",
                            "изоляция полимерная",
                            "заземление",
                            "стойка решетчатая (для металлических опор)",
                            "стойка многогранная (для металлических опор)",
                            "стойка (для железобетонных опор) или приставка железобетонная для деревянных опор",
                            "стойка (для деревянных опор)",
                            "приставка деревянная (для деревянных опор)",
                            "траверса металлическая",
                            "траверса железобетонная",
                            "траверса/ подтраверсный брус (для деревянных опор)",
                            "ветровая связь (для деревянных опор)",
                            "тросостойка",
                            "оттяжка (при наличии)",
                            "общие дефекты",
                            "фундамент оттяжки (измеряются при наличии оттяжек)",
                            "фундамент опоры",
                            "Группа критических параметров изоляции (можно перенести эти параметры в соответствующий раздел по типам изоляции)",
                            "Группа критических параметров опоры (можно перенести эти параметры в соответствующий раздел по частям опоры)")
                            .contains(it.toString())
                    )
                        page++
                }
                row.getCell(5)?.let {

                    if ((it.toString() != "") and (it.toString() != "Параметр функционального узла")) {
                        for (i in 8..12) {

                            val title = it.toString() + " " + if (row.getCell(6) != null) row.getCell(6) else ""
                            if (row.getCell(i)?.toString() != "-") {
                                val grade = i - 8
                                val state = row.getCell(i).toString()
                                val set = Setting(0, title, state, grade, page, false, false)
                                list.add(set)
                            }
                        }

                    }
                }
                text += row.getCell(5)?.toString() + '\n'
            }
            return list.toList()
            //textview1.text = text
            //textView.text = list.groupBy { it.title }.map { (key, value) -> value[0].title + ' ' + value[0].page.toString() + '\n' }.toString()
        } catch (e1: IOException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
        return listOf()
    }
}