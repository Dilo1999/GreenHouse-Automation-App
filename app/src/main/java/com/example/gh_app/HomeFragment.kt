package com.example.gh_app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find card views
        val marketCard = view.findViewById<CardView>(R.id.marketcard)
        val deseasCard = view.findViewById<CardView>(R.id.deseasCard)
        val grothCard = view.findViewById<CardView>(R.id.grothCard)
        val soilCard = view.findViewById<CardView>(R.id.soilCard)

        // Set click listeners
        marketCard.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    Market::class.java
                )
            )
        }
        deseasCard.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    Deseas::class.java
                )
            )
        }
        grothCard.setOnClickListener { startActivity(Intent(activity, Growth::class.java)) }
        soilCard.setOnClickListener { startActivity(Intent(activity, Soil::class.java)) }
        return view
    }
}
