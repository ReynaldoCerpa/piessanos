package viewControllers;

import Model.Medicamento;
import Model.Medico;
import Model.Tratamiento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TratamientosItem implements Initializable {
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
    private void editItem(ActionEvent event){
        listener.editListener(editButton.getId(), event);
    }

    @FXML
    private void deleteItem(ActionEvent event) throws SQLException {
        listener.deleteListener(editButton.getId(), event);
    }

    public void setData(Tratamiento tratamiento, Listener listener){

        this.listener = listener;
        deleteButton.setId(tratamiento.getClave());
        editButton.setId(tratamiento.getClave());
        codigo.setText(tratamiento.getClave());
        nombre.setText(tratamiento.getNombre());
        precio.setText(tratamiento.getPrecio());
        descripcion.setText(tratamiento.getDescripcion());
    }
    public String getButtonID(){
        return editButton.getId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
