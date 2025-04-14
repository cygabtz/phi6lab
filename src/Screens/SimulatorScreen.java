package Screens;

import Components.*;
import Components.Module;
import Constants.FinalColors;
import Constants.Sizes;
import Main.GUI;
import SimulationEngine.BeamDrawing;
import SimulationEngine.BeamReactionCalculator;
import SimulationEngine.Elements;
import SimulationEngine.SimuZone;
import org.matheclipse.core.eval.ExprEvaluator;
import processing.core.PApplet;
import processing.core.PConstants;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static Constants.Layout.*;
import static SimulationEngine.BeamReactionCalculator.calculateReactions;
import static SimulationEngine.Elements.SUPPORT_TYPE.PIN;
import static SimulationEngine.Elements.SUPPORT_TYPE.ROLLER;

/**
 * Pantalla principal de simulación de estructuras tipo viga.
 *
 * <p>Esta clase extiende {@link Screen} y constituye el núcleo interactivo del entorno de simulación.
 * El usuario puede definir la geometría de la viga, aplicar fuerzas puntuales y momentos,
 * establecer apoyos y calcular las reacciones mediante {@link BeamReactionCalculator}.
 *
 * <p>La interfaz está organizada en distintas zonas:
 * <ul>
 *   <li>Una barra superior con controles generales (título, guardar, calcular).</li>
 *   <li>Una barra lateral izquierda con botones que permiten abrir módulos.</li>
 *   <li>Cuatro módulos interactivos: propiedades de la viga, fuerzas, momentos y apoyos.</li>
 *   <li>Una zona de simulación visual ({@link SimuZone}) donde se representa gráficamente la viga con sus cargas y apoyos.</li>
 *   <li>Un panel de resultados que muestra las reacciones calculadas.</li>
 * </ul>
 *
 * <p>La clase integra elementos visuales ({@link TextField}, {@link Slider}, {@link FieldSlider}),
 * controles de interacción con el teclado y ratón, lógica de sincronización con el motor de simulación
 * y validación visual de integridad de los datos.
 *
 * <p>Se invoca y gestiona desde {@link Main.Phi6Lab} a través de la clase {@link Main.GUI}, siendo la pantalla activa
 * cuando el usuario crea o abre una simulación.
 *
 * @see Main.Phi6Lab
 * @see SimuZone
 * @see BeamReactionCalculator
 * @see Main.GUI
 * @see FieldSlider
 */
public class SimulatorScreen extends Screen {

    // === Configuración base ===

    /**
     * Instancia principal de Processing usada para dibujar y recibir eventos.
     */
    private final PApplet p5;

    /**
     * Margen base usado para espaciar visualmente los elementos.
     */
    private final float margin = 6;

    // === Barra superior ===

    /**
     * Campo de texto editable para el título del proyecto.
     */
    public TextField simuTitleField;

    /**
     * Botón para calcular las reacciones de la viga.
     */
    private Button calculateButton;

    /**
     * Botón para guardar la simulación actual.
     */
    private Button saveButton;

    private Button clearButton;


    // === Barra lateral izquierda ===

    /**
     * Botones que permiten mostrar u ocultar los módulos (viga, apoyos, fuerzas, momentos).
     */
    public ButtonIcon[] leftButtons;

    /**
     * Módulo visual de propiedades de la viga.
     */
    public Module beamModule;

    /**
     * Módulo visual de fuerzas puntuales.
     */
    public Module forceModule;

    /**
     * Ancho estándar de los módulos verticales.
     */
    float moduleWidth = hRect * 2 - frame - 2 * margin;


    // === Módulo de viga ===

    /**
     * Slider que define la longitud de la viga.
     */
    public Slider beamSizeSlider;

    /**
     * Campo de texto vinculado al slider de longitud de la viga.
     */
    public TextField beamSizeField;

    /**
     * {@link FieldSlider} que sincroniza texto y slider de longitud.
     */
    public FieldSlider beamFieldSlider;

    /**
     * Valor actual de longitud de la viga en metros.
     */
    float beamSize = 10;


    // === Módulo de fuerzas puntuales ===

    /**
     * Número total de fuerzas activas.
     */
    public int numForces = 1;

    /**
     * Botón para añadir una nueva fuerza puntual.
     */
    public Button addForceButton;

    /**
     * Campo de texto para la magnitud de la fuerza inicial.
     */
    public TextField initialValueForceField;

    /**
     * Campo de texto para la ubicación de la fuerza inicial.
     */
    public TextField initialUbiForceField;

    /**
     * Slider para la magnitud de la fuerza inicial.
     */
    public Slider initialValueForceSlider;

    /**
     * Slider para la ubicación de la fuerza inicial.
     */
    public Slider initialUbiForceSlider;

    /**
     * Lista de sliders de magnitud de todas las fuerzas.
     */
    public ArrayList<FieldSlider> forceValueFieldSliders;

    /**
     * Lista de sliders de ubicación de todas las fuerzas.
     */
    public ArrayList<FieldSlider> forceUbiFieldSliders;

    /**
     * Ancho reservado para mostrar etiquetas de nombres de fuerzas.
     */
    float forceNameBoxWidth = frame * 0.75f;

    /**
     * Lista de etiquetas (F1, F2...) asignadas a cada fuerza.
     */
    public ArrayList<String> forceNames;

    /**
     * Botones para eliminar fuerzas individuales.
     */
    public ArrayList<Button> forceDeleteButtons;

    /**
     * Botón inicial de eliminación de fuerza.
     */
    public Button initialDeleteForceButton;

//
// === Módulo de momentos ===

    /**
     * Módulo visual de momentos puntuales.
     */
    private Module momentModule;

    /**
     * Número total de momentos activos.
     */
    private int numMoments = 1;

    /**
     * Botón para añadir un nuevo momento.
     */
    public Button addMomentButton;

    /**
     * Campo de texto para la magnitud del momento inicial.
     */
    public TextField initialValueMomentField;

    /**
     * Campo de texto para la ubicación del momento inicial.
     */
    public TextField initialUbiMomentField;

    /**
     * Slider para la magnitud del momento inicial.
     */
    public Slider initialValueMomentSlider;

    /**
     * Slider para la ubicación del momento inicial.
     */
    public Slider initialUbiMomentSlider;

    /**
     * Lista de sliders de magnitud de todos los momentos.
     */
    public ArrayList<FieldSlider> momentValueFieldSliders;

    /**
     * Lista de sliders de ubicación de todos los momentos.
     */
    public ArrayList<FieldSlider> momentUbiFieldSliders;

    /**
     * Ancho reservado para etiquetas de momentos.
     */
    float momentNameBoxWidth = frame * 0.75f;

    /**
     * Lista de etiquetas (M1, M2...) para cada momento.
     */
    public ArrayList<String> momentNames;

    /**
     * Botones para eliminar momentos individuales.
     */
    public ArrayList<Button> momentDeleteButtons;

    /**
     * Botón inicial de eliminación de momento.
     */
    public Button initialDeleteMomentButton;

//
// === Módulo de apoyos ===

    /**
     * Objeto lógico que representa el apoyo A.
     */
    private Elements.Support supportA = null;

    /**
     * Objeto lógico que representa el apoyo B.
     */
    private Elements.Support supportB = null;

    /**
     * Módulo visual para la configuración de apoyos.
     */
    private Module supportModule;

    /**
     * Botón para activar/desactivar el apoyo A.
     */
    private Button deleteButtonA;

    /**
     * Botón para activar/desactivar el apoyo B.
     */
    private Button deleteButtonB;

    /**
     * Slider que define la posición del apoyo A.
     */
    private Slider supportSliderA;

    /**
     * Slider que define la posición del apoyo B.
     */
    private Slider supportSliderB;

    /**
     * Campo de texto vinculado al apoyo A.
     */
    private TextField supportFieldA;

    /**
     * Campo de texto vinculado al apoyo B.
     */
    private TextField supportFieldB;

    /**
     * Slider y campo sincronizados para el apoyo A.
     */
    private FieldSlider supportFieldSliderA;

    /**
     * Slider y campo sincronizados para el apoyo B.
     */
    private FieldSlider supportFieldSliderB;

    /**
     * Botón para seleccionar tipo PIN para el apoyo A.
     */
    private Button pinButtonA;

    /**
     * Botón para seleccionar tipo PIN para el apoyo B.
     */
    private Button pinButtonB;

    /**
     * Botón para seleccionar tipo ROLLER para el apoyo A.
     */
    private Button rollerButtonA;

    /**
     * Botón para seleccionar tipo ROLLER para el apoyo B.
     */
    private Button rollerButtonB;

    /**
     * Botón para seleccionar tipo FIXED para el apoyo A.
     */
    private Button fixedButtonA;

    /**
     * Botón para seleccionar tipo FIXED para el apoyo B.
     */
    private Button fixedButtonB;

    /**
     * Indica si el apoyo A está actualmente activo.
     */
    private boolean supportActiveA = false;

    /**
     * Indica si el apoyo B está actualmente activo.
     */
    private boolean supportActiveB = false;

    /**
     * Tipo actual del apoyo A.
     */
    private Elements.SUPPORT_TYPE typeA;

    /**
     * Tipo actual del apoyo B.
     */
    private Elements.SUPPORT_TYPE typeB;

//
// === Zona de simulación y cálculo ===

    /**
     * Encargado de dibujar gráficamente la viga y sus elementos.
     */
    BeamDrawing beamDrawing;

    /**
     * Motor principal de simulación y renderizado de resultados.
     */
    SimuZone simuZone;

//
// === Parámetros y resultados ===

    /**
     * Límite máximo permitido para las magnitudes de fuerza.
     */
    private float maxForceMagnitude = 1000f;

    /**
     * Límite máximo permitido para las magnitudes de momento.
     */
    private float maxMomentMagnitude = 1000f;

    /**
     * Arreglo con los resultados de las reacciones: [RAx, RAy, MA, RBx, RBy, MB].
     */
    private double[] results = new double[6];

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
        initializeTopBar();
        initializeLeftButtons();
        initializeBeamModule();
        initializePointForceModule();
        initializeMomentModule();
        initializeSimulationZone();
        initializeSupportModule();
        closeAllModules();
        clearEverything();
        setAllUbiSlidersMaxTo(beamSizeSlider.value);
    }

    /**
     * Renderiza todos los elementos gráficos de la pantalla de simulación.
     *
     * <p>Este método debe ser llamado en cada ciclo del método {@link Main.Phi6Lab#draw()},
     * y representa visualmente la totalidad de la interfaz de usuario de la simulación estructural.
     *
     * <p>El orden de visualización está diseñado para mantener la jerarquía visual y funcional del entorno:
     * <ul>
     *   <li><b>Barra superior:</b> incluye el campo de título y botones para calcular, guardar y limpiar.</li>
     *   <li><b>Barra lateral izquierda:</b> contiene los botones de acceso a módulos.</li>
     *   <li><b>Zona de módulos:</b> muestra los módulos activos (viga, fuerzas, momentos) y sus componentes interactivos.</li>
     *   <li><b>Zona de simulación (SimuZone):</b> visualiza gráficamente la viga, fuerzas, momentos y apoyos definidos por el usuario.</li>
     *   <li><b>Módulo de apoyos:</b> permite gestionar visualmente los apoyos A y B.</li>
     *   <li><b>Panel de resultados:</b> muestra las reacciones calculadas con base en la configuración actual.</li>
     * </ul>
     *
     * <p>Se invoca automáticamente por {@link Main.GUI#displayActualScreen(PApplet)} cuando la pantalla activa es un {@code SimulatorScreen}.
     */
    public void display() {
        // Top bar
        displayTopBar();

        // Left bar
        displayLeftBar();

        // Module zone
        beamModule.display();
        forceModule.display();
        momentModule.display();
        displayHeaders();

        // SimuZone
        simuZone.display();

        // Support Module
        supportModule.display();

        // Results Panel
        displayResultsPanel();
    }

    /**
     * Dibuja la barra superior de la pantalla de simulación.
     *
     * <p>Incluye el fondo oscuro superior, el área para el logo de Phi6Lab,
     * el campo de texto editable para el título de la simulación y los botones
     * de "Calcular reacciones", "Guardar" y "Limpiar".
     *
     * <p>Este método es invocado automáticamente dentro de {@link #display()}.
     */
    private void displayTopBar() {
        p5.fill(FinalColors.bgBlack());
        p5.strokeWeight(2);
        p5.stroke(FinalColors.bgLightGrey());
        p5.rect(0, 0, screenH, frame);
        p5.rect(0, 0, frame, frame); //Phi6Lab logo
        simuTitleField.display();

        calculateButton.display();
        saveButton.display();
        clearButton.display();
    }

    /**
     * Dibuja la barra lateral izquierda que contiene los botones de acceso a los módulos.
     *
     * <p>Estos botones permiten abrir módulos como la viga, los apoyos, las fuerzas y los momentos.
     * Solo un módulo puede estar abierto al mismo tiempo.
     *
     * <p>Este método es invocado dentro de {@link #display()}.
     */
    private void displayLeftBar() {
        p5.rect(0, frame, frame, screenV - frame - 1);
        for (ButtonIcon bt : leftButtons) bt.display();
    }

    /**
     * Muestra los encabezados de los módulos abiertos (fuerzas y momentos).
     *
     * <p>Este método se encarga de dibujar etiquetas con los títulos
     * "Valor de la fuerza", "Ubicación de la fuerza", "Valor del momento" y
     * "Ubicación del momento", según el módulo activo.
     *
     * <p>También establece la posición y alineación de estos encabezados en relación
     * con sus respectivos sliders y campos de texto.
     *
     * <p>Internamente llama al método {@link #displayModuleHeaders(Module, String, String, TextField, Slider, TextField, Slider, Button)}.
     */
    private void displayHeaders() {
        if (forceModule.opened) {
            displayModuleHeaders(forceModule, "Valor de la fuerza", "Ubicación de la fuerza", initialValueForceField, initialValueForceSlider, initialUbiForceField, initialUbiForceSlider, addForceButton);
        }

        if (momentModule.opened) {
            displayModuleHeaders(momentModule, "Valor del momento", "Ubicación del momento", initialValueMomentField, initialValueMomentSlider, initialUbiMomentField, initialUbiMomentSlider, addMomentButton);
        }
    }

    /**
     * Muestra los encabezados de los módulos abiertos (fuerzas y momentos).
     *
     * <p>Este método se encarga de dibujar etiquetas con los títulos
     * "Valor de la fuerza", "Ubicación de la fuerza", "Valor del momento" y
     * "Ubicación del momento", según el módulo activo.
     *
     * <p>También establece la posición y alineación de estos encabezados en relación
     * con sus respectivos sliders y campos de texto.
     *
     * <p>Internamente llama al método {@link #displayModuleHeaders(Module, String, String, TextField, Slider, TextField, Slider, Button)}.
     */
    private void displayModuleHeaders(Module module, String label1, String label2, TextField initialValueField, Slider initialValueSlider, TextField initialUbiField, Slider initialUbiSlider, Button addButton) {
        p5.stroke(FinalColors.primaryYellow());
        p5.line(module.x + 2 * margin, addButton.y + addButton.height + 2 * margin + frame * 0.75f, module.x + module.width - 2 * margin, addButton.y + addButton.height + 2 * margin + frame * 0.75f);

        p5.textAlign(PConstants.CENTER, PConstants.CENTER);
        p5.textSize(Sizes.buttonText);
        p5.fill(FinalColors.textWhite());

        p5.text(label1, initialValueField.x + margin + (initialValueField.width + margin + initialValueSlider.width) / 2, addButton.y + addButton.height + 2 * margin + (frame * 0.75f) / 2);

        p5.text(label2, initialUbiField.x + margin + (initialUbiField.width + initialUbiSlider.width) / 2, addButton.y + addButton.height + 2 * margin + (frame * 0.75f) / 2);
    }


    /**
     * Maneja los eventos de presión del botón del ratón dentro de la pantalla de simulación.
     *
     * <p>Este método es llamado desde {@link Main.Phi6Lab#mousePressed()} cuando {@link Main.GUI}
     * tiene como pantalla activa una instancia de {@code SimulatorScreen}.
     *
     * <p>Al presionar el ratón, se evalúan e interpretan las acciones del usuario en distintos contextos:
     * <ul>
     *   <li>Actualización del título del proyecto si se hace clic sobre el campo de texto.</li>
     *   <li>Cálculo de reacciones si se presiona el botón correspondiente.</li>
     *   <li>Apertura de módulos laterales según el ícono presionado.</li>
     *   <li>Interacción con sliders, botones y elementos dentro de los módulos activos.</li>
     * </ul>
     *
     * <p>Cada módulo implementa su propia lógica interna en métodos auxiliares separados
     * para garantizar claridad y mantenibilidad del código.
     */
    public void mousePressed() {
        simuTitleField.mousePressed();

        // Botón de calcular
        calculateButtonMousePressed();

        // Botón de limpiar
        clearButtonMousePressed();

        // Botón de guardar
        saveButtonMousePressed();

        // Abrir un solo módulo a la vez
        leftButtonsMousePressed();


        // Realizar acciones en función de sí el módulo está abierto
        beamModuleMousePressed();
        forceModuleMousePressed();
        momentModuleMousePressed();
        supportModuleMousePressed();

        // Actulizar listas de nombres en SimuZone
        updateLabels();

    }

    private void saveButtonMousePressed() {
        if (saveButton.mouseOverButton(p5)) {
            GUI.currentSimId = saveSimToDB(GUI.currentSimId); // Actualiza o crea
        }
    }

    private void calculateButtonMousePressed() {
        if (calculateButton.mouseOverButton(p5)) {
            updateResults();
        }
    }

    private void clearButtonMousePressed() {
        if (clearButton.mouseOverButton(p5)) clearEverything();
    }

    /**
     * Controla la apertura de módulos desde los botones laterales.
     *
     * <p>Solo se permite un módulo abierto a la vez. Al hacer clic sobre un ícono,
     * se cierran los demás módulos y se activa el correspondiente.
     */
    private void leftButtonsMousePressed() {
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
    }

    /**
     * Maneja la interacción del usuario con el módulo de la viga (beam).
     *
     * <p>Permite modificar la longitud de la viga mediante un {@link FieldSlider}
     * y actualiza automáticamente los límites máximos de posición de fuerzas, momentos y apoyos.
     */
    private void beamModuleMousePressed() {
        if (beamModule.opened) {
            beamModule.mousePressed();
            beamFieldSlider.mousePressed();

            // Asegurarse de que ningún elemento se puede colocar más allá de la longitud de la viga
            setAllUbiSlidersMaxTo(beamSizeSlider.value);
        }
    }

    /**
     * Maneja los eventos del ratón dentro del módulo de fuerzas puntuales.
     *
     * <ul>
     *   <li>Agrega nuevas fuerzas si se presiona el botón "Añadir fuerza".</li>
     *   <li>Actualiza los sliders activos y sincroniza sus valores con {@link SimuZone}.</li>
     *   <li>Permite eliminar fuerzas si se presionan los botones correspondientes.</li>
     *   <li>Permite cerrar el módulo.</li>
     * </ul>
     */
    private void forceModuleMousePressed() {
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
    }

    /**
     * Maneja los eventos del ratón dentro del módulo de momentos.
     *
     * <ul>
     *   <li>Agrega nuevos momentos si se presiona el botón "Añadir momento".</li>
     *   <li>Actualiza sliders y sincroniza valores con {@link SimuZone}.</li>
     *   <li>Permite eliminar momentos existentes.</li>
     *   <li>Permite cerrar el módulo.</li>
     * </ul>
     */
    private void momentModuleMousePressed() {
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
    }

    /**
     * Maneja las interacciones dentro del módulo de apoyos (soportes).
     *
     * <ul>
     *   <li>Activa o desactiva apoyos A y B.</li>
     *   <li>Permite cambiar el tipo de apoyo (PIN, ROLLER, FIXED) para cada extremo.</li>
     *   <li>Permite cerrar el módulo.</li>
     * </ul>
     */
    private void supportModuleMousePressed() {
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
    }

    /**
     * Actualiza el valor de la longitud de la viga en la zona de simulación ({@link SimuZone}).
     *
     * <p>Este método sincroniza el valor del {@link Slider} asociado al módulo de viga
     * con el atributo {@code beamValue} de {@link SimuZone}, que representa la longitud actual de la viga.
     *
     * <p>Debe llamarse siempre que se modifique la longitud de la viga mediante
     * interacción con el {@link FieldSlider} correspondiente.
     */
    private void updateSimuBeam() {
        simuZone.beamValue = beamFieldSlider.slider.value;
    }

    /**
     * Maneja el evento de arrastre del ratón sobre la pantalla de simulación.
     *
     * <p>Este método se llama desde {@link Main.Phi6Lab#mouseDragged()} cuando la pantalla activa es
     * una instancia de {@code SimulatorScreen}.
     *
     * <p>Se encarga de actualizar dinámicamente los valores asociados a sliders mientras el usuario arrastra:
     * <ul>
     *   <li>En el módulo de viga, actualiza la longitud con {@link #updateSimuBeam()}.</li>
     *   <li>En el módulo de fuerzas, actualiza magnitudes y posiciones con {@link #updateForces()}.</li>
     *   <li>En el módulo de apoyos, permite arrastrar los sliders de posición A y B, y actualiza sus ubicaciones con {@link #updateSupportPosition(String)}.</li>
     * </ul>
     *
     * <p>También se actualizan las etiquetas de identificación de fuerzas y momentos en {@link SimuZone} mediante {@link #updateLabels()}.
     */
    public void mouseDragged() {

        // Actualizar valores al desplazar ratón en módulo de viga
        if (beamModule.opened) updateSimuBeam();

        // Actualizar valores al desplazar ratón en módulo de fuerzas
        if (forceModule.opened) updateForces();

        // Actualizar valores al desplazar ratón en módulo de apoyos
        if (supportModule.opened) {
            supportFieldSliderA.mousePressed();
            supportFieldSliderB.mousePressed();
            supportModule.mousePressed();
            updateSupportPosition("A");
            updateSupportPosition("B");
            updateMoments();
        }

        // Actualizar valores al desplazar ratón en módulo de momentos
        if (forceModule.opened) updateMoments();

        // Actulizar listas de nombres en SimuZone
        updateLabels();
    }

    /**
     * Actualiza los valores de magnitud y posición de todas las fuerzas puntuales
     * en la zona de simulación ({@link SimuZone}) con base en los valores actuales
     * de los {@link FieldSlider} asociados a cada fuerza.
     *
     * <p>Este método recorre todas las fuerzas registradas y sincroniza sus datos
     * con los controles visuales del usuario. Si la lista de fuerzas en {@code simuZone}
     * está vacía, se emite un mensaje de advertencia en consola.
     */
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

    /**
     * Actualiza los valores de magnitud y posición de todos los momentos aplicados
     * en la zona de simulación ({@link SimuZone}) utilizando los valores actuales
     * de los {@link FieldSlider} correspondientes.
     *
     * <p>Este método sincroniza los objetos {@link Elements.Moment} en {@code simuZone}
     * con los controles visuales modificados por el usuario. Si la lista de fuerzas
     * en {@code simuZone} está vacía, se emite un mensaje de advertencia, aunque
     * esto parece ser un error lógico ya que debería validarse la lista de momentos.
     */
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

    /**
     * Maneja la liberación del botón del ratón sobre los diferentes módulos interactivos de la simulación.
     *
     * <p>Este método se llama desde {@link Main.Phi6Lab#mouseReleased()} únicamente cuando la pantalla activa
     * en la GUI es una instancia de {@link SimulatorScreen}.
     *
     * <p>Al liberar el ratón:
     * <ul>
     *   <li>Finaliza cualquier acción de arrastre en sliders o campos de texto.</li>
     *   <li>Actualiza los valores internos de la viga, fuerzas, momentos y apoyos.</li>
     *   <li>Recalcula los límites máximos de los sliders si ha cambiado la longitud de la viga.</li>
     *   <li>Actualiza las posiciones de las etiquetas mostradas en la zona de simulación ({@link SimuZone}).</li>
     * </ul>
     *
     * <p>Cada módulo es procesado de manera independiente si se encuentra activo (`opened == true`).
     * Este enfoque modular permite que el usuario interactúe con un único panel a la vez sin conflictos.
     */
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
     * Controla la interacción con el teclado dentro de la pantalla de simulación.
     *
     * <p>Este método se ejecuta automáticamente desde el método `keyPressed()` de la clase principal
     * {@link Main.Phi6Lab}, cuando la pantalla activa en {@link Main.GUI} es una instancia de {@code SimulatorScreen}.
     *
     * <p>Permite al usuario modificar mediante el teclado:
     * <ul>
     *   <li>El título de la simulación si el puntero está sobre el campo correspondiente.</li>
     *   <li>El tamaño de la viga.</li>
     *   <li>Los valores y posiciones de las fuerzas y momentos.</li>
     *   <li>Las posiciones de los apoyos A y B.</li>
     * </ul>
     *
     * <p>Cada uno de los {@link FieldSlider} y {@link TextField} que detecta estar seleccionado
     * interpreta las teclas presionadas (letras, números o retroceso).
     * Si el texto ingresado es válido, actualiza automáticamente el valor del {@link Slider} asociado.
     *
     * @param key     carácter presionado (por ejemplo 'a', '1', ' ')
     * @param keyCode código de tecla presionada (por ejemplo BACKSPACE o código ASCII)
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
     * Inicializa los componentes gráficos de la barra superior de la pantalla de simulación.
     *
     * <p>Esta barra contiene los controles generales de la simulación, incluyendo:
     * <ul>
     *   <li>Un {@link TextField} para editar el nombre del proyecto.</li>
     *   <li>Un botón para calcular las reacciones estáticas de la viga mediante {@link BeamReactionCalculator}.</li>
     *   <li>Un botón para guardar el proyecto .</li>
     *   <li>Un botón para limpiar el proyecto .</li>
     * </ul>
     *
     * <p>Los componentes son posicionados de forma relativa al layout definido en {@code Constants.Layout}.
     * Este método es invocado una única vez durante la construcción de {@code SimulatorScreen}.
     */
    private void initializeTopBar() {
        // Simulator Title
        simuTitleField = new TextField(p5, frame + margin, margin, 2 * hRect - frame - 2 * margin, frame - 2 * margin);
        simuTitleField.setEmptyText("Nuevo proyecto...");

        // Calculate Button
        float buttonW = hRect * 2 / 3;
        calculateButton = new Button(p5, screenH - buttonW - 2 * margin, margin, buttonW, frame - 2 * margin);
        calculateButton.strokeColorOn = FinalColors.accentSkyBlue();
        calculateButton.setText("Calcular reacciones");

        // Save Button
        buttonW = hRect / 2;
        saveButton = new Button(p5, calculateButton.x - buttonW - 2 * margin, margin, buttonW, frame - 2 * margin);
        saveButton.setText("Guardar");

        // Clear Button
        clearButton = new Button(p5, calculateButton.x - buttonW - buttonW - 4 * margin, margin, buttonW, frame - 2 * margin);
        clearButton.setText("Limpiar");
    }

    /**
     * Inicializa los botones de acceso rápido a módulos en la barra lateral izquierda.
     *
     * <p>Se crean cuatro instancias de {@link ButtonIcon}, cada una con un ícono representativo,
     * que permiten al usuario abrir uno de los siguientes módulos:
     * <ul>
     *   <li>Viga ("beam")</li>
     *   <li>Apoyos ("support")</li>
     *   <li>Fuerzas ("load")</li>
     *   <li>Momentos ("momentum")</li>
     * </ul>
     *
     * <p>Los botones se distribuyen verticalmente con espaciado uniforme y se colocan en el margen izquierdo
     * de la interfaz. Las imágenes SVG correspondientes se cargan desde la carpeta <code>data/icons/</code>.
     *
     * <p>Este método es llamado una sola vez desde el constructor de {@code SimulatorScreen}.
     */
    private void initializeLeftButtons() {
        leftButtons = new ButtonIcon[4];
        String[] iconPaths = {"beam", "support", "load", "momentum"};
        for (int i = 0; i < leftButtons.length; i++) {
            leftButtons[i] = new ButtonIcon(p5, margin, (frame + margin) + frame * i, frame - 2 * margin, frame - 2 * margin);
            leftButtons[i].setIcon("data/icons/" + iconPaths[i] + ".svg");
        }
    }

    /**
     * Inicializa el módulo de propiedades de la viga en la interfaz de simulación.
     *
     * <p>Este módulo permite al usuario definir la longitud de la viga mediante un control interactivo compuesto por:
     * <ul>
     *   <li>Un {@link TextField} donde se puede escribir directamente el valor de longitud.</li>
     *   <li>Un {@link Slider} que permite ajustar dicho valor visualmente.</li>
     *   <li>Un {@link FieldSlider} que sincroniza ambos elementos.</li>
     * </ul>
     *
     * <p>El módulo se posiciona en la parte superior izquierda de la pantalla y su tamaño es fijo.
     * Se le asigna el título "Propiedades viga" y se configura con valores iniciales seguros.
     * Este módulo se utiliza como referencia para limitar la posición de otros elementos como fuerzas, momentos y apoyos.
     *
     * <p>Este método es invocado una única vez desde el constructor de {@code SimulatorScreen}.
     */
    private void initializeBeamModule() {
        beamModule = new Module(p5, frame + margin, frame + margin, moduleWidth, 150);
        beamModule.setTitle("Propiedades viga");

        float beamSizeFieldWidth = beamModule.width / 5 - 4 * margin;

        beamSizeField = new TextField(p5, beamModule.x + 2 * margin, beamModule.y + margin + frame, beamSizeFieldWidth, frame * 0.75f);
        beamSizeField.setBorderColor(FinalColors.primaryYellow());
        beamSizeField.borderEnabled = true;

        beamSizeSlider = new Slider(p5, beamModule.x + beamSizeFieldWidth + vMargin, beamModule.y + margin + frame, 4 * beamModule.width / 5 - 3 * margin, frame * 0.75f, 0, 100, 50);
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
     * Inicializa todos los componentes visuales y lógicos del módulo de fuerzas puntuales.
     *
     * <p>Este módulo permite al usuario añadir, configurar y eliminar fuerzas aplicadas sobre la viga.
     * Cada fuerza consta de dos valores configurables:
     * <ul>
     *   <li><b>Magnitud:</b> valor de la fuerza representado con un {@link FieldSlider} compuesto por un {@link TextField} y un {@link Slider}.</li>
     *   <li><b>Ubicación:</b> posición de aplicación de la fuerza a lo largo de la viga, también controlada mediante un {@link FieldSlider}.</li>
     * </ul>
     *
     * <p>La función realiza las siguientes tareas:
     * <ol>
     *   <li>Instancia el módulo contenedor de fuerzas con dimensiones y título apropiado.</li>
     *   <li>Crea el botón "Añadir fuerza" y lo configura visualmente.</li>
     *   <li>Inicializa los sliders y campos de texto correspondientes a la magnitud y ubicación de la primera fuerza.</li>
     *   <li>Agrega todos los componentes al módulo y los organiza en listas para gestión posterior.</li>
     *   <li>Genera un botón inicial para eliminar la primera fuerza, el cual también se registra en su lista correspondiente.</li>
     * </ol>
     *
     * <p>Este método debe ser llamado una sola vez en el constructor de {@code SimulatorScreen},
     * antes de que se empiece a interactuar con el módulo.
     *
     * @see FieldSlider
     * @see TextField
     * @see Slider
     * @see Button
     * @see Module
     */
    private void initializePointForceModule() {
        //Se podría sintetizar usando los métodos de clonación posteriormente añadidos.

        //Instanciación del módulo
        forceModule = new Module(p5, beamModule.x, beamModule.y, beamModule.width, 600);
        forceModule.setTitle("Fuerzas puntuales");

        float inputZoneWidth = (forceModule.width - 3 * margin - forceNameBoxWidth) / 2;

        //Botón de añadir fuerza puntual
        addForceButton = new Button(p5, forceModule.x + 2 * margin, forceModule.y + margin + frame, forceModule.width - 4 * margin, frame * 0.75f);
        addForceButton.setText("Añadir fuerza");
        addForceButton.strokeColorOn = FinalColors.primaryYellow();
        addForceButton.strokeColorOff = FinalColors.primaryYellow();
        addForceButton.strokeWeight = 1;


        //Instanciación del TextField inicial del valor de la fuerza
        initialValueForceField = new TextField(p5, forceModule.x + 4 * margin + forceNameBoxWidth, addForceButton.y + addForceButton.height + frame * 0.75f + 4 * margin, inputZoneWidth / 3 - margin, frame * 0.75f);
        initialValueForceField.setBorderColor(FinalColors.primaryYellow());
        initialValueForceField.borderEnabled = true;

        //Instanciación del Slider inicial del valor de la fuerza
        initialValueForceSlider = new Slider(p5, initialValueForceField.x + initialValueForceField.width + 2 * margin, initialValueForceField.y, inputZoneWidth * 2 / 3 - 4 * margin, frame * 0.75f, 0, maxForceMagnitude, 50);
        initialValueForceSlider.value = 10;

        //Añadir a la lista de FieldSliders
        forceValueFieldSliders = new ArrayList<>();
        forceValueFieldSliders.add(new FieldSlider(p5, initialValueForceField, initialValueForceSlider));
        forceModule.components.add(initialValueForceSlider);

        //Instanciación del TextField de la ubicación de la fuerza
        initialUbiForceField = new TextField(p5, initialValueForceSlider.x + initialValueForceSlider.width + 3 * margin, addForceButton.y + addForceButton.height + frame * 0.75f + 4 * margin, inputZoneWidth / 3 - margin, frame * 0.75f);
        initialUbiForceField.setBorderColor(FinalColors.primaryYellow());
        initialUbiForceField.borderEnabled = true;


        //Instanciación del Slider de la ubicación de la fuerza
        initialUbiForceSlider = new Slider(p5, initialUbiForceField.x + initialUbiForceField.width + 2 * margin, initialValueForceField.y, inputZoneWidth * 2 / 3 - 4 * margin, frame * 0.75f, 0, 100, 50);
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
        initialDeleteForceButton = new Button(p5, forceModule.x + 2 * margin, initialValueForceField.y, frame * 0.75f, frame * 0.75f);
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
        addMomentButton = new Button(p5, momentModule.x + 2 * margin, momentModule.y + margin + frame, momentModule.width - 4 * margin, frame * 0.75f);
        addMomentButton.setText("Añadir momento");
        addMomentButton.strokeColorOn = FinalColors.primaryYellow();
        addMomentButton.strokeColorOff = FinalColors.primaryYellow();
        addMomentButton.strokeWeight = 1;


        //Instanciación del TextField inicial del valor del momento
        initialValueMomentField = new TextField(p5, momentModule.x + 4 * margin + momentNameBoxWidth, addMomentButton.y + addMomentButton.height + frame * 0.75f + 4 * margin, inputZoneWidth / 3 - margin, frame * 0.75f);
        initialValueMomentField.setBorderColor(FinalColors.primaryYellow());
        initialValueMomentField.borderEnabled = true;

        //Instanciación del Slider inicial del valor del momento
        initialValueMomentSlider = new Slider(p5, initialValueMomentField.x + initialValueMomentField.width + 2 * margin, initialValueMomentField.y, inputZoneWidth * 2 / 3 - 4 * margin, frame * 0.75f, 0, maxMomentMagnitude, 50);
        momentModule.components.add(initialValueMomentSlider);

        //Añadir a la lista de FieldSliders
        momentValueFieldSliders = new ArrayList<>();
        momentValueFieldSliders.add(new FieldSlider(p5, initialValueMomentField, initialValueMomentSlider));

        //Instanciación del TextField de la ubicación del momento
        initialUbiMomentField = new TextField(p5, initialValueMomentSlider.x + initialValueMomentSlider.width + 3 * margin, addMomentButton.y + addMomentButton.height + frame * 0.75f + 4 * margin, inputZoneWidth / 3 - margin, frame * 0.75f);
        initialUbiMomentField.setBorderColor(FinalColors.primaryYellow());
        initialUbiMomentField.borderEnabled = true;

        //Instanciación del Slider de la ubicación del momento
        initialUbiMomentSlider = new Slider(p5, initialUbiMomentField.x + initialUbiMomentField.width + 2 * margin, initialValueMomentField.y, inputZoneWidth * 2 / 3 - 4 * margin, frame * 0.75f, 0, 100, 50);

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
        initialDeleteMomentButton = new Button(p5, momentModule.x + 2 * margin, initialValueMomentField.y, frame * 0.75f, frame * 0.75f);
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
     * Añade una nueva fuerza puntual a la simulación.
     *
     * <p>Este método incrementa el contador de fuerzas, crea un nuevo par de {@link FieldSlider} para
     * configurar la magnitud y ubicación de la nueva fuerza, y la incorpora al motor de simulación
     * representado por {@link SimuZone}.
     *
     * <p>El flujo de acciones incluye:
     * <ul>
     *   <li>Incrementar {@code numForces} para reflejar la nueva cantidad de fuerzas.</li>
     *   <li>Crear visualmente los sliders y campos de texto asociados mediante {@link #newForceFieldSlider()}.</li>
     *   <li>Actualizar los valores máximos de los sliders de ubicación con {@link #setAllUbiSlidersMaxTo(float)}
     *       en función del tamaño actual de la viga.</li>
     *   <li>Añadir la nueva fuerza a {@code simuZone.forces} con su magnitud y posición inicial leída del nuevo slider.</li>
     *   <li>Verificar la integridad visual y lógica de los componentes con {@link #checkForceIntegrity()}.</li>
     * </ul>
     *
     * <p>Este método es invocado cada vez que el usuario presiona el botón "Añadir fuerza".
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

    /**
     * Crea y posiciona un nuevo conjunto de controles para una fuerza puntual adicional.
     *
     * <p>Este método se encarga de generar visualmente un nuevo par de {@link FieldSlider} que permiten:
     * <ul>
     *   <li>Configurar la magnitud (valor) de la fuerza.</li>
     *   <li>Definir su ubicación (posición) a lo largo de la viga.</li>
     * </ul>
     *
     * <p>También crea un botón de eliminación asociado a esta fuerza y actualiza las listas
     * internas que mantienen sincronizados los controles visuales, los datos lógicos y la interfaz gráfica.
     *
     * <p>Los nuevos elementos se posicionan en vertical debajo del conjunto inicial,
     * respetando un espaciado constante determinado por la altura del primer campo más márgenes.
     *
     * <p>Este método es invocado por {@link #addPointForce()} cada vez que el usuario añade una nueva fuerza.
     */
    private void newForceFieldSlider() {
        int index = forceValueFieldSliders.size();

        float y = initialValueForceField.y + (initialValueForceField.height + 2 * margin) * index;

        // === FieldSlider para magnitud ===
        TextField newForceValueField = new TextField(p5, initialValueForceField.x, y, initialValueForceField.width, initialValueForceField.height);
        newForceValueField.setBorderColor(FinalColors.primaryYellow());
        newForceValueField.borderEnabled = true;

        Slider newForceValueSlider = new Slider(p5, initialValueForceSlider.x, y, initialValueForceSlider.width, initialValueForceSlider.height, 0, 100, 50);
        newForceValueSlider.setValueAt(5); // Valor por defecto

        FieldSlider newForceValueFieldSlider = new FieldSlider(p5, newForceValueField, newForceValueSlider);
        newForceValueFieldSlider.setValue(5); // sincroniza ambos

        forceValueFieldSliders.add(newForceValueFieldSlider);
        forceModule.components.add(newForceValueSlider);
        forceModule.components.add(newForceValueField);

        // === FieldSlider para ubicación ===
        TextField newForceUbiField = new TextField(p5, initialUbiForceField.x, y, initialUbiForceField.width, initialUbiForceField.height);
        newForceUbiField.setBorderColor(FinalColors.primaryYellow());
        newForceUbiField.borderEnabled = true;

        Slider newForceUbiSlider = new Slider(p5, initialUbiForceSlider.x, y, initialUbiForceSlider.width, initialUbiForceSlider.height, 0, beamFieldSlider.slider.value, beamFieldSlider.slider.value / 2);
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

    /**
     * Elimina una fuerza puntual de la simulación y de la interfaz gráfica.
     *
     * <p>Este método realiza las siguientes acciones:
     * <ul>
     *   <li>Decrementa el contador {@code numForces}.</li>
     *   <li>Elimina los {@link FieldSlider} de magnitud y posición, y el botón de eliminación
     *       de sus respectivas listas lógicas.</li>
     *   <li>Elimina la fuerza del motor de simulación ({@link SimuZone}).</li>
     *   <li>Remueve todos los componentes visuales asociados del {@link Module} de fuerzas.</li>
     *   <li>Actualiza la lista de etiquetas con {@link #updateLabels()}.</li>
     *   <li>Verifica la integridad del módulo con {@link #checkForceIntegrity()}.</li>
     *   <li>Reorganiza los elementos restantes visualmente mediante {@link #repositionForceComponents()}.</li>
     * </ul>
     *
     * @param forceIndex índice de la fuerza a eliminar.
     */
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

    /**
     * Reposiciona visualmente todos los componentes del módulo de fuerzas puntuales.
     *
     * <p>Recalcula y actualiza la coordenada vertical {@code y} de cada:
     * <ul>
     *   <li>{@link FieldSlider} de magnitud (valor).</li>
     * <li>{@link FieldSlider} de ubicación (posición).</li>
     *   <li>Botón de eliminación asociado.</li>
     * </ul>
     * en función de su orden en la lista, manteniendo un espaciado uniforme respecto al campo inicial.
     *
     * <p>Este método se utiliza tras eliminar una fuerza para reorganizar correctamente los elementos restantes.
     */
    private void repositionForceComponents() {
        for (int i = 0; i < forceValueFieldSliders.size(); i++) {
            float y = initialValueForceField.y + (initialValueForceField.height + 2 * margin) * i;

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

    /**
     * Añade un nuevo momento puntual a la simulación.
     *
     * <p>Este método incrementa el contador de momentos, crea un nuevo par de {@link FieldSlider}
     * para configurar su magnitud y ubicación, y lo incorpora a la zona de simulación {@link SimuZone}.
     *
     * <p>El flujo de ejecución incluye:
     * <ul>
     *   <li>Actualizar {@code numMoments} para reflejar el nuevo total.</li>
     *   <li>Crear y posicionar visualmente los controles con {@link #newMomentFieldSlider()}.</li>
     *   <li>Ajustar los límites máximos de los sliders de ubicación según la longitud actual de la viga.</li>
     *   <li>Añadir el nuevo momento a {@code simuZone.moments} con su configuración inicial.</li>
     *   <li>Verificar que todos los componentes visuales del módulo estén sincronizados correctamente con {@link #checkMomentIntegrity()}.</li>
     * </ul>
     *
     * <p>Este método se ejecuta al presionar el botón "Añadir momento" en la interfaz.
     */
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

    /**
     * Crea y posiciona un nuevo conjunto de controles visuales para un momento puntual adicional.
     *
     * <p>Este método genera:
     * <ul>
     *   <li>Un {@link FieldSlider} para la magnitud del momento (valor).</li>
     *   <li>Un {@link FieldSlider} para la ubicación del momento a lo largo de la viga.</li>
     *   <li>Un botón para eliminar el momento correspondiente.</li>
     * </ul>
     *
     * <p>Los nuevos controles se colocan de forma vertical, uno debajo del otro,
     * respetando el espaciado definido por los campos iniciales del módulo.
     * Además, los elementos se agregan a las listas correspondientes y al módulo visual para mantener
     * la sincronización entre la lógica, la interfaz gráfica y el motor de simulación.
     *
     * <p>Este método es invocado automáticamente por {@link #addMoment()} cada vez que se añade un nuevo momento.
     */
    private void newMomentFieldSlider() {
        int numMomentFieldSliders = momentValueFieldSliders.size();

        float y = initialValueMomentField.y + (initialValueMomentField.height + 2 * margin) * numMomentFieldSliders;

        // Instanciación de los FieldSlider del valor del momento
        TextField newMomentValueField = new TextField(p5, initialValueMomentField.x, y, initialValueMomentField.width, initialValueMomentField.height);
        newMomentValueField.setBorderColor(FinalColors.primaryYellow());
        newMomentValueField.borderEnabled = true;

        Slider newMomentValueSlider = new Slider(p5, initialValueMomentSlider.x, y, initialValueMomentSlider.width, initialValueMomentSlider.height, 0, 100, 50);

        FieldSlider newMomentValueFieldSlider = new FieldSlider(p5, newMomentValueField, newMomentValueSlider);
        momentValueFieldSliders.add(newMomentValueFieldSlider);
        momentModule.components.add(newMomentValueSlider);
        momentModule.components.add(newMomentValueField);

        // Instanciación de los FieldSlider de la ubicación del momento
        TextField newMomentUbiField = new TextField(p5, initialUbiMomentField.x, y, initialUbiMomentField.width, initialUbiMomentField.height);
        newMomentUbiField.setBorderColor(FinalColors.primaryYellow());
        newMomentUbiField.borderEnabled = true;

        Slider newMomentUbiSlider = new Slider(p5, initialUbiMomentSlider.x, y, initialUbiMomentSlider.width, initialUbiMomentSlider.height, 0, 100, 50);

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

    /**
     * Verifica la integridad lógica y visual del módulo de momentos.
     *
     * <p>Este método compara el tamaño de las listas internas que gestionan los {@link FieldSlider} de magnitud,
     * los {@link FieldSlider} de ubicación, los botones de eliminación y la lista de momentos en {@link SimuZone}.
     * Si hay discrepancias en los conteos o si algún componente visual no está registrado correctamente
     * en el {@link Module} correspondiente, se imprime un mensaje de advertencia en consola.
     *
     * <p>Este chequeo ayuda a garantizar que todas las estructuras relacionadas con los momentos estén
     * sincronizadas y que no haya errores de interfaz después de operaciones como añadir o eliminar momentos.
     */
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

    /**
     * Elimina un momento puntual de la simulación y su interfaz gráfica.
     *
     * <p>A partir del índice especificado:
     * <ul>
     *   <li>Elimina el {@link FieldSlider} de magnitud, el de ubicación y el botón de eliminación de las listas internas.</li>
     *   <li>Remueve el objeto {@link Elements.Moment} correspondiente de {@link SimuZone}.</li>
     *   <li>Elimina todos los componentes visuales del {@link Module} de momentos.</li>
     *   <li>Reorganiza visualmente los elementos restantes mediante {@link #repositionMomentComponents()}.</li>
     *   <li>Verifica la integridad del módulo con {@link #checkMomentIntegrity()}.</li>
     * </ul>
     *
     * <p>Este método es llamado al presionar el botón de eliminar asociado a un momento específico.
     *
     * @param index índice del momento a eliminar.
     */
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

    /**
     * Reposiciona visualmente todos los componentes del módulo de momentos.
     *
     * <p>Este método recalcula y reasigna la coordenada vertical {@code y} de cada conjunto de componentes:
     * <ul>
     *   <li>{@link FieldSlider} de magnitud</li>
     *   <li>{@link FieldSlider} de ubicación</li>
     *   <li>Botón de eliminación</li>
     * </ul>
     * en función de su índice, utilizando como referencia la posición del campo inicial.
     *
     * <p>Se debe llamar después de eliminar un momento para reorganizar los elementos restantes
     * y mantener una distribución coherente y limpia en la interfaz del {@link Module}.
     */
    private void repositionMomentComponents() {
        for (int i = 0; i < momentValueFieldSliders.size(); i++) {
            float y = initialValueMomentField.y + (initialValueMomentField.height + 2 * margin) * i;

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

    /**
     * Inicializa la zona de simulación gráfica ({@link SimuZone}) y carga los datos iniciales.
     *
     * <p>Configura la instancia de {@link SimuZone} con su posición y dimensiones en pantalla,
     * asigna la longitud actual de la viga, y transfiere todos los datos relevantes para renderizar
     * las fuerzas y momentos definidos por el usuario.
     *
     * <p>En detalle:
     * <ul>
     *   <li>Se establece {@code beamValue} en función del slider de longitud de viga.</li>
     *   <li>Se copian las etiquetas de fuerzas y momentos a {@code forceLabels} y {@code momentLabels}.</li>
     *   <li>Se crean objetos {@link Elements.Force} y {@link Elements.Moment} con sus valores y posiciones actuales,
     *       y se añaden a las listas correspondientes dentro de {@code simuZone}.</li>
     * </ul>
     *
     * <p>Este método debe llamarse al construir la pantalla de simulación para garantizar
     * que todos los elementos visuales estén sincronizados con los datos lógicos.
     */
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

    /**
     * Cierra todos los módulos laterales de la interfaz de simulación.
     *
     * <p>Establece la propiedad {@code opened} de cada {@link Module} (viga, fuerzas, momentos y apoyos)
     * en {@code false}, ocultándolos de la vista del usuario.
     *
     * <p>Este método se utiliza para asegurar que solo un módulo esté visible a la vez,
     * normalmente antes de abrir uno nuevo desde los botones de navegación lateral.
     */
    private void closeAllModules() {
        beamModule.opened = false;
        forceModule.opened = false;
        momentModule.opened = false;
        supportModule.opened = false;
    }

    /**
     * Inicializa el módulo de configuración de apoyos en la interfaz de simulación.
     *
     * <p>Este módulo permite definir la posición y el tipo de dos apoyos (A y B) sobre la viga.
     * Para cada apoyo se crean:
     * <ul>
     *   <li>Un botón para activarlo o desactivarlo.</li>
     *   <li>Un {@link FieldSlider} que incluye un {@link TextField} y un {@link Slider} para configurar su posición.</li>
     *   <li>Tres botones adicionales para seleccionar su tipo: PIN, ROLLER o FIXED.</li>
     * </ul>
     *
     * <p>Los controles de A y B se posicionan uno debajo del otro con espaciado vertical.
     * Los botones para seleccionar el tipo de apoyo de B se clonan y reposicionan desde los de A.
     *
     * <p>Este método debe llamarse una sola vez durante la construcción de {@code SimulatorScreen}.
     */
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
        supportFieldA = new TextField(p5, deleteButtonA.x + deleteButtonA.width + 2 * margin, supportModule.y + margin + frame, supportFieldWidth, frame * 0.75f);
        supportFieldA.setBorderColor(FinalColors.primaryYellow());
        supportFieldA.borderEnabled = true;
        supportFieldA.setEmptyText("Posición");

        // Crear Slider del apoyo A
        supportSliderA = new Slider(p5, supportFieldA.x + supportFieldA.width + 2 * margin, supportModule.y + margin + frame, supportModule.width - deleteButtonA.width - supportFieldA.width - 8 * margin - 3 * deleteButtonA.width - 6 * margin, frame * 0.75f, 0, 100, 50);
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
        Button[] supButtonsB = {pinButtonB = pinButtonA.clone(), rollerButtonB = rollerButtonA.clone(), fixedButtonB = fixedButtonA.clone()};

        for (Button b : supButtonsB) {
            b.y = supportSliderB.y;
            supportModule.components.add(b);
        }

    }

    /**
     * Establece un nuevo valor máximo para todos los sliders de ubicación
     * de fuerzas, momentos y apoyos en función del tamaño actual de la viga.
     *
     * <p>Esto asegura que ningún elemento interactivo pueda colocarse fuera del dominio de la viga.
     * Además, actualiza visualmente la posición de los sliders y campos de texto asociados.
     *
     * @param max nuevo valor máximo permitido para los sliders de ubicación.
     */
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

    /**
     * Actualiza el valor máximo permitido en todos los sliders de magnitud de fuerzas.
     *
     * <p>Este método aplica el valor de {@code maxForceMagnitude} como nuevo límite superior
     * para cada slider asociado a una fuerza puntual.
     */
    private void updateForceSliderRanges() {
        for (FieldSlider fs : forceValueFieldSliders) {
            fs.slider.maxValue = maxForceMagnitude;
        }
    }

    /**
     * Actualiza el valor máximo permitido en todos los sliders de magnitud de momentos.
     *
     * <p>Este método aplica el valor de {@code maxMomentMagnitude} como nuevo límite superior
     * para cada slider asociado a un momento puntual.
     */
    private void updateMomentSliderRanges() {
        for (FieldSlider fs : momentValueFieldSliders) {
            fs.slider.maxValue = maxMomentMagnitude;
        }
    }

    /**
     * Actualiza las etiquetas (nombres) visibles de fuerzas y momentos en la zona de simulación.
     *
     * <p>Sincroniza {@code forceLabels} y {@code momentLabels} de {@link SimuZone} con las listas internas
     * {@code forceNames} y {@code momentNames}, utilizadas para etiquetar cada carga gráficamente.
     */
    private void updateLabels() {
        simuZone.forceLabels = new ArrayList<>(forceNames);
        simuZone.momentLabels = new ArrayList<>(momentNames);
    }

    /**
     * Activa o desactiva el apoyo A en la viga.
     *
     * <p>Si el apoyo está desactivado, se añade a {@link SimuZone} con su tipo y posición actual;
     * si ya está activo, se elimina de la lista de apoyos. También actualiza el texto del botón
     * de activación correspondiente.
     */
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

    /**
     * Activa o desactiva el apoyo B en la viga.
     *
     * <p>Funciona de forma análoga a {@link #toggleSupportA()}: si el apoyo B no está activo, se añade
     * a {@link SimuZone}; si ya está presente, se elimina. Se actualiza también el texto del botón.
     */
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

    /**
     * Actualiza el tipo de apoyo A y su estilo visual en la interfaz.
     *
     * <p>Resalta el botón correspondiente al tipo seleccionado (PIN, ROLLER o FIXED)
     * y actualiza el tipo del objeto {@link Elements.Support} con id "A" dentro de {@link SimuZone}.
     *
     * @param type el nuevo tipo de apoyo a aplicar.
     */
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

    /**
     * Actualiza el tipo de apoyo B y su estilo visual en la interfaz.
     *
     * <p>Resalta el botón correspondiente al tipo seleccionado (PIN, ROLLER o FIXED)
     * y actualiza el tipo del objeto {@link Elements.Support} con id "B" dentro de {@link SimuZone}.
     *
     * @param type el nuevo tipo de apoyo a aplicar.
     */

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

    /**
     * Imprime en consola un resumen del estado actual de los apoyos en {@link SimuZone}.
     *
     * <p>Incluye información sobre id, posición y tipo de cada apoyo presente.
     * Se usa principalmente con fines de depuración.
     */
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

    /**
     * Actualiza la posición del apoyo A o B en {@link SimuZone} según el valor actual
     * del {@link Slider} correspondiente.
     *
     * @param id el identificador del apoyo ("A" o "B").
     */
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

    /**
     * Calcula las reacciones estáticas de la viga con base en los elementos actuales.
     *
     * <p>Verifica que haya al menos una fuerza y un apoyo para proceder.
     * Llama a {@link BeamReactionCalculator#calculateReactions} y guarda los resultados.
     * También imprime los resultados en consola.
     */
    private void updateResults() {
        String results = "";
        if (simuZone.forces.isEmpty() || simuZone.supports.isEmpty()) {
            System.out.println("No se puede calcular nada. Añada algún elemento");
        } else {
            results = Arrays.toString(calculateReactions(simuZone.forces, simuZone.moments, simuZone.supports));
        }
        System.out.println(results);
        this.results = calculateReactions(simuZone.forces, simuZone.moments, simuZone.supports);
    }

    /**
     * Dibuja el panel de resultados con las reacciones calculadas.
     *
     * <p>Muestra las componentes de reacción en los extremos A y B (RAx, RAy, MA, RBx, RBy, MB)
     * dentro de un área sombreada de la interfaz gráfica.
     */
    private void displayResultsPanel() {

        // Recuadro
        p5.fill(FinalColors.bgGrey());
        float panelX = 2 * hRect + 2 * margin;
        float panelY = 4 * vRect + 2 * margin;
        float panelW = 3 * hRect - 4 * margin;
        float panelH = 2 * vRect - 4 * margin;

        p5.rect(panelX, panelY, panelW, panelH);

        // Matriz de textos
        p5.textSize(Sizes.widgetHeading);
        p5.fill(FinalColors.textWhite());
        p5.textAlign(PConstants.CENTER);
        p5.text("Resultados", panelX + panelW / 2, panelY + frame / 2);


        String[] label1 = {"RAx", "RAy", "MA"};

        String[] values1 = new String[3];
        String[] values2 = new String[3];

        for (int i = 0; i < results.length; i++) {
            if (i < 3) values1[i] = "" + results[i];
            else values2[i - 3] = "" + results[i - 3];
        }

        for (int i = 0; i < 3; i++) {
            String string = label1[i] + ": " + values1[i];
            p5.text(string, panelX + 3 * frame, panelY + 2 * frame + frame * i);
        }

        String[] label2 = {"RBx", "RBy", "MB"};

        for (int i = 0; i < 3; i++) {
            String string = label2[i] + ": " + values2[i];
            p5.text(label2[i], panelX + frame / 2 + panelW / 2, panelY + 2 * frame + frame * i);
        }

    }

    /**
     * Verifica que todas las estructuras visuales y lógicas relacionadas con las fuerzas
     * estén sincronizadas.
     *
     * <p>Comprueba que la cantidad de sliders de magnitud, sliders de posición,
     * botones de eliminación y fuerzas en {@link SimuZone} coincidan.
     * También revisa que cada componente visual esté correctamente registrado en el módulo.
     *
     * <p>Imprime advertencias detalladas en consola si detecta alguna inconsistencia.
     */
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

    // === Sincronización con la base de datos

    public void clearEverything() {
        // Limpiar datos
        forceValueFieldSliders.clear();
        forceUbiFieldSliders.clear();
        forceDeleteButtons.clear();
        forceNames.clear();
        momentValueFieldSliders.clear();
        momentUbiFieldSliders.clear();
        momentDeleteButtons.clear();
        momentNames.clear();

        // Reiniciar contadores
        numForces = 0;
        numMoments = 0;

        // Limpiar módulos visuales
        forceModule.components.clear();
        momentModule.components.clear();
        supportModule.components.clear();

        // Reinicializar sliders de soporte
        initializeSupportModule();

        // Reiniciar zona de simulación
        simuZone.forces.clear();
        simuZone.moments.clear();
        simuZone.supports.clear();

        // Reinicializar sliders máximos
        setAllUbiSlidersMaxTo(beamSizeSlider.value);

        // === Añadir fuerza inicial de nuevo ===
        initializeCreateForce();

        // === Añadir momento inicial de nuevo ===
        initializeCreateMoment();

        // Cerrar todos los módulos
        closeAllModules();
    }

    private void initializeCreateForce() {
        numForces = 1;
        forceNames.add("1");

        float inputZoneWidth = (forceModule.width - 3 * margin - forceNameBoxWidth) / 2;

        // === Botón de añadir fuerza ===
        addForceButton = new Button(p5, forceModule.x + 2 * margin, forceModule.y + margin + frame, forceModule.width - 4 * margin, frame * 0.75f);
        addForceButton.setText("Añadir fuerza");
        addForceButton.strokeColorOn = FinalColors.primaryYellow();
        addForceButton.strokeColorOff = FinalColors.primaryYellow();
        addForceButton.strokeWeight = 1;
        forceModule.components.add(addForceButton);

        // === Valor de la fuerza ===
        TextField valueField = new TextField(p5, forceModule.x + 4 * margin + forceNameBoxWidth, addForceButton.y + addForceButton.height + frame * 0.75f + 4 * margin, inputZoneWidth / 3 - margin, frame * 0.75f);
        valueField.setBorderColor(FinalColors.primaryYellow());
        valueField.borderEnabled = true;

        Slider valueSlider = new Slider(p5, valueField.x + valueField.width + 2 * margin, valueField.y, inputZoneWidth * 2 / 3 - 4 * margin, frame * 0.75f, 0, maxForceMagnitude, 10);
        FieldSlider valFS = new FieldSlider(p5, valueField, valueSlider);
        valFS.setValue(10);

        forceValueFieldSliders.add(valFS);
        forceModule.components.add(valueField);
        forceModule.components.add(valueSlider);

        // === Ubicación de la fuerza ===
        TextField posField = new TextField(p5, valueSlider.x + valueSlider.width + 3 * margin, valueField.y, inputZoneWidth / 3 - margin, frame * 0.75f);
        posField.setBorderColor(FinalColors.primaryYellow());
        posField.borderEnabled = true;

        Slider posSlider = new Slider(p5, posField.x + posField.width + 2 * margin, posField.y, inputZoneWidth * 2 / 3 - 4 * margin, frame * 0.75f, 0, beamFieldSlider.slider.value, beamFieldSlider.slider.value / 2);
        FieldSlider posFS = new FieldSlider(p5, posField, posSlider);
        posFS.setValue(beamFieldSlider.slider.value / 2);

        forceUbiFieldSliders.add(posFS);
        forceModule.components.add(posField);
        forceModule.components.add(posSlider);

        // === Botón de eliminar ===
        Button delBtn = new Button(p5, forceModule.x + 2 * margin, valueField.y, frame * 0.75f, frame * 0.75f);
        delBtn.setText("1");
        delBtn.strokeColorOn = FinalColors.primaryYellow();
        delBtn.strokeColorOff = FinalColors.primaryYellow();
        delBtn.strokeWeight = 1;
        forceDeleteButtons.add(delBtn);
        forceModule.components.add(delBtn);

        // === SimuZone ===
        simuZone.forces.add(new Elements.Force(10, beamFieldSlider.slider.value / 2, false));
    }

    private void initializeCreateMoment() {
        numMoments = 1;
        momentNames.add("1");

        float inputZoneWidth = (momentModule.width - 3 * margin - momentNameBoxWidth) / 2;

        // === Botón de añadir momento ===
        addMomentButton = new Button(p5, momentModule.x + 2 * margin, momentModule.y + margin + frame, momentModule.width - 4 * margin, frame * 0.75f);
        addMomentButton.setText("Añadir momento");
        addMomentButton.strokeColorOn = FinalColors.primaryYellow();
        addMomentButton.strokeColorOff = FinalColors.primaryYellow();
        addMomentButton.strokeWeight = 1;
        momentModule.components.add(addMomentButton);

        // === Valor del momento ===
        TextField valueField = new TextField(p5, momentModule.x + 4 * margin + momentNameBoxWidth, addMomentButton.y + addMomentButton.height + frame * 0.75f + 4 * margin, inputZoneWidth / 3 - margin, frame * 0.75f);
        valueField.setBorderColor(FinalColors.primaryYellow());
        valueField.borderEnabled = true;

        Slider valueSlider = new Slider(p5, valueField.x + valueField.width + 2 * margin, valueField.y, inputZoneWidth * 2 / 3 - 4 * margin, frame * 0.75f, 0, maxMomentMagnitude, 10);
        FieldSlider valFS = new FieldSlider(p5, valueField, valueSlider);
        valFS.setValue(10);

        momentValueFieldSliders.add(valFS);
        momentModule.components.add(valueField);
        momentModule.components.add(valueSlider);

        // === Ubicación del momento ===
        TextField posField = new TextField(p5, valueSlider.x + valueSlider.width + 3 * margin, valueField.y, inputZoneWidth / 3 - margin, frame * 0.75f);
        posField.setBorderColor(FinalColors.primaryYellow());
        posField.borderEnabled = true;

        Slider posSlider = new Slider(p5, posField.x + posField.width + 2 * margin, posField.y, inputZoneWidth * 2 / 3 - 4 * margin, frame * 0.75f, 0, beamFieldSlider.slider.value, beamFieldSlider.slider.value / 2);
        FieldSlider posFS = new FieldSlider(p5, posField, posSlider);
        posFS.setValue(beamFieldSlider.slider.value / 2);

        momentUbiFieldSliders.add(posFS);
        momentModule.components.add(posField);
        momentModule.components.add(posSlider);

        // === Botón eliminar momento ===
        Button delBtn = new Button(p5, momentModule.x + 2 * margin, valueField.y, frame * 0.75f, frame * 0.75f);
        delBtn.setText("1");
        delBtn.strokeColorOn = FinalColors.primaryYellow();
        delBtn.strokeColorOff = FinalColors.primaryYellow();
        delBtn.strokeWeight = 1;
        momentDeleteButtons.add(delBtn);
        momentModule.components.add(delBtn);

        // === SimuZone ===
        simuZone.moments.add(new Elements.Moment(10, beamFieldSlider.slider.value / 2, true));
    }

    public void loadSimFromDB(int simId) {
        try {
            clearEverything();

            // === Cargar longitud de la viga ===
            String qViga = "SELECT v.LONGITUD " + "FROM simulador s " + "JOIN viga v ON s.VIGA_idVIGA = v.idVIGA " + "WHERE s.idSIMULADOR = " + simId;

            ResultSet rsViga = Main.Phi6Lab.db.query.executeQuery(qViga);
            if (rsViga.next()) {
                float longitud = rsViga.getFloat("LONGITUD");
                beamFieldSlider.setValue(longitud);
                simuZone.beamValue = longitud;
                setAllUbiSlidersMaxTo(longitud);
            }

            // === Cargar elementos (fuerzas, momentos, apoyos) ===
            String qElems = "SELECT e.*, t.NOMBRE AS tipoNombre " + "FROM elemento e " + "JOIN tipo t ON e.TIPO_idTIPO = t.idTIPO " + "WHERE e.VIGA_idVIGA = (" + "SELECT VIGA_idVIGA FROM simulador WHERE idSIMULADOR = " + simId + ")";

            ResultSet rs = Main.Phi6Lab.db.query.executeQuery(qElems);

            while (rs.next()) {
                int tipoId = rs.getInt("TIPO_idTIPO");
                float valor = rs.getFloat("VALOR");
                float ubicacion = rs.getFloat("UBICACION");
                String sentido = rs.getString("SENTIDO");
                String tipoNombre = rs.getString("tipoNombre");

                switch (tipoId) {
                    case 1 -> { // === Fuerza puntual ===
                        addPointForce();
                        FieldSlider valFS = forceValueFieldSliders.getLast();
                        FieldSlider ubiFS = forceUbiFieldSliders.getLast();
                        valFS.setValue(Math.abs(valor));
                        ubiFS.setValue(ubicacion);
                        simuZone.forces.getLast().setUpward(sentido.equalsIgnoreCase("ARRIBA"));
                    }
                    case 3 -> { // === Momento ===
                        addMoment();
                        FieldSlider valFS = momentValueFieldSliders.getLast();
                        FieldSlider ubiFS = momentUbiFieldSliders.getLast();
                        valFS.setValue(Math.abs(valor));
                        ubiFS.setValue(ubicacion);
                        simuZone.moments.getLast().setClokwise(sentido.equalsIgnoreCase("HORARIO"));
                    }
                    case 4, 5, 6 -> { // === Apoyo ===
                        Elements.SUPPORT_TYPE type = switch (tipoNombre.toUpperCase()) {
                            case "SOPORTE_FIJO" -> Elements.SUPPORT_TYPE.PIN;
                            case "SOPORTE_MOVIL" -> Elements.SUPPORT_TYPE.ROLLER;
                            case "SOPORTE_EMPOTRADO" -> Elements.SUPPORT_TYPE.FIXED;
                            default -> Elements.SUPPORT_TYPE.PIN; // por defecto
                        };

                        if (!supportActiveA) {
                            supportFieldSliderA.setValue(ubicacion);
                            updateSupportTypeA(type);
                            toggleSupportA();
                        } else {
                            supportFieldSliderB.setValue(ubicacion);
                            updateSupportTypeB(type);
                            toggleSupportB();
                        }
                    }
                    default -> {
                        // Ignorar fuerza distribuida u otros tipos --> extensiones
                    }
                }
            }

            // === 3. Título del simulador ===
            String qTitulo = "SELECT TITULO FROM simulador WHERE idSIMULADOR = " + simId;
            ResultSet rsTitulo = Main.Phi6Lab.db.query.executeQuery(qTitulo);
            if (rsTitulo.next()) {
                simuTitleField.setText(rsTitulo.getString("TITULO"));
            }

            updateLabels();
            System.out.println("Simulación cargada correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int saveSimToDB(int simId) {
        try {
            String titulo = simuTitleField.getText();
            float longitud = beamFieldSlider.slider.value;
            String now = java.time.LocalDateTime.now().toString();

            int vigaId;

            // === Insertar o actualizar VIGA ===
            if (simId <= 0) {
                // Nueva simulación, crear nueva viga
                String qInsertViga = "INSERT INTO viga (LONGITUD) VALUES (" + longitud + ")";
                Main.Phi6Lab.db.query.execute(qInsertViga);
                ResultSet rsViga = Main.Phi6Lab.db.query.executeQuery("SELECT LAST_INSERT_ID() as id");
                rsViga.next();
                vigaId = rsViga.getInt("id");

                // === 2. Crear nuevo simulador ===
                String qInsertSim = "INSERT INTO simulador (TITULO, CREACION, MODIFICACION, VIGA_idVIGA) " + "VALUES ('" + titulo + "', '" + now + "', '" + now + "', " + vigaId + ")";
                Main.Phi6Lab.db.query.execute(qInsertSim);
                ResultSet rsSim = Main.Phi6Lab.db.query.executeQuery("SELECT LAST_INSERT_ID() as id");
                rsSim.next();
                simId = rsSim.getInt("id");

            } else {
                // Simulador existente: obtener viga
                String qGetViga = "SELECT VIGA_idVIGA FROM simulador WHERE idSIMULADOR = " + simId;
                ResultSet rsViga = Main.Phi6Lab.db.query.executeQuery(qGetViga);
                rsViga.next();
                vigaId = rsViga.getInt("VIGA_idVIGA");

                // Actualizar longitud de viga y simulador
                String qUpdateViga = "UPDATE viga SET LONGITUD = " + longitud + " WHERE idVIGA = " + vigaId;
                Main.Phi6Lab.db.query.execute(qUpdateViga);

                String qUpdateSim = "UPDATE simulador SET TITULO = '" + titulo + "', MODIFICACION = '" + now + "' " + "WHERE idSIMULADOR = " + simId;
                Main.Phi6Lab.db.query.execute(qUpdateSim);

                // Eliminar elementos existentes
                String qDeleteElems = "DELETE FROM elemento WHERE VIGA_idVIGA = " + vigaId;
                Main.Phi6Lab.db.query.execute(qDeleteElems);
            }

            // === Insertar fuerzas ===
            for (int i = 0; i < simuZone.forces.size(); i++) {
                Elements.Force f = simuZone.forces.get(i);
                int tipoId = 1;
                String sentido = f.isUpward() ? "ARRIBA" : "ABAJO";
                String q = "INSERT INTO elemento (VALOR, UBICACION, SENTIDO, TIPO_idTIPO, VIGA_idVIGA) " + "VALUES (" + f.getMagnitude() + ", " + f.getPosition() + ", '" + sentido + "', " + tipoId + ", " + vigaId + ")";
                Main.Phi6Lab.db.query.execute(q);
            }

            // === Insertar momentos ===
            for (int i = 0; i < simuZone.moments.size(); i++) {
                Elements.Moment m = simuZone.moments.get(i);
                int tipoId = 3;
                String sentido = m.isClokwise() ? "HORARIO" : "ANTIHORARIO";
                String q = "INSERT INTO elemento (VALOR, UBICACION, SENTIDO, TIPO_idTIPO, VIGA_idVIGA) " + "VALUES (" + m.getMagnitude() + ", " + m.getPosition() + ", '" + sentido + "', " + tipoId + ", " + vigaId + ")";
                Main.Phi6Lab.db.query.execute(q);
            }

            // === Insertar apoyos ===
            for (int i = 0; i < simuZone.supports.size(); i++) {
                Elements.Support s = simuZone.supports.get(i);
                int tipoId = switch (s.type) {
                    case PIN -> 4;
                    case ROLLER -> 5;
                    case FIXED -> 6;
                };
                String sentido = "N/A";
                String q = "INSERT INTO elemento (VALOR, UBICACION, SENTIDO, TIPO_idTIPO, VIGA_idVIGA) " + "VALUES (0, " + s.position + ", '" + sentido + "', " + tipoId + ", " + vigaId + ")";
                Main.Phi6Lab.db.query.execute(q);
            }

            System.out.println("Simulación guardada correctamente con ID " + simId);
            return simId;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}