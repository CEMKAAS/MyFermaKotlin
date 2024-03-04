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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.ManagerMenuPackage.AddManagerFragment
import com.hfad.myferma.Chart.WriteOffChartFragment
import com.hfad.myferma.Settings.InfoFragment
import com.hfad.myferma.MainActivity
import com.hfad.myferma.R
import com.hfad.myferma.Settings.SettingsFragment
import com.hfad.myferma.db.MyConstanta
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.text.DecimalFormat

class WriteOffFragment : Fragment(), View.OnClickListener {
    private lateinit var resultText: TextView
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var radioButton1: RadioButton
    private lateinit var radioButton2: RadioButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var addWriteOffEdit: TextInputLayout
    private lateinit var animalsSpiner: AutoCompleteTextView

    private var tempList = mutableMapOf<String, Double>()
    private var productList = mutableListOf<String>()
    private var f = DecimalFormat("0.00")
    private var status: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_write_off, container, false)
        myDB = MyFermaDatabaseHelper(requireContext())

        productList.clear()

        addArray()
        add()

        // Установка EditText
        addWriteOffEdit = layout.findViewById(R.id.add_edit)
        addWriteOffEdit.editText!!.setOnEditorActionListener(editorListenerWriteOff)

        //Установка Radio
        radioGroup = layout.findViewById<View>(R.id.radioGroup) as RadioGroup
        radioButton1 = layout.findViewById<View>(R.id.radio_button_1) as RadioButton
        radioButton2 = layout.findViewById<View>(R.id.radio_button_2) as RadioButton

        // Установка Текста Todo Status воообще
        resultText = layout.findViewById<View>(R.id.result_text) as TextView

        //Установка спинера
        animalsSpiner = layout.findViewById<View>(R.id.animals_spiner) as AutoCompleteTextView
        animalsSpiner.setText(productList[0], false)

        val product: String = animalsSpiner.text.toString()

        resultText.text = f.format(tempList[product]) + unitString(product)

        animalsSpiner.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                val productClick: String = productList[position]
                resultText.text = f.format(tempList[productClick]) +  unitString(productClick)
                addWriteOffEdit.suffixText = unitString(productClick)
                addWriteOffEdit.error = null
                addWriteOffEdit.endIconDrawable = null
                addWriteOffEdit.endIconDrawable
            }
        addWriteOffEdit.setStartIconDrawable(R.drawable.baseline_shopping_bag_24)

        val addWriteOff: Button = layout.findViewById<View>(R.id.addWriteOff_button) as Button
        addWriteOff.setOnClickListener { onClickAddWriteOff() }

        val addWriteOffChar: Button = layout.findViewById<View>(R.id.writeOffChart_button) as Button
        addWriteOffChar.setOnClickListener { moveToNextFragment(WriteOffChartFragment()) }

        //настройка верхнего меню фаб кнопку
        val appBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        appBar.title = "Мои Списания"
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

        //Радио переключатель
        radioGroup.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                R.id.radio_button_1 -> status = 0
                R.id.radio_button_2 -> status = 1
            }
        }
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_edit -> addWriteOffEdit.editText!!
                .setOnEditorActionListener(editorListenerWriteOff)
        }
    }

    private val editorListenerWriteOff: OnEditorActionListener =
        OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                saveInTable()
            }
            false
        }

    private fun onClickAddWriteOff() {
        val activity: MainActivity = MainActivity()
        saveInTable()
        activity.closeKeyboard()
    }

    private fun saveInTable() {
        if (addWriteOffEdit.editText!!.text.toString() != "") {

            val animalsType: String = animalsSpiner.text.toString()

            val inputUnitString: String =
                addWriteOffEdit.editText!!.text.toString().replace(",".toRegex(), ".")
                    .replace("[^\\d.]".toRegex(), "")

            // Для ввода целых чисел или дробных
            if (animalsType == "Яйца") {
                if (inputUnitString.contains(".")) {
                    addWriteOffEdit.error = "Яйца не могут быть дробными..."
                    addWriteOffEdit.error
                    return
                }
            }
            val inputUnit: Double = inputUnitString.toDouble()

            //убираем ошибку
            addWriteOffEdit.isErrorEnabled = false

            if (comparison(animalsType, inputUnit)) {
                //проверка, что введены цены на товар
                if (status == 0) {
                    myDB.insertToDbWriteOff(animalsType, inputUnit, R.drawable.baseline_cottage_24)
                } else {
                    myDB.insertToDbWriteOff(animalsType, inputUnit, R.drawable.baseline_delete_24)
                }
                tempList[animalsType] = tempList[animalsType]!! - inputUnit

                resultText.text = f.format((tempList[animalsType])) + unitString(animalsType)

                addWriteOffEdit.editText!!.text.clear()
                addWriteOffEdit.setEndIconDrawable(R.drawable.baseline_done_24)
                addWriteOffEdit.endIconDrawable
            }
        } else {
            addWriteOffEdit.error = "Введите кол-во!"
            addWriteOffEdit.error
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

    //проверка что не уйдем в минус
    private fun comparison(animalsType: String, inputUnit: Double): Boolean {
        if (tempList[animalsType]!! - inputUnit < 0) {
            addWriteOffEdit.error = "Нет столько товара на складе"
            addWriteOffEdit.error
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