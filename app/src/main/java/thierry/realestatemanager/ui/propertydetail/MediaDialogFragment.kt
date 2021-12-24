package thierry.realestatemanager.ui.propertydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import thierry.realestatemanager.R

class MediaDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_media_dialog, container, false)

        val videoview = rootView.findViewById<VideoView>(R.id.videoview)
        val imageview = rootView.findViewById<ImageView>(R.id.imageview)
        val mArgs = arguments
        val mediaUri: String? = mArgs!!.getString("key")

        if (mediaUri.toString().contains("Photo")) {
            videoview.isVisible = false
            imageview.isVisible = true
            imageview.setImageURI(mediaUri?.toUri())
        } else {
            videoview.isVisible = true
            imageview.isVisible = false
            videoview.setVideoURI(mediaUri?.toUri())
            val mediaController = MediaController(videoview.context)
            videoview.setOnClickListener(View.OnClickListener {
                videoview.start()
            })

            videoview.setOnPreparedListener {
                it.setOnVideoSizeChangedListener { mediaPlayer, i, i2 ->
                    videoview.setMediaController(mediaController)
                    mediaController.setAnchorView(requireView().rootView)
                }
                videoview.start()
                videoview.pause()
            }
        }

        return rootView
    }

}