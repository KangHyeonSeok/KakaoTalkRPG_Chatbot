package com.whyk.chatrpg

class CharacterSelectScene(RPG : RPGManager, SceneNarration : String) : Scene(RPG, SceneNarration, arrayOf("기사","검사","마법사","연금술사","도적","음유시인","승려")) {

    override fun getCurrent(): String {
        return super.getCurrent() + "\n다음의 클래스를 선택 하실 수 있습니다.\n.기사 .검사 .마법사 .연금술사 .도적 .음유시인 .승려\n선택이 완료되면 .다음을 입력해 주세요"
    }

    override fun command(sender: String, command: String): String {

        when(command) {
            ".기사" -> {
                RPG.SelectJob(Job("기사",4,2,3,2),sender)
                return "$sender 님이 기사를 선택 하였습니다."
            }
            ".검사" -> {
                RPG.SelectJob(Job("검사",5,2,2,2),sender)
                return "$sender 님이 검사를 선택 하였습니다."
            }
            ".마법사" -> {
                RPG.SelectJob(Job("마법사",1,2,5,2),sender)
                return "$sender 님이 마법사를 선택 하였습니다."
            }
            ".연금술사" -> {
                RPG.SelectJob(Job("연금술사",2,2,3,2),sender)
                return "$sender 님이 연금술사를 선택 하였습니다."
            }
            ".도적" -> {
                RPG.SelectJob(Job("도적",3,2,4,2),sender)
                return "$sender 님이 도적을 선택 하였습니다."
            }
            ".음유시인" -> {
                RPG.SelectJob(Job("음유시인",2,2,5,2),sender)
                return "$sender 님이 음유시인을 선택 하였습니다."
            }
            ".승려" -> {
                RPG.SelectJob(Job("승려",3,2,4,2),sender)
                return "$sender 님이 승려를 선택 하였습니다."
            }
            ".다음" -> {
                return "클래스 선택이 끝났습니다.\n\n\n" + RPG.SelectNextScene()
            }
        }


        return ""
    }

}