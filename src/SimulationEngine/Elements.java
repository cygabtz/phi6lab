package SimulationEngine;

/**
 * Clase contenedora de los distintos elementos físicos que pueden actuar sobre una viga:
 * fuerzas, momentos y apoyos.
 */
public class Elements {

    /**
     * Enumeración que representa los tipos de soporte disponibles.
     */
    public static enum SUPPORT_TYPE {PIN, ROLLER, FIXED}

    /**
     * Representa una fuerza puntual aplicada sobre la viga.
     */
    public static class Force {
        /** Magnitud de la fuerza (en N) */
        double magnitude;
        /** Posición de la fuerza respecto al origen de la viga (en m) */
        double position;
        /** Dirección de la fuerza: true para hacia arriba, false hacia abajo */
        boolean isUpward;
        /** Ángulo de aplicación respecto al eje horizontal (en grados) */
        double angle;

        /**
         * Constructor para una fuerza puntual vertical.
         * @param magnitude magnitud de la fuerza
         * @param position posición sobre la viga
         * @param isUpward true si es hacia arriba
         */
        public Force(double magnitude, double position, boolean isUpward) {
            this.magnitude = isUpward ? Math.abs(magnitude) : -Math.abs(magnitude);
            this.position = position;
            this.isUpward = isUpward;
            this.angle = 90;
        }

        /**
         * Devuelve la componente horizontal de la fuerza.
         * @return componente horizontal
         */
        double getHorizontalComponent() {
            return magnitude * Math.cos(Math.toRadians(angle));
        }

        /**
         * Devuelve la componente vertical de la fuerza.
         * @return componente vertical
         */
        double getVerticalComponent() {
            return magnitude * Math.sin(Math.toRadians(angle));
        }

        // Setters

        /**
         * Establece la magnitud de la fuerza.
         * @param magnitude nueva magnitud
         */
        public void setMagnitude(double magnitude) {
            this.magnitude = magnitude;
        }

        /**
         * Establece la posición de la fuerza.
         * @param position nueva posición
         */
        public void setPosition(double position) {
            this.position = position;
        }

        /**
         * Establece si la fuerza es hacia arriba.
         * @param upward true si es hacia arriba
         */
        public void setUpward(boolean upward) {
            isUpward = upward;
        }

        /**
         * Establece el ángulo de la fuerza.
         * @param angle nuevo ángulo
         */
        public void setAngle(double angle) {
            this.angle = angle;
        }

        // Getters

        /** @return magnitud de la fuerza */
        public double getMagnitude() { return magnitude; }

        /** @return posición de la fuerza */
        public double getPosition() { return position; }

        /** @return true si es hacia arriba */
        public boolean isUpward() { return isUpward; }

        /** @return ángulo de la fuerza */
        public double getAngle() { return angle; }
    }

    /**
     * Representa un momento puntual aplicado sobre la viga.
     */
    public static class Moment {
        /** Magnitud del momento (Nm) */
        double magnitude;
        /** Posición del momento sobre la viga (m) */
        double position;
        /** Sentido del momento: true si es horario */
        boolean isClokwise;

        /**
         * Constructor del momento.
         * @param magnitude magnitud
         * @param position posición
         * @param isClokwise true si horario
         */
        public Moment(double magnitude, double position, boolean isClokwise) {
            this.magnitude = isClokwise ? -Math.abs(magnitude) : Math.abs(magnitude);
            this.position = position;
        }

        // Setters

        /** @param magnitude nueva magnitud */
        public void setMagnitude(double magnitude) { this.magnitude = magnitude; }

        /** @param position nueva posición */
        public void setPosition(double position) { this.position = position; }

        /** @param clokwise true si horario */
        public void setClokwise(boolean clokwise) { isClokwise = clokwise; }

        // Getters

        /** @return magnitud del momento */
        public double getMagnitude() { return magnitude; }

        /** @return posición del momento */
        public double getPosition() { return position; }

        /** @return true si es horario */
        public boolean isClokwise() { return isClokwise; }
    }

    /**
     * Representa un soporte (apoyo) mecánico sobre la viga.
     */
    public static class Support {
        /** Identificador del soporte (e.g. "A", "B") */
        public String id;
        /** Tipo de soporte */
        public SUPPORT_TYPE type;
        /** Posición del soporte respecto al origen de la viga */
        public double position;

        /**
         * Constructor del soporte.
         * @param id identificador
         * @param type tipo de soporte
         * @param position posición sobre la viga
         */
        public Support(String id, SUPPORT_TYPE type, double position) {
            this.id = id;
            this.type = type;
            this.position = position;
        }
    }
}