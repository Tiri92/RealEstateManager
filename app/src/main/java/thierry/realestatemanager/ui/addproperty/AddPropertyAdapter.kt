package thierry.realestatemanager.ui.addproperty

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import thierry.realestatemanager.databinding.ItemAddAndUpdatePhotoBinding
import thierry.realestatemanager.model.Photo
import thierry.realestatemanager.model.Video

class AddPropertyAdapter(private val listOfPropertyPhoto: MutableList<Photo>, private val listOfPropertyVideo: MutableList<Video>,callback: PhotoDescriptionChanged) :
    RecyclerView.Adapter<AddPropertyAdapter.ViewHolder>() {

    private var callback: PhotoDescriptionChanged? = callback

    interface PhotoDescriptionChanged {
        fun onDescriptionPhotoChanged(description: String, uri: String)
    }

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

        holder.photoDescriptionEditText.addTextChangedListener{
            callback?.onDescriptionPhotoChanged(holder.photoDescriptionEditText.text.toString(), listOfPropertyPhoto[position].uri)
        }

    }

    override fun getItemCount(): Int {
        return listOfPropertyPhoto.size
    }

    class ViewHolder(binding: ItemAddAndUpdatePhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val propertyPicturesAU: ImageView = binding.propertyPicturesAU
        val photoDescriptionEditText: TextInputEditText = binding.photoDescriptionEditText
    }

}