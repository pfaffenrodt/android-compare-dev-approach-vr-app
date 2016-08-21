package de.pfaffenrodt.gearvrf.menu;

/**
 * Created by Dimitri Pfaffenrodt on 07.08.2016.
 */
public interface Control {
    String getId();
    void onPick();
    void onNoPick();
}
