package com.hfad.myferma.incubator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.hfad.myferma.R
import com.hfad.myferma.db.MyFermaDatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AllListFragment : Fragment(), View.OnClickListener, ListAdapterIncubator.Listener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var name: TextInputLayout
    private lateinit var size: TextInputLayout
    private lateinit var time1: TextInputLayout
    private lateinit var time2: TextInputLayout
    private lateinit var time3: TextInputLayout
    private lateinit var updateButton: Button

    private var massId = mutableListOf<String>()
    private var massTemp = mutableListOf<String>()
    private var massDamp = mutableListOf<String>()
    private var massOver = mutableListOf<String>()
    private var massAiring = mutableListOf<String>()


    private var dataFragment: Int = 0

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var myDB: MyFermaDatabaseHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout: View = inflater.inflate(R.layout.fragment_all_list, container, false)

        myDB = MyFermaDatabaseHelper(requireContext())

        var massId2 = arrayOf<String>()
        var massTemp2 = arrayOf<String>()
        var massDamp2 = arrayOf<String>()
        var massOver2 = arrayOf<String>()
        var massAiring2 = arrayOf<String>()


        val bundle: Bundle? = arguments
        if (bundle != null) {
            dataFragment = bundle.getInt("data")
            massId2 = bundle.getStringArray("id")!!
            massTemp2 = bundle.getStringArray("temp")!!
            massDamp2 = bundle.getStringArray("damp")!!
            massOver2 = bundle.getStringArray("over")!!
            massAiring2 = bundle.getStringArray("airing")!!
        }

        massId = massId2.toMutableList()
        massTemp = massTemp2.toMutableList()
        massDamp = massDamp2.toMutableList()
        massOver = massOver2.toMutableList()
        massAiring = massAiring2.toMutableList()

        //Создание модального bottomSheet
        showBottomSheetDialog()

        val appBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        appBar.title = "Мои Инкубатор"
        appBar.menu.findItem(R.id.delete).isVisible = false
        appBar.menu.findItem(R.id.filler).isVisible = true

        appBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.filler -> bottomSheetDialog.show()
            }
            true
        })

        appBar.setNavigationOnClickListener {
            backNow(NowIncubatorFragment())
        }

        recyclerView = layout.findViewById(R.id.recyclerView)
        val listAdapterIncubator =
            ListAdapterIncubator(massId, massTemp, massDamp, massOver, massAiring, this)
        recyclerView.adapter = listAdapterIncubator
        recyclerView.layoutManager = LinearLayoutManager(activity)


        return layout
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.update_button -> upDate(v)
        }
    }

    private fun upDate(view: View?) {

        massId[1] = name.editText!!.text.toString().trim { it <= ' ' }
        massId[4] = size.editText!!
            .text.toString().trim { it <= ' ' }.replace("\\D+".toRegex(), "")
        massId[10] = time1.editText!!.text.toString()
        massId[11] = time2.editText!!.text.toString()
        massId[12] = time3.editText!!.text.toString()

        //убираем ошибку
        name.isErrorEnabled = false

        //вывод ошибки
        if (name.editText!!.text.toString() == "") {
            if (name.editText!!.text.toString() == "") {
                name.error = "Укажите название!"
                name.error
            }
        } else {
            myDB.updateIncubator(massId)
            Toast.makeText(
                activity,
                "Обновлено ${massId[1]}, кол-во яиц ${massId[4]} шт.",
                Toast.LENGTH_SHORT
            ).show()
        }
        bottomSheetDialog.dismiss()
    }

    private fun addChart(fragment: Fragment, day: Int) {
        val bundle: Bundle = Bundle()

        bundle.putInt("data", day+1)
        bundle.putStringArray("id", massId.toTypedArray())
        bundle.putStringArray("temp", massTemp.toTypedArray())
        bundle.putStringArray("damp", massDamp.toTypedArray())
        bundle.putStringArray("over", massOver.toTypedArray())
        bundle.putStringArray("airing", massAiring.toTypedArray())
        bundle.putBoolean("allList", true)

        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()

    }


    //Добавляем bottobSheet
    private fun showBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.fragment_bottom_setting)

        name = bottomSheetDialog.findViewById(R.id.name_incubator)!!
        size = bottomSheetDialog.findViewById(R.id.eggAll)!!
        time1 = bottomSheetDialog.findViewById(R.id.time1)!!
        time2 = bottomSheetDialog.findViewById(R.id.time2)!!
        time3 = bottomSheetDialog.findViewById(R.id.time3)!!

        updateButton = bottomSheetDialog.findViewById(R.id.update_button)!!
        updateButton.setOnClickListener(this)

        name.editText!!.setText(massId[1])
        size.editText!!.setText(massId[4])
        time1.editText!!.setText(massId[10])
        time2.editText!!.setText(massId[11])
        time3.editText!!.setText(massId[12])

        time1.editText!!.setOnClickListener {
            val timeOne = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(8)
                .setMinute(0)
                .setTitleText("Установите время для отправки уведомлений")
                .build()
            timeOne.show(requireActivity().supportFragmentManager, "wer")
            timeOne.addOnPositiveButtonClickListener(View.OnClickListener { v1: View? ->
                val cale: Calendar = Calendar.getInstance()
                cale.set(Calendar.SECOND, 0)
                cale.set(Calendar.MINUTE, timeOne.minute)
                cale.set(Calendar.HOUR_OF_DAY, timeOne.hour)
                val format: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.UK)
                val formattedDate: String = format.format(cale.timeInMillis)
                time1.editText!!.setText(formattedDate)
            })
        }

        time2.editText!!.setOnClickListener {
            val timeTwo = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Установите время для отправки уведомлений")
                .build()
            timeTwo.show(requireActivity().supportFragmentManager, "wer")
            timeTwo.addOnPositiveButtonClickListener(View.OnClickListener { v1: View? ->
                val cale1: Calendar = Calendar.getInstance()
                cale1.set(Calendar.SECOND, 0)
                cale1.set(Calendar.MINUTE, timeTwo.minute)
                cale1.set(Calendar.HOUR_OF_DAY, timeTwo.hour)
                val format: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.UK)
                val formattedDate: String = format.format(cale1.timeInMillis)
                time2.editText!!.setText(formattedDate)
            })
        }

        time3.editText!!.setOnClickListener {
            val timeThree = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(18)
                .setMinute(0)
                .setTitleText("Установите время для отправки уведомлений")
                .build()
            timeThree.show(requireActivity().supportFragmentManager, "wer")
            timeThree.addOnPositiveButtonClickListener(View.OnClickListener { v1: View? ->
                val cale2: Calendar = Calendar.getInstance()
                cale2.set(Calendar.SECOND, 0)
                cale2.set(Calendar.MINUTE, timeThree.minute)
                cale2.set(Calendar.HOUR_OF_DAY, timeThree.hour)
                val format: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.UK)
                val formattedDate: String = format.format(cale2.timeInMillis)
                time3.editText!!.setText(formattedDate)
            })
        }

    }


    private fun backNow(fragment: Fragment) {
        val bundle: Bundle = Bundle()

        bundle.putString("name", massId[1])
        bundle.putString("data", massId[3])
        bundle.putString("id", massId[0])


        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()

       fragment.arguments = bundle

    }

    override fun onClick(position: Int, day: Int) {
        addChart(editDayIncubatorFragment(), day)
    }
}