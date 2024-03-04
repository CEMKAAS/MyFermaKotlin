package com.hfad.myferma.incubator.AddIncubator

import android.database.Cursor
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.hfad.myferma.R
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class AddIncubatorBeginFragment : Fragment() {
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var nameIncubator: TextInputLayout
    private lateinit var data: TextInputLayout

    private lateinit var eggAll: TextInputLayout
    private lateinit var time1: TextInputLayout
    private lateinit var time2: TextInputLayout
    private lateinit var time3: TextInputLayout
    private lateinit var friz: CheckBox
    private lateinit var over: CheckBox
    private lateinit var animalsSpiner: AutoCompleteTextView

    private var id = mutableListOf<String>()
    private var name = mutableListOf<String>()

    private var massTemp = mutableListOf<String>()
    private var massDamp = mutableListOf<String>()
    private var massOver = mutableListOf<String>()
    private var massAiring = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_add_incubator_begin, container, false)

        //Подключение к базам данных
        myDB = MyFermaDatabaseHelper(requireContext())

        //Убираем Фаб
        val fab: ExtendedFloatingActionButton =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.visibility = View.GONE

        //Настройка аппбара
        val appBar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.title = "Новый Инкубатор"
        appBar.menu.findItem(R.id.delete).isVisible = false

        nameIncubator = layout.findViewById<TextInputLayout>(R.id.name_incubator)
        data = layout.findViewById<TextInputLayout>(R.id.data)
        eggAll = layout.findViewById<TextInputLayout>(R.id.eggAll)
        friz = layout.findViewById<CheckBox>(R.id.friz)
        over = layout.findViewById<CheckBox>(R.id.overturn)
        animalsSpiner = layout.findViewById<View>(R.id.animals_spiner) as AutoCompleteTextView
        time1 = layout.findViewById<TextInputLayout>(R.id.time1)
        time2 = layout.findViewById<TextInputLayout>(R.id.time2)
        time3 = layout.findViewById<TextInputLayout>(R.id.time3)

        // Настройка изначальных зачений инкубатора
        animalsSpiner.setText("Курицы", false)

        val cursor = myDB.readAllDataIncubator()
        if (cursor.count == 0) {
            nameIncubator.editText?.setText("Инкубатор №1")
        } else {
            cursor.moveToLast()
            nameIncubator.editText?.setText("Инкубатор №" + (cursor.getInt(0) + 1))
        }
        cursor.close()

        time1.editText?.setText("08:00")
        time2.editText?.setText("12:00")

        val calendar: Calendar = Calendar.getInstance()
        data.editText?.setText(
            calendar.get(Calendar.DAY_OF_MONTH).toString() + "." + (calendar.get(
                Calendar.MONTH
            ) + 1) + "." + calendar.get(Calendar.YEAR)
        )

        // Настройка календаря
        val constraintsBuilder: CalendarConstraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder)
            .setTitleText("Выберите дату закладки яиц")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        // Установка сколько всего яиц
        eggAll.editText?.setText("0")

        data.editText?.setOnClickListener {
            datePicker.show(requireActivity().supportFragmentManager, "wer")
            datePicker.addOnPositiveButtonClickListener(
                MaterialPickerOnPositiveButtonClickListener<Any?> { selection ->
                    val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.timeInMillis = (selection as Long?)!!
                    val format: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
                    val formattedDate: String = format.format(calendar.time)
                    data.editText?.setText(formattedDate)
                })
        }
        time1.editText?.setOnClickListener {
            val timeOne = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(8)
                .setMinute(0)
                .setTitleText("Установите время для отправки уведомлений")
                .build()
            timeOne.show(requireActivity().supportFragmentManager, "wer")
            timeOne.addOnPositiveButtonClickListener(View.OnClickListener { v1: View? ->
                val cale: Calendar = Calendar.getInstance()
                cale.set(Calendar.SECOND, 0)
                cale.set(Calendar.MINUTE, timeOne.minute)
                cale.set(Calendar.HOUR_OF_DAY, timeOne.hour)
                val format: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.UK)
                val formattedDate: String = format.format(cale.timeInMillis)
                time1.editText?.setText(formattedDate)
            })
        }
        time2.editText?.setOnClickListener(View.OnClickListener {
            val timeTwo = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Установите время для отправки уведомлений")
                .build()
            timeTwo.show(requireActivity().supportFragmentManager, "wer")
            timeTwo.addOnPositiveButtonClickListener(View.OnClickListener { v1: View? ->
                val cale1: Calendar = Calendar.getInstance()
                cale1.set(Calendar.SECOND, 0)
                cale1.set(Calendar.MINUTE, timeTwo.minute)
                cale1.set(Calendar.HOUR_OF_DAY, timeTwo.hour)
                val format: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.UK)
                val formattedDate: String = format.format(cale1.timeInMillis)
                time2.editText?.setText(formattedDate)
            })
        })
        time3.editText?.setOnClickListener {
            val timeThree = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(18)
                .setMinute(0)
                .setTitleText("Установите время для отправки уведомлений")
                .build()
            timeThree.show(requireActivity().supportFragmentManager, "wer")
            timeThree.addOnPositiveButtonClickListener(View.OnClickListener { v1: View? ->
                val cale2: Calendar = Calendar.getInstance()
                cale2.set(Calendar.SECOND, 0)
                cale2.set(Calendar.MINUTE, timeThree.minute)
                cale2.set(Calendar.HOUR_OF_DAY, timeThree.hour)
                val format: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.UK)
                val formattedDate: String = format.format(cale2.timeInMillis)
                time3.editText?.setText(formattedDate)
            })
        }

        //Кнопка
        val next: Button = layout.findViewById<View>(R.id.begin) as Button
        next.setOnClickListener {

            //Поиск архивных инкубаторов
            if (arhiveSeach()) {
                arhiveChoice(animalsSpiner.text.toString())
            } else {
                nextFragment(AddIncubatorFragment(), false)
            }

        }

        return layout
    }

    //Поиск архивных инкубаторов
    //Todo SQL
    private fun arhiveSeach(): Boolean {
        val cursor: Cursor = myDB.readAllDataIncubator()
        var arhive = false
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                if ((cursor.getString(8) == "1")
                    && (cursor.getString(2) == animalsSpiner.text.toString())
                ) {
                    id.add(cursor.getString(0))
                    name.add(cursor.getString(1))
                    arhive = true
                }
            }
        }
        cursor.close()
        return arhive
    }

    //Переход на вторую влакдаку
    //todo perenos vo fragment с архива
    private fun nextFragment(fragment: Fragment, arhiveBoolean: Boolean) {

        val bundle = Bundle()

        bundle.putString("name", nameIncubator.editText?.text.toString())
        bundle.putString("type", animalsSpiner.text.toString())
        bundle.putString("data", data.editText?.text.toString())
        bundle.putInt("eggAll", eggAll.editText?.text.toString().toInt())
        bundle.putString("time1", time1.editText?.text.toString())
        bundle.putString("time2", time2.editText?.text.toString())
        bundle.putString("time3", time3.editText?.text.toString())
        bundle.putBoolean("friz", friz.isChecked)
        bundle.putBoolean("over", over.isChecked)

        if (arhiveBoolean) {
            bundle.putStringArray("temp", massTemp.toTypedArray())
            bundle.putStringArray("damp", massDamp.toTypedArray())
            bundle.putStringArray("over", massOver.toTypedArray())
            bundle.putStringArray("airing", massAiring.toTypedArray())
        }
        bundle.putBoolean("arhive", arhiveBoolean)

        fragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }


    private fun arhiveChoice(
        typeBirds: String
    ) {
        val textView = TextView(activity)

        textView.text = "Выбрать данные из архива?\n" +
                "Данные которые Вы ввели не изменятся, температура, влажность, поворот и проветривание, будут добавлены из выбраного архива"
        textView.setPadding(20)
        textView.textSize = 18.0F
        textView.gravity = Gravity.CENTER

        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setCustomTitle(textView)

        val nameId = name.toTypedArray()
        val idFragment = id.toTypedArray()
        val n: IntArray = intArrayOf(0)

        builder.setSingleChoiceItems(
            nameId, 0
        ) { dialog, which ->
            n[0] = which
        }

        builder.setPositiveButton(
            "Да"
        ) { dialogInterface, i ->
            Toast.makeText(activity, ("Вы выбрали из архива: " + nameId[n[0]]), Toast.LENGTH_SHORT)
                .show()

            //TODO SQL AutoPerevorot
            setMassivToType(idFragment[n[0]])

            nextFragment(AddIncubatorFragment(), true)

        }
        builder.setNegativeButton("Нет") { dialogInterface, i ->
            nextFragment(
                AddIncubatorFragment(),
                false
            )
        }

        builder.show()
    }


    private fun setMassivToType(idFragment: String) {
        when (animalsSpiner.text.toString()) {
            "Курицы" -> {logicSetMassivToType(idFragment, 21) }
            "Гуси" -> {logicSetMassivToType(idFragment, 30) }
            "Перепела" -> {logicSetMassivToType(idFragment, 17) }
            "Индюки", "Утки" -> {logicSetMassivToType(idFragment, 28)
            }
        }

    }

    private fun logicSetMassivToType(idFragment: String, size: Int) {
        massTemp = setCursor(myDB.idIncubatorTemp(idFragment), 1, size)
        massDamp = setCursor(myDB.idIncubatorDamp(idFragment), 1, size)
        massOver = setCursor(myDB.idIncubatorOver(idFragment), 1, size)
        massAiring = setCursor(myDB.idIncubatorAiring(idFragment), 1, size)
    }

    private fun setCursor(cursor: Cursor, sizeBegin: Int, size: Int): MutableList<String> {
        cursor.moveToNext()

        val mass = mutableListOf<String>()
        for (i in sizeBegin..size) {
            mass.add(cursor.getString(i).toString())
        }
        cursor.close()
        return mass
    }

}
