package jpp.tcrush.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import jpp.tcrush.gamelogic.Level;
import jpp.tcrush.gamelogic.Shape;
import jpp.tcrush.gamelogic.field.CELL;
import jpp.tcrush.gamelogic.field.GameFieldElement;
import jpp.tcrush.gamelogic.utils.Coordinate2D;
import jpp.tcrush.gamelogic.utils.Move;
import jpp.tcrush.parse.LevelParser;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import  javafx.scene.paint.Color;
import javafx.beans.value.ChangeListener;

import javax.sound.midi.Soundbank;


public class TCrushGui extends Application implements EventHandler<ActionEvent> {

    List<String> bottomShapeList = new ArrayList<>();
    String Level2 = "TCrush-LevelDefinition:\n" +
            "bb##\n" +
            "+bb#\n" +
            "oorr\n" +
            "orr#\n" +
            "\n" +
            "Shapes:\n" +
            "1:(0,0);(0,1);(-1,1);(1,0);\n" +
            "1:(0,0);(0,1);(1,1);(-1,0);\n" +
            "1:(0,0);(1,0);(0,-1);";
    InputStream stream = new ByteArrayInputStream(Level2.getBytes(StandardCharsets.UTF_8));
    Level aktuellesLevel = LevelParser.parseStreamToLevel(stream);
    javafx.scene.control.ScrollPane scrollPane;

    GraphicsContext gc;

    int AusgewahltesElement = 0;

    int WidthPlusHeightWidth = 900;


    Canvas mycanvas;
    Canvas mycanvasBottom = new Canvas(2000,100);

    Map<Coordinate2D, GameFieldElement> fieldMap;
    List<GameFieldElement> gameFieldElementList = new ArrayList<>();


    FileChooser fileChooser = new FileChooser();

    String Level;
    String LevelOrginal = "TCrush-LevelDefinition:\n" +
            "bb##\n" +
            "+bb#\n" +
            "oorr\n" +
            "orr#\n" +
            "\n" +
            "Shapes:\n" +
            "1:(0,0);(0,1);(-1,1);(1,0);\n" +
            "1:(0,0);(0,1);(1,1);(-1,0);\n" +
            "1:(0,0);(1,0);(0,-1);";

    Button levelLaden;
    Button levelSpeichern;
    Button levelZurucksetzen;
    Stage window;

    BorderPane borderPane;

    Button pointbutton;

    Group bottomGroup = new Group();

    Button btn;
    GridPane gridPane = new GridPane();

    List<Canvas> canvasListCenter = new ArrayList<>();
    List<Button> buttonList = new ArrayList<>();


    Button labelAusgewähltImBottom = new Button("Element 0 ausgewaehlt");

    VBox v = new VBox();
    HBox h = new HBox();

    Label gewonnen;

    Button erklaerungsbutton = new Button("Erklaerung");

    List<Button> BottomButtonList = new ArrayList<>();
    boolean wurdeGecklickt2 = false;
    int AbstandHGap = 6;
    int AbstandVGap = 6;



    int wurdeGecklickt = 0;

    GridPane gridpaneBottom = new GridPane();


    public static void main(String[] args) {
        launch(args);
        System.out.println("Hello");

    }

    @Override
    public void start(Stage window) throws Exception {





        gridPane.setAlignment(Pos.CENTER);


        window.setTitle("TCrush");
        HBox obereButtonReihe = new HBox(30);
        levelLaden = new Button("Level laden");

        /*levelLaden.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });*/
        //levelLaden.setOnAction(this);
        erklaerungsbutton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        final Stage dialog = new Stage();
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(window);
                        VBox dialogVbox = new VBox(20);
                        dialogVbox.getChildren().add(new Text("Transparent = Block\nKreis mit Pink = Fallthrough\nBittet startet die Applikation neu nach den You Won / You Lost Status\nDas Auseinandergehen der Nodes soll resizable bedeuten\nViel Spaß ;)"));
                        Scene dialogScene = new Scene(dialogVbox, 300, 200);
                        dialog.setScene(dialogScene);
                        dialog.show();
                    }
                }
        );

        levelLaden.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(window);
                        if (file != null) {

                            for (int i = 0; i < aktuellesLevel.getHeight(); i++) {
                                for (int j = 0; j < aktuellesLevel.getWidth(); j++) {
                                    gridPane.getChildren().remove(buttonList.get(j+i* aktuellesLevel.getWidth()));
                                }
                            }

                            try {
                                Level = Files.readString(Paths.get(file.getPath()), StandardCharsets.US_ASCII);
                                LevelOrginal = Files.readString(Paths.get(file.getPath()), StandardCharsets.US_ASCII);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            gameFieldElementList.clear();
                            canvasListCenter.clear();
                            buttonList.clear();



                            InputStream stream = new ByteArrayInputStream(Level.getBytes(StandardCharsets.UTF_8));
                            aktuellesLevel = LevelParser.parseStreamToLevel(stream);
                            System.out.println(Level);
                            fieldMap = aktuellesLevel.fieldMap;
                            int height = aktuellesLevel.getHeight();
                            int width = aktuellesLevel.getWidth();
                            for (int i = 0; i < height; i++) {
                                for (int j = 0; j < width; j++) {
                                    gameFieldElementList.add(fieldMap.get(new Coordinate2D(j,i)));
                                }
                            }
                            System.out.println(gameFieldElementList);

                            ovalWithRectangle(aktuellesLevel.getHeight(), aktuellesLevel.getWidth(), gameFieldElementList);
                            borderPane.setCenter(gridPane);
                            bottomShapes();
                            btn.setGraphic(mycanvasBottom);
                            HBox h = new HBox();
                            scrollPane.setContent(btn);
                            h.setAlignment(Pos.BOTTOM_LEFT);
                            BottomButtonList.clear();
                            BottomButtonList.add(new Button(" "));
                            h.getChildren().add(BottomButtonList.get(0));

                            for (int i = 0; i < aktuellesLevel.getAllowedShapes().size(); i++) {
                                BottomButtonList.add(new Button(""+aktuellesLevel.getAllowedShapes().get(i).amount));
                                h.getChildren().add(BottomButtonList.get(i+1));
                            }
                            h.setSpacing(90);
                            VBox v = new VBox(scrollPane, h);
                            borderPane.setBottom(v); // wenn ich hier Hbox eingebe funkt hier nichts mehr

                        }
                    }
                });
        levelSpeichern = new Button("Level speichern");
        levelSpeichern.setOnAction(event ->  {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(window);

            if (file != null) {
                saveTextToFile(Level, file);
            }
        });
        levelZurucksetzen = new Button("Level zurucksetzen");
        //levelZurucksetzen.setOnAction(event ->  {zurücksetzenLevel();});
        levelZurucksetzen.setOnAction(this);

        obereButtonReihe.setPadding(new Insets(15,5,5,5));
        obereButtonReihe.setAlignment(Pos.BASELINE_CENTER);
        //obereButtonReihe.setStyle("-fx-background-color: #75816b;");
        obereButtonReihe.getChildren().addAll(levelLaden, levelSpeichern, levelZurucksetzen, labelAusgewähltImBottom, erklaerungsbutton);




        borderPane = new BorderPane();
        borderPane.setTop(obereButtonReihe);

        fieldMap = aktuellesLevel.fieldMap;
        int height = aktuellesLevel.getHeight();
        int width = aktuellesLevel.getWidth();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                gameFieldElementList.add(fieldMap.get(new Coordinate2D(j,i)));
            }
        }

        ovalWithRectangle(aktuellesLevel.getHeight(), aktuellesLevel.getWidth(), gameFieldElementList);
        /*drawCircleWithRectangle(0,0,gc, Color.GREEN);
        //todo wenn es kein Item für die Cell gibt dann ist Color = Black
        drawCircleWithRectangle(1,0,gc, Color.BLACK);*/

        scrollPane = new javafx.scene.control.ScrollPane();




        bottomShapes();
        btn = new Button();
        btn.setGraphic(mycanvasBottom);

        scrollPane.setContent(btn);


        borderPane.setCenter(gridPane);
        bottomGroup.getChildren().add(scrollPane);

        h.setAlignment(Pos.BOTTOM_LEFT);
        BottomButtonList.clear();
        BottomButtonList.add(new Button(" "));
        h.getChildren().add(BottomButtonList.get(0));

        for (int i = 0; i < aktuellesLevel.getAllowedShapes().size(); i++) {
            BottomButtonList.add(new Button(""+aktuellesLevel.getAllowedShapes().get(i).amount));
            h.getChildren().add(BottomButtonList.get(i+1));
        }
        h.setSpacing(90);
        VBox v = new VBox(scrollPane, h);
        borderPane.setBottom(v); // wenn ich hier Hbox eingebe funkt hier nichts mehr



        StackPane root = new StackPane();
        root.getChildren().add(borderPane);


       /* gridPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Node node = (Node) event.getTarget();
                int row = GridPane.getRowIndex(node);
                int column = GridPane.getColumnIndex(node);
                System.out.println(row);
                System.out.println(column);


            }
        });
*/

        mycanvasBottom.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                /*Circle c = new Circle(30);
                c.setCenterX(event.getX());
                c.setCenterY(event.getY());
                if(event.getButton() == MouseButton.PRIMARY){
                    c.setFill(Color.RED);
                }
                */

                System.out.println(event.getSceneX());
                double x = event.getSceneX();
                x = x - 80;
                x = x / 120;
                int xAbgerundet = (int) x;
                System.out.println("wird auf "+xAbgerundet+ "element geclickt");
                System.out.println(bottomShapeList.get(xAbgerundet));
                AusgewahltesElement = xAbgerundet;
                labelAusgewähltImBottom.setText(xAbgerundet +" Element ausgwaehlt");
                labelAusgewähltImBottom.setStyle("-fx-text-fill: red");
                if(wurdeGecklickt2){
                    for (int i = 0; i < BottomButtonList.size(); i++) {
                        BottomButtonList.get(i).setStyle("-fx-background-color: #FFFFFF; ");
                    }
                }
                wurdeGecklickt2 = true;
                BottomButtonList.get(xAbgerundet +1).setStyle("-fx-background-color: #ff0000; ");


            }
        });
        //todo groeße hiervon abhängig machen
        window.widthProperty().addListener((obs, oldVal, newVal) -> {
            //System.out.println("Width obs = " + obs);
            //System.out.println("Width oldVal = " + oldVal);//old Value wechselt pro Pixel
           /* System.out.println("Width newVal = " + newVal); //new Value
            WidthPlusHeightWidth = (int) newVal;
            System.out.println(WidthPlusHeightWidth);
            h.setSpacing(WidthPlusHeightWidth % 10);
            gridPane.setHgap(300);
            canvasListCenter.clear();
            buttonList.clear();
            root.resize(20,20);
*/
            //todo mach das ganze window großer
            /*canvasListCenter.clear();*/
            /* buttonList.clear();*/
            AbstandHGap = newVal.intValue()-820;
            if(AbstandHGap > 0){
                AbstandHGap = (int) ((int) AbstandHGap /2.5);
                gridPane.setHgap(AbstandHGap);
                /*borderPane.setCenter(null);*/

            }

            //todo easy hier ein bisschen rumspielen
            System.out.println("das ist der Abstand " +AbstandHGap);
            System.out.println("lol =" + newVal);
            System.out.println("geschafft");



            //borderPane.setCenter(null);
            /*for (int i = 0; i < aktuellesLevel.getHeight(); i++) {
                for (int j = 0; j < aktuellesLevel.getWidth(); j++) {
                    gridPane.getChildren().remove(buttonList.get(j+i* aktuellesLevel.getWidth()));
                }
            }
            gameFieldElementList.clear();
            canvasListCenter.clear();
            buttonList.clear();*/

            //on change width
        });

        window.heightProperty().addListener((obs, oldVal, newVal) -> {
            //System.out.println("Height obs = " + obs); kann man ignorieren
            //System.out.println("Height oldVal = " + oldVal); //old Value wechselt pro Pixel
            System.out.println("Height newVal = " + newVal); //new Value
            AbstandVGap = newVal.intValue()-480;
            if(AbstandVGap > 0){
                AbstandVGap = (int) ((int) AbstandVGap /2.5);
                gridPane.setVgap(AbstandVGap);
            }
            /*WidthPlusHeightWidth = (int) newVal;
            System.out.println(WidthPlusHeight);*/

            //on change height
        });

        window.setScene(new Scene(root, 800, 400));

        window.show();
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == levelLaden) {
            System.out.println("Button 1 printed");
        }
        if (event.getSource() == levelZurucksetzen) {

            InputStream stream = new ByteArrayInputStream(LevelOrginal.getBytes(StandardCharsets.UTF_8));
            aktuellesLevel = LevelParser.parseStreamToLevel(stream);
            System.out.println(LevelOrginal);
            gameFieldElementList.clear();
            fieldMap = aktuellesLevel.fieldMap;
            int height = aktuellesLevel.getHeight();
            int width = aktuellesLevel.getWidth();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    gameFieldElementList.add(fieldMap.get(new Coordinate2D(j,i)));
                }
            }
            for (int k = 0; k < width*height; k++) {

                buttonList.get(k).setStyle("-fx-background-color: #00000000; ");
            }
            System.out.println(gameFieldElementList);

            ovalWithRectangle(aktuellesLevel.getHeight(), aktuellesLevel.getWidth(), gameFieldElementList);
            borderPane.setCenter(gridPane);
            bottomShapes();
            btn.setGraphic(mycanvasBottom);
            HBox h = new HBox();
            scrollPane.setContent(btn);
            h.setAlignment(Pos.BOTTOM_LEFT);
            BottomButtonList.clear();
            BottomButtonList.add(new Button(" "));
            h.getChildren().add(BottomButtonList.get(0));

            for (int i = 0; i < aktuellesLevel.getAllowedShapes().size(); i++) {
                BottomButtonList.add(new Button(""+aktuellesLevel.getAllowedShapes().get(i).amount));
                h.getChildren().add(BottomButtonList.get(i+1));
            }
            h.setSpacing(90);
            VBox v = new VBox(scrollPane, h);
            borderPane.setBottom(v); // wenn ich hier Hbox eingebe funkt hier nichts mehr

        }
        if(event.getSource() == pointbutton){
            System.out.println("pointbutton wurde gepressed");
        }
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            throw new IllegalArgumentException();
        }
    }

    public void zurucksetzenLevel(){

    }

    public void ovalWithRectangle(int height, int width){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                canvasListCenter.add(new Canvas(40,40));
            }
        }
        int canvasCounter = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                gc = canvasListCenter.get(canvasCounter).getGraphicsContext2D();
                drawGrid(gc, Color.RED);
                if(j == 0){
                    gridPane.add(canvasListCenter.get(canvasCounter), 0,i);
                }else{
                    gridPane.add(canvasListCenter.get(canvasCounter), j,i);
                }
                canvasCounter += 1;

            }
        }
    }
    public void ovalWithRectangle(int height, int width,List<GameFieldElement> gameFieldElements){
        System.out.println("ovalWithR wurde ausgeführt");
        canvasListCenter.clear();
        buttonList.clear();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                canvasListCenter.add(new Canvas(50,50));
                buttonList.add(new Button());
            }
        }
        Color color = Color.RED;

        int counter = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                switch (gameFieldElements.get(counter).toString()){
                    case "b": color = Color.BLUE; break;
                    case "g": color = Color.GREEN; break;
                    case "r": color = Color.RED; break;
                    case "y": color = Color.YELLOW; break;
                    case "p": color = Color.PURPLE; break;
                    case "B": color = Color.BLACK; break;
                    case "o": color = Color.ORANGE; break;
                    case "n": color = Color.GRAY; break;
                    case "#": color = Color.TRANSPARENT; break;  //todo block
                    case "+": color = Color.MAGENTA; break;
                    default: color = Color.WHITESMOKE;


                }

                System.out.println(gameFieldElements.get(i+j).toString());
                System.out.println("lol");
                gc = canvasListCenter.get(counter).getGraphicsContext2D();

                drawCircleWithRectangle(gc, color);
                buttonList.get(counter).setGraphic(canvasListCenter.get(counter));
                gridPane.add(buttonList.get(counter), j, i);
                counter += 1;
            }
        }
        for (int i = 0; i < buttonList.size(); i++) {
            int j = i;
            buttonList.get(i).addEventHandler(MouseEvent.MOUSE_ENTERED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            System.out.println("ich bin nummer "+j);
                            int Ycoordinate = j/height;
                            int Xcoordiante = j % height;
                            System.out.println("Y Kood "+Ycoordinate);
                            System.out.println("X Kood" + Xcoordiante);
                            buttonList.get(j);
                            System.out.println(j);
                            System.out.println(bottomShapeList.toString());

                            List<Coordinate2D> Punkte = aktuellesLevel.getAllowedShapes().get(AusgewahltesElement).getPoints();
                            for (int k = 0; k < Punkte.size(); k++) {
                                int x = Punkte.get(k).getX();
                                int y = Punkte.get(k).getY();
                                buttonList.get(j+x-y*width).setStyle("-fx-background-color: #ff0000; ");



                            }
                            /*canvasListCenter.get(j).getGraphicsContext2D().setFill(Color.RED);*/



                        }
                    });



            buttonList.get(i).addEventHandler(MouseEvent.MOUSE_EXITED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            System.out.println("ich bin nummer "+j);
                            int Ycoordinate = j/height;
                            int Xcoordiante = j % height;

                            System.out.println("ich exite gerade");

                            //canvasListCenter.get(j).getGraphicsContext2D().setFill(Color.RED);
                            List<Coordinate2D> Punkte = aktuellesLevel.getAllowedShapes().get(AusgewahltesElement).getPoints();
                            for (int k = 0; k < Punkte.size(); k++) {
                                int x = Punkte.get(k).getX();
                                int y = Punkte.get(k).getY();
                                buttonList.get(j+x-y*width).setStyle("-fx-background-color: #00000000; ");
                            }


                        }
                    });
            buttonList.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int Ycoordinate = j/height;
                    int Xcoordiante = j % height;
                    System.out.println(" klicked !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println(aktuellesLevel.getAllowedShapes().get(AusgewahltesElement).toString());
                    System.out.println(Xcoordiante);
                    System.out.println(Ycoordinate);
                    Collection<Move> moveList = aktuellesLevel.setShapeAndUpdate(aktuellesLevel.getAllowedShapes().get(AusgewahltesElement), new Coordinate2D(Xcoordiante, Ycoordinate));
                    if (aktuellesLevel.isWon()){
                        /*for (int i = 0; i < aktuellesLevel.getHeight(); i++) {
                            for (int j = 0; j < aktuellesLevel.getWidth(); j++) {
                                gridPane.getChildren().remove(buttonList.get(j+i* aktuellesLevel.getWidth()));
                            }
                        }*/

                        //borderPane.getChildren().clear();
                        //popupfenster aufmachen
                        gridPane = null;
                        gameFieldElementList.clear();
                        System.out.println("gewonnen");
                        borderPane.setBottom(new Button("win"));
                        //borderPane.setCenter(new Button("gewonnen"));//Kinder löschen//borderpane umsetzen
                        final Stage dialog = new Stage();
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(window);
                        VBox dialogVbox = new VBox(20);
                        dialogVbox.getChildren().add(new Text("You Won\n Bitte Oeffnen Sie die Application erneut"));
                        Scene dialogScene = new Scene(dialogVbox, 300, 200);
                        dialog.setScene(dialogScene);
                        dialog.show();
                        /*gridPane = null;
                        gameFieldElementList.clear();
                        System.out.println("gewonnen");
                        borderPane.setBottom(null);
                        gewonnen.setText("hello");
                        //borderPane.setCenter(new Button("gewonnen"));//Kinder löschen//borderpane umsetzen
                        final Stage dialog = new Stage();
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(window);
                        VBox dialogVbox = new VBox(20);
                        dialogVbox.getChildren().add(new Text("You Won\n Bitte Oeffnen Sie die Application erneut"));
                        Scene dialogScene = new Scene(dialogVbox, 300, 200);
                        dialog.setScene(dialogScene);
                        dialog.show();*/


                    }
                    else if(!aktuellesLevel.canMakeAnyMove2()){ //todo hier im Else block liegt das Problem
                        gridPane = null;
                        gameFieldElementList.clear();
                        System.out.println("gewonnen");
                        borderPane.setBottom(new Button("verloren"));
                        //borderPane.setCenter(new Button("gewonnen"));//Kinder löschen//borderpane umsetzen
                        final Stage dialog = new Stage();
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(window);
                        VBox dialogVbox = new VBox(20);
                        dialogVbox.getChildren().add(new Text("You Lost"));
                        Scene dialogScene = new Scene(dialogVbox, 300, 200);
                        dialog.setScene(dialogScene);
                        dialog.show();
                    }
                    else{
                        System.out.println(aktuellesLevel.toString());
                        String LevelUpdate = aktuellesLevel.toString();
                        InputStream stream = new ByteArrayInputStream(LevelUpdate.getBytes(StandardCharsets.UTF_8));
                        aktuellesLevel = LevelParser.parseStreamToLevel(stream);
                        System.out.println(LevelUpdate);
                        gameFieldElementList.clear();
                        fieldMap = aktuellesLevel.fieldMap;
                        int height = aktuellesLevel.getHeight();
                        int width = aktuellesLevel.getWidth();
                        for (int i = 0; i < height; i++) {
                            for (int j = 0; j < width; j++) {
                                gameFieldElementList.add(fieldMap.get(new Coordinate2D(j,i)));
                            }
                        }
                        for (int k = 0; k < width*height; k++) {

                            buttonList.get(k).setStyle("-fx-background-color: #00000000; ");
                        }
                        System.out.println(gameFieldElementList);

                        ovalWithRectangle(aktuellesLevel.getHeight(), aktuellesLevel.getWidth(), gameFieldElementList);
                        borderPane.setCenter(gridPane);
                        bottomShapes();
                        btn.setGraphic(mycanvasBottom);
                        HBox h = new HBox();

                        scrollPane.setContent(btn);
                        h.setAlignment(Pos.BOTTOM_LEFT);
                        BottomButtonList.clear();
                        BottomButtonList.add(new Button(" "));
                        h.getChildren().add(BottomButtonList.get(0));

                        for (int i = 0; i < aktuellesLevel.getAllowedShapes().size(); i++) {
                            BottomButtonList.add(new Button(""+aktuellesLevel.getAllowedShapes().get(i).amount));
                            h.getChildren().add(BottomButtonList.get(i+1));
                        }
                        h.setSpacing(90);
                        VBox v = new VBox(scrollPane, h);
                        borderPane.setBottom(v); // wenn ich hier Hbox eingebe funkt hier nichts mehr
                    }
                    /*for (int k = 0; k < moveList.size(); k++) {
                        moveList.get
                    }*/// todo hier irgendiw die MOvelist durchbekommen
                    /*System.out.println("aktuelles Level Ausgabe" + aktuellesLevel.toString());
                    if(!aktuellesLevel.canMakeAnyMove2()){ //todo hier im Else block liegt das Problem
                        gridPane = null;
                        gameFieldElementList.clear();
                        System.out.println("gewonnen");
                        borderPane.setBottom(new Button("verloren"));
                        //borderPane.setCenter(new Button("gewonnen"));//Kinder löschen//borderpane umsetzen
                        final Stage dialog = new Stage();
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(window);
                        VBox dialogVbox = new VBox(20);
                        dialogVbox.getChildren().add(new Text("You Lost"));
                        Scene dialogScene = new Scene(dialogVbox, 300, 200);
                        dialog.setScene(dialogScene);
                        dialog.show();
                    }*/


                }
            });
        }
        System.out.println(gameFieldElements.toString());

    }
    //setonmouseentered
    //abfragen wo der Canvas ist
//canvas sollte sich rezi
    //große shapes
    //real angebote
    public void drawGrid(GraphicsContext gc, Color color){

        drawRectangle( gc);

        // Set the stroke and fill color.
        gc.setStroke(Color.BLUE);
        gc.setFill(color);

        gc.fillOval(0+5, 0 + 5, 40, 40);
    }

    public void drawCircleWithRectangle(GraphicsContext gc, Color color){

        drawRectangle(gc);

        // Set the stroke and fill color.
        gc.setStroke(Color.BLUE);
        gc.setFill(color);

        gc.fillOval(5,  5, 40, 40);
    }

    public  void drawRectangle(GraphicsContext gc){
        gc.setStroke(Color.GREEN);
        gc.setFill(Color.GRAY);


        gc.fillRect(+0,0,50,50);
    }

    public void bottomShapes(){


        // Get the graphics context for the canvas.
        gc = mycanvasBottom.getGraphicsContext2D();




        gc.setLineWidth(2.0);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.GRAY);

        int Abstand = 0;

        if(!Objects.isNull(aktuellesLevel)){
            gc.clearRect(0, 0, mycanvasBottom.getWidth(), mycanvasBottom.getHeight());
            bottomShapeList.clear();
            Abstand = 80;
            for (int i = 0; i < aktuellesLevel.getAllowedShapes().size(); i++) {
                Abstand = randomShape(Abstand, i);
                System.out.println(aktuellesLevel.getAllowedShapes().toString());
                bottomShapeList.add("Shape Nummer "+i);
            }
            System.out.println(aktuellesLevel.getAllowedShapes().size());
            System.out.println("in der for schleife");
        }else{
            Abstand = point(Abstand);
            bottomShapeList.add("point");
            Abstand = sRow(Abstand);
            bottomShapeList.add("sRow");
            Abstand = sColumn(Abstand);
            bottomShapeList.add("sColumn");
            Abstand = row(Abstand);
            bottomShapeList.add("row");
            Abstand = column(Abstand);
            bottomShapeList.add("column");
            Abstand = upT(Abstand);
            bottomShapeList.add("upT");
            Abstand = downT(Abstand);
            bottomShapeList.add("downT");
            Abstand = rightT(Abstand);
            bottomShapeList.add("rightT");
            Abstand = leftT(Abstand);
            bottomShapeList.add("leftT");
            Abstand = point(Abstand);
        }
        System.out.println("ich wurde abgerufen");





    }

    public int point(int Abstand){

        gc.fillRect(Abstand+20,30, 20, 20);
        gc.strokeRect(Abstand+20,30,20,20);



        return 80 + Abstand;
    }

    public int randomShape(int Abstand, int index){
        Shape shape = aktuellesLevel.getAllowedShapes().get(index);

        List<Coordinate2D> punkte = shape.getPoints();
        System.out.println("lol");
        System.out.println(aktuellesLevel.getAllowedShapes().get(index).getPoints());
        for (int i = 0; i < punkte.size(); i++) {
            gc.fillRect(Abstand +(20*punkte.get(i).getX()), (30-(20*punkte.get(i).getY())),20,20);
            gc.strokeRect(Abstand +(20*punkte.get(i).getX()), 30-(20*punkte.get(i).getY()),20,20);
        }
        gc.strokeRect(Abstand -50, 0, 120,100 );

        return Abstand + 120;

    }

    public int sRow(int Abstand){
        gc.fillRect(Abstand+10,30, 20, 20);
        gc.strokeRect(Abstand+10,30,20,20);
        gc.fillRect(20 +Abstand+10,30, 20, 20);
        gc.strokeRect(20 +Abstand+10,30,20,20);
        return  80 + Abstand;
    }

    public int sColumn(int Abstand){
        gc.fillRect(Abstand +20,20, 20, 20);
        gc.strokeRect(Abstand+20,20,20,20);
        gc.fillRect(Abstand+20,40, 20, 20);
        gc.strokeRect(Abstand+20,40,20,20);
        return 80 + Abstand;
    }

    public int row(int Abstand){
        gc.fillRect(Abstand ,30, 20, 20);
        gc.strokeRect(Abstand ,30,20,20);
        gc.fillRect(20 +Abstand ,30, 20, 20);
        gc.strokeRect(20 +Abstand,30,20,20);
        gc.fillRect(40 +Abstand,30, 20, 20);
        gc.strokeRect(40 +Abstand,30,20,20);
        return 80 + Abstand;
    }

    public int column(int Abstand){
        gc.fillRect(Abstand +20,10, 20, 20);
        gc.strokeRect(Abstand +20,10,20,20);
        gc.fillRect(Abstand+20,30, 20, 20);
        gc.strokeRect(Abstand+20,30,20,20);
        gc.fillRect(Abstand+20,50, 20, 20);
        gc.strokeRect(Abstand+20,50,20,20);
        return  80 + Abstand;
    }

    public int upT(int Abstand){
        gc.fillRect(Abstand,30, 20, 20);
        gc.strokeRect(Abstand,30,20,20);
        gc.fillRect(20 +Abstand,30, 20, 20);
        gc.strokeRect(20+ Abstand,30,20,20);
        gc.fillRect(40+ Abstand,30, 20, 20);
        gc.strokeRect(40 +Abstand,30,20,20);
        gc.fillRect(20 +Abstand,10, 20, 20);
        gc.strokeRect(20+Abstand,10,20,20);
        return 80 + Abstand;
    }
    public int downT(int Abstand){
        gc.fillRect(Abstand,30, 20, 20);
        gc.strokeRect(Abstand,30,20,20);
        gc.fillRect(20 +Abstand,30, 20, 20);
        gc.strokeRect(20+ Abstand,30,20,20);
        gc.fillRect(40+ Abstand,30, 20, 20);
        gc.strokeRect(40 +Abstand,30,20,20);
        gc.fillRect(20 +Abstand,50, 20, 20);
        gc.strokeRect(20+Abstand,50,20,20);
        return 80 + Abstand;
    }

    public int rightT(int Abstand){
        gc.fillRect(Abstand+10,10, 20, 20);
        gc.strokeRect(Abstand+10,10,20,20);
        gc.fillRect(Abstand+10,30, 20, 20);
        gc.strokeRect(Abstand+10,30,20,20);
        gc.fillRect(Abstand+10,50, 20, 20);
        gc.strokeRect(Abstand+10,50,20,20);
        gc.fillRect(Abstand+20+10,30, 20, 20);
        gc.strokeRect(Abstand+20+10,30,20,20);
        return  80 + Abstand;
    }
    public int leftT(int Abstand){
        gc.fillRect(Abstand+20+10,10, 20, 20);
        gc.strokeRect(Abstand+20+10,10,20,20);
        gc.fillRect(Abstand+10,30, 20, 20);
        gc.strokeRect(Abstand+10,30,20,20);
        gc.fillRect(Abstand+20+10,50, 20, 20);
        gc.strokeRect(Abstand+20+10,50,20,20);
        gc.fillRect(Abstand+20+10,30, 20, 20);
        gc.strokeRect(Abstand+20+10,30,20,20);
        return  80 + Abstand;
    }





  /*  public void layoutChildren(){
        super.layoutChildren();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, aktuellesLevel.getWidth(), aktuellesLevel.getHeight());

        // vertical lines
        gc.setStroke(Color.BLUE);
        for(int i = 0 ; i < aktuellesLevel.getWidth() ; i+=30){
            gc.strokeLine(i, 0, i, aktuellesLevel.getHeight() - (aktuellesLevel.getHeight())
                    %30);
        }

        // horizontal lines
        gc.setStroke(Color.RED);
        for(int i = 30 ; i < aktuellesLevel.getHeight() ; i+=30){
            gc.strokeLine(30, i, aktuellesLevel.getWidth(), i);
        }

    }*/
}
