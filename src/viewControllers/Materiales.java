package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Materiales {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private Button registerMaterialesButton, displayProveedoresButton;

    public Materiales(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
        if (!motor.isAdmin()){
            registerMaterialesButton.setVisible(false);
            displayProveedoresButton.setVisible(false);
        }
    }

    public void registerMateriales(ActionEvent event) {
        motor.showRegisterMateriales(event);
    }

    public void displayProveedoresMateriales(ActionEvent event) {
        motor.showProveedoresMateriales(event);
    }

    public void backToInventario(ActionEvent event) {
        motor.showInventory(event);
    }
}
