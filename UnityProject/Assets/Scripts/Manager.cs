using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;

public class Manager : MonoBehaviour
{
    private static Manager manager;
    public static Manager Instance
    {
        get
        {
            if (manager == null)
                manager = FindObjectOfType<Manager>();
            return manager;
        }
    }

    public Scenario CurrentScenario;
    public Frame CurrentFrame;

    private void Start()
    {
        CurrentFrame = new StartFrame();

        Database.Instance.DataDownload();
    }

    public void OnReceive(string uuid, string msg, string sender,string room)
    {
        Logs.Instance.Bubble($"{sender} : {msg}");
        StringBuilder sb = new StringBuilder("");
        
        if (!msg.StartsWith("."))
        {
            Send(uuid, sb.ToString());
            return;
        }

        CurrentFrame.OnReceiveCommand(uuid, msg, sender, "");
    }


    public void Send(string uuid, string msg)
    {
        Logs.Instance.Bubble($"GM : {msg}");
#if UNITY_EDITOR
        Debug.Log($"{uuid} : {msg}");
#else
        NotiListener.Send(uuid, msg);
#endif
    }

}
