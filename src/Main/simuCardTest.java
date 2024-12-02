package Main;

import Components.Simulator;

public class simuCardTest {
    public static void main(String[] args) {
        Simulator simu = new Simulator("SimuA", Simulator.SIMU_TYPE.KINETIC);
        System.out.println(simu.getCreationDateTime());
    }
}
