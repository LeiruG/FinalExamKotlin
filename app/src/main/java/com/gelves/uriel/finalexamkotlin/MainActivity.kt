package com.gelves.uriel.finalexamkotlin

import android.app.Activity
import android.app.LoaderManager
import android.content.Context
import android.content.Loader
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView

import java.util.ArrayList

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallba    cks<ArrayList<Album>> {
    private var searchBy = 1
    private var indicator: TextView? = null

    private var searchTxt: TextView? = null
    private var recyclerView: RecyclerView? = null
    var search = ""
    private var albumsAdapter: AlbumsAdapter? = null
    private var loaderManager: LoaderManager? = null

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rView) as RecyclerView
        searchTxt = findViewById(R.id.search) as TextView
        indicator = findViewById(R.id.empty) as TextView

        indicator!!.text = "No Albums"

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {


        } else {
            indicator!!.text = "No Connection"
        }

        searchTxt!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Log.e("kobe", "click")
                search = searchTxt!!.text.toString()
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })
    }

    fun performSearch() {
        indicator!!.visibility = View.GONE
        loaderManager = getLoaderManager()
        loaderManager!!.initLoader<ArrayList<Album>>(searchBy, null, this)
        hideSoftKeyboard(this@MainActivity)

    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<ArrayList<Album>> {

        val baseUri = Uri.parse(REQUEST_URL)
        val uriBuilder = baseUri.buildUpon()

        if (id == ALBUM_LOADER_ID1) {
            uriBuilder.appendQueryParameter("method", "artist.gettopalbums")
            uriBuilder.appendQueryParameter("artist", search) // edit later
            uriBuilder.appendQueryParameter("api_key", "490b41d76995ab4e15ca4d9d04e015a9")
            uriBuilder.appendQueryParameter("limit", "50")
            uriBuilder.appendQueryParameter("format", "json")
        } else if (id == ALBUM_LOADER_ID2) {
            uriBuilder.appendQueryParameter("method", "album.search")
            uriBuilder.appendQueryParameter("album", search) // edit later
            uriBuilder.appendQueryParameter("api_key", "490b41d76995ab4e15ca4d9d04e015a9")
            //12696eb0c1e42d1b92a4293a54269236
            uriBuilder.appendQueryParameter("limit", "50")
            uriBuilder.appendQueryParameter("format", "json")
        }
        Log.d("kobe", uriBuilder.toString())

        return AlbumLoader(this, uriBuilder.toString(), id)
    }

    override fun onLoadFinished(loader: Loader<ArrayList<Album>>, data: ArrayList<Album>?) {
        recyclerView!!.visibility = View.VISIBLE
        Log.e("kobe", "daa size " + data!!.size)
        albumsAdapter = AlbumsAdapter(this, data)
        if (data != null && !data.isEmpty()) {
            indicator!!.visibility = View.GONE
            val layoutManager = LinearLayoutManager(this)
            recyclerView!!.layoutManager = layoutManager
            recyclerView!!.adapter = albumsAdapter
        } else {
            recyclerView!!.visibility = View.GONE
            indicator!!.visibility = View.VISIBLE
        }
        getLoaderManager().destroyLoader(searchBy)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.clear) {
            searchTxt!!.text = ""
        }
        if (id == R.id.artistName) {
            indicator!!.text = "Search album by artist"
            searchBy = 1
        }
        if (id == R.id.albumName) {
            indicator!!.text = "Search album by name"
            searchBy = 2
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Album>>) {

    }

    companion object {

        private val REQUEST_URL = "http://ws.audioscrobbler.com/2.0/?"

        private val ALBUM_LOADER_ID1 = 1
        private val ALBUM_LOADER_ID2 = 2
        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(
                    Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken, 0)
        }
    }
}