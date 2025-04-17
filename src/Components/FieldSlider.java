package Components;

import processing.core.PApplet;

/**
 * Componente compuesto que vincula un campo de texto numérico ({@link TextField})
 * con un deslizador interactivo ({@link Slider}) para sincronizar entrada textual y gráfica.
 *
 * <p>Permite al usuario introducir un valor mediante teclado o arrastrar el deslizador.
 * Cualquier cambio en uno de los dos elementos se refleja automáticamente en el otro.
 *
 * <p>Muy utilizado en pantallas como {@code SimulatorScreen} para controlar posiciones,
 * longitudes o magnitudes físicas de forma intuitiva.
 */
public class FieldSlider implements Cloneable {
    /**
     * Campo de texto asociado al valor del deslizador.
     */
    public TextField textField;

    /**
     * Deslizador gráfico que refleja el valor introducido.
     */
    public Slider slider;

    /**
     * Instancia de Processing para gestionar eventos y renderizado.
     */
    PApplet p5;

    /**
     * Crea un nuevo {@code FieldSlider} compuesto por un campo de texto y un deslizador sincronizados.
     *
     * @param p5        instancia de Processing utilizada para renderizado e interacción
     * @param textField campo de texto que representa el valor editable
     * @param slider    deslizador gráfico vinculado al campo
     */
    public FieldSlider(PApplet p5, TextField textField, Slider slider) {
        this.p5 = p5;
        this.textField = textField;
        this.slider = slider;
    }

    /**
     * Procesa la entrada de teclado cuando el usuario interactúa con el campo de texto o el deslizador.
     *
     * <p>Actualiza el valor del {@code Slider} si el texto introducido es válido
     * y está dentro del rango permitido. Se admite el uso de comas como separador decimal.
     *
     * @param key     carácter introducido
     * @param keyCode código de la tecla presionada
     */
    public void keyPressed(char key, int keyCode) {
        if (textField.mouseOverTextField(p5) || slider.isMouseOverSlider()) {
            textField.keyPressed(key, keyCode);
            float val;
            try {
                val = Float.parseFloat(textField.getText().replace(',', '.')); // Soporte para coma como decimal
            } catch (NumberFormatException e) {
                val = slider.minValue;
            }
            if (val >= slider.minValue && val <= slider.maxValue) {
                slider.setValueAt((int) val);
            }
        }
    }

    /**
     * Detecta si el usuario ha hecho clic sobre el campo de texto o el deslizador.
     * En tal caso, activa ambos para que respondan a eventos de entrada.
     */
    public void mousePressed() {
        if (textField.mouseOverTextField(p5) || slider.isMouseOverSlider()) {
            this.textField.mousePressed();
            this.slider.mousePressed();
        }
    }

    /**
     * Libera el control del deslizador y actualiza el campo de texto con el valor final,
     * formateado con dos decimales.
     */
    public void mouseReleased() {
        this.slider.mouseReleased();
        String number = String.format("%.2f", this.slider.value);
        this.textField.setText(number);
    }

    /**
     * Crea una copia exacta del {@code FieldSlider}, incluyendo una clonación profunda
     * de sus componentes internos ({@code TextField} y {@code Slider}).
     *
     * @return nueva instancia de {@code FieldSlider} clonada
     */
    @Override
    public FieldSlider clone() {
        // Clonamos los objetos asociados
        TextField clonedTextField = this.textField.clone();
        Slider clonedSlider = this.slider.clone();
        // Creamos una nueva instancia de FieldSlider
        return new FieldSlider(this.p5, clonedTextField, clonedSlider);
    }

    /**
     * Establece un nuevo valor para el componente, sincronizando tanto el campo de texto
     * como el deslizador.
     *
     * <p>Si el valor está fuera del rango permitido, se ajusta al mínimo o máximo.
     *
     * @param val nuevo valor a establecer
     */
    public void setValue(float val) {
        // Asegura que el valor esté dentro del rango permitido
        val = Math.max(slider.minValue, Math.min(slider.maxValue, val));

        // Actualiza el slider
        slider.setValueAt(val);

        // Actualiza el textField con dos decimales
        textField.setText(String.format("%.2f", val));
    }

}
