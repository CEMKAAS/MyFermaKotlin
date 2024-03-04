package com.hfad.myferma.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.ManagerMenuPackage.AddManagerFragment
import com.hfad.myferma.Chart.SaleChartFragment
import com.hfad.myferma.Settings.InfoFragment
import com.hfad.myferma.MainActivity
import com.hfad.myferma.R
import com.hfad.myferma.Settings.SettingsFragment
import com.hfad.myferma.db.MyConstanta
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.text.DecimalFormat

class SaleFragment : Fragment(), View.OnClickListener {
    private lateinit var resultText: TextView
    private lateinit var priceSale: TextView
    private lateinit var error: TextView
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var addSaleEdit: TextInputLayout
    private lateinit var addPrice: TextInputLayout
    private lateinit var animalsSpiner: AutoCompleteTextView

    private lateinit var checkPrice: CheckBox
    private var tempList = mutableMapOf<String, Double>()
    private var tempListPrice = mutableMapOf<String, Double>()
    private var productList = mutableListOf<String>()
    private var f = DecimalFormat("0.00")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_sale, container, false)
        myDB = MyFermaDatabaseHelper(requireContext())

        productList.clear()

        addArray()
        add()
        addMapPrice()

        // Установка EditText
        addSaleEdit = layout.findViewById(R.id.addSale_edit)
        addSaleEdit.editText!!.setOnEditorActionListener(editorListenerSale)

        // Установка EditText Price
        addPrice = layout.findViewById<View>(R.id.addPrice_edit) as TextInputLayout

        // Установка текста
        priceSale = layout.findViewById<View>(R.id.priceSale_text) as TextView
        resultText = layout.findViewById<View>(R.id.totalSale_text) as TextView
        error = layout.findViewById<View>(R.id.errorText) as TextView

        // Установка CheckBox
        checkPrice = layout.findViewById(R.id.check_price)

        // Установка Spinner
        animalsSpiner = layout.findViewById<View>(R.id.animals_spiner) as AutoCompleteTextView
        animalsSpiner.setText(productList[0], false)
        addPrice.visibility = View.GONE
        val product = animalsSpiner.text.toString()

        resultText.text =
            f.format(tempList[product]) + unitString(product) //Todo suffix+ f = new DecimalFormat("0.00");
        priceSale.text = unitString(product) + " Товара " + product + " " + tempListPrice[product] + " ₽"

        //AppBar
        val appBar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.menu.findItem(R.id.magazine).isVisible = true
        appBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.magazine -> moveToNextFragment(AddManagerFragment())
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

        //При выборе спинера происходят следующие изменения
        animalsSpiner.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                val productClick = productList[position]
                resultText.text = f.format(tempList[productClick]) +  unitString(productClick)
                priceSale.text = "${unitString(productClick)} Товара $productClick ${tempListPrice[productClick]} ₽"
                addSaleEdit.suffixText = unitString(productClick)
                addSaleEdit.endIconDrawable = null
                addSaleEdit.endIconDrawable
                addPrice.endIconDrawable = null
                addPrice.endIconDrawable
            }

        // установка иконки
        addSaleEdit.setStartIconDrawable(R.drawable.baseline_shopping_bag_24)

        // устнановка кнопок
        val addSale: Button = layout.findViewById<View>(R.id.addSale_button) as Button
        addSale.setOnClickListener { onClickAddSale() }

        val saleChart: Button = layout.findViewById<View>(R.id.saleChart_button) as Button
        saleChart.setOnClickListener { moveToNextFragment(SaleChartFragment()) }

        //Настройка чека
        checkPrice.setOnClickListener {
            if (checkPrice.isChecked) {
                addPrice.visibility = View.VISIBLE
            } else {
                addPrice.visibility = View.GONE
            }
            addSaleEdit.endIconDrawable = null
            addSaleEdit.endIconDrawable
            addPrice.endIconDrawable = null
            addPrice.endIconDrawable

        }

//        if (checkPrice.isChecked) {
//            addPrice.visibility = View.VISIBLE
//        }

        return layout
    }

    override fun onStart() {
        super.onStart()
        val view: View? = view
        if (view != null) {
            val arrayAdapterProduct = ArrayAdapter(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                productList
            )
            animalsSpiner.setAdapter(arrayAdapterProduct)
        }
    }

    //Добавляем продукцию в список
    private fun addArray() {
        val cursor = myDB.readAllDataProduct()
        while (cursor.moveToNext()) {
            val product: String = cursor.getString(1)
            productList.add(product)
        }
        cursor.close()
    }

    //Формируем список из БД
    private fun add() {
//TODO Возможно ли сделать все через SQL?
        for (product in productList) {

            val cursor = myDB.selectTableNameAndSumCount(
                MyConstanta.TABLE_NAME,
                MyConstanta.TITLE,
                product,
                MyConstanta.DISCROTION
            )

            if (cursor.count != 0) {

                cursor.moveToNext()
                tempList[product] = cursor.getDouble(1)
                cursor.close()

                val cursorSale =
                    myDB.selectTableNameAndSumCount(
                        MyConstanta.TABLE_NAMESALE,
                        MyConstanta.TITLESale,
                        product,
                        MyConstanta.DISCROTIONSale
                    )
                if (cursorSale.count != 0) {
                    cursorSale.moveToNext()
                    tempList[product] = tempList[product]!! - cursorSale.getDouble(1)
                }
                cursorSale.close()

                val cursorWriteOff = myDB.selectTableNameAndSumCount(
                    MyConstanta.TABLE_NAMEWRITEOFF,
                    MyConstanta.TITLEWRITEOFF,
                    product, MyConstanta.DISCROTIONSWRITEOFF
                )

                if (cursorWriteOff.count != 0) {
                    cursorWriteOff.moveToNext()
                    tempList[product] = tempList[product]!! - cursorWriteOff.getDouble(1)
                }
                cursorWriteOff.close()
            } else {
                tempList[product] = 0.0
            }
        }
    }


    private fun addMapPrice() {
        val cursor = myDB.readAllDataPrice()
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                val product: String = cursor.getString(1)
                val price: Double = cursor.getDouble(2)
                tempListPrice[product] = price
            }
        }
        cursor.close()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.addSale_edit -> addSaleEdit.editText!!
                .setOnEditorActionListener(editorListenerSale)
        }
    }

    private val editorListenerSale: OnEditorActionListener =
        OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                saleInTable()
            }
            false
        }

    private fun onClickAddSale() {
        val activity = MainActivity()
        saleInTable()
        activity.closeKeyboard()
    }

    //Основная логика добавления в журнал
    private fun saleInTable() {

        if (addSaleEdit.editText!!.text.toString() != "") {

            val animalsType: String = animalsSpiner.text.toString()
            val inputUnitString: String =
                addSaleEdit.editText!!.text.toString().replace(",".toRegex(), ".")
                    .replace("[^\\d.]".toRegex(), "")

            // Для ввода целых чисел или дробных
            if (animalsType == "Яйца") {
                if (inputUnitString.contains(".")) {
                    addSaleEdit.error = "Яйца не могут быть дробными..."
                    addSaleEdit.error
                    return
                }
            }

            // Ошибка если отсутсвует цифра
            if (checkPrice.isChecked && (addPrice.editText!!.text.toString() == "")) {
                addPrice.error = "Укажите цену!"
                addPrice.error
                return
            }

            val inputUnit = inputUnitString.toDouble()

            //убираем ошибку
            addSaleEdit.isErrorEnabled = false
            addPrice.isErrorEnabled = false

            //проверка, что введены цены на товар
            if (tempList.containsKey(animalsType)) {

                if (comparison(animalsType, inputUnit)) {

                    // проверка чекера
                    val priceSale = if (checkPrice.isChecked) {
                        addPrice.editText!!.text.toString().toDouble()
                    } else {
                        inputUnit * (tempListPrice[animalsType])!!
                    }

                    tempList[animalsType] = tempList[animalsType]!! - inputUnit
                    myDB.insertToDbSale(animalsType, inputUnit, priceSale)

                    resultText.text = f.format(tempList[animalsType]) + unitString(animalsType)
                    Toast.makeText(activity, "Вы заработали $priceSale ₽", Toast.LENGTH_SHORT)
                        .show()

                    // установка значков после выполнения
                    addSaleEdit.editText!!.text.clear()
                    error.text = ""
                    addSaleEdit.setEndIconDrawable(R.drawable.baseline_done_24)
                    addSaleEdit.endIconDrawable

                    addPrice.editText!!.text.clear()
                    addPrice.setEndIconDrawable(R.drawable.baseline_done_24)
                    addPrice.endIconDrawable
                }
            } else {
                error.text =
                    "Пожалуйста, введите цену за 1 ед. товара в разделе Мои Финансы, чтобы продать"
                Toast.makeText(activity, "Пожалуйста, укажите цену!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            addSaleEdit.error = "Введите кол-во!"
            addSaleEdit.error
        }
    }

    private fun unitString(animals: String): String {
        return when (animals) {
            "Яйца" -> {
                f = DecimalFormat("0")
                " шт."
            }

            "Молоко" -> " л."
            "Мясо" -> " кг."
            else -> " ед."
        }
    }

    //проверка что не уйдем в минус
    private fun comparison(animalsType: String, inputUnit: Double): Boolean {
        if (tempList[animalsType]!! - inputUnit < 0) {
            addSaleEdit.error = "Нет столько товара на складе"
            addSaleEdit.error
            return false
        }
        return true
    }

    private fun moveToNextFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }
}