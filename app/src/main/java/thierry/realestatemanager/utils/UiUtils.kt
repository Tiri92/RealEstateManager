package thierry.realestatemanager.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import thierry.realestatemanager.R

class UiUtils {

    companion object {

        private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
            // below line is use to generate a drawable.
            val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

            // below line is use to set bounds to our vector drawable.
            vectorDrawable!!.setBounds(0,
                0,
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight)

            // below line is use to create a bitmap for our
            // drawable which we have added.
            val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888)

            // below line is use to add bitmap in our canvas.
            val canvas = Canvas(bitmap)

            // below line is use to draw our
            // vector drawable in canvas.
            vectorDrawable.draw(canvas)

            // after generating our bitmap we are returning our bitmap.
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

        fun addMarker(map: GoogleMap, context: Context, lat: Double, lng: Double, title: String) {
            map.addMarker(MarkerOptions()
                .position(LatLng(lat, lng))
                .icon(bitmapDescriptorFromVector(context,
                    R.drawable.ic_baseline_other_houses_24))
                .title(title))
        }

    }

}