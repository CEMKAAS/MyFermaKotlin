package com.hfad.myferma.incubator

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.R
import com.hfad.myferma.db.MyFermaDatabaseHelper

class AddIncubatorFragment : Fragment(), AddAdapterIncubator.Listener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var myDB: MyFermaDatabaseHelper

    private var massId = mutableListOf<String>()
    private var massTemp = mutableListOf<String>()
    private var massDamp = mutableListOf<String>()
    private var massOver = mutableListOf<String>()
    private var massAiring = mutableListOf<String>()

    private var nameIncubator = ""
    private var typeIncubator = ""
    private var dataIncubator = ""
    private var eggAll = 0
    private var time1 = ""
    private var time2 = ""
    private var time3 = ""
    private var friz = false
    private var frizInt = "0"
    private var over = false
    private var overInt = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout: View = inflater.inflate(R.layout.fragment_add_incubator, container, false)

        myDB = MyFermaDatabaseHelper(requireContext())

        helpIncubator()

        var arhive = false

        val bundle: Bundle? = arguments
        if (bundle != null) {
            nameIncubator = bundle.getString("name").toString()
            typeIncubator = bundle.getString("type").toString()
            dataIncubator = bundle.getString("data").toString()
            eggAll = bundle.getInt("eggAll")
            time1 = bundle.getString("time1").toString()
            time2 = bundle.getString("time2").toString()
            time3 = bundle.getString("time3").toString()
            friz = bundle.getBoolean("friz")
            over = bundle.getBoolean("over")
            arhive = bundle.getBoolean("arhive")
        }

        if (arhive) {
            massTemp = bundle?.getStringArray("temp")!!.toMutableList()
            massDamp = bundle.getStringArray("damp")!!.toMutableList()
            massOver = bundle.getStringArray("over")!!.toMutableList()
            massAiring = bundle.getStringArray("airing")!!.toMutableList()

        } else {
            setIncubator()
            setOverAndAiring()
        }


        //Настройка аппбара
        val appBar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.title = nameIncubator


        recyclerView = layout.findViewById(R.id.recyclerView)
        val listAdapterIncubator =
            AddAdapterIncubator(
                massTemp,
                massDamp,
                massOver,
                massAiring,
                this
            )
        recyclerView.adapter = listAdapterIncubator
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val begin: Button = layout.findViewById(R.id.update_button)
        begin.setOnClickListener {
            beginIncubator(layout)
        }

        return layout

    }


    private fun setIncubator(
    ) {
        when (typeIncubator) {
            "Курицы" -> {
                massTemp = mutableListOf(
                    "37.9",
                    "37.9",
                    "37.9",
                    "37.9",
                    "37.9",
                    "37.9",
                    "37.9",
                    "37.9",
                    "37.9",
                    "37.9",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.3",
                    "37.3",
                    "37.0",
                    "37.0",
                    "37.0"
                )
                massDamp = mutableListOf(
                    "66",
                    "66",
                    "66",
                    "66",
                    "66",
                    "66",
                    "66",
                    "66",
                    "66",
                    "66",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "47",
                    "47",
                    "70",
                    "70",
                    "70"
                )
                massAiring = mutableListOf(
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "2 раза по 5 мин",
                    "2 раза по 5 мин",
                    "2 раза по 5 мин",
                    "2 раза по 5 мин",
                    "2 раза по 5 мин",
                    "2 раза по 5 мин",
                    "2 раза по 20 мин",
                    "2 раза по 20 мин",
                    "2 раза по 5 мин",
                    "2 раза по 5 мин"
                )
                massOver = mutableListOf(
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "2-3",
                    "0"
                )
            }

            "Гуси" -> {
                massTemp = mutableListOf(
                    "38.0",
                    "37.8",
                    "37.8",
                    "37.6",
                    "37.6",
                    "37.6",
                    "37.6",
                    "37.6",
                    "37.6",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3",
                    "37.3"
                )
                massDamp = mutableListOf(
                    "65",
                    "65",
                    "65",
                    "70",
                    "70",
                    "70",
                    "70",
                    "70",
                    "70",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75"
                )
                massAiring = mutableListOf(
                    "нет",
                    "1 раз по 20 мин",
                    "1 раз по 20 мин",
                    "1 раз по 20 мин",
                    "1 раз по 20 мин",
                    "2 раз по 20 мин",
                    "2 раз по 20 мин",
                    "2 раз по 20 мин",
                    "2 раз по 20 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин",
                    "3 раза по 45 мин"
                )
                massOver = mutableListOf(
                    "3-4",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "10",
                    "0",
                    "0",
                    "0"
                )
            }

            "Перепела" -> {
                massTemp = mutableListOf(
                    "38.0",
                    "38.0",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.5",
                    "37.5",
                    "37.5"
                )
                massDamp = mutableListOf(
                    "55",
                    "55",
                    "55",
                    "55",
                    "55",
                    "55",
                    "55",
                    "55",
                    "55",
                    "55",
                    "55",
                    "55",
                    "55",
                    "55",
                    "37.5",
                    "37.5",
                    "37.5"
                )
                massAiring = mutableListOf(
                    "нет",
                    "нет",
                    "нет",
                    "1 раз по 5 мин",
                    "1 раз по 5 мин",
                    "1 раз по 5 мин",
                    "1 раз по 5 мин",
                    "1 раз по 5 мин",
                    "1 раз по 5 мин",
                    "1 раз по 5 мин",
                    "1 раз по 5 мин",
                    "1 раз по 5 мин",
                    "1 раз по 5 мин",
                    "1 раз по 5 мин",
                    "нет",
                    "нет",
                    "нет"
                )
                massOver = mutableListOf(
                    "3-6",
                    "3-6",
                    "3-6",
                    "3-6",
                    "3-6",
                    "3-6",
                    "3-6",
                    "3-6",
                    "3-6",
                    "3-6",
                    "3-6",
                    "3-6",
                    "3-6",
                    "3-6",
                    "3-6",
                    "нет",
                    "нет"
                )
            }

            "Индюки" -> {
                massTemp = mutableListOf(
                    "38.0",
                    "38.0",
                    "38.0",
                    "38.0",
                    "38.0",
                    "38.0",
                    "38.0",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.7",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5"
                )
                massDamp = mutableListOf(
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "45",
                    "45",
                    "45",
                    "45",
                    "45",
                    "45",
                    "65",
                    "65",
                    "65",
                    "65",
                    "65",
                    "65",
                    "65",
                    "65",
                    "65",
                    "65",
                    "65",
                    "65",
                    "65",
                    "65",
                    "65"
                )
                massAiring = mutableListOf(
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "2 раза по 5 мин",
                    "2 раза по 5 мин",
                    "2 раза по 5 мин",
                    "2 раза по 5 мин",
                    "2 раза по 5 мин",
                    "2 раза по 5 мин",
                    "2 раза по 5 мин",
                    "4 раза по 10 мин",
                    "4 раза по 10 мин",
                    "4 раза по 10 мин",
                    "4 раза по 10 мин",
                    "4 раза по 10 мин",
                    "4 раза по 10 мин",
                    "4 раза по 10 мин",
                    "4 раза по 10 мин",
                    "4 раза по 10 мин",
                    "4 раза по 10 мин",
                    "4 раза по 10 мин",
                    "нет",
                    "нет",
                    "нет"
                )
                massOver = mutableListOf(
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "4",
                    "4",
                    "4",
                    "4",
                    "4",
                    "4",
                    "4",
                    "4",
                    "4",
                    "4",
                    "4",
                    "нет",
                    "нет",
                    "нет"
                )
            }

            "Утки" -> {
                massTemp = mutableListOf(
                    "38.0",
                    "38.0",
                    "38.0",
                    "38.0",
                    "38.0",
                    "38.0",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.8",
                    "37.5",
                    "37.5",
                    "37.5",
                    "37.5"
                )
                massDamp = mutableListOf(
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "75",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "60",
                    "90",
                    "90",
                    "90",
                    "90"
                )
                massAiring = mutableListOf(
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "нет",
                    "2 раза по 15 мин",
                    "2 раза по 15 мин",
                    "2 раза по 15 мин",
                    "2 раза по 20 мин",
                    "2 раза по 20 мин",
                    "2 раза по 15 мин",
                    "2 раза по 15 мин",
                    "2 раза по 15 мин",
                    "2 раза по 15 мин",
                    "2 раза по 15 мин",
                    "2 раза по 15 мин",
                    "нет",
                    "нет",
                    "нет"
                )
                massOver = mutableListOf(
                    "4",
                    "4",
                    "4",
                    "4",
                    "4",
                    "4",
                    "4",
                    "4-6",
                    "4-6",
                    "4-6",
                    "4-6",
                    "4-6",
                    "4-6",
                    "4-6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "6",
                    "нет",
                    "нет",
                    "нет"
                )
            }
        }
    }

//Если стоят галочки на авто
    private fun setOverAndAiring() {
        when (typeIncubator) {
            "Курицы" -> {
                if (over) addOverAndAiring(21, massOver)
                if (friz) addOverAndAiring(21, massAiring)
            }

            "Индюки", "Утки" -> {
                if (over) addOverAndAiring(28, massOver)
                if (friz) addOverAndAiring(28, massAiring)
            }

            "Гуси" -> {
                if (over) addOverAndAiring(30, massOver)
                if (friz) addOverAndAiring(30, massAiring)
            }

            "Перепела" -> {
                if (over) addOverAndAiring(17, massOver)
                if (friz) addOverAndAiring(17, massAiring)
            }

        }
    }

    private fun addOverAndAiring(day: Int, mass: MutableList<String>) {
        mass.clear()
        for (i in 1..day) {
            mass.add("Авто")
        }
        if (over) overInt = "1"
        if (friz) frizInt = "1"
    }

    private fun setMassToSql() {
        when (typeIncubator) {
            "Курицы" -> {
                endMass(9, massTemp)
                endMass(9, massDamp)
                endMass(9, massOver)
                endMass(9, massAiring)
            }

            "Индюки", "Утки" -> {
                endMass(2, massTemp)
                endMass(2, massDamp)
                endMass(2, massOver)
                endMass(2, massAiring)
            }

            "Перепела" -> {
                endMass(13, massTemp)
                endMass(13, massDamp)
                endMass(13, massOver)
                endMass(13, massAiring)
            }
        }

    }

    private fun endMass(day: Int, mass: MutableList<String>) {
        for (i in 1..day) {
            mass.add("0")
        }
    }


    private fun helpIncubator() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Справка")
        builder.setMessage(
            "Если у Вашего инкубатора имеется погрешность Вы можете задать нужную температуру здесь, или воспользоваться рекомендуемыми значениями в процессе их можно изменять.\n" +
                    "Старайтесь вести журнал каждый день и кооректно заносить данные, чтобы в будующем пользоваться этими же данными из архива. "
        )
        builder.setPositiveButton(
            "Отлично!"
        ) { dialogInterface, i ->

        }
        builder.show()
    }


    private fun beginIncubator(view: View?) {
        val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Запустить $nameIncubator ?")
        builder.setMessage(
            "Вы уверены, что хотите запустить $nameIncubator ?"
        )
        builder.setPositiveButton(
            "Да"
        ) { dialogInterface, i ->


            setMassToSql()

            myDB.insertToDbIncubator(
                nameIncubator,
                typeIncubator,
                dataIncubator,
                overInt,
                eggAll.toString(),
                eggAll.toString(),
                frizInt,
                "0",
                "0",
                time1,
                time2,
                time3
            )
            myDB.insertToDbIncubatorTemp(massTemp)
            myDB.insertToDbIncubatorDamp(massDamp)
            myDB.insertToDbIncubatorAiring(massAiring)
            myDB.insertToDbIncubatorOver(massOver)

            addChart()
        }
        builder.setNegativeButton(
            "Нет"
        ) { dialogInterface, i -> }

        builder.create().show()
    }

    private fun addChart() {
        val incubatorMenuFragment: IncubatorMenuFragment = IncubatorMenuFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, incubatorMenuFragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }

    override fun onClick(position: Int, day: Int) {
        TODO("Not yet implemented")
    }

}