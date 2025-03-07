package Screens;

//Processing properties

import Components.*;
import Components.Module;
import Constants.FinalColors;
import processing.core.PApplet;

import java.util.ArrayList;

import static Constants.Layout.*;

public class SimulatorScreen extends Screen {
    private final float margin = 6;
    //Top bar
     //Top bar width
    private float textfieldWidth = 2 * hRect - frame - 2 * margin;
    public TextField nameField;

    //Left bar
    public ButtonIcon[] leftButtons;

    //Left Module Box
    public ModuleBox leftBox;
    public ArrayList<Module> leftModules;
    private Module createModule, beamModule;
    float moduleWidth = hRect * 2 - frame - 2 * margin;
    float moduleBoxHeight = screenV - frame - 2 * margin;
    float moduleHeight = moduleBoxHeight / 8;

    //Beam Module
    public Slider beamSizeSlider;
    public TextField beamSizeField;

    public SimulatorScreen(PApplet p5, Constants.Colors appColors, Constants.Fonts fonts) {
        super(p5, appColors, fonts);

        //Inicialización de componentes

        //Name Field ------------------------------------------------------------------
        nameField = new TextField(p5, frame + margin, margin, textfieldWidth, frame - 2 * margin);
        nameField.setEmptyText("Nuevo proyecto...");

        //Left bar buttons ------------------------------------------------------------
        leftButtons = new ButtonIcon[4];
        String[] iconPaths = {"beam", "support", "load", "momentum"};
        for (int i = 0; i < leftButtons.length; i++) {
            leftButtons[i] = new ButtonIcon(p5, margin, (frame + margin) + frame * i, frame - 2 * margin, frame - 2 * margin);
            //leftButtons[i].setColors(0, appColors.accentSkyBlue(), 0, 0);
            leftButtons[i].setIcon("data/icons/" + iconPaths[i] + ".svg");
        }

        //Left Module Box -------------------------------------------------------------
        leftModules = new ArrayList<>();

        //createModule = new Module(p5, frame + margin, frame + margin, moduleWidth, moduleHeight);
        //ButtonIcon prueba = new ButtonIcon(p5, createModule.x, createModule.y, frame, frame);
        //createModule.components.add(prueba);

        beamModule = new Module(p5, frame + margin, frame + margin, moduleWidth, 300);
        beamModule.setTitle("Propiedades viga");

        float beamSizeFieldWidth = beamModule.width/5 - 4*margin;

        beamSizeField = new TextField(p5, beamModule.x + 2*margin, beamModule.y + margin + frame,
                beamSizeFieldWidth, frame*0.75f);
        beamSizeField.setBorderColor(FinalColors.primaryYellow());
        beamSizeField.borderEnabled = true;

        beamSizeSlider = new Slider(p5, beamModule.x + beamSizeFieldWidth + vMargin, beamModule.y + margin + frame,
                4*beamModule.width/5 - 3*margin, frame*0.75f, 0, 100, 50);

        beamModule.components.add(beamSizeSlider);
        beamModule.components.add(beamSizeField);
        beamSizeField.setEmptyText("Longitud");

        //leftModules.add(createModule);
        leftModules.add(beamModule);

        leftBox = new ModuleBox(p5, leftModules, frame + margin, frame + margin, moduleWidth, moduleBoxHeight);

    }

    @Override
    public void display() {
        //Top bar --------------------------------------------------------------------
        p5.fill(FinalColors.bgBlack());
        p5.strokeWeight(2); p5.stroke(FinalColors.bgLightGrey());
        p5.rect(0, 0, screenH, frame);
        p5.rect(0, 0, frame, frame); //Phi6Lab logo
        nameField.display();

        //Left bar -------------------------------------------------------------------
        p5.rect(0, frame, frame, screenV - frame - 1);
        for (ButtonIcon bt : leftButtons) bt.display();


        //Module box -----------------------------------------------------------------
        leftBox.display();

//        p5.strokeWeight(2);
//        p5.line(hRect*2, bf, hRect*2, screenV);
//        p5.rect(bf+margin, bf+margin, moduleWidth, moduleBoxHeight, corner);

        //createElement.display();
        //Module Box Grid
        //p5.stroke(0, 255, 0);
//        for (int i=0; i<2; i++){
//            for (int j=0; j<8; j++){
//                p5.rect((bf+margin) + moduleWidth/2 * i,(bf+margin) + moduleHeight*j,
//                        (moduleWidth)/2, moduleHeight, corner);
//            }
//        }


    }

}
