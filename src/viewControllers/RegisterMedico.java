package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.*;
import java.text.SimpleDateFormat;

public class RegisterMedico {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField cedulaInput, nombreInput, apellidopInput, apellidomInput,
            calleInput, numExtInput, numIntInput, coloniaInput, cpInput, ciudadInput,
            usernameInput, passwordInput, telefonoInput, tipoTelefonoInput;
    @FXML
    private Label usernameAlert;

    public RegisterMedico(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void saveRegisterMedico(ActionEvent event) throws SQLException {
        //validar que no haya input nulos ademas del num_Int
        if (usernameInput.getText().equals("") && passwordInput.getText().equals("")){

        } else {

            ResultSet myRes = null;
            try{
                myRes = database.connectSQL("medico");
            } catch (Exception e){
                e.printStackTrace();
            }

            boolean notfound = true;

            while(myRes.next()){
                String username = myRes.getString("usuario");
                if(username.equals(usernameInput.getText())){
                    notfound = false;
                    usernameAlert.setVisible(true);
                    System.out.println("username already taken");
                    break;
                }
            }
            if (notfound){
                try{
                    if (numIntInput.getText().equals("")){
                        numIntInput.setText(null);
                    }
                    String sql = "insert into medico "+"(cedula_profesional, nombre, nomPaterno, nomMaterno, calle,"
                            +" num_ext, num_int, colonia, codigoPostal, ciudad, fecha_registro, contrasena, usuario, isAdmin)"
                            +" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement stmt = database.insertData(sql);
                    stmt.setString(1,cedulaInput.getText());
                    stmt.setString(2,nombreInput.getText());
                    stmt.setString(3,apellidopInput.getText());
                    stmt.setString(4,apellidomInput.getText());
                    stmt.setString(5,calleInput.getText());
                    stmt.setString(6,numExtInput.getText());
                    stmt.setString(7,numIntInput.getText());
                    stmt.setString(8,coloniaInput.getText());
                    stmt.setString(9,cpInput.getText());
                    stmt.setString(10,ciudadInput.getText());
                    stmt.setDate(11, new Date(0));
                    stmt.setString(12,passwordInput.getText());
                    stmt.setString(13,usernameInput.getText());
                    stmt.setString(14,"0");
                    stmt.executeUpdate();
                    usernameAlert.setVisible(false);

                    String telefono = "insert into medico_telefono (numTelefono, tipo, cedula_profesional)"
                            + "values (?,?, (select cedula_profesional from medico where cedula_profesional=?))";
                    PreparedStatement telStmt = database.insertData(telefono);
                    telStmt.setString(1, telefonoInput.getText());
                    telStmt.setString(2, tipoTelefonoInput.getText());
                    telStmt.setString(3, cedulaInput.getText());
                    telStmt.executeUpdate();

                    motor.showAdministrarMedicos(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void cancelRegisterMedico(ActionEvent event) {
        motor.showAdministrarMedicos(event);
    }
}
