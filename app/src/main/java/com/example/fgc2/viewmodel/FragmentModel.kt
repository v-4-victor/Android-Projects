package com.example.fgc2.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fgc2.DB.*
import kotlinx.coroutines.launch

class FragmentModel : ViewModel()  {

    var pagesData = mutableListOf<fragmentPageData>()
    var getAllSettings  = MutableLiveData<List<Setting>>().apply{
        value = listOf()
    }
    var resultList = MutableList(12){mutableListOf<Setting>()}
    var chosenMap = List(12){ mutableMapOf<Int,Int>()}
}