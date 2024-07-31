package com.example.contactapp.roomdb.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField
import java.io.Serializable
import java.util.Date

/*
* Yha "entity" Folder me ek "Contact" name ka Class bnaya
 */

// * Yha maine "Contact Class" ke aage se {} hatakar () lagaya h

// * isko hum Model Class bolte h

@Entity   // * Yeh "entity" lgane se Room ko aab pta lag gya ki yeh meri "entity" Class h
data class Contact(

    /* * Jo chize Yha Optional h yani usser de ya na de to bhi wo chale to isiliye uske aage "?" lga
       * denge taki wo Input nhi dene per koi Error nahi aayega
       *
       * Aab yha maine PhoneNumber ka Datatype bhi "String" de rkha h, wo isiliye kyuki hame isko
       * SQL DataBase me Store krana h, Or waha iss tarah se 2 se bade numbers ko Store karne
       * keliye hame "string" ka use karna padta h
    */

    // @PrimaryKey(autoGenerate = true)
   @PrimaryKey
   var id : Int?=null, // * Isko maine bnaya Primary Key, taki databse ko pta chale ki koun Primary key h
    var profile: ByteArray?=null,
    var name : String?=null,
    var phoneNumber : String?=null,
    var email : String?=null,
//    var date: Date
): Serializable

