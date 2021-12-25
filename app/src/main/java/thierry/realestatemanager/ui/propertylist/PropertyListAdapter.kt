package thierry.realestatemanager.ui.propertylist

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import thierry.realestatemanager.R
import thierry.realestatemanager.model.FullProperty
import thierry.realestatemanager.utils.Utils
import java.time.format.DateTimeFormatter

class PropertyListAdapter(
    private val properties: List<FullProperty>,
    private val onClickListener: View.OnClickListener,
    private val onContextClickListener: View.OnContextClickListener,
) :
    RecyclerView.Adapter<PropertyListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_property_list_content, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = properties[position]
        val formattedPrice: String? = Utils.formatThePrice(item.property.price!!)
        holder.propertyPrice.text = formattedPrice.toString()
        holder.propertyType.text = item.property.type
        holder.propertyTown.text = item.property.address!!.city

        item.mediaList.forEachIndexed { index, media ->
            if (media.propertyId == item.property.id) {
                if (media.position?.equals(0) == true) {
                    Glide.with(holder.itemView)
                        .load(item.mediaList[index].uri)
                        .centerCrop()
                        .into(holder.propertyPhoto)
                } else if (index == 0) {
                    Glide.with(holder.itemView)
                        .load(item.mediaList[index].uri)
                        .centerCrop()
                        .into(holder.propertyPhoto)
                }
            }
        }

        with(holder.itemView) {
            tag = item.property.id
            setOnClickListener(onClickListener)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setOnContextClickListener(onContextClickListener)
            }
        }

        if (item.property.dateOfSale != null) {
            holder.dateOfSaleOfProperty.text = Utils.epochMilliToLocalDate(item.property.dateOfSale)
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }
        holder.isSoldTextView.isVisible = item.property.isSold == true
        holder.dateOfSaleOfProperty.isVisible = item.property.isSold == true

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
        val isSoldTextView: TextView = binding.findViewById(R.id.propertyIsSold)
        val dateOfSaleOfProperty: TextView = binding.findViewById(R.id.date_of_sale_of_property)
    }

}