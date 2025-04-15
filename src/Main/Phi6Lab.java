package Main;

//Processing properties

import Components.Button;
import Components.FieldSlider;
import Constants.FinalColors;
import Constants.StaticFonts;
import Screens.SimulatorScreen;
import processing.core.PApplet;
import Constants.Colors;
import Constants.Fonts;

import static Constants.Layout.*;


public class Phi6Lab extends PApplet {
    public static void main(String[] args) {
        PApplet.main("Main.Phi6Lab", args);
    }

    @Override
    public void settings() {
        size(parseInt(screenH), parseInt(screenV));
        smooth(10);
        fullScreen();
    }

    Constants.Colors appColors;
    GUI gui;
    Constants.Fonts appFonts;

    public static DataBase db;

    public void setup() {
        frameRate(80);
        appColors = new Colors(this);
        appFonts = new Fonts(this);
        StaticFonts.initialize(this);

        db = new DataBase("admin", "12345", "simulaciones");


        try {
            db.connect();
        } catch (Exception e) {
            System.out.println("BASE NO CONECTADA");
        }


        gui = new GUI(this, appColors, appFonts, db);
        GUI.setCurrentScreen(GUI.SCREEN.LOGIN);

        System.out.println("H: " + screenH + "V: " + screenV);
    }

    public void draw() {
        background(FinalColors.bgBlack());
        gui.displayActualScreen(this);
        //if (gui.currentScreen instanceof Screens.HomeScreen) text("Usuario: " + GUI.currentUser, 50, screenV-50);
    }


    //KEYBOARD INTERACTION ----------------------------------------------
    public void keyPressed() {
        if (gui.currentScreen instanceof Screens.HomeScreen hs) {
            //TextList
            if (hs.searchBar.getTextField().mouseOverTextField(this)) {
                hs.searchBar.getTextField().keyPressed(key, keyCode);
                hs.searchBar.update(this, appFonts);
            }
        } else if (gui.currentScreen instanceof Screens.SimulatorScreen ss) {
            ss.keyPressed(key, keyCode);
        }

        if (gui.currentScreen instanceof Screens.LoginScreen ls) {
            ls.keyPressed(key, keyCode);
        }

    }

    public void mousePressed() {
        if (gui.currentScreen instanceof Screens.HomeScreen hs) {
            hs.mousePressed(gui);
        } else if (gui.currentScreen instanceof Screens.SimulatorScreen ss) {
            ss.mousePressed();
        }

        if (gui.currentScreen instanceof Screens.LoginScreen ls) {
            ls.mousePressed(gui);
        }


    }

    public void mouseDragged() {
        if (gui.currentScreen instanceof Screens.SimulatorScreen ss) {
            ss.mouseDragged();
        }
    }

    public void mouseReleased() {
        if (gui.currentScreen instanceof SimulatorScreen ss) {
            ss.mouseReleased();
        }
    }

}