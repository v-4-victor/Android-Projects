package com.example.fgc2.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.fgc2.DB.*
import kotlinx.coroutines.launch

class SettingModel(app: Application) : AndroidViewModel(app) {
    private val SetsRepos = Repository(getDatabase(app))
    private val allObject = SetsRepos.getAllObjects()

    var id = 0
    var chain = MutableLiveData<String>().apply {
        value = ""
    }
    var chain_name = MutableLiveData<String>().apply {
        value = ""
    }
    var type = MutableLiveData<Boolean>().apply {
        value = false
    }
    var number = MutableLiveData<String>().apply {
        value = ""
    }

    fun insert(sets: Object) = viewModelScope.launch { SetsRepos.InsertSet(sets) }
    fun insert(set: Setting) = viewModelScope.launch { SetsRepos.Insert(set) }
    fun insert(set: Result) = viewModelScope.launch { SetsRepos.InsertResult(set) }
    fun insertAllSettings(sets: List<Setting>) =
        viewModelScope.launch { SetsRepos.InsertAllSettings(sets) }

    fun insertAllResults(res:List<Result>) = viewModelScope.launch { SetsRepos.InsertAllResults(res) }

    fun deleteAll() = viewModelScope.launch { SetsRepos.DeleteAll() }
    fun deleteObject(obj: Object) = viewModelScope.launch { SetsRepos.DeleteObject(obj) }
    fun deleteAllResults() = viewModelScope.launch { SetsRepos.DeleteAllResults() }
    fun updateAllSets(sets: List<Setting>) = viewModelScope.launch { SetsRepos.UpdateAllSets(sets) }
    fun getAllSettings() = SetsRepos.getSettings()
    fun getAllObjects() = SetsRepos.getAllObjects()
    fun getAllResults() = SetsRepos.getAllResults()

    fun getChains() = SetsRepos.getChains()
    fun getNames(chain: String) = SetsRepos.getNames(chain)
    fun getNumbers(chain: String, name: String) = SetsRepos.getNumbers(chain, name)
    //fun getObjectSettings() = SetsRepos.getObjectSettings()

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SettingModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}