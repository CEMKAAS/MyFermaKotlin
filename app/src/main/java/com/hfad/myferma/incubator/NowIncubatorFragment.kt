package com.hfad.myferma.incubator

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.R
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class NowIncubatorFragment : Fragment(), View.OnClickListener {
    private lateinit var addEdit: TextInputLayout
    private var daysNow: Long = 0
    private lateinit var name: TextView
    private lateinit var dataNow: TextView
    private lateinit var tempNow: TextView
    private lateinit var dampNow: TextView
    private lateinit var overturnNow: TextView
    private lateinit var airingNow: TextView
    private lateinit var ovoskopNow: TextView
    private lateinit var tempTomorrow: TextView
    private lateinit var dampTomorrow: TextView
    private lateinit var overturnTomorrow: TextView
    private lateinit var airingTomorrow: TextView
    private lateinit var ovoskopTomorrow: TextView
    private lateinit var tomorrow: TextView


    private lateinit var myDB: MyFermaDatabaseHelper
    private var massId = mutableListOf<String>()
    private var massTemp = mutableListOf<String>()
    private var massDamp = mutableListOf<String>()
    private var massOver = mutableListOf<String>()
    private var massAiring = mutableListOf<String>()
    private var dateToday = ""
    private var day: Int = 0
    private lateinit var foto: Button
    private lateinit var editDayIncubator: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout: View = inflater.inflate(R.layout.fragment_icubator_now, container, false)

        var nameFragment = ""
        var dataFragment = ""
        var idIncubator = ""

        val appBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        appBar.menu.findItem(R.id.delete).isVisible = false
        appBar.title = "Мои инкубатор"
        appBar.setNavigationOnClickListener {
            replaceFragment(HomeIncubatorFragment())
        }


        val bundle: Bundle? = arguments
        if (bundle != null) {
            nameFragment = bundle.getString("name").toString()
            dataFragment = bundle.getString("data").toString()
            idIncubator = bundle.getString("id").toString()
        }

        myDB = MyFermaDatabaseHelper(requireContext())
        name = layout.findViewById(R.id.name)
        dataNow = layout.findViewById(R.id.data_now)
        tempNow = layout.findViewById(R.id.temp_now)
        dampNow = layout.findViewById(R.id.damp_now)
        overturnNow = layout.findViewById(R.id.overturn_now)
        airingNow = layout.findViewById(R.id.airing_now)
        ovoskopNow = layout.findViewById(R.id.ovoskop_now)
        tempTomorrow = layout.findViewById(R.id.temp_tomorrow)
        dampTomorrow = layout.findViewById(R.id.damp_tomorrow)
        overturnTomorrow = layout.findViewById(R.id.overturn_tomorrow)
        airingTomorrow = layout.findViewById(R.id.airing_tomorrow)
        ovoskopTomorrow = layout.findViewById(R.id.ovoskop_tomorrow)
        tomorrow = layout.findViewById(R.id.tomorrow)
        addEdit = layout.findViewById(R.id.add_edit)
        addEdit.visibility = View.GONE

        //Кнопочки
        foto = layout.findViewById<View>(R.id.add_button) as Button
        foto.setOnClickListener(this)

        editDayIncubator = layout.findViewById<View>(R.id.addChart_button) as Button
        editDayIncubator.setOnClickListener(this)

        val editIncubator: Button = layout.findViewById<View>(R.id.dayDamp1) as Button
        editIncubator.setOnClickListener(this)

        val endIncubator: Button = layout.findViewById<View>(R.id.end) as Button
        endIncubator.setOnClickListener(this)

        // Скрываем временно не нужные кнопки
        ovoskopNow.visibility = View.GONE
        ovoskopTomorrow.visibility = View.GONE

        // Фаб кнопочка
        val fab: ExtendedFloatingActionButton =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.visibility = View.GONE

        // Настройка даты
        val calendar: Calendar = Calendar.getInstance()
        dateToday = (calendar.get(Calendar.DAY_OF_MONTH) + 1).toString() + "." + (calendar.get(
            Calendar.MONTH
        ) + 1) + "." + calendar.get(Calendar.YEAR)
        val myFormat = SimpleDateFormat("dd.MM.yyyy")

        try {
            val date1: Date = myFormat.parse(dataFragment)
            val date2: Date = myFormat.parse(dateToday)
            daysNow = date2.time - date1.time
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }

        // Установка дня
        day = TimeUnit.DAYS.convert(daysNow, TimeUnit.MILLISECONDS).toInt()

        //Загружаем информацию с массивы
        massId = setCursor(myDB.idIncubator(idIncubator), 0, 12)
        massTemp = setCursor(myDB.idIncubatorTemp(idIncubator), 1, 30)
        massDamp = setCursor(myDB.idIncubatorDamp(idIncubator), 1, 30)
        massOver = setCursor(myDB.idIncubatorOver(idIncubator), 1, 30)
        massAiring = setCursor(myDB.idIncubatorAiring(idIncubator), 1, 30)

        // Установка отображения инкубатора
        if (day > 30) {
            setEnd(day)
        } else {
            name.text = nameFragment
            dataNow.text = "Идет $day день "
            tempNow.text = "Температура должна быть " + massTemp[day] + " °C"
            dampNow.text = "Влажность должна быть " + massDamp[day] + " %"
            // установка перевoрота
            setOverturn(overturnNow, day)

            // установка проветривания
            setAiring(airingNow, day)

            //уставнока меню на завтра
            setMenuTomorrow(day)

            //установка овоскопа
            setOvoskop(day)

            //установка если день закончился
            setEnd(day)
        }

        return layout
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.end -> endInc()
            R.id.addChart_button -> addChart(editDayIncubatorFragment())
            R.id.dayDamp1 -> addChart(AllListFragment())
            R.id.add_button -> addChart(FotoIncubatorFragment())
        }
    }

    //Установка курсоров
    private fun setCursor(cursor: Cursor, sizeBegin: Int, size: Int): MutableList<String> {
        cursor.moveToNext()

        val mass = mutableListOf<String>()
        for (i in sizeBegin..size) {
            mass.add(cursor.getString(i).toString())
        }
        cursor.close()
        return mass
    }

    //настройка меню на завтра
    private fun setMenuTomorrow(day: Int) {

        if (massId[2] == "Курицы" && day == 21) {
            setGone()
        } else if (massId[2] == "Индюки" && day == 28) {
            setGone()
        } else if (massId[2] == "Гуси" && day == 30) {
            setGone()
        } else if (massId[2] == "Утки" && day == 28) {
            setGone()
        } else if (massId[2] == "Перепела" && day == 17) {
            setGone()
        } else {
            tempTomorrow.text = "Температура должна быть " + massTemp[1 + day] + " °C"
            dampTomorrow.text = "Влажность должна быть " + massDamp[1 + day] + " %"
            setAiring(airingTomorrow, day + 1)
            setOverturn(overturnTomorrow, day + 1)
            ovoskopTomorrow.visibility = View.GONE
        }
    }

    //установка переворота
    private fun setOverturn(overturn: TextView, day: Int) {
        if (massOver[day] == "нет") {
            overturn.text = "Переворачивать не нужно"
        } else if (massOver[day] == "Авто") {
            overturn.text = "Переворот автоматический"
        } else {
            overturn.text = "Переворачивать нужно ${massOver[day]} раз"
        }
    }

    //установка проветривания
    private fun setAiring(airing: TextView, day: Int) {
        if (massAiring[day] == "нет") {
            airing.text = "Провертивать не нужно"
        } else if (massAiring[day] == "Авто") {
            airing.text = "Провертривание автоматическое"
        } else {
            airing.text = "Провертивать нужно ${massAiring[day]}"
        }
    }

    //установка видимости на завтра
    private fun setGone() {
        tempTomorrow.visibility = View.GONE
        dampTomorrow.visibility = View.GONE
        overturnTomorrow.visibility = View.GONE
        airingTomorrow.visibility = View.GONE
        ovoskopTomorrow.visibility = View.GONE
        tomorrow.visibility = View.GONE
    }

    // Проверяем закончилось или нет
    private fun setEnd(day: Int) {
        if ((massId[2] == "Курицы") && day > 21) {
            beginIncubator()
        } else if ((massId[2] == "Индюки") && day > 28) {
            beginIncubator()
        } else if ((massId[2] == "Гуси") && day > 30) {
            beginIncubator()
        } else if ((massId[2] == "Утки") && day > 28) {
            beginIncubator()
        } else if ((massId[2] == "Перепела") && day > 17) {
            beginIncubator()
        }
    }

    // Завершаем работу инкубатора принудительно
    private fun endInc() {
        if ((massId[2] == "Курицы") && day <= 21) {
            beginEndIncubator()
        } else if ((massId[2] == "Индюки") && day <= 28) {
            beginEndIncubator()
        } else if ((massId[2] == "Гуси") && day <= 30) {
            beginEndIncubator()
        } else if ((massId[2] == "Утки") && day <= 28) {
            beginEndIncubator()
        } else if ((massId[2] == "Перепела") && day <= 17) {
            beginEndIncubator()
        } else {
            setEnd(day) //todo
        }
    }

    private fun beginEndIncubator() {
        val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Завершить " + name.text + " ?")
        builder.setMessage(
            ("Вы уверены, что хотите завершить " + name.text + " ?\n" +
                    "Еще слишком рано завершать, удалим или добавим в архив?")
        )
        builder.setPositiveButton(
            "В архив"
        ) { dialogInterface, i ->
            massId[8] = "1"
            massId[9] = dateToday
            myDB.updateIncubator(massId)
            myDB.updateIncubatorTemp(massTemp, massId[0])
            myDB.updateIncubatorDamp(massDamp, massId[0])
            myDB.updateIncubatorOver(massOver, massId[0])
            myDB.updateIncubatorAiring(massAiring, massId[0])
            addChart(IncubatorMenuFragment())
        }
        builder.setNegativeButton(
            "Удалить"
        ) { dialogInterface, i ->
            myDB.deleteOneRowIncubator(massId[0])
            addChart(IncubatorMenuFragment())
        }
        builder.create().show()
    }

    private fun beginIncubator() {
        tempNow.visibility = View.GONE
        dampNow.visibility = View.GONE
        overturnNow.visibility = View.GONE
        airingNow.visibility = View.GONE
        ovoskopNow.visibility = View.GONE
        foto.visibility = View.GONE
        editDayIncubator.visibility = View.GONE
        tempTomorrow.visibility = View.GONE
        dampTomorrow.visibility = View.GONE
        overturnTomorrow.visibility = View.GONE
        airingTomorrow.visibility = View.GONE
        tomorrow.visibility = View.GONE
        ovoskopTomorrow.visibility = View.VISIBLE
        addEdit.visibility = View.VISIBLE

        dataNow.text = "Поздравляем!"
        ovoskopTomorrow.text =
            "Вы заложили " + massId[4] + " яиц\nСколько птенцов у Вас вылупилось?"
        val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Поздравлем с появлением птенцов!")
        builder.setMessage("Мы сохранили Ваши данные в архив, чтобы вы не забыли параметры!\n Укажите кол-во птенцов прежде чем завершать")
        builder.setPositiveButton(
            "Завершить!"
        ) { dialogInterface, i ->
            massId[6] = addEdit.editText!!.text.toString()
            massId[8] = "1"
            massId[9] = dateToday
            myDB.updateIncubator(massId)
            myDB.updateIncubatorTemp(massTemp, massId[0])
            myDB.updateIncubatorDamp(massDamp, massId[0])
            myDB.updateIncubatorOver(massOver, massId[0])
            myDB.updateIncubatorAiring(massAiring, massId[0])
            addChart(IncubatorMenuFragment())
        }
        builder.setNegativeButton(
            "Внести птенцов"
        ) { dialogInterface, i -> }
        builder.create().show()
    }

    private fun addChart(fragment: Fragment) {
        val bundle: Bundle = Bundle()
        bundle.putInt("data", day)
        bundle.putString("type", massId[2])

        bundle.putStringArray("id", massId.toTypedArray())
        bundle.putStringArray("temp", massTemp.toTypedArray())
        bundle.putStringArray("damp", massDamp.toTypedArray())
        bundle.putStringArray("over", massOver.toTypedArray())
        bundle.putStringArray("airing", massAiring.toTypedArray())
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
        fragment.arguments = bundle
    }

    //установка овоскопа
    private fun setOvoskop(day: Int) {
        //todo доделать овоскоп
        if ((massId[2] == "Курицы")) {
            when (day) {
                6 -> ovoskopTomorrow.visibility = View.VISIBLE
                7 -> ovoskopNow.visibility = View.VISIBLE
                10 -> ovoskopTomorrow.visibility = View.VISIBLE
                11 -> ovoskopNow.visibility = View.VISIBLE
                15 -> ovoskopTomorrow.visibility = View.VISIBLE
                16 -> ovoskopNow.visibility = View.VISIBLE
            }
        } else if ((massId[2] == "Индюки")) {
            when (day) {
                7 -> ovoskopTomorrow.visibility = View.VISIBLE
                8 -> ovoskopNow.visibility = View.VISIBLE
                13 -> ovoskopTomorrow.visibility = View.VISIBLE
                14 -> ovoskopNow.visibility = View.VISIBLE
                24 -> ovoskopTomorrow.visibility = View.VISIBLE
                25 -> ovoskopNow.visibility = View.VISIBLE
            }
        } else if ((massId[2] == "Гуси")) {
            when (day) {
                8 -> ovoskopTomorrow.visibility = View.VISIBLE
                9 -> ovoskopNow.visibility = View.VISIBLE
                14 -> ovoskopTomorrow.visibility = View.VISIBLE
                15 -> ovoskopNow.visibility = View.VISIBLE
                20 -> ovoskopTomorrow.visibility = View.VISIBLE
                21 -> ovoskopNow.visibility = View.VISIBLE
            }
        } else if ((massId[2] == "Утки")) {
            when (day) {
                7 -> ovoskopTomorrow.visibility = View.VISIBLE
                8 -> ovoskopNow.visibility = View.VISIBLE
                14 -> ovoskopTomorrow.visibility = View.VISIBLE
                15 -> ovoskopNow.visibility = View.VISIBLE
                25 -> ovoskopTomorrow.visibility = View.VISIBLE
                26 -> ovoskopNow.visibility = View.VISIBLE
            }
        } else if ((massId[2] == "Перепела")) {
            when (day) {
                5 -> ovoskopTomorrow.visibility = View.VISIBLE
                6 -> ovoskopNow.visibility = View.VISIBLE
                12 -> ovoskopTomorrow.visibility = View.VISIBLE
                13 -> ovoskopNow.visibility = View.VISIBLE
            }
        } else {
            ovoskopNow.visibility = View.GONE
            ovoskopTomorrow.visibility = View.GONE
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }

}

