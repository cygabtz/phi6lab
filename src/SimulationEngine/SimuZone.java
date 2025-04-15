package SimulationEngine;

import Constants.FinalColors;
import Constants.StaticFonts;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

import java.util.ArrayList;

import static Constants.Layout.*;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static processing.core.PApplet.radians;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.CORNER;

/**
 * Representa la zona gráfica de simulación de la viga.
 *
 * <p>Esta clase se encarga de renderizar visualmente la viga, las fuerzas puntuales,
 * momentos y apoyos definidos por el usuario, así como sus etiquetas.
 *
 * <p>Utiliza elementos de {@link Elements} para representar cada tipo de carga y soporte,
 * y adapta su posición al tamaño y escala de la viga.
 *
 * <p>Los métodos de dibujo se organizan por tipo de elemento, y utilizan funciones auxiliares
 * para representar flechas, arcos y símbolos de apoyo.
 */
public class SimuZone {
    // === Contexto gráfico y dimensiones ===

    /**
     * Instancia principal de Processing utilizada para dibujar.
     */
    private PApplet p5;

    /**
     * Coordenada X (horizontal) de inicio de la viga en pantalla.
     */
    public float beamX;

    /**
     * Coordenada Y (vertical) de la viga.
     */
    public float beamY;

    /**
     * Ancho (longitud visual) de la viga.
     */
    public float beamWidth;

    /**
     * Altura gráfica de la viga en píxeles.
     */
    public float beamHeight;

    /**
     * Longitud real de la viga en metros. Se usa para escalar posiciones físicas a pantalla.
     */
    public float beamValue;

    public enum DIRECTION {UP, DOWN, LEFT, RIGHT}

    // === Elementos estructurales ===

    /**
     * Lista de fuerzas puntuales aplicadas sobre la viga.
     */
    public ArrayList<Elements.Force> forces;

    /**
     * Lista de apoyos (soportes) activos sobre la viga.
     */
    public ArrayList<Elements.Support> supports;

    /**
     * Lista de momentos aplicados sobre la viga.
     */
    public ArrayList<Elements.Moment> moments;

    // === Etiquetas visuales ===

    /**
     * Etiquetas visibles para cada fuerza (e.g., F1, F2, ...).
     */
    public ArrayList<String> forceLabels = new ArrayList<>();

    /**
     * Etiquetas visibles para cada momento (e.g., M1, M2, ...).
     */
    public ArrayList<String> momentLabels = new ArrayList<>();

    // === Estilos visuales ===

    /**
     * Color del trazo para dibujar los elementos.
     */
    private int strokeColor = FinalColors.primaryYellow();

    /**
     * Color de relleno para la viga.
     */
    private int fillColor = FinalColors.accentDenimBlue();

    /**
     * Color del texto en etiquetas y valores.
     */
    private int textColor = FinalColors.textWhite();

    /**
     * Grosor del trazo de líneas.
     */
    private float strokeW = 1.0f;

    /**
     * Tamaño del texto de etiquetas.
     */
    private int fontSize = 14;

    /**
     * Fuente tipográfica usada en etiquetas.
     */
    private PFont pFont = StaticFonts.getFont(0);

    // === Configuración visual de apoyos ===

    /**
     * Tamaño gráfico de los apoyos (ancho y alto).
     */
    public float supportSize = 20;

    /**
     * Diámetro de las ruedas en apoyos tipo ROLLER.
     */
    public float wheelDiameter = 10;

    /**
     * Crea una nueva instancia de {@code SimuZone}.
     *
     * @param p5     instancia principal de {@link processing.core.PApplet}.
     * @param x      posición horizontal de la zona de simulación.
     * @param y      posición vertical de la viga.
     * @param width  ancho total de la viga.
     * @param height altura visual de la viga.
     */
    public SimuZone(PApplet p5, float x, float y, float width, float height) {
        this.p5 = p5;
        // Inicializamos las listas
        forces = new ArrayList<>();
        supports = new ArrayList<>();
        moments = new ArrayList<>();

        this.beamX = x;
        this.beamY = y;
        this.beamWidth = width;
        this.beamHeight = height;
    }

    /**
     * Dibuja todos los elementos gráficos de la zona de simulación.
     *
     * <p>Esto incluye la viga principal, las fuerzas puntuales, los momentos
     * y los apoyos activos, usando las listas {@link #forces}, {@link #moments} y {@link #supports}.
     */
    public void display() {
        // Dibujar fondo
        drawBg();

        // Dibuja la viga (rectángulo)
        drawBeam();

        // Dibujar las flechas de cada fuerza
        drawForces();

        // Dibujar los símbolos de cada momento
        drawMoments();

        // Dibujar apoyos
        drawSupports();
    }

    /**
     * Dibuja el fondo del panel de simulación como un rectángulo.
     */
    private void drawBg() {
        p5.fill(FinalColors.bgLightGrey());
        p5.noStroke();
        p5.rect(2 * hRect + 2 * margin, frame + 2 * margin, 3 * hRect - 4 * margin, 4 * vRect - 4 * margin - frame, corner);
    }

    /**
     * Dibuja el cuerpo principal de la viga como un rectángulo horizontal.
     */
    private void drawBeam() {
        p5.push();
        applyStyle();
        p5.rect(beamX, beamY, beamWidth, beamHeight);
        //drawArrow(beamX, beamY, 0, 100, 2, 20);
        p5.pop();
    }

    /**
     * Dibuja las fuerzas puntuales definidas por el usuario.
     *
     * <p>Cada fuerza se representa mediante una flecha orientada según su dirección,
     * y se le asigna una etiqueta correspondiente desde {@link #forceLabels}.
     */
    void drawForces() {
        p5.push();
        applyStyle();

        int index = 0;
        for (Elements.Force f : forces) {
            if (f == null) {
                System.out.println("SimuZone: no se puede dibujar fuerza poque lista vacía");
            } else {
                float arrowLength = 80;
                float arrowThickness = 2;
                float arrowHeadSize = 15;
                float pos = PApplet.map((float) f.position, 0, beamValue, 0, beamWidth);
                pos = PApplet.constrain(pos, 0, beamWidth);
                float forceX = (float) (beamX + pos);

                if (f.isUpward) {
                    drawArrowDir(forceX, beamY + arrowLength + arrowHeadSize, DIRECTION.UP, arrowLength, arrowThickness, arrowHeadSize);
                } else {
                    drawArrowDir(forceX, beamY, DIRECTION.DOWN, arrowLength, arrowThickness, arrowHeadSize);
                }

                // Etiqueta

                float labelWidth = 60;
                String label = "F" + forceLabels.get(index);
                float textY = beamY - arrowLength - labelWidth + (labelWidth / 4); // Centrado vertical

                //Rectángulo
                p5.noStroke();
                p5.fill(FinalColors.primaryYellow());
                p5.rect(forceX - labelWidth / 2, beamY - arrowLength - labelWidth, labelWidth, labelWidth / 2, corner);

                // Texto
                p5.fill(FinalColors.bgBlack());
                p5.textAlign(PConstants.CENTER, PConstants.CENTER);
                p5.textSize(20);
                p5.text(label, forceX, textY);

                index++;
            }

        }
        p5.pop();
    }

    /**
     * Dibuja los momentos aplicados sobre la viga.
     *
     * <p>Cada momento se representa con una flecha circular (arco) que indica su sentido
     * (horario o antihorario), y se etiqueta según la lista {@link #momentLabels}.
     */
    void drawMoments() {
        p5.push();
        applyStyle();
        for (Elements.Moment m : moments) {
            if (m == null) {
                System.out.println("SimuZone: no se puede dibujar momento poque lista vacía");
            } else {
                //Variables
                float startAngle = -p5.HALF_PI;
                float stopAngle = p5.PI;
                float arcRadius = 35;

                // Cambiar de escala
                float pos = PApplet.map((float) m.position, 0, beamValue, 0, beamWidth);

                // Evitar pasar el máximo
                pos = PApplet.constrain(pos, 0, beamWidth);

                // Dibujar los arcos
                float momentX = beamX + pos;
                if (m.isClokwise) {
                    circArrowInv(momentX, beamY, arcRadius, startAngle, stopAngle, 10);
                } else {
                    circArrow(momentX, beamY, arcRadius, startAngle, stopAngle, 10);
                }

                // Dibujar etiqueta del momento
                int i = moments.indexOf(m); // Obtener índice actual
                if (i >= 0 && i < momentLabels.size()) {
                    String label = "M" + momentLabels.get(i);
                    float labelWidth = 40;
                    float labelHeight = 18;

                    float labelX = momentX - labelWidth / 2;
                    float labelY = beamY - arcRadius - labelHeight - 5;

                    // Fondo del label
                    p5.fill(FinalColors.primaryYellow());
                    p5.noStroke();
                    p5.rect(labelX, labelY, labelWidth, labelHeight, corner);

                    // Texto centrado
                    p5.fill(FinalColors.bgBlack());
                    p5.textAlign(PConstants.CENTER, PConstants.CENTER);
                    p5.textSize(20);
                    p5.text(label, momentX, labelY + labelHeight / 2);
                }

            }
        }
        p5.pop();
    }

    /**
     * Dibuja los apoyos activos definidos por el usuario.
     *
     * <p>El tipo de apoyo (PIN, ROLLER o FIXED) determina el símbolo gráfico a mostrar.
     * La posición de cada apoyo se calcula según la longitud actual de la viga.
     */
    void drawSupports() {
        // Cada soporte se dibuja en función de su tipo
        if (!supports.isEmpty()) {
            for (Elements.Support sp : supports) {

                if (sp.type == null) break;

                float spX = beamX + PApplet.map((float) sp.position, 0, beamValue, 0, beamWidth);

                // Línea base donde se ubican los soportes: justo debajo de la viga
                float baseY = beamY + beamHeight;
                switch (sp.type) {
                    case PIN:
                        drawPinSupport(spX, baseY);
                        break;
                    case ROLLER:
                        drawRollerSupport(spX, baseY);
                        break;
                    case FIXED:
                        // Para FIXED se comprueba si está en el extremo izquierdo o derecho
                        if (Math.abs(sp.position) < 0.001) {
                            drawFixedSupport(spX, baseY, true);  // extremo izquierdo
                        } else if (Math.abs(sp.position - beamWidth) < 0.001) {
                            drawFixedSupport(spX, baseY, false); // extremo derecho
                        } else {
                            // Si se coloca en el centro se puede dibujar por defecto hacia la izquierda
                            drawFixedSupport(spX, baseY, true);
                        }
                        break;
                }
            }
        }
    }

    /**
     * Dibuja un momento en sentido antihorario (flecha circular).
     *
     * @param x         posición horizontal del centro.
     * @param y         posición vertical del centro.
     * @param radius    radio del arco.
     * @param start     ángulo inicial.
     * @param stop      ángulo final.
     * @param arrowSize tamaño de la punta de flecha.
     */
    void circArrow(float x, float y, float radius, float start, float stop, float arrowSize) {
        p5.ellipseMode(CENTER);
        p5.noFill();
        p5.stroke(FinalColors.primaryYellow());
        p5.strokeWeight(2);

        p5.arc(x, y, radius * 2, radius * 2, start, stop);

        float arrowX = (float) (x + cos(stop) * radius);
        float arrowY = (float) (y + sin(stop) * radius);

        float point1X = (float) (x + (cos(stop) * radius) + (cos(stop - radians(arrowSize * 5)) * (arrowSize)));
        float point1Y = (float) (y + (sin(stop) * radius) + (sin(stop - radians(arrowSize * 5)) * (arrowSize)));

        float point2X = (float) (x + (cos(stop) * radius) + (cos(stop - radians(-arrowSize * 5)) * (-arrowSize)));
        float point2Y = (float) (y + (sin(stop) * radius) + (sin(stop - radians(-arrowSize * 5)) * (-arrowSize)));

        p5.line(arrowX, arrowY, point1X, point1Y);
        p5.line(arrowX, arrowY, point2X, point2Y);
    }

    /**
     * Dibuja un momento en sentido horario (flecha circular invertida).
     *
     * @param x         posición horizontal del centro.
     * @param y         posición vertical del centro.
     * @param radius    radio del arco.
     * @param start     ángulo inicial.
     * @param stop      ángulo final.
     * @param arrowSize tamaño de la punta de flecha.
     */
    void circArrowInv(float x, float y, float radius, float start, float stop, float arrowSize) {
        p5.ellipseMode(CENTER);
        p5.noFill();
        p5.stroke(FinalColors.primaryYellow());
        p5.strokeWeight(2);

        // Dibujar arco
        p5.arc(x, y, radius * 2, radius * 2, start, stop);

        // Coordenadas del punto donde termina el arco
        float arrowX = (float) (x + cos(start) * radius);
        float arrowY = (float) (y + sin(start) * radius);

        // Calcular puntos del triángulo reflejados
        float point1X = (float) (x + (cos(start) * radius) + (cos(start + radians(arrowSize * 5)) * (arrowSize)));
        float point1Y = (float) (y + (sin(start) * radius) + (sin(start + radians(arrowSize * 5)) * (arrowSize)));

        float point2X = (float) (x + (cos(start) * radius) + (cos(start + radians(-arrowSize * 5)) * (-arrowSize)));
        float point2Y = (float) (y + (sin(start) * radius) + (sin(start + radians(-arrowSize * 5)) * (-arrowSize)));

        // Dibujar flecha reflejada
        p5.line(arrowX, arrowY, point1X, point1Y);
        p5.line(arrowX, arrowY, point2X, point2Y);
    }


    private void applyStyle() {
        p5.stroke(strokeColor);
        p5.fill(fillColor);
        p5.strokeWeight(strokeW);
        p5.textFont(pFont);
        p5.textSize(fontSize);
    }

    /**
     * Dibuja una flecha orientada en un ángulo específico, usada para representar fuerzas.
     *
     * @param startX    coordenada X de inicio.
     * @param startY    coordenada Y de inicio.
     * @param degrees   ángulo de dirección en grados.
     * @param length    longitud de la flecha.
     * @param thickness grosor de la línea.
     * @param triHeight tamaño del triángulo en la punta.
     */
    void drawArrow(float startX, float startY, float degrees, float length, float thickness, float triHeight) {
        float angle = (float) Math.toRadians(degrees);

        p5.strokeWeight(thickness);
        p5.stroke(FinalColors.primaryYellow());
        p5.fill(FinalColors.primaryYellow());

        float triWidth = 20;

        // Coordenadas del extremo de la línea
        float endX = startX + length * (float) sin(angle);
        float endY = startY - length * (float) cos(angle);

        // Dibujar línea
        p5.line(startX, startY, endX, endY);

        // Coordenadas de los puntos del triángulo
        float tipX = endX + triHeight * (float) sin(angle);
        float tipY = endY - triHeight * (float) cos(angle);
        float leftX = endX - (triWidth / 2) * (float) cos(angle);
        float leftY = endY - (triWidth / 2) * (float) sin(angle);
        float rightX = endX + (triWidth / 2) * (float) cos(angle);
        float rightY = endY + (triWidth / 2) * (float) sin(angle);

        // Dibujar triángulo
        p5.triangle(tipX, tipY, leftX, leftY, rightX, rightY);
    }

    /**
     * Dibuja una flecha en una dirección cardinal (UP, DOWN, LEFT, RIGHT).
     *
     * @param startX    coordenada X de inicio.
     * @param startY    coordenada Y de inicio.
     * @param direction dirección de la flecha.
     * @param length    longitud de la flecha.
     * @param thickness grosor del trazo.
     * @param triHeight tamaño del triángulo en la punta.
     */
    void drawArrowDir(float startX, float startY, DIRECTION direction, float length, float thickness, float triHeight) {

        // Ajustar el ángulo según el valor del enumerado

        switch (direction) {
            case UP -> drawArrow(startX, startY - length - triHeight, 0, length, thickness, triHeight);
            case DOWN -> drawArrow(startX, startY - length - triHeight, 180, length, thickness, triHeight);
            case LEFT -> drawArrow(startX + length + triHeight, startY, 270, length, thickness, triHeight);
            case RIGHT -> drawArrow(startX - length - triHeight, startY, 90, length, thickness, triHeight);
        }

    }

    /**
     * Dibuja un apoyo de tipo PIN (articulado) como un triángulo.
     *
     * @param x     posición horizontal.
     * @param baseY posición vertical base.
     */
    void drawPinSupport(float x, float baseY) {
        p5.push();

        applyStyle();
        float triangleWidth = supportSize + wheelDiameter;
        x -= triangleWidth / 2;
        float triangleHeight = supportSize + wheelDiameter;

        // Triángulo
        p5.fill(FinalColors.primaryYellow());
        p5.triangle(x + (triangleWidth) / 2, baseY, x, baseY + triangleHeight, x + triangleWidth, baseY + triangleHeight);

        // Línea horizontal debajo del triángulo
        applyStyle();
        p5.line(x - triangleWidth / 2, baseY + triangleHeight, x + triangleWidth + triangleWidth / 2, baseY + triangleHeight);

        p5.pop();
    }

    /**
     * Dibuja un apoyo de tipo FIXED (empotrado) en el extremo izquierdo o derecho.
     *
     * @param x        posición horizontal.
     * @param baseY    posición vertical base.
     * @param leftSide si es {@code true}, el apoyo se dibuja hacia la izquierda.
     */
    void drawFixedSupport(float x, float baseY, boolean leftSide) {
        p5.push();
        applyStyle();
        float supportHeight = 100;

        if (leftSide) {
            p5.rect(x - supportHeight / 2, baseY - beamHeight - (supportHeight - beamHeight) / 2, supportHeight / 2, supportHeight);
        } else {
            // supportHeight = beamHeight + 2*b
            p5.rect(x, baseY - beamHeight - (supportHeight - beamHeight) / 2, supportHeight / 2, supportHeight);
        }
        p5.pop();
    }

    /**
     * Dibuja un apoyo de tipo ROLLER (rodillo), con un triángulo y ruedas debajo.
     *
     * @param x     posición horizontal.
     * @param baseY posición vertical base.
     */
    void drawRollerSupport(float x, float baseY) {
        p5.push();
        float triangleWidth = supportSize;
        x -= triangleWidth / 2;
        float triangleHeight = supportSize;
        p5.fill(150);

        // Triángulo
        applyStyle();
        p5.fill(FinalColors.primaryYellow());
        p5.triangle(x + (triangleWidth) / 2, baseY, x, baseY + triangleHeight, x + triangleWidth, baseY + triangleHeight);

        // Línea horizontal debajo del triángulo
        applyStyle();
        p5.line(x - triangleWidth / 2, baseY + triangleHeight, x + triangleWidth + triangleWidth / 2, baseY + triangleHeight);


        // Dibuja ruedas (círculos pequeños)
        p5.fill(FinalColors.primaryYellow());
        p5.ellipseMode(CORNER);
        p5.ellipse(x - triangleWidth / 2, baseY + triangleHeight, wheelDiameter, wheelDiameter);
        p5.ellipseMode(CENTER);
        p5.ellipse(x + triangleWidth / 2, baseY + triangleHeight + wheelDiameter / 2, wheelDiameter, wheelDiameter);
        p5.ellipse(x + triangleWidth + wheelDiameter / 2, baseY + triangleHeight + wheelDiameter / 2, wheelDiameter, wheelDiameter);
        p5.pop();
    }

}
