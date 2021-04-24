package viewControllers;

import javafx.event.ActionEvent;

public class ExpedienteUser {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public ExpedienteUser(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }
    public void newConsulta(ActionEvent event) {
        motor.showNewConsulta(event);
    }

    public void displayHome(ActionEvent event) {
        motor.showClient(event);
    }
}
