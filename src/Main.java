import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLNumericArray;
import com.jmatio.types.MLStructure;
import org.la4j.matrix.sparse.CCSMatrix;
import org.la4j.operation.ooplace.OoPlaceMatrixByVectorMultiplication;
import org.la4j.vector.DenseVector;
import org.la4j.vector.dense.BasicVector;

import java.io.IOException;
import java.util.Map;

/**
 * Created by eliam on 12/04/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        MatFileReader reader = new MatFileReader("poisson2D.mat");
        Map<String, MLArray> matlabMatrix = reader.getContent();

        MLStructure structure = (MLStructure) matlabMatrix.get("Problem");
        MLNumericArray bigA = (MLNumericArray) structure.getField("A");
        MLNumericArray smallB = (MLNumericArray) structure.getField("b");

        CCSMatrix matrix = new CCSMatrix(bigA.getM(), bigA.getN());
        for (int i=0; i<bigA.getM(); i++) {
            for (int j = 0; j<bigA.getN(); j++) {
                double value = bigA.get(i, j).doubleValue();

                if (value != 0)
                    matrix.set(i, j, value);
            }
        }

        printBigA(matrix, bigA.getN(), bigA.getM());
        printSmallB();

        double tas [] = new double[smallB.getM()];
        DenseVector vector = new BasicVector(tas);

        OoPlaceMatrixByVectorMultiplication test = new OoPlaceMatrixByVectorMultiplication();
        //System.out.println(test.apply(matrix, vector));
    }

    static private void printBigA(CCSMatrix matrix, int rows, int cols) {
        for (int i=0; i<rows; i++) {
            for (int j = 0; j<cols; j++) {
                double value = matrix.get(j, i);

                if (value != 0)
                    System.out.println((j + 1) + " - " + (i + 1) + " - " + value);
            }
        }
    }

    static private void printSmallB(double [] vals) {
        for (int i=0; i<vals.length; i++)
            System.out.println((i + 1) + " - " + vals[i]);
    }
}
