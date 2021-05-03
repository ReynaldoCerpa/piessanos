package viewControllers;

import Model.Medicamento;
import Model.Medico;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MedicamentosItem implements Initializable {
    @FXML
    private Label codigo;

    @FXML
    private Label nombre;

    @FXML
    private Label precio;

    @FXML
    private Label cantidad;

    @FXML
    private Label descripcion;

    @FXML
    private ImageView editButton;

    @FXML
    private ImageView deleteButton;

    private Listener listener;

    @FXML
    private void editMedicamento(ActionEvent event){
        listener.editListener(editButton.getId(), event);
    }

    @FXML
    private void deleteMedicamento(ActionEvent event) throws SQLException {
        listener.deleteListener(editButton.getId(), event);
    }

    public void setData(Medicamento medicamento, Listener listener){

        this.listener = listener;
        deleteButton.setId(medicamento.getCodigo());
        editButton.setId(medicamento.getCodigo());
        codigo.setText(medicamento.getCodigo());
        nombre.setText(medicamento.getNombre());
        precio.setText(medicamento.getPrecio());
        cantidad.setText(medicamento.getCantidadInventario());
        descripcion.setText(medicamento.getDescripcion());
    }
    public String getButtonID(){
        return editButton.getId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
