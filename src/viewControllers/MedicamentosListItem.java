package viewControllers;

import Model.Medicamento;
import Model.MedicamentoList;
import Model.Tratamiento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MedicamentosListItem implements Initializable {
    @FXML
    private Label id;

    @FXML
    private Label nombre;

    @FXML
    private Label precio;

    @FXML
    private RadioButton MEDrb;

    @FXML
    private ImageView deleteButton;

    private Listener listener;

    @FXML
    private void selectListener(ActionEvent event) throws SQLException {
        listener.selectListener(MEDrb.getId(), MEDrb.isSelected(), event);
    }

    public void setData(MedicamentoList medicamento, Listener listener){

        this.listener = listener;
        MEDrb.setId(medicamento.getId());
        id.setText(medicamento.getId());
        nombre.setText(medicamento.getNombre());
        precio.setText(medicamento.getPrecio());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
