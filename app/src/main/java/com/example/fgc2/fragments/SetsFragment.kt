package com.example.fgc2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.fgc2.R
import com.example.fgc2.ui.SetsControl

class SetsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sets, container, false)
        val destruct = view.findViewById<SetsControl>(R.id.destruct)
        val destruct2 = view.findViewById<SetsControl>(R.id.destruct2)
        val destruct3 = view.findViewById<SetsControl>(R.id.destruct3)
        val destruct4 = view.findViewById<SetsControl>(R.id.destruct4)
        val destruct5 = view.findViewById<SetsControl>(R.id.destruct5)
        val destruct6 = view.findViewById<SetsControl>(R.id.destruct6)
        val destruct7 = view.findViewById<SetsControl>(R.id.destruct7)
        val destruct8 = view.findViewById<SetsControl>(R.id.destruct8)
        val destruct9 = view.findViewById<SetsControl>(R.id.destruct9)
        destruct.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder
                .setItems(
                    R.array.have_not
                ) { _, which ->
                    destruct.setValueText(resources.getStringArray(R.array.have_not)[which])
                }
            builder.create()
            builder.show()
        }
        destruct.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder
                .setItems(
                    R.array.have_not
                ) { _, which ->
                    destruct.setValueText(resources.getStringArray(R.array.have_not)[which])
                }
            builder.create()
            builder.show()
        }

        destruct2.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder
                .setItems(
                    R.array.have_not3
                ) { _, which ->
                    destruct2.setValueText(resources.getStringArray(R.array.have_not3)[which])
                }
            builder.create()
            builder.show()
        }
        destruct3.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder
                .setItems(
                    R.array.have_not
                ) { _, which ->
                    destruct3.setValueText(resources.getStringArray(R.array.have_not)[which])
                }
            builder.create()
            builder.show()
        }
        destruct4.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder
                .setItems(
                    R.array.have_not
                ) { _, which ->
                    destruct4.setValueText(resources.getStringArray(R.array.have_not)[which])
                }
            builder.create()
            builder.show()
        }
        destruct5.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder
                .setItems(
                    R.array.have_not
                ) { _, which ->
                    destruct5.setValueText(resources.getStringArray(R.array.have_not)[which])
                }
            builder.create()
            builder.show()
        }
        destruct6.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder
                .setItems(
                    R.array.have_not
                ) { _, which ->
                    destruct6.setValueText(resources.getStringArray(R.array.have_not)[which])
                }
            builder.create()
            builder.show()
        }
        destruct7.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder
                .setItems(
                    R.array.have_not
                ) { _, which ->
                    destruct7.setValueText(resources.getStringArray(R.array.have_not)[which])
                }
            builder.create()
            builder.show()
        }
        destruct8.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder
                .setItems(
                    R.array.have_not2
                ) { _, which ->
                    destruct8.setValueText(resources.getStringArray(R.array.have_not2)[which])
                }
            builder.create()
            builder.show()
        }
        destruct9.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder
                .setItems(
                    R.array.have_not3
                ) { _, which ->
                    destruct9.setValueText(resources.getStringArray(R.array.have_not3)[which])
                }
            builder.create()
            builder.show()
        }
        return view
    }
}