package com.whyk.chatrpg

class Enemy(var Name : String, var Strength : Int, var StrengthDice : Int,var HP : Int) : SceneComponent() {

    override fun getCurrent() : String {
        return Name + "\n힘 : " + Strength + " + " + StrengthDice + "D\n체력 : " + HP
    }
}