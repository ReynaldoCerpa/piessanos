package viewControllers;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConsultasItem implements Initializable {
    @FXML
    private Label id;

    @FXML
    private Label fecha, atendido;

    @FXML
    private ImageView editButton;

    @FXML
    private ImageView deleteButton;
    @FXML
    private Button consultarButton;

    private Listener listener;

    @FXML
    private void editItem(ActionEvent event){
        listener.editListener(editButton.getId(), event);
    }

    @FXML
    private void nuevaConsulta(ActionEvent event) throws SQLException {
        listener.deleteListener(editButton.getId(), event);
    }
    @FXML
    public void openExpediente(ActionEvent event) throws SQLException {
        listener.showListener(editButton.getId(), event);
    }
    public void setData(Consulta item, Listener listener){

        this.listener = listener;
        editButton.setId(item.getNumCita());
        id.setText(item.getNumCita());
        fecha.setText(item.getFecha());
        atendido.setText(item.getAtendido());
        if (item.getAtendido().equals("si")){
            consultarButton.setVisible(false);
        }
    }

    public String getButtonID(){
        return editButton.getId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
