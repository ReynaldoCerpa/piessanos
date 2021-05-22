package viewControllers;


import Model.Proveedor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProveedoresItem implements Initializable {
    @FXML
    private Label id;

    @FXML
    private Label nombre;

    @FXML
    private Label direccion;

    @FXML
    private Label telefono;

    @FXML
    private ImageView editButton;

    @FXML
    private ImageView deleteButton;

    private Listener listener;

    @FXML
    private void editProveedor(ActionEvent event){
        listener.editListener(editButton.getId(), event);
    }

    @FXML
    private void deleteProveedor(ActionEvent event) throws SQLException {
        listener.deleteListener(editButton.getId(), event);
    }

    public void setData(Proveedor proveedor, Listener listener){

        this.listener = listener;
        deleteButton.setId(proveedor.getid());
        editButton.setId(proveedor.getid());
        id.setText(proveedor.getid());
        nombre.setText(proveedor.getNombre());
        direccion.setText(proveedor.getCalle()+", "+proveedor.getColonia()+", "+proveedor.getCiudad()+", codigo postal: "+proveedor.getCodigoPostal());
        telefono.setText(proveedor.getTelefono());
    }
    public String getButtonID(){
        return editButton.getId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
