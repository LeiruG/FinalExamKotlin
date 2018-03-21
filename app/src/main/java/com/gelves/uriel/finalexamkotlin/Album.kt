package com.gelves.uriel.finalexamkotlin

/**
 * Created by Lucem on 21/03/2018.
 */

class Album {
    var name: String? = null
    var artist: String? = null
    var url: String? = null
    var image: String? = null

    constructor() {}

    constructor(name: String, artist: String, url: String, image: String) {
        this.name = name
        this.artist = artist
        this.url = url
        this.image = image
    }
}