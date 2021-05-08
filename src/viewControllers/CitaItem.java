package viewControllers;

import Model.Cita;
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

public class CitaItem implements Initializable {
    @FXML
    private Label numcita;

    @FXML
    private Label paciente;

    @FXML
    private Label hora;

    @FXML
    private Label fecha;

    @FXML
    private Label lugar;

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

    public void setData(Cita cita, Listener listener){

        this.listener = listener;
        deleteButton.setId(cita.getNumCita());
        editButton.setId(cita.getNumCita());
        numcita.setText(cita.getNumCita());
        paciente.setText(cita.getId_paciente());
        hora.setText(cita.getHora());
        fecha.setText(cita.getFecha());
        lugar.setText(cita.getDomicilio());
    }
    public String getButtonID(){
        return editButton.getId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
