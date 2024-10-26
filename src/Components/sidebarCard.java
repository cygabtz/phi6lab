package Components;


import processing.core.PApplet;
import processing.core.PImage;
import static Constants.Layout.*;


public class sidebarCard {
    private int height;
    private String tittle;
    private String logoDir;
    private PImage pImage;


    public sidebarCard(int height, String tittle, String logoDir){
        this.height = height;
        this.tittle = tittle;
        this.logoDir = logoDir;
    }

    public void displaySideBarCard(PApplet p5, Constants.Colors c){
        p5.pushStyle();
        p5.fill(c.bgLightGrey());
        p5.rect(marginH*2, marginV*2, sideBarCardWidh, this.height, corner);
        p5.popStyle();
    }
}
