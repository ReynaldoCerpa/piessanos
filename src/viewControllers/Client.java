package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private Label usernameLabel;
    @FXML
    private Button administrarMedicosButton, tratamientosButton, promocionesButton;
    @FXML
    private ImageView promocionicon, tratamientoicon;

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;
        ResultSet myRes = null;
        try{
            myRes = database.connectSQL("medico");
            myRes.next();
        } catch (Exception e){
            e.printStackTrace();
        }
        String name = motor.getNombre()+" "+motor.getApellidop()+" "+motor.getApellidom();
        usernameLabel.setText(name);

        if (!motor.isAdmin()){
            administrarMedicosButton.setVisible(false);
            tratamientosButton.setVisible(false);
            promocionesButton.setVisible(false);
            promocionicon.setVisible(false);
            tratamientoicon.setVisible(false);
        }

    }

    public void logout(ActionEvent event) {
        motor.showLogin(event);
    }

    public void displayPacients(ActionEvent event) {
        motor.showPacientes(event);
    }
    public void displayPromociones(ActionEvent event) {
        motor.showPromocion(event);
    }

    public void displayInventario(ActionEvent event) {
        motor.showInventory(event);
    }

    public void displayExpedientes(ActionEvent event) {
        motor.showExpedienteUser(event);
    }

    public void displayCitas(ActionEvent event) {
        motor.showCita(event);
    }

    public void displayTratamientos(ActionEvent event) {
        motor.showTratamientos(event);
    }


    public void displayAdministrarMedicos(ActionEvent event) {
        motor.showAdministrarMedicos(event);
    }
}
