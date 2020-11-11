using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Database : ScriptableObject
{
    public List<Scenario> Scenarios = new List<Scenario>();
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
}
