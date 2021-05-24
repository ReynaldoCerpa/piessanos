package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditMedico {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField cedulaInput, nombreInput, apellidopInput, apellidomInput,
            calleInput, numExtInput, numIntInput, coloniaInput, cpInput, ciudadInput,
            telefonoInput, tipoTelefonoInput, usuarioInput;
    @FXML
    private PasswordField passwordInput, confirmPass;
    @FXML
    private Label alertText;
    @FXML
    private Group alertGroup, requiredGroup;
    private String currentUsername = "";

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;

        System.out.println(motor.getSelectedItem());
        ResultSet myRes = null, telRes = null;
        try {
            telRes = database.connectSQL("medico_telefono");
            myRes = database.connectSQL("medico");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(myRes.next() && telRes.next()){
            if (motor.getSelectedItem().equals(myRes.getString("cedula_profesional"))){
                cedulaInput.setText(myRes.getString("cedula_profesional"));
                nombreInput.setText(myRes.getString("nombre"));
                telefonoInput.setText(telRes.getString("numTelefono"));
                tipoTelefonoInput.setText(telRes.getString("tipo"));
                apellidopInput.setText(myRes.getString("nomPaterno"));
                apellidomInput.setText(myRes.getString("nomMaterno"));
                calleInput.setText(myRes.getString("calle"));
                numExtInput.setText(myRes.getString("num_ext"));
                System.out.println("num int: "+myRes.getString("num_int"));
                numIntInput.setText(myRes.getString("num_int"));
                coloniaInput.setText(myRes.getString("colonia"));
                cpInput.setText(myRes.getString("codigoPostal"));
                ciudadInput.setText(myRes.getString("ciudad"));
                usuarioInput.setText(myRes.getString("usuario"));
                currentUsername = myRes.getString("usuario");
                passwordInput.setText(myRes.getString("contrasena"));
                break;
            }
        }
    }

    public void saveRegisterMedico(ActionEvent event) throws SQLException {
        alertText.setText("");
        if (cpInput.getText().isEmpty() || numExtInput.getText().isEmpty() || ciudadInput.getText().isEmpty() || coloniaInput.getText().isEmpty()
                || calleInput.getText().isEmpty() || tipoTelefonoInput.getText().isEmpty() || telefonoInput.getText().isEmpty()
                || apellidopInput.getText().isEmpty() || apellidomInput.getText().isEmpty() || nombreInput.getText().isEmpty()
                || cedulaInput.getText().isEmpty() || usuarioInput.getText().isEmpty() || passwordInput.getText().isEmpty() || confirmPass.getText().isEmpty()){
            alertGroup.setVisible(true);
            requiredGroup.setVisible(true);
            alertText.setText("Rellene todos los campos obligatorios\n");
        }else {
            ResultSet myRes = null;

            try{
                myRes = database.connectSQL("medico");
            } catch (Exception e){
                e.printStackTrace();
            }

            boolean notfound = true;
            boolean out = false, out2 = false, out4 = false;

            while(myRes.next()){

                String usuario = myRes.getString("usuario");
                if (usuarioInput.getText().equals(usuario) && !usuarioInput.getText().equals(currentUsername) && !out){
                    notfound = false;
                    out = true;
                    alertText.setText(alertText.getText() + "Nombre de usuario en uso\n");
                    alertGroup.setVisible(true);
                    System.out.println("Nombre de usuario en uso");
                }
                if (!passwordInput.getText().equals(confirmPass.getText()) && !out2){
                    notfound = false;
                    out2 = true;
                    alertText.setText(alertText.getText() + "Las contraseñas no coinciden\n");
                    alertGroup.setVisible(true);
                    System.out.println("Las contraseñas no coinciden");
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
                    String sql = "update medico set cedula_profesional = ?, nombre = ?, nomPaterno = ?, nomMaterno = ?, calle = ?,"
                            +" num_ext = ?, num_int = ?, colonia = ?, codigoPostal = ?, ciudad = ?, usuario = ?, contrasena = ?"
                            +"where cedula_profesional = ?";
                    String id = motor.getSelectedItem();
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
                    stmt.setString(11,usuarioInput.getText());
                    stmt.setString(12,passwordInput.getText());
                    stmt.setString(13,id);
                    stmt.executeUpdate();
                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    String telefono = "update medico_telefono  set numTelefono = ?, tipo = ?, cedula_profesional = ?"
                            + "where cedula_profesional = ?";
                    PreparedStatement telStmt = database.updateData(telefono);
                    telStmt.setString(1, telefonoInput.getText());
                    telStmt.setString(2, tipoTelefonoInput.getText());
                    telStmt.setString(3, cedulaInput.getText());
                    telStmt.setString(4,id);
                    telStmt.executeUpdate();

                    motor.showAdministrarMedicos(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelRegisterMedico(ActionEvent event) {
        String text = "¿Está seguro que desea cancelar el registro?";
        if (motor.confirmAction(text, "")){
            motor.showAdministrarMedicos(event);
        }
    }
}
