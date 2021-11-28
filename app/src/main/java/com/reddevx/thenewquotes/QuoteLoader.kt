package com.reddevx.thenewquotes

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.*
import com.reddevx.thenewquotes.models.Quote
import com.reddevx.thenewquotes.ui.MainActivity

class QuoteLoader{

    private val firestore = FirebaseFirestore.getInstance()


    // Data
       var _quoteList:ArrayList<Quote> = ArrayList()

//    internal val quoteList:ArrayList<Quote>
//    get() {return _quoteList}





     fun loadQuotes(collectionPath:String) {
            val mCollection = firestore.collection(collectionPath)
            mCollection.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.w("Collection Error","Listen failed",error)

                        return
                    }

                    for (doc in value!!.documents){
//                        val imageUrl = doc.getString(MainActivity.Constants.FIRE_STORE_IMAGE_KEY)!!
//                        val quoteText = doc.getString(MainActivity.Constants.FIRE_STORE_QUOTE_KEY)!!
//                        val category = doc.getString(MainActivity.Constants.FIRE_STORE_CATEGORY_KEY)!!
                        val quote = doc.toObject(Quote::class.java)
                        if (quote != null) {
                            _quoteList.add(quote)
                        }
                    }

                }
            })
        }




}