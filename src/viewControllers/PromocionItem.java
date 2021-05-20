package viewControllers;

import Model.Cita;
import Model.Medicamento;
import Model.Medico;
import Model.Promocion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PromocionItem implements Initializable {
    @FXML
    private Label codigo;

    @FXML
    private Label nombre;

    @FXML
    private Label descuento;

    @FXML
    private Label fecha;

    @FXML
    private Label tratamiento;

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

    public void setData(Promocion item, Listener listener){

        this.listener = listener;
        deleteButton.setId(item.getCodigo());
        editButton.setId(item.getCodigo());
        codigo.setText(item.getCodigo());
        tratamiento.setText(item.getClave_tratamiento());
        nombre.setText(item.getNombre());
        fecha.setText(item.getFecha());
        descuento.setText(item.getDescuento());
    }
    public String getButtonID(){
        return editButton.getId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
