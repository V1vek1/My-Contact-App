package com.example.contactapp.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.contactapp.roomdb.entity.Contact

/*
* NOTE :-
*
* Yha per maine Ek D.A.O (Data Access Object) Class bnaya tha Per hame pta h ki D.A.O Class nahi "interface" hota h
* isiliye maine class hatakar "interface" kardiya
*
* "Interface" ki ek Khaas Baat yeh ki isme Function bnane ke baad Body dene ki jarurat nahi hoti h, body means
* ki curly Braces "{}" dene ki jarurat nahi h, wo khud samjh jata h
*
* D.A.O CRUD (Create, Read, Update, Delete) operation per kam karta h
*
* to Yha mai iss "Interface" me CRUD operation keliye Functons bnaunga
 */



@Dao  // * Yeh "D.A.O" lgane se Room ko aab pta lag gya ki yeh meri "D.A.O" Yani Data Access Object Interface h
interface ContactDao {

    // * Yha mai iss "Interface" me CRUD operation keliye Functons bnaunga, RoomDb iss sabhi Function ko aapne me
    // * khud se Implement karlega

    // * Create Function
    @Insert(onConflict = OnConflictStrategy.ABORT)  // * Yeh "@Insert" se lekar uske baad tak ke code ko isliye use karte
                                                    // * h, Manlo agar user koi data insert karta h or wo data kisi ki "id"
                                                    // * h to, iss Condition me "OnConflictStrategy.REPLACE" use karke
                                                    // * Duplicate Value ko "REPLACE" karna h to replace kardega

    fun createContact(contact: Contact): Long  // * iss Function me "contact" Variable me aapna "Entity" wala Contact pass kiya



    // * Update Function

    @Update  // * maine Yha Notation dediya, iski "help" se "Code samjh" jayega ki ha hame isme aaye hue Code ko Update
             // * karna h
    fun updateContact(contact: Contact) : Int


    // Upsert Function
//    @Upsert    // * Iss Function ki Help se hum, kisi Value ko Insert Or Update dono kar sakte h, Per hum abhi iska
//               // * use nahi kar rhe h
//    fun upsert(contact: Contact)


    // * Read Function
    @Query("SELECT * FROM CONTACT")  // * Maine yha Anotaion ke sath sath ye btaya ki kaise "roomDb" se data lena h

    fun readContact(): LiveData<List<Contact>> // * Yha Read me mai "all Contacts" ko kewal dekh dakte h isiliye mai yha isko
                                               // * show kar rha hu "user ko" "List" ke Form me


    @Query("SELECT * FROM CONTACT WHERE id= :id1")
    fun readContact(id1 : Int): Contact


    // * Delete Function
    @Delete   // * maine Yha Notation dediya, iski "help" se "Code samjh" jayega ki ha hame isme aaye hue Code ko Delete
              // * karna h
    fun deleteContact(contact: Contact)

}