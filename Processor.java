import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import java.util.*;

/**
 * Created by Lukado on 23. 11. 2017.
 */
class Processor {
    private double capSize=0;

    /**
     * Konstruktor pro výpočet výběru velikosti kloubu protézy. Výsledek vypisuje do GUI.
     * @param line přímka
     * @param imgRatio ratio obrázku
     * @param templateRatio ratio šablony
     * @param l výstupní label
     */
    Processor(Line line, double imgRatio, double templateRatio, Label l) {
        HashMap<Double, String> caps = initAlofitSizes();
        double capLength = getLineToCm(line, imgRatio, templateRatio);

        double lower = Double.MAX_VALUE;
        for (Map.Entry<Double, String> entry : caps.entrySet()){
            double tmp = Math.abs(capLength-entry.getKey());
            if (tmp<lower) {
                lower = tmp;
                capSize = entry.getKey();
            }
        }
        l.setText(caps.get(capSize));
    }

    /**
     * Inicializace výstupních velikostí protéz do mapy
     * @return
     */
    private HashMap<Double, String> initAlofitSizes(){
        HashMap<Double, String> aloSizes = new HashMap<Double, String>(){};
        aloSizes.put(4.6,"46/FF");
        aloSizes.put(4.8,"48/GG");
        aloSizes.put(5.0,"50/HH");
        aloSizes.put(5.2,"52/II");
        aloSizes.put(5.4,"54/JJ");
        aloSizes.put(5.6,"56/KK");
        aloSizes.put(5.8,"58/LL");
        aloSizes.put(6.0,"60/MM");
        aloSizes.put(6.2,"62/NN");
        aloSizes.put(6.4,"64/OO");
        aloSizes.put(6.6,"66/PP");
        aloSizes.put(6.8,"68/QU");

        return aloSizes;
    }

    /**
     * Převod délky zakreslené přímky na centimetry
     * @param line přímka
     * @param imgRatio poměr obrázku
     * @param templateRatio poměr šablony
     * @return velikost přepočtené přímky
     */
    private double getLineToCm(Line line, double imgRatio, double templateRatio){
        double lineLength = Math.sqrt(Math.pow(line.getEndX()-line.getStartX(), 2)
                + Math.pow(line.getEndY()-line.getStartY(), 2))*imgRatio; // delka rezu

//        prevedeno na cm, PPI = 127 = sqrt(1920^2+1080^2)/17.3 - 17.3 velikost mého ntb displeje, pure CM na ntb
//        double lineToMetrics = (lineLength * 2.54 / 127);
//        double pixToCm = (1 / 25.4) * 96;

        double templateOneCm = 94*imgRatio/templateRatio;
        //0.2 magická konstanta která převádí cm výsledky do podoby na alofit papírech
        return lineLength/templateOneCm+0.2;
    }

    public double getCapSize(){
        return capSize;
    }
}
