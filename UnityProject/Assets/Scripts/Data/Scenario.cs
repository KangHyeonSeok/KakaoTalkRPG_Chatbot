using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class Scenario : iData
{
    public string name;
    public string description;
    public string placeName;
    
    public string ToShow()
    {
        return $".{name} : {placeName}\n{description}";
    }
}
