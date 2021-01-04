package com.example.fgc2.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fgc2.DB.Object
import com.example.fgc2.DB.Result
import com.example.fgc2.DB.fragmentPageData
import com.example.fgc2.Excel.WriteExcel
import com.example.fgc2.R
import com.example.fgc2.fragments.MainFragment
import com.example.fgc2.ui.EndPage
import com.example.fgc2.ui.SetsPage1
import com.example.fgc2.ui.SetsPageWork
import com.example.fgc2.viewmodel.FragmentModel
import com.example.fgc2.viewmodel.SettingModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var fragments: List<Fragment>
    private val chosenList = mutableMapOf<Int, Int>()
    var x = false
    var id_object = 0
    var chain = ""
    var name_chain = ""
    var id_number = 0
    lateinit var list: List<Result>
    var url: Uri? = null
        private set
    private val viewModel: SettingModel by lazy {
        ViewModelProvider(this, SettingModel.Factory(application))
            .get(SettingModel::class.java)
    }
    private val newModel: FragmentModel by viewModels()
    fun addSetting(set: Pair<Int, Int>) {
        chosenList[set.first] = set.second
    }

    fun deleteSettingFromPage(page: Int) {
        chosenList.remove(page)
    }
    fun clearList() = chosenList.clear()

    fun getAllSets(): Map<Int, Int> = chosenList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.getAllResults().observe(this) {
            list = it
        }
        viewModel.getAllSettings().observe(this)
        {
            newModel.getAllSettings.value = it
        }
        newModel.pagesData.addAll(
            listOf(
                fragmentPageData(
                    "Состояние изоляции:",
                    listOf("изоляция фарфоровая/стеклянная", "изоляция полимерная"),
                    "Изоляция",
                    0,
                    0.184
                ),
                fragmentPageData("Арматура:", listOf(), "", 2, 0.135),
                fragmentPageData("Заземление:", listOf(), "", 3, 0.135),
                fragmentPageData(
                    "Стойка:",
                    listOf(
                        "решетчатая (для металлических опор)",
                        "многогранная (для металлических опор)",
                        " (для железобетонных опор) или приставка железобетонная для деревянных опор",
                        "(для деревянных опор)",
                        "приставка деревянная (для деревянных опор)"
                    ),
                    "Тип стойки",
                    4, 0.135
                ),
                fragmentPageData(
                    "Траверса:",
                    listOf(
                        "металлическая",
                        "железобетонная",
                        "подтраверсный брус (для деревянных опор)",
                        "ветровая связь (для деревянных опор)"
                    ),
                    "Тип траверсы",
                    9, 0.135
                ),
                fragmentPageData("Тросостойка:", listOf(), "", 13, 0.135),
                fragmentPageData("оттяжка (при наличии):", listOf(), "", 14, 0.135),
                fragmentPageData("общие дефекты:", listOf(), "", 15, 0.135),
                fragmentPageData(
                    "фундамент оттяжки (измеряются при наличии оттяжек):",
                    listOf(),
                    "",
                    16,
                    0.033
                ),
                fragmentPageData("фундамент опоры:", listOf(), "", 17, 0.033),
                fragmentPageData(
                    "Группа критических параметров изоляции:",
                    listOf(),
                    "",
                    18,
                    0.005
                ),
                fragmentPageData("Группа критических параметров опоры:", listOf(), "", 19, 0.003)
            )
        )
        fragments = listOf(
            MainFragment(),
            SetsPage1(
                "Состояние изоляции:",
                listOf("изоляция фарфоровая/стеклянная", "изоляция полимерная"),
                "Изоляция",
                0,
                0.184
            ),
            SetsPage1("Арматура:", listOf(), "", 2, 0.135),
            SetsPage1("Заземление:", listOf(), "", 3, 0.135),
            SetsPage1(
                "Стойка:",
                listOf(
                    "решетчатая (для металлических опор)",
                    "многогранная (для металлических опор)",
                    " (для железобетонных опор) или приставка железобетонная для деревянных опор",
                    "(для деревянных опор)",
                    "приставка деревянная (для деревянных опор)"
                ),
                "Тип стойки",
                4, 0.135
            ),
            SetsPage1(
                "Траверса:",
                listOf(
                    "металлическая",
                    "железобетонная",
                    "подтраверсный брус (для деревянных опор)",
                    "ветровая связь (для деревянных опор)"
                ),
                "Тип траверсы",
                9, 0.135
            ),
            SetsPage1("Тросостойка:", listOf(), "", 13, 0.135),
            SetsPage1("оттяжка (при наличии):", listOf(), "", 14, 0.135),
            SetsPage1("общие дефекты:", listOf(), "", 15, 0.135),
            SetsPage1(
                "фундамент оттяжки (измеряются при наличии оттяжек):",
                listOf(),
                "",
                16,
                0.033
            ),
            SetsPage1("фундамент опоры:", listOf(), "", 17, 0.033),
            SetsPage1("Группа критических параметров изоляции:", listOf(), "", 18, 0.005),
            SetsPage1("Группа критических параметров опоры:", listOf(), "", 19, 0.003),
            EndPage()
        )
        requestMultiplePermissions()

        viewPager.adapter = ViewPagerAdapter(this)
    }

    fun requestMultiplePermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            ),
            1
        )
    }

    inner class ViewPagerAdapter(fm: FragmentActivity) :
        FragmentStateAdapter(fm) {

        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> MainFragment.createMe(5)
//            1 -> SetsPage1(
//                "Состояние изоляции:",
//                listOf("изоляция фарфоровая/стеклянная", "изоляция полимерная"),
//                "Изоляция",
//                0,
//                0.184
//            )
//            1 -> SetsPageWork.createMe(1)
//            2 -> SetsPage1("Арматура:", listOf(), "", 2, 0.135)
//            3 -> SetsPage1("Заземление:", listOf(), "", 3, 0.135)
//            4 -> SetsPage1(
//                "Стойка:",
//                listOf(
//                    "решетчатая (для металлических опор)",
//                    "многогранная (для металлических опор)",
//                    " (для железобетонных опор) или приставка железобетонная для деревянных опор",
//                    "(для деревянных опор)",
//                    "приставка деревянная (для деревянных опор)"
//                ),
//                "Тип стойки",
//                4, 0.135
//            )
//            5 -> SetsPage1(
//                "Траверса:",
//                listOf(
//                    "металлическая",
//                    "железобетонная",
//                    "подтраверсный брус (для деревянных опор)",
//                    "ветровая связь (для деревянных опор)"
//                ),
//                "Тип траверсы",
//                9, 0.135
//            )
//
//            6 -> SetsPage1("Тросостойка:", listOf(), "", 13, 0.135)
//            7 -> SetsPage1("оттяжка (при наличии):", listOf(), "", 14, 0.135)
//            8 -> SetsPage1("общие дефекты:", listOf(), "", 15, 0.135)
//            9 -> SetsPage1(
//                "фундамент оттяжки (измеряются при наличии оттяжек):",
//                listOf(),
//                "",
//                16,
//                0.033
//            )
//            10 -> SetsPage1("фундамент опоры:", listOf(), "", 17, 0.033)
//            11 -> SetsPage1("Группа критических параметров изоляции:", listOf(), "", 18, 0.005)
//            12 -> SetsPage1("Группа критических параметров опоры:", listOf(), "", 19, 0.003)
            13 -> EndPage()
            else -> SetsPageWork.createMe(position)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            "photo" -> Toast.makeText(applicationContext, "Camera", Toast.LENGTH_SHORT).show()
            "save" -> save()
            "calc" -> {
                if (fragments[1].isAdded) {
                    (fragments[1] as SetsPage1).makeResults(id_object)

                }

            }
            "add" -> {
                val int = Intent(this, AddObjectActivity::class.java)
                startActivity(int)
            }
            "Settings" -> {
                val int = Intent(this, SettingsActivity::class.java)
                startActivity(int)
            }
            else -> Toast.makeText(applicationContext, "ccc", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    fun calc(results: List<Result>) {
        viewModel.insertAllResults(results)
        viewPager.currentItem = 0
    }

    fun save() {
        if (list.isNotEmpty()) {
            WriteExcel(this).writeExcel(list, filesDir,Object(0,chain,name_chain,false,"0"))
        } else
            Toast.makeText(
                this,
                "Дефектов(результатов) нет. Перейдите на следующую страницу выбрать деефекты",
                Toast.LENGTH_SHORT
            ).show()
//        try {
//            val fos = FileOutputStream(mFile)
//            val myData: String =
//                (fragments[0] as MainFragment).getData().map { it + "\n" }.joinToString(
//                    ""
//                )
//            fos.write(myData.toByteArray())
//            fos.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        Toast.makeText(this, getExternalFilesDir("MyFileStorage").toString(), Toast.LENGTH_LONG)
//            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            url = data?.data
        }
    }
}
