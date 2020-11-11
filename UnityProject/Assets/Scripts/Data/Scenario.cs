using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class Scenario
{
    public string name;
    public string description;
    public List<Scene> Scenes = new List<Scene>();

    public string ToShow()
    {
        return $"{name} : {description}";
    }
}
