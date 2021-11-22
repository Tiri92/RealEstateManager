package thierry.realestatemanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import thierry.realestatemanager.R
import thierry.realestatemanager.databinding.ItemAddAndUpdatePhotoBinding
import thierry.realestatemanager.model.Photo

class AddAndUpdateAdapter(private val listOfPropertyPhoto: List<Photo>) :
    RecyclerView.Adapter<AddAndUpdateAdapter.ViewHolder>() {

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