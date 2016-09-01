using UnityEngine;
using System.Collections;

public class Controller : MonoBehaviour {
    private Camera mainCamera;
    private RaycastHit hit;
    public GameObject selectedItem;
	// Use this for initialization
	void Start () {
        mainCamera = Camera.main;
    }

    // Update is called once per frame
    void Update()
    {

        Vector3 cameraCenter = mainCamera.ScreenToWorldPoint(new Vector3(Screen.width / 2, Screen.height / 2, mainCamera.nearClipPlane));
        if (Physics.Raycast(cameraCenter, mainCamera.transform.forward, out hit, 1000))
        {
            if (!hit.transform.gameObject.Equals(selectedItem))
            {
                if(selectedItem != null) {
                    Selectable lastSelectable = selectedItem.GetComponent<Selectable>();
                    if (lastSelectable != null)
                    {
                        lastSelectable.setSelected(false);
                    }
                }
                selectedItem = hit.transform.gameObject;
                if(selectedItem != null)
                {
                    Selectable newSelectable = selectedItem.GetComponent<Selectable>();
                    newSelectable.setSelected(true);
                }
            }
        }
    }
}
