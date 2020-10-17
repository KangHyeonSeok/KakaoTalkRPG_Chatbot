package com.whyk.chatrpg

open class Scene (var RPG : RPGManager, var SceneNarration : String, var CommandList : Array<String>){

    open fun getCurrent() : String {
        return SceneNarration
    }

    open fun command(sender : String, command : String) : String {
        return sender + "는 " + command + " 를 하였다."
    }
}