package metronomePackage;

import javafx.scene.image.Image;

public class Note {
    private Image imageNote;

    public Note(int denominator) {
        switch (denominator){
            case 1:
                imageNote=new Image("metronomePackage/images/01.png");
                break;
            case 2:
                imageNote=new Image("metronomePackage/images/02.png");
                break;
            case 4:
                imageNote=new Image("metronomePackage/images/03.png");
                break;
            case 8:
                imageNote=new Image("metronomePackage/images/04.png");
                break;
            case 16:
                imageNote=new Image("metronomePackage/images/05.png");
                break;
            case 32:
                imageNote=new Image("metronomePackage/images/06.png");
                break;
        }
    }

    public Image getImageNote() {
        return imageNote;
    }


}
