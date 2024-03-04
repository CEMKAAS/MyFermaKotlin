package com.hfad.myferma.incubator.MenuIncubators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.hfad.myferma.R
import com.hfad.myferma.WarehouseFragment
import com.hfad.myferma.db.MyFermaDatabaseHelper


class IncubatorMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout: View = inflater.inflate(R.layout.fragment_incubator_menu, container, false)



        //убириаем фаб кнопку
        val fab: ExtendedFloatingActionButton =
            requireActivity().findViewById<View>(R.id.extended_fab) as ExtendedFloatingActionButton
        fab.visibility = View.GONE

        //настройка верхнего меню фаб кнопку
        val appBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        appBar.title = "Мои Инкубатор"
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        appBar.menu.findItem(R.id.delete).isVisible = true
        appBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.delete ->deleteAllIcubator()
            }
            true
        })
        appBar.setNavigationOnClickListener {moveToNextFragment(WarehouseFragment())}

        val tabLayout: TabLayout = layout.findViewById(R.id.tab)
        val viewPager2: ViewPager2 = layout.findViewById(R.id.view_pager)

        val menuAdapter = MenuAdapter(requireActivity())

        viewPager2.adapter = menuAdapter
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)!!.select()
            }
        })
        return layout
    }


    private fun deleteAllIcubator() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Удалить все инкубаторы?")
        builder.setMessage("Вы уверены, что хотите удалить все инкубаторы включая архив?")
        builder.setPositiveButton(
            "Да"
        ) { dialogInterface, i ->
            val myDB = MyFermaDatabaseHelper(requireContext())
            myDB.deleteAllIncubator()

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
    private fun moveToNextFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }

}