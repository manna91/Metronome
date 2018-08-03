package metronomePackage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //<editor-fold desc="GUI elements">
    @FXML
    Pane mainPane;
    @FXML
    Spinner<Integer> bpmSpinner;
    @FXML
    Label expressionLbl;
    @FXML
    Button clickTempoBtn;
    @FXML
    ComboBox<Integer> numberOfBeatsComboBox;
    @FXML
    ComboBox<Integer> typeOfBeatComboBox;
    @FXML
    ComboBox<String> soundComboBox;
    @FXML
    Slider pitchSlider;
    @FXML
    Slider volumeSlider;
    @FXML
    HBox viewNotesHbox;
    @FXML
    ComboBox<String> baseNoteBox;
    @FXML
    Button stopBtn;
    @FXML
    Button playBtn;
    @FXML
    Button saveBtn;
    @FXML
    Pane popupPane;
    @FXML
    TextField nameSetsInput;
    @FXML
    Button savePopupBtn;
    @FXML
    ComboBox<String> savedParamatersComboBox;
    @FXML
    Pane overWritePane;
    @FXML
    Button yesBtn;
    @FXML
    Button noBtn;

    //</editor-fold>

    private Integer clickCounter = 0;
    private int volume;
    private int tempo;
    private int typeOfBeat;
    private int noteNumber;
    private int instrumentNumber;
    private double factor;
    private Metronome metronome;
    private ClickTempoCalculate calculate = new ClickTempoCalculate();
    private ArrayList<Parameters> parameters;
    private DataBaseConnect dataBaseConnect;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stopBtn.setDisable(true);
        uploadMeasure();
        baseNoteBox.setButtonCell(new StatusListCell());
        baseNoteBox.getSelectionModel().select(2);
        bpmSpinner.getValueFactory().setValue(80);
        uploadTimeSignature();
        expressionKiir();
        clickCounter = 0;
        clickTempoBtn.setOnAction(this::clickTempoAction);
        numberOfBeatsComboBox.getSelectionModel().select(3);
        typeOfBeatComboBox.getSelectionModel().select(2);
        notesDrawing();
        soundComboBox.getItems().addAll("Woodblock", "Rain", "Kalimba", "Xylophone", "Agogo", "Melodic Tom");
        soundComboBox.getSelectionModel().selectFirst();
        volumeSlider.setMin(0);
        volumeSlider.setMax(127);
        volumeSlider.setValue(100);

        pitchSlider.setMin(0);
        pitchSlider.setMax(127);
        pitchSlider.setValue(60);

        updateParametersComboBox();
        selectParameters();

    }

    private class StatusListCell extends ListCell<String> {
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(null);
            setText(null);
            if(item!=null){
                ImageView imageView = new ImageView(new Image(item));
                imageView.setFitHeight(30);
                imageView.setFitWidth(18);
                setGraphic(imageView);
            }
        }
    }

    private void uploadMeasure(){
        String egesz = "metronomePackage/images/01.png";
        String fel = "metronomePackage/images/02.png";
        String negyed = "metronomePackage/images/03.png";
        String nyolcad = "metronomePackage/images/04.png";
        String tizenhatod = "metronomePackage/images/05.png";
        String harmincketted = "metronomePackage/images/06.png";
        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll(egesz, fel, negyed, nyolcad, tizenhatod, harmincketted);
        baseNoteBox.getItems().addAll(options);
        baseNoteBox.setCellFactory(c->new StatusListCell());
    }

    private double baseFactorValue(int index){
        switch (index){
            case 0:
                factor=1;
                break;
            case 1:
                factor = 1.0/2.0;
                break;
            case 2:
                factor = 1.0/4.0;
                break;
            case 3:
                factor = 1.0/8.0;
                break;
            case 4:
                factor = 1.0/16.0;
                break;
            case 5:
                factor = 1.0/32.0;
                break;
        }
        return factor;
    }

    private void uploadTimeSignature() {
        for (int i = 1; i <= 36; i++) {
            numberOfBeatsComboBox.getItems().add(i);
        }
        typeOfBeatComboBox.getItems().addAll(1, 2, 4, 8, 16, 32);
    }

    private void notesImages(Note note) {
        for (int i = 0; i < numberOfBeatsComboBox.getValue(); i++) {
            if (numberOfBeatsComboBox.getValue() < 8) {
                ImageView imageView = new ImageView(note.getImageNote());
                imageView.setFitWidth(30);
                imageView.setFitHeight(50);
                viewNotesHbox.getChildren().add(imageView);
            } else if (numberOfBeatsComboBox.getValue() >= 8) {
                ImageView imageView = new ImageView(note.getImageNote());
                imageView.setFitWidth(500 / numberOfBeatsComboBox.getValue() * 0.9);
                imageView.setFitHeight(500 / numberOfBeatsComboBox.getValue() * 0.9);
                viewNotesHbox.setSpacing(500 / numberOfBeatsComboBox.getValue() * 0.1);
                viewNotesHbox.getChildren().add(imageView);
            }
        }

    }

    private void notesDrawing() {
        Note note = new Note(typeOfBeatComboBox.getValue());
        notesImages(note);

        typeOfBeatComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                viewNotesHbox.getChildren().clear();
                Note note = new Note(newValue);

                notesImages(note);
            }
        }
        );
        numberOfBeatsComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                viewNotesHbox.getChildren().clear();
                Note note = new Note(typeOfBeatComboBox.getValue());

                notesImages(note);
            }
        });

    }

    private void expressionKiir() {
        ExpressionTempo expressionTempo = new ExpressionTempo(bpmSpinner.getValue());
        expressionLbl.setText(expressionTempo.getExpression());
        bpmSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {

                ExpressionTempo expression = new ExpressionTempo(newValue);

                expressionLbl.setText(expression.getExpression());
            }
        });

    }

    private int instrumentSet(String instrumentName){
        switch (instrumentName){
            case "Woodblock":
                instrumentNumber = 115;
                break;
            case "Rain":
                instrumentNumber = 96;
                break;
            case "Kalimba":
                instrumentNumber = 108;
                break;
            case "Xylophone":
                instrumentNumber = 13;
                break;
            case "Agogo":
                instrumentNumber = 113;
                break;
            case "Melodic Tom":
                instrumentNumber = 117;
                break;
        }
        return instrumentNumber;

    }

    @FXML
    private void clickTempoAction(ActionEvent event) {
        this.clickCounter++;
        Integer measuredTime = calculate.timerHandling(clickCounter);
        bpmSpinner.getValueFactory().setValue(60000/measuredTime);

    }

    @FXML
    private void playAction(ActionEvent event) {
        stopBtn.setDisable(false);
        volume = (int) volumeSlider.getValue();
        tempo =  bpmSpinner.getValue();
        typeOfBeat = typeOfBeatComboBox.getValue();
        noteNumber = (int) pitchSlider.getValue();
        instrumentNumber =instrumentSet(soundComboBox.getValue());
        factor=baseFactorValue(baseNoteBox.getSelectionModel().getSelectedIndex());

        metronome=new Metronome(tempo, factor, typeOfBeat, volume, noteNumber, instrumentNumber);
        metronome.start();

        playBtn.setDisable(true);
        baseNoteBox.setDisable(true);
        baseNoteBox.setOpacity(1);
        bpmSpinner.setDisable(true);
        bpmSpinner.setOpacity(1);
        clickTempoBtn.setDisable(true);
        clickTempoBtn.setOpacity(1);
        numberOfBeatsComboBox.setDisable(true);
        numberOfBeatsComboBox.setOpacity(1);
        typeOfBeatComboBox.setDisable(true);
        typeOfBeatComboBox.setOpacity(1);
        soundComboBox.setDisable(true);
        soundComboBox.setOpacity(1);
        pitchSlider.setDisable(true);
        pitchSlider.setOpacity(1);
        volumeSlider.setDisable(true);
        volumeSlider.setOpacity(1);
        savedParamatersComboBox.setDisable(true);
        savedParamatersComboBox.setOpacity(1);
        saveBtn.setDisable(true);
        saveBtn.setOpacity(1);

    }

    @FXML
    private void stopAction(ActionEvent event){
        metronome.stop();
        stopBtn.setDisable(true);
        playBtn.setDisable(false);
        baseNoteBox.setDisable(false);
        bpmSpinner.setDisable(false);
        clickTempoBtn.setDisable(false);
        numberOfBeatsComboBox.setDisable(false);
        typeOfBeatComboBox.setDisable(false);
        soundComboBox.setDisable(false);
        pitchSlider.setDisable(false);
        volumeSlider.setDisable(false);
        savedParamatersComboBox.setDisable(false);
        saveBtn.setDisable(false);
    }

    @FXML
    private void save(ActionEvent event){
        popupPane.setVisible(true);
        popupPane.setDisable(false);
        popupPane.setOpacity(1);
        mainPane.setDisable(true);
        mainPane.setOpacity(0.7);
        viewNotesHbox.setVisible(false);
        nameSetsInput.clear();
    }

    @FXML
    private void savePopup(ActionEvent event){
        dataBaseConnect = new DataBaseConnect();
        ArrayList<Parameters> parametersList = new ArrayList<>();
        parametersList = dataBaseConnect.getAllParameters();

        String name =nameSetsInput.getText();
        boolean nameExist=false;
        for(int i=0; i<parametersList.size(); i++){
            if(parametersList.get(i).getName().matches(name)){
                nameExist=true;
            }
        }

        if(nameExist==false){
            Parameters parameters=new Parameters(name, baseNoteBox.getSelectionModel().getSelectedIndex(), bpmSpinner.getValue(),
                    numberOfBeatsComboBox.getSelectionModel().getSelectedIndex(), typeOfBeatComboBox.getSelectionModel().getSelectedIndex(),
                    soundComboBox.getSelectionModel().getSelectedIndex(),
                    (int)pitchSlider.getValue(), (int)volumeSlider.getValue());
            dataBaseConnect.addParameters(parameters);
            connCloseAndReset();
        }
        else{
            overWritePane.setVisible(true);
            popupPane.setDisable(true);
            popupPane.setOpacity(0.7);
        }
    }

    private void connCloseAndReset(){
        popupPane.setVisible(false);
        overWritePane.setVisible(false);
        mainPane.setDisable(false);
        mainPane.setOpacity(1);
        viewNotesHbox.setVisible(true);
        dataBaseConnect.closeConnection();
        updateParametersComboBox();
    }

    @FXML
    private void yesAnswer(ActionEvent event){
        dataBaseConnect.recordDelete(nameSetsInput.getText());
        Parameters parameters=new Parameters(nameSetsInput.getText(), baseNoteBox.getSelectionModel().getSelectedIndex(), bpmSpinner.getValue(),
                numberOfBeatsComboBox.getValue(), typeOfBeatComboBox.getValue(), soundComboBox.getSelectionModel().getSelectedIndex(),
                (int)pitchSlider.getValue(), (int)volumeSlider.getValue());
        dataBaseConnect.addParameters(parameters);
        connCloseAndReset();
    }

    @FXML
    private void noAnswer(ActionEvent event){
        dataBaseConnect.closeConnection();
        overWritePane.setVisible(false);
        popupPane.setVisible(true);
        popupPane.setDisable(false);
        popupPane.setOpacity(1);

    }

    private void updateParametersComboBox(){
        savedParamatersComboBox.getItems().clear();
        dataBaseConnect = new DataBaseConnect();
        ArrayList<Parameters> list = new ArrayList<>();
        list = dataBaseConnect.getAllParameters();
        for(int i=0; i<list.size(); i++){
            savedParamatersComboBox.getItems().add(list.get(i).getName());
        }
        dataBaseConnect.closeConnection();
    }

    private void selectParameters(){
        savedParamatersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                dataBaseConnect = new DataBaseConnect();
                Parameters newParams = new Parameters();
                newParams = dataBaseConnect.getParameters(newValue);
                baseNoteBox.getSelectionModel().select(newParams.getFactor());
                bpmSpinner.getValueFactory().setValue(newParams.getBpm());
                numberOfBeatsComboBox.getSelectionModel().select(newParams.getNumberOfBeats());
                typeOfBeatComboBox.getSelectionModel().select(newParams.getTypeOfBeats());
                soundComboBox.getSelectionModel().select(newParams.getSound());
                pitchSlider.setValue((double)newParams.getPitch());
                volumeSlider.setValue((double)newParams.getVolume());
                dataBaseConnect.closeConnection();
            }
        });

    }
}

