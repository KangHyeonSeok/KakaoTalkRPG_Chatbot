using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

[RequireComponent(typeof(UnityEngine.UI.InputField))]
public class TestInput : MonoBehaviour
{
    public Manager manager;
    private InputField Input;
    void Start()
    {
        Input = GetComponent<InputField>();
    }

    public void OnEnd()
    {
        manager.OnReceive("", Input.text, "", "");
        Input.text = "";
    }
}
