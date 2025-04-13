package Screens;

import Components.*;
import Components.Module;
import Constants.FinalColors;
import Constants.Sizes;
import SimulationEngine.BeamDrawing;
import SimulationEngine.Elements;
import SimulationEngine.SimuZone;
import org.matheclipse.core.eval.interfaces.IArrayFunction;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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

    float beamSize = 10;

    //Force Module
    public int numForces = 1;
    public Button addForceButton;
    public TextField initialValueForceField, initialUbiForceField;
    public Slider initialValueForceSlider, initialUbiForceSlider;
    public ArrayList<FieldSlider> forceValueFieldSliders, forceUbiFieldSliders;
    float forceNameBoxWidth = frame * 0.75f;
    public ArrayList<String> forceNames;
    public ArrayList<Button> forceDeleteButtons;
    public Button initialDeleteForceButton;

    //Moment Module
    private Module momentModule;
    private int numMoments = 1;
    public Button addMomentButton;
    public TextField initialValueMomentField, initialUbiMomentField;
    public Slider initialValueMomentSlider, initialUbiMomentSlider;
    public ArrayList<FieldSlider> momentValueFieldSliders, momentUbiFieldSliders;
    float momentNameBoxWidth = frame * 0.75f;
    public ArrayList<String> momentNames;
    public ArrayList<Button> momentDeleteButtons;
    public Button initialDeleteMomentButton;

    // Módulo de apoyos
    private Elements.Support supportA = null;
    private Elements.Support supportB = null;
    private Module supportModule;
    private Button deleteButtonA, deleteButtonB;
    private Slider supportSliderA, supportSliderB;
    private TextField supportFieldA, supportFieldB;
    private FieldSlider supportFieldSliderA, supportFieldSliderB;
    private Button pinButtonA, pinButtonB;
    private Button rollerButtonA, rollerButtonB;
    private Button fixedButtonA, fixedButtonB;

    private boolean supportActiveA = false;
    private boolean supportActiveB = false;
    private Elements.SUPPORT_TYPE typeA, typeB;


    // Simulation Zone Paramenters

    BeamDrawing beamDrawing;
    SimuZone simuZone;

    // Límites máximos ajustables
    private float maxForceMagnitude = 1000f;
    private float maxMomentMagnitude = 1000f;


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
        initializeSimulationZone();
        initializeSupportModule();
        closeAllModules();
        setAllUbiSlidersMaxTo(beamSizeSlider.value);
    }

    private void checkForceIntegrity() {
        int valCount = forceValueFieldSliders.size();
        int ubiCount = forceUbiFieldSliders.size();
        int btnCount = forceDeleteButtons.size();
        int drawCount = simuZone.forces.size();

        System.out.println("---------- Verificación de Fuerzas ----------");
        System.out.println("Value sliders: " + valCount);
        System.out.println("Position sliders: " + ubiCount);
        System.out.println("Delete buttons: " + btnCount);
        System.out.println("SimuZone forces: " + drawCount);

        boolean ok = true;

        if (valCount != ubiCount || valCount != btnCount || valCount != drawCount) {
            System.out.println("Inconsistencia en conteos");
            ok = false;
        }

        // Verifica que cada componente fue eliminado visualmente
        for (FieldSlider fs : forceValueFieldSliders) {
            if (!forceModule.components.contains(fs.slider) || !forceModule.components.contains(fs.textField)) {
                System.out.println("Value FieldSlider no está en forceModule.components");
                ok = false;
            }
        }

        for (FieldSlider fs : forceUbiFieldSliders) {
            if (!forceModule.components.contains(fs.slider) || !forceModule.components.contains(fs.textField)) {
                System.out.println("Ubi FieldSlider no está en forceModule.components");
                ok = false;
            }
        }

        for (Button b : forceDeleteButtons) {
            if (!forceModule.components.contains(b)) {
                System.out.println("DeleteButton no está en forceModule.components");
                ok = false;
            }
        }

        if (ok) {
            System.out.println("Todo está sincronizado");
        }
    }


    @Override
    public void display() {
        //System.out.println(Arrays.toString(simuZone.supports.toArray()));

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
        momentModule.display();

        if (forceModule.opened) {
            displayModuleHeaders(forceModule, "Valor de la fuerza", "Ubicación de la fuerza",
                    initialValueForceField, initialValueForceSlider,
                    initialUbiForceField, initialUbiForceSlider,
                    addForceButton);
        }

        if (momentModule.opened) {
            displayModuleHeaders(momentModule, "Valor del momento", "Ubicación del momento",
                    initialValueMomentField, initialValueMomentSlider,
                    initialUbiMomentField, initialUbiMomentSlider,
                    addMomentButton);
        }

        p5.fill(FinalColors.bgLightGrey());
        p5.noStroke();
        p5.rect(2 * hRect + 2 * margin, frame + 2 * margin, 3 * hRect - 4 * margin, 4 * vRect - 4 * margin - frame, corner);

        //SimuZone

        simuZone.display();

        // Support Module
        supportModule.display();
    }

    /**
     * Dibuja encabezados para un módulo
     *
     * @param module
     * @param label1
     * @param label2
     * @param initialValueField
     * @param initialValueSlider
     * @param initialUbiField
     * @param initialUbiSlider
     * @param addButton
     */
    private void displayModuleHeaders(Module module, String label1, String label2,
                                      TextField initialValueField, Slider initialValueSlider,
                                      TextField initialUbiField, Slider initialUbiSlider,
                                      Button addButton) {
        p5.stroke(FinalColors.primaryYellow());
        p5.line(module.x + 2 * margin,
                addButton.y + addButton.height + 2 * margin + frame * 0.75f,
                module.x + module.width - 2 * margin,
                addButton.y + addButton.height + 2 * margin + frame * 0.75f);

        p5.textAlign(PConstants.CENTER, PConstants.CENTER);
        p5.textSize(Sizes.buttonText);
        p5.fill(FinalColors.textWhite());

        p5.text(label1,
                initialValueField.x + margin + (initialValueField.width + margin + initialValueSlider.width) / 2,
                addButton.y + addButton.height + 2 * margin + (frame * 0.75f) / 2);

        p5.text(label2,
                initialUbiField.x + margin + (initialUbiField.width + initialUbiSlider.width) / 2,
                addButton.y + addButton.height + 2 * margin + (frame * 0.75f) / 2);
    }


    /**
     * Llama a los <code> mousePressed()</code> de cada <code>Components</code>.
     * Hace que los componentes reaccionen en función de la posición del ratón.
     */
    public void mousePressed() {
        simuTitleField.mousePressed();

        // Abrir un solo módulo a la vez

        if (leftButtons[0].mouseOverButton(p5)) {
            closeAllModules();
            beamModule.opened = true;
        }

        if (leftButtons[1].mouseOverButton(p5)) {
            closeAllModules();
            supportModule.opened = true;
        }

        if (leftButtons[2].mouseOverButton(p5)) {
            closeAllModules();
            forceModule.opened = true;
        }

        if (leftButtons[3].mouseOverButton(p5)) {
            closeAllModules();
            momentModule.opened = true;
        }

        // Realizar acciones en función de sí el módulo está abierto

        if (beamModule.opened) {
            beamModule.mousePressed();
            beamFieldSlider.mousePressed();

            // Asegurarse de que ningún elemento se puede colocar más allá de la longitud de la viga
            setAllUbiSlidersMaxTo(beamSizeSlider.value);
        }

        if (forceModule.opened) {

            // Añadir fuerza si se pulsa "Añadir fuerza"
            if (addForceButton.mouseOverButton(p5)) addPointForce();

            // Actualizar el estado de los FieldSliders
            for (FieldSlider fs : forceValueFieldSliders) fs.mousePressed();
            for (FieldSlider fs : forceUbiFieldSliders) fs.mousePressed();

            // Cambiar el estado de las fuerzas del motor de simulación
            updateForces();

            // Bóton de cerrar del módulo
            forceModule.mousePressed();

            // Eliminar fuerzas si se pulsa el botón de eliminar
            for (int i = 0; i < forceDeleteButtons.size(); i++) {
                if (forceDeleteButtons.get(i).mouseOverButton(p5)) {
                    deletePointForce(i);
                    break;
                }
            }
        }

        if (momentModule.opened) {
            // Añadir momento
            if (addMomentButton.mouseOverButton(p5)) addMoment();

            // Actualizar el estado de los FieldSliders
            for (FieldSlider fs : momentValueFieldSliders) fs.mousePressed();
            for (FieldSlider fs : momentUbiFieldSliders) fs.mousePressed();

            // Bóton de cerrar del módulo
            momentModule.mousePressed();

            // Eliminar fuerzas si se pulsa el botón de eliminar
            for (int i = 0; i < momentDeleteButtons.size(); i++) {
                if (momentDeleteButtons.get(i).mouseOverButton(p5)) {
                    deleteMoment(i);
                    break;
                }
            }

            // Cambiar el estado de las fuerzas del motor de simulación
            updateMoments();
        }

        if (supportModule.opened) {

            // Activar / desactivar apoyo
            if (deleteButtonA.mouseOverButton(p5)) toggleSupportA();
            if (deleteButtonB.mouseOverButton(p5)) toggleSupportB();

            // Seleccionar el tipo de apoyo de A
            if (pinButtonA.mouseOverButton(p5)) updateSupportTypeA(Elements.SUPPORT_TYPE.PIN);
            if (rollerButtonA.mouseOverButton(p5)) updateSupportTypeA(Elements.SUPPORT_TYPE.ROLLER);
            if (fixedButtonA.mouseOverButton(p5)) updateSupportTypeA(Elements.SUPPORT_TYPE.FIXED);

            // Seleccionar el tipo de apoyo de B
            if (pinButtonB.mouseOverButton(p5)) updateSupportTypeB(Elements.SUPPORT_TYPE.PIN);
            if (rollerButtonB.mouseOverButton(p5)) updateSupportTypeB(Elements.SUPPORT_TYPE.ROLLER);
            if (fixedButtonB.mouseOverButton(p5)) updateSupportTypeB(Elements.SUPPORT_TYPE.FIXED);

            // Botón de cerrar módulo
            supportModule.mousePressed();

        }

        // Actulizar listas de nombres en SimuZone
        updateLabels();

    }

    private void updateSimuBeam() {
        simuZone.beamValue = beamFieldSlider.slider.value;
    }

    public void mouseDragged() {
        if (beamModule.opened) {
            updateSimuBeam();
        } else if (forceModule.opened) {
            updateForces();
        }

        if (supportModule.opened) {
            supportFieldSliderA.mousePressed();
            supportFieldSliderB.mousePressed();
            supportModule.mousePressed();
            updateSupportPosition("A");
            updateSupportPosition("B");
            updateMoments();
        }

        // Actulizar listas de nombres en SimuZone
        updateLabels();
    }

    private void updateForces() {
        for (int i = 0; i < numForces; i++) {
            float magnitude = forceValueFieldSliders.get(i).slider.value;
            float ubi = forceUbiFieldSliders.get(i).slider.value;
            if (simuZone.forces.isEmpty()) {
                System.out.println("No se puede actualizar la lista de fuerza de SimuZone porque está vacía");
            } else {
                simuZone.forces.get(i).setMagnitude(magnitude);
                simuZone.forces.get(i).setPosition(ubi);
            }
        }
    }

    private void updateMoments() {
        for (int i = 0; i < numMoments; i++) {
            float magnitude = momentValueFieldSliders.get(i).slider.value;
            float ubi = momentUbiFieldSliders.get(i).slider.value;
            if (simuZone.forces.isEmpty()) {
                System.out.println("No se puede actualizar la lista de momentos de SimuZone porque está vacía");
            } else {
                //System.out.println("Momento "+i+" actualizado a magnitude "+ magnitude + " y ubi "+ubi);
                simuZone.moments.get(i).setMagnitude(magnitude);
                simuZone.moments.get(i).setPosition(ubi);
            }
        }
    }

    public void mouseReleased() {

        //Beam Module
        if (beamModule.opened) {
            beamFieldSlider.mouseReleased();
            setAllUbiSlidersMaxTo(beamSizeSlider.value);
        }

        // Force Module
        if (forceModule.opened) {
            for (FieldSlider fs : forceValueFieldSliders) fs.mouseReleased();
            for (FieldSlider fs : forceUbiFieldSliders) fs.mouseReleased();
            updateForces();
        }

        // Moment Module
        if (momentModule.opened) {
            for (FieldSlider fs : momentValueFieldSliders) fs.mouseReleased();
            for (FieldSlider fs : momentUbiFieldSliders) fs.mouseReleased();
            updateMoments();
        }

        // Support Module
        if (supportModule.opened) {
            supportFieldSliderA.mouseReleased();
            supportFieldSliderB.mouseReleased();
            updateSupportPosition("A");
            updateSupportPosition("B");

        }

        // Actulizar listas de nombres en SimuZone
        updateLabels();
    }

    /**
     * Controla la interacción con el teclado. Se debe estar encima para cambiar el texto
     *
     * @param key
     * @param keyCode
     */
    public void keyPressed(char key, int keyCode) {

        // Cambiar nombre de la simulación
        if (simuTitleField.mouseOverTextField(p5)) simuTitleField.keyPressed(key, keyCode);

        // Beam Module
        beamFieldSlider.keyPressed(key, keyCode);
        updateSimuBeam();

        // Force Module
        for (FieldSlider fs : forceValueFieldSliders) fs.keyPressed(key, keyCode);
        for (FieldSlider fs : forceUbiFieldSliders) fs.keyPressed(key, keyCode);
        updateForces();

        // Moment Module
        for (FieldSlider fs : momentValueFieldSliders) fs.keyPressed(key, keyCode);
        for (FieldSlider fs : momentUbiFieldSliders) fs.keyPressed(key, keyCode);
        updateMoments();

        // Support Module
        supportFieldSliderA.keyPressed(key, keyCode);
        supportFieldSliderB.keyPressed(key, keyCode);

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
        beamSizeSlider.maxValue = 100;
        beamSizeSlider.value = 0;

        beamModule.components.add(beamSizeSlider);
        beamModule.components.add(beamSizeField);
        beamSizeField.setEmptyText("Longitud");
        beamFieldSlider = new FieldSlider(p5, beamSizeField, beamSizeSlider);
        beamFieldSlider.slider.setValueAt(10);
        beamFieldSlider.textField.setText("10");
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
                0, maxForceMagnitude, 50
        );
        initialValueForceSlider.value = 10;

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
        initialUbiForceSlider.value = 0;
        initialUbiForceSlider.maxValue = beamFieldSlider.slider.value;

        //Añadir a la lista de FieldSliders
        forceUbiFieldSliders = new ArrayList<>();
        forceUbiFieldSliders.add(new FieldSlider(p5, initialUbiForceField, initialUbiForceSlider));


        //Añadir componentes al módulo
        forceModule.components.add(addForceButton);
        forceModule.components.addAll(forceValueFieldSliders);
        forceModule.components.add(initialUbiForceField);
        forceModule.components.add(initialUbiForceSlider);

        //Nombre de cada fuerza
        forceNames = new ArrayList<>();
        for (int i = 0; i < numForces; i++) forceNames.add("" + (i + 1));

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

        forceDeleteButtons = new ArrayList<>();
        forceDeleteButtons.add(initialDeleteForceButton);
        forceModule.components.addAll(forceDeleteButtons);

    }

    private void initializeMomentModule() {
        //Se podría sintetizar usando los métodos de clonación posteriormente añadidos.

        //Instanciación del módulo
        momentModule = new Module(p5, beamModule.x, beamModule.y, beamModule.width, 600);
        momentModule.setTitle("Momentos puntuales");

        float inputZoneWidth = (momentModule.width - 3 * margin - momentNameBoxWidth) / 2;

        //Botón de añadir momento puntual
        addMomentButton = new Button(p5, momentModule.x + 2 * margin, momentModule.y + margin + frame,
                momentModule.width - 4 * margin, frame * 0.75f);
        addMomentButton.setText("Añadir momento");
        addMomentButton.strokeColorOn = FinalColors.primaryYellow();
        addMomentButton.strokeColorOff = FinalColors.primaryYellow();
        addMomentButton.strokeWeight = 1;


        //Instanciación del TextField inicial del valor del momento
        initialValueMomentField = new TextField(p5,
                momentModule.x + 4 * margin + momentNameBoxWidth,
                addMomentButton.y + addMomentButton.height + frame * 0.75f + 4 * margin,
                inputZoneWidth / 3 - margin, frame * 0.75f);
        initialValueMomentField.setBorderColor(FinalColors.primaryYellow());
        initialValueMomentField.borderEnabled = true;

        //Instanciación del Slider inicial del valor del momento
        initialValueMomentSlider = new Slider(p5,
                initialValueMomentField.x + initialValueMomentField.width + 2 * margin,
                initialValueMomentField.y,
                inputZoneWidth * 2 / 3 - 4 * margin, frame * 0.75f,
                0, maxMomentMagnitude, 50
        );
        momentModule.components.add(initialValueMomentSlider);

        //Añadir a la lista de FieldSliders
        momentValueFieldSliders = new ArrayList<>();
        momentValueFieldSliders.add(new FieldSlider(p5, initialValueMomentField, initialValueMomentSlider));

        //Instanciación del TextField de la ubicación del momento
        initialUbiMomentField = new TextField(p5,
                initialValueMomentSlider.x + initialValueMomentSlider.width + 3 * margin,
                addMomentButton.y + addMomentButton.height + frame * 0.75f + 4 * margin,
                inputZoneWidth / 3 - margin, frame * 0.75f);
        initialUbiMomentField.setBorderColor(FinalColors.primaryYellow());
        initialUbiMomentField.borderEnabled = true;

        //Instanciación del Slider de la ubicación del momento
        initialUbiMomentSlider = new Slider(p5,
                initialUbiMomentField.x + initialUbiMomentField.width + 2 * margin,
                initialValueMomentField.y,
                inputZoneWidth * 2 / 3 - 4 * margin, frame * 0.75f,
                0, 100, 50
        );

        //Añadir a la lista de FieldSliders
        momentUbiFieldSliders = new ArrayList<>();
        momentUbiFieldSliders.add(new FieldSlider(p5, initialUbiMomentField, initialUbiMomentSlider));

        //Añadir componentes al módulo
        momentModule.components.add(addMomentButton);
        momentModule.components.addAll(momentValueFieldSliders);
        momentModule.components.add(initialUbiMomentField);
        momentModule.components.add(initialUbiMomentSlider);

        //Nombre de cada momento
        momentNames = new ArrayList<>();
        for (int i = 0; i < numForces; i++) momentNames.add("" + (i + 1));

        //Instanciación del botón inicial para borrar un momento
        initialDeleteMomentButton = new Button(p5, momentModule.x + 2 * margin,
                initialValueMomentField.y,
                frame * 0.75f, frame * 0.75f);
        momentModule.components.add(initialDeleteMomentButton);
        initialDeleteMomentButton.strokeColorOn = FinalColors.primaryYellow();
        initialDeleteMomentButton.strokeColorOff = FinalColors.primaryYellow();
        initialDeleteMomentButton.strokeWeight = 1;
        initialDeleteMomentButton.buttonText = Integer.toBinaryString(numForces);
        initialDeleteMomentButton.textSize = Sizes.widgetHeading;

        momentDeleteButtons = new ArrayList<>();
        momentDeleteButtons.add(initialDeleteMomentButton);

        momentModule.components.addAll(momentDeleteButtons);
    }

    /**
     * Instancia nuevos dos {@link FieldSlider} y los añade a una lista.
     * Un <code>FieldSlider</code> controla el valor de la fuerza puntual
     * y otro su posición respecto el origen de la viga.
     */
    private void addPointForce() {
        numForces++;

        // Crear FieldSlider
        newForceFieldSlider();

        // Recalcular valores y posiciones de los sliders
        setAllUbiSlidersMaxTo(beamSizeSlider.value);

        // Añadir fuerza al motor de simulación
        float magnitude = forceValueFieldSliders.getLast().slider.value;
        float ubi = forceUbiFieldSliders.getLast().slider.value;
        simuZone.forces.add(new Elements.Force(magnitude, ubi, false));

        checkForceIntegrity();
    }

    private void newForceFieldSlider() {
        int index = forceValueFieldSliders.size();

        float y = initialValueForceField.y +
                (initialValueForceField.height + 2 * margin) * index;

        // === FieldSlider para magnitud ===
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
        newForceValueSlider.setValueAt(5); // Valor por defecto

        FieldSlider newForceValueFieldSlider = new FieldSlider(p5, newForceValueField, newForceValueSlider);
        newForceValueFieldSlider.setValue(5); // sincroniza ambos

        forceValueFieldSliders.add(newForceValueFieldSlider);
        forceModule.components.add(newForceValueSlider);
        forceModule.components.add(newForceValueField);

        // === FieldSlider para ubicación ===
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
                0, beamFieldSlider.slider.value, beamFieldSlider.slider.value / 2);
        newForceUbiSlider.setValueAt(beamFieldSlider.slider.value / 2); // mitad de la viga

        FieldSlider newForceUbiFieldSlider = new FieldSlider(p5, newForceUbiField, newForceUbiSlider);
        newForceUbiFieldSlider.setValue(beamFieldSlider.slider.value / 2);

        forceUbiFieldSliders.add(newForceUbiFieldSlider);
        forceModule.components.add(newForceUbiSlider);
        forceModule.components.add(newForceUbiField);

        // === Botón eliminar ===
        Button newDeleteButton = initialDeleteForceButton.clone();
        newDeleteButton.buttonText = "" + (index + 1);
        newDeleteButton.y = y;

        forceDeleteButtons.add(newDeleteButton);
        forceModule.components.add(newDeleteButton); // Solo este, no todos

        // Añadir a la lista de nombre
        forceNames.add("" + numForces);
    }

    private void deletePointForce(int forceIndex) {
        System.out.println("ELIMINANDO FUERZA CON ÍNDICE: " + forceIndex);
        numForces--;

        // Eliminar de listas lógicas
        FieldSlider valueFS = forceValueFieldSliders.remove(forceIndex);
        FieldSlider ubiFS = forceUbiFieldSliders.remove(forceIndex);
        Button deleteButton = forceDeleteButtons.remove(forceIndex);

        // Eliminar del motor de simulación
        simuZone.forces.remove(forceIndex);

        // Eliminar visualmente del módulo
        forceModule.components.remove(deleteButton);
        forceModule.components.remove(valueFS.slider);
        forceModule.components.remove(valueFS.textField);
        forceModule.components.remove(ubiFS.slider);
        forceModule.components.remove(ubiFS.textField);

        // Eliminar de la lista de nombres
        forceNames.remove(forceIndex);
        updateLabels();

        checkForceIntegrity();
        repositionForceComponents();
    }

    private void repositionForceComponents() {
        for (int i = 0; i < forceValueFieldSliders.size(); i++) {
            float y = initialValueForceField.y +
                    (initialValueForceField.height + 2 * margin) * i;

            // Reasignar y a valor (FieldSlider)
            FieldSlider valueFS = forceValueFieldSliders.get(i);
            valueFS.textField.y = y;
            valueFS.slider.y = y;

            // Reasignar y a posición (FieldSlider)
            FieldSlider ubiFS = forceUbiFieldSliders.get(i);
            ubiFS.textField.y = y;
            ubiFS.slider.y = y;

            // Reasignar y al botón de eliminar
            Button deleteBtn = forceDeleteButtons.get(i);
            deleteBtn.y = y;
        }
    }

    private void addMoment() {
        numMoments++;

        // Crear nuevo FielSlider
        newMomentFieldSlider();

        // Recalcular valores y posiciones de los sliders
        setAllUbiSlidersMaxTo(beamSizeSlider.value);

        // Añadir momento al motor de simulación
        float magnitude = momentValueFieldSliders.getLast().slider.value;
        float ubi = momentUbiFieldSliders.getLast().slider.value;
        simuZone.moments.add(new Elements.Moment(magnitude, ubi, true));

        checkMomentIntegrity();
    }

    private void newMomentFieldSlider() {
        int numMomentFieldSliders = momentValueFieldSliders.size();

        float y = initialValueMomentField.y +
                (initialValueMomentField.height + 2 * margin) * numMomentFieldSliders;

        // Instanciación de los FieldSlider del valor del momento
        TextField newMomentValueField = new TextField(p5,
                initialValueMomentField.x, y,
                initialValueMomentField.width,
                initialValueMomentField.height);
        newMomentValueField.setBorderColor(FinalColors.primaryYellow());
        newMomentValueField.borderEnabled = true;

        Slider newMomentValueSlider = new Slider(p5,
                initialValueMomentSlider.x, y,
                initialValueMomentSlider.width,
                initialValueMomentSlider.height,
                0, 100, 50);

        FieldSlider newMomentValueFieldSlider = new FieldSlider(p5, newMomentValueField, newMomentValueSlider);
        momentValueFieldSliders.add(newMomentValueFieldSlider);
        momentModule.components.add(newMomentValueSlider);
        momentModule.components.add(newMomentValueField);

        // Instanciación de los FieldSlider de la ubicación del momento
        TextField newMomentUbiField = new TextField(p5,
                initialUbiMomentField.x, y,
                initialUbiMomentField.width,
                initialUbiMomentField.height);
        newMomentUbiField.setBorderColor(FinalColors.primaryYellow());
        newMomentUbiField.borderEnabled = true;

        Slider newMomentUbiSlider = new Slider(p5,
                initialUbiMomentSlider.x, y,
                initialUbiMomentSlider.width,
                initialUbiMomentSlider.height,
                0, 100, 50);

        FieldSlider newMomentUbiFieldSlider = new FieldSlider(p5, newMomentUbiField, newMomentUbiSlider);
        momentUbiFieldSliders.add(newMomentUbiFieldSlider);
        momentModule.components.add(newMomentUbiSlider);
        momentModule.components.add(newMomentUbiField);

        momentNames.add("" + numMoments);
        System.out.println("NumMoments: " + numMoments);

        // Instanciación del botón inicial para borrar un momento
        Button newDeleteButton = initialDeleteMomentButton.clone();
        newDeleteButton.buttonText = momentNames.getLast();
        newDeleteButton.y = y;
        momentDeleteButtons.add(newDeleteButton);
        momentModule.components.addAll(momentDeleteButtons);
    }

    private void checkMomentIntegrity() {
        int valueCount = momentValueFieldSliders.size();
        int ubiCount = momentUbiFieldSliders.size();
        int btnCount = momentDeleteButtons.size();
        int drawCount = simuZone.moments.size();

        System.out.println("---------- Verificación de Momentos ----------");
        System.out.println("Value sliders: " + valueCount);
        System.out.println("Position sliders: " + ubiCount);
        System.out.println("Delete buttons: " + btnCount);
        System.out.println("BeamDrawing moments: " + drawCount);

        boolean ok = true;

        if (valueCount != ubiCount || valueCount != btnCount || valueCount != drawCount) {
            System.out.println("Inconsistencia en conteos");
            ok = false;
        }

        // Verifica que cada componente esté en el módulo
        for (FieldSlider fs : momentValueFieldSliders) {
            if (!momentModule.components.contains(fs.slider) || !momentModule.components.contains(fs.textField)) {
                System.out.println("Value FieldSlider no está completamente en momentModule.components");
                ok = false;
            }
        }

        for (FieldSlider fs : momentUbiFieldSliders) {
            if (!momentModule.components.contains(fs.slider) || !momentModule.components.contains(fs.textField)) {
                System.out.println(" Ubi FieldSlider no está completamente en momentModule.components");
                ok = false;
            }
        }

        for (Button b : momentDeleteButtons) {
            if (!momentModule.components.contains(b)) {
                System.out.println(" DeleteButton no está en momentModule.components");
                ok = false;
            }
        }

        if (ok) {
            System.out.println(" Todo está sincronizado en momentModule");
        }
    }


    private void deleteMoment(int index) {
        numMoments--;

        // Eliminar de listas lógicas
        FieldSlider valueFS = momentValueFieldSliders.remove(index);
        FieldSlider ubiFS = momentUbiFieldSliders.remove(index);
        Button deleteBtn = momentDeleteButtons.remove(index);

        // Eliminar del motor de simulación
        simuZone.moments.remove(index);

        // Eliminar visualmente del módulo
        momentModule.components.remove(valueFS.slider);
        momentModule.components.remove(valueFS.textField);
        momentModule.components.remove(ubiFS.slider);
        momentModule.components.remove(ubiFS.textField);
        momentModule.components.remove(deleteBtn);

        // Reorganiza los elementos restantes
        repositionMomentComponents();
        checkMomentIntegrity();
    }

    private void repositionMomentComponents() {
        for (int i = 0; i < momentValueFieldSliders.size(); i++) {
            float y = initialValueMomentField.y +
                    (initialValueMomentField.height + 2 * margin) * i;

            // Reposicionar sliders de valor
            FieldSlider valueFS = momentValueFieldSliders.get(i);
            valueFS.textField.y = y;
            valueFS.slider.y = y;

            // Reposicionar sliders de ubicación
            FieldSlider ubiFS = momentUbiFieldSliders.get(i);
            ubiFS.textField.y = y;
            ubiFS.slider.y = y;

            // Reposicionar botón de borrar
            Button deleteBtn = momentDeleteButtons.get(i);
            deleteBtn.y = y;
            deleteBtn.buttonText = "" + (i + 1);
        }
    }

    private void initializeSimulationZone() {
        simuZone = new SimuZone(p5, 2 * hRect + 2 * margin + hRect / 2, 2 * vRect, 3 * hRect - 4 * margin - hRect, frame);

        // Longitud de la viga en m (para calcular las posiciones)
        simuZone.beamValue = beamFieldSlider.slider.value;

        // Cargar nombres en SimuZone
        simuZone.forceLabels = new ArrayList<>(forceNames);
        simuZone.momentLabels = new ArrayList<>(momentNames);

        // Cargar fuerzas en SimuZone
        for (int i = 0; i < numForces; i++) {
            float magnitude = forceValueFieldSliders.get(i).slider.value;
            float position = forceUbiFieldSliders.get(i).slider.value;
            simuZone.forces.add(new Elements.Force(magnitude, position, false));
        }

        // Cargar momentos en SimZone
        for (int i = 0; i < numMoments; i++) {
            float magnitude = momentValueFieldSliders.get(i).slider.value;
            float position = momentUbiFieldSliders.get(i).slider.value;
            simuZone.moments.add(new Elements.Moment(magnitude, position, true));
        }

    }

    private void closeAllModules() {
        beamModule.opened = false;
        forceModule.opened = false;
        momentModule.opened = false;
        supportModule.opened = false;
    }

    private void initializeSupportModule() {
        supportModule = new Module(p5, frame + margin, frame + margin, moduleWidth, 400);
        supportModule.setTitle("Propiedades soporte");

        float supportFieldWidth = supportModule.width / 5 - 4 * margin;

        // Botón de activar desactivar apoyo A
        deleteButtonA = initialDeleteForceButton.clone();
        deleteButtonA.y = supportModule.y + margin + frame;
        deleteButtonA.setText("A");
        supportModule.components.add(deleteButtonA);

        // Crear TextField del apoyo A
        supportFieldA = new TextField(p5,
                deleteButtonA.x + deleteButtonA.width + 2 * margin,
                supportModule.y + margin + frame,
                supportFieldWidth, frame * 0.75f);
        supportFieldA.setBorderColor(FinalColors.primaryYellow());
        supportFieldA.borderEnabled = true;
        supportFieldA.setEmptyText("Posición");

        // Crear Slider del apoyo A
        supportSliderA = new Slider(p5,
                supportFieldA.x + supportFieldA.width + 2 * margin,
                supportModule.y + margin + frame,
                supportModule.width - deleteButtonA.width - supportFieldA.width - 8 * margin - 3 * deleteButtonA.width - 6 * margin,
                frame * 0.75f, 0, 100, 50);
        supportSliderA.maxValue = beamSizeSlider.value;
        supportSliderA.value = supportSliderA.maxValue / 2;

        // Crear FieldSlider
        supportFieldSliderA = new FieldSlider(p5, supportFieldA, supportSliderA);

        // Añadir al módulo
        supportModule.components.add(supportSliderA);
        supportModule.components.add(supportFieldA);

        // Crear TextField del apoyo B

        supportFieldB = supportFieldA.clone();
        supportFieldB.y += supportFieldB.height + 2 * margin;

        // Crear Slider del apoyo B
        supportSliderB = supportSliderA.clone();
        supportSliderB.y += supportSliderB.height + 2 * margin;

        // Crear FieldSlider del apoyo B y añadir al módulo
        supportFieldSliderB = new FieldSlider(p5, supportFieldB, supportSliderB);
        supportModule.components.add(supportSliderB);
        supportModule.components.add(supportFieldB);

        // Botón de activar desactivar apoyo B
        deleteButtonB = deleteButtonA.clone();
        deleteButtonB.y += supportFieldB.height + 2 * margin;
        deleteButtonB.setText("B");
        supportModule.components.add(deleteButtonB);

        // Botones para seleccionar el tipo de apoyo en A

        //Escoger A=PIN
        pinButtonA = deleteButtonA.clone();
        pinButtonA.setText("F"); // F de fijo
        pinButtonA.x = supportSliderA.x + supportSliderA.width + 2 * margin;
        supportModule.components.add(pinButtonA);

        // Escoger A=ROLLER
        rollerButtonA = deleteButtonA.clone();
        rollerButtonA.setText("A"); // A de articulado
        rollerButtonA.x = pinButtonA.x + pinButtonA.width + 2 * margin;
        supportModule.components.add(rollerButtonA);

        // Escoger A=FIXED
        fixedButtonA = deleteButtonA.clone();
        fixedButtonA.setText("E"); // E de empotrado
        fixedButtonA.x = rollerButtonA.x + rollerButtonA.width + 2 * margin;
        supportModule.components.add(fixedButtonA);


        // Botones para seleccionar el apoyo en B
        Button[] supButtonsB = {
                pinButtonB = pinButtonA.clone(),
                rollerButtonB = rollerButtonA.clone(),
                fixedButtonB = fixedButtonA.clone()
        };

        for (Button b : supButtonsB) {
            b.y = supportSliderB.y;
            supportModule.components.add(b);
        }

    }

    private void setAllUbiSlidersMaxTo(float max) {

        // Force Module
        for (FieldSlider fs : forceUbiFieldSliders) {
            fs.slider.maxValue = max;
            // Recalcular posiciones Slider y TextField
            fs.setValue(fs.slider.value);
        }

        // Moment Module
        for (FieldSlider fs : momentUbiFieldSliders) {
            fs.slider.maxValue = max;
            // Recalcular posiciones Slider y TextField
            fs.setValue(fs.slider.value);
        }

        // Support Module
        supportSliderA.maxValue = max;
        supportSliderA.setValueAt(supportSliderA.value);
        supportSliderB.maxValue = max;
        supportSliderB.setValueAt(supportSliderB.value);

    }

    public void setMaxForceMagnitude(float maxVal) {
        this.maxForceMagnitude = maxVal;
        updateForceSliderRanges();
    }

    public void setMaxMomentMagnitude(float maxVal) {
        this.maxMomentMagnitude = maxVal;
        updateMomentSliderRanges();
    }

    private void updateForceSliderRanges() {
        for (FieldSlider fs : forceValueFieldSliders) {
            fs.slider.maxValue = maxForceMagnitude;
        }
    }

    private void updateMomentSliderRanges() {
        for (FieldSlider fs : momentValueFieldSliders) {
            fs.slider.maxValue = maxMomentMagnitude;
        }
    }

    private void updateLabels() {
        simuZone.forceLabels = new ArrayList<>(forceNames);
        simuZone.momentLabels = new ArrayList<>(momentNames);
    }

    private void toggleSupportA() {
        if (!supportActiveA) {
            // Activar
            float pos = supportFieldSliderA.slider.value;
            simuZone.supports.add(new Elements.Support("A", typeA, pos));
            deleteButtonA.setText("A");
            supportActiveA = true;
        } else {
            // Desactivar
            if (!simuZone.supports.isEmpty()) {
                simuZone.supports.removeIf(s -> Objects.equals(s.id, "A"));
            }
            deleteButtonA.setText("D");
            supportActiveA = false;
        }
    }

    private void toggleSupportB() {
        if (!supportActiveB) {
            float pos = supportFieldSliderB.slider.value;
            simuZone.supports.add(new Elements.Support("B", typeB, pos));
            deleteButtonB.setText("A");
            supportActiveB = true;
        } else {
            if (!simuZone.supports.isEmpty()) {
                simuZone.supports.removeIf(s -> Objects.equals(s.id, "B"));
            }
            deleteButtonB.setText("D");
            supportActiveB = false;
        }
    }

    private void updateSupportTypeA(Elements.SUPPORT_TYPE type) {
        typeA = type;

        // Resetear estilo
        pinButtonA.strokeWeight = 1;
        rollerButtonA.strokeWeight = 1;
        fixedButtonA.strokeWeight = 1;

        // Activar estilo del seleccionado
        switch (type) {
            case PIN -> pinButtonA.strokeWeight = 3;
            case ROLLER -> rollerButtonA.strokeWeight = 3;
            case FIXED -> fixedButtonA.strokeWeight = 3;
        }

        // Buscar en simuZone el support con id "A" y actualizar su tipo
        for (Elements.Support s : simuZone.supports) {
            if ("A".equals(s.id)) {
                s.type = type;
                break;
            }
        }
    }

    private void updateSupportTypeB(Elements.SUPPORT_TYPE type) {
        typeB = type;

        // Resetear estilo
        pinButtonB.strokeWeight = 1;
        rollerButtonB.strokeWeight = 1;
        fixedButtonB.strokeWeight = 1;

        // Activar estilo del seleccionado
        switch (type) {
            case PIN -> pinButtonB.strokeWeight = 3;
            case ROLLER -> rollerButtonB.strokeWeight = 3;
            case FIXED -> fixedButtonB.strokeWeight = 3;
        }

        // Buscar en simuZone el support con id "B" y actualizar su tipo
        for (Elements.Support s : simuZone.supports) {
            if ("B".equals(s.id)) {
                s.type = type;
                break;
            }
        }
    }

    private void checkSupportTypes() {
        System.out.println("--------- Verificación de Supports en simuZone ---------");

        if (simuZone.supports.isEmpty()) {
            System.out.println("No hay supports en simuZone.");
            return;
        }

        for (Elements.Support s : simuZone.supports) {
            String id = s.id != null ? s.id : "(sin id)";
            String type = s.type != null ? s.type.toString() : "(sin tipo)";
            System.out.printf("Support %s → Posición: %.2f, Tipo: %s%n", id, s.position, type);
        }

        System.out.println("--------------------------------------------------------");
    }

    private void updateSupportPosition(String id) {
        float newPosition;
        if (id.equals("A")) {
            newPosition = supportFieldSliderA.slider.value;
        } else if (id.equals("B")) {
            newPosition = supportFieldSliderB.slider.value;
        } else {
            return;
        }

        for (Elements.Support s : simuZone.supports) {
            if (id.equals(s.id)) {
                s.position = newPosition;
                System.out.println("Posición del soporte " + id + " actualizada a " + newPosition);
                break;
            }
        }
    }




}
