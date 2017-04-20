import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLSparse;
import com.jmatio.types.MLStructure;

import org.la4j.Vector;
import org.la4j.linear.GaussianSolver;
import org.la4j.linear.LinearSystemSolver;
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

    private static final String FILE_NAME = "poisson3Da.mat";
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

        printLog("Retrieving non-zero values indexes ...");
        int rowsIndex[] = bigA.getIR();
        int colsIndex[] = bigA.getIC();
        Double values[] = bigA.exportReal();
        printLog("Completed. There are " + values.length + " values.");

        printLog("Creating empty matrix 'A' ...");
        SparseDoubleMatrix2D matrix = new SparseDoubleMatrix2D(values.length, values.length);
        //CRSMatrix matrix = CRSMatrix.zero(bigA.getN(), bigA.getM());
        printLog("Completed.");

        printLog("Setting matrix 'A' values based on non-zero values found ...");
        int prevPerc = 0;
        for (int i=0; i<rowsIndex.length; i++) {
            matrix.set(rowsIndex[i], colsIndex[i], values[i]);

            int newPerc = (int) ((double)i / (double)rowsIndex.length * 100.0);
            if (newPerc > prevPerc) {
                printLog("## " + newPerc + "%");
                prevPerc = newPerc;
            }
        }
        printLog("Completed.");

        printLog("Computing A*x ...");
        //Vector x = Vector.constant(bigA.getM(), 1.0);
        //Vector smallB = matrix * x;
        DoubleMatrix1D x = DoubleFactory1D.dense.make(matrix.columns(), 1);
        DoubleMatrix1D smallB = matrix.zMult(x, null);
        printLog("Completed.");

        printLog("Solving A with b ...");

        //LinearSystemSolver solver = new GaussianSolver(matrix);
        //printLog("POSSO? " + solver.applicableTo(matrix));
        //Vector Xe = solver.solve(smallB);
        Algebra algebra = new Algebra();
        DoubleMatrix1D Xe = ;
        printLog("Completed.");

        printLog("Computing x-Xe ...");
        Vector ris = x.subtract(Xe);
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
