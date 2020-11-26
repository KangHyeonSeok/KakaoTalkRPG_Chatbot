using GoogleSheetsToUnity;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
#if UNITY_EDITOR
using UnityEditor;
#endif

public class Database : ScriptableObject
{
    public string SheetID = "1k9cSK7iOqHOD1RzzYR0sW8iM0wz14Vk6Z4Jko5LimOQ";

    public List<Scenario> Scenarios = new List<Scenario>();
    public List<Scene> Scenes = new List<Scene>();
    public List<Place> Places = new List<Place>();
    public List<Job> Jobs = new List<Job>();
    public List<Item> Items = new List<Item>();
    public List<SKill> SKills = new List<SKill>();
    public List<Event> Events = new List<Event>();
    public List<Enemy> Enemies = new List<Enemy>();

    private static Database database;

    public static Database Instance
    {
        get
        {
            if (database == null)
                database = Resources.Load<Database>("Database");
            if (database == null)
            {
                Debug.LogError("Can't find database. Please check created database and is in Resources folder.");
                return null;
            }
            return database;
        }
    }

#if UNITY_EDITOR
    [MenuItem("Database/DataDownload")]
    static void Download()
    {
        Instance.DataDownload();
    }
#endif

    public void DataDownload()
    {
        SpreadsheetManager.ReadPublicSpreadsheet(new GSTU_Search(SheetID, "Scenario", "A1", "Z1000"), (x) => OnReceiveScenario(x));
        SpreadsheetManager.ReadPublicSpreadsheet(new GSTU_Search(SheetID, "Job", "A1", "Z1000"), (x) => OnReceiveJob(x));
        //Scene
        //Place
        //Job
        //Item
        //Ability
        //Event
        //Enemy
    }

    void OnReceiveScenario(GstuSpreadSheet ss)
    {
        Scenarios.Clear();

        var rows = ss.columns["A"];
        rows.RemoveAt(0);

        foreach (var p in rows)
        {
            Scenario scenario = new Scenario();
            scenario.name = ss[p.value, "이름"].value;
            scenario.placeName = ss[p.value, "시작 장소"].value;
            scenario.description = ss[p.value, "내용"].value;
            Scenarios.Add(scenario);
            Debug.Log(scenario.ToShow());
        }
    }

    void OnReceiveJob(GstuSpreadSheet ss)
    {
        Jobs.Clear();

        var rows = ss.columns["A"];
        rows.RemoveAt(0);

        foreach (var p in rows)
            Jobs.Add(new Job(ss, p.value));
    }
    
}
