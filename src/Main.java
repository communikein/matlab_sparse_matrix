import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLNumericArray;
import com.jmatio.types.MLStructure;

import java.io.IOException;
import java.util.Map;

/**
 * Created by eliam on 12/04/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        MatFileReader reader = new MatFileReader("poisson2D.mat");
        Map<String, MLArray> matrix = reader.getContent();

        for (String key : matrix.keySet()) {
            MLStructure prova = (MLStructure) matrix.get(key);
            MLNumericArray numericArray = (MLNumericArray) prova.getField("A");

            System.out.println(prova.getField("A").contentToString());
            System.out.println(numericArray.get(116, 0));
        }
    }
}
