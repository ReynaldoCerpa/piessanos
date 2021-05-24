package viewControllers;

import com.mysql.cj.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class EditCita {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField horaInput, minutoInput, domicilio;
    @FXML
    private DatePicker fechaInput;
    @FXML
    private RadioButton yes, no;
    @FXML
    private Label alertText, domiciliolabel, selectedItemLabel;
    @FXML
    private Group alertGroup, requiredGroup;
    private String a_domicilio = "0";

    public EditCita(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;

        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("cita");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(myRes.next()){
            if (motor.getSelectedItem().equals(myRes.getString("idCita"))){

                String id = myRes.getString("id_paciente");

                String pacientequery = "select * from paciente where id = ?";
                PreparedStatement paciente = database.updateData(pacientequery);
                paciente.setString(1, id);
                ResultSet nombrePaciente = paciente.executeQuery();

                String fullname= "";
                while(nombrePaciente.next()) {
                    fullname = nombrePaciente.getString("nombre") + " " + nombrePaciente.getString("nomPaterno") + " " + nombrePaciente.getString("nomMaterno");
                }

                String fulltime = myRes.getString("hora");
                selectedItemLabel.setText(fullname);
                horaInput.setText(fulltime.charAt(0)+""+fulltime.charAt(1));
                minutoInput.setText(fulltime.charAt(3)+""+fulltime.charAt(4));
                String dom = myRes.getString("a_domicilio");
                System.out.println(dom);
                if (dom.equals("1")){
                    System.out.println("entro al equals");
                    domicilio.clear();
                    domicilio.setText(dom);
                    yes.setSelected(true);
                    no.setSelected(false);
                    domicilio.setVisible(true);
                    domiciliolabel.setVisible(true);
                }
                domicilio.setText(myRes.getString("domicilio"));
                String resDate = myRes.getString("fecha");
                fechaInput.setValue(LocalDate.parse(resDate));


                break;
            }
        }
    }

    public void saveItem(ActionEvent event) throws SQLException {
        alertText.setText("");
        if (fechaInput.getValue() == null || horaInput.getText().equals("") || minutoInput.getText().equals("") || domicilio.getText().equals("")){
            alertGroup.setVisible(true);
            requiredGroup.setVisible(true);
            alertText.setText("Rellene todos los campos obligatorios\n");
        }else {
            ResultSet myRes = null;
            try{
                myRes = database.connectSQL("cita");
            } catch (Exception e){
                e.printStackTrace();
            }

            boolean notfound = true;
            boolean out2 = false, out4 = false;

            int size = 1;
            while (myRes.next()){
                size++;
                if (!StringUtils.isStrictlyNumeric(horaInput.getText()) || !StringUtils.isStrictlyNumeric(minutoInput.getText()) || (Integer.parseInt(horaInput.getText()) > 24) || (Integer.parseInt(horaInput.getText()) < 0)
                        || (Integer.parseInt(minutoInput.getText()) > 59) || (Integer.parseInt(minutoInput.getText()) < 0) && !out2){
                    notfound = false;
                    out2 = true;
                    alertText.setText(alertText.getText() + "Hora invalida\n");
                    alertGroup.setVisible(true);
                    System.out.println("invalid data input descuento");
                }
            }
            while(myRes.next()){

            }
            if (notfound){
                try{
                    String date = motor.formatDate(fechaInput);
                    String time = motor.formatTime(horaInput.getText(), minutoInput.getText());
                    String sql = "update cita set hora = ?, a_domicilio = ?, domicilio = ?, fecha = ?"
                            +" where idCita = ?";
                    String id = motor.getSelectedItem();
                    PreparedStatement stmt = database.updateData(sql);

                    stmt.setString(1, time);
                    stmt.setString(2, a_domicilio);
                    stmt.setString(3, domicilio.getText());
                    stmt.setDate(4, Date.valueOf(date));
                    stmt.setString(5,id);
                    stmt.executeUpdate();
                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    motor.showCita(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    public void cancelRegister(ActionEvent event) {
        String text = "¿Está seguro que desea cancelar el registro?";
        if (motor.confirmAction(text, "")){
            motor.showCita(event);
        }
    }

    public void noRadioButton(ActionEvent event) {
        yes.setSelected(false);
        domicilio.setVisible(false);
        domicilio.clear();
        domiciliolabel.setVisible(false);
        domicilio.setText("Local");
        a_domicilio = "0";
    }

    public void yesRadioButton(ActionEvent event) {
        no.setSelected(false);
        domicilio.clear();
        domicilio.setVisible(true);
        domiciliolabel.setVisible(true);
        a_domicilio = "1";
    }
}
