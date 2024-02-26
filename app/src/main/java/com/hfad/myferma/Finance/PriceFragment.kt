package com.hfad.myferma.Finance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.R
import com.hfad.myferma.db.MyFermaDatabaseHelper

class PriceFragment : Fragment(), View.OnClickListener {
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var til: TextInputLayout
    private var tempList = mutableMapOf<String, Double>()
    private var productList = mutableListOf<String>()
    private lateinit var layout: View

    // Прогрузка фрагмента и его активных частей
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = inflater.inflate(R.layout.fragment_price, container, false)
        myDB = MyFermaDatabaseHelper(requireContext())

        // Настройка аппбара настройка стелочки назад
        val appBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        appBar.setNavigationOnClickListener { requireActivity().supportFragmentManager.popBackStack() }

        addArray()

        addMap()

        onBackPressed()

        editTxt()

        return layout
    }

    //Формируем массив из БД
    private fun addArray() {
        val cursor = myDB.readAllDataProduct()
        while (cursor.moveToNext()) {
            val product: String = cursor.getString(1)
            productList.add(product)
        }
        cursor.close()
    }

    // todo изменить sql
    private fun addMap() {
        val cursor = myDB.readAllDataPrice()

        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                val product: String = cursor.getString(1)

                if (productList.contains(product)) {

                    val price: Double = cursor.getDouble(2)
                    tempList[product] = price
                }
            }
        } else {

            for (product: String in productList) {
                tempList[product] = 0.0
            }
        }
        cursor.close()
    }

    // Настраиваем программно EditText
    private fun onBackPressed() {
        for (product: String in productList) {

            val container: LinearLayout = layout.findViewById<View>(R.id.mlayout) as LinearLayout
            container.orientation = LinearLayout.VERTICAL
            til = TextInputLayout(requireContext())

            // Установка наименования
            til.hint = "Товар " + product
            til.helperText = "Укажите цену за одну единицу товара"
            til.suffixText = "₽"
            til.isErrorEnabled = true
            til.tag = product

            //Установка задней иконки
            til.endIconMode = TextInputLayout.END_ICON_CUSTOM
            til.setEndIconDrawable(R.drawable.baseline_east_24)
            til.endIconDrawable
            til.setStartIconDrawable(R.drawable.baseline_shopping_bag_24)
            til.startIconDrawable

            val et = TextInputEditText(til.context)
            et.inputType = EditorInfo.TYPE_CLASS_NUMBER
            et.imeOptions = EditorInfo.IME_ACTION_GO
            til.addView(et)
            container.addView(til)
        }
    }

    // Назначаем EditText
    private fun editTxt() {
        for (entry: Map.Entry<String, Double> in tempList.entries) {

            val p: TextInputLayout = layout.findViewWithTag(entry.key)

            startIcon(p, entry.key)
            p.editText!!.setText(entry.value.toString())
            p.setEndIconOnClickListener(endIconClick(p, entry.key))
            p.editText!!.setOnEditorActionListener(keyboardClick(p, entry.key))
        }
    }

    // Запуск со значка
    private fun endIconClick(p: TextInputLayout, product: String): View.OnClickListener {
        return View.OnClickListener { checkInsertPrise(p, product) }
    }

    // Запуск с клавиатуры
    private fun keyboardClick(p: TextInputLayout, product: String): OnEditorActionListener {
        val editorListenerEgg =
            OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    checkInsertPrise(p, product)
                }
                false
            }
        return editorListenerEgg
    }

    //Проверка EditText
    private fun checkInsertPrise(p: TextInputLayout, product: String) {
        if (p.editText!!.text.toString() != "") {
            p.isErrorEnabled = false
            val price: Double = p.editText!!.text.toString().toDouble()
            inserPrice(product, price)
            p.setEndIconDrawable(R.drawable.baseline_done_24)
            p.endIconDrawable
        } else {
            p.error = "Введите цену!"
            p.error
        }
    }

    //Логика заполнение таблицы
    private fun inserPrice(animalsType: String, price: Double) {

        //Проверка была ли первый раз внесена цена или нет
        if (myDB.dataPrice(animalsType).count == 0) {
            myDB.insertToDbPrice(animalsType, price)
            Toast.makeText(
                activity,
                "Добавили цену за $animalsType $price руб.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                activity,
                "Обновили цену за $animalsType $price руб.",
                Toast.LENGTH_SHORT
            ).show()
            myDB.updateDataPrice(animalsType, price)
        }
    }

    //Настройка значков на продукции (в будущем можно дополнить)
    private fun startIcon(p: TextInputLayout, product: String?) {
        if ((product == "Яйца")) {
            p.setStartIconDrawable(R.drawable.ic_action_egg)
            p.startIconDrawable
        } else if ((product == "Молоко")) {
            p.setStartIconDrawable(R.drawable.ic_action_milk)
            p.startIconDrawable
        } else if ((product == "Мясо")) {
            p.setStartIconDrawable(R.drawable.ic_action_meat)
            p.startIconDrawable
        }
    }

    override fun onClick(v: View) {}
}