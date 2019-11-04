package kayleenp.github.io.getapet

import PetGridItemDecoration
import PetViewState
import android.accounts.Account
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import generateMockPets
import kayleenp.github.io.getapet.accountprofile.AccountProfileActivity
import kayleenp.github.io.getapet.listener.NavigationIconClickListener
import kayleenp.github.io.getapet.petdetail.PetDetailActivity
import kayleenp.github.io.getapet.petdetail.PetDetailActivity.Companion.EXTRA_PET_ID
import kayleenp.github.io.getapet.petdetail.PetDetailActivity.Companion.EXTRA_PET_IS_DARK
import kayleenp.github.io.getapet.petdetail.PetDetailActivity.Companion.EXTRA_PET_PICTURE_URL


import kayleenp.github.io.getapet.petgrid.StaggeredProductAdapter
import kayleenp.github.io.getapet.publishpet.PublishPetActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.backdrop_menu.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupActionBar()
    setupContentBackground()
    setupPetGrid()
    setupPublishPetButton()
    setupProfileAccountButton()
  }

  private fun setupActionBar() {
    setSupportActionBar(toolbar)
    setupBackdrop()
  }

  private fun setupBackdrop() {
    toolbar.setNavigationOnClickListener(
      NavigationIconClickListener(
        toolbar = toolbar,
        sheet = content,
        sheetOverlay = contentSheetOverlay,
        backdropMenu = backdropMenu,
        interpolator = AccelerateDecelerateInterpolator(),
        openIcon = getDrawable(R.drawable.ic_drawer),
        closeIcon = getDrawable(R.drawable.shr_close_menu)
      )
    )
  }

  private fun setupContentBackground() {
    content.background = getDrawable(R.drawable.bg_backdrop)
  }

  private fun setupPetGrid() {
    petGrid.setHasFixedSize(false)
    val gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)

    petGrid.layoutManager = gridLayoutManager
    val adapter = StaggeredProductAdapter(generateMockPets(), onGridItemSelected(), onFavItemClick())
    petGrid.adapter = adapter
    val largePadding = resources.getDimensionPixelSize(R.dimen.shr_staggered_product_grid_spacing_large)
    val smallPadding = resources.getDimensionPixelSize(R.dimen.shr_staggered_product_grid_spacing_small)
    petGrid.addItemDecoration(PetGridItemDecoration(smallPadding, smallPadding))
  }

  private fun onGridItemSelected(): ((PetViewState) -> Unit) = { petState ->
    val intent = Intent(this, PetDetailActivity::class.java)
    intent.putExtra(EXTRA_PET_ID, petState.petId)
    intent.putExtra(EXTRA_PET_PICTURE_URL, petState.url)
    intent.putExtra(EXTRA_PET_IS_DARK, petState.darkImage)
    startActivity(intent)
  }

  private fun onFavItemClick(): ((PetViewState) -> Unit) = { petState ->
  }

  private fun setupPublishPetButton() {
    addPetButton.setOnClickListener { val intent = Intent(this,PublishPetActivity::class.java)
    startActivity(intent)}
  }

  private fun setupProfileAccountButton() {
    myAccountButton.setOnClickListener { val intent = Intent(this,AccountProfileActivity::class.java)
      startActivity(intent)}
  }

}

