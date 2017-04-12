import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLNumericArray;
import com.jmatio.types.MLStructure;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by eliam on 12/04/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        MatFileReader reader = new MatFileReader("poisson2D.mat");
        Map<String, MLArray> matrix = reader.getContent();

        MLStructure prova = (MLStructure) matrix.get("Problem");
        MLNumericArray numericArray = (MLNumericArray) prova.getField("A");

        for (int i=0; i<numericArray.getM(); i++) {
            for (int j = 0; i < numericArray.getN(); j++) {
                double value = numericArray.get(i, j).doubleValue();

                if (value != 0) {

                }
            }
        }

        //System.out.println(mat);
    }


    public class SparseCell {
        private int rowIndex;
        private int colIndex;
        private double value;

        public SparseCell(int row, int col, double value) {
            setColIndex(col);
            setRowIndex(row);
            setValue(value);
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public void setRowIndex(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        public int getColIndex() {
            return colIndex;
        }

        public void setColIndex(int colIndex) {
            this.colIndex = colIndex;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}
