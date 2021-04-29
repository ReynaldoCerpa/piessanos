package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EditMedico {
    private Motor motor;
    private AdministrarMedicos am;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField cedulaInput, nombreInput, apellidopInput, apellidomInput,
            calleInput, numExtInput, numIntInput, coloniaInput, cpInput, ciudadInput,
            usernameInput, passwordInput, confirmPassword, telefonoInput, tipoTelefonoInput;

    public EditMedico(AdministrarMedicos am) {
        this.am = am;
    }


    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;

        System.out.println(am.getSelectedMedico());
        ResultSet myRes = null, telRes = null;
        try {
            telRes = database.connectSQL("medico_telefono");
            myRes = database.connectSQL("medico");
            myRes.next();
            telRes.next();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(myRes.next()){

            if (am.getSelectedMedico().equals(myRes.getString("cedula_profesional"))){
                cedulaInput.setText(myRes.getString("cedula_profesional"));
                nombreInput.setText(myRes.getString("nombre"));
                telefonoInput.setText(telRes.getString("numTelefono"));
                tipoTelefonoInput.setText(telRes.getString("tipo"));
                usernameInput.setText(myRes.getString("usuario"));
                apellidopInput.setText(myRes.getString("nomPaterno"));
                apellidomInput.setText(myRes.getString("nomMaterno"));
                calleInput.setText(myRes.getString("calle"));
                numIntInput.setText(myRes.getString("num_int"));
                numExtInput.setText(myRes.getString("num_ext"));
                coloniaInput.setText(myRes.getString("colonia"));
                cpInput.setText(myRes.getString("codigoPostal"));
                ciudadInput.setText(myRes.getString("ciudad"));
                passwordInput.setText(myRes.getString("contrasena"));
                break;
            }
        }
    }

    public void saveRegisterMedico(ActionEvent event) {

    }

    public void cancelRegisterMedico(ActionEvent event) {
        motor.showAdministrarMedicos(event);
    }
}
