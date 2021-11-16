package thierry.realestatemanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import thierry.realestatemanager.R

class ItemDetailAdapter: RecyclerView.Adapter<ItemDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.itemView)
            .load(R.drawable.property_drawable)
            .centerCrop()
            .into(holder.image)

    }

    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolder(binding: View):
        RecyclerView.ViewHolder(binding.rootView){
        val image: ImageView = binding.findViewById(R.id.propertyPictures)
    }

}