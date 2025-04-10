package Screens;

import Components.*;
import Components.Module;
import Constants.FinalColors;
import Constants.Sizes;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;
import java.util.Arrays;

import static Constants.Layout.*;

public class SimulatorScreen extends Screen {
    private final PApplet p5;
    private final float margin = 6;
    //Top bar
    public TextField simuTitleField;

    //Left bar
    public ButtonIcon[] leftButtons;
    public Module beamModule, forceModule;
    float moduleWidth = hRect * 2 - frame - 2 * margin;

    //Beam Module
    public Slider beamSizeSlider;
    public TextField beamSizeField;
    public FieldSlider beamFieldSlider;

    //Force Module
    public int numForces = 1;
    public Button addForceButton;
    public TextField initialValueForceField, initialUbiForceField;
    public Slider initialValueForceSlider, initialUbiForceSlider;
    public ArrayList<FieldSlider> forceValueFieldSliders, forceUbiFieldSliders;
    float forceNameBoxWidth = frame * 0.75f;
    public ArrayList<String> forceNames;
    public ArrayList<Button> deleteButtons;
    public Button initialDeleteForceButton;

    //Moment Module
    private Module momentModule;

    /**
     * Constructor de la pantalla de simulación.
     * Se instancian todos los <code> Components</code> la pantalla.
     *
     * <p> Una barra lateral derecha controla la visualización de los paneles. Estos paneles modifican
     * los atributos de la viga, añaden cargas puntuales y distribuidas, modifican las cargas, añaden
     * y modifican momentos y añaden soportes a la viga.
     *
     * <p> Una barra superior consta de un botón de retorno a la pantalla inicial, botón de guardar
     * proyecto, botón de cerrar y campo de texto para modificar el título.
     *
     * <p> En el cuadrante superior derecho se visualiza un diagrama interactivo de la viga, sus cargas,
     * momentos, y soportes.
     *
     * <p> En el cuadrante inferior derecho un módulo permite la visualización de los gráficos asociados
     * a la viga y acceder a la pantalla de graficación.
     *
     * @param p5
     * @param appColors
     * @param fonts
     */
    public SimulatorScreen(PApplet p5, Constants.Colors appColors, Constants.Fonts fonts) {
        super(p5, appColors, fonts);
        this.p5 = p5;
        initializeSimulatorTitle();
        initializeLeftButtons();
        initializeBeamModule();
        initializePointForceModule();
        initializeMomentModule();

        //Temporal

        forceModule.opened = false;
    }

    @Override
    public void display() {
        //Top bar --------------------------------------------------------------------
        p5.fill(FinalColors.bgBlack());
        p5.strokeWeight(2);
        p5.stroke(FinalColors.bgLightGrey());
        p5.rect(0, 0, screenH, frame);
        p5.rect(0, 0, frame, frame); //Phi6Lab logo
        simuTitleField.display();

        //Left bar -------------------------------------------------------------------
        p5.rect(0, frame, frame, screenV - frame - 1);
        for (ButtonIcon bt : leftButtons) bt.display();

        //Module zone -----------------------------------------------------------------
        beamModule.display();
        forceModule.display();

        if (forceModule.opened) {
            p5.stroke(FinalColors.primaryYellow());
            p5.line(forceModule.x + 2*margin, addForceButton.y + addForceButton.height + 2 * margin + frame*0.75f,
                    forceModule.x+forceModule.width-2*margin, addForceButton.y + addForceButton.height + 2 * margin + frame*0.75f);

            p5.textAlign(PConstants.CENTER, PConstants.CENTER);
            p5.textSize(Sizes.buttonText);
            p5.fill(FinalColors.textWhite());
            p5.text("Valor de la fuerza",
                    initialValueForceField.x + margin + (initialValueForceField.width + margin + initialValueForceSlider.width)/2,
                    addForceButton.y + addForceButton.height + 2 * margin + (frame*0.75f)/2);

            p5.text("Ubicación de la fuerza", initialUbiForceField.x + margin + (initialUbiForceField.width + initialUbiForceSlider.width)/2,
                    addForceButton.y + addForceButton.height + 2 * margin + (frame*0.75f)/2);
        }
    }

    /**
     * Llama a los <code> mousePressed()</code> de cada <code>Components</code>.
     * Hace que los componentes reaccionen en función de la posición del ratón.
     */
    public void mousePressed() {
        simuTitleField.mousePressed();
        beamFieldSlider.mousePressed();
        beamModule.mousePressed();

        if (addForceButton.mouseOverButton(p5)) addPointForce();

        for (FieldSlider fs : forceValueFieldSliders) fs.mousePressed();
        for (FieldSlider fs : forceUbiFieldSliders) fs.mousePressed();

        forceModule.mousePressed();
    }

    public void mouseReleased() {
        beamFieldSlider.mouseReleased();
        for (FieldSlider fs : forceValueFieldSliders) fs.mouseReleased();
        for (FieldSlider fs : forceUbiFieldSliders) fs.mouseReleased();
    }

    public void keyPressed(char key, int keyCode) {
        if (simuTitleField.mouseOverTextField(p5)) simuTitleField.keyPressed(key, keyCode);

        if (simuTitleField.mouseOverTextField(p5) || beamSizeSlider.isMouseOverSlider()) {
            beamFieldSlider.keyPressed(key, keyCode);
        }

        for (FieldSlider fs : forceValueFieldSliders) fs.keyPressed(key, keyCode);
        for (FieldSlider fs : forceUbiFieldSliders) fs.keyPressed(key, keyCode);
    }

    /**
     * Instancia un {@link TextField} con el título del simulador.
     */
    private void initializeSimulatorTitle() {
        simuTitleField = new TextField(p5, frame + margin, margin,
                2 * hRect - frame - 2 * margin, frame - 2 * margin);
        simuTitleField.setEmptyText("Nuevo proyecto...");
    }

    /**
     * Instancia varios {@link ButtonIcon} que permiten mostrar los {@link Module}.
     * Estos se situan en el margen derecho superior de la pantalla.
     */
    private void initializeLeftButtons() {
        leftButtons = new ButtonIcon[4];
        String[] iconPaths = {"beam", "support", "load", "momentum"};
        for (int i = 0; i < leftButtons.length; i++) {
            leftButtons[i] = new ButtonIcon(p5, margin, (frame + margin) + frame * i,
                    frame - 2 * margin, frame - 2 * margin);
            leftButtons[i].setIcon("data/icons/" + iconPaths[i] + ".svg");
        }
    }

    /**
     * Instancia todos los componentes para el {@link Module} de propiedades de la viga.
     * El módulo permite controlar la longitud de la viga mediante un {@link FieldSlider}.
     */
    private void initializeBeamModule() {
        beamModule = new Module(p5, frame + margin, frame + margin, moduleWidth, 150);
        beamModule.setTitle("Propiedades viga");

        float beamSizeFieldWidth = beamModule.width / 5 - 4 * margin;

        beamSizeField = new TextField(p5, beamModule.x + 2 * margin, beamModule.y + margin + frame,
                beamSizeFieldWidth, frame * 0.75f);
        beamSizeField.setBorderColor(FinalColors.primaryYellow());
        beamSizeField.borderEnabled = true;

        beamSizeSlider = new Slider(p5, beamModule.x + beamSizeFieldWidth + vMargin, beamModule.y + margin + frame,
                4 * beamModule.width / 5 - 3 * margin, frame * 0.75f, 0, 100, 50);

        beamModule.components.add(beamSizeSlider);
        beamModule.components.add(beamSizeField);
        beamSizeField.setEmptyText("Longitud");
        beamFieldSlider = new FieldSlider(p5, beamSizeField, beamSizeSlider);
    }

    /**
     * Instancia los componentes del {@link Module} de fuerzas puntuales.
     * Un {@link Button} permite añadir una nueva fuerza.
     * Cada fuerzas tiene un {@link FieldSlider} para su valor y otro para su posición en la viga.
     * Todos los componentes se ubican en función de los {@link TextField} y {@link Slider} iniciales.
     * Hay dos listas de <code>FieldSlider</code> que permiten guardar la información de la base de datos.
     */
    private void initializePointForceModule() {
        //Se podría sintetizar usando los métodos de clonación posteriormente añadidos.


        //Instanciación del módulo
        forceModule = new Module(p5, beamModule.x, beamModule.y, beamModule.width, 600);
        forceModule.setTitle("Fuerzas puntuales");

        float inputZoneWidth = (forceModule.width - 3 * margin - forceNameBoxWidth) / 2;

        //Botón de añadir fuerza puntual
        addForceButton = new Button(p5, forceModule.x + 2 * margin, forceModule.y + margin + frame,
                forceModule.width - 4 * margin, frame * 0.75f);
        addForceButton.setText("Añadir fuerza");
        addForceButton.strokeColorOn = FinalColors.primaryYellow();
        addForceButton.strokeColorOff = FinalColors.primaryYellow();
        addForceButton.strokeWeight = 1;


        //Instanciación del TextField inicial del valor de la fuerza
        initialValueForceField = new TextField(p5,
                forceModule.x + 4 * margin + forceNameBoxWidth,
                addForceButton.y + addForceButton.height + frame * 0.75f + 4 * margin,
                inputZoneWidth / 3 - margin, frame * 0.75f);
        initialValueForceField.setBorderColor(FinalColors.primaryYellow());
        initialValueForceField.borderEnabled = true;

        //Instanciación del Slider inicial del valor de la fuerza
        initialValueForceSlider = new Slider(p5,
                initialValueForceField.x + initialValueForceField.width + 2 * margin,
                initialValueForceField.y,
                inputZoneWidth * 2 / 3 - 4 * margin, frame * 0.75f,
                0, 100, 50
        );

        //Añadir a la lista de FieldSliders
        forceValueFieldSliders = new ArrayList<>();
        forceValueFieldSliders.add(new FieldSlider(p5, initialValueForceField, initialValueForceSlider));
        forceModule.components.add(initialValueForceSlider);

        //Instanciación del TextField de la ubicación de la fuerza
        initialUbiForceField = new TextField(p5,
                initialValueForceSlider.x + initialValueForceSlider.width + 3 * margin,
                addForceButton.y + addForceButton.height + frame * 0.75f + 4 * margin,
                inputZoneWidth / 3 - margin, frame * 0.75f);
        initialUbiForceField.setBorderColor(FinalColors.primaryYellow());
        initialUbiForceField.borderEnabled = true;

        //Instanciación del Slider de la ubicación de la fuerza
        initialUbiForceSlider = new Slider(p5,
                initialUbiForceField.x + initialUbiForceField.width + 2 * margin,
                initialValueForceField.y,
                inputZoneWidth * 2 / 3 - 4 * margin, frame * 0.75f,
                0, 100, 50
        );

        //Añadir a la lista de FieldSliders
        forceUbiFieldSliders = new ArrayList<>();
        forceUbiFieldSliders.add(new FieldSlider(p5, initialUbiForceField, initialUbiForceSlider));


        //Añadir componentes al módulo
        forceModule.components.add(addForceButton);

//        forceModule.components.add(initialValueForceField);
//        forceModule.components.add(initialValueForceSlider);
        forceModule.components.addAll(forceValueFieldSliders);

        forceModule.components.add(initialUbiForceField);
        forceModule.components.add(initialUbiForceSlider);

        //Nombre de cada fuerza
        forceNames = new ArrayList<>();
        for (int i = 0; i <= numForces; i++) forceNames.add("" + (i + 1));

        //Instanciación del botón inicial para borrar un fuerza
        initialDeleteForceButton = new Button(p5, forceModule.x + 2 * margin,
                initialValueForceField.y,
                frame * 0.75f, frame * 0.75f);
        forceModule.components.add(initialDeleteForceButton);
        initialDeleteForceButton.strokeColorOn = FinalColors.primaryYellow();
        initialDeleteForceButton.strokeColorOff = FinalColors.primaryYellow();
        initialDeleteForceButton.strokeWeight = 1;
        initialDeleteForceButton.buttonText = Integer.toBinaryString(numForces);
        initialDeleteForceButton.textSize = Sizes.widgetHeading;

        deleteButtons = new ArrayList<>();
        deleteButtons.add(initialDeleteForceButton);

        forceModule.components.addAll(deleteButtons);


    }

    private void initializeMomentModule(){

    }

    /**
     * Instancia nuevos dos {@link FieldSlider} y los añade a una lista.
     * Un <code>FieldSlider</code> controla el valor de la fuerza puntual
     * y otro su posición respecto el origen de la viga.
     */
    private void addPointForce() {
        numForces++;
        int numForceFieldSliders = forceValueFieldSliders.size();

        float y = initialValueForceField.y +
                (initialValueForceField.height + 2 * margin) * numForceFieldSliders;

        //Instanciación de los FieldSlider del valor de la fuerza
        TextField newForceValueField = new TextField(p5,
                initialValueForceField.x, y,
                initialValueForceField.width,
                initialValueForceField.height);
        newForceValueField.setBorderColor(FinalColors.primaryYellow());
        newForceValueField.borderEnabled = true;

        Slider newForceValueSlider = new Slider(p5,
                initialValueForceSlider.x, y,
                initialValueForceSlider.width,
                initialValueForceSlider.height,
                0, 100, 50);


        FieldSlider newForceValueFieldSlider = new FieldSlider(p5, newForceValueField, newForceValueSlider);
        forceValueFieldSliders.add(newForceValueFieldSlider);
        forceModule.components.add(newForceValueSlider);
        forceModule.components.add(newForceValueField);

        //Instanciación de los FieldSlider de la ubicación de la fuerza

        TextField newForceUbiField = new TextField(p5,
                initialUbiForceField.x, y,
                initialUbiForceField.width,
                initialUbiForceField.height);
        newForceUbiField.setBorderColor(FinalColors.primaryYellow());
        newForceUbiField.borderEnabled = true;

        Slider newForceUbiSlider = new Slider(p5,
                initialUbiForceSlider.x, y,
                initialUbiForceSlider.width,
                initialUbiForceSlider.height,
                0, 100, 50);


        FieldSlider newForceUbiFieldSlider = new FieldSlider(p5, newForceUbiField, newForceUbiSlider);
        forceUbiFieldSliders.add(newForceUbiFieldSlider);
        forceModule.components.add(newForceUbiSlider);
        forceModule.components.add(newForceUbiField);

        forceNames.add("" + numForces);
        System.out.println(numForces);

        //Instanciación del botón inicial para borrar un fuerza
        Button newDeleteButton = initialDeleteForceButton.clone();
        newDeleteButton.buttonText = forceNames.getLast();
        newDeleteButton.y = y;
        deleteButtons.add(newDeleteButton);
        forceModule.components.addAll(deleteButtons);
    }

}
