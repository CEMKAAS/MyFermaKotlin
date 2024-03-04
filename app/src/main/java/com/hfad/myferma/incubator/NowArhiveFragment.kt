package com.hfad.myferma.incubator

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.hfad.myferma.R
import com.hfad.myferma.db.MyFermaDatabaseHelper
import com.hfad.myferma.incubator.MenuIncubators.IncubatorMenuFragment

class NowArhiveFragment : Fragment(), View.OnClickListener, ListAdapterIncubator.Listener {
    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var type: TextView
    private lateinit var date: TextView
    private lateinit var egg: TextView
    private lateinit var littleBirds: TextView
    private var massId = mutableListOf<String>()
    private var massTemp = mutableListOf<String>()
    private var massDamp = mutableListOf<String>()
    private var massOver = mutableListOf<String>()
    private var massAiring = mutableListOf<String>()
    private lateinit var recyclerView: RecyclerView
    private var id: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout: View = inflater.inflate(R.layout.fragment_now_arhive, container, false)

        val bundle: Bundle? = arguments
        if (bundle != null) {
            id = bundle.getString("id").toString()
        }

        //убириаем фаб кнопку
        val fab: ExtendedFloatingActionButton =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.visibility = View.GONE

        myDB = MyFermaDatabaseHelper(requireContext())

        val appBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        appBar.menu.findItem(R.id.delete).isVisible = false

        //Загружаем информацию с массивы
        massId = setCursor(myDB.idIncubator(id), 0, 12)
        massTemp = setCursor(myDB.idIncubatorTemp(id), 1, 30)
        massDamp = setCursor(myDB.idIncubatorDamp(id), 1, 30)
        massOver = setCursor(myDB.idIncubatorOver(id), 1, 30)
        massAiring = setCursor(myDB.idIncubatorAiring(id), 1, 30)

        type = layout.findViewById(R.id.type)
        date = layout.findViewById(R.id.date)
        egg = layout.findViewById(R.id.egg)
        littleBirds = layout.findViewById(R.id.little_birds)
        appBar.title = massId[1]
        type.text = massId[2]
        date.text = massId[3] + " - " + massId[9]
        egg.text = "Было заложено: " + massId[5] + " яйц"

        littleBirds.text = "Вышло птенцов: " + massId[6]
        recyclerView = layout.findViewById(R.id.recyclerView)

        val listAdapterIncubator: ListAdapterIncubator =
            ListAdapterIncubator(massId, massTemp, massDamp, massOver, massAiring, this)

        recyclerView.adapter = listAdapterIncubator
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val add: Button = layout.findViewById<View>(R.id.begin) as Button
        add.setOnClickListener(this)
        return layout
    }

    private fun setCursor(cursor: Cursor, sizeBegin: Int, size: Int): MutableList<String> {
        cursor.moveToNext()

        val mass = mutableListOf<String>()

        for (i in sizeBegin..(size )) {
            mass.add(cursor.getString(i).toString())
        }
        cursor.close()
        return mass
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.begin -> beginEndIncubator()
        }
    }

    private fun beginEndIncubator() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Удалить " + massId[1] + " ?")
        builder.setMessage("Вы уверены, что хотите удалить " + massId[1] + " из архива ?")
        builder.setPositiveButton(
            "Да"
        ) { dialogInterface, i ->
            myDB.deleteOneRowIncubator(massId[0])
            val incubatorMenuFragment: IncubatorMenuFragment = IncubatorMenuFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.conteiner, incubatorMenuFragment, "visible_fragment")
                .addToBackStack(null)
                .commit()
        }
        builder.setNegativeButton(
            "Нет"
        ) { dialogInterface, i -> }
        builder.create().show()
    }

    override fun onClick(position: Int, day: Int) {

    }
}