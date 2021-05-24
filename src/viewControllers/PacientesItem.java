package viewControllers;

import Model.Medicamento;
import Model.Medico;
import Model.Paciente;
import Model.Tratamiento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PacientesItem implements Initializable {
    @FXML
    private Label id;

    @FXML
    private Label nombre;

    @FXML
    private Label nomPaterno;

    @FXML
    private Label nomMaterno;

    @FXML
    private Label telefono;

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
    @FXML
    public void openExpediente(ActionEvent event) throws SQLException {
        listener.showListener(editButton.getId(), event);
    }
    public void setData(Paciente paciente, Listener listener){

        this.listener = listener;
        editButton.setId(paciente.getId());
        id.setText(paciente.getId());
        nombre.setText(paciente.getNombre());
        nomPaterno.setText(paciente.getNomPaterno());
        nomMaterno.setText(paciente.getNomMaterno());
        telefono.setText(paciente.getTelefono());
    }

    public String getButtonID(){
        return editButton.getId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
