package com.hfad.myferma.incubator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import com.hfad.myferma.R
import com.hfad.myferma.db.MyFermaDatabaseHelper

class editDayIncubatorFragment : Fragment(), View.OnClickListener {
    private lateinit var tempInput: TextInputLayout
    private lateinit var dampInput: TextInputLayout
    private lateinit var overInput: TextInputLayout
    private lateinit var airingInput: TextInputLayout
    private lateinit var updateButton: Button
    private lateinit var textUnit: TextView

    private var massId = mutableListOf<String>()
    private var massTemp = mutableListOf<String>()
    private var massDamp = mutableListOf<String>()
    private var massOver = mutableListOf<String>()
    private var massAiring = mutableListOf<String>()
    private var dataFragment: Int = 0
    private var allListFragment : Boolean = false

    private lateinit var myDB: MyFermaDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout: View = inflater.inflate(R.layout.fragment_edit_day_incubator, container, false)
        myDB = MyFermaDatabaseHelper(requireContext())

        val appBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        appBar.menu.findItem(R.id.delete).isVisible = false
        appBar.menu.findItem(R.id.filler).isVisible = false
        appBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

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
            allListFragment = bundle.getBoolean("allList")

        }

        massId = massId2.toMutableList()
        massTemp = massTemp2.toMutableList()
        massDamp = massDamp2.toMutableList()
        massOver = massOver2.toMutableList()
        massAiring = massAiring2.toMutableList()

        textUnit = layout.findViewById(R.id.text_unit)
        tempInput = layout.findViewById(R.id.discAdd_input)
        dampInput = layout.findViewById(R.id.dayAdd_input)
        overInput = layout.findViewById(R.id.mountAdd_input)
        airingInput = layout.findViewById(R.id.yearAdd_input)

        textUnit.text = "День ${dataFragment} "
        tempInput.editText!!.setText(massTemp[dataFragment])
        dampInput.editText!!.setText(massDamp[dataFragment])
        overInput.editText!!.setText(massOver[dataFragment])
        airingInput.editText!!.setText(massAiring[dataFragment])

        updateButton = layout.findViewById(R.id.update_button)
        updateButton.setOnClickListener(this)
        return layout
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.update_button -> upDate(v)
        }
    }

    private fun upDate(view: View?) {
        massTemp[dataFragment] =
            tempInput.editText!!.text.toString().trim { it <= ' ' }
                .replace(",".toRegex(), ".").replace("[^\\d.]".toRegex(), "")

        massDamp[dataFragment] =
            dampInput.editText!!.text.toString().trim { it <= ' ' }
                .replace("\\D+".toRegex(), "")

        massOver[dataFragment] =
            overInput.editText!!.text.toString().trim { it <= ' ' }
        massAiring[dataFragment] =
            airingInput.editText!!.text.toString().trim { it <= ' ' }

        //убираем ошибку
        tempInput.isErrorEnabled = false
        dampInput.isErrorEnabled = false
        overInput.isErrorEnabled = false
        airingInput.isErrorEnabled = false

        //вывод ошибки
        if (((tempInput.editText!!
                .text.toString() == "") || (dampInput.editText!!
                .text.toString() == "") || (overInput.editText!!
                .text.toString() == "") || (airingInput.editText!!.text
                .toString() == ""))
        ) {
            if ((tempInput.editText!!.text.toString() == "")) {
                tempInput.error = "Укажите температуру!"
                tempInput.error
            }
            if ((dampInput.editText!!.text.toString() == "")) {
                dampInput.error = "Укажите влажность"
                dampInput.error
            }
            if ((overInput.editText!!.text.toString() == "")) {
                overInput.error = "Укажите кол-во переворотов"
                overInput.error
            }
            if ((airingInput.editText!!.text.toString() == "")) {
                airingInput.error = "Укажите кол-вл проветриваний"
                airingInput.error
            }
        } else {
            myDB.updateIncubatorTemp(massTemp, massId[0])
            myDB.updateIncubatorDamp(massDamp, massId[0])
            myDB.updateIncubatorOver(massOver, massId[0])
            myDB.updateIncubatorAiring(massAiring, massId[0])

            replaceFragment()



        }
    }

    private fun replaceFragment()
    {
        val bundle: Bundle = Bundle()
        var fragment: Fragment = AllListFragment()

        if (allListFragment) {
            bundle.putInt("data",dataFragment)
            bundle.putStringArray("id", massId.toTypedArray())
            bundle.putStringArray("temp", massTemp.toTypedArray())
            bundle.putStringArray("damp", massDamp.toTypedArray())
            bundle.putStringArray("over", massOver.toTypedArray())
            bundle.putStringArray("airing", massAiring.toTypedArray())
        } else {
            bundle.putString("name", massId[1])
            bundle.putString("type", massId[2])
            bundle.putString("data", massId[3])
            bundle.putString("id", massId[0])
            fragment = NowIncubatorFragment()
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner, fragment, "visible_fragment")
            .addToBackStack(null)
            .commit()
        fragment.arguments = bundle
    }

}