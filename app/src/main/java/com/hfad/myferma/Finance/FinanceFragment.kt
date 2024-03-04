package com.hfad.myferma.Finance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.Chart.FinanceChart2Fragment
import com.hfad.myferma.Chart.FinanceChartFragment
//import com.hfad.myferma.Chart.FinanceChart2Fragment
//import com.hfad.myferma.Chart.FinanceChartFragment
import com.hfad.myferma.Settings.InfoFragment
import com.hfad.myferma.ProductAdapter
import com.hfad.myferma.R
import com.hfad.myferma.Settings.SettingsFragment
import com.hfad.myferma.db.MyConstanta
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import kotlin.Throws

import com.hfad.myferma.db.MyFermaDatabaseHelper

class FinanceFragment : Fragment(), View.OnClickListener {
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var recyclerView: RecyclerView

    private lateinit var dataSheet: TextInputLayout
    private lateinit var buttonSheet: Button
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var dateFirst: Date
    private lateinit var dateEnd: Date

    private var tempList = mutableMapOf<String, Double>()
    private var productList = mutableListOf<String>()
    private var f = DecimalFormat("0.00")

    private lateinit var totalAmountText: TextView
    private lateinit var totalExpensesText: TextView
    private lateinit var clearFinanceText: TextView
    private lateinit var titleDate: TextView

    // Прогрузка фрагмента и его активных частей
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_finance, container, false)

        // Открываем БД
        myDB = MyFermaDatabaseHelper(requireContext())

        //Установка кнопки во фрагменте
        val priceButton: Button = layout.findViewById<View>(R.id.financeChart_button) as Button
        priceButton.setOnClickListener(this)

        val price2Button: Button = layout.findViewById<View>(R.id.financeChart2_button) as Button
        price2Button.setOnClickListener(this)

        //Настройка листа
        addArray()

        //Настройка мапы
        addProduct()

        // Настраиваем адаптер
        recyclerView = layout.findViewById(R.id.recyclerView)

        val productAdapter = ProductAdapter(tempList, "Продали ", " ₽")
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        //Общая сумма и Расходы
        val totalAmount: Double = totalSum(MyConstanta.PRICEALL, MyConstanta.TABLE_NAMESALE)
        val totalExpenses: Double = totalSum(MyConstanta.DISCROTIONEXPENSES, MyConstanta.TABLE_NAMEEXPENSES)
        val clearFinance: Double = totalAmount - totalExpenses

        totalAmountText = layout.findViewById<View>(R.id.totalAmount_text) as TextView
        totalExpensesText = layout.findViewById<View>(R.id.totalExpenses_text) as TextView
        clearFinanceText = layout.findViewById<View>(R.id.clearFinance_text) as TextView

        titleDate = layout.findViewById<View>(R.id.titleDate) as TextView

        totalAmountText.text = " " + f.format(totalAmount).toString() + " ₽"
        totalExpensesText.text = " " + f.format(totalExpenses).toString() + " ₽"
        clearFinanceText.text = " " + f.format(clearFinance).toString() + " ₽"

        //Создание модального bottomSheet
        showBottomSheetDialog()

        //Настройка кнопки и верхнего бара
        val appBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        appBar.menu.findItem(R.id.filler).isVisible = true
        appBar.menu.findItem(R.id.price).isVisible = true
        appBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.price -> moveToNextFragment(PriceFragment())
                R.id.filler -> bottomSheetDialog.show()
                R.id.more -> {
                    moveToNextFragment(InfoFragment())
                    appBar.title = "Информация"
                }

                R.id.setting -> {
                    moveToNextFragment(SettingsFragment())
                    appBar.title = "Мои настройки"
                }
            }
            true
        })

        // Настройка календаря на период
        val constraintsBuilder: CalendarConstraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

        val datePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setCalendarConstraints(constraintsBuilder)
            .setTitleText("Выберите даты")
            .setSelection(
                Pair.create(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()

        dataSheet.editText?.setOnClickListener {
            datePicker.show(requireActivity().supportFragmentManager, "wer")
            datePicker.addOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>> { selection ->
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                val calendar2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                val startDate = selection.first
                val endDate = selection.second
                calendar.timeInMillis = startDate
                calendar2.timeInMillis = endDate
                val format = SimpleDateFormat("dd.MM.yyyy")
                val formattedDate1 = format.format(calendar.time)
                val formattedDate2 = format.format(calendar2.time)
                try {
                    dateFirst = format.parse(formattedDate1)
                    dateEnd = format.parse(formattedDate2)
                } catch (e: ParseException) {
                    throw RuntimeException(e)
                }
                dataSheet.editText!!.setText("$formattedDate1-$formattedDate2")
                titleDate.text = "$formattedDate1-$formattedDate2"
            })
        }

        // Настройка кнопки в bottomSheet
        buttonSheet.setOnClickListener {
            try {
                filter()
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }

            recyclerView.adapter = ProductAdapter(tempList, "Продали ", " ₽")
            recyclerView.layoutManager = LinearLayoutManager(activity)
            bottomSheetDialog.dismiss()
        }

        //Съэкономлено пока отложим
//            TextView savedOnEggs_text = (TextView) view.findViewById(R.id.savedOnEggs_text);
//            TextView savedOnMilk_text = (TextView) view.findViewById(R.id.savedOnMilk_text);
//            TextView savedOnMeat_text = (TextView) view.findViewById(R.id.savedOnMeat_text);
//
//            savedOnEggs_text.setText(String.valueOf((mydbManager.sumSaleEggPrice())));
//            savedOnMilk_text.setText(String.valueOf((mydbManager.sumSaleMilkPrice())));
//            savedOnMeat_text.setText(String.valueOf((mydbManager.sumSaleMeatPrice())));

        return layout
    }

    // Добавляем продукцию
    private fun addArray() {
        val cursor = myDB.readAllDataProduct()
        if(cursor.count != 0) {
            while (cursor.moveToNext()) {
                val product: String = cursor.getString(1)
                productList.add(product)
            }
        }else{
            productList.add("Нет товаров")
        }
        cursor.close()
    }

    //Считаем сумму по каждому продукту, Который есть в списке
    private fun addProduct() {

        for (product: String in productList) {
            val cursor = myDB.selectTableNameAndSumCount(
                MyConstanta.TABLE_NAMESALE,
                MyConstanta.TITLESale,
                product,
                MyConstanta.DISCROTIONSale
            )

            if (cursor.count != 0) {
                cursor.moveToNext()
                if (tempList[product] == null) tempList[product] = cursor.getDouble(1)
            } else tempList[product] = 0.0
            cursor.close()
        }
    }

    //расчеты
    // Общая прибыль и общии расходы
    private fun totalSum(priceColumn: String, table: String): Double {
        val cursor = myDB.readDataAllSumTable(priceColumn, table)
        val sum = if (cursor.count != 0) {
            cursor.moveToNext()
            cursor.getDouble(0)
        } else 0.0
        cursor.close()
        return sum
    }

    //Кнопка
    override fun onClick(v: View) {
        when (v.id) {
            R.id.financeChart_button -> onClickFinanceChart(v, FinanceChartFragment())
            R.id.financeChart2_button -> onClickFinanceChart(v, FinanceChart2Fragment())
        }
    }

    //Функция кнопки
    private fun onClickFinanceChart(view: View?, fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, (fragment), "visible_fragment")
            .addToBackStack(null)
            .commit()
    }

    //Добавляем bottobSheet
    private fun showBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.fragment_bottom_finace)
        dataSheet = bottomSheetDialog.findViewById<TextInputLayout>(R.id.data_sheet)!!
        buttonSheet = bottomSheetDialog.findViewById<Button>(R.id.button_sheet)!!
    }

    @Throws(ParseException::class)
    fun filter() {

        tempList.clear()

        val format = SimpleDateFormat("dd.MM.yyyy")

        var totalAmountD = 0.0
        var totalExpenses = 0.0

        for (product: String in productList) {

            val cursor = myDB.idProduct1(MyConstanta.TABLE_NAMESALE, MyConstanta.TITLESale, product)

            if (cursor.count != 0) {
                while (cursor.moveToNext()) {
                    val data: String =
                        cursor.getString(3) + "." + cursor.getString(4) + "." + cursor.getString(5)

                    val dateNow: Date = format.parse(data) as Date

                    if ((dateFirst.before(dateNow) && dateEnd.after(dateNow)) || (dateFirst == dateNow) || (dateEnd == dateNow)) {

                        val productUnit: Double = cursor.getDouble(6)

                        if (tempList[product] == null) {
                            tempList[product] = productUnit

                        } else {

                            val sum: Double = tempList[product]!! + productUnit
                            tempList[product] = sum
                        }

                        totalAmountD += productUnit
                    }
                }
                cursor.close()
            } else tempList[product] = 0.0
        }

        val cursorExpens = myDB.readAllDataExpenses()
        if (cursorExpens.count != 0) {
            while (cursorExpens.moveToNext()) {
                val data: String =
                    cursorExpens.getString(3) + "." + cursorExpens.getString(4) + "." + cursorExpens.getString(5)
                val dateNow: Date = format.parse(data)

                if ((dateFirst.before(dateNow) && dateEnd.after(dateNow)) || (dateFirst == dateNow) || (dateEnd == dateNow)) {
                    val productUnit: Double = cursorExpens.getDouble(2)
                    totalExpenses += productUnit
                }
            }
            cursorExpens.close()
        }

        val clearFinance: Double = totalAmountD - totalExpenses

        totalAmountText.text = " " + f.format(totalAmountD).toString() + " ₽"
        totalExpensesText.text = " " + f.format(totalExpenses).toString() + " ₽"
        clearFinanceText.text = " " + f.format(clearFinance).toString() + " ₽"
    }

    private fun moveToNextFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }
}