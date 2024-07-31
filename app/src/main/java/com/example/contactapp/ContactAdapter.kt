package com.example.contactapp

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.RotateAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.databinding.ContactItemBinding
import com.example.contactapp.roomdb.entity.Contact

/*
* Yha hamne ek "Class bnaya h jiska name h "adapter
* Adapter loop ki tarah kaam karta h, mtlb ki ek design bna diya or abb bas, jha jha aisa chiz ki
* jarurat h bas wha use kar liye Adapter ka
 */

class ContactAdapter(var contactList: List<Contact>, var context: Context) :
    RecyclerView.Adapter<ContactAdapter.MyViewHolder>() {

    // * "Binding.root" me root ka matlab h ki, uaa class ka sabse phala "layout" se start karna
    inner class MyViewHolder(var binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root)


    /*
    * Niche wale tino Functions Automatically Create hote h Jab hum Adapter bnate h
    *
    * "onCreateViewHolder" ek Function h joki hamare Adapter ke "Inner Class ko Call karta h
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }


    // * Yeh wala "getItemCount" Function yha Kind of Loop ka kaam karta h, Means Yeh btata h ki
    // * List me Jitne bhi Items h, isko utne times chala do bas
    override fun getItemCount(): Int {
        return contactList.size
    }


    // * Yeh Function Yeh btayega ki hamare "recyclerView" koun sa Views/elements kaha per h wo yehs show karega
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val contact = contactList[position]
        if (contact.profile != null) {

            var imageByte = contact.profile

            if (imageByte != null) {
                var image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
                holder.binding.profile.setImageBitmap(image)
                holder.binding.profile.visibility = View.VISIBLE
                holder.binding.sign.visibility = View.GONE


            }

                 /*
                 * Yha mai "Contact" me se jo "name" aa rhe h unme jha bhi "space" dikh rha h wha se Split
                 * kar rha hu, Phir usko ek Stirng me Store kra rha hu, Phir mai "splitname" varaible per
                 * "foreach" Loop chalaya, jabtak ki jo contact se Strings aaye h unki INdex 0 na hojaye
                 */

            else {
                val splitName = contact.name?.split(" ")
                var sign: String = ""
                splitName?.forEach {
                    if (it.isNotEmpty()) {
                        sign += it[0]

                    }
                }
                holder.binding.sign.text = sign
                holder.binding.profile.visibility = View.GONE
                holder.binding.sign.visibility = View.VISIBLE
            }

        } else {
            val splitName = contact.name?.split(" ")
            var sign: String = ""
            splitName?.forEach {
                if (it.isNotEmpty()) {
                    sign += it[0]

                }
            }
            context


            holder.binding.sign.text = sign
            holder.binding.sign.text = sign
            holder.binding.profile.visibility = View.GONE
            holder.binding.sign.visibility = View.VISIBLE
        }

        holder.binding.name.text = contact.name
        holder.binding.phone.text = contact.phoneNumber
        holder.binding.email.text = contact.email

        holder.itemView.setOnClickListener {


            context.startActivity(
                Intent(context, AddEditActivity::class.java).putExtra("FLAG", 1)
                    .putExtra("DATA", contact)
            )

        }
        holder.binding.profile.setOnClickListener {

        }

        makeAnimation(holder.itemView)

    }

    fun makeAnimation(view : View){
        var anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 2000
   //     anim.repeatCount = 5
        view.startAnimation(anim)
    }

}