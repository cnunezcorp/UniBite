package com.unibite.app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.unibite.app.R
import com.unibite.app.databinding.FragmentProfileBinding
import com.unibite.app.model.UserModel


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentProfileBinding.inflate(inflater, container, false)

        setUserData()
        binding.saveInfoButton.setOnClickListener{
            val name = binding.profileName.text.toString()
            val email = binding.profileEmail.text.toString()
            val phone = binding.profilePhone.text.toString()

            updateUserData(name, email, phone)
        }
        return binding.root

    }

    private fun updateUserData(name: String, email: String, phone: String) {
        val userId = auth.currentUser?.uid
        if (userId != null){
            val userReference = database.getReference("user").child(userId)

            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "phone" to phone
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Perfil Actualizado Correctamente", Toast.LENGTH_SHORT).show()

            } .addOnFailureListener{
                Toast.makeText(requireContext(), "No se pudo actualizar el Perfil", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null){
            val userReference = database.getReference("user").child(userId)
            userReference.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile != null ){
                            binding.profileName.setText(userProfile.name)
                            binding.profileEmail.setText(userProfile.phone)
                            binding.profilePhone.setText(userProfile.phone)
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