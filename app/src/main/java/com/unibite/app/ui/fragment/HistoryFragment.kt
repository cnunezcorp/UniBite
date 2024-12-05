package com.unibite.app.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.unibite.app.ui.activity.RecentOrderItemsActivity
import com.unibite.app.databinding.FragmentHistoryBinding
import com.unibite.app.model.OrderDetails
import com.unibite.app.ui.adapter.BuyAgainAdapter

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        //Mostrando el historico de compras
        retrieveBuyHistory()


        binding.recentBuyItem.setOnClickListener{
            seeItemsRecentBuy()
        }
        return binding.root
    }

    private fun seeItemsRecentBuy() {
        listOfOrderItem.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(), RecentOrderItemsActivity::class.java)
            intent.putExtra("RecentBuyOrderItem", listOfOrderItem)
            startActivity(intent)
        }
    }

    private fun retrieveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""

        val buyItemReference: DatabaseReference = database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        // Log detallado del historial recuperado
                        Log.d("HistoryFragment", "Buy History Item: $it")
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                    setDataInRecentBuyItem()
                    setPreviousBuyItemsRecyclerView()
                } else {
                    Log.d("HistoryFragment", "No items found in buy history.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HistoryFragment", "Error retrieving buy history: ${error.message}")
            }
        })
    }

    private fun setDataInRecentBuyItem() {
        binding.recentBuyItem.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()
        recentOrderItem?.let {
            with(binding) {
                Log.d("HistoryFragment", "Recent Order: $it")
                buyAgainFoodName.text = it.foodNames?.firstOrNull() ?: ""
                buyAgainFoodPrice.text = "${it.foodPrices?.firstOrNull() ?: ""}$"
                val image = it.foodImages?.firstOrNull() ?: ""
                val uri = Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(buyAgainFoodImage)
            }
        } ?: run {
            Log.d("HistoryFragment", "No recent order found.")
        }
    }

    private fun setPreviousBuyItemsRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImage = mutableListOf<String>()

        for (i in 1 until listOfOrderItem.size) { // Omitir el primero (ya usado en `recentBuyItem`)
            val order = listOfOrderItem[i]
            Log.d("HistoryFragment", "Processing Order: $order")
            order.foodNames?.firstOrNull()?.let { buyAgainFoodName.add(it) }
            order.foodPrices?.firstOrNull()?.let { buyAgainFoodPrice.add(it) }
            order.foodImages?.firstOrNull()?.let { buyAgainFoodImage.add(it) }
        }

        // Log final de las listas generadas
        Log.d("HistoryFragment", "Final Buy Again Names: $buyAgainFoodName")
        Log.d("HistoryFragment", "Final Buy Again Prices: $buyAgainFoodPrice")
        Log.d("HistoryFragment", "Final Buy Again Images: $buyAgainFoodImage")

        val rv = binding.buyAgainRecyclerView
        rv.layoutManager = LinearLayoutManager(requireContext())
        buyAgainAdapter = BuyAgainAdapter(buyAgainFoodName, buyAgainFoodPrice, buyAgainFoodImage, requireContext())
        rv.adapter = buyAgainAdapter
    }




}