package kayleenp.github.io.getapet.publishpet

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle

import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kayleenp.github.io.getapet.R
import kotlinx.android.synthetic.main.activity_publish_pet.*


class PublishPetActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

  var petAge = arrayOf("Baby", "Young", "Adult", "Elder")
  var petAgeSpinner:Spinner? = null

  var petGender = arrayOf("Male", "Female")
  var petGenderSpinner:Spinner? = null

  var petType = arrayOf("Dog", "Cat", "Other")
  var petTypeSpinner:Spinner? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_publish_pet)

    petAgeSpinner = this.ageSpinner
    petGenderSpinner = this.genderSpinner
    petTypeSpinner = this.typeSpinner

    // Create an ArrayAdapter using a simple petAgeSpinner layout and languages array
    val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, petAge)
    // Set layout to use when the list of choices appear
    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    // Set Adapter to Spinner
    petAgeSpinner!!.setAdapter(aa)

    val aa2 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, petGender)
    petGenderSpinner!!.setAdapter(aa2)

    val aa3 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, petType)
    petTypeSpinner!!.setAdapter(aa3)

    //BUTTON CLICK
    img_pick.setOnClickListener {
      //check runtime permission
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
          PackageManager.PERMISSION_DENIED){
          //permission denied
          val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
          //show popup to request runtime permission
          requestPermissions(permissions, PERMISSION_CODE);
        }
        else{
          //permission already granted
          pickImageFromGallery();
        }
      }
      else{
        //system OS is < Marshmallow
        pickImageFromGallery();
      }
    }
  }

  private fun pickImageFromGallery() {
    //Intent to pick image
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    startActivityForResult(intent, IMAGE_PICK_CODE)
  }

  companion object {
    //image pick code
    private val IMAGE_PICK_CODE = 1000;
    //Permission code
    private val PERMISSION_CODE = 1001;
  }

  //handle requested permission result
  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    when(requestCode){
      PERMISSION_CODE -> {
        if (grantResults.size >0 && grantResults[0] ==
          PackageManager.PERMISSION_GRANTED){
          //permission from popup granted
          pickImageFromGallery()
        }
        else{
          //permission from popup denied
          Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  //handle result of picked image
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
      imageButton.setImageURI(data?.data)
    }

  }

  override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

  }

  override fun onNothingSelected(arg0: AdapterView<*>) {

  }
}