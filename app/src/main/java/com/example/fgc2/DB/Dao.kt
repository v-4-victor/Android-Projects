package com.example.fgc2.DB

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Dao
interface DaoTables {
    @Insert
    fun insert(obj: Object)

    @Insert
    fun insert(sets: Setting)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllSettings(sets: List<Setting>):List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(res: Result)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllResults(res: List<Result>)


    //@Insert
    //fun insert(obj:Object_has_settings)
    @Update
    fun update(obj: Object)

    @Update
    fun update(sets: Setting)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(sets: List<Setting>)

    @Update
    fun update(res: Result)

    @Delete
    fun delete(obj: Object)

    @Delete
    fun delete(sets: Setting)

    @Delete
    fun delete(res: Result)

    @Query("DELETE FROM Object")
    fun deleteAllData()

    @Query("SELECT * FROM Object ORDER BY id DESC")
    fun getAllObjects(): LiveData<List<Object>>

    @Query("DELETE FROM Setting")
    fun deleteAllSets()

    @Query("DELETE FROM Result")
    fun deleteAllResults()
    @Query("SELECT * FROM Setting ORDER BY title DESC")
    fun getAllSettings(): LiveData<List<Setting>>

    @Query("SELECT * FROM Result ORDER BY id_object DESC")
    fun getAllResults(): LiveData<List<Result>>

    @Query("SELECT chain FROM Object ORDER BY id DESC")
    fun getChains(): LiveData<List<String>>

    @Query("SELECT name_chain FROM Object WHERE chain = :chain ORDER BY id DESC")
    fun getNames(chain: String): LiveData<List<String>>

    @Query("SELECT number FROM Object WHERE chain = :chain AND name_chain = :name_chain ORDER BY id DESC")
    fun getNumbers(chain: String, name_chain: String): LiveData<List<String>>
}

@Database(
    entities = [Object::class, Setting::class, Result::class],
    version = 3
)
abstract class DataBase : RoomDatabase() {
    abstract val dao: DaoTables
}

private lateinit var INSTANCE: DataBase
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Result ADD COLUMN number INTEGER")
    }
}
fun getDatabase(context: Context): DataBase {
    synchronized(DataBase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                DataBase::class.java,
                "DATA.db"
            )
                .addMigrations(MIGRATION_1_2)
                .createFromAsset("DATA.db")
                //.setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}