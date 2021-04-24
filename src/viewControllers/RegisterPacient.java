package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RegisterPacient {
    private SQLconnector database = new SQLconnector();
    private Motor motor;

    @FXML
    private TextField nombreInput, apellidopInput, apellidomInput, tel1Input, tel2Input, alergiasInput, enfermedadesInput, medPrescritosInput;

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public RegisterPacient(){

    }

    public void saveRegister(ActionEvent event) {
        String sql = "insert into paciente"+"(id, nombre, nomPaterno, nomMaterno, numTelefono, tipo, )";
    }

    public void cancelRegister(ActionEvent event) {
        motor.cancelRegisterPacient(event);
    }
}
