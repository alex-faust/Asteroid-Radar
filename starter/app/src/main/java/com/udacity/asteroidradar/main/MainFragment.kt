package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.AsteroidFilter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        setHasOptionsMenu(true)

        binding.asteroidRecycler.adapter = AsteroidRvAdapter(AsteroidRvAdapter.OnClickListener {
            mainViewModel.displayAsteroidDetails(it)
        })

        mainViewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                mainViewModel.displayAsteroidDetailsComplete()
            }
        }

        /*mainViewModel.asteroidList.observe(viewLifecycleOwner) {
            asteroids?.apply {

            }
        }*/




        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mainViewModel.updateFilter(
            when (item.itemId) {
                R.id.show_today -> AsteroidFilter.SHOW_TODAY
                R.id.show_saved -> AsteroidFilter.SHOW_SAVED
                else -> AsteroidFilter.SHOW_WEEK
            }
        )
        return true
    }
}
