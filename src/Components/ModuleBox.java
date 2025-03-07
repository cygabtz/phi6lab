package Components;

import processing.core.PApplet;

import java.util.ArrayList;

import static Constants.Layout.vRect;

public class ModuleBox {
    PApplet p5;
    private float x, y, width, height;
    public ArrayList<Components.Module> modules;
    public ArrayList<Components.Module> deAtachedModules;
    public Components.Module draggedModule = null;
    private float offsetX, offsetY;

    //Constants
    private final float margin = 6;

    public ModuleBox(PApplet p5, ArrayList<Components.Module> modules,
                     float x, float y, float width, float height) {
        this.p5 = p5;
        this.modules = modules;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        for (int i=1; i<modules.size(); i++){
            modules.get(i).setY(modules.get(i).getY() + modules.get(i-1).getHeight() + margin);
        }
    }

    public void display(){
        p5.push();
        p5.noFill(); p5.stroke(255);
        //p5.rect(x, y, width, height, Constants.Layout.corner);

        for (Components.Module module : modules) {
            if (module.state == Module.STATE.ATTACHED){
                module.display();
            }
        }

        p5.pop();
    }

//    public boolean isModuleInside(Components.Module module){
//        return  module.getX() >= 0 && module.getY() >= 0     &&
//                                 module.getWidth()  <= width &&
//                module.getY() + module.getHeight()  <= height;
//    }

    public void mousePressed(){
        for (Components.Module module : modules) {
            if (module.isMouseOver()) {
                draggedModule = module;
                offsetX = p5.mouseX - module.getX();
                offsetY = p5.mouseY - module.getY();
                break;
            }
        }
    }

//    public void mouseDragged(){
//        if (draggedModule != null) {
//            draggedModule.setX(p5.mouseX - offsetX);
//            draggedModule.setY(p5.mouseY - offsetY);
//            draggedModule.anchored = false;
//
////            if (!isModuleInside(draggedModule)) {
////                deattachedModules.add(draggedModule);
////                modules.remove(draggedModule);
////            }
//        }
//    }

}
