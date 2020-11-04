using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NotiListener : MonoBehaviour
{
    public void OnReceive(string json)
    {
        Debug.Log(json);
        StartCoroutine(DelaySend(json));
    }

    IEnumerator DelaySend(string json)
    {
        ToUnityArguments arguments = JsonUtility.FromJson<ToUnityArguments>(json);
        Debug.Log(arguments.Msg);
        AndroidJavaClass androidJavaClass = new AndroidJavaClass("com.whyk.notilistener.NotiListener");
        FromUnityArguments fromUnityArguments = new FromUnityArguments
        {
            Uuid = arguments.Uuid,
            Msg = "이거슨 유니티에서 보내는 메세지 임당!"
        };
        yield return new WaitForSeconds(0.5f);
        Debug.Log("OnUnity Side : " + JsonUtility.ToJson(fromUnityArguments));
        androidJavaClass.CallStatic("ReplyFromUnity", JsonUtility.ToJson(fromUnityArguments));
    }
}

class ToUnityArguments
{
    public string Uuid;
    public string Room;
    public bool IsGroupChat;
    public string Sender;
    public string Msg;
}

class FromUnityArguments
{
    public string Uuid;
    public string Msg;
}