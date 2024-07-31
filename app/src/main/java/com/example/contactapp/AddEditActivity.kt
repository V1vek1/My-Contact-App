package com.example.contactapp

import android.app.Activity
import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.contactapp.databinding.ActivityAddEditBinding
import com.example.contactapp.mvvmarch.AddEditActivityViewModel
import com.example.contactapp.roomdb.DbBuilder
import com.example.contactapp.roomdb.entity.Contact
import com.github.dhaval2404.imagepicker.ImagePicker


class AddEditActivity : AppCompatActivity() {

    // * Yha mai Binding bnaya
    val binding by lazy {
        ActivityAddEditBinding.inflate(layoutInflater)
    }
    var contact = Contact()
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!


                binding.imageView.setImageURI(fileUri)

                // * Jab hame File System se image ko store karna hota h to image ko "readBytes" ki help se
                // * lete h
                val imageBytes = contentResolver.openInputStream(fileUri)?.readBytes()

                contact.profile = imageBytes

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    lateinit var viewModel: AddEditActivityViewModel

    var flag = -1

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // * Yha miane Binding lagaya
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        viewModel = ViewModelProvider(this).get(AddEditActivityViewModel::class.java)


        if (intent.hasExtra("FLAG")) {  // * "hasExtra" ka kaam hota h ki Yeh Check kare ki Kya ye Flag phele
                                              // * wha h ya nahi, Agr h to yeh karo warna , Else wala kaam karo

            flag = intent.getIntExtra("FLAG", -1)


            /*
            * Yeh Condition tabhi chalegi jab, hamare App, SDK Version 33 se jada per Run ho rha hoga warna
            * "else" wala Condition chalega
           */

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                contact = intent.getSerializableExtra("DATA", Contact::class.java)!!

            else {
                contact = intent.getSerializableExtra("DATA") as Contact // * Yha "as" ka use, Typecasting ke liye hota h

            }


            binding.saveContact.text = "Update Contact"
            var imageByte = contact.profile

            if (imageByte != null) {
                var image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
                binding.imageView.setImageBitmap(image)


            }
            binding.email.editText?.setText(contact.email)
            binding.phone.editText?.setText(contact.phoneNumber)
            binding.name.editText?.setText(contact.name)

        }

        binding.imageView.setOnLongClickListener {

            // * Uper wale Line of Code ko iss tarike se bhi likh sakte h
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.image_dialog)


            /*
            * *********************NOTE :- Yha mai Niche ke codes ko Explain kiya hu ****************************
            *
            * Yha maine "dialog Box" me ek ImageView ko liya or usko ek "image" name ke Variable me
            * Store kiya,
            *
            * Phir uske Niche maine uss Image ko Drawable se liya or phir ek Variable "imageObject"
            *  me Store kiya,
            *
            * phir uss "image" ko Initailize/Show kardiya
            *
            * Phir mai "DialBox" ka Background ko Transparent kar rha hu
            *
            * Phir maine "Dialog Box" ko Show kra diya
             */
            val image = dialog.findViewById<ImageView>(R.id.image)
            val imageObject = binding.imageView.drawable
            image.setImageDrawable(imageObject)


            /*
            * Yha maine "Dialog Box" ko "WindowManager or Layoutparameter" ki help se
            * Width or Height ko change kiya,
            *
            * Phir iss "Dialog Box" ko "blurBehind" ki help se kitna percent "Blur" Effect
            * rakhna h wo diya,
            *
            * Phir "Flags" ka use karke "blur" dikhaya Background ke piche me,
            *
            * phir btaya ki "Dialog Box" ki Window per (activity) Area Blur ya
            * jobhi karna h wo kitne Area me karna h
             */
            val lp = WindowManager.LayoutParams()
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.blurBehindRadius = 100
            lp.flags = WindowManager.LayoutParams.FLAG_BLUR_BEHIND
            dialog.window?.attributes = lp
            dialog.show()

            true


        }

        // * Yha maine btaya ki "SaveContact" Button per Click karne per kya - kya karna h
        binding.saveContact.setOnClickListener {

            contact.name = binding.name.editText?.text.toString()
            contact.email = binding.email.editText?.text.toString()
            contact.phoneNumber = binding.phone.editText?.text.toString()
            if (flag == 1) {

                viewModel.updateData(contact) {
                    if (it != null) {
                        if (it > 0) {
                            Toast.makeText(this@AddEditActivity, it.toString(), Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                    }
                }

            } else {
                viewModel.storeData(contact) {

                    if (it != null) {
                        if (it > 0) {
                            Toast.makeText(this@AddEditActivity, it.toString(), Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                    }
                }
            }


        }

        /*
         * Maine Yha "ImageView" per Image lgane keliye "ImagePicker" ka use kiya h
         */
        binding.imageView.setOnClickListener {
            ImagePicker.with(this).crop().createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
        }


    }


}