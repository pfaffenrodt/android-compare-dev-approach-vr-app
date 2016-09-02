using UnityEngine;
using System.Collections;

public class VideoController : MonoBehaviour {

	// Use this for initialization
	void Start ()
    {
#if !UNITY_ANDROID
        // MovieTexture is not supported on mobile plattform.
        // we have to create your own MovieTexture or buy one in assetstore
        ((MovieTexture)GetComponent<Renderer>().material.mainTexture).Play();
#endif
    }

    // Update is called once per frame
    void Update () {
	
	}
}
