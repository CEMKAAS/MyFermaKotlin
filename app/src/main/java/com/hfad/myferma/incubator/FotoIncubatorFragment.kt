package com.hfad.myferma.incubator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.hfad.myferma.R

class FotoIncubatorFragment : Fragment() {
    private var day: Int = 0
    private var type: String = ""
    private lateinit var name: TextView
    private lateinit var description: TextView
    private lateinit var image: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val layout: View = inflater.inflate(R.layout.fragment_foto_incubator, container, false)
        val appBar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        appBar.menu.findItem(R.id.delete).isVisible = false
        appBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val bundle: Bundle? = arguments
        if (bundle != null) {
            day = bundle.getInt("data")
            type = bundle.getString("type").toString()
        }

        name = layout.findViewById(R.id.name)
        description = layout.findViewById(R.id.textView2)
        image = layout.findViewById(R.id.imageView2)
        setFoto()
        return layout
    }

    private fun setDescription(description1: String, image1: Int) {
        name.text = "День $day"
        description.text = "$description1\nЯйцо должно выглядеть так как указано на картинке"
        image.setImageResource(image1)
    }

    private fun setFoto() {
        if (type == "Курицы") {
            if (day < 7) {
                image.setImageResource(R.drawable.chiken1)
                name.text = "День $day"
                description.text = "Овоскопировать еще рано, на " + 7 + " день яйцо должно выглядеть так, если нет, его нужно убрать из инкубатора"
            } else if (day in 7..10) {
                setDescription("Первое овоскопирование", R.drawable.chiken1)
            } else if (day in 11..15) {
                setDescription("Второе овоскопирование", R.drawable.chiken2)
            } else if (day >= 16) {
                setDescription("Третье овоскопирование", R.drawable.chiken3)
            }
        } else if (type == "Индюки") {
            if (day < 8) {
                image.setImageResource(R.drawable.turkeys1)
                name.text = "День $day"
                description.text = "Овоскопировать еще рано, на " + 8 + " день яйцо должно выглядеть так, если нет, его нужно убрать из инкубатора"
            } else if (day in 8..13) {
                setDescription("Первое овоскопирование", R.drawable.turkeys1)
            } else if (day in 14..24) {
                setDescription("Второе овоскопирование", R.drawable.turkeys2)
            } else if (day >= 25) {
                setDescription("Третье овоскопирование", R.drawable.turkeys3)
            }
        } else if (type == "Гуси") {
            if (day < 9) {
                image.setImageResource(R.drawable.goose1)
                name.text = "День $day"
                description.text = "Овоскопировать еще рано, на " + 9 + " день яйцо должно выглядеть так, если нет, его нужно убрать из инкубатора"
            } else if (day in 9..14) {
                setDescription("Первое овоскопирование", R.drawable.goose1)
            } else if (day in 15..20) {
                setDescription("Второе овоскопирование", R.drawable.goose2)
            } else if (day >= 21) {
                setDescription("Третье овоскопирование", R.drawable.goose3)
            }
        } else if (type == "Утки") {
            if (day < 8) {
                image.setImageResource(R.drawable.duck1)
                name.text = "День $day"
                description.text = "Овоскопировать еще рано, на " + 8 + " день яйцо должно выглядеть так, если нет, его нужно убрать из инкубатора"
            } else if (day in 8..14) {
                setDescription("Первое овоскопирование", R.drawable.duck1)
            } else if (day in 15..25) {
                setDescription("Второе овоскопирование", R.drawable.duck2)
            } else if (day >= 26) {
                setDescription("Третье овоскопирование", R.drawable.duck3)
            }
        } else if (type == "Перепела") {
            if (day < 6) {
                image.setImageResource(R.drawable.quail1)
                name.text = "День $day"
                description.text = "Овоскопировать еще рано, на " + 6 + " день яйцо должно выглядеть так, если нет, его нужно убрать из инкубатора"
            } else if (day in 6..12) {
                setDescription("Первое овоскопирование", R.drawable.quail1)
            } else if (day in 13..15) {
                setDescription("Второе овоскопирование", R.drawable.quail2)
            }
        }
    }
}