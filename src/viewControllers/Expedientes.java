package viewControllers;

import javafx.event.ActionEvent;

public class Expedientes {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public Expedientes(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void displayHome(ActionEvent event) {
        motor.showClient(event);
    }

    public void showExpediente(ActionEvent event) {
    }
}
