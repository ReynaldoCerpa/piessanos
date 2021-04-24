package viewControllers;

import javafx.event.ActionEvent;

public class Tratamientos {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public Tratamientos(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void displayHome(ActionEvent event) {
        motor.showClient(event);
    }

    public void showTratamiento(ActionEvent event) {
        motor.showTratamiento(event);
    }
}
