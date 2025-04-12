package SimulationEngine;

import processing.core.PApplet;

import java.util.ArrayList;

import static SimulationEngine.Elements.*;

// ***********************
// CLASE PRINCIPAL: SKETCH DE LA VIGA
// ***********************

public class BeamDrawing extends PApplet {

    // Parámetros de escala y posición de la viga
    float scaleFactor = 100;         // 100 píxeles por metro
    float beamX = 100;               // Origen horizontal de la viga
    float beamY = 250;               // Posición vertical (parte superior de la viga)
    float beamLength = 6.0f;         // Longitud de la viga en metros
    float beamHeight = 20;           // Altura en píxeles de la viga
    float supportSize = 20;          //Altura y ancho de los soportes en píxeles

    public enum DIRECTION {UP, DOWN, LEFT, RIGHT} //Dirección de las flechas

    float ang = 90;

    float wheelDiameter = 10;

    // Listas de elementos para la viga
    ArrayList<Force> forces;
    ArrayList<Support> supports;
    ArrayList<Moment> moments;

    public static void main(String[] args) {
        PApplet.main("SimulationEngine.BeamDrawing");
    }

    public void settings() {
        size(1000, 600);
    }

    public void setup() {
        // Inicializamos las listas
        forces = new ArrayList<Force>();
        supports = new ArrayList<Support>();
        moments = new ArrayList<Moment>();

        // ----------------------------
        // Datos de prueba (se pueden modificar)
        // ----------------------------
        // Soportes:
        // Se pueden colocar únicamente dentro de [0, beamLength] (en metros).
        // En este ejemplo colocamos un soporte PIN a 1.5 m,
        // un soporte ROLLER a 4.5 m y un soporte FIXED al extremo derecho (6 m).
        supports.add(new Support(SUPPORT_TYPE.PIN, 1.5));
        supports.add(new Support(SUPPORT_TYPE.ROLLER, 0));
        supports.add(new Support(SUPPORT_TYPE.FIXED, 6));

        // Fuerzas:
        // Se dibujan sobre la viga con una flecha y su etiqueta.
        // La dirección se decide según isUpward (true: flecha hacia arriba, false: flecha hacia abajo)
        forces.add(new Force(200, 2.0, true));
        forces.add(new Force(150, 3.5, false));

        // Momentos:
        // Se representan con un arco circular; el sentido se determina:
        // valor negativo = sentido horario, valor positivo = antihorario.
        moments.add(new Moment(50, 3.0, false));   // Antihorario (positivo)
        moments.add(new Moment(30, 5.0, true));      // Horario (negativo)

        textSize(12);
        smooth();

    }

    public void draw() {
        background(255);
        strokeWeight(1);

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
        fill(200);
        stroke(0);
        rect(beamX, beamY, beamLength * scaleFactor, beamHeight);
    }

    // ----------------------------
    // Métodos para dibujar soportes
    // ----------------------------
    void drawSupports() {
        // Cada soporte se dibuja en función de su tipo
        for (Support sp : supports) {
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
        push();
        float triangleWidth = supportSize + wheelDiameter;
        x -= triangleWidth / 2;
        float triangleHeight = supportSize + wheelDiameter;
        fill(150);
        stroke(0);

        // Triángulo
        triangle(x + (triangleWidth) / 2, baseY, x,
                baseY + triangleHeight,
                x + triangleWidth, baseY + triangleHeight);

        // Línea horizontal debajo del triángulo
        line(x - triangleWidth / 2, baseY + triangleHeight,
                x + triangleWidth + triangleWidth / 2, baseY + triangleHeight);

        // Pequeñas líneas oblicuas en los extremos de la base del triángulo
//        line(x - triangleWidth/2, baseY, x - triangleWidth/2 - 5, baseY + 10);
//        line(x + triangleWidth/2, baseY, x + triangleWidth/2 + 5, baseY + 10);
        pop();
    }

    // Dibuja el soporte tipo ROLLER: similar al PIN, pero con ruedas (círculos pequeños) debajo
    void drawRollerSupport(float x, float baseY) {
        push();
        float triangleWidth = supportSize;
        x -= triangleWidth / 2;
        float triangleHeight = supportSize;
        fill(150);

        // Triángulo
        stroke(0);
        triangle(x + (triangleWidth) / 2, baseY, x,
                baseY + triangleHeight,
                x + triangleWidth, baseY + triangleHeight);

        // Línea horizontal debajo del triángulo
        line(x - triangleWidth / 2, baseY + triangleHeight,
                x + triangleWidth + triangleWidth / 2, baseY + triangleHeight);

        // Líneas oblicuas
//        line(x - triangleWidth / 2, baseY, x - triangleWidth / 2 - 5, baseY + 10);
//        line(x + triangleWidth / 2, baseY, x + triangleWidth / 2 + 5, baseY + 10);

        // Dibuja ruedas (círculos pequeños)
        fill(100);
        ellipseMode(CORNER);
        ellipse(x - triangleWidth / 2, baseY + triangleHeight, wheelDiameter, wheelDiameter);
        ellipseMode(CENTER);
        ellipse(x + triangleWidth / 2, baseY + triangleHeight + wheelDiameter / 2, wheelDiameter, wheelDiameter);
        ellipse(x + triangleWidth + wheelDiameter / 2, baseY + triangleHeight + wheelDiameter / 2,
                wheelDiameter, wheelDiameter);
        pop();
    }

    // Dibuja el soporte tipo FIXED: una línea vertical con pequeñas líneas oblicuas a su lado
    // El parámetro leftSide indica si las pequeñas oblicuas se dibujan a la izquierda (cuando está en el extremo izquierdo)
    void drawFixedSupport(float x, float baseY, boolean leftSide) {
        push();
        float supportHeight = 30;
        stroke(0);
        if (leftSide) {
            rect(x-supportHeight/2, baseY-supportHeight, supportHeight/2, supportHeight+supportHeight/2);
        } else {
            // Líneas oblicuas a la derecha
            rect(x, baseY-supportHeight, supportHeight/2, supportHeight+supportHeight/2);
        }
        pop();
    }

    // ----------------------------
    // Método para dibujar fuerzas
    // ----------------------------
    void drawForces() {
        push();
        for (Force f : forces) {
            float forceX = beamX + (float) (f.position * scaleFactor);
            float arrowLength = 40;
            float arrowThickness = 2;
            float arrowHeadSize = 20;

            if (f.isUpward) {
                drawArrowDir(forceX, beamY+arrowLength+arrowHeadSize, DIRECTION.UP, arrowLength, arrowThickness, arrowHeadSize);
            } else {
                drawArrowDir(forceX, beamY, DIRECTION.DOWN, arrowLength, arrowThickness, arrowHeadSize);
            }

            // Etiqueta de la fuerza
            String forceLabel = "F: " + nf(abs((float) f.magnitude), 1, 1) + " N";
            if (f.isUpward) {
                text(forceLabel, forceX - textWidth(forceLabel) / 2, beamY - arrowLength - arrowHeadSize - 5);
            } else {
                text(forceLabel, forceX - textWidth(forceLabel) / 2, beamY + beamHeight + arrowLength + arrowHeadSize + 15);
            }
        }
        pop();
    }


    // Utilidad: dibuja una flecha desde (x1,y1) hasta (x2,y2)
    void drawArrow(float x1, float y1, float x2, float y2) {
        push();
        stroke(255, 0, 0);
        line(x1, y1, x2, y2);
        // Dibuja la cabeza de la flecha
        float angle = atan2(y1 - y2, x2 - x1);
        translate(x2, y2);
        rotate(angle);
        line(0, 0, -7, -7);
        line(0, 0, -7, 7);
        pop();
    }

    // ----------------------------
    // Método para dibujar momentos
    // ----------------------------
    void drawMoments() {
        // Se dibuja un arco circular en donde se indica el sentido:
        // Si el momento es positivo (antihorario) se dibuja de 0 a PI, y si es negativo (horario) de PI a TWO_PI.
        push();
        for (Moment m : moments) {
            float posX = beamX + (float) (m.position * scaleFactor);
            // Se fija una altura para el centro del arco (por encima de la viga)
            float centerY = beamY - 60;
            float arcRadius = 35;
            float startAngle, stopAngle;
            noFill();
            stroke(0);
            if (m.magnitude > 0) {
                startAngle = 0;
                stopAngle = PI;
                circArrow(posX, centerY, arcRadius, startAngle, stopAngle, 10);
            } else {
                startAngle = PI;
                stopAngle = TWO_PI;
                push();
                scale(1, -1);
                translate(0, -height);
                float randomAdjust = height/2.7f;
                circArrow(posX, centerY+randomAdjust, arcRadius, startAngle, stopAngle, 10);
                pop();
            }


            // Dibuja la etiqueta en el centro del arco
            String momentLabel = "M: " + nf(abs((float) m.magnitude), 1, 1) + " kN*m";
            fill(0);
            noStroke();
            text(momentLabel, posX - textWidth(momentLabel) / 2, centerY);
        }
        pop();
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
        ellipseMode(CENTER);
        noFill();

        arc(x, y, radius * 2, radius * 2, start, stop);

        float arrowX = x + cos(stop) * radius;
        float arrowY = y + sin(stop) * radius;

        float point1X = x + (cos(stop) * radius) + (cos(stop - radians(arrowSize * 5)) * (arrowSize));
        float point1Y = y + (sin(stop) * radius) + (sin(stop - radians(arrowSize * 5)) * (arrowSize));

        float point2X = x + (cos(stop) * radius) + (cos(stop - radians(-arrowSize * 5)) * (-arrowSize));
        float point2Y = y + (sin(stop) * radius) + (sin(stop - radians(-arrowSize * 5)) * (-arrowSize));

        line(arrowX, arrowY, point1X, point1Y);
        line(arrowX, arrowY, point2X, point2Y);
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
        for (Support sp : supports) {
            if (sp.position <= 0.001 || sp.position >= beamLength - 0.001)
                continue;
            float posX = beamX + (float) (sp.position * scaleFactor);
            y = annotationInitialY + annotationIndex * annotationSpacing;
            drawDimensionLine(startX, posX, y, sp.position + " m");
            annotationIndex++;
        }

        // Acotaciones para fuerzas (omitiendo si se sitúan en un extremo)
        for (Force f : forces) {
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
        stroke(0);
        // Tics verticales pequeños
        line(startX, y - 5, startX, y + 5);
        line(endX, y - 5, endX, y + 5);
        // Línea horizontal uniendo los tics
        line(startX, y, endX, y);
        // Etiqueta centrada
        float textX = (startX + endX) / 2;
        noStroke();
        fill(0);
        text(label, textX - textWidth(label) / 2, y - 10);
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

        strokeWeight(thickness); // Grosor de la línea

        float triWidth = 20;

        // Coordenadas del extremo de la línea
        float endX = startX + length * (float) Math.sin(angle);
        float endY = startY - length * (float) Math.cos(angle);

        // Dibujar línea
        line(startX, startY, endX, endY);

        // Coordenadas de los puntos del triángulo
        float tipX = endX + triHeight * (float) Math.sin(angle);
        float tipY = endY - triHeight * (float) Math.cos(angle);
        float leftX = endX - (triWidth / 2) * (float) Math.cos(angle);
        float leftY = endY - (triWidth / 2) * (float) Math.sin(angle);
        float rightX = endX + (triWidth / 2) * (float) Math.cos(angle);
        float rightY = endY + (triWidth / 2) * (float) Math.sin(angle);

        // Dibujar triángulo
        triangle(tipX, tipY, leftX, leftY, rightX, rightY);
    }

    /**
     *
     * @param startX coordenada x de la punta de la flecha
     * @param startY coordenada y de la punta de la flecha
     * @param direction
     * @param length
     * @param thickness
     */
    void drawArrowDir(float startX, float startY, DIRECTION direction, float length, float thickness, float triHeight) {

        // Ajustar el ángulo según el valor del enumerado

        switch (direction) {
            case UP -> drawArrow(startX, startY-length-triHeight, 0, length, thickness, triHeight);
            case DOWN -> drawArrow(startX, startY-length-triHeight, 180, length, thickness, triHeight);
            case LEFT -> drawArrow(startX+length+triHeight, startY, 270, length, thickness, triHeight);
            case RIGHT -> drawArrow(startX-length-triHeight, startY, 90, length, thickness, triHeight);
        }

    }


}