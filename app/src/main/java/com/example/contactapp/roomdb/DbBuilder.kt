package com.example.contactapp.roomdb

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.contactapp.dbName

/*
* Yeh maine Ek "Object" bnaya h Or ye hum "Database" ke "Objects" ko "access" karne liye ye "Object" bna rhe h
* jisko "Single Build" bhi bolte h mtlb isme jobhi Changes hone wo sabhi jagah reflect hoga
 */


object DbBuilder {

    private var database: Database? = null  // * Agar database Null h to New Database bnado

    // * Function bnaya h, Jo Room Database ko Build kar rha h
    fun getdb(context: Context): Database? {
        if (database == null) {
            database = Room.databaseBuilder(
                context,
                Database::class.java,
                dbName
            )
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE) // Yeh line ka matlab h ki hum isse database ko Memory me
                                                                   // create kar rhe h

                .fallbackToDestructiveMigration()    // Iss code ka matlab h ki agar mai yani ki user koi bhi changes karta
                                                     // purane data me to yeh code uss ko purane wale ki jagah new se
                                                     // update kardega

                .allowMainThreadQueries()    // Iss Code ka matlab h ki, hame pta h ki hamara database jada bada
                                             // nahi h to hame hum "mainThread" ko hi Allow kardete h ki, tu
                                             // hamare data ko hi har 16milisecond me refresh karke check karle
                                             // ki wo sahi chal raha h ki nahi
            
                .build()
        }
        return database       // Or phir Databse return kardo
    }
}