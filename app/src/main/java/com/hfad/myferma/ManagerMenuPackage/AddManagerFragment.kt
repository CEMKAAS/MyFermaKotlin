package com.hfad.myferma.ManagerMenuPackage

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.Settings.InfoFragment
import com.hfad.myferma.R
import com.hfad.myferma.Settings.SettingsFragment
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class AddManagerFragment : Fragment(), CustomAdapterAdd.Listener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyImageview: ImageView
    private lateinit var noData: TextView
    private lateinit var sixColumn: TextView
    private lateinit var dicsPrice: TextView
    private lateinit var myDB: MyFermaDatabaseHelper
    private var customAdapterAdd: CustomAdapterAdd? = null
    private var productListAll = mutableListOf<String>()
    private lateinit var animalsSpinerSheet: AutoCompleteTextView
    private lateinit var dataSheet: TextInputLayout
    private lateinit var buttonSheet: Button
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var product = mutableListOf<ProductDB>()
    private var productNow = mutableListOf<ProductDB>()
    private var dateFirst: Date? = null
    private var dateEnd: Date? = null
    private var statusPrice: String = ""
    private var priceDics: String = ""
    private var cursorManager: Cursor? = null
    private var visibility = 0
    private var myRow = 0
    private var appBarManager = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layout = inflater.inflate(R.layout.fragment_add_manager, container, false)

        product.clear()
        productNow.clear()

        //Подключение к базе данных
        myDB = MyFermaDatabaseHelper(requireContext())

        //Настройка кнопки и верхнего бара
        val appBar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.menu.findItem(R.id.filler).isVisible = true
        appBar.menu.findItem(R.id.magazine).isVisible = false
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        appBar.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
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
        }
        appBar.setNavigationOnClickListener { requireActivity().supportFragmentManager.popBackStack() }

        when (appBar.title.toString()) {
            "Мои Товары" -> {
                cursorManager = myDB.readAllData()
                visibility = View.GONE
                statusPrice = "Цена"
                priceDics = "Кол-во"
                myRow = R.layout.my_row
            }
            "Мои Продажи" -> {
                cursorManager = myDB.readAllDataSale()
                visibility = View.VISIBLE
                statusPrice = "Цена"
                priceDics = "Кол-во"
                myRow = R.layout.my_row_sale
            }
            "Мои Покупки" -> {
                cursorManager = myDB.readAllDataExpenses()
                visibility = View.GONE
                statusPrice = "Нет"
                priceDics = "Цена"
                myRow = R.layout.my_row
            }
            "Мои Списания" -> {
                cursorManager = myDB.readAllDataWriteOff()
                visibility = View.VISIBLE
                statusPrice = "Статус"
                priceDics = "Кол-во"
                myRow = R.layout.my_row_write_off
            }
        }

        appBarManager = appBar.title.toString()

        //Добавление товара в лист
        add()

        //Создание модального bottomSheet
        showBottomSheetDialog()

        // Настройка календаря на период
        val constraintsBuilder = CalendarConstraints.Builder()
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
                dataSheet.editText?.setText("$formattedDate1-$formattedDate2")
            })
        }

        // Настройка кнопки в bottomSheet
        buttonSheet.setOnClickListener {
            try {
                filter()
                customAdapterAdd = CustomAdapterAdd(productNow, myRow, this)
                recyclerView.adapter = customAdapterAdd
                recyclerView.layoutManager = LinearLayoutManager(activity)
                bottomSheetDialog.dismiss()
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }
        }

        //убириаем фаб кнопку
        val fab = requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.visibility = View.GONE

        //Создание отображения списка
        sixColumn = layout.findViewById(R.id.six_column)
        dicsPrice = layout.findViewById(R.id.dics_price)
        recyclerView = layout.findViewById(R.id.recyclerView)
        emptyImageview = layout.findViewById(R.id.empty_imageview)
        noData = layout.findViewById(R.id.no_data)

        sixColumn.visibility = visibility
        sixColumn.text = statusPrice
        dicsPrice.text = priceDics

        //Добавдение товаров в лист
        storeDataInArraysClass(cursorManager!!)

        //Создание адаптера
        customAdapterAdd = CustomAdapterAdd(productNow, myRow, this)
        recyclerView.adapter = customAdapterAdd
        recyclerView.layoutManager = LinearLayoutManager(activity)

        return layout
    }

    override fun onStart() {
        super.onStart()
        val view = view
        if (view != null) {
            val arrayAdapterProduct = ArrayAdapter(
                requireActivity().applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                productListAll
            )
            animalsSpinerSheet.setAdapter(arrayAdapterProduct)
        }
    }

    //Добавляем продукцию в список
    fun add() {
        if (statusPrice != "Нет") {
            val cursor = myDB.readAllDataProduct()

            while (cursor.moveToNext()) {
                val product = cursor.getString(1)
                productListAll .add(product)
            }
            cursor.close()

        } else {
            val cursor = myDB.readDataExpensesGroup()

            while (cursor.moveToNext()) {
                productListAll.add(cursor.getString(0))
            }
            cursor.close()
        }
        productListAll.add("Все")
    }


    //Добавляем базу данных в лист //TODO Надо скоратить
    private fun storeDataInArraysClass(cursor: Cursor) {
        if (cursor.count == 0) {
            emptyImageview.visibility = View.VISIBLE
            noData.visibility = View.VISIBLE
        } else if (statusPrice == "Нет") {
            storeDataInArraysClassLogic(cursor, 0)
        } else {
            storeDataInArraysClassLogic(cursor, 6)
        }
        productNow.addAll(product)
    }

    private fun storeDataInArraysClassLogic(cursor: Cursor, id: Int) {
        cursor.moveToLast()
        product.add(
            ProductDB(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getDouble(2),
                cursor.getString(3) + "." + cursor.getString(4) + "." + cursor.getString(5),
                cursor.getInt(id)
            )
        )
        while (cursor.moveToPrevious()) {
            product.add(
                ProductDB(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3) + "." + cursor.getString(4) + "." + cursor.getString(5),
                    cursor.getInt(id)
                )
            )
        }
        cursor.close()
        emptyImageview.visibility = View.GONE
        noData.visibility = View.GONE
    }

    @Throws(ParseException::class)
    fun filter() {

        productNow.clear()

        val animalsSpinerSheetText = animalsSpinerSheet.text.toString()
        val format = SimpleDateFormat("dd.MM.yyyy")

        if (animalsSpinerSheetText == "Все" && dataSheet.editText!!.text.toString() == "") {
            productNow.addAll(product)

        } else if (animalsSpinerSheetText == "Все" && dataSheet.editText!!.text.toString() != "") {

            for (productDB in product) {

                val dateNow = format.parse(productDB.data.toString())
                if (dateFirst!!.before(dateNow) && dateEnd!!.after(dateNow) || dateFirst == dateNow || dateEnd == dateNow) {
                    productNow.add(productDB)
                }

            }
        } else if (animalsSpinerSheetText != "Все" && dataSheet.editText!!.text.toString() == "") {

            for (productDB in product) {
                if (animalsSpinerSheetText == productDB.name) {
                    productNow.add(productDB)
                }
            }

        } else {

            for (productDB in product) {

                if (animalsSpinerSheetText == productDB.name) {
                    val dateNow = format.parse(productDB.data.toString())
                    if (dateFirst!!.before(dateNow) && dateEnd!!.after(dateNow) || dateFirst == dateNow || dateEnd == dateNow) {
                        productNow.add(productDB)
                    }
                }

            }
        }
    }

    //Добавляем bottobSheet
    private fun showBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireActivity())
        bottomSheetDialog.setContentView(R.layout.fragment_bottom)
        animalsSpinerSheet = bottomSheetDialog.findViewById(R.id.animals_spiner_sheet)!!
        animalsSpinerSheet.setText("Все", false)
        dataSheet = bottomSheetDialog.findViewById(R.id.data_sheet)!!
        buttonSheet = bottomSheetDialog.findViewById(R.id.button_sheet)!!
    }

    private fun addChart(productDB: ProductDB) {
        val updateProductFragment = UpdateProductFragment()
        val bundle = Bundle()
        bundle.putParcelable("fd", productDB)
        bundle.putString("id", appBarManager)
        updateProductFragment.arguments = bundle
        moveToNextFragment(updateProductFragment)
    }

    private fun moveToNextFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }

    override fun onClick(position: Int, productDB: ProductDB) {
        addChart(productDB)
    }
}