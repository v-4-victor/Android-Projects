package com.example.fgc2.DB

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.w3c.dom.Comment
import java.util.*

@Entity(tableName = "Object")
data class Object(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var chain: String,
    var name_chain: String?,
    var type: Boolean,
    var number: String?,
)


@Entity(tableName = "Setting")
data class Setting(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val state: String = "",
    val grade: Int,
    val page: Int,
    val type: Boolean,
    val object_type: Boolean
)

@Entity(
    tableName = "Result", primaryKeys = ["id_object","number", "title"], foreignKeys = [ForeignKey(
        entity = Object::class,
        parentColumns = ["id"],
        childColumns = ["id_object"]
    )]
)
data class Result(
    val id_object: Int,
    val number: Int,
    @Embedded
    val setting: Setting,
    val date: String,
    val comment: String
)

data class fragmentPageData(
    val title: String,
    val mainSettingStates: List<String>,
    val mainTitle:String,
    val startPage: Int,
    val k: Double
)