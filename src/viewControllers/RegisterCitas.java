package viewControllers;

import Model.Cita;
import com.mysql.cj.util.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RegisterCitas implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField pacienteInput, horaInput, minutoInput, domicilio;
    @FXML
    private DatePicker fechaInput;
    @FXML
    private RadioButton yes, no;
    @FXML
    private Label alertText, domiciliolabel, selectedItemLabel;
    @FXML
    private Group alertGroup, requiredGroup, pacientesListGroup;
    @FXML
    private Button registerPacienteButton;
    @FXML
    private ListView<String> itemList;
    private ArrayList<String> idArray = new ArrayList<>();
    private ArrayList<String> nameArray = new ArrayList<>();
    private String a_domicilio = "0";
    private String id = "", name = "";

    public RegisterCitas(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
        if (motor.getSelectedPacient()){
            id = motor.getPacientID();
            String pacientName = motor.getPacientName();
            selectedItemLabel.setText(id+" "+pacientName);
            pacientesListGroup.setDisable(true);
            registerPacienteButton.setDisable(true);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            domicilio.setText("Local");
            loadItems();
            itemList.getItems().addAll(nameArray);
            itemList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                    String item = itemList.getSelectionModel().getSelectedItem();
                    for (int i = 0; i < nameArray.size(); i++){
                        if (item.equals(nameArray.get(i))){
                            id = idArray.get(i);
                            break;
                        }
                    }
                    try {
                        selectedItemLabel.setText(findItem(id));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveItem(ActionEvent event) throws SQLException {

        alertText.setText("");
        if (fechaInput.getValue() == null || horaInput.getText().isEmpty() || minutoInput.getText().isEmpty()){
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
            boolean out = false, out2 = false, out3 = false, out4 = false, out5 = false;

            int size = 1;
            while(myRes.next()){
                size++;
                String time = motor.formatTime(horaInput.getText(), minutoInput.getText())+":00";
                String date = motor.formatDate(fechaInput);
                String fecha = myRes.getString("fecha");
                String hora = myRes.getString("hora");
                if (!StringUtils.isStrictlyNumeric(horaInput.getText()) || !StringUtils.isStrictlyNumeric(minutoInput.getText()) || (Integer.parseInt(horaInput.getText()) > 24) || (Integer.parseInt(horaInput.getText()) < 0)
                        || (Integer.parseInt(minutoInput.getText()) > 59) || (Integer.parseInt(minutoInput.getText()) < 0) && !out){
                    notfound = false;
                    out = true;
                    alertText.setText(alertText.getText() + "Hora invalida\n");
                    alertGroup.setVisible(true);
                    System.out.println("invalid data input descuento");
                }
                if(selectedItemLabel.getText().isEmpty() && !out3){
                    notfound = false;
                    out3 = true;
                    alertText.setText(alertText.getText() + "Seleccione un paciente\n");
                    alertGroup.setVisible(true);
                    System.out.println("Seleccione un paciente");
                }
                if((date.equals(fecha) && time.equals(hora)) && !out4){
                    notfound = false;
                    out4 = true;
                    alertText.setText(alertText.getText() + "Hora de cita ya reservada\n");
                    alertGroup.setVisible(true);
                    System.out.println("Hora de cita ya reservada");
                }
            }
            if (notfound){
                try{

                    String idCita = motor.generateID("C-",size);
                    String date = motor.formatDate(fechaInput);
                    String time = motor.formatTime(horaInput.getText(), minutoInput.getText());
                    String sql = "insert into cita "+"(idCita, hora, a_domicilio, domicilio, fecha, id_paciente)"
                            +" values (?,?,?,?,?,(select id from paciente where id=?))";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, idCita);
                    stmt.setString(2, time);
                    stmt.setString(3, a_domicilio);
                    stmt.setString(4, domicilio.getText());
                    stmt.setDate(5, Date.valueOf(date));
                    stmt.setString(6, id);
                    stmt.executeUpdate();
                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    if (motor.goBack()){
                        motor.showExpedienteUser(event);
                        motor.setSelectedPacient(false, "","");
                        motor.setGoBack(false);
                    } else {
                        motor.showCita(event);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelRegister(ActionEvent event) {
        if (motor.goBack()){
            String text = "¿Está seguro que desea cancelar el registro?";
            if (motor.confirmAction(text, "")){
                motor.showExpedienteUser(event);
                motor.setSelectedPacient(false, "","");
                motor.setGoBack(false);
            }
        } else {
            String text = "¿Está seguro que desea cancelar el registro?";
            if (motor.confirmAction(text, "")){
                motor.showCita(event);
            }
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

    public void loadItems() throws SQLException {
        ResultSet myRes = null;
        String id, name;
        try{
            myRes = database.connectSQL("paciente");
        } catch (Exception e){
            e.printStackTrace();
        }
        while (myRes.next()){
            id = myRes.getString("id");
            name = myRes.getString("id")+"      "+myRes.getString("nombre")
                        +" "+myRes.getString("nomPaterno") +" "+myRes.getString("nomMaterno");
            idArray.add(id);
            nameArray.add(name);
        }
    }

    public String findItem(String id) throws SQLException {
        ResultSet myRes = null;
        try{
            myRes = database.connectSQL("paciente");
        } catch (Exception e){
            e.printStackTrace();
        }
        while (myRes.next()){
            if (id.equals(myRes.getString("id"))){
                name = myRes.getString("nombre")+" "+myRes.getString("nomPaterno") +" "+myRes.getString("nomMaterno");
            }
        }
        return name;
    }

    public void showRegisterPaciente(ActionEvent event) {
        motor.setGoBack(true);
        motor.showRegisterPacient(event);
    }
}
