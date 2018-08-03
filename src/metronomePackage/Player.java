package metronomePackage;

import javax.sound.midi.*;

public class Player implements MetaEventListener {

    private static final int END_OF_TRACK_MESSAGE = 47;
    private Sequencer sequencer;
    private boolean loop;

    public Player() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.setSlaveSyncMode(Sequencer.SyncMode.INTERNAL_CLOCK);
            sequencer.setTempoInBPM(80);
            sequencer.open();
            sequencer.addMetaEventListener(this);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void play(Sequence sequence, boolean loop) {

        if (sequencer != null && sequence != null && sequencer.isOpen()) {
            try {
                sequencer.setSequence(sequence);

                if (loop) {
                    sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
                }

                sequencer.start();
                this.loop = loop;

            } catch (InvalidMidiDataException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setBPM(int bpm) {
        sequencer.setTempoInBPM(bpm);
    }

    public void meta(MetaMessage event) {

        if (event.getType() == END_OF_TRACK_MESSAGE) {
            if (sequencer != null && sequencer.isOpen() && loop) {
                sequencer.start();
            }
        }
    }


    public void stop() {

        if (sequencer != null && sequencer.isOpen()) {
            sequencer.stop();
            sequencer.setMicrosecondPosition(0);
        }
    }


    public void close() {
        if (sequencer != null && sequencer.isOpen()) {
            sequencer.close();
        }
    }



}