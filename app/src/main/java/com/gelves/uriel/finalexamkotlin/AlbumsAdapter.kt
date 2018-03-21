package com.gelves.uriel.finalexamkotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide

import java.util.ArrayList

/**
 * Created by Lucem on 21/03/2018.
 */

class AlbumsAdapter(private val mContext: Context, private val albums: ArrayList<Album>) : RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.album_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AlbumsAdapter.ViewHolder, position: Int) {
        if (!albums[position].image!!.isEmpty()) {
            Glide.with(mContext).load(albums[position].image).into(holder.image)
        }
        holder.title.text = albums[position].name
        holder.artist.text = albums[position].artist

    }

    override fun getItemCount(): Int {
        Log.e("kobe", albums.size.toString() + "  ")
        return albums.size

    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView
        private val title: TextView
        private val artist: TextView

        init {
            image = itemView.findViewById<View>(R.id.image) as ImageView
            title = itemView.findViewById<View>(R.id.title) as TextView
            artist = itemView.findViewById<View>(R.id.artist) as TextView

            itemView.setOnClickListener {
                val albumUri = Uri.parse(albums[adapterPosition].url)
                val websiteIntent = Intent(Intent.ACTION_VIEW, albumUri)
                mContext.startActivity(websiteIntent)
            }

        }
    }
}