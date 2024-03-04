package com.hfad.myferma.incubator.MenuIncubators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.hfad.myferma.R
import com.hfad.myferma.db.MyFermaDatabaseHelper
import com.hfad.myferma.incubator.AddIncubator.AddIncubatorBeginFragment
import com.hfad.myferma.incubator.NowIncubatorFragment


class HomeIncubatorFragment : Fragment(), View.OnClickListener, HomeAdapterIncubator.Listener {

    private lateinit var myDB: MyFermaDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private var id = mutableListOf<String>()
    private var name = mutableListOf<String>()
    private var type = mutableListOf<String>()
    private var data = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout: View = inflater.inflate(R.layout.fragment_home_incubator, container, false)
        myDB = MyFermaDatabaseHelper(requireContext())

        //настройка верхнего меню фаб кнопку
        val appBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        appBar.title = "Мои Инкубатор"

        val fab: ExtendedFloatingActionButton =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.setOnClickListener(this)
        fab.show()
        fab.text = "Добавить"
        fab.setIconResource(R.drawable.baseline_add_24)
        fab.icon

        recyclerView = layout.findViewById(R.id.recyclerView)

        storeDataInArrays()

        val incubatorImageAdapter = HomeAdapterIncubator(id, name, type, data, this, true)
        recyclerView.adapter = incubatorImageAdapter
        val layoutManager = GridLayoutManager(activity, 2)
        recyclerView.layoutManager = layoutManager

        return layout
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.extended_fab -> onClickButton(v, AddIncubatorBeginFragment())
        }
    }

    private fun storeDataInArrays() {
        val cursor = myDB.readAllDataIncubator()
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                if ((cursor.getString(8) == "0")) {
                    id.add(cursor.getString(0))
                    name.add(cursor.getString(1))
                    type.add(cursor.getString(2))
                    data.add(cursor.getString(3))
                }
            }
        }
        cursor.close()
    }

    private fun onClickButton(view: View?, fragment: Fragment?) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, (fragment)!!, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }

    private fun addChart(position: Int, name: String?, type: String?, data: String?, id: String?) {
        val incubatorMenuFragment = NowIncubatorFragment()
        val bundle = Bundle()
        bundle.putString("name", name)
        bundle.putString("type", type)
        bundle.putString("data", data)
        bundle.putString("id", id)

        incubatorMenuFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, incubatorMenuFragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }

    override fun onClick(position: Int, name: String, type: String, data: String, id: String) {
        addChart(position, name, type, data, id)
    }
}