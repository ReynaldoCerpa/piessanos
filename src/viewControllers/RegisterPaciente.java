package viewControllers;

import com.mysql.cj.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterPaciente {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField nombreInput, apellidopInput, apellidomInput, tel1Input, tel2Input;
    @FXML
    private TextArea alergiasInput, enfermedadesInput, medPrescritosInput;
    @FXML
    private Label alertText;
    @FXML
    private Group alertGroup, requiredGroup;
    @FXML
    private ListView itemList;

    public RegisterPaciente(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void saveItem(ActionEvent event) throws SQLException {
        alertText.setText("");
        if (nombreInput.getText().equals("") || apellidopInput.getText().equals("") || apellidomInput.getText().equals("") || tel1Input.getText().equals("") || alergiasInput.getText().equals("") || enfermedadesInput.getText().equals("") || medPrescritosInput.getText().equals("")){
            alertGroup.setVisible(true);
            requiredGroup.setVisible(true);
            alertText.setText("Rellene todos los campos obligatorios\n");
        }else {
            ResultSet myRes = null, telRes = null, enfRes = null, medRes = null, aleRes = null;
            try{
                myRes = database.connectSQL("paciente");
                telRes = database.connectSQL("paciente_telefono");
                enfRes = database.connectSQL("expediente_enfermedad");
                medRes = database.connectSQL("expediente_medicamentoPrescrito");
                aleRes = database.connectSQL("expediente_alergia");
            } catch (Exception e){
                e.printStackTrace();
            }

            boolean notfound = true;
            boolean out = false, out2 = false, out3 = false;

            int size = 1;
            while(myRes.next() && telRes.next()){
                size++;
                String tel = telRes.getString("numTelefono");
                if (tel1Input.equals(tel) && !out){
                    notfound = false;
                    out = true;
                    alertText.setText(alertText.getText() + "Telefono ya existente\n");
                    alertGroup.setVisible(true);
                    System.out.println("Telefono ya existente");
                }
                if (tel1Input.getText().length() != 10 && !out2){
                    notfound = false;
                    out2 = true;
                    alertText.setText(alertText.getText() + "Solo telefonos de 10 digitos\n");
                    alertGroup.setVisible(true);
                    System.out.println("solo telefonos de 10 digitos");
                }
            }
            if (notfound){
                try{
                    String id = motor.generateID("P-",size);
                    String exp_id = motor.generateID("EP-",size);
                    String sql = "insert into paciente "+"(id, nombre, nomPaterno, nomMaterno)"
                            +" values (?,?,?,?)";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, id);
                    stmt.setString(2, nombreInput.getText());
                    stmt.setString(3, apellidopInput.getText());
                    stmt.setString(4, apellidomInput.getText());
                    stmt.executeUpdate();

                    String expediente = "insert into expediente "+"(id, fecha_creacion, id_paciente)"
                            +" values (?,?,(select id from paciente where id=?))";
                    PreparedStatement expStmt = database.updateData(expediente);
                    expStmt.setString(1, exp_id);
                    expStmt.setString(2, String.valueOf(motor.formatCurrDate()));
                    expStmt.setString(3, id);
                    expStmt.executeUpdate();

                    String telefono = "insert into paciente_telefono (numTelefono, tipo, id_paciente)"
                            + "values (?,?, (select id from paciente where id=?))";
                    PreparedStatement telStmt = database.updateData(telefono);
                    telStmt.setString(1, tel1Input.getText());
                    telStmt.setString(2, "celular");
                    telStmt.setString(3, id);
                    telStmt.executeUpdate();

                    String alergias = "insert into expediente_alergia (id, nombre, descripcion, id_expediente)"
                            + "values (?, ?, ?, (select id from expediente where id=?))";
                    PreparedStatement aleStmt = database.updateData(alergias);
                    aleStmt.setString(1, exp_id+size);
                    aleStmt.setString(2, alergiasInput.getText());
                    aleStmt.setString(3, "descripcion");
                    aleStmt.setString(4, exp_id);
                    aleStmt.executeUpdate();

                    String medicamento = "insert into expediente_medicamentoPrescrito (id, nombre, descripcion, id_expediente)"
                            + "values (?, ?, ?, (select id from expediente where id=?))";
                    PreparedStatement medStmt = database.updateData(medicamento);
                    medStmt.setString(1, exp_id+size);
                    medStmt.setString(2, medPrescritosInput.getText());
                    medStmt.setString(3, "descripcion");
                    medStmt.setString(4, exp_id);
                    medStmt.executeUpdate();

                    String enfermedad = "insert into expediente_enfermedad (id, nombre, descripcion, id_expediente)"
                            + "values (?, ?, ?, (select id from expediente where id=?))";
                    PreparedStatement enfStmt = database.updateData(enfermedad);
                    enfStmt.setString(1, exp_id+size);
                    enfStmt.setString(2, enfermedadesInput.getText());
                    enfStmt.setString(3, "descripcion");
                    enfStmt.setString(4, exp_id);
                    enfStmt.executeUpdate();

                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    if (motor.goBack()){
                        motor.showRegisterCita(event);
                        motor.setGoBack(false);
                    } else {
                        motor.showPacientes(event);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelRegister(ActionEvent event) {
        if (motor.goBack()){
            motor.showRegisterCita(event);
            motor.setGoBack(false);
        } else {
            motor.showPacientes(event);
        }
    }

}
