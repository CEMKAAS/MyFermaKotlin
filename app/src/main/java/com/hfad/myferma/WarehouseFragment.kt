package com.hfad.myferma

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.myferma.menu.WriteOffFragment
import com.hfad.myferma.db.MyConstanta
import com.hfad.myferma.db.MyFermaDatabaseHelper
import com.hfad.myferma.incubator.MenuIncubators.IncubatorMenuFragment
import java.text.DecimalFormat


class WarehouseFragment : Fragment() {
    private lateinit var myDB: MyFermaDatabaseHelper
    private var productList = mutableListOf<String>()
    private val unit: String? = null
    private var f = DecimalFormat("0.00")
    private var eggFormat = DecimalFormat("0")

    //Создание фрагмента
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout: View = inflater.inflate(R.layout.fragment_warehouse, container, false)
        // Открываем БД
        myDB = MyFermaDatabaseHelper(requireContext())

        // Назначаем кнопки
        val writeOffFragment: Button = layout.findViewById<View>(R.id.writeOff_button) as Button
        writeOffFragment.setOnClickListener { onClickButtonOff(WriteOffFragment()) }

        val incubator: Button = layout.findViewById<View>(R.id.incubator_button) as Button
        incubator.setOnClickListener { onClickButtonOff(IncubatorMenuFragment()) }

        //Настройка листа
        addArray()

        // Настраиваем адаптер
        val recyclerView = layout.findViewById<RecyclerView>(R.id.recyclerView)
        val productAdapter = ProductAdapter(add(), "", " Шт.")
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return layout
    }

    private fun addArray() {
        val cursor: Cursor = myDB.readAllDataProduct()
        while (cursor.moveToNext()) {
            val product: String = cursor.getString(1)
            productList.add(product)
        }
        cursor.close()
    }

    private fun add(): Map<String, Double> {

        val tempList: MutableMap<String, Double> = HashMap()

        for (product: String in productList) {

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
        return tempList
    }

    private fun onClickButtonOff(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }
}