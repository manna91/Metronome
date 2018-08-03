package metronomePackage;

public class Parameters {
    private String name;
    private int factor;
    private int bpm;
    private int numberOfBeats;
    private int typeOfBeats;
    private int sound;
    private double pitch;
    private double volume;

    public Parameters(){

    }

    public Parameters(String name, int factor, int bpm, int numberOfBeats, int typeOfBeats, int sound, double pitch, double volume){
        this.name=name;
        this.factor=factor;
        this.bpm=bpm;
        this.numberOfBeats=numberOfBeats;
        this.typeOfBeats=typeOfBeats;
        this.sound=sound;
        this.pitch=pitch;
        this.volume=volume;
    }

    public String getName() {
        return name;
    }

    public int getFactor() {
        return factor;
    }

    public int getBpm() {
        return bpm;
    }

    public int getNumberOfBeats() {
        return numberOfBeats;
    }

    public int getTypeOfBeats() {
        return typeOfBeats;
    }

    public int getSound() {
        return sound;
    }

    public double getPitch() {
        return pitch;
    }

    public double getVolume() {
        return volume;
    }
}
