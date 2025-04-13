package Components;

import processing.core.PApplet;

public class FieldSlider implements Cloneable {
    public TextField textField;
    public Slider slider;
    PApplet p5;

    /**
     * Crea un campo de texto numérico con un deslizable vinculado.
     * Incorpora un {@link TextField} que modifica su valor <code>text</code> y al mismo tiempo
     * el valor <code>value</code> del {@link Slider}
     *
     * @param p5
     * @param textField instancia del campo de texto <code>TextField</code>
     * @param slider    instancia de un deslizable <code>Slider</code>
     */
    public FieldSlider(PApplet p5, TextField textField, Slider slider) {
        this.p5 = p5;
        this.textField = textField;
        this.slider = slider;
    }

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
                slider.setValueAt((int)val);
            }
        }
    }


    public void mousePressed() {
        if (textField.mouseOverTextField(p5) || slider.isMouseOverSlider()) {
            this.textField.mousePressed();
            this.slider.mousePressed();
        }
    }

    public void mouseReleased() {
        this.slider.mouseReleased();
        String number = String.format("%.2f", this.slider.value);
        this.textField.setText(number);
    }

    @Override
    public FieldSlider clone() {
        // Clonamos los objetos asociados
        TextField clonedTextField = this.textField.clone();
        Slider clonedSlider = this.slider.clone();
        // Creamos una nueva instancia de FieldSlider
        return new FieldSlider(this.p5, clonedTextField, clonedSlider);
    }

    public void setValue(float val) {
        // Asegura que el valor esté dentro del rango permitido
        val = Math.max(slider.minValue, Math.min(slider.maxValue, val));

        // Actualiza el slider
        slider.setValueAt(val);

        // Actualiza el textField con dos decimales
        textField.setText(String.format("%.2f", val));
    }


}
