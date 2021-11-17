package thierry.realestatemanager.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import thierry.realestatemanager.R
import thierry.realestatemanager.model.Property
import thierry.realestatemanager.utils.Utils

class ItemListAdapter(
    private val properties: List<Property>,
    private val onClickListener: View.OnClickListener,
    private val onContextClickListener: View.OnContextClickListener
) :
    RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = properties[position]
        val formattedPrice: String? = Utils.formatThePrice(item.price)
        holder.propertyPrice.text = formattedPrice.toString()
        holder.propertyType.text = "House"
        holder.propertyTown.text = "Paris"

        Glide.with(holder.itemView)
            .load(R.drawable.property_drawable)
            .centerCrop()
            .into(holder.propertyPhoto)

        with(holder.itemView) {
            tag = item.id
            setOnClickListener(onClickListener)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setOnContextClickListener(onContextClickListener)
            }
        }

    }

    override fun getItemCount(): Int {
        return properties.size
    }

    class ViewHolder(binding: View) :
        RecyclerView.ViewHolder(binding.rootView) {
        val propertyPrice: TextView = binding.findViewById(R.id.propertyPrice)
        val propertyType: TextView = binding.findViewById(R.id.propertyType)
        val propertyTown: TextView = binding.findViewById(R.id.propertyTown)
        val propertyPhoto: ImageView = binding.findViewById(R.id.propertyPicture)
    }

}