using GoogleSheetsToUnity;
using UnityEngine;

public class TestGoogleSheet : MonoBehaviour
{
    static string SheetID = "1k9cSK7iOqHOD1RzzYR0sW8iM0wz14Vk6Z4Jko5LimOQ";

    [ContextMenu("Test")]
    public void Test()
    {
        SpreadsheetManager.ReadPublicSpreadsheet(new GSTU_Search(SheetID, "Place", "A1", "Z1000"), (x) => OnReceive(x));
    }

    void OnReceive(GstuSpreadSheet ss)
    {
        foreach(var pair in ss.Cells) {
            Debug.Log(pair.Key + " : " + pair.Value.value);
        }
    }
}
