package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.udacity.asteroidradar.domain.Asteroid

class DetailViewModel(asteroid: Asteroid, app: Application): AndroidViewModel(app) {

}

/*class DetailViewModel(marsProperty: MarsProperty, app: Application) : AndroidViewModel(app) {

    private val _selelctedProperty = MutableLiveData<MarsProperty>()
    val selectedProperty: LiveData<MarsProperty>
        get() = _selelctedProperty

    init {
        _selelctedProperty.value = marsProperty
    }

    val displayPropertyPrice = selectedProperty.map {
        app.applicationContext.getString(
            when (it.isRental) {
                true -> R.string.display_price_monthly_rental
                false -> R.string.display_price
            }, it.price
        )
    }

    val displayPropertyType = selectedProperty.map {
        app.applicationContext.getString(
            R.string.display_type,
            app.applicationContext.getString(
                when (it.isRental) {
                    true -> R.string.type_rent
                    false -> R.string.type_sale
                }
            )
        )
    }
}*/
