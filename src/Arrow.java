import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by dennis on 3/29/16.
 */
public class Arrow extends ImageView
{
    public Arrow(String direction)
    {
        this.setImage(new Image(direction));
    }
}
