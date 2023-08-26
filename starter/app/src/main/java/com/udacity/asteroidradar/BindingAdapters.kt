package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidRvAdapter
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.main.AsteroidApiStatus

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("listData")
fun bindingRecyclerView(recyclerView: RecyclerView, data:List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidRvAdapter
    adapter.submitList(data)
}

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if(it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        //Picasso.get().load(it).into(imageView)
        Picasso.with(imgView.context).load(imgUri).placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image).into(imgView)
        //glide can load a placeholder image while waiting and a different one if it fails
        /**Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions().placeholder(R.drawable.loading_animation).error(R.drawable.ic_broken_image))
            .into(imgView)*/
    }
}

@BindingAdapter("asteroidApiStatus")
fun bindStatus(statusImageView: ImageView, status: AsteroidApiStatus?) {
    when (status) {
        AsteroidApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        AsteroidApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        AsteroidApiStatus.DONE -> {
            //statusImageView.visibility = View.GONE
        }
        else -> {}
    }
}