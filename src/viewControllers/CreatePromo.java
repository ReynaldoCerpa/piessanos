package viewControllers;

import javafx.event.ActionEvent;

public class CreatePromo {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public CreatePromo(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void cancelCreatePromo(ActionEvent event) {
        String text = "¿Está seguro que desea cancelar el registro?";
        if (motor.confirmAction(text, "")){
            motor.showPromo(event);
        }
    }


}
