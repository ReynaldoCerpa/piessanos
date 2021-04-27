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

    private Listener listener;
    private MedicoItem medicoItem;

    @FXML
    private void editMedico(MouseEvent mouseEvent){
        listener.onClickListener(medicoItem);
    }

    @FXML
    private void deleteMedico(MouseEvent mouseEvent) {

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
