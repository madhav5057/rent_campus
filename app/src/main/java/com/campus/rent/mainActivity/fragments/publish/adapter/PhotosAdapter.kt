import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.campus.rent.R
import com.campus.rent.utils.Constants.TAG

class PhotosAdapter(val context: Context, private var list: MutableList<Uri>) :
    RecyclerView.Adapter<PhotosAdapter.ViewModel>() {

    inner class ViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.imageView2)
        val close = itemView.findViewById<ImageView>(R.id.close)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val view = LayoutInflater.from(context).inflate(R.layout.item_photos, parent, false)
        return ViewModel(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        val uri = list[position]
        holder.image.setImageURI(uri)

        // Set close button functionality
        holder.close.setOnClickListener {
            list.remove(uri)
            Log.d(TAG, "$list")
            notifyDataSetChanged()
        }
    }
}
