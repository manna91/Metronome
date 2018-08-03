package metronomePackage;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Metronome {

    private Sequence sequence;
    private Player player;

    public Metronome(int bpm, double baseNote_szorzotenyezo, int nevezo, int vol, int notenumber, int instrument){
        init(bpm, baseNote_szorzotenyezo, nevezo, vol, notenumber, instrument);

    }

    private int calculate(int bpm, double factor, int nevezo){
        double wholePeriodTime = 60000.0/((double)bpm*factor);
        double periodTime = wholePeriodTime/(double) nevezo;

        double calculatedBpm=60000.0/periodTime + 0.5;

        return (int) calculatedBpm;

    }

    private void init(int bpm, double baseNote_szorzotenyezo, int denominator, int vol, int notenumber, int instrument) {

        int ticksPerBeat = 500000;
        int instrument_channel = 0;
        ShortMessage noteOnMsg = new ShortMessage();
        ShortMessage noteOffMsg = new ShortMessage();
        ShortMessage instrumentChangeMsg = new ShortMessage();
        Track track = null;

        try {
            sequence = new Sequence(Sequence.PPQ, ticksPerBeat);
            track = sequence.createTrack();

            instrumentChangeMsg.setMessage(ShortMessage.PROGRAM_CHANGE, instrument_channel, instrument, 0);
            noteOnMsg.setMessage(ShortMessage.NOTE_ON, instrument_channel, notenumber,
                    vol);
//            noteOnMsg.setMessage(0x90, 40, 0x7f);
            noteOffMsg.setMessage(ShortMessage.NOTE_OFF, instrument_channel,
                    notenumber, vol);
//            noteOffMsg.setMessage(0x80, 40, 0x7f);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        track.add(new MidiEvent(instrumentChangeMsg, 0));
        track.add(new MidiEvent(noteOnMsg, 10000));
        track.add(new MidiEvent(noteOffMsg, ticksPerBeat));

        player = new Player();
        player.setBPM(calculate(bpm, baseNote_szorzotenyezo, denominator));
    }

    public void start() {
        player.play(sequence, true);

    }

    public void stop() {
        player.stop();
    }



}
