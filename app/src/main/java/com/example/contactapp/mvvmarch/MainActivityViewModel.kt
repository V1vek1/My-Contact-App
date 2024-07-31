package com.example.contactapp.mvvmarch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.contactapp.roomdb.Database
import com.example.contactapp.roomdb.DbBuilder
import com.example.contactapp.roomdb.entity.Contact

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    var repo: Repo


    lateinit var data: LiveData<List<Contact>>


    /*
     * Yeh "init" tab use karte h jab hame pta hki hame iss file ke Run hote hi isko Run karna h, to hum
     * wo sab code "init" Function ke under me karte h
     */
    init {

        repo = Repo(application)

        data = repo.getData()!!

    }

    fun deleteContact(contact: Contact){
        repo.deleteData(contact)
    }

}