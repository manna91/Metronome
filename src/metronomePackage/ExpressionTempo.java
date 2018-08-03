package metronomePackage;

public class ExpressionTempo {

    private String expression;

    public ExpressionTempo(int bpm) {
        if (bpm < 20) expression = new String("larghissimo");
        else if (bpm >= 20 && bpm < 40) this.expression = new String("adagissimo");
        else if (bpm >= 40 && bpm < 60) this.expression = new String("largo");
        else if (bpm >= 60 && bpm < 80) this.expression = new String("adagio");
        else if (bpm >= 80 && bpm < 100) this.expression = new String("moderato");
        else if (bpm >= 100 && bpm < 140) this.expression = new String("allegro");
        else if (bpm >= 140 && bpm < 160) this.expression = new String("vivace");
        else if (bpm >= 160 && bpm < 200) this.expression = new String("presto");
        else if (bpm >= 200) this.expression = new String("prestissimo");
    }

    public String getExpression() {
        return expression;
    }
}
