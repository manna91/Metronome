package metronomePackage;


public class ClickTempoCalculate {
    private Stopper stopper1 = new Stopper();
    private Stopper stopper2 = new Stopper();

    public ClickTempoCalculate() {

    }

    public int timerHandling(int clickSzamlalo) {
        if (clickSzamlalo % 2 != 0) {
            this.stopper1.start();
            if (clickSzamlalo != 1) {
                this.stopper2.end();
                return this.stopper2.getTotalTime();
            }
        }
        else if (clickSzamlalo % 2 ==0) {
            this.stopper1.end();
            this.stopper2.start();
            return this.stopper1.getTotalTime();

        }
        return 0;


    }


}
