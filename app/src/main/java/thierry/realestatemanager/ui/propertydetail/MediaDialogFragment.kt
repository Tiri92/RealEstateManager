package thierry.realestatemanager.ui.propertydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        val startButton = rootView.findViewById<FloatingActionButton>(R.id.videoview_start)
        val pauseButton = rootView.findViewById<FloatingActionButton>(R.id.videoview_pause)
        val mArgs = arguments
        val mediaUri: String? = mArgs!!.getString("key")

        if (mediaUri.toString().contains("Photo")) {
            videoview.isVisible = false
            imageview.isVisible = true
            startButton.hide()
            pauseButton.hide()
            Glide.with(imageview)
                .load(mediaUri)
                .centerCrop()
                .into(imageview)
        } else {
            videoview.isVisible = true
            imageview.isVisible = false
            pauseButton.show()
            videoview.setVideoURI(mediaUri?.toUri())
            pauseButton.setOnClickListener(View.OnClickListener {
                pauseButton.hide()
                startButton.show()
                videoview.pause()
            })
            startButton.setOnClickListener(View.OnClickListener {
                startButton.hide()
                pauseButton.show()
                videoview.start()
            })
            videoview.setOnPreparedListener {
                videoview.start()
            }
            videoview.setOnCompletionListener {
                if (it.isPlaying) {
                    startButton.hide()
                    pauseButton.show()
                } else {
                    startButton.show()
                    pauseButton.hide()
                }
            }
        }

        return rootView
    }

}