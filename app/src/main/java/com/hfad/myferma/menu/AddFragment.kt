package com.hfad.myferma.menu

import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.ManagerMenuPackage.AddManagerFragment
import com.hfad.myferma.Chart.AddChartFragment
import com.hfad.myferma.Settings.InfoFragment
import com.hfad.myferma.MainActivity
import com.hfad.myferma.R
import com.hfad.myferma.Settings.SettingsFragment
import com.hfad.myferma.db.MyConstanta
import com.hfad.myferma.db.MyFermaDatabaseHelper

class AddFragment : Fragment(), View.OnClickListener {
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var addEdit: TextInputLayout
    private lateinit var totalAddText: TextView
    private lateinit var animalsSpiner: AutoCompleteTextView
    private var tempList = mutableMapOf<String, Double>()
    private var productList = mutableListOf<String>()
    private var f = DecimalFormat("0.00")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layout = inflater.inflate(R.layout.fragment_add, container, false)
        myDB = MyFermaDatabaseHelper(requireContext())

        productList.clear()

        addArray()
        add()

        //Установка EditText
        addEdit = layout.findViewById(R.id.add_edit)
        addEdit.editText!!.setOnEditorActionListener(editorListenerAdd)

        // Установка спинера  Todo Status воообще
        animalsSpiner = layout.findViewById<View>(R.id.animals_spiner) as AutoCompleteTextView
        animalsSpiner.setText(productList[0], false)

        // Установка текста
        val product = animalsSpiner.text.toString()

        totalAddText = layout.findViewById<View>(R.id.totalAdd_text) as TextView
        totalAddText.text = f.format(tempList[product]) + unitString(product)

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

        animalsSpiner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val productClick = productList[position]
                totalAddText.text = f.format(tempList[productClick]) + unitString(productClick)
                addEdit.suffixText = unitString(productClick)
                addEdit.endIconDrawable = null
                addEdit.endIconDrawable
            }

        addEdit.setStartIconDrawable(R.drawable.baseline_shopping_bag_24)


        val add = layout.findViewById<Button>(R.id.add_button)
        add.setOnClickListener { onClickAdd() }

        val addChart = layout.findViewById<Button>(R.id.addChart_button)
        addChart.setOnClickListener { moveToNextFragment(AddChartFragment()) }

        if (savedInstanceState != null) {
            animalsSpiner.setText(productList[0], false)
        }

        return layout
    }

    override fun onStart() {
        super.onStart()
        val view = view
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
            val product = cursor.getString(1)
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_edit -> addEdit.editText!!.setOnEditorActionListener(editorListenerAdd)
        }
    }

    //Добавление продкции в таблицу через клавиатуру
    private val editorListenerAdd = TextView.OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_GO) {
            addInTable()
        }
        false
    }

    //Добавление продкции в таблицу через кнопку
    private fun onClickAdd() {
        val activity = MainActivity()
        addInTable()
        activity.closeKeyboard()
    }

    // Логика добавление продукции в таблицу
    private fun addInTable() {

        if (addEdit.editText!!.text.toString() != "" && animalsSpiner.text.toString() != "") {

            val animalsType = animalsSpiner.text.toString()
            val inputUnitString = addEdit.editText!!
                .text.toString().replace(",".toRegex(), ".").replace("[^\\d.]".toRegex(), "")

            // Для ввода целых чисел или дробных
            if (animalsType == "Яйца") {
                if (inputUnitString.contains(".")) {
                    addEdit.error = "Яйца не могут быть дробными..."
                    addEdit.error
                    return
                }
            }

            val inputUnit = inputUnitString.toDouble()

            //убираем ошибку
            addEdit.isErrorEnabled = false

            myDB.insertToDb(animalsType, inputUnit)
            tempList[animalsType] = tempList[animalsType]!! + inputUnit
            totalAddText.text = f.format(tempList[animalsType]) + unitString(animalsType)

            addEdit.editText!!.text.clear()
            addEdit.setEndIconDrawable(R.drawable.baseline_done_24)
            addEdit.endIconDrawable

            Toast.makeText(
                activity,
                "Добавлено $inputUnit${unitString(animalsType)}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            addEdit.error = "Введите кол-во!"
            addEdit.error
        }
    }

    //todo Что-то придумать!
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

    private fun moveToNextFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }
}