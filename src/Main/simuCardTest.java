package Main;

import Components.Simulator;

public class simuCardTest {
    public static void main(String[] args) {
        Simulator simu = new Simulator("SimuA");
        System.out.println(simu.getCreationDateTime());
    }
}
