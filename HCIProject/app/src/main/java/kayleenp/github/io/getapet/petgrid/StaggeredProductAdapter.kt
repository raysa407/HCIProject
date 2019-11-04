package kayleenp.github.io.getapet.petgrid
import PetViewState
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kayleenp.github.io.getapet.R
import kayleenp.github.io.getapet.common.ColorUtils
import kayleenp.github.io.getapet.petgrid.picasso.Tint


/**
 * Adapter used to show an asymmetric grid of products, with 2 items in the first column, and 1
 * item in the second column, and so on.
 *
 * Grabbed from material components code labs.
 */
class StaggeredProductAdapter(
    private val pets: List<PetViewState>?,
    private val onItemSelected: ((PetViewState) -> Unit)? = null,
    private val onFavItemClick: ((PetViewState) -> Unit)? = null
) : RecyclerView.Adapter<StaggeredProductAdapter.StaggeredProductCardViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaggeredProductCardViewHolder {


        var layoutId = R.layout.staggered_pet_card_first


        val layoutView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        return StaggeredProductCardViewHolder(layoutView)


    }

    override fun onBindViewHolder(holder: StaggeredProductCardViewHolder, position: Int) {
        if (pets != null && position < pets.size) {
            val pet = pets[position]
            holder.bind(pet, onItemSelected, onFavItemClick)
        }

    }

    override fun getItemCount(): Int {
        return pets?.size ?: 0
    }

    class StaggeredProductCardViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private var card: View = itemView.findViewById(R.id.card)

        private var productImage: ImageView = itemView.findViewById(R.id.product_image)
        private var productTitle: TextView = itemView.findViewById(R.id.product_title)


        fun bind(
            pet: PetViewState,
            onItemSelected: ((PetViewState) -> Unit)?,
            onFavItemClick: ((PetViewState) -> Unit)?
        ) {
            bindTexts(pet)

            bindPicture(pet) { petState, bitmap -> bindFavIcon(petState, bitmap) }
            bindClickListener(onItemSelected, pet)
        }

        private fun bindTexts(pet: PetViewState) {
            productTitle.text = "${pet.species} · ${pet.breed}"

        }


        private fun bindPicture(pet: PetViewState, onLoaded: (PetViewState, Bitmap) -> Unit) {
            Picasso.get()
                .load(pet.url)
                .transform(Tint(1f, ContextCompat.getColor(itemView.context, R.color.colorPrimary)))
                .fit()
                .centerCrop()
                .into(productImage, object : Callback {
                    override fun onSuccess() {
                        onLoaded(pet, (productImage.drawable as BitmapDrawable).bitmap)
                    }

                    override fun onError(e: java.lang.Exception?) {
                    }
                })
        }

        private fun bindClickListener(
            onItemSelected: ((PetViewState) -> Unit)?,
            pet: PetViewState
        ) {
            card.setOnClickListener { onItemSelected?.invoke(pet) }
            productImage.setOnClickListener { onItemSelected?.invoke(pet) }
        }

        private fun bindFavIcon(pet: PetViewState, bitmap: Bitmap) {
            val iconSize = itemView.context.resources.getDimensionPixelSize(R.dimen.icon_button_size)

            Palette.from(bitmap)
                .maximumColorCount(3)
                .clearFilters()
                .setRegion(0, 0, bitmap.width - 1, iconSize)
                .generate { palette -> applyTopPalette(pet, bitmap, palette) }
        }

        private fun applyTopPalette(pet: PetViewState, bitmap: Bitmap, palette: Palette?) {
            val lightness = ColorUtils.isDark(palette)
            val isDark = if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                ColorUtils.isDark(bitmap, bitmap.width / 2, 0)
            } else {
                lightness == ColorUtils.IS_DARK
            }


            pet.darkImage = isDark
        }
    }
}
