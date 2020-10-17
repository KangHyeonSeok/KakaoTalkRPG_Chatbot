package com.whyk.chatrpg
import java.util.*

class RPGManager() {
    var SceneIndex : Int = -1
    var SceneList : MutableList<Scene> = mutableListOf()
    var PlayerList : MutableMap<String,Job> = mutableMapOf()
    init {
        SceneIndex = -1
        PlayerList = mutableMapOf()
        var characterSelectScene : Scene = CharacterSelectScene(this,"게임을 시작 합니다. 여러분은 고블린 둥지를 탐험 하는 모험을 하게 될 것입니다.")
        SceneList.add(characterSelectScene)

        var enterance = Scene(this,"여러분은 고블린 둥지의 입구에 서 있습니다. 이제 와서 돌아가는 것은 선택지에 없습니다. 자신의 상태를 확인 하고 안으로 들어갈 준비를 마쳐 주세요.", arrayOf("다음"))
        SceneList.add(enterance)
    }

    //var isStart : Boolean = false

    fun Command(sender : String, command : String):String {
        if(!command.startsWith("."))
            return ""
        if(command==".시작" && SceneIndex < 0)
            return SelectNextScene()

        if(SceneIndex>= 0) {
            return SceneList[SceneIndex].command(sender,command)
        }

        return ""
    }

    fun SelectJob(job : Job,sender: String) {
        PlayerList[sender] = job
    }

    fun SelectNextScene() : String {
        SceneIndex++
        return SceneList[SceneIndex].getCurrent()
    }

    fun getPlayerList() : String {
        var result : String = ""
        for ((name, job) in PlayerList) {
            result += "${job.Name}[$name] "
        }
        return result
    }

    fun DealETC(sender: String, command: String) : String {

        var result = dice(sender,command)
        if(result.isNotEmpty())
            return result
        return ""
    }

    private fun dice(sender: String, command: String) : String {
        val regex = """.주사위(\s*)(\d*)""".toRegex()
        val matchResult = regex.find(command)
        if( matchResult != null ) {
            val groupValues : List<String> = matchResult.groupValues
            if( groupValues.count() > 1 ) {
                val result = groupValues[2].toIntOrNull()
                return if(result != null && result > 1 && result <= 10000000) {
                    val random = Random()
                    val num = random.nextInt(result +1)
                    sender+"님 : " + num.toString()
                }else
                    "주사위 값은 2~10000000 사이만 가능 합니다."
            }
        }

        return ""
    }

    public fun Save() {

    }

    public fun Load() {

    }
}