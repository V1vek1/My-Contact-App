package com.example.contactapp

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/*
* Yha iss Class me mai "Coroutine" kya hota h kaise work karta h, wosa sikhne wale h
 */

fun main() {

    // Agar mai aab yha Call karu Greet Function ko to wo nahi hoga kyuki wo suspend ho chuka h, agar aab hame
    // greet Function ko call karna h to hame "runBlocking" ka use karna hoga


    runBlocking {
        // Per mai iss Block me Call kar sakta hu kyuki Yha mai "runBlocking" ka use kar rha hu joki
        // "Coroutine" ka ek Function h

        // greet()

        repeat(100) {
            async {
                if (it % 2 == 0)
                {
                    Thread.sleep(100)
                }

                else
                {
                    Thread.sleep(101)
                }
                  println(it)
            }


//            async {
//                greet()
//            }
//
//            async {
//                greet1()
//            }

        }

    }

    suspend fun greet() {
        delay(5000)
        println("Hey..")

        //  Hum iss Coroutine me bhi, Dusre wale "Coroutine" ko Call kar sakte h, kyuki ek "Coroutine" function me
        // hum dusre Coroutine function ko call kar sakte h

       // greet1()
    }

    suspend fun greet1() {
        println("Greet 2....")
    }
}

