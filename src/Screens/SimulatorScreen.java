package Screens;

//Processing properties
import processing.core.PApplet;
import Constants.Fonts;
import Constants.Colors;
import processing.core.PFont;

import static Constants.Layout.*;

public class SimulatorScreen extends Screen {
    public SimulatorScreen(PApplet p5, Constants.Colors appColors, Constants.Fonts fonts){
        super(p5, appColors, fonts);
    }

    @Override
    public void display() {
        p5.strokeWeight(5);
        p5.stroke(255);
        p5.line(0, vRect/2, screenH, vRect/2);
    }
}
