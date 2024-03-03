package com.hfad.myferma

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.hfad.myferma.AddPackage.AddFragment
import com.hfad.myferma.AddPackage.AddManagerFragment
import com.hfad.myferma.AddPackage.UpdateProductFragment
import com.hfad.myferma.Finance.ExpensesPackage.ExpensesFragment
//import com.hfad.myferma.Chart.FinanceChart2Fragment
//import com.hfad.myferma.Chart.FinanceChartFragment
import com.hfad.myferma.Finance.FinanceFragment
import com.hfad.myferma.Finance.PriceFragment
import com.hfad.myferma.SalePackage.SaleFragment
import com.hfad.myferma.WriteOff.WriteOffFragment
import com.hfad.myferma.databinding.ActivityMainBinding
import com.hfad.myferma.db.MyFermaDatabaseHelper
import com.hfad.myferma.incubator.IncubatorMenuFragment
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fab: ExtendedFloatingActionButton
    private var isAllFabsVisible: Boolean? = null

    private val time1 = mutableListOf<String>()
    private val time2 = mutableListOf<String>()
    private val time3 = mutableListOf<String>()

    private lateinit var myDB: MyFermaDatabaseHelper

    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appBar = findViewById<MaterialToolbar>(R.id.topAppBar)

        if (savedInstanceState == null) {  //при повороте приложение не брасывается
            replaceFragment(WarehouseFragment())
            appBar.title = "Мой Склад" // При включении включает данный фрагмент
        }

        fab = findViewById(R.id.extended_fab)
        fab.visibility = View.GONE
        isAllFabsVisible = false

        myDB = MyFermaDatabaseHelper(this)

        //Назначение кнопок на нижней навигации
        binding.navView.setOnItemSelectedListener { item ->
            position = item.itemId
            when (position) {
                R.id.warehouse_button -> {
                    replaceFragment(WarehouseFragment())
                    appBar.title = "Мой Склад"
                    fab.hide()
                    fab.visibility = View.GONE
                }

                R.id.finance_button -> {
                    replaceFragment(FinanceFragment())
                    appBar.title = "Мои Финансы"
                    fab.hide()
                    fab.visibility = View.GONE
                }

                R.id.add_button -> {
                    replaceFragment(AddFragment())
                    fab.hide()
                    fab.visibility = View.GONE
                }

                R.id.sale_button -> {
                    replaceFragment(SaleFragment())
                    fab.hide()
                    fab.visibility = View.GONE
                }

                R.id.expenses_button -> {
                    replaceFragment(ExpensesFragment())
                    fab.hide()
                    fab.visibility = View.GONE
                }
            }
            true
        }

        // AppBar
        appBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.more -> {
                    replaceFragment(InfoFragment())
                    appBar.title = "Информация"
                    fab.hide()
                    fab.visibility = View.GONE
                }

                R.id.setting -> {
                    replaceFragment(SettingsFragment())
                    appBar.title = "Мои настройки"
                    fab.hide()
                    fab.visibility = View.GONE
                }

                R.id.delete -> beginIncubator()
            }
            true
        })
        appBar.menu.findItem(R.id.price).isVisible = false
        appBar.menu.findItem(R.id.magazine).isVisible = false
        appBar.menu.findItem(R.id.delete).isVisible = false
        appBar.menu.findItem(R.id.filler).isVisible = false

//        //убираем ботом навигацию и фабкнопку при вызове клавиатуры
//        KeyboardVisibilityEvent.setEventListener(
//            this,
//            object : KeyboardVisibilityEventListener() {
//                fun onVisibilityChanged(isOpen: Boolean) {
//                    Log.d(ContentValues.TAG, "onVisibilityChanged: Keyboard visibility changed")
//                    if (isOpen) {
//                        Log.d(ContentValues.TAG, "onVisibilityChanged: Keyboard is open")
//                        binding.navView.setVisibility(View.GONE)
//                        //                            fab.setVisibility(View.INVISIBLE);
//                        Log.d(ContentValues.TAG, "onVisibilityChanged: NavBar got Invisible")
//                    } else {
//                        Log.d(ContentValues.TAG, "onVisibilityChanged: Keyboard is closed")
//                        binding.navView.setVisibility(View.VISIBLE)
//                        //                            fab.setVisibility(View.VISIBLE);
//                        Log.d(ContentValues.TAG, "onVisibilityChanged: NavBar got Visible")
//                    }
//                }
//            })


//        if (getFragmentManager().findFragmentByTag("WriteOff") != null && getFragmentManager().findFragmentByTag("WriteOff").isVisible()) {
//            fba(WriteOffFragment.class);
//        }


//        if (getFragmentManager().findFragmentByTag("WriteOff") != null && getFragmentManager().findFragmentByTag("WriteOff").isVisible()) {
//            fba(WriteOffFragment.class);
//        }

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentByTag("visible_fragment")
            if (fragment is WarehouseFragment) {
                appBar.title = "Мой Склад"
                position = 0
                fab.hide()
                fab.visibility = View.GONE
                appBar.navigationIcon = null
                appBar.menu.findItem(R.id.price).isVisible = false
                appBar.menu.findItem(R.id.magazine).isVisible = false
                appBar.menu.findItem(R.id.delete).isVisible = false
                appBar.menu.findItem(R.id.filler).isVisible = false
            }
            if (fragment is FinanceFragment) {
                position = 1
                appBar.title = "Мои Финансы"
                fab.hide()
                fab.visibility = View.GONE

                appBar.menu.findItem(R.id.magazine).isVisible = false
                appBar.menu.findItem(R.id.delete).isVisible = false
                appBar.navigationIcon = null
            }
            if (fragment is PriceFragment) {
                appBar.title = "Моя Цена"
                position = 1
                fab.hide()
                fab.visibility = View.GONE
                appBar.menu.findItem(R.id.price).isVisible = false
                appBar.menu.findItem(R.id.magazine).isVisible = false
                appBar.menu.findItem(R.id.delete).isVisible = false
                appBar.menu.findItem(R.id.filler).isVisible = false
            }
            if (fragment is AddFragment) {
                appBar.title = "Мои Товары"
                fab.hide()
                fab.visibility = View.GONE
                position = 2
                appBar.menu.findItem(R.id.price).isVisible = false
                appBar.menu.findItem(R.id.delete).isVisible = false
                appBar.navigationIcon = null
                appBar.menu.findItem(R.id.filler).isVisible = false
            }
            if (fragment is SaleFragment) {
                appBar.title = "Мои Продажи"
                fab.hide()
                fab.visibility = View.GONE
                position = 3
                appBar.menu.findItem(R.id.price).isVisible = false
                appBar.menu.findItem(R.id.delete).isVisible = false
                appBar.navigationIcon = null
                appBar.menu.findItem(R.id.filler).isVisible = false
            }
            if (fragment is ExpensesFragment) {
                appBar.title = "Мои Покупки"
                fab.hide()
                fab.visibility = View.GONE
                position = 4
                appBar.menu.findItem(R.id.price).isVisible = false
                appBar.menu.findItem(R.id.delete).isVisible = false
                appBar.navigationIcon = null
                appBar.menu.findItem(R.id.filler).isVisible = false
            }
            if (fragment is InfoFragment) {
                appBar.title = "Информация"
                fab.hide()
                fab.visibility = View.GONE
                appBar.navigationIcon = null
                appBar.menu.findItem(R.id.price).isVisible = false
                appBar.menu.findItem(R.id.magazine).isVisible = false
                appBar.menu.findItem(R.id.delete).isVisible = false
                appBar.menu.findItem(R.id.filler).isVisible = false
            }
            if (fragment is WriteOffFragment) {
                appBar.title = "Мои Списания"
                fab.hide()
                fab.visibility = View.GONE
                position = 0
                appBar.menu.findItem(R.id.price).isVisible = false
                appBar.menu.findItem(R.id.delete).isVisible = false
                appBar.navigationIcon = null
                appBar.menu.findItem(R.id.filler).isVisible = false
            }
            if (fragment is IncubatorMenuFragment) {
                fbaShowBackIncubator()
                position = 0
                appBar.menu.findItem(R.id.price).isVisible = false
                appBar.menu.findItem(R.id.magazine).isVisible = false
                appBar.menu.findItem(R.id.filler).isVisible = false
            }
            if (fragment is UpdateProductFragment) {
                appBar.menu.findItem(R.id.price).isVisible = false
                appBar.menu.findItem(R.id.magazine).isVisible = false
                appBar.menu.findItem(R.id.filler).isVisible = false
            }
            binding.navView.menu.getItem(position).isChecked = true
        }

        // Добавляем основые продукты, если приложение открыто впервые
        add()
        //Реклама от яндекса
    }


    //Выводит приложение из сна для уведомления
    override fun onStart() {
        super.onStart()
        val manager = getSystemService(ALARM_SERVICE) as AlarmManager
        storeDataInArrays()
        val dat = Date()
        val cal_now = Calendar.getInstance()
        cal_now.time = dat
        val cal_alarm = Calendar.getInstance()
        cal_alarm.time = dat
        cal_alarm[Calendar.HOUR_OF_DAY] = 20
        cal_alarm[Calendar.MINUTE] = 0
        cal_alarm[Calendar.SECOND] = 0
        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1)
        }
        val myIntent = Intent(this@MainActivity, AlarmReceiver::class.java)
        manager[AlarmManager.RTC_WAKEUP, cal_alarm.timeInMillis] =  PendingIntent.getBroadcast(this@MainActivity, 0, myIntent, PendingIntent.FLAG_IMMUTABLE)
        sda(time1)
        sda(time2)
        sda(time3)
    }

    fun add() {
        val cursor: Cursor = myDB.readAllDataProduct()
        if (cursor.count == 0) {
            // Добавить в список
            myDB.insertDataProduct("Яйца", 1)
            myDB.insertDataProduct("Молоко", 0)
            myDB.insertDataProduct("Мясо", 0)

            // Добавить в добавление
            myDB.insertToDb("Яйца", 0.0)
            myDB.insertToDb("Молоко", 0.0)
            myDB.insertToDb("Мясо", 0.0)

            // Добавить в продажи
            myDB.insertToDbSale("Яйца", 0.0, 0.0)
            myDB.insertToDbSale("Молоко", 0.0, 0.0)
            myDB.insertToDbSale("Мясо", 0.0, 0.0)

            myDB.insertToDbPrice("Яйца", 0.0)
            myDB.insertToDbPrice("Молоко", 0.0)
            myDB.insertToDbPrice("Мясо", 0.0)
        }
        cursor.close()
    }

    private fun sda(time: List<String>) {
        val manager = getSystemService(ALARM_SERVICE) as AlarmManager
        val dat = Date()
        val cal_now = Calendar.getInstance()
        cal_now.time = dat
        val cal_alarm = Calendar.getInstance()
        for (i in time.indices) {
            if (time[i] == "0" || time[i] == "") {
                continue
            } else {
                val time11 = time[i].toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                cal_alarm.time = dat
                cal_alarm[Calendar.HOUR_OF_DAY] = time11[0].toInt()
                cal_alarm[Calendar.MINUTE] = time11[1].toInt()
                cal_alarm[Calendar.SECOND] = 0
                if (cal_alarm.before(cal_now)) {
                    cal_alarm.add(Calendar.DATE, 1)
                }
                val myIntent1 = Intent(this@MainActivity, AlarmIncubator::class.java)
                manager[AlarmManager.RTC_WAKEUP, cal_alarm.timeInMillis] =  PendingIntent.getBroadcast(
                    this@MainActivity,
                    0,
                    myIntent1,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
        }
    }


    private fun storeDataInArrays() {
        val cursor = myDB.readAllDataIncubator()
        if (cursor.count == 0) {
            cursor.close()
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getString(8) == "0") {
                    time1.add(cursor.getString(10))
                    time2.add(cursor.getString(11))
                    time3.add(cursor.getString(12))
                }
            }
            cursor.close()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
    }

    //Закрытие клавиатуры после нажатия на кнопку
    fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    //Сворачивание клавиатуры при нажатие на любую часть экрана
//    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//        val v = currentFocus
//        val ret = super.dispatchTouchEvent(event)
//        if (v is EditText) {
//            val w = currentFocus
//            val scrcoords = IntArray(2)
//            w!!.getLocationOnScreen(scrcoords)
//            val x = event.rawX + w.left - scrcoords[0]
//            val y = event.rawY + w.top - scrcoords[1]
//            Log.d(
//                "Activity",
//                "Touch event " + event.rawX + "," + event.rawY + " " + x + "," + y + " rect " + w.left + "," + w.top + "," + w.right + "," + w.bottom + " coords " + scrcoords[0] + "," + scrcoords[1]
//            )
//            if (event.action == MotionEvent.ACTION_UP && (x < w.left || x >= w.right || y < w.top || y) > w.bottom) {
//                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
//            }
//        }
//        return ret
//    }

    private fun beginIncubator() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Удаляем ?")
        builder.setMessage("Вы уверены, что хотите удалить все инкубаторы, включая архивные?")
        builder.setPositiveButton(
            "Да"
        ) { dialogInterface, i ->
            myDB.deleteAllIncubator()
            replaceFragment(WarehouseFragment())
        }
        builder.setNegativeButton(
            "Нет"
        ) { dialogInterface, i -> }
        builder.create().show()
    }

    // показывать фаб кнопку при нажатие назад
    private fun fbaShowBackIncubator() {
        fab.show()
        fab.text = "Добавить"
        fab.setIconResource(R.drawable.baseline_add_24)
        fab.icon
    }
}