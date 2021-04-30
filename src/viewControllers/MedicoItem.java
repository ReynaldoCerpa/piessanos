package viewControllers;

import Model.Medico;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MedicoItem implements Initializable {
    @FXML
    private Label cedula;
    @FXML
    private Label nombre;
    @FXML
    private Label telefono;
    @FXML
    private Label usuario;
    @FXML
    private ImageView editButton;
    @FXML
    private ImageView deleteButton;
    @FXML
    private Label nomPaterno;
    @FXML
    private Label nomMaterno;

    private Listener listener;

    @FXML
    private void editMedico(ActionEvent event){
        listener.editListener(editButton.getId(), event);
    }

    @FXML
    private void deleteMedico(ActionEvent event) throws SQLException {
        listener.deleteListener(editButton.getId(), event);
    }

    public void setData(Medico medico, Listener listener){

        this.listener = listener;
        deleteButton.setId(medico.getCedula());
        editButton.setId(medico.getCedula());
        nomPaterno.setText(medico.getNomPaterno());
        nomMaterno.setText(medico.getNomMaterno());
        telefono.setText(medico.getTelefono());
        usuario.setText(medico.getUsuario());
        cedula.setText(medico.getCedula());
        nombre.setText(medico.getNombre());
    }
    public String getButtonID(){
        return editButton.getId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
