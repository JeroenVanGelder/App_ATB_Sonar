package Applicatie;

/**
 * Created by ricko on 27-2-2017.
 */

    public class AP1 {
    private final MyAP2 superClass = new MyAP2();
    public int varInt;

    public void openMethod() {
        superClass.openMethod();
    }

    public void secretMethod() {
        superClass.secretMethod();
    }

    private class MyAP2 extends AP2 {
        public void openMethod() {

        }
    }
}


