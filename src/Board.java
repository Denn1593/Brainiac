import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class Board extends Scene
{
    private Random random = new Random();
    private Piece[][] pieces = new Piece[8][8];
    private Pane pane = new Pane();
    private int cursorX = 0;
    private int cursorY = 0;
    private Arrow[] arrowX = new Arrow[2];
    private Timeline arrowXanimation = new Timeline();
    private Arrow[] arrowY = new Arrow[2];
    private Timeline arrowYanimation = new Timeline();
    private Timeline piecesAnimation = new Timeline();
    private Timeline moveLeftAnimaiton = new Timeline();
    private Timeline moveRightAnimaiton = new Timeline();
    private Timeline moveUpAnimaiton = new Timeline();
    private Timeline moveDownAnimaiton = new Timeline();
    private boolean specialmode = false;
    private Button menuButton = new Button("Menu");
    private AudioClip moveAudio = new AudioClip(getClass().getResource("move.wav").toString());
    private AudioClip noMoveAudio = new AudioClip(getClass().getResource("nomove.wav").toString());
    private AudioClip music = new AudioClip(getClass().getResource("music.wav").toString());
    private ArrayList<Integer> rows = new ArrayList<>();
    private ArrayList<Integer> columns = new ArrayList<>();

    public Board(int scramble, int specials, boolean specialmode)
    {
        super(new Pane(), 480, 520);
        ArrayList<Piece> specialList = new ArrayList<>(64);
        this.pane = (Pane) this.getRoot();
        this.specialmode = specialmode;
        Background background = new Background();
        background.setLayoutX(40);
        background.setLayoutY(40);
        background.setEffect(new DropShadow(10, Color.color(0, 0, 0)));
        pane.getChildren().addAll(background, menuButton);
        menuButton.setLayoutX(400);
        menuButton.setLayoutY(480);
        menuButton.setPrefWidth(60);
        menuButton.setOnAction(e-> returnTomenu());
        music.setCycleCount(AudioClip.INDEFINITE);
        music.play();

        arrowX[0] = new Arrow("up.png");
        arrowX[1] = new Arrow("down.png");
        arrowY[0] = new Arrow("left.png");
        arrowY[1] = new Arrow("right.png");

        //add all pieces to the board in winposition
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                pieces[x][y] = new Piece(y, false);
                pieces[x][y].setLayoutX(45 + 50 * x);
                pieces[x][y].setLayoutY(45 + 50 * y);
                specialList.add(pieces[x][y]);
                pane.getChildren().add(pieces[x][y]);
            }
        }

        //choose special pieces from pieces at the board
        for(int i = 0; i < specials; i++)
        {
            int r = random.nextInt(specialList.size());
            specialList.get(r).setSpecial(true);
            specialList.remove(r);
        }
        System.out.println(specialmode);
        //if specialmode is not on, check which lines are moveable once
        if(!specialmode)
        {
            findPossibleColumns();
            findPossibleRows();
        }

        //scramble the board
        for(int i = 0; i < scramble; i++)
        {
            if(specialmode)
            {
                findPossibleRows();
                findPossibleColumns();
                System.out.println("row size: "+rows.size());
                System.out.println("column size" +columns.size());
                for(Integer in: rows)
                {
                    System.out.println("row:" +in);
                }
                for(Integer in: columns)
                {
                    System.out.println("column" +in);
                }
            }
            int direction = random.nextInt(4);
            int moves = random.nextInt(4) + 1;
            if(direction == 0)
            {
                cursorY = rows.get(random.nextInt(rows.size()));
                System.out.println(cursorY);
                for (int j = 0; j < moves; j++)
                {
                    moveLeft();
                }
            }
            if(direction == 1)
            {
                cursorY = rows.get(random.nextInt(rows.size()));
                System.out.println(cursorY);
                for (int j = 0; j < moves; j++)
                {
                    moveRight();
                }
            }
            if(direction == 2)
            {
                cursorX = columns.get(random.nextInt(columns.size()));
                System.out.println(cursorX);
                for (int j = 0; j < moves; j++)
                {
                    moveUp();
                }
            }
            if(direction == 3)
            {
                cursorX = columns.get(random.nextInt(columns.size()));
                System.out.println(cursorX);
                for (int j = 0; j < moves; j++)
                {
                    moveDown();
                }
            }
        }
        cursorX = 0;
        cursorY = 0;
        animatePieces();
        initArrowsPosition();
        pane.getChildren().addAll(arrowX[0], arrowX[1], arrowY[0], arrowY[1]);
        this.setOnKeyPressed(this::keyInput);
        this.setOnKeyReleased(this::keyRelease);
    }

    public void findPossibleRows()
    {
        rows.clear();
        for(int y = 0; y < 8; y++)
        {
            cursorY = y;
            if(checkMoveability(true))
            {
                rows.add(y);
                System.out.println("y: "+y);
            }
        }
    }

    public void findPossibleColumns()
    {
        columns.clear();
        for(int x = 0; x < 8; x++)
        {
            cursorX = x;
            if(checkMoveability(false))
            {
                columns.add(x);
                System.out.println("x: "+x);
            }
        }
    }

    public void keyRelease(KeyEvent ke)
    {
        if(ke.getCode() == KeyCode.SHIFT)
        {
            arrowX[0].setEffect(null);
            arrowX[1].setEffect(null);
            arrowY[0].setEffect(null);
            arrowY[1].setEffect(null);
        }
    }

    public void keyInput(KeyEvent ke)
    {
        if(ke.getCode() == KeyCode.LEFT && !ke.isShiftDown())
        {
            if(cursorX < 1)
            {
                cursorX = 7;
            }
            else
            {
                cursorX--;
            }
            animateArrowsX();
        }
        if(ke.getCode() == KeyCode.RIGHT && !ke.isShiftDown())
        {
            if(cursorX > 6)
            {
                cursorX = 0;
            }
            else
            {
                cursorX++;
            }
            animateArrowsX();
        }
        if(ke.getCode() == KeyCode.UP && !ke.isShiftDown())
        {
            if(cursorY < 1)
            {
                cursorY = 7;
            }
            else
            {
                cursorY--;
            }
            animateArrowsY();
        }
        if(ke.getCode() == KeyCode.DOWN && !ke.isShiftDown())
        {
            if(cursorY > 6)
            {
                cursorY = 0;
            }
            else
            {
                cursorY++;
            }
            animateArrowsY();
        }
        if(ke.isShiftDown())
        {
            arrowX[0].setEffect(new DropShadow(3, Color.color(1, 0, 0)));
            arrowX[1].setEffect(new DropShadow(3, Color.color(1, 0, 0)));
            arrowY[0].setEffect(new DropShadow(3, Color.color(1, 0, 0)));
            arrowY[1].setEffect(new DropShadow(3, Color.color(1, 0, 0)));
            moveLines(ke);
        }
    }

    public boolean checkMoveability(boolean horizontal)
    {
        boolean isAble = !specialmode;
        if(horizontal)
        {
            for(int i = 0; i < 8; i++)
            {
                if(pieces[i][cursorY].isSpecial())
                {
                    isAble = specialmode;
                }
            }
        }
        if(!horizontal)
        {
            for(int i = 0; i < 8; i++)
            {
                if(pieces[cursorX][i].isSpecial())
                {
                    isAble = specialmode;
                }
            }
        }
        return isAble;
    }

    public void moveLeft()
    {
        Piece temp = null;
        temp = pieces[0][cursorY];
        for (int i = 0; i < 7; i++) {
            pieces[i][cursorY] = pieces[i + 1][cursorY];
        }
        pieces[7][cursorY] = temp;
        System.out.println("moved "+cursorY+ " left");
    }

    public void moveRight()
    {
        Piece temp = null;
        temp = pieces[7][cursorY];
        for (int i = 7; i > 0; i--) {
            pieces[i][cursorY] = pieces[i - 1][cursorY];
        }
        pieces[0][cursorY] = temp;
        System.out.println("moved "+cursorY+ " right");
    }

    public void moveUp()
    {
        Piece temp = null;
        temp = pieces[cursorX][0];
        for (int i = 0; i < 7; i++) {
            pieces[cursorX][i] = pieces[cursorX][i + 1];
        }
        pieces[cursorX][7] = temp;
        System.out.println("moved "+cursorX+ " up");
    }

    public void moveDown()
    {
        Piece temp = null;
        temp = pieces[cursorX][7];
        for (int i = 7; i > 0; i--) {
            pieces[cursorX][i] = pieces[cursorX][i - 1];
        }
        pieces[cursorX][0] = temp;
        System.out.println("moved "+cursorX+ " down");
    }

    public void moveLines(KeyEvent ke)
    {
        if(ke.getCode() == KeyCode.LEFT)
        {
            if(checkMoveability(true))
            {
                moveLeft();
                animatePieces();
                animateMoveLeft();
                moveAudio.play();
            }
            else
            {
                noMoveAudio.play();
            }
        }
        if(ke.getCode() == KeyCode.RIGHT)
        {
            if(checkMoveability(true))
            {
                moveRight();
                animatePieces();
                animateMoveRight();
                moveAudio.play();
            }
            else
            {
                noMoveAudio.play();
            }
        }
        if(ke.getCode() == KeyCode.UP)
        {
            if(checkMoveability(false))
            {
                moveUp();
                animatePieces();
                animateMoveUp();
                moveAudio.play();
            }
            else
            {
                noMoveAudio.play();
            }
        }
        if(ke.getCode() == KeyCode.DOWN)
        {
            if(checkMoveability(false))
            {
                moveDown();
                animatePieces();
                animateMoveDown();
                moveAudio.play();
            }
            else
            {
                noMoveAudio.play();
            }
        }
    }

    public void returnTomenu()
    {
        music.stop();
        Run.window.setScene(Run.menu);
    }

    public void animatePieces()
    {
        piecesAnimation = new Timeline();
        for (int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                KeyValue kv10 = new KeyValue(pieces[x][y].layoutXProperty(), pieces[x][y].getLayoutX());
                KeyValue kv11 = new KeyValue(pieces[x][y].layoutYProperty(), pieces[x][y].getLayoutY());
                KeyFrame kf1 = new KeyFrame(Duration.millis(0), kv10, kv11);
                KeyValue kv20 = new KeyValue(pieces[x][y].layoutXProperty(), 45 + x * 50, Interpolator.EASE_OUT);
                KeyValue kv21 = new KeyValue(pieces[x][y].layoutYProperty(), 45 + y * 50, Interpolator.EASE_OUT);
                KeyFrame kf2 = new KeyFrame(Duration.millis(100), kv20, kv21);
                piecesAnimation.getKeyFrames().addAll(kf1, kf2);
            }
        }
        piecesAnimation.play();
    }

    public void animateArrowsX()
    {
        arrowXanimation = new Timeline();
        KeyValue kv10 = new KeyValue(arrowX[0].layoutXProperty(), arrowX[0].getLayoutX());
        KeyValue kv11 = new KeyValue(arrowX[1].layoutXProperty(), arrowX[1].getLayoutX());
        KeyFrame kf1 = new KeyFrame(Duration.millis(0), kv10, kv11);
        KeyValue kv20 = new KeyValue(arrowX[0].layoutXProperty(), 55 + cursorX * 50, Interpolator.EASE_OUT);
        KeyValue kv21 = new KeyValue(arrowX[1].layoutXProperty(), 55 + cursorX * 50, Interpolator.EASE_OUT);
        KeyFrame kf2 = new KeyFrame(Duration.millis(100), kv20, kv21);
        arrowXanimation.getKeyFrames().addAll(kf1, kf2);
        arrowXanimation.play();
    }

    public void animateArrowsY()
    {
        arrowYanimation = new Timeline();
        KeyValue kv10 = new KeyValue(arrowY[0].layoutYProperty(), arrowY[0].getLayoutY());
        KeyValue kv11 = new KeyValue(arrowY[1].layoutYProperty(), arrowY[1].getLayoutY());
        KeyFrame kf1 = new KeyFrame(Duration.millis(0), kv10, kv11);
        KeyValue kv20 = new KeyValue(arrowY[0].layoutYProperty(), 55 + cursorY * 50, Interpolator.EASE_OUT);
        KeyValue kv21 = new KeyValue(arrowY[1].layoutYProperty(), 55 + cursorY * 50, Interpolator.EASE_OUT);
        KeyFrame kf2 = new KeyFrame(Duration.millis(100), kv20, kv21);
        arrowYanimation.getKeyFrames().addAll(kf1, kf2);
        arrowYanimation.play();
    }

    public void animateMoveLeft()
    {
        moveLeftAnimaiton = new Timeline();
        KeyValue kv1 = new KeyValue(arrowY[0].layoutXProperty(), arrowY[0].getLayoutX());
        KeyFrame kf1 = new KeyFrame(Duration.millis(0), kv1);
        KeyValue kv2 = new KeyValue(arrowY[0].layoutXProperty(), 0);
        KeyFrame kf2 = new KeyFrame(Duration.millis(50), kv2);
        KeyValue kv3 = new KeyValue(arrowY[0].layoutXProperty(), 10);
        KeyFrame kf3 = new KeyFrame(Duration.millis(100), kv3);
        moveLeftAnimaiton.getKeyFrames().addAll(kf1, kf2, kf3);
        moveLeftAnimaiton.play();
    }

    public void animateMoveRight()
    {
        moveRightAnimaiton = new Timeline();
        KeyValue kv1 = new KeyValue(arrowY[1].layoutXProperty(), arrowY[1].getLayoutX());
        KeyFrame kf1 = new KeyFrame(Duration.millis(0), kv1);
        KeyValue kv2 = new KeyValue(arrowY[1].layoutXProperty(), 460);
        KeyFrame kf2 = new KeyFrame(Duration.millis(50), kv2);
        KeyValue kv3 = new KeyValue(arrowY[1].layoutXProperty(), 450);
        KeyFrame kf3 = new KeyFrame(Duration.millis(100), kv3);
        moveRightAnimaiton.getKeyFrames().addAll(kf1, kf2, kf3);
        moveRightAnimaiton.play();
    }

    public void animateMoveUp()
    {
        moveUpAnimaiton = new Timeline();
        KeyValue kv1 = new KeyValue(arrowX[0].layoutYProperty(), arrowX[0].getLayoutY());
        KeyFrame kf1 = new KeyFrame(Duration.millis(0), kv1);
        KeyValue kv2 = new KeyValue(arrowX[0].layoutYProperty(), 0);
        KeyFrame kf2 = new KeyFrame(Duration.millis(50), kv2);
        KeyValue kv3 = new KeyValue(arrowX[0].layoutYProperty(), 10);
        KeyFrame kf3 = new KeyFrame(Duration.millis(100), kv3);
        moveUpAnimaiton.getKeyFrames().addAll(kf1, kf2, kf3);
        moveUpAnimaiton.play();
    }

    public void animateMoveDown()
    {
        moveDownAnimaiton = new Timeline();
        KeyValue kv1 = new KeyValue(arrowX[1].layoutYProperty(), arrowX[1].getLayoutY());
        KeyFrame kf1 = new KeyFrame(Duration.millis(0), kv1);
        KeyValue kv2 = new KeyValue(arrowX[1].layoutYProperty(), 460);
        KeyFrame kf2 = new KeyFrame(Duration.millis(50), kv2);
        KeyValue kv3 = new KeyValue(arrowX[1].layoutYProperty(), 450);
        KeyFrame kf3 = new KeyFrame(Duration.millis(100), kv3);
        moveDownAnimaiton.getKeyFrames().addAll(kf1, kf2, kf3);
        moveDownAnimaiton.play();
    }

    public void initArrowsPosition()
    {
        arrowX[0].setLayoutX(55);
        arrowX[1].setLayoutX(55);
        arrowX[0].setLayoutY(10);
        arrowX[1].setLayoutY(450);
        arrowY[0].setLayoutY(55);
        arrowY[1].setLayoutY(55);
        arrowY[0].setLayoutX(10);
        arrowY[1].setLayoutX(450);
    }
}
