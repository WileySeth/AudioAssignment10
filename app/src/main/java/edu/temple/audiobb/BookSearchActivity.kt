package edu.temple.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class BookSearchActivity : AppCompatActivity() {

    val volleyQueue : RequestQueue by lazy {
        Volley.newRequestQueue(this)
    }

    val searchEditTextView: TextView by lazy {
        findViewById(R.id.searchEditTextView)
    }

    val cancelButton: Button by lazy {
        findViewById(R.id.cancelButton)
    }

    val searchButton: Button by lazy {
        findViewById(R.id.searchButton)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)

        searchButton.setOnClickListener{
            fetchBooks(searchEditTextView.text.toString())
        }


    }

    fun fetchBooks(searchText: String): BookList {
        val url =  "https://kamorris.com/lab/ci3515/search.php?term=" + searchText

        lateinit var bookListReturned: BookList

        volleyQueue.add (
            JsonArrayRequest(Request.Method.GET
                , url
                , null
                , {
                    response ->
                    try {
                        for(i in 0 until response.length()){
                            val book: JSONObject = response.getJSONObject(i)

                            val id: Int = book.getInt("id")
                            val title: String = book.getString("title")
                            val author: String = book.getString("author")
                            val imageUrl: String = book.getString("cover_url")

                            bookListReturned.add(Book(id, title, author, imageUrl))

                        }



                    } catch (e : JSONException) {
                        e.printStackTrace()
                    }
                }
                , {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                })
        )

        return bookListReturned
    }
}