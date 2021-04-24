package viewControllers;

import javafx.event.ActionEvent;

import java.sql.SQLException;

public class Pacients {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public Pacients(){

    }
    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }
    public void registerPacient(ActionEvent event) {
        motor.showRegisterPacient(event);
    }
    public void displayHome(ActionEvent event){
        motor.showClient(event);
    }
}
