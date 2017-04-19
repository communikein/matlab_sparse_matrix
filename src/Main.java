import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLSparse;
import com.jmatio.types.MLStructure;

import org.la4j.matrix.sparse.CRSMatrix;
import org.la4j.operation.MatrixVectorOperation;
import org.la4j.operation.VectorVectorOperation;
import org.la4j.operation.ooplace.OoPlaceMatrixByVectorMultiplication;
import org.la4j.operation.ooplace.OoPlaceVectorsSubtraction;
import org.la4j.vector.DenseVector;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 *
 * Created by Elia Maracani on 12/04/2017.
 */
public class Main {

    private static final String FILE_NAME = "poisson3Db.mat";
    private static Date start = new Date(), now = new Date();

    public static void main(String[] args) throws IOException {
        printLog("Program started.");
        printLog("Loading file " + FILE_NAME + " in memory ...");
        MatFileReader reader = new MatFileReader(FILE_NAME);
        printLog("Completed");

        printLog("Retrieving matrix 'A' ...");
        Map<String, MLArray> matlabMatrix = reader.getContent();
        MLStructure structure = (MLStructure) matlabMatrix.get("Problem");
        MLSparse bigA = (MLSparse) structure.getField("A");
        printLog("Completed. Found matrix " + bigA.getM() + "x" + bigA.getN() + ".");

        printLog("Creating empty matrix 'A' ...");
        CRSMatrix matrix = CRSMatrix.zero(bigA.getN(), bigA.getM());
        printLog("Completed.");

        printLog("Retrieving non-zero values indexes ...");
        int rowsIndex[] = bigA.getIR();
        int colsIndex[] = bigA.getIC();
        printLog("Completed. There are " + rowsIndex.length + " values.");
        printLog("Setting matrix 'A' values based on non-zero values found ...");
        for (int i=0; i<rowsIndex.length; i++) {
            double val = bigA.get(rowsIndex[i], colsIndex[i]);

            matrix.set(rowsIndex[i], colsIndex[i], val);
            if (i % 100 == 0)
                printLog("## " + (i / rowsIndex.length * 100) + "%");
        }
        printLog("Completed.");

        printLog("Computing A*x ...");
        MatrixVectorOperation matrixVectorMultiply = new OoPlaceMatrixByVectorMultiplication();
        DenseVector x = DenseVector.constant(bigA.getM(), 1.0);
        DenseVector smallB = (DenseVector) matrix.multiply(x);
        printLog("Completed.");

        printLog("Computing A*b ...");
        DenseVector Xe = (DenseVector) matrixVectorMultiply.apply(matrix, smallB);
        printLog("Completed.");

        printLog("Computing x-Xe ...");
        VectorVectorOperation vectorVectorSub = new OoPlaceVectorsSubtraction();
        DenseVector ris = (DenseVector) vectorVectorSub.apply(x, Xe);
        printLog("Completed.");

        printLog("Computing ||x-Xe|| - ||Xe|| ...");
        double error = ris.norm() / Xe.norm();
        printLog("Completed");
        printLog("!!!! Error --> " + error + " <-- !!!!");
    }

    static private void printLog(String message) {
        String msg = (((new Date()).getTime() - start.getTime()) / 1000) + "s # " + message;

        System.out.println(msg);
    }

    static private void printBigA(CRSMatrix matrix, int[] rowsIndex, int[] colsindex) {
        for (int i=0; i<rowsIndex.length; i++) {
            int row = rowsIndex[i];
            int col = colsindex[i];

            if (matrix.nonZeroAt(row, col))
                System.out.println("--- ROW:" + row + " COL:" + col + " VAL:" + matrix.get(row, col));
        }
    }

    static private void printSmallB(DenseVector vals) {
        for (int i=0; i<vals.length(); i++)
            System.out.println((i + 1) + " - " + vals.get(i));
    }
}
