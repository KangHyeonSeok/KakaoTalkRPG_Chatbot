using System.Collections;
using System.Collections.Generic;
using System.Text;
using UnityEngine;

public class CharacterSelectFrame : Frame
{
    public override void OnReceiveCommand(string uuid, string command, string sender, string previous)
    {
        StringBuilder sb = new StringBuilder(previous);
        switch (command)
        {
            case string name when Database.Instance.Jobs.Exists((x) => "." + x.name == name):
                Database.Instance.Jobs.Find((x) => "." + x.name == name).User = sender;
                sb.AppendLine($"{sender}님은 {name}을 선택 하였습니다.");
                break;
            case string next when next.CompareTo(".다음") == 0:
                Manager.Instance.CurrentFrame = new ScenarioSelectFrame();
                Manager.Instance.CurrentFrame.OnReceiveCommand(uuid, "", sender, "캐릭터 선택을 종료 하였습니다.");
                return;
            default:
                {
                    if (!string.IsNullOrEmpty(previous))
                        sb.AppendLine();
                    sb.AppendLine("캐릭터를 선택 하여 주세요.");
                    foreach (Job job in Database.Instance.Jobs)
                        sb.AppendLine(job.ToShow());
                }
                break;
        }
        Manager.Instance.Send(uuid, sb.ToString());
    }
}
