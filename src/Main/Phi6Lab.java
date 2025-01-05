package Main;

//Processing properties
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

    public void setup(){
        frameRate(80);
        appColors = new Colors(this);
        appFonts = new Fonts(this);
        gui = new GUI(this, appColors, appFonts);
    }

    public void draw(){
        background(appColors.bgBlack());
        gui.displayActualScreen(this);
    }


    //KEYBOARD INTERACTION ----------------------------------------------
    public void keyPressed(){
        //Printing key
//        System.out.println("keyPressed(): key: " + key + " with keyCode: " + keyCode);

        //Screen switching (temporal)
        int keyNum = Character.getNumericValue(key);
        if(keyNum>=0 && keyNum<=5){
            println("Key reference: " + Character.getNumericValue(key));
            gui.setCurrentScreen(GUI.SCREEN.values()[keyNum]);
        }


        if (gui.currentScreen instanceof Screens.HomeScreen hs){
            //TextList
            if(hs.searchBar.getTextField().mouseOverTextField(this)){
                hs.searchBar.getTextField().keyPressed(key, keyCode);
                hs.searchBar.update(this, appFonts);
            }
        }
    }

    public void mousePressed(){
        println("X: "+mouseX+", Y:"+mouseY);

        if (gui.currentScreen instanceof Screens.HomeScreen hs){
            //"Create" button
            if(hs.createButton.mouseOverButton(this)){
                hs.createButton.setEnabled(true);
                hs.createButton.setText("ENABLED");
            }
            //TextList
            if(hs.searchButton.mouseOverButton(this) && hs.searchButton.isEnabled()){
                hs.selectedText = hs.searchBar.getSelectedValue();
            }
            hs.searchBar.getTextField().isPressed(this);
            hs.searchBar.buttonPressed(this);
        }

    }

    public void mouseDragged(){
        println("MOUSE DRAGGED");
    }

    public void mouseReleased() {
        println("MOUSE RELEASED");
    }

    public void changeVar(){

    }
}