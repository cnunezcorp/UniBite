package com.unibite.app.ui.activity

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.unibite.app.R
import com.unibite.app.data.OrderRepository
import com.unibite.app.databinding.ActivityPayOutBinding
import com.unibite.app.domain.usecase.OrderUseCase
import com.unibite.app.model.OrderDetails
import com.unibite.app.ui.fragment.CongratsBottomSheetFragment
import com.unibite.app.viewmodel.PayOutViewModel
import com.unibite.app.viewmodel.PayOutViewModelFactory

class PayOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayOutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var foodItemDescription: ArrayList<String>
    private lateinit var foodItemIngredient: ArrayList<String>
    private lateinit var foodItemQuantities: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        setUserData()

        //Get user details from Firebase
        val intent = intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") ?: arrayListOf()
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") ?: arrayListOf()
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") ?: arrayListOf()
        foodItemDescription = intent.getStringArrayListExtra("FoodItemDescription") ?: arrayListOf()
        foodItemIngredient = intent.getStringArrayListExtra("FoodItemIngredient") ?: arrayListOf()
        foodItemQuantities = intent.getIntegerArrayListExtra("FoodItemQuantities") ?: arrayListOf()

        totalAmount = calculateTotalAmount().toString() + "$"
        binding.payTotalAmount.isEnabled = false
        binding.payTotalAmount.setText(totalAmount)

        binding.backButton.setOnClickListener{
            finish()
        }

        binding.placeMyOrder.setOnClickListener{

            //get data from TextView
            name = binding.payName.toString().trim()
            phone = binding.payPhone.text.toString().trim()
            if (name.isBlank() && phone.isBlank()){
                Toast.makeText(this, "Debe Completar Todos los Detalles", Toast.LENGTH_SHORT).show()
            }
            else{
                placeOrder()
            }


        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid?:""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(userId, name, foodItemName, foodItemPrice, foodItemImage, foodItemQuantities, totalAmount,phone, time, itemPushKey, false, false)
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog = CongratsBottomSheetFragment()
            bottomSheetDialog.show(supportFragmentManager, "Test")
            removeItemFromCart()
            addOrderToHistory(orderDetails)
        } .addOnFailureListener {
            Toast.makeText(this, "Orden Fallida", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {

            }
    }

    private fun removeItemFromCart() {
        val cartItemsReference = databaseReference.child("user").child(userId).child("CartItems")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0  until  foodItemPrice.size){
            var price = foodItemPrice[i]
            val lastChar = price.last()
            val priceInteVale = if (lastChar == '$'){
                price.dropLast(1).toInt()
            }
            else{
                price.toInt()
            }
            var quantity = foodItemQuantities[i]
            totalAmount += priceInteVale * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null){
            val userId = user.uid
            val userReference = databaseReference.child("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        val names = snapshot.child("name").getValue(String::class.java)?:""
                        val phones = snapshot.child("phone").getValue(String::class.java)?:""
                        binding.apply {
                            payName.setText(names)
                            payPhone.setText(phones)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}
