package thierry.realestatemanager.ui.addproperty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import thierry.realestatemanager.R
import thierry.realestatemanager.model.Media

class AddPropertyAdapter(

    private val listOfPropertyMedia: MutableList<Media>,
    callback: PhotoDescriptionChanged
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var callback: PhotoDescriptionChanged? = callback

    interface PhotoDescriptionChanged {
        fun onDescriptionPhotoChanged(description: String, uri: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_and_update_photo, parent, false)
            return PhotoViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_and_update_video, parent, false)
            return VideoViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return listOfPropertyMedia.size
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mediaModel: Media) {
            val propertyPhoto: ImageView = itemView.findViewById(R.id.property_photo)
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
        }

    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mediaModel: Media) {
            val propertyVideo: VideoView = itemView.findViewById(R.id.property_video)
            propertyVideo.setVideoURI(mediaModel.uri.toUri())
            propertyVideo.setOnClickListener(View.OnClickListener {
                propertyVideo.start()
            })

            propertyVideo.setOnCompletionListener {
                propertyVideo.start()
                propertyVideo.pause()
            }

            propertyVideo.setOnFocusChangeListener { view, b ->
                propertyVideo.start()
                propertyVideo.pause()
            }

            propertyVideo.setOnPreparedListener {
                propertyVideo.start()
                propertyVideo.pause()
            }

            val videoDescription: TextInputEditText =
                itemView.findViewById(R.id.video_description_edit_text)
            videoDescription.addTextChangedListener {
                callback?.onDescriptionPhotoChanged(
                    videoDescription.text.toString(),
                    mediaModel.uri
                )
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (listOfPropertyMedia[position].uri.contains("/data/user/")) {
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