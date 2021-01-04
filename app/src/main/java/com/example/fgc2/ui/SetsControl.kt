package com.example.fgc2.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fgc2.R
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.setting.view.*


class SetsControl : ConstraintLayout{
    constructor(context: Context):super(context)
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.deletethis, this, true)
    }
    constructor(context: Context, attrs: AttributeSet):super(context,attrs)
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.deletethis, this, true)
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.SetsControl, 0, 0
        )
        TitleText.text = a.getString(R.styleable.SetsControl_title)
        DescribeText.text = a.getString(R.styleable.SetsControl_value)
    }
    @Parcelize
    data class Item(
        var title: String,
        var value: String
    ) : Parcelable
    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putParcelable("stuff", Item( TitleText.text.toString(),DescribeText.text.toString()))
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state != null) // implicit null check
        {

            val bundle =  state as Bundle
            val item:Item? = bundle.getParcelable("stuff") // ... load stuff
            item?.let {
                Log.e("This","Log ${item.value}")
                TitleText.text = item.title
                DescribeText.text = item.value
            }
            super.onRestoreInstanceState(bundle.getParcelable("superState"))
        }
        //super.onRestoreInstanceState(state)
    }

    fun setTitleText(text: String?) {
        TitleText.text = text
    }
    fun getValueText() = DescribeText.text.toString()
    fun getTitleText() = TitleText.text.toString()
    fun setValueText(text: String?) {
        DescribeText.text = text
    }
}