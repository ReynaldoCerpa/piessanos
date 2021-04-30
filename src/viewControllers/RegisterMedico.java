package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
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
            usernameInput, passwordInput, confirmPassword, telefonoInput, tipoTelefonoInput;
    @FXML
    private Label alertText;
    @FXML
    private Group alertGroup, requiredGroup;

    public RegisterMedico(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void saveRegisterMedico(ActionEvent event) throws SQLException {
        alertText.setText("");
        if (cpInput.getText().equals("") || numExtInput.getText().equals("") || ciudadInput.getText().equals("") || coloniaInput.getText().equals("") || calleInput.getText().equals("") || tipoTelefonoInput.getText().equals("") || telefonoInput.getText().equals("") || apellidopInput.getText().equals("") || apellidomInput.getText().equals("") || nombreInput.getText().equals("") || cedulaInput.getText().equals("") || usernameInput.getText().equals("") || passwordInput.getText().equals("")){
            alertGroup.setVisible(true);
            requiredGroup.setVisible(true);
            alertText.setText("Rellene todos los campos obligatorios\n");
        }else {
            ResultSet myRes = null;
            int counter = 0, counter2 = 0;
            try{
                myRes = database.connectSQL("medico");
            } catch (Exception e){
                e.printStackTrace();
            }

            boolean notfound = true;
            boolean out = false, out2 = false, out3 = false, out4 = false;

            while(myRes.next()){
                String username = myRes.getString("usuario");
                String cedula = myRes.getString("cedula_profesional");
                String password = myRes.getString("contrasena");
                if(username.equals(usernameInput.getText()) && !out){
                    notfound = false;
                    out = true;
                    alertText.setText(alertText.getText() + "Nombre de usuario existente\n");
                    alertGroup.setVisible(true);
                    System.out.println("username already taken");
                }
                if (cedula.equals(cedulaInput.getText()) && !out2){
                    notfound = false;
                    out2 = true;
                    alertText.setText(alertText.getText() + "Cedula existente\n");
                    alertGroup.setVisible(true);
                    System.out.println("cedula already taken");
                }
                if (!passwordInput.getText().equals(confirmPassword.getText()) && !out3){
                    notfound = false;
                    out3 = true;
                    alertText.setText(alertText.getText() + "Las contraseñas no coinciden\n");
                    alertGroup.setVisible(true);
                    System.out.println("passwords doesnt match");
                }
                if (telefonoInput.getText().length() != 10 && !out4){
                    notfound = false;
                    out4 = true;
                    alertText.setText(alertText.getText() + "Solo telefonos de 10 digitos\n");
                    alertGroup.setVisible(true);
                    System.out.println("solo telefonos de 10 digitos");
                }
            }
            if (notfound){
                try{
                    if (numIntInput.getText().equals("")){
                        numIntInput.setText("");
                    }
                    String sql = "insert into medico "+"(cedula_profesional, nombre, nomPaterno, nomMaterno, calle,"
                            +" num_ext, num_int, colonia, codigoPostal, ciudad, fecha_registro, contrasena, usuario, isAdmin)"
                            +" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement stmt = database.updateData(sql);
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
                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    String telefono = "insert into medico_telefono (numTelefono, tipo, cedula_profesional)"
                            + "values (?,?, (select cedula_profesional from medico where cedula_profesional=?))";
                    PreparedStatement telStmt = database.updateData(telefono);
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
