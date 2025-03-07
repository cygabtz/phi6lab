package Main;

import processing.core.PApplet;
import java.util.ArrayList;

public class ModuleBox extends PApplet {
    ArrayList<Module> modules;
    Module draggedModule = null;
    float offsetX, offsetY;

    public static void main(String[] args) {
        PApplet.main("Main.ModuleBox");
    }

    public void settings() {
        size(800, 600);
    }

    public void setup() {
        modules = new ArrayList<>();
        modules.add(new Module(this, "Título 1", 50, 50, 200, 100));
        modules.add(new Module(this, "Título 2", 50, 160, 200, 100));
    }

    public void draw() {
        background(255);
        for (Module module : modules) {
            module.display();
        }
    }

    public void mousePressed() {
        for (Module module : modules) {
            if (module.isMouseOver()) {
                draggedModule = module;
                offsetX = mouseX - module.x;
                offsetY = mouseY - module.y;
                break;
            }
        }
    }

    public void mouseDragged() {
        if (draggedModule != null) {
            draggedModule.x = mouseX - offsetX;
            draggedModule.y = mouseY - offsetY;
        }
    }

    public void mouseReleased() {
        draggedModule = null;
    }

    class Module {
        PApplet parent;
        String title;
        float x, y, w, h;
        boolean isPopup = false;

        Module(PApplet parent, String title, float x, float y, float w, float h) {
            this.parent = parent;
            this.title = title;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        void display() {
            parent.fill(200);
            parent.rect(x, y, w, h);
            parent.fill(0);
            parent.text(title, x + 10, y + 20);
            parent.fill(255, 0, 0);
            parent.rect(x + w - 20, y, 20, 20); // Botón de cerrar
            parent.fill(0, 255, 0);
            parent.rect(x, y, 20, 20); // Botón de desanclar
        }

        boolean isMouseOver() {
            return parent.mouseX > x && parent.mouseX < x + w && parent.mouseY > y && parent.mouseY < y + h;
        }
    }
}

