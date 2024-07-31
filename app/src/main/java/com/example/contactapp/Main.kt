package com.example.contactapp

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun add(a:Int, b: Int, ab:(a: Boolean)-> Unit){
    println(a+b)
    ab(true)
}
fun main() {
    add(23,54){
        a->
        if (a){
            println(23*67)

        }else{
            67-23
        }

    }


}