import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class MoveButton extends Texture {
    MoveButton(FileHandle s){
        super(s);
    }
    boolean isClicked(){
        return Gdx.input.isTouched();
    }
}
