using UnityEngine;
using UnityEngine.UI;

public class Logs : MonoBehaviour
{
    private static Logs mInstance;
    public static Logs Instance
    {
        get
        {
            if(mInstance == null)
                mInstance = FindObjectOfType<Logs>();
            return mInstance;
        }
    }

    public Transform Contents;

    public GameObject BubblePrefab;

    private ScrollRect mRect;

    private void Start()
    {
        mRect = GetComponent<ScrollRect>();
    }

    public void Bubble(string msg)
    {
        GameObject instance = Instantiate(BubblePrefab,Contents);
        instance.GetComponent<Text>().text = msg;
        if (Contents.childCount > 100)
            Destroy(Contents.GetChild(0).gameObject);
        if (mRect != null)
            mRect.verticalNormalizedPosition = 1;
    }
}
