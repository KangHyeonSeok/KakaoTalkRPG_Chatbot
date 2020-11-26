public class StartFrame : Frame
{
    public override void OnReceiveCommand(string uuid, string command, string sender, string previous)
    {
        if(command.CompareTo(".시작") == 0)
        {
            Manager.Instance.CurrentFrame = new CharacterSelectFrame();
            Manager.Instance.CurrentFrame.OnReceiveCommand(uuid, "", sender, sender + "님이 게임을 시작을 선언 하였습니다.");

        }
        else
        {
            Manager.Instance.Send(uuid, "사용 가능한 명령 : .시작");
        }
    }
}
