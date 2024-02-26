package com.hfad.myferma.AddPackage

import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.MainActivity
import com.hfad.myferma.R
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
    private var unit: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layout = inflater.inflate(R.layout.fragment_add, container, false)
        myDB = MyFermaDatabaseHelper(requireContext())


        addArray()
        add()

        //Установка EditText
        addEdit = layout.findViewById(R.id.add_edit)
        addEdit.editText!!.setOnEditorActionListener(editorListenerAdd)

        //TODO Нужнали она?
        val fab =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.show()
        fab.text = "Журнал"
        fab.setIconResource(R.drawable.ic_action_book)
        fab.icon

        // Установка спинера  Todo Status воообще
        animalsSpiner = layout.findViewById<View>(R.id.animals_spiner) as AutoCompleteTextView
        animalsSpiner.setText(productList[0], false)

        // Установка текста
        val product = animalsSpiner.text.toString()
        unitString(product)

        totalAddText = layout.findViewById<View>(R.id.totalAdd_text) as TextView
        totalAddText.text = f.format(tempList[product]) + unit

        animalsSpiner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val productClick = productList[position]
                unitString(productClick)
                totalAddText.text = f.format(tempList[productClick]) + unit
                addEdit.suffixText = unit
                addEdit.endIconDrawable = null
                addEdit.endIconDrawable
            }

        addEdit.setStartIconDrawable(R.drawable.baseline_shopping_bag_24)

        val add = layout.findViewById<Button>(R.id.add_button)
        add.setOnClickListener(this)

        val addChart = layout.findViewById<Button>(R.id.addChart_button)
        addChart.setOnClickListener(this)

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
                cursorSale.moveToNext()
                tempList[product] = tempList[product]!! - cursorSale.getDouble(1)
                cursorSale.close()


                val cursorWriteOff = myDB.selectTableNameAndSumCount(
                    MyConstanta.TABLE_NAMEWRITEOFF,
                    MyConstanta.TITLEWRITEOFF,
                    product, MyConstanta.DISCROTIONSWRITEOFF
                )

                cursorWriteOff.moveToNext()
                tempList[product] = tempList[product]!! - cursorWriteOff.getDouble(1)
                cursorWriteOff.close()

            } else {
                tempList[product] = 0.0
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_edit -> addEdit.editText!!.setOnEditorActionListener(editorListenerAdd)
            R.id.add_button -> onClickAdd(v)
            R.id.addChart_button -> addChart(AddChartFragment())
            R.id.extended_fab -> addChart(AddManagerFragment())
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
    private fun onClickAdd(view: View?) {
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
            totalAddText.text = f.format(tempList[animalsType]) + unit

            addEdit.editText!!.text.clear()
            addEdit.setEndIconDrawable(R.drawable.baseline_done_24)
            addEdit.endIconDrawable

            Toast.makeText(activity, "Добавлено $inputUnit$unit", Toast.LENGTH_SHORT).show()
        } else {
            addEdit.error = "Введите кол-во!"
            addEdit.error
        }
    }

    //todo Что-то придумать!
    private fun unitString(animals: String) {
        when (animals) {
            "Яйца" -> {
                f = DecimalFormat("0")
                unit = " шт."
            }

            "Молоко" -> unit = " л."
            "Мясо" -> unit = " кг."
            else -> unit = " ед."

        }
    }

    private fun addChart(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }
}