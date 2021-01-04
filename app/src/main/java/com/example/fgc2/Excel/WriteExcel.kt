package com.example.fgc2.Excel

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.fgc2.DB.Object
import com.example.fgc2.DB.Result
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.util.CellRangeAddress
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream


class WriteExcel(val context: Context) {

    fun writeExcel(list: List<Result>, filesDir: File, obj: Object) {
        val file = File(filesDir, "path.txt")
        val fileFio = File(filesDir, "acc.txt")
        if (file.length() > 0) {

            val workbook = HSSFWorkbook();
            val numbers = list.distinctBy { it.number }.map { it.number?:0}
            numbers.sorted()
            val f223 = File(file.readText())
            var f2 =
                if (checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) f223
                else File(context.getExternalFilesDir("Data"), "Exc.xls")
            var FIO = ""
            var rank = ""
            if(fileFio.length()> 0)
            {
                val lines = fileFio.readLines()
                FIO = lines[0]
                rank = lines[1]
            }
            for(i in numbers.indices) {
                val sheet = workbook.createSheet("Опора № ${numbers[i] + 1}")
                var rownum = 0
                var row = sheet.createRow(rownum)
                sheet.addMergedRegion(CellRangeAddress(0, 0, 1, 7))
                var cell = row.createCell(1, CellType.STRING)
                cell.setCellValue("ЛИСТОК ОСМОТРА")
                rownum += 2
                row = sheet.createRow(rownum)
                val tabRow = listOf("ВЛ", obj.chain, "кВ", obj.name_chain)
                for (i in 0..3) {
                    cell = row.createCell(i, CellType.STRING)
                    cell.setCellValue(tabRow[i])
                }
                rownum+=2
                row = sheet.createRow(rownum)
                val style = workbook.createCellStyle()
                style.borderBottom = BorderStyle.THIN
                style.borderLeft = BorderStyle.THIN
                style.borderRight = BorderStyle.THIN
                style.borderTop = BorderStyle.THIN
                with(row.createCell(0, CellType.STRING))
                {
                    setCellValue("№ п.п.")
                    setCellStyle(style)
                }
                with(row.createCell(2, CellType.STRING))
                {
                    setCellValue("Выявленный дефект")
                    setCellStyle(style)
                }

                with(row.createCell(1, CellType.STRING))
                {
                    setCellValue("Номер опоры")
                    setCellStyle(style)
                }
                var k = 1
                val tmpList = list.filter { it.number == numbers[i] }
                for (emp in tmpList) {
                    rownum++
                    row = sheet.createRow(rownum)

                    // EmpNo (A)
                    cell = row.createCell(0, CellType.STRING)
                    cell.setCellValue(k.toString())
                    cell.setCellStyle(style)
                    // EmpName (B)
                    cell = row.createCell(1, CellType.STRING)
                    cell.setCellValue((emp.number + 1).toString())
                    cell.setCellStyle(style)
                    // Salary (C)
                    k++
                    cell = row.createCell(2, CellType.STRING)
                    cell.setCellValue(emp.setting.title)
                    cell.setCellStyle(style)
                }
                rownum+= 2
                row = sheet.createRow(rownum)
                val tabb = listOf("ФИО :", FIO, "Должность: ", rank)
                for (j in 0..3) {
                    cell = row.createCell(j, CellType.STRING)
                    cell.setCellValue(tabb[j])
                }
            }
//            rownum += 2
//            row = sheet.createRow(rownum)
//            sheet.addMergedRegion(CellRangeAddress(rownum, rownum, 0, 3))
//            sheet.addMergedRegion(CellRangeAddress(rownum, rownum, 5, 6))
//            row.createCell(0, CellType.STRING).setCellValue("Осмотр произведен от опоры №")
//            row.createCell(4, CellType.NUMERIC).setCellValue(0.0)
//            row.createCell(5, CellType.STRING).setCellValue("до опоры №")
//            row.createCell(7, CellType.NUMERIC).setCellValue(17.0)
            file.writeText(f223.absolutePath)
            f2.mkdir()
            f2 = File(f2, "Result.xls")
            val outPut = FileOutputStream(f2)
            try {
                workbook.write(outPut)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            outPut.close()
            Toast.makeText(context, f2.absolutePath, Toast.LENGTH_SHORT).show()
        }
    }


    fun checkPermission(permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

}