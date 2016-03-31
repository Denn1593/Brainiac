import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Background extends ImageView
{
    private WritableImage image = new WritableImage(400, 400);
    private PixelWriter pixelWriter = image.getPixelWriter();

    public Background()
    {
        int color = 0;
        double[] r = {0.75, 1, 0.75, 0.75, 1, 0.75, 1, 1};
        double[] g = {0.75, 0.75, 1, 0.75, 1, 1, 0.75, 1};
        double[] b = {0.75, 0.75, 0.5, 1, 0.75, 1, 1, 1};
        for(int x = 0; x < 400; x++)
        {
            for(int y = 0; y < 400; y++)
            {
                color = (y) / 50;
                pixelWriter.setColor(x, y, Color.color(r[color], g[color], b[color]));
            }
        }
        this.setImage(image);
    }
}
