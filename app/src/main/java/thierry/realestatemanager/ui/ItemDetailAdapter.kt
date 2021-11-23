package thierry.realestatemanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import thierry.realestatemanager.R
import thierry.realestatemanager.model.Photo

class ItemDetailAdapter(private val listOfPropertyPhoto: List<Photo>): RecyclerView.Adapter<ItemDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.propertyPictures)
            .load(listOfPropertyPhoto[position].uri)
            .centerCrop()
            .into(holder.propertyPictures)

    }

    override fun getItemCount(): Int {
        return listOfPropertyPhoto.size
    }

    class ViewHolder(binding: View):
        RecyclerView.ViewHolder(binding.rootView){
        val propertyPictures: ImageView = binding.findViewById(R.id.propertyPictures)
    }

}