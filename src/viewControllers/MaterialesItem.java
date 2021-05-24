package viewControllers;

import Model.Material;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MaterialesItem implements Initializable {
    @FXML
    private Label numInventario;

    @FXML
    private Label nombre;

    @FXML
    private Label precio;

    @FXML
    private Label cantidad;

    @FXML
    private Label descripcion, proveedor, precioproveedor;

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

    public void setData(Material material, Listener listener){

        this.listener = listener;
        deleteButton.setId(String.valueOf(material.getId()));
        editButton.setId(String.valueOf(material.getId()));
        numInventario.setText(String.valueOf(material.getId()));
        nombre.setText(material.getNombre());
        cantidad.setText(material.getCantidadInventario());
        proveedor.setText(material.getProveedor());
        precioproveedor.setText(material.getPrecioproveedor());
    }
    public String getButtonID(){
        return editButton.getId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
