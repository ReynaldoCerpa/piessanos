package viewControllers;

import Model.Medico;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
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

    public void setData(Medico medico){

        editButton.setId(medico.getCedula());
        deleteButton.setId(medico.getCedula());
        cedula.setText(medico.getCedula());
        nombre.setText(medico.getNombre());
        nomPaterno.setText(medico.getNomPaterno());
        nomMaterno.setText(medico.getNomMaterno());
        telefono.setText(medico.getTelefono());
        usuario.setText(medico.getUsuario());


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void editMedico(MouseEvent mouseEvent) {

    }

    public void deleteMedico(MouseEvent mouseEvent) {

    }
}
