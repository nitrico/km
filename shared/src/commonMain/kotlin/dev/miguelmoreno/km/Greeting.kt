package dev.miguelmoreno.km

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}