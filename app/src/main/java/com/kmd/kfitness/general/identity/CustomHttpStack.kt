package com.kmd.kfitness.general.identity

import com.android.volley.toolbox.HurlStack
import java.net.HttpURLConnection
import java.net.URL

class CustomHttpStack() : HurlStack() {
    override fun createConnection(url: URL): HttpURLConnection {
        val connection = super.createConnection(url)

        // Add headers to every request
        connection.setRequestProperty("Cookie", UserIdentity.instance.accessToken)

        return connection
    }
}