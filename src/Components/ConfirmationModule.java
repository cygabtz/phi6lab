package Components;

import processing.core.PApplet;
import static Constants.Layout.*;
import Constants.FinalColors;

/**
 * Módulo reutilizable de confirmación para acciones críticas (guardar, eliminar...).
 */
public class ConfirmationModule extends Module {

    public Button acceptButton;
    public Button cancelButton;
    private Runnable onConfirm; // Acción a ejecutar si se acepta

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

    public void display(String message) {
        super.display();
        if (!opened) return;

        p5.fill(FinalColors.textWhite());
        p5.textSize(18);
        p5.textAlign(p5.CENTER, p5.CENTER);
        p5.text(message, x + width / 2, y + height / 2 - frame);
    }

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

    public void setOnConfirm(Runnable r) {
        this.onConfirm = r;
    }
}
