using System.Text;

public class ScenarioSelectFrame : Frame
{    
    public override void OnReceiveCommand(string uuid, string command, string sender, string previous)
    {
        StringBuilder sb = new StringBuilder(previous);
        switch (command)
        {
            case string name when Database.Instance.Scenarios.Exists((x) => "." + x.name == name):
                Manager.Instance.CurrentScenario = Database.Instance.Scenarios.Find((x) => "." + x.name == name);
                sb.AppendLine($"{name} 시나리오가 선택 되었습니다.");
                Manager.Instance.CurrentFrame = new PlayFrame();
                Manager.Instance.CurrentFrame.OnReceiveCommand(uuid, "", sender, sb.ToString());
                return;
            default:
                {
                    if (!string.IsNullOrEmpty(previous))
                        sb.AppendLine();
                    sb.AppendLine("시나리오를 선택 하여 주세요.");
                    foreach (Scenario scenario in Database.Instance.Scenarios)
                        sb.AppendLine(scenario.ToShow());
                }
                break;
        }
        Manager.Instance.Send(uuid, sb.ToString());
    }
}
