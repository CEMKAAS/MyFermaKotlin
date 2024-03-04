package com.hfad.myferma.Settings

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.R
import com.hfad.myferma.db.MyFermaDatabaseHelper


class SettingsFragment : Fragment() {
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var menuProducts: TextInputLayout
    private lateinit var addProduct: AutoCompleteTextView
    private var arrayListProduct = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout: View = inflater.inflate(R.layout.fragment_settings, container, false)

        addProduct = layout.findViewById<View>(R.id.add_product) as AutoCompleteTextView
        menuProducts = layout.findViewById(R.id.menuProduct)

        //Подключение к базе данных
        myDB = MyFermaDatabaseHelper(requireContext())

        addArray()

        val appBar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar.menu.findItem(R.id.magazine).isVisible = false
        appBar.menu.findItem(R.id.filler).isVisible = false
        appBar.menu.findItem(R.id.delete).isVisible = false
        appBar.navigationIcon = null
        appBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
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

        // устнановка кнопок
        val addProductButton: Button = layout.findViewById<View>(R.id.add_product_button) as Button
        addProductButton.setOnClickListener { setAddProduct() }

        val deleteProductButton: Button =
            layout.findViewById<View>(R.id.delete_product_button) as Button
        deleteProductButton.setOnClickListener { deleteProduct() }

        return layout
    }

    override fun onStart() {
        super.onStart()
        val view: View? = view
        if (view != null) {
            val arrayAdapterProduct = ArrayAdapter(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                arrayListProduct
            )
            addProduct.setAdapter(arrayAdapterProduct)
        }
    }

    //Добавление значий в лист из БД
    private fun addArray() {
        val cursor: Cursor = myDB.readAllDataProduct()
        while (cursor.moveToNext()) {
            val product: String = cursor.getString(1)
            arrayListProduct.add(product)
        }
        cursor.close()
    }

    private fun setAddProduct() {
        val nameProduct: String = addProduct.text.toString()
        menuProducts.isErrorEnabled = false
        if (arrayListProduct.size <= 7) {
            if (!arrayListProduct.contains(nameProduct)) {
                arrayListProduct.add(nameProduct)

                val arrayAdapterProduct = ArrayAdapter(
                    requireContext().applicationContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayListProduct
                )
                addProduct.setAdapter(arrayAdapterProduct)

                val builder: MaterialAlertDialogBuilder =
                    MaterialAlertDialogBuilder(requireContext())
                builder.setTitle("Добавить товар $nameProduct ?")
                builder.setMessage("Добавленный товар $nameProduct вы сможете добавлять на склад, продавать и списывать!")
                builder.setPositiveButton(
                    "Да"
                ) { dialogInterface, i ->
                    Toast.makeText(
                        activity,
                        "Вы добавили товар $nameProduct",
                        Toast.LENGTH_SHORT
                    ).show()
                    myDB.insertDataProduct(addProduct.text.toString(), 0)
                    myDB.insertToDbPrice(addProduct.text.toString(), 0.0)
                }
                builder.setNegativeButton(
                    "Нет"
                ) { dialogInterface, i -> }
                builder.show()
            } else {
                menuProducts.error = "Такой товар уже есть"
                menuProducts.error
            }
        } else {
            menuProducts.error = "Всего можно установить 10 товаров, вы превысили лимит!"
            menuProducts.error
        }
    }

    private fun deleteProduct() {
        val nameProduct: String = addProduct.text.toString()
        menuProducts.isErrorEnabled = false

        if (arrayListProduct.contains(nameProduct)) {
            val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            builder.setTitle("Удалить товар ${addProduct.text} ?")
            builder.setMessage(
                "Товар ${addProduct.text} больше не будет отображаться на складе, его нельзя будет добавить, продать или списать. Данные внесенные в журнал будут отображаться."
            )
            builder.setPositiveButton(
                "Да"
            ) { dialogInterface, i ->
                Toast.makeText(activity, "Вы удалили товар ", Toast.LENGTH_SHORT).show()

                //Удаляем из списка
                arrayListProduct.remove(nameProduct)
                val arrayAdapterProduct = ArrayAdapter(
                    requireContext().applicationContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayListProduct
                )
                addProduct.setAdapter(arrayAdapterProduct)

                // удаляем из БД в продуктах и цене
                myDB.deleteOneRowProduct(addProduct.text.toString())

                // чистим поле
                addProduct.text.clear()
            }
            builder.setNegativeButton(
                "Нет"
            ) { dialogInterface, i -> }
            builder.show()
        } else {
            menuProducts.error = "Такого товара нет"
            menuProducts.error
        }
    }

    private fun moveToNextFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }
}