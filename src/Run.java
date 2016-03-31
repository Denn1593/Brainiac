import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Run extends Application
{
    private int specialsCount = 2;
    private int scrambleCount = 10;
    private TextField scramble = new TextField("10");
    private TextField specials = new TextField("2");
    public static Stage window = null;
    public static Scene menu = null;
    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage window)
    {
        Pane pane = new Pane();
        this.window = window;
        //create elements
        Button plusSpecial = new Button("+");
        plusSpecial.setOnAction(e-> plusSpecial());
        Button minusSpecial = new Button("-");
        minusSpecial.setOnAction(e-> minusSpecial());
        Button plusScramble = new Button("+");
        plusScramble.setOnAction(e-> plusScramble());
        Button minusScramble = new Button("-");
        minusScramble.setOnAction(e-> minusScramble());
        Button start = new Button("Im ready!");
        CheckBox special = new CheckBox();
        Label specialLabel = new Label("Special mode");
        Label scrambleLabel = new Label("scramble");
        Label title = new Label("Brainiac!");
        Label specialsLabel = new Label("Special pieces");
        title.setFont(Font.font(30));
        scramble.setPromptText("Input number...");

        //position elements
        plusSpecial.setLayoutX(210);
        plusSpecial.setLayoutY(150);
        plusSpecial.setPrefWidth(30);
        minusSpecial.setLayoutX(150);
        minusSpecial.setLayoutY(150);
        minusSpecial.setPrefWidth(30);
        plusScramble.setLayoutX(210);
        plusScramble.setLayoutY(120);
        plusScramble.setPrefWidth(30);
        minusScramble.setLayoutX(150);
        minusScramble.setLayoutY(120);
        minusScramble.setPrefWidth(30);
        title.setLayoutX(10);
        title.setLayoutY(10);
        specialLabel.setLayoutX(10);
        specialLabel.setLayoutY(80);
        special.setLayoutX(210);
        special.setLayoutY(80);
        scrambleLabel.setLayoutX(10);
        scrambleLabel.setLayoutY(125);
        scramble.setLayoutX(180);
        scramble.setPrefWidth(30);
        scramble.setLayoutY(120);
        specials.setLayoutX(180);
        specials.setLayoutY(150);
        specials.setPrefWidth(30);
        specialsLabel.setLayoutX(10);
        specialsLabel.setLayoutY(155);
        start.setLayoutX(150);
        start.setLayoutY(200);
        start.setPrefWidth(90);

        //add events to elements

        start.setOnAction(e-> window.setScene(new Board(scrambleCount, specialsCount, special.isSelected())));

        // initialize window
        pane.getChildren().addAll(start, special, specialLabel, scramble, scrambleLabel, title, specials, specialsLabel, plusSpecial, minusSpecial, plusScramble, minusScramble);
        menu = new Scene(pane, 240, 240);
        window.setScene(menu);
        window.setTitle("Brainiac!");
        window.setResizable(false);
        window.show();
    }

    public void plusSpecial()
    {
        if(specialsCount < 6)
        {
            specialsCount++;
            specials.setText(Integer.toString(specialsCount));
            specials.setEffect(null);
        }
    }
    public void minusSpecial()
    {
        if(specialsCount > 0)
        {
            specialsCount--;
            specials.setText(Integer.toString(specialsCount));
            specials.setEffect(null);
        }
    }

    public void plusScramble()
    {
        if(scrambleCount < 100)
        {
            scrambleCount++;
            scramble.setText(Integer.toString(scrambleCount));
            scramble.setEffect(null);
        }
    }

    public void minusScramble()
    {
        if(scrambleCount > 0)
        {
            scrambleCount--;
            scramble.setText(Integer.toString(scrambleCount));
            scramble.setEffect(null);
        }
    }
}
