package viewControllers;

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
            if (motor.getSelectedItem().equals(myRes.getString("numCita"))){

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
                fechaInput.setAccessibleText(myRes.getString("fecha"));


                break;
            }
        }
    }

    public void saveItem(ActionEvent event) throws SQLException {
        alertText.setText("");
        if (horaInput.getText().equals("") || minutoInput.getText().equals("") || domicilio.getText().equals("")){
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
            boolean out = false;

            int size = 1;
            while (myRes.next()){
                size++;
            }
            while(myRes.next()){

            }
            if (notfound){
                try{
                    String date = motor.formatDate(fechaInput);
                    String time = motor.formatTime(horaInput.getText(), minutoInput.getText());
                    String sql = "update cita set hora = ?, a_domicilio = ?, domicilio = ?, fecha = ?"
                            +" where numCita = ?";
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
        motor.showCita(event);
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
