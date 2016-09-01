using UnityEngine;
using System.Collections;

public class Selectable : MonoBehaviour {
    private Animator animator;
	// Use this for initialization
	void Start () {
        animator = GetComponent<Animator>();
	}
	
	// Update is called once per frame
	void Update () {
	
	}

    public void setSelected(bool selected) {
        animator.SetBool("selected", selected);
    }
}
