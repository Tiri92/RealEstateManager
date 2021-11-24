package thierry.realestatemanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import thierry.realestatemanager.databinding.ItemAddAndUpdatePhotoBinding

class AddPropertyAdapter(private val listOfPropertyPhoto: MutableList<String>) :
    RecyclerView.Adapter<AddPropertyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemAddAndUpdatePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.propertyPicturesAU)
            .load(listOfPropertyPhoto[position].uri)
            .centerCrop()
            .into(holder.propertyPicturesAU)

    }

    override fun getItemCount(): Int {
        return listOfPropertyPhoto.size
    }

    class ViewHolder(binding: ItemAddAndUpdatePhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val propertyPicturesAU: ImageView = binding.propertyPicturesAU
    }

}