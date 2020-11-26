using System.Text;

public class PlayFrame : Frame
{
    public override void OnReceiveCommand(string uuid, string command, string sender, string previous)
    {
        StringBuilder sb = new StringBuilder(previous);
        switch(command)
        {
            default:
                sb.AppendLine(Manager.Instance.CurrentScenario.ToShow());
                break;
        }

        Manager.Instance.Send(uuid, sb.ToString());
    }
}
