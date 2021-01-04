package com.example.fgc2.DB

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class Repository(private val database: DataBase) {
    private val dao: DaoTables = database.dao


    suspend fun Insert(set: Setting) {
        withContext(Dispatchers.IO) {
            dao.insert(set)
        }
    }

    suspend fun DeleteAllResults() {
        withContext(Dispatchers.IO) {
            dao.deleteAllResults()
        }
    }

    suspend fun InsertSet(sets: Object) {
        withContext(Dispatchers.IO) {
            dao.insert(sets)
        }
    }

    suspend fun InsertResult(res: Result) {
        withContext(Dispatchers.IO) {
            dao.insert(res)
        }
    }

    suspend fun InsertAllResults(res: List<Result>) {
        withContext(Dispatchers.IO) {
            dao.insertAllResults(res)
        }
    }

    suspend fun InsertAllSettings(state: List<Setting>) {
        withContext(Dispatchers.IO) {
            dao.insertAllSettings(state)
        }
    }

    fun getAllObjects() = dao.getAllObjects()
    fun getSettings() = dao.getAllSettings()
    fun getAllResults() = dao.getAllResults()
    fun getChains() = dao.getChains()
    fun getNames(chain: String) = dao.getNames(chain)
    fun getNumbers(chain: String, name: String) = dao.getNumbers(chain, name)
    //fun getObjectSettings() = dao.getAllObjectSettings()

    suspend fun UpdateSet(sets: Object) {
        withContext(Dispatchers.IO) {
            dao.update(sets)
        }
    }

    suspend fun DeleteObject(sets: Object) {
        withContext(Dispatchers.IO) {
            dao.delete(sets)
        }
    }

    suspend fun DeleteAll() {
        withContext(Dispatchers.IO) {
            //Timber.d("refresh videos is called");
            dao.deleteAllResults()
            dao.deleteAllData()
            dao.deleteAllSets()
        }
    }

    suspend fun UpdateAllSets(state: List<Setting>) {
        withContext(Dispatchers.IO) {
            dao.insertAllSettings(state)
        }
    }
}