package com.example.tickets.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tickets.R
import com.example.tickets.adapter.OfferListAdapter
import com.example.tickets.databinding.FragmentOfferListBinding
import com.example.tickets.model.entity.Offer
import com.example.tickets.model.network.ApiClient
import com.example.tickets.model.service.FakeService
import retrofit2.Call
import retrofit2.Response


class OfferListFragment : Fragment() {
    private var offers: List<Offer> = emptyList()


    companion object {
        fun newInstance() = OfferListFragment()
    }

    private var _binding: FragmentOfferListBinding? = null
    private val binding
        get() = _binding!!

    private val adapter: OfferListAdapter by lazy {
        OfferListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfferListBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        ApiClient.apiService.getFlights().enqueue(object : retrofit2.Callback<List<Offer>> {
            override fun onResponse(call: Call<List<Offer>>, response: Response<List<Offer>>) {
                if (response.isSuccessful) {
                    response.body()?.let { fetchedOffers ->
                        offers = fetchedOffers
                        adapter.submitList(offers)
                    } ?: run {
                        Toast.makeText(requireContext(), "No offers available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to retrieve offers. Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Offer>>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupUI() {
        with(binding) {
            offerList.adapter = adapter

            sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.sort_by_price -> {
                        val newList = adapter.currentList.sortedBy { it.price }
                        adapter.submitList(newList)
                    }

                    R.id.sort_by_duration -> {
                        val newList = adapter.currentList.sortedBy { it.flight.duration }
                        adapter.submitList(newList)
                    }
                }
            }
        }
    }
}