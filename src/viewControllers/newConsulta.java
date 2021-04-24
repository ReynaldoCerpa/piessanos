package viewControllers;

import javafx.event.ActionEvent;

public class newConsulta {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public newConsulta(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }
    public void saveNewConsulta(ActionEvent event) {
        motor.showExpedienteUser(event);
    }
}
