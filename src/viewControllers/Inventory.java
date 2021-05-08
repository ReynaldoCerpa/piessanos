package viewControllers;

import javafx.event.ActionEvent;

public class Inventory {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public Inventory(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void displayMedicamentos(ActionEvent event) {
        motor.showMedicamentos(event);
    }

    public void displayMateriales(ActionEvent event) {
        motor.showMateriales(event);
    }

    public void displayHome(ActionEvent event) {
        motor.showClient(event);
    }

    public void displayProveedores(ActionEvent event) {
        motor.showProveedores(event);
    }
}
