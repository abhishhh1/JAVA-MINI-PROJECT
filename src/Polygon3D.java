import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;



public class Polygon3D {
    public static Box createCube(int width,int height,int depth) {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.BLUE);
        Box box = new Box(width,height,depth);
        box.setMaterial(material);
        return box;
    }
}