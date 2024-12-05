package com.unibite.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.unibite.app.R
import com.unibite.app.data.MenuRepository
import com.unibite.app.databinding.FragmentHomeBinding
import com.unibite.app.domain.usecase.GetPopularItemsUseCase
import com.unibite.app.model.MenuItemModel
import com.unibite.app.ui.adapter.MenuAdapter
import com.unibite.app.viewmodel.home.HomeViewModel
import com.unibite.app.viewmodel.home.HomeViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by lazy {
        val repository = MenuRepository() // Repositorio existente
        val useCase = GetPopularItemsUseCase(repository)
        val factory = HomeViewModelFactory(useCase)
        ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupObservers()

        binding.viewAllMenu.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "BottomSheet")
        }
        homeViewModel.fetchPopularItems() // Obtiene los elementos populares
        return binding.root
    }

    private fun setupObservers() {
        homeViewModel.popularItems.observe(viewLifecycleOwner) { items ->
            setPopularItemsAdapter(items)
        }

        homeViewModel.error.observe(viewLifecycleOwner) { error ->
            // Muestra el error si ocurre
        }
    }

    private fun setPopularItemsAdapter(items: List<MenuItemModel>) {
        val adapter = MenuAdapter(items, requireContext())
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecyclerView.adapter = adapter
    }

    //Configurando Banner
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setItemClickListener(object :ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Imagen Seleccionada $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

}