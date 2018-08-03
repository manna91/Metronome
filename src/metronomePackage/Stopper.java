package metronomePackage;

public class Stopper {

    private long startTime = 0;
    private long endTime = 0;

    public Stopper(){};

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public void end() {
        this.endTime   = System.currentTimeMillis();
    }

    public int getTotalTime() {
        return (int)(this.endTime - this.startTime);
    }

}
