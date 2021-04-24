package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Medicamentos {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private Button registerMedicamentosButton, displayProveedoresButton;

    public Medicamentos(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;

        if (!motor.isAdmin()){
            registerMedicamentosButton.setVisible(false);
            displayProveedoresButton.setVisible(false);
        }
    }

    public void registerMedicamentos(ActionEvent event) {
        motor.showRegisterMedicamentos(event);
    }

    public void displayProveedores(ActionEvent event) {
        motor.showProveedores(event);
    }

    public void backToInventario(ActionEvent event) {
        motor.showInventory(event);
    }
}
