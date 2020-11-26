using GoogleSheetsToUnity;
using System.Collections.Generic;

[System.Serializable]
public class Job
{
    //이름 의지  지식 힘   민첩 체력  정신력 가진 아이템 장착 아이템 능력  이벤트
    public string name;
    public string description;
    public int will;
    public int knowledge;
    public int strength;
    public int agility;
    public int helth;
    public List<Item> Items = new List<Item>();
    public List<Item> EquipItems = new List<Item>();
    public List<Ability> Abilities = new List<Ability>();
    public List<Event> Events = new List<Event>();

    public string User;

    public string ToShow()
    {
        return $".{name} : {description}";
    }

    public Job() { }

    public Job(GstuSpreadSheet ss, string key)
    {
        name = ss[key, "이름"].value;
        description = ss[key, "설명"].value;
        int.TryParse(ss[key, "의지"].value, out will);
        int.TryParse(ss[key, "지식"].value, out knowledge);
        int.TryParse(ss[key, "힘"].value, out strength);
        int.TryParse(ss[key, "민첩"].value, out agility);
        int.TryParse(ss[key, "체력"].value, out helth);
    }
}
