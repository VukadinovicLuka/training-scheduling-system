package nikolalukatrening.GUI2.customTable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

public class CustomTable extends JTable {
    public CustomTable(TableModel model) {
        super(model);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        // Assume the isActivated status is in the 8th column (index 7 since it's 0-based)
        // if table has more than 8 columns

        if (column >= 8){
            if (!Boolean.TRUE.equals(getValueAt(row, 8))) {
                c.setBackground(Color.RED);
            } else {
                c.setBackground(Color.GREEN);
            }
        }
        return c;
    }
}

