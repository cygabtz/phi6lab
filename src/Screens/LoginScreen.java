package Screens;

import Components.*;
import Constants.Colors;
import Main.GUI;
import Main.Phi6Lab;
import processing.core.PApplet;
import processing.core.PImage;

import static Constants.Layout.*;

/**
 * Pantalla de inicio de sesión y registro de usuario para la aplicación Phi6Lab.
 *
 * <p>Permite al usuario autenticarse con nombre de usuario y contraseña o acceder como invitado.
 * También ofrece la posibilidad de crear una nueva cuenta de usuario a través de un formulario de registro.
 *
 * <p>Esta clase extiende {@link Screen} y utiliza componentes personalizados como {@link TextField} y {@link Button}
 * para construir la interfaz. Gestiona la autenticación del usuario y actualiza {@code GUI.currentUser}
 * para restringir el acceso a simuladores propios.
 *
 * <p>Al iniciar sesión correctamente, se redirige a {@link HomeScreen} y se cargan los simuladores del usuario activo.
 *
 * @see TextField
 * @see Button
 * @see GUI
 * @see HomeScreen
 * @see Phi6Lab
 */
public class LoginScreen extends Screen {

    private TextField userField;
    private TextField passField;
    private Button loginButton;
    private Button guestButton;

    // Estado
    private boolean showRegisterForm = false;

    // Campos para registro
    private TextField regUserField;
    private TextField regPassField;
    private TextField regConfirmField;
    private Button registerButton;
    private Button createAccountButton;

    // Banner
    private PImage bannerImage;
    private float bannerWidth, bannerHeight;


    /**
     * Constructor de la pantalla de login.
     *
     * @param p5 instancia de Processing
     * @param appColors paleta de colores de la aplicación
     * @param appFonts tipografías disponibles para la interfaz
     */
    public LoginScreen(PApplet p5, Colors appColors, Constants.Fonts appFonts) {
        super(p5, appColors, appFonts);
        initializeComponents();
    }

    /**
     * Inicializa todos los campos y botones visibles en la pantalla,
     * incluyendo formularios de inicio de sesión y registro.
     */
    private void initializeComponents() {
        float fieldWidth = hRect * 2;
        float fieldHeight = frame;

        float centerX = screenH / 2f - fieldWidth / 2;
        float startY = screenV / 2f - 2 * fieldHeight;

        userField = new TextField(p5, centerX, startY, fieldWidth, fieldHeight);
        userField.setEmptyText("Usuario");
        userField.borderEnabled = true;

        passField = new TextField(p5, centerX, startY + fieldHeight + margin, fieldWidth, fieldHeight);
        passField.setEmptyText("Contraseña");
        passField.borderEnabled = true;

        loginButton = new Button(p5, centerX, startY + 2 * (fieldHeight + margin), fieldWidth, fieldHeight);
        loginButton.setText("Iniciar sesión");

        guestButton = new Button(p5, centerX, startY + 3 * (fieldHeight + margin), fieldWidth, fieldHeight);
        guestButton.setText("Entrar como invitado");

        registerButton = new Button(p5, loginButton.x, guestButton.y + guestButton.height + margin, loginButton.width, loginButton.height);
        registerButton.setText("Registrarse");

        float regY = guestButton.y + guestButton.height + 2 * (loginButton.height + margin);
        regUserField = new TextField(p5, loginButton.x, regY, loginButton.width, loginButton.height);
        regUserField.setEmptyText("Nuevo usuario");
        regUserField.borderEnabled = true;

        regPassField = new TextField(p5, loginButton.x, regY + loginButton.height + margin, loginButton.width, loginButton.height);
        regPassField.setEmptyText("Contraseña");
        regPassField.borderEnabled = true;

        regConfirmField = new TextField(p5, loginButton.x, regY + 2 * (loginButton.height + margin), loginButton.width, loginButton.height);
        regConfirmField.setEmptyText("Confirmar contraseña");
        regConfirmField.borderEnabled = true;

        createAccountButton = new Button(p5, loginButton.x, regY + 3 * (loginButton.height + margin), loginButton.width, loginButton.height);
        createAccountButton.setText("Crear cuenta");

        bannerWidth = 4 * hRect;
        bannerHeight = 2 * vRect - 2 * hMargin;

        bannerImage = p5.loadImage("data/icons/banner.png");
        if (bannerImage != null) {
            bannerImage.resize((int) screenH/2, 0); // mantener aspecto
        }

    }

    /**
     * Dibuja todos los elementos visuales de la pantalla de login, incluyendo
     * campos de texto, botones y el formulario de registro si está activo.
     */
    @Override
    public void display() {
        userField.display();
        passField.display();
        loginButton.display();
        guestButton.display();

        userField.display();
        passField.display();
        loginButton.display();
        guestButton.display();
        registerButton.display();

        if (showRegisterForm) {
            regUserField.display();
            regPassField.display();
            regConfirmField.display();
            createAccountButton.display();
        }

        displayBanner();

    }

    /**
     * Dibuja el banner superior en la pantalla de inicio de sesión.
     *
     * <p>Este método se encarga de mostrar visualmente la imagen decorativa
     * correspondiente al banner de la pantalla de login, centrada en la parte superior.
     *
     * <p>Primero establece el estilo gráfico (relleno y sin borde), y si la imagen
     * del banner está correctamente cargada, la dibuja en la posición determinada.
     *
     * <p>Este método es llamado automáticamente dentro del método {@link #display()},
     * y su ejecución es condicional a que {@code bannerImage} no sea nulo.
     *
     * @see #display()
     */
    private void displayBanner() {
        p5.push();

        // Fondo del banner
        p5.fill(appColors.bgGrey());
        p5.noStroke();
        //p5.rect(hRect + vMargin, vMargin, bannerWidth, bannerHeight, corner);

        // Imagen del banner
        if (bannerImage != null) {
            p5.image(bannerImage, hRect + 100, vMargin + 100);
        }

        p5.pop();
    }


    /**
     * Gestiona las acciones del usuario con el ratón, incluyendo:
     * - Inicio de sesión
     * - Acceso como invitado
     * - Activación del formulario de registro
     * - Creación de cuenta
     *
     * @param gui instancia del sistema de pantallas
     */
    public void mousePressed(GUI gui) {
        userField.mousePressed();
        passField.mousePressed();
        regUserField.mousePressed();
        regPassField.mousePressed();
        regConfirmField.mousePressed();

        if (loginButton.mouseOverButton(p5)) {
            String user = userField.getText();
            String pass = passField.getText();

            if (Phi6Lab.db.isUserPass(user, pass)) {
                GUI.currentUser = user;
                GUI.setCurrentScreen(GUI.SCREEN.HOME);
                System.out.println("Sesión iniciada como " + user);
                ((HomeScreen) gui.screens[GUI.SCREEN.HOME.ordinal()]).loadSimuladores();
            } else {
                System.out.println("Usuario o contraseña incorrectos");
            }
        }

        if (guestButton.mouseOverButton(p5)) {
            GUI.currentUser = "invitado";
            GUI.setCurrentScreen(GUI.SCREEN.HOME);
            ((HomeScreen) gui.screens[GUI.SCREEN.HOME.ordinal()]).loadSimuladores();
        }

        if (registerButton.mouseOverButton(p5)) {
            showRegisterForm = !showRegisterForm;
        }

        if (showRegisterForm && createAccountButton.mouseOverButton(p5)) {
            String newUser = regUserField.getText();
            String newPass = regPassField.getText();
            String confirmPass = regConfirmField.getText();

            if (newUser.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                System.out.println("Completa todos los campos.");
            } else if (!newPass.equals(confirmPass)) {
                System.out.println("Las contraseñas no coinciden.");
            } else if (Phi6Lab.db.existsUser(newUser)) {
                System.out.println("El usuario ya existe.");
            } else {
                Phi6Lab.db.insertNewUser(newUser, newPass);
                GUI.currentUser = newUser;
                GUI.setCurrentScreen(GUI.SCREEN.HOME);
                System.out.println("Usuario creado y sesión iniciada como " + newUser);
            }
        }
    }

    /**
     * Procesa la entrada del teclado en los campos de texto activos.
     *
     * @param key carácter presionado
     * @param keyCode código ASCII o especial del carácter
     */
    public void keyPressed(char key, int keyCode) {
        // Campos de login
        if (userField.selected) userField.keyPressed(key, keyCode);
        if (passField.selected) passField.keyPressed(key, keyCode);

        // Campos de registro
        if (showRegisterForm) {
            if (regUserField.selected) regUserField.keyPressed(key, keyCode);
            if (regPassField.selected) regPassField.keyPressed(key, keyCode);
            if (regConfirmField.selected) regConfirmField.keyPressed(key, keyCode);
        }
    }

    /**
     * Restaura el estado inicial del formulario de login,
     * borrando campos y ocultando el panel de registro.
     */
    public void reset() {
        userField.setText("");
        passField.setText("");
        showRegisterForm = false;
    }
}
