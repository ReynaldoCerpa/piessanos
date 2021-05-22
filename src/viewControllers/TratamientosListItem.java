package viewControllers;

import Model.Tratamiento;
import Model.TratamientoList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TratamientosListItem implements Initializable {
    @FXML
    private Label id;

    @FXML
    private Label nombre;

    @FXML
    private Label precio;

    @FXML
    private RadioButton TRTrb;

    @FXML
    private ImageView deleteButton;

    private Listener listener;

    @FXML
    private void selectListener(ActionEvent event) throws SQLException {
        listener.selectListener(TRTrb.getId(), TRTrb.isSelected(),  event);
    }

    public void setData(TratamientoList tratamiento, Listener listener){

        this.listener = listener;
        TRTrb.setId(tratamiento.getId());
        id.setText(tratamiento.getId());
        nombre.setText(tratamiento.getNombre());
        precio.setText(tratamiento.getPrecio());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
