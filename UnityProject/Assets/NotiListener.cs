using UnityEngine;
using UnityEngine.Events;

[System.Serializable]
public class NotiEvent : UnityEvent<string,string,string,string>
{
}

public class NotiListener : MonoBehaviour
{
    private static AndroidJavaClass AndroidListener;

    public NotiEvent OnNoti;

    public void OnReceive(string json)
    {
        if(string.IsNullOrEmpty(json))
        {
            Debug.LogError("NotiListener : Received json is empty");
            return;
        }

        ToUnityArguments arguments = JsonUtility.FromJson<ToUnityArguments>(json);
        if(arguments == null )
        {
            Debug.LogError("NotiListener : Json parsing failed with " + json);
            return;
        }

        OnNoti?.Invoke(arguments.Uuid, arguments.Msg, arguments.Sender, arguments.Room);
    }

    public void SendMsg(string uuid, string msg)
    {
        Send(uuid, msg);
    }

    public static void Send(string uuid, string msg)
    {
        if(AndroidListener == null)
            AndroidListener = new AndroidJavaClass("com.whyk.notilistener.NotiListener");

        FromUnityArguments fromUnityArguments = new FromUnityArguments
        {
            Uuid = uuid,
            Msg = msg
        };
        AndroidListener.CallStatic("ReplyFromUnity", JsonUtility.ToJson(fromUnityArguments));
    }
}

[System.Serializable]
public class ToUnityArguments
{
    public string Uuid;
    public string Room;
    public bool IsGroupChat;
    public string Sender;
    public string Msg;
}

[System.Serializable]
public class FromUnityArguments
{
    public string Uuid;
    public string Msg;
}