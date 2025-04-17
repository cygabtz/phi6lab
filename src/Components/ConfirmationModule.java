package Components;

import processing.core.PApplet;
import static Constants.Layout.*;
import Constants.FinalColors;

/**
 * Módulo reutilizable para confirmar acciones críticas como guardar, eliminar o salir sin guardar.
 *
 * <p>Extiende la clase {@link Module} y proporciona dos botones estándar: "Aceptar" y "Cancelar",
 * además de un mensaje configurable. Permite ejecutar una acción personalizada cuando el usuario confirma.
 *
 * <p>El módulo debe activarse visualmente y estar contenido dentro del sistema de módulos de la aplicación
 * como un componente flotante.
 */
public class ConfirmationModule extends Module {

    /** Botón que confirma la acción. Ejecuta la operación definida si se pulsa. */
    public Button acceptButton;

    /** Botón que cancela la operación y cierra el módulo. */
    public Button cancelButton;

    /** Acción a ejecutar cuando se pulsa el botón de aceptar. */
    private Runnable onConfirm;

    /**
     * Constructor del módulo de confirmación. Define botones estándar con estilos personalizados
     * y los agrega al conjunto de componentes internos.
     *
     * @param p5     instancia de Processing
     * @param x      posición horizontal del módulo
     * @param y      posición vertical del módulo
     * @param width  ancho del módulo
     * @param height alto del módulo
     */
    public ConfirmationModule(PApplet p5, float x, float y, float width, float height) {
        super(p5, x, y, width, height);
        this.setTitle("Confirmación");

        float buttonWidth = width / 3;
        float buttonHeight = frame;

        // Botón de aceptar
        acceptButton = new Button(p5, x + margin, y + height - buttonHeight - margin, buttonWidth, buttonHeight);
        acceptButton.setText("Aceptar");
        acceptButton.strokeColorOn = FinalColors.accentSkyBlue();

        // Botón de cancelar
        cancelButton = new Button(p5, x + width - buttonWidth - margin, y + height - buttonHeight - margin, buttonWidth, buttonHeight);
        cancelButton.setText("Cancelar");
        cancelButton.strokeColorOn = FinalColors.primaryYellow();

        components.add(acceptButton);
        components.add(cancelButton);
    }

    /**
     * Muestra el módulo de confirmación junto con un mensaje personalizado centrado.
     *
     * @param message texto a mostrar en el centro del módulo
     */
    public void display(String message) {
        super.display();
        if (!opened) return;

        p5.push();
        p5.fill(FinalColors.textWhite());
        p5.textSize(18);
        p5.textAlign(p5.CENTER, p5.CENTER);
        p5.text(message, x + width / 2, y + height / 2 - frame);
        p5.pop();
    }

    /**
     * Gestiona las acciones del ratón sobre los botones del módulo.
     * <ul>
     *   <li>Si se pulsa "Aceptar", se ejecuta {@code onConfirm} y se cierra el módulo.</li>
     *   <li>Si se pulsa "Cancelar", simplemente se cierra el módulo.</li>
     * </ul>
     */
    public void mousePressed() {
        if (acceptButton.mouseOverButton(p5)) {
            if (onConfirm != null) onConfirm.run();
            this.opened = false;
        }

        if (cancelButton.mouseOverButton(p5)) {
            this.opened = false;
        }

        super.mousePressed();
    }

    /**
     * Establece la acción a ejecutar si el usuario acepta la confirmación.
     *
     * @param r función que se ejecutará al pulsar "Aceptar"
     */
    public void setOnConfirm(Runnable r) {
        this.onConfirm = r;
    }
}
