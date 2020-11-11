using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;

public class Manager : MonoBehaviour
{
    private Scenario CurrentScenario;

    public void OnReceive(string uuid, string msg, string sender,string room)
    {
        StringBuilder sb = new StringBuilder("");
        
        if (!msg.StartsWith("."))
        {
            Send(uuid, sb.ToString());
            return;
        }

        if(CurrentScenario == null )
        {
            switch(msg)
            {                
                case string name when Database.Instance.Scenarios.Exists((x)=> x.name == name):
                    CurrentScenario = Database.Instance.Scenarios.Find((x) => x.name == name);
                    sb.AppendLine($"{name} 시나리오가 선택 되었습니다.");
                    break;
                case string _ when msg.CompareTo(".시작") == 0:
                default:
                    {
                        sb.AppendLine("시나리오를 선택 하여 주세요.");
                        foreach (Scenario scenario in Database.Instance.Scenarios)
                            sb.AppendLine(scenario.ToShow());
                    }
                    break;
            }
        }

        Send(uuid, sb.ToString());
    }


    void Send(string uuid, string msg)
    {
#if UNITY_EDITOR
        Debug.Log($"{uuid} : {msg}");
#else
        NotiListener.Send(uuid, msg);
#endif

    }

}
