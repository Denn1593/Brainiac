import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Piece extends ImageView
{
    private WritableImage image = new WritableImage(40, 40);
    private PixelWriter pixelWriter = image.getPixelWriter();
    private int color = 0;
    private boolean special = false;
    private Timeline specialAnimation = new Timeline();

    public Piece(int color, boolean special)
    {
        this.color = color;
        this.setImage(image);
        setColor(color);
        this.setEffect(new DropShadow(1, Color.BLACK));
        if(special)
        {
            this.setEffect(new Glow(5));
        }
    }

    public void setColor(int color)
    {
        double[] r = {0, 1, 0, 0, 1, 0, 1, 0.75};
        double[] g = {0, 0, 1, 0, 1, 1, 0, 0.75};
        double[] b = {0, 0, 0, 1, 0, 1, 1, 0.75};

        double cDist = 0;
        double alpha = 0;

        for(int x = 0; x < 40; x++)
        {
            for(int y = 0; y < 40; y++)
            {
                cDist = Math.sqrt((x - 20) * (x - 20) + (y - 20) * (y - 20));
                if (cDist > 18) {
                    alpha = -0.5 * cDist + 10;
                } else {
                    alpha = 1;
                }
                if (cDist < 20) {
                    pixelWriter.setColor(x, y, Color.color(0.75 * r[color] + cDist / 80, 0.75 * g[color] + cDist / 80, 0.75 * b[color] + cDist / 80, alpha));
                }
            }
        }
    }

    public int getColor() {
        return color;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
        if(special)
        {
            DropShadow dropShadow = new DropShadow(10, Color.color(1, 0, 0));
            this.setEffect(dropShadow);
            KeyValue kv1 = new KeyValue(dropShadow.radiusProperty(), 10, Interpolator.EASE_BOTH);
            KeyFrame kf1 = new KeyFrame(Duration.millis(0), kv1);
            KeyValue kv2 = new KeyValue(dropShadow.radiusProperty(), 20, Interpolator.EASE_BOTH);
            KeyFrame kf2 = new KeyFrame(Duration.millis(1000), kv2);
            specialAnimation.getKeyFrames().addAll(kf1, kf2);
            specialAnimation.setCycleCount(Timeline.INDEFINITE);
            specialAnimation.setAutoReverse(true);
            specialAnimation.play();
        }
        else
        {
            this.setEffect(null);
            specialAnimation.stop();
            this.setEffect(null);
        }
    }
}
