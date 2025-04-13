package SimulationEngine;

import Constants.FinalColors;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayList;

import static Constants.Layout.hRect;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static processing.core.PApplet.radians;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.CORNER;

public class BeamDrawing {
    PApplet p5;
    public float beamX = 100;               // Origen horizontal de la viga
    public float beamY = 250;               // Posición vertical (parte superior de la viga)
    public float beamLength = 6.0f;         // Longitud de la viga en metros

    public float scaleFactor = 20;
    public final float beamDrawLengthPx = 2*hRect; // Longitud fija de la viga en pantalla en píxeles

    public float beamHeight = 20;           // Altura en píxeles de la viga
    public float supportSize = 20;          //Altura y ancho de los soportes en píxeles

    public enum DIRECTION {UP, DOWN, LEFT, RIGHT} //Dirección de las flechas
    public float wheelDiameter = 10;

    // Listas de elementos para la viga
    public ArrayList<Elements.Force> forces;
    public ArrayList<Elements.Support> supports;
    public ArrayList<Elements.Moment> moments;

    // Control de estilo
    private int strokeColor = 0;        // Negro
    private int fillColor = 200;        // Gris claro
    private int textColor = 0;          // Negro
    private float strokeW = 1.0f;       // Grosor de trazo
    private int fontSize = 12;          // Tamaño de texto
    private PFont pFont;                // Fuente de texto


    public BeamDrawing(PApplet p5){
        this.p5 = p5;
        // Inicializamos las listas
        forces = new ArrayList<>();
        supports = new ArrayList<>();
        moments = new ArrayList<>();
    }

    public void draw() {
        scaleFactor = beamDrawLengthPx / beamLength;

        // Dibuja la viga (rectángulo)
        drawBeam();

        // Dibuja soportes
        drawSupports();

        // Dibuja fuerzas con flechas y etiqueta
        drawForces();

        // Dibuja momentos (arcos circulares con flecha)
        drawMoments();

        // Dibuja las acotaciones (distancias) debajo del dibujo
        //drawAnnotations();
    }

    // ----------------------------
    // Método para dibujar la viga
    // ----------------------------
    void drawBeam() {
        applyStyle();
        p5.rect(beamX, beamY, beamLength * scaleFactor, beamHeight);
    }

    // ----------------------------
    // Métodos para dibujar soportes
    // ----------------------------
    void drawSupports() {
        // Cada soporte se dibuja en función de su tipo
        for (Elements.Support sp : supports) {
            // Convertimos la posición en metros a píxeles
            float spX = beamX + (float) (sp.position * scaleFactor);
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
                    } else if (Math.abs(sp.position - beamLength) < 0.001) {
                        drawFixedSupport(spX, baseY, false); // extremo derecho
                    } else {
                        // Si se coloca en el centro se puede dibujar por defecto hacia la izquierda
                        drawFixedSupport(spX, baseY, true);
                    }
                    break;
            }
        }
    }

    // Dibuja el soporte tipo PIN: un triángulo con línea horizontal debajo y pequeñas líneas oblicuas
    void drawPinSupport(float x, float baseY) {
        p5.push();

        applyStyle();
        float triangleWidth = supportSize + wheelDiameter;
        x -= triangleWidth / 2;
        float triangleHeight = supportSize + wheelDiameter;

        // Triángulo
        p5.fill(FinalColors.primaryYellow());
        p5.triangle(x + (triangleWidth) / 2, baseY, x,
                baseY + triangleHeight,
                x + triangleWidth, baseY + triangleHeight);

        // Línea horizontal debajo del triángulo
        applyStyle();
        p5.line(x - triangleWidth / 2, baseY + triangleHeight,
                x + triangleWidth + triangleWidth / 2, baseY + triangleHeight);

        p5.pop();
    }

    // Dibuja el soporte tipo ROLLER: similar al PIN, pero con ruedas (círculos pequeños) debajo
    void drawRollerSupport(float x, float baseY) {
        p5.push();
        float triangleWidth = supportSize;
        x -= triangleWidth / 2;
        float triangleHeight = supportSize;
        p5.fill(150);

        // Triángulo
        applyStyle();
        p5.fill(FinalColors.primaryYellow());
        p5.triangle(x + (triangleWidth) / 2, baseY, x,
                baseY + triangleHeight,
                x + triangleWidth, baseY + triangleHeight);

        // Línea horizontal debajo del triángulo
        applyStyle();
        p5.line(x - triangleWidth / 2, baseY + triangleHeight,
                x + triangleWidth + triangleWidth / 2, baseY + triangleHeight);


        // Dibuja ruedas (círculos pequeños)
        p5.fill(FinalColors.primaryYellow());
        p5.ellipseMode(CORNER);
        p5.ellipse(x - triangleWidth / 2, baseY + triangleHeight, wheelDiameter, wheelDiameter);
        p5.ellipseMode(CENTER);
        p5.ellipse(x + triangleWidth / 2, baseY + triangleHeight + wheelDiameter / 2, wheelDiameter, wheelDiameter);
        p5.ellipse(x + triangleWidth + wheelDiameter / 2, baseY + triangleHeight + wheelDiameter / 2,
                wheelDiameter, wheelDiameter);
        p5.pop();
    }

    void drawFixedSupport(float x, float baseY, boolean leftSide) {
        p5.push();
        applyStyle();
        float supportHeight = 100;

        if (leftSide) {
            p5.rect(x-supportHeight/2, baseY - beamHeight - (supportHeight-beamHeight)/2, supportHeight/2, supportHeight);
        } else {
            // supportHeight = beamHeight + 2*b
            p5.rect(x, baseY - beamHeight - (supportHeight-beamHeight)/2, supportHeight/2, supportHeight);
        }
        p5.pop();
    }

    // ----------------------------
    // Método para dibujar fuerzas
    // ----------------------------
    void drawForces() {
        p5.push();
        applyStyle();
        for (Elements.Force f : forces) {
            float forceX = beamX + (float) (f.position * scaleFactor);
            float arrowLength = 80;
            float arrowThickness = 2;
            float arrowHeadSize = 15;

            if (f.isUpward) {
                drawArrowDir(forceX, beamY+arrowLength+arrowHeadSize, BeamDrawing.DIRECTION.UP, arrowLength, arrowThickness, arrowHeadSize);
            } else {
                drawArrowDir(forceX, beamY, BeamDrawing.DIRECTION.DOWN, arrowLength, arrowThickness, arrowHeadSize);
            }

            // Etiqueta de la fuerza
            p5.fill(textColor);
            String forceLabel = "F: " + PApplet.nf(Math.abs((float) f.magnitude), 1, 1) + " N";
            if (f.isUpward) {
                p5.text(forceLabel, forceX - p5.textWidth(forceLabel) / 2, beamY - arrowLength - arrowHeadSize - 5);
            } else {
                p5.text(forceLabel, forceX - p5.textWidth(forceLabel) / 2, beamY + beamHeight + arrowLength + arrowHeadSize + 15);
            }
        }
        p5.pop();
    }

    // ----------------------------
    // Método para dibujar momentos
    // ----------------------------
    void drawMoments() {

        // Se dibuja un arco circular en donde se indica el sentido:

        p5.push();
        applyStyle();
        for (Elements.Moment m : moments) {
            float posX = beamX + (float) (m.position * scaleFactor);
            // Se fija una altura para el centro del arco (por encima de la viga)
            float centerY = beamY - 0;
            float arcRadius = 35;
            float startAngle, stopAngle;
            p5.noFill();
            startAngle = -p5.HALF_PI;
            stopAngle = p5.PI;
            if (m.magnitude > 0) {
                circArrow(posX, centerY, arcRadius, startAngle, stopAngle, 10);
            } else {
                circArrowInv(posX, centerY, arcRadius, startAngle, stopAngle, 10);
            }


            // Dibuja la etiqueta en el centro del arco
            String momentLabel = "M: " + PApplet.nf(Math.abs((float) m.magnitude), 1, 1) + " m";
            p5.fill(0);
            p5.noStroke();
            p5.fill(textColor);
            p5.text(momentLabel, posX - p5.textWidth(momentLabel) / 2, centerY);
        }
        p5.pop();
    }

    /**
     * Dibuja una flecha circular en sentido horario
     * @param x
     * @param y
     * @param radius
     * @param start
     * @param stop
     * @param arrowSize
     */
    void circArrow(float x, float y, float radius, float start, float stop, float arrowSize) {
        p5.ellipseMode(CENTER);
        p5.noFill();
        p5.strokeWeight(2);
        p5.stroke(FinalColors.primaryYellow());

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
     * Lo mismo que circArrow() pero apunta en sentido antihorario
     * @param x
     * @param y
     * @param radius
     * @param start
     * @param stop
     * @param arrowSize
     */
    void circArrowInv(float x, float y, float radius, float start, float stop, float arrowSize) {
        p5.ellipseMode(CENTER);
        p5.noFill();
        p5.stroke(FinalColors.primaryYellow());

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




    // ----------------------------
    // Método para dibujar las acotaciones (dimensiones)
    // ----------------------------
    void drawAnnotations() {
        // Se dibuja una acotación para:
        //   a) Toda la longitud de la viga
        //   b) Cada soporte (si no está en el extremo)
        //   c) Cada fuerza (si no está en el extremo)
        // Las acotaciones se sitúan debajo del dibujo, una debajo de otra.
        float annotationInitialY = beamY + beamHeight + 80;
        float annotationSpacing = 20;
        int annotationIndex = 0;

        // Acotación para toda la viga
        float startX = beamX;
        float endX = beamX + beamLength * scaleFactor;
        float y = annotationInitialY + annotationIndex * annotationSpacing;
        drawDimensionLine(startX, endX, y, beamLength + " m");
        annotationIndex++;

        // Acotaciones para soportes (se omite si el soporte está en un extremo)
        for (Elements.Support sp : supports) {
            if (sp.position <= 0.001 || sp.position >= beamLength - 0.001)
                continue;
            float posX = beamX + (float) (sp.position * scaleFactor);
            y = annotationInitialY + annotationIndex * annotationSpacing;
            drawDimensionLine(startX, posX, y, sp.position + " m");
            annotationIndex++;
        }

        // Acotaciones para fuerzas (omitiendo si se sitúan en un extremo)
        for (Elements.Force f : forces) {
            if (f.position <= 0.001 || f.position >= beamLength - 0.001)
                continue;
            float posX = beamX + (float) (f.position * scaleFactor);
            y = annotationInitialY + annotationIndex * annotationSpacing;
            drawDimensionLine(startX, posX, y, f.position + " m");
            annotationIndex++;
        }
    }

    // Dibuja una línea de dimensión con dos tics verticales en sus extremos y una etiqueta centrada.
    void drawDimensionLine(float startX, float endX, float y, String label) {
        p5.stroke(0);
        // Tics verticales pequeños
        p5.line(startX, y - 5, startX, y + 5);
        p5.line(endX, y - 5, endX, y + 5);
        // Línea horizontal uniendo los tics
        p5.line(startX, y, endX, y);
        // Etiqueta centrada
        float textX = (startX + endX) / 2;
        p5.noStroke();
        p5.fill(0);
        p5.text(label, textX - p5.textWidth(label) / 2, y - 10);
    }

    /**
     * Dibuja una flecha. Ángulos en grados: 0 dibuja la flecha apuntando arriba, 180 hacia abajo.
     * @param startX coordenada x de la base de la flecha
     * @param startY  coordenada y de la base de la flecha
     * @param degrees
     * @param length
     * @param thickness
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
     *
     * @param startX coordenada x de la punta de la flecha
     * @param startY coordenada y de la punta de la flecha
     * @param direction
     * @param length
     * @param thickness
     */
    void drawArrowDir(float startX, float startY, BeamDrawing.DIRECTION direction, float length, float thickness, float triHeight) {

        // Ajustar el ángulo según el valor del enumerado

        switch (direction) {
            case UP -> drawArrow(startX, startY-length-triHeight, 0, length, thickness, triHeight);
            case DOWN -> drawArrow(startX, startY-length-triHeight, 180, length, thickness, triHeight);
            case LEFT -> drawArrow(startX+length+triHeight, startY, 270, length, thickness, triHeight);
            case RIGHT -> drawArrow(startX-length-triHeight, startY, 90, length, thickness, triHeight);
        }

    }


    private void applyStyle() {
        p5.stroke(strokeColor);
        p5.fill(fillColor);
        p5.strokeWeight(strokeW);
        p5.textFont(pFont);
        p5.textSize(fontSize);
    }


    // Setters del control de estilo

    public void setStrokeColor(int c) {
        this.strokeColor = c;
    }
    public void setFillColor(int c) {
        this.fillColor = c;
    }
    public void setTextColor(int c) {
        this.textColor = c;
    }
    public void setStrokeWeight(float w) {
        this.strokeW = w;
    }
    public void setFontSize(int size) {
        this.fontSize = size;
    }

    public void setPFont(PFont pFont) {
        this.pFont = pFont;
    }

}
