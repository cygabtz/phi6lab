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
        System.out.println("keyPressed(): key: " + key + " with keyCode: " + keyCode);

        //Screen switching (temporal)
        int keyNum = Character.getNumericValue(key);
        if(keyNum>=0 && keyNum<=5){
            println("Key reference: " + Character.getNumericValue(key));
            gui.setCurrentScreen(GUI.SCREEN.values()[keyNum]);
        }


        if (gui.currentScreen instanceof Screens.HomeScreen hs){
            //TextField
            hs.searchBar.keyPressed(key, keyCode);

            //TextList
            if(hs.tList.getTextField().mouseOverTextField(this)){
                hs.tList.getTextField().keyPressed(key, keyCode);
                hs.tList.update(this, appFonts);
            }
        }
    }

    public void mousePressed(){
        println("X: "+mouseX+", Y:"+mouseY);

        if (gui.currentScreen instanceof Screens.HomeScreen hs){
            //"Create" button
            if(hs.b1.mouseOverButton(this)){
                hs.b1.setEnabled(true);
                hs.b1.setButtonText("ENABLED");
            }
            //TextField
            hs.searchBar.isPressed(this);
            //TextList

            if(hs.b.mouseOverButton(this) && hs.b.isEnabled()){
                hs.selectedText = hs.tList.getSelectedValue();
            }

            // Mirarm si pitjam damunt el textList (camp de text o botó)
            hs.tList.getTextField().isPressed(this);
            hs.tList.buttonPressed(this);
        }

    }

    public void mouseDragged(){
        println("MOUSE DRAGGED");
    }

    public void mouseReleased() {
        println("MOUSE RELEASED");
    }
}