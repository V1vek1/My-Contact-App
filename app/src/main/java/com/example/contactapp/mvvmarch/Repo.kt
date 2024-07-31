package com.example.contactapp.mvvmarch

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.contactapp.roomdb.Database
import com.example.contactapp.roomdb.DbBuilder
import com.example.contactapp.roomdb.entity.Contact


/*
* Repository bas ye karta h ki yeh dekhta h ki hame kaha kaha se Data aa rha h uske path ko as a
* Function Contain karna hota h
 */


class Repo(
    var context: Context
) {
    var database: Database? = null

    init {
        database = DbBuilder.getdb(context)
    }


    // * Yeh Function Contact ko Read kar rha h, "ContactDao" se

    fun getData(): LiveData<List<Contact>>? {
      return  database?.contactDao()?.readContact()
    }

    fun insertData(contact: Contact): Long? {
         return database?.contactDao()?.createContact(contact)
    }
    fun deleteData(contact: Contact) {
        database?.contactDao()?.deleteContact(contact)
    }

    fun updateData(contact: Contact): Int? {
      return  database?.contactDao()?.updateContact(contact)
    }


}