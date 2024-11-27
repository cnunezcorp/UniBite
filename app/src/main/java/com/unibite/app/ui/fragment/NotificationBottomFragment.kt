package com.unibite.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unibite.app.R
import com.unibite.app.databinding.FragmentNotificationBottomBinding
import com.unibite.app.ui.adapter.NotificationAdapter
import java.util.ArrayList

class NotificationBottomFragment :  BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBottomBinding.inflate(layoutInflater, container, false)
        val notifications =
            listOf(
                "Su orden ha sido cancelada existosamente",
                "Están preparando tu pedido",
                "¡Felicidades! Tu pedido ha sido realizado"
            )
        val notificationImages =
            listOf(
                R.drawable.sademoji,
                R.drawable.mixing,
                R.drawable.congrats
            )
        val adapter = NotificationAdapter (
            ArrayList(notifications),
            ArrayList(notificationImages)
        )

        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

    }
}