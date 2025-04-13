package SimulationEngine;

public class Elements {
    public static enum SUPPORT_TYPE {PIN, ROLLER, FIXED}

    public static class Force {
        double magnitude; // Magnitud de la fuerza
        double position;  // Posición de la fuerza respecto al origen de la viga
        boolean isUpward; // Dirección de la fuerza: true para hacia arriba, false para hacia abajo
        double angle;     // Ángulo de la fuerza respecto al eje horizontal (en grados)

        public Force(double magnitude, double position, boolean isUpward) {
            this.magnitude = isUpward ? Math.abs(magnitude) : -Math.abs(magnitude);
            this.position = position;
            this.isUpward = isUpward;
            this.angle = 90;
        }

        // Calcular las componentes horizontal y vertical
        double getHorizontalComponent() {
            return magnitude * Math.cos(Math.toRadians(angle));
        }

        double getVerticalComponent() {
            return magnitude * Math.sin(Math.toRadians(angle));
        }

        // Setters

        public void setMagnitude(double magnitude) {
            this.magnitude = magnitude;
        }

        public void setPosition(double position) {
            this.position = position;
        }

        public void setUpward(boolean upward) {
            isUpward = upward;
        }

        public void setAngle(double angle) {
            this.angle = angle;
        }

    }

    /**
     * Momentos que el usuario puede añadir.
     */
    public static class Moment {
        double magnitude;   // Magnitud del momento
        double position;    // Posición del momento respecto al origen de la viga
        boolean isClokwise; // Sentido del momento. Criterio: negativo sentido horario, postivo antihorario

        public Moment(double magnitude, double position, boolean isClokwise) {
            this.magnitude = isClokwise ? -Math.abs(magnitude) : Math.abs(magnitude);
            this.position = position;
        }

        public void setMagnitude(double magnitude) {
            this.magnitude = magnitude;
        }

        public void setPosition(double position) {
            this.position = position;
        }

        public void setClokwise(boolean clokwise) {
            isClokwise = clokwise;
        }
    }

    public static class Support {
        public String id;
        public SUPPORT_TYPE type;      // Tipo de soporte: "fijo", "articulado", "empotrado"
        public double position;  // Posición del soporte respecto al origen de la viga

        public Support(String id, SUPPORT_TYPE type, double position) {
            this.id = id;
            this.type = type;
            this.position = position;
        }
    }
}
