import java.util.ArrayList;

/**
 * Created by eliam on 12/04/2017.
 */
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

    @Override
    public String toString() {
        return "[row: " + getRowIndex() + ", col:" + getColIndex() + ", val: " + getValue() + "]";
    }

    public static String toString(ArrayList<SparseCell> cells) {
        StringBuilder ris = new StringBuilder();
        for (SparseCell cell : cells)
            ris.append(cell.toString()).append("\n");

        return ris.toString();
    }
}