package thierry.realestatemanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import thierry.realestatemanager.R

class AddAndUpdateAdapter: RecyclerView.Adapter<AddAndUpdateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_add_and_update_photo, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.itemView)
            .load(R.drawable.property_drawable)
            .centerCrop()
            .into(holder.propertyPicturesAU)

    }

    override fun getItemCount(): Int {
        return 5
    }

    class ViewHolder(binding: View):
        RecyclerView.ViewHolder(binding.rootView){
        val propertyPicturesAU: ImageView = binding.findViewById(R.id.propertyPicturesAU)
    }

}