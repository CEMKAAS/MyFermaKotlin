package com.hfad.myferma.AddPackage

import android.content.DialogInterface
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.InfoFragment
import com.hfad.myferma.R
import com.hfad.myferma.SettingsFragment
import com.hfad.myferma.db.MyConstanta
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.util.Calendar
import java.util.TimeZone

class UpdateProductFragment : Fragment() {
    private lateinit var textUnit: TextView
    private lateinit var titleExpenses: TextInputLayout
    private lateinit var titleCount: TextInputLayout
    private lateinit var titleData: TextInputLayout
    private lateinit var titlePrice: TextInputLayout
    private lateinit var menu: TextInputLayout
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var writeOffSpiner: AutoCompleteTextView
    private lateinit var productDB: ProductDB
    private var id: String? = null
    private var oldCount: String? = null
    private lateinit var myDB: MyFermaDatabaseHelper
    private var addSum = 0.0
    private var saleSum = 0.0
    private var writeOffSum = 0.0
    private var f: DecimalFormat? = null
    private var unit: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_update_product, container, false)

        //Подкючаемся к БД
        myDB = MyFermaDatabaseHelper(requireContext())

        // Получаем информацию из предыдущего фрагмента
        val bundle = this.arguments
        if (bundle != null) {
            productDB = bundle.getParcelable("fd")!!
            id = bundle.getString("id")
        }

        // Настройка аппбара и настройка стрелки, чтобы вернутся назад
        val appBar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        appBar.setNavigationOnClickListener(View.OnClickListener { requireActivity().supportFragmentManager.popBackStack() })
        appBar.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.more -> {
                    replaceFragment(InfoFragment())
                    appBar.title = "Информация"
                }

                R.id.setting -> {
                    replaceFragment(SettingsFragment())
                    appBar.title = "Мои настройки"
                }
            }
            true
        }

        // Подключаемся к фронту
        textUnit = layout.findViewById(R.id.text_unit)
        titleExpenses = layout.findViewById(R.id.tilleExpenses_input)
        titleCount = layout.findViewById(R.id.titleSale_input)
        titleData = layout.findViewById(R.id.daySale_input)
        titlePrice = layout.findViewById(R.id.priceEdit_input)
        menu = layout.findViewById(R.id.menu)
        writeOffSpiner = layout.findViewById(R.id.writeOff_spiner)
        updateButton = layout.findViewById(R.id.update_button)
        deleteButton = layout.findViewById(R.id.delete_button)

        // Настройка фронта
        textUnit.text = productDB.name
        unitString(productDB.name)
        titleExpenses.editText!!.setText(productDB.name)
        titleCount.editText!!.setText(f!!.format(productDB.disc))
        titleCount.suffixText = unit
        titleData.editText!!.setText(productDB.data)
        titlePrice.editText!!.setText(productDB.price.toString())

        if (R.drawable.baseline_cottage_24 == productDB.price) {
            writeOffSpiner.setText("На собственные нужды", false)
        } else {
            writeOffSpiner.setText("На утилизацию", false)
        }

        // сохраняем значение, которое было изначально
        oldCount = titleCount.editText!!
            .text.toString().trim { it <= ' ' }
            .replace(",".toRegex(), ".").replace("[^\\d.]".toRegex(), "")

        // Настройка календаря
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

       val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder)
            .setTitleText("Выберите дату")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) //Todo выбирать дату из EditText
            .build()

        titleData.editText!!.setOnClickListener {
            datePicker.show(requireActivity().supportFragmentManager, "wer")
            datePicker.addOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener<Any?> { selection ->
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = selection as Long
                val format = java.text.SimpleDateFormat("dd.MM.yyyy")
                val formattedDate: String = format.format(calendar.time)
                    titleData.editText!!.setText(formattedDate)
                })
        }

        // Настройка видимости фронта
        titleExpenses.visibility = View.GONE
        menu.visibility = View.GONE

        //Для товаров
        if ((id == "Мои Товары")) {
            titlePrice.visibility = View.GONE

            // для покупок
        } else if ((id == "Мои Покупки")) {

            titleExpenses.visibility = View.VISIBLE
            textUnit.visibility = View.GONE
            titlePrice.visibility = View.GONE
            titleCount.hint = "Цена"
            titleCount.helperText = "Укажите цену за товар"
            titleCount.editText!!.setText(productDB.disc.toString())

            // для списаний
        } else if ((id == "Мои Списания")) {
            titlePrice.visibility = View.GONE
            menu.visibility = View.VISIBLE
        }

        updateButton.setOnClickListener {
            when (id) {
                "Мои Товары" -> {
                    myProduct()
                }
                "Мои Продажи" -> {
                    mySale()
                }
                "Мои Покупки" -> {
                    myExpenses()
                }
                "Мои Списания" -> {
                    myWriteOff()
                }
            }
        }

        deleteButton.setOnClickListener {

            val product = textUnit.text.toString()
            val count = oldCount!!

            // Проверяем если мы удалим в продажах, товаров и списаниях, уйдем ли мы в минус
            if ((id == "Мои Товар") || (id == "Мои Продажи") || (id == "Мои Списания")) {
                // Проверяем если мы удалим, уйдем ли мы в минус

                if (sumDelete(product, count) < 0) {

                    val builder = MaterialAlertDialogBuilder(requireContext())
                    builder.setTitle("Вы в минусе!")
                    builder.setMessage(
                        "Если вы удалите данную позицию вы уйдете в минус!\n" +
                                "Вы действительно хотите это сделать ? "
                    )
                    builder.setPositiveButton("Да"
                    ) { dialogInterface, i -> delete() }

                    builder.setNegativeButton("Нет"
                    ) { dialog, which -> }

                    builder.show()
                } else {
                    delete()
                }

                // удаляем, если это покупки
            } else {
                delete()
            }
        }
        return layout
    }

    // Мои продукты
    private fun myProduct() {

        val product = textUnit.text.toString()
        val count = titleCount.editText!!.text.toString().trim { it <= ' ' }
            .replace(",".toRegex(), ".").replace("[^\\d.]".toRegex(), "")

        val data = titleData.editText!!.text.toString().split("\\.".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()

        //убираем ошибку
        titleCount.isErrorEnabled = false
        titleData.isErrorEnabled = false

        //вывод ошибки
        if (count == "") {
                titleCount.error = "Введите кол-во!"
                titleCount.error
        } else if (containsEgg(product, count)) {
            titleCount.error = "Яйца не могут быть дробными..."
            titleCount.error
        } else if (sum(product, count) < 0) {
            titleCount.error = "Столько товара нет на складе!\nу Вас списано " + (saleSum + writeOffSum)
            titleCount.error
        } else {
            myDB.updateData(productDB.id.toString(), product, count, data[0], data[1], data[2])
            replaceFragment(AddManagerFragment())
        }
    }

    //Мои продажи
    private fun mySale() {
        val product = textUnit.text.toString()
        val count = titleCount.editText!!.text.toString().trim { it <= ' ' }
            .replace(",".toRegex(), ".").replace("[^\\d.]".toRegex(), "")
        val data = titleData.editText!!.text.toString().split("\\.".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val sale = titlePrice.editText!!.text.toString()

        //убираем ошибку
        titleCount.isErrorEnabled = false
        titleData.isErrorEnabled = false
        titlePrice.isErrorEnabled = false

        //вывод ошибки
        if ((count == "") || (sale == "")) {
            if ((count == "")) {
                titleCount.error = "Введите кол-во!"
                titleCount.error
            }
            if ((sale == "")) {
                titlePrice.error = "Укажите цену"
                titlePrice.error
            }

        } else if (containsEgg(product, count)) {
            titleCount.error = "Яйца не могут быть дробными..."
            titleCount.error

        } else if (sum(product, count) < 0) {

            titleCount.error = "Столько товара нет на складе!\nу Вас добавленно $addSum списано $writeOffSum"
            titleCount.error

        } else {
            myDB.updateDataSale(productDB.id.toString(), product, count, data[0], data[1], data[2], sale.toDouble())
            replaceFragment(AddManagerFragment())
        }
    }

    // Мои продажи
    private fun myExpenses() {
        val product = titleExpenses.editText!!.text.toString()
        val count = titleCount.editText!!.text.toString().trim { it <= ' ' }
            .replace(",".toRegex(), ".").replace("[^\\d.]".toRegex(), "")
        val data = titleData.editText!!.text.toString().split("\\.".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()

        //убираем ошибку
        titleExpenses.isErrorEnabled = false
        titleCount.isErrorEnabled = false
        titleData.isErrorEnabled = false

        //вывод ошибки
        if ((count == "")  || (product == "")) {
            if ((count == "")) {
                titleCount.error = "Введите кол-во!"
                titleCount.error
            }
            if ((product == "")) {
                titleExpenses.error = "Укажите название"
                titleExpenses.error
            }
        } else if (containsEgg(product, count)) {
            titleCount.error = "Яйца не могут быть дробными..."
            titleCount.error
        } else {
            myDB.updateDataExpenses(productDB.id.toString(), product, count, data[0], data[1], data[2])
            replaceFragment(AddManagerFragment())
        }
    }

    //Мои списания
    private fun myWriteOff() {
        val product = textUnit.text.toString()
        val count = titleCount.editText!!.text.toString().trim { it <= ' ' }
            .replace(",".toRegex(), ".").replace("[^\\d.]".toRegex(), "")
        val data = titleData.editText!!.text.toString().split("\\.".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()

        //убираем ошибку
        titleCount.isErrorEnabled = false
        titleData.isErrorEnabled = false

        // Настройка картинок
        var statusDrawable = R.drawable.baseline_cottage_24

        if (writeOffSpiner.text.toString() == "На утилизацию") {
            statusDrawable = R.drawable.baseline_delete_24
        }

        //вывод ошибки
        if (count == "") {
                titleCount.error = "Введите кол-во!"
                titleCount.error

        } else if (containsEgg(product, count)) {
            titleCount.error = "Яйца не могут быть дробными..."
            titleCount.error
        } else if (sum(product, count) < 0) {
            titleCount.error = "Столько товара нет на складе!\nу Вас добавленно $addSum продано $saleSum"
            titleCount.error
        } else {
            myDB.updateDataWriteOff(productDB.id.toString(), product, count, data[0], data[1], data[2], statusDrawable)
            replaceFragment(AddManagerFragment())
        }
    }

    //Проверяем есть ли запятая или нет в яйцах
    private fun containsEgg(title: String, count: String): Boolean {
        if (title == "Яйца") {
            if (count.contains(".") || count.contains(",")) {
                return true
            }
        }
        return false
    }

    //Считаем сколько у нас товара на текущий момент
    private fun sum(product: String, count: String): Double {

        val diff = oldCount?.toDouble()!! - count.toDouble()
        var nowUnitProduct = 0.0
        val a = add(product)
        return when (id) {
            "Мои Товары" -> {
                nowUnitProduct = addSum - diff - saleSum - writeOffSum
                nowUnitProduct
            }
            "Мои Покупки" -> {
                nowUnitProduct = addSum - (saleSum - diff) - writeOffSum
                nowUnitProduct
            }
            "Мои Списания" -> {
                nowUnitProduct = addSum - writeOffSum - (writeOffSum - diff)
                nowUnitProduct
            }
            else -> {
                nowUnitProduct
            }
        }
    }

    //Считаем сколько у нас товара на текущий момент
    private fun sumDelete(product: String, count: String): Double {
        val a = add(product)
        val c = count.toDouble()
        return a - c
    }

    //Считаем сколько данного товара на данный момент
    fun add(product: String): Double {
        val cursor = myDB.selectTableNameAndSumCount(
            MyConstanta.TABLE_NAME,
            MyConstanta.TITLE,
            product,
            MyConstanta.DISCROTION
        )

        if (cursor.count != 0) {

            cursor.moveToNext()
            addSum = cursor.getDouble(1)
            cursor.close()

            val cursorSale =
                myDB.selectTableNameAndSumCount(
                    MyConstanta.TABLE_NAMESALE,
                    MyConstanta.TITLESale,
                    product,
                    MyConstanta.DISCROTIONSale
                )

            cursorSale.moveToNext()
            saleSum = cursorSale.getDouble(1)
            cursorSale.close()

            val cursorWriteOff = myDB.selectTableNameAndSumCount(
                MyConstanta.TABLE_NAMEWRITEOFF,
                MyConstanta.TITLEWRITEOFF,
                product, MyConstanta.DISCROTIONSWRITEOFF
            )

            cursorWriteOff.moveToNext()
            writeOffSum = cursorWriteOff.getDouble(2)
            cursorWriteOff.close()

        }
        return addSum-saleSum-writeOffSum
    }

    //Удаляем и возвращаемся назад
    fun delete() {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        builder.setTitle("Удалить " + textUnit.text.toString() + " ?")
        builder.setMessage("Вы уверены, что хотите удалить " + textUnit.text.toString() + " ?")
        builder.setPositiveButton("Да") { dialogInterface, i ->
            when (id) {
                "Мои Товары" -> {
                    myDB.deleteOneRow(productDB.id.toString())
                }
                "Мои Продажи" -> {
                    myDB.deleteOneRowSale(productDB.id.toString())
                }
                "Мои Покупки" -> {
                    myDB.deleteOneRowExpenses(productDB.id.toString())
                }
                "Мои Списания" -> {
                    myDB.deleteOneRowWriteOff(productDB.id.toString())
                }
            }
            replaceFragment(AddManagerFragment())
        }
        builder.setNegativeButton("Нет") { dialogInterface, i -> }
        builder.create().show()
    }

    //Возвращаемся назад при нажатии на клавишу
    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }

    private fun unitString(animals: String) {
        if (id != "Мои Покупки") {
            when (animals) {
                "Яйца" -> {
                    f = DecimalFormat("0")
                    unit = " шт."
                }
                "Молоко" -> {
                    f = DecimalFormat("0.00")
                    unit = " л."
                }
                "Мясо" -> {
                    f = DecimalFormat("0.00")
                    unit = " кг."
                }
                else -> {
                    f = DecimalFormat("0.00")
                    unit = " ед."
                }
            }
        } else {
            unit = " ₽"
            f = DecimalFormat("0.00")
        }
    }
}