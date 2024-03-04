package com.hfad.myferma.menu

import android.icu.text.DecimalFormat
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.hfad.myferma.R
import com.google.android.material.textfield.TextInputLayout
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.view.inputmethod.EditorInfo
import android.widget.Button
import com.hfad.myferma.MainActivity
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.hfad.myferma.ManagerMenuPackage.AddManagerFragment
import com.hfad.myferma.Chart.ExpensesChartFragment
import com.hfad.myferma.Settings.InfoFragment
import com.hfad.myferma.Settings.SettingsFragment
import com.hfad.myferma.db.MyConstanta
import com.hfad.myferma.db.MyFermaDatabaseHelper

class ExpensesFragment : Fragment(), View.OnClickListener {
    private lateinit var totalExpensesText: TextView
    private lateinit var expensesEditText: TextInputLayout
    private lateinit var menu: TextInputLayout
    private lateinit var expensesNameEditText: AutoCompleteTextView
    private var arrayListAnaimals = mutableListOf<String>()
    private var arrayAdapterAnimals: ArrayAdapter<String>? = null
    private var f = DecimalFormat("0.00")
    private lateinit var myDB: MyFermaDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_expenses, container, false)
        myDB = MyFermaDatabaseHelper(requireContext())
        arrayListAnaimals.clear()
        expensesEditText = layout.findViewById(R.id.expenses_editText)
        expensesNameEditText = layout.findViewById(R.id.expensesName_editText)
        menu = layout.findViewById<TextInputLayout>(R.id.menu)

        expensesEditText.editText!!.setOnEditorActionListener(editorListenerExpenses)
        expensesNameEditText.setOnEditorActionListener(editorListenerExpenses)

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

        val addExpenses = layout.findViewById<Button>(R.id.addExpenses_button)
        addExpenses.setOnClickListener { onClickAddExpenses() }

        val expensesChart = layout.findViewById<Button>(R.id.expensesChart_button)
        expensesChart.setOnClickListener { moveToNextFragment(ExpensesChartFragment()) }

        allExpenses()

        totalExpensesText = layout.findViewById<TextView>(R.id.totalExpenses_text)
        totalExpensesText.text = f.format(clearFinance).toString() + " ₽"
        return layout
    }

    override fun onStart() {
        super.onStart()
        val view: View? = view
        if (view != null) {
            arrayAdapterAnimals = ArrayAdapter(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                arrayListAnaimals
            )
            expensesNameEditText.setAdapter(arrayAdapterAnimals)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.expenses_editText -> expensesEditText.editText!!
                .setOnEditorActionListener(editorListenerExpenses)
        }
    }

    private val editorListenerExpenses: OnEditorActionListener =
        OnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_NEXT -> {
                    Toast.makeText(activity, "Добавлено!", Toast.LENGTH_SHORT)
                    menu.isErrorEnabled = false
                }

                EditorInfo.IME_ACTION_GO -> expensesInTable()
            }
            false
        }

    private fun onClickAddExpenses() {
        val activity: MainActivity = MainActivity()
        expensesInTable()
        activity.closeKeyboard()
    }

    private fun expensesInTable() {
        menu.isErrorEnabled = false

        if (expensesEditText.editText!!
                .text.toString() != "" && expensesNameEditText.text
                .toString() != ""
        ) {
            val expensesName: String = expensesNameEditText.text.toString()
            val inputExpensesString: String =
                expensesEditText.editText!!.text.toString()

            //убираем ошибку
            expensesEditText.isErrorEnabled = false
            if (!arrayListAnaimals.contains(expensesName)) {

                arrayListAnaimals.add(expensesName)
                arrayAdapterAnimals = ArrayAdapter(
                    requireContext().applicationContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayListAnaimals
                )
                expensesNameEditText.setAdapter(arrayAdapterAnimals)
            }

            val inputExpenses: Double =
                inputExpensesString.replace(",".toRegex(), ".").replace("[^\\d.]".toRegex(), "")
                    .toDouble()

            myDB.insertToDbExpenses(expensesName, inputExpenses)

            Toast.makeText(activity, "Добавлено!", Toast.LENGTH_SHORT).show()

            expensesEditText.setEndIconDrawable(R.drawable.baseline_done_24)
            expensesEditText.endIconDrawable
            totalExpensesText.text = f.format(clearFinance).toString() + " ₽"
            expensesNameEditText.text.clear()
            expensesEditText.editText!!.text.clear()

        } else {
            if ((expensesEditText.editText!!.text.toString() == "")) {
                expensesEditText.error = "Введите цену!"
                expensesEditText.error
            }
            if ((expensesNameEditText.text.toString() == "")) {
                menu.error = "Введите товар"
                menu.error
            }
        }
    }

    private fun allExpenses() {
        val cursor = myDB.readDataExpensesGroup()
        if (cursor.count != 0) while (cursor.moveToNext()) arrayListAnaimals.add(cursor.getString(1))
        cursor.close()
    }

    private fun moveToNextFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }

    //расчеты
    // Общая прибыль и общии расходы
    private fun totalSum(priceColumn: String, table: String): Double {
        val cursor = myDB.readDataAllSumTable(priceColumn, table)
        cursor.moveToNext()
        val sum = if (cursor.count != 0) cursor.getDouble(0)
        else 0.0
        cursor.close()
        return sum
    }

    //Считает чистую прибыль(тяжелый способо, но пока так)
    private val clearFinance: Double
        get() {
            val totalAmount: Double = totalSum(MyConstanta.PRICEALL, MyConstanta.TABLE_NAMESALE)
            val totalExpenses: Double =
                totalSum(MyConstanta.DISCROTIONEXPENSES, MyConstanta.TABLE_NAMEEXPENSES)
            return totalAmount - totalExpenses
        }
}