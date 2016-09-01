using UnityEngine;
using UnityEngine.UI;
using System.Collections;
public class Menu : MonoBehaviour {
    public NavigationItem[] navigationItems;
    private GameObject[] navigationItemGameObjects;
	// Use this for initialization
	void Start () {
        navigationItemGameObjects = new GameObject[navigationItems.Length]; 
        for (int i = 0; i < navigationItems.Length; i++)
        {
                navigationItemGameObjects[i] = createNavigationItemGameObject(navigationItems[i]);
        }
        positionMenu();
    }
	
	// Update is called once per frame
	void Update () {
	}

    private GameObject createNavigationItemGameObject(NavigationItem navigationItem) {
        GameObject navigationItemGameObject = (GameObject)Instantiate(Resources.Load("prefabs/NavigationItem"));
        navigationItemGameObject.name = "NavigationItem("+navigationItem.title+")";
        navigationItemGameObject.transform.parent = this.transform;
        Image image = navigationItemGameObject.GetComponentInChildren<Image>();
        image.sprite = navigationItem.image;
        Text text = navigationItemGameObject.GetComponentInChildren<Text>();
        text.text = navigationItem.title;
        OpenSceneOnClick openSceneOnClick = navigationItemGameObject.GetComponent<OpenSceneOnClick>();
        openSceneOnClick.targetScene = navigationItem.scene;
        return navigationItemGameObject;
    }

    private void positionMenu()
    {
        int size = navigationItemGameObjects.Length;
        for (int i = 0; i < size; i++)
        {
            Transform transform = navigationItemGameObjects[i].transform;
            transform.Translate(0f, 0f, NavigationItem.NAVIGATION_ITEM_POSITION_Z);
            float degree = 360.0f * i / size;
            Debug.Log("degree: "+degree);
            transform.RotateAround(this.transform.position, Vector3.up, degree);
            Debug.Log(transform);
            Debug.Log(this.transform);
        }
    }
}
