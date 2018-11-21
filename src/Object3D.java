import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Object3D extends Application {
    Stage window;
    Button solidShape,wireFrame;
    Box cube;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;
    private double anchorX,anchorY;
    private double anchorAngleX,anchorAngleY;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) {
        window=primaryStage;

        cube = Polygon3D.createCube(50,50,50);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/wood.jpg")));
        cube.setMaterial(material);

        SmartGroup group = new SmartGroup();
        group.getChildren().add(cube);

        VBox layout=new VBox(10);
        layout.setPadding(new Insets(10,10,10,10));

        solidShape=new Button("Solid Frame");
        solidShape.setOnAction( e -> setSolidShape(cube) );
        wireFrame=new Button("Wire Frame");
        wireFrame.setOnAction( e -> setWireFrame(cube) );
        layout.getChildren().addAll(solidShape,wireFrame,group);

        Camera camera = new PerspectiveCamera();

        Scene scene = new Scene(layout, WIDTH, HEIGHT);
        scene.setFill(Color.WHITE);
        scene.setCamera(camera);

        Point3D location=new Point3D(375.0,100.0,-500.0);
        group.translateXProperty().set(location.getX());
        group.translateYProperty().set(location.getY());
        group.translateZProperty().set(location.getZ());

        initMouseControl(group,scene);

        window.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case W:
                    group.translateZProperty().set(group.getTranslateZ() + 10);
                    break;
                case S:
                    group.translateZProperty().set(group.getTranslateZ() - 10);
                    break;
            }
        });

        window.setTitle("OOM Mini Project");
        window.setScene(scene);
        window.setResizable(false);
        window.show();
    }

    private void initMouseControl(SmartGroup group, Scene scene) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0,Rotate.X_AXIS),
                yRotate = new Rotate(0,Rotate.Y_AXIS)
        );

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + (anchorX - event.getSceneX()));
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    void setSolidShape(Box cube) {
        cube.setDrawMode(DrawMode.FILL);
    }

    void setWireFrame(Box cube) {
        cube.setDrawMode(DrawMode.LINE);
    }
}