import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLNumericArray;
import com.jmatio.types.MLStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by eliam on 12/04/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        MatFileReader reader = new MatFileReader("poisson2D.mat");
        Map<String, MLArray> matrix = reader.getContent();

        MLStructure prova = (MLStructure) matrix.get("Problem");
        MLNumericArray numericArray = (MLNumericArray) prova.getField("A");

        ArrayList<SparseCell> ris = new ArrayList<>();
        for (int i=0; i<numericArray.getM(); i++) {
            for (int j = 0; j<numericArray.getN(); j++) {
                double value = numericArray.get(i, j).doubleValue();

                if (value != 0) {
                    SparseCell newCell = new SparseCell(j, i, value);
                    ris.add(newCell);
                    System.out.println(newCell);
                }
            }
        }
    }
}
