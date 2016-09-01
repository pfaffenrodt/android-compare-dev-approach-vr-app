using UnityEngine;
using System.Collections;
using UnityEngine.EventSystems;
using UnityEngine.SceneManagement;

public class OpenSceneOnClick : MonoBehaviour, IPointerClickHandler
{
    public int targetScene;
    // Use this for initialization
    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {

    }

    void OnClick()
    {
    }

    public void OnPointerClick(PointerEventData eventData)
    {
        Debug.Log("open scene" + targetScene);
        SceneManager.LoadScene(targetScene);
    }

}
