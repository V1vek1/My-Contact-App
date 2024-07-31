package com.example.contactapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.contactapp.databinding.ActivityMainBinding
import com.example.contactapp.mvvmarch.MainActivityViewModel
import com.example.contactapp.roomdb.DbBuilder
import com.example.contactapp.roomdb.entity.Contact

/*
* Iss Project me Room ko use karne keiye hamne uski dependencies Or Plugins ko Implements kiya
* phir uske baad aapne kotlin ka Version upgrade kiya iss Project ka "Libs.versions.toml" me jakar
* kyuki mai use kar rha hu Beta Version isliye isme Error aa rhe the Compile karne me issiliye
* jaise hi update kiy sab sahi ho gya h
*
*
* Phir maine "MainActivity" wale folder me ek Package bnaya "roomdb" name ka phir uske "under me" ek or
* "Package" bnaya jiska name h "entity", phir iski "location File Explorer" me open karke "roomdb" folder
* ke under ek or "dao" name ka "folder" bnaya
 */


class MainActivity : AppCompatActivity() {
    private val binding by lazy {

        ActivityMainBinding.inflate(layoutInflater)
    }

    var contactList = ArrayList<Contact>()
    var viewModel: MainActivityViewModel? = null
    lateinit var adapter: ContactAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        /*
         * Yha mai Aapne App se "CALL PHONE" ka PErmision le rha hu
         * agar yeh Permission h to Ye run kardo warna "Else" ke code ko Run karo
         */

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            createUI()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CALL_PHONE
            )
        ) {

            val alertDialog = AlertDialog.Builder(this)
                .setMessage("This app require read video permission to run")
                .setTitle("Permission Required")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.CALL_PHONE
                        ), DIAl_CALL_PERMISSION
                    )
                }
                .setNegativeButton("cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()

        } else {

            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(Manifest.permission.CALL_PHONE),
                DIAl_CALL_PERMISSION
            )
        }

    }

    fun createUI() {

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding.floatingActionButton.setOnClickListener {

            startActivity(Intent(this@MainActivity, AddEditActivity::class.java))
        }


        viewModel!!.data.observeForever {
            contactList.clear()  // * Yha mai "Purane Contact" ko "Clear" kar rha hu jab "New Update" ho rha h to
            it.forEach {
                contactList.add(it)
            }
            adapter.notifyDataSetChanged()

        }


        /*
         * Yha maine "ItemTouchHelper" bnaya h kyuki, Yeh "items" ko "Swipe" karne ke liye ise hota h, iss Swiping me
         * hum Features add kar sakte h ki "item" ko "Swipe" karne per "Call" lgado ya "delete" kardo
         */
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            1,
            ItemTouchHelper.LEFT
                    or ItemTouchHelper.RIGHT
        ) {


            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (ItemTouchHelper.LEFT == direction) {
                    viewModel!!.deleteContact(contactList.get(viewHolder.adapterPosition))
                } else {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data =
                        Uri.parse("tel:${contactList.get(viewHolder.adapterPosition).phoneNumber}")
                    startActivity(intent)
                    adapter.notifyDataSetChanged()
                }

            }

        }).attachToRecyclerView(binding.rv)


        binding.rv.layoutManager = LinearLayoutManager(this)
        adapter = ContactAdapter(contactList, this)  // Yha mai "contactList" me data le rha hu
        binding.rv.adapter = adapter
        Log.d("MYTAG", "onCreate: ${R.layout.activity_main}")



        /*
         * "UI" me jo SearchBox bnaya h uske liye Functionality Add kar rha hu yah Per,
         *
         * mai Yha
         */
        binding.searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("MyTAG", "onTextChanged: $s")

                if (s == null)
                {
                    adapter.contactList = contactList
                }
                else
                {
                    if (s.length == 0 || s.isNullOrBlank() || s.isNullOrEmpty())
                    {
                        adapter.contactList = contactList
                    }
                    else
                    {
                        var tempList = ArrayList<Contact>()
                        contactList.forEach {

                            if(it.name != null)
                            {
                                /*
                                 *Yha mai "Name Or Phone Number dono ko per yeh Condition lga rha h,
                                 * "ignoreCase = true" ka matlab h ki, Hum "SearchBox" me "Capital ya Small",
                                 * kisi bhi Letter se Search kare to, wo contact hame mil jaye, Agar wo
                                 * contact List me h to
                                 */
                                if (it.name!!.contains(s, ignoreCase = true) || it.phoneNumber!!.contains(s))
                                {
                                    tempList.add(it)
                                }
                            }
                        }

                        adapter.contactList = tempList
                        adapter.notifyDataSetChanged() // * iska matlab kya h
                    }
                }

            }

            override fun afterTextChanged(s: Editable?) {
               // TODO("Not yet implemented")  // * NOTE:- Agar TODO nahi hatayenge to Error aayega "TODO"
                                               // * ka matlab h ki kuch na kuch karo yha per, Jab TODO hata
                                               // * denge to kuch nahi bhi karenge to bhi App Chal jayega
            }

        })
    }




    /*
        * yha mai Uperwale "else" Condition ki Permission ko Chexk kar rha hu ki agar Permission Code, "DIAL_CALL_PERMISSION"
        * se match karta h to yeh karo warna h yeh karo
         */

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == DIAl_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createUI()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CALL_PHONE
                )
            ) {

                val alertDialog = AlertDialog.Builder(this)
                    .setMessage("This app require read video permission to run")
                    .setTitle("Permission Required")
                    .setCancelable(false)
                    .setPositiveButton("OK") { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.CALL_PHONE
                            ), DIAl_CALL_PERMISSION
                        )
                    }
                    .setNegativeButton("cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    .create()
                alertDialog.show()

            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity, arrayOf(Manifest.permission.CALL_PHONE),
                    DIAl_CALL_PERMISSION
                )
            }
        }
    }
}