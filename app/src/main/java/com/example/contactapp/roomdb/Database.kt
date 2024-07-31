package com.example.contactapp.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.contactapp.DB_VERSION
import com.example.contactapp.roomdb.dao.ContactDao
import com.example.contactapp.roomdb.entity.Contact

/*
* NOTE:-
*
* Yha maine Annotation diya ki yeh h mera "Database abstract Class" Jisme maine "Contact Class" ko pass kiya h
*
* "Version" change hota rahta h jaise hi hum kuch data Update ya kuch bhi karenge to wo Version change hoga
*
* "ExportSchema = false" ka matlab h ki hamare Database ko koi Export na karpaye, iske liye hum "Documentation"
* Ready karte h ki hamne kis Version me kya use kiya h
*
* * Jab bhi mai "entity" ka schema Change karunga, to hame hamesa Yha "version" ko chnage karna hoga
 */

@Database
    (entities = [Contact::class], version = DB_VERSION)
     abstract class Database : RoomDatabase()
{
    abstract fun contactDao(): ContactDao  // * Iski help se hum "data" ko Access kar rhe h,
}