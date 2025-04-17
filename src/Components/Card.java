package Components;

import Constants.FinalColors;
import Constants.Fonts;
import processing.core.PApplet;
import processing.core.PImage;

import static Constants.Layout.corner;

/**
 * Clase que representa una tarjeta visual (Card) dentro de una galería de simulaciones.
 *
 * <p>Cada tarjeta extiende la funcionalidad básica de {@link Button} e incluye
 * propiedades adicionales como título, fecha de creación, imagen de previsualización
 * y fecha de última modificación.
 *
 * <p>Las tarjetas se utilizan principalmente en {@link Components.Gallery} para mostrar
 * las simulaciones disponibles asociadas al usuario.
 */
public class Card extends Button{
    /** Identificador único del simulador asociado a esta tarjeta. */
    String id;

    /** Título del simulador o del contenido de la tarjeta. */
    String title;

    /** Categoría opcional de la tarjeta (actualmente no utilizada). */
    String category;

    /** Fecha de última modificación del simulador. */
    String lastModified;

    /** Fecha de creación del simulador. */
    String creation;

    /** Imagen mostrada en la parte superior de la tarjeta. */
    PImage image;

    /** Ruta de acceso a la imagen (usada para precargar o guardar). */
    String imgPath;

    /** Altura visual de la imagen en la tarjeta. */
    float imageHeight;

    /** Conjunto de fuentes utilizadas para los textos de la tarjeta. */
    Constants.Fonts appFonts;

    /**
     * Crea una nueva tarjeta visual con dimensiones y posición dadas.
     *
     * @param p5     instancia de Processing
     * @param x      coordenada horizontal
     * @param y      coordenada vertical
     * @param width  ancho de la tarjeta
     * @param height alto total de la tarjeta
     */
    public Card(PApplet p5, float x, float y, float width, float height){
        super(p5, x, y, width, height);
        imageHeight = height/1.5f;
        title = "New Card";
        appFonts = new Fonts(p5);
    }

    /**
     * Carga y construye la imagen de previsualización de la tarjeta.
     *
     * @param p5   instancia de Processing
     * @param path ruta de la imagen
     */
    public void buildImage(PApplet p5, String path){
        p5.noStroke();
        p5.fill(-1);
        p5.background(0);

        p5.rect(this.getX(), this.getY(), this.getWidth(), imageHeight+10,
                corner, corner, 0, 0);

        this.image = p5.loadImage(path);
        image.resize((int) p5.width, p5.height);
        image.mask(p5.get());
    }

    /**
     * Asigna los datos básicos del simulador a la tarjeta.
     *
     * @param strings arreglo con los campos: [id, título, fecha de creación, última modificación]
     */
    public void setInfo(String [] strings){
            System.out.println("Id: "+ id);
            this.id = strings[0];
            this.title = strings[1];
            this.creation = strings[2];
            this.lastModified = strings[3];
            this.imgPath = "";
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageHeight(float height){
        this.imageHeight = height;
    }

    /**
     * Dibuja la tarjeta en pantalla, incluyendo su imagen superior y los textos inferiores
     * (título, fecha de creación y de última modificación).
     *
     * <p>La tarjeta cambia de color al pasar el cursor y muestra el borde activo si está habilitada.
     * La imagen superior y el contenido textual se organizan automáticamente.
     */
    @Override
    public void display() {
        p5.pushStyle();
        if(!isEnabled()){
            p5.fill(fillColorDisabled);
            p5.noStroke();
        }
        else if(mouseOverButton(p5)){
            p5.fill(fillColorOver);
            p5.stroke(strokeWeight); p5.stroke(strokeColorOn);
        }
        else{
            p5.fill(fillColor);          // El mouse está fuera del botón
            p5.noStroke();
        }

        //Propiedades Button

        p5.rect(this.getX(), this.getY()+imageHeight, this.getWidth(), this.getHeight()-imageHeight, corner);

        //Imagen superior
        //p5.image(image, 0,0);
        p5.fill(0, 0);
        p5.stroke(getStrokeColorOn());
        p5.rect(this.getX(), this.getY(), this.getWidth(), imageHeight+10,
                corner, corner, 0, 0);

        //Texto inferior
        p5.fill(FinalColors.textWhite()); p5.textAlign(p5.CENTER); p5.textSize(20);
        p5.textFont(appFonts.fonts[1]); //Change Fonts to static
            //Título
            p5.text(title, getX() + getWidth()/2, getY() + imageHeight + getHeight()/10);
            //Descripción
            p5.textFont(appFonts.fonts[0]); p5.textAlign(p5.LEFT);
            float marginH = 15,  marginV = 10, height = this.getY()+imageHeight + 60;
            //Tiempos
            p5.text("Última modificación: "+this.lastModified,
                    this.getX()+marginH, height+appFonts.fonts[0].getSize()+marginV, this.getWidth(), height);
            p5.text("Creado: "+this.creation,
                    this.getX()+marginH, height, this.getWidth(), height);

        p5.popStyle();
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getCreation() {
        return creation;
    }

    public PImage getImage() {
        return image;
    }

    public String getImgPath() {
        return imgPath;
    }

    public float getImageHeight() {
        return imageHeight;
    }

    public Fonts getAppFonts() {
        return appFonts;
    }
}
