package viewControllers;

import javafx.event.ActionEvent;

public class Tratamiento {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public Tratamiento(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void displayTratamientos(ActionEvent event) {
        motor.showTratamientos(event);
    }
}
