package com.example.contactapp.mvvmarch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.contactapp.roomdb.Database
import com.example.contactapp.roomdb.DbBuilder
import com.example.contactapp.roomdb.entity.Contact

// * Yha maine ek Class bnaya h Or yeh ek "ViewModel" h, Yha jo maine "content" ki jagah "application" use
// * kiya h kyuki yeh bhi "context" ki tarah hi kaam karta h

class AddEditActivityViewModel(application: Application): AndroidViewModel(application)   {

        /*
         * yha mai "Repository" ko "Dricet Initialize" kardiya h, kyuki mai chata hu ki jaha bhi "database" ka use h wha
         * yeh "repo" se Data ko fetch karle
         */
    var repo: Repo
    init {

        repo = Repo(application)

    }


    // * Iss Function me maine pura Function ka parameter hi bhej dia h, Or Yha "Unit" jo lga huaa h uska matlab h ki
    // * "Lamda Function aayega
    fun storeData(contact: Contact,function :(i : Long?)-> Unit){
       var i= repo.insertData(contact)
        function(i)
    }

    fun updateData(contact: Contact, function: (i: Int?) -> Unit) {
        function(repo.updateData(contact))
    }

}