using System.Collections;
using System.Collections.Generic;
using UnityEngine;

abstract public class Frame
{
    abstract public void OnReceiveCommand(string uuid, string command, string sender, string previous);
}
