package thierry.realestatemanager.ui.updateproperty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import thierry.realestatemanager.R
import thierry.realestatemanager.model.Media
import thierry.realestatemanager.ui.propertydetail.MediaDialogFragment

class UpdatePropertyAdapter(

    val listOfPropertyMedia: List<Media>,
    callback: PhotoDescriptionChanged,
    val supportFragmentManager: FragmentManager,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var callback: PhotoDescriptionChanged? = callback

    interface PhotoDescriptionChanged {
        fun onDescriptionPhotoChanged(description: String, uri: String)

        fun onDeleteMedia(media: Media)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_update_photo, parent, false)
            PhotoViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_update_video, parent, false)
            VideoViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return listOfPropertyMedia.size
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mediaModel: Media) {
            val propertyPhoto: ImageView = itemView.findViewById(R.id.property_photo)
            itemView.setOnClickListener {
                val mediaDialogFragment = MediaDialogFragment()
                val args = Bundle()
                args.putString("key", mediaModel.uri)
                mediaDialogFragment.arguments = args
                mediaDialogFragment.show(supportFragmentManager, "MediaDialogFragment")
            }
            Glide.with(propertyPhoto)
                .load(mediaModel.uri)
                .centerCrop()
                .into(propertyPhoto)
            val photoDescription: TextInputEditText =
                itemView.findViewById(R.id.photo_description_edit_text)
            photoDescription.addTextChangedListener {
                callback?.onDescriptionPhotoChanged(
                    photoDescription.text.toString(),
                    mediaModel.uri
                )
            }
            photoDescription.setText(mediaModel.description)

            val deletePhotoButton: ImageView = itemView.findViewById(R.id.delete_property_photo)
            deletePhotoButton.setOnClickListener {
                callback?.onDeleteMedia(mediaModel)
            }
        }

    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mediaModel: Media) {
            val propertyVideo: ImageView = itemView.findViewById(R.id.property_video)
            itemView.setOnClickListener {
                val mediaDialogFragment = MediaDialogFragment()
                val args = Bundle()
                args.putString("key", mediaModel.uri)
                mediaDialogFragment.arguments = args
                mediaDialogFragment.show(supportFragmentManager, "MediaDialogFragment")
            }
            Glide.with(propertyVideo)
                .load(mediaModel.uri)
                .centerCrop()
                .into(propertyVideo)
            val videoDescription: TextInputEditText =
                itemView.findViewById(R.id.video_description_edit_text)
            videoDescription.addTextChangedListener {
                callback?.onDescriptionPhotoChanged(
                    videoDescription.text.toString(),
                    mediaModel.uri
                )
            }
            videoDescription.setText(mediaModel.description)

            val deleteVideoButton: ImageView = itemView.findViewById(R.id.delete_property_video)
            deleteVideoButton.setOnClickListener {
                callback?.onDeleteMedia(mediaModel)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (listOfPropertyMedia[position].uri.contains("Photo")) {
            0
        } else {
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            (holder as PhotoViewHolder).bind(listOfPropertyMedia[position])
        } else {
            (holder as VideoViewHolder).bind(listOfPropertyMedia[position])
        }
    }

}