import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Lukado on 23. 11. 2016.
 */
public class Controller {
    public BorderPane mainStage;
    public Button picBut, cutBut;
    public ImageView imgPort;
    public TextField minThreshold, maxThreshold, Sigma, kernel;
    public Pane pane;
    public Separator sep;
    public Label out, aluSize;

    private String filePath;
    private Line line;
    private Image img;
    private double aspectRatio;
    private double imgWidth;
    private double imgHeight;
    private boolean isDragged = false;

    private Path path;

    /**
     * Volá třídu na výpočet protézy
     *
     */
    public void solve() {
        if (img != null && line.isVisible()) {
//            Processor prcs = new Processor(filePath, Integer.parseInt(maxThreshold.getText()), Integer.parseInt(minThreshold.getText()),
//                    Float.parseFloat(Sigma.getText()), Integer.parseInt(kernel.getText()), getExt());
            double imgResizeRatio = img.getWidth() / imgWidth;
            Processor prcs = new Processor(line, imgResizeRatio, aluSize);
            sep.setVisible(true);
            out.setVisible(true);
            aluSize.setVisible(true);

            drawCap(prcs);
        }else{
            aluSize.setText("Image doesn't exist");
        }
    }

    private void drawCap(Processor p) {
        path = new Path();
        ArcTo arcTo = new ArcTo();
        MoveTo moveTo = new MoveTo();

        arcTo.setRadiusX(p.getCapSize());
        arcTo.setRadiusY(p.getCapSize());

        if(line.getStartX()>line.getEndX()){
            moveTo.setX(line.getStartX());
            moveTo.setY(line.getStartY());
            arcTo.setX(line.getEndX());
            arcTo.setY(line.getEndY());
        }else{
            arcTo.setX(line.getStartX());
            arcTo.setY(line.getStartY());
            moveTo.setX(line.getEndX());
            moveTo.setY(line.getEndY());
        }

        path.setStroke(Paint.valueOf("red"));
        path.getElements().add(moveTo);
        path.getElements().add(arcTo);

        pane.getChildren().add(path);
    }

    /**
     * Otevírá obrázek rentgenu a zpracovává ho pro zobrazení a následnou práci.
     *
     * @param actionEvent
     */
    public void openFile(ActionEvent actionEvent) {
        Stage stage = (Stage) mainStage.getScene().getWindow();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Picture loader");
        fileChooser.setInitialDirectory(
                new File("E:/Skola/Ctvrtak/DBM2/Semestralka_orez_kosti/Data_semestralni prace")
//                new File(System.getProperty("user.dir"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.png", "*.jpeg"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            picBut.setText(file.getName());
            filePath = file.getPath();
            img = new Image(file.toURI().toString());
            aspectRatio = img.getWidth() / img.getHeight();
            imgPort.setImage(img);
            imgHeight = Math.min(imgPort.getFitHeight(), imgPort.getFitWidth() / aspectRatio);
            imgWidth = Math.min(imgPort.getFitWidth(), imgPort.getFitHeight() * aspectRatio);
            imgPort.setX(pane.getWidth() / 2 - imgWidth / 2);
        }

    }

    /**
     * Inicializace funkcí programu, nastavení jednotlivých panelů, funkce myši při akci.
     */
    @FXML
    public void initialize() {
        imgPort.fitWidthProperty().bind(pane.widthProperty());
        imgPort.fitHeightProperty().bind(pane.heightProperty());
        //        minThreshold.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d*")) {
//                minThreshold.setText(newValue.replaceAll("[^\\d]", ""));
//            }
//        });
//
//        maxThreshold.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d*")) {
//                maxThreshold.setText(newValue.replaceAll("[^\\d]", ""));
//            }
//        });
//
//        Sigma.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d.*")) {
//                Sigma.setText(newValue.replaceAll("[^\\d]", ""));
//            }
//        });

        line = new Line();
        line.setStrokeWidth(1);
        line.setStroke(Paint.valueOf("red"));
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setVisible(false);
        pane.getChildren().add(line);

        pane.setOnMousePressed(t -> {
            setLinePoints(t.getX(), t.getY(), t.getX(), t.getY());
            pane.getChildren().remove(path);
        });

        pane.setOnMouseDragged(t -> {
            if (img != null) {
                isDragged = true;
                line.setVisible(true);
                setLineEndPoints(t.getX(), t.getY());
                pane.setCursor(Cursor.NONE);
            }
        });

        pane.setOnMouseReleased(t -> {
            if (!isDragged) {
                line.setVisible(false);
                sep.setVisible(false);
                out.setVisible(false);
                aluSize.setVisible(false);
            }
            else solve();

            isDragged = false;
            pane.setCursor(Cursor.CROSSHAIR);
        });

        pane.heightProperty().addListener(observable -> {
            double tmp2 = imgHeight;
            imgWidth = Math.min(imgPort.getFitWidth(), imgPort.getFitHeight() * aspectRatio);
            imgHeight = Math.min(imgPort.getFitHeight(), imgPort.getFitWidth() / aspectRatio);
            if (img != null) imgPort.setX(pane.getWidth() / 2 - imgWidth / 2);

            double imgResize = tmp2 - imgHeight;

            if (line != null && imgResize == 0)
                setLinePoints(line.getStartX(), line.getStartY() - imgResize, line.getEndX(), line.getEndY() - imgResize);
            else line.setVisible(false);
        });

        pane.widthProperty().addListener(observable -> {
            double tmp1 = imgPort.getX();
            double tmp2 = imgWidth;

            imgHeight = Math.min(imgPort.getFitHeight(), imgPort.getFitWidth() / aspectRatio);
            imgWidth = Math.min(imgPort.getFitWidth(), imgPort.getFitHeight() * aspectRatio);
            if (img != null) {
                imgPort.setX(pane.getWidth() / 2 - imgWidth / 2);
            }

            double layoutMove = tmp1 - imgPort.getX();
            double imgResize = tmp2 - imgWidth;

            if (line != null && imgResize == 0)
                setLinePoints(line.getStartX() - layoutMove, line.getStartY(), line.getEndX() - layoutMove, line.getEndY());
            else line.setVisible(false);
        });

    }

    /**
     * Přiřadí přímce a pomocným proměnným počáteční i koncové souřadnice přímky
     *
     * @param sX počáteční X
     * @param sY počáteční Y
     * @param eX koncové X
     * @param eY koncové Y
     */
    private void setLinePoints(double sX, double sY, double eX, double eY) {
        line.setStartX(sX);
        line.setStartY(sY);
        line.setEndX(eX);
        line.setEndY(eY);
    }

    /**
     * Přiřadí přímce koncové souřadnice přímky
     *
     * @param eX koncové X
     * @param eY koncové Y
     */
    private void setLineEndPoints(double eX, double eY) {
        setLinePoints(line.getStartX(), line.getStartY(), eX, eY);
    }

    /**
     * Z celé cesty k souboru zjistí jeho příponu
     *
     * @return přípona souboru
     */
    private String getExt() {
        String extension = "";

        int i = filePath.lastIndexOf('.');
        if (i > 0) {
            extension = filePath.substring(i + 1);
        }
        return extension;
    }
}
