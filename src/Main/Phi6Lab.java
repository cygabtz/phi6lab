package Main;

//Processing properties
import Components.Slider;
import Components.TextField;
import Constants.FinalColors;
import Screens.SimulatorScreen;
import processing.core.PApplet;
import Constants.Colors;
import Constants.Fonts;

import java.util.Arrays;

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

    public DataBase db;

    public void setup(){
        frameRate(80);
        appColors = new Colors(this);
        appFonts = new Fonts(this);
        gui = new GUI(this, appColors, appFonts);

        db = new DataBase("admin", "12345", "simulaciones");
        db.connect();


        System.out.println( String.valueOf(db.totalFuerzasPuntuales()  ) );

//        String[] infoColumna = db.getInfoArray("tipo", "NOMBRE");
//        println("COLUMNA: " );
//        printArray(infoColumna);
//
//        System.out.println( Arrays.deepToString(db.getInfoArray2DTipo()) );
//
//        System.out.println( Arrays.deepToString(db.getInfoArray2DElementoABAJO()) );
//
//        System.out.println( Arrays.deepToString( db.getTipoOfElement() ) );
//
//        System.out.println( String.valueOf(db.totalFuerzasPuntuales()  ) );
//
//        String user = "usuarioEjemplo";
//        String pass = "342";
//        System.out.println(db.isUserPass(user, pass));
//
//        String newUser = "pepito";
//        String newPass = "qwerty";
//        db.insertUser(newUser, newPass);
//
//        db.deleteUser(newUser);
//
//        String originalUser = "martina56";
//        String originalPass = "1111";
//        db.insertUser(originalUser, originalPass);
//        db.updateUser(user, "martinaPatata", "2222");
//
//        System.out.println( Arrays.deepToString( db.getInfoSimuladores()   ) );
    }

    public void draw(){
        background(FinalColors.bgBlack());
        gui.displayActualScreen(this);
    }


    //KEYBOARD INTERACTION ----------------------------------------------
    public void keyPressed(){
        //Printing key
//        System.out.println("keyPressed(): key: " + key + " with keyCode: " + keyCode);

        //Selección de pantalla
        int keyNum = Character.getNumericValue(key);
//        if(keyNum>=0 && keyNum<=4){
//            println("Key reference: " + Character.getNumericValue(key));
//            gui.setCurrentScreen(GUI.SCREEN.values()[keyNum]);
//        }


        if (gui.currentScreen instanceof Screens.HomeScreen hs){
            //TextList
            if(hs.searchBar.getTextField().mouseOverTextField(this)){
                hs.searchBar.getTextField().keyPressed(key, keyCode);
                hs.searchBar.update(this, appFonts);
            }
        }
        else if (gui.currentScreen instanceof Screens.SimulatorScreen ss){
            if (ss.nameField.mouseOverTextField(this)) ss.nameField.keyPressed(key, keyCode);

            TextField bsf = ss.beamSizeField;
            Slider s = ss.beamSizeSlider;
            bsf.keyPressed(key, keyCode);
            if(!bsf.text.isEmpty() &&  keyNum>=s.minValue && keyNum<=s.maxValue){
                s.setValueAt(keyNum);
            }


        }
        else if (gui.currentScreen instanceof Screens.GraphScreen){

        }
        else if (gui.currentScreen instanceof Screens.SettingsScreen){

        }
        else if (gui.currentScreen instanceof Screens.AboutScreen){

        }
    }

    public void mousePressed(){
        //println("X: "+mouseX+", Y:"+mouseY);

        if (gui.currentScreen instanceof Screens.HomeScreen hs){
            //"Create" button
            if(hs.newButtonIcon.mouseOverButton(this)){
                hs.newButtonIcon.setEnabled(true);
                gui.setCurrentScreen(GUI.SCREEN.SIMULATOR);
            }
            //Gallery buttons
            for (Button cardButton : hs.gallery.getCards()) {
                if(cardButton.mouseOverButton(this)){
                    cardButton.setEnabled(true);
                    gui.setCurrentScreen(GUI.SCREEN.SIMULATOR);
                }
            }
            //TextList
            if(hs.searchButton.mouseOverButton(this) && hs.searchButton.isEnabled()){
                hs.selectedText = hs.searchBar.getSelectedValue();
            }
            hs.searchBar.getTextField().mousePressed(this);
            hs.searchBar.buttonPressed(this);
        }
        else if (gui.currentScreen instanceof Screens.SimulatorScreen ss){
            ss.nameField.mousePressed(this);
            //ss.leftBox.mousePressed();
            ss.beamSizeSlider.mousePressed();
            ss.beamSizeField.mousePressed(this);
        }

    }

    public void mouseDragged(){
        //println("MOUSE DRAGGED");
        if (gui.currentScreen instanceof Screens.SimulatorScreen ss){
            //ss.leftBox.mouseDragged();

        }
    }

    public void mouseReleased() {
        if (gui.currentScreen instanceof SimulatorScreen ss){
            ss.beamSizeSlider.mouseReleased();
        }
    }

    public void changeVar(){

    }
}