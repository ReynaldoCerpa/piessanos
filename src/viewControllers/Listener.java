package viewControllers;

import Model.Medico;
import javafx.event.ActionEvent;

import javax.swing.*;
import java.sql.SQLException;

public interface Listener {
    public void editListener(String id, ActionEvent event);
    public void deleteListener(String id, ActionEvent event) throws SQLException;
}