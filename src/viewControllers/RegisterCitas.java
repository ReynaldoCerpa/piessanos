package viewControllers;

import Model.Cita;
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
    private Group alertGroup, requiredGroup;
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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
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
        String date = motor.formatDate(fechaInput);
        String time = motor.formatTime(horaInput.getText(), minutoInput.getText());
        alertText.setText("");
        if (date.equals("") || horaInput.getText().equals("") || minutoInput.getText().equals("")){
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
            boolean out = false, out2 = false, out3 = false;

            int size = 1;
            while(myRes.next()){
                size++;

            }
            if (notfound){
                try{

                    String sql = "insert into cita "+"(numcita, hora, a_domicilio, domicilio, fecha, id_paciente)"
                            +" values (?,?,?,?,?,(select id from paciente where id=?))";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setInt(1, size);
                    stmt.setString(2, time);
                    stmt.setString(3, a_domicilio);
                    stmt.setString(4, domicilio.getText());
                    stmt.setDate(5, Date.valueOf(date));
                    stmt.setString(6, id);
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
        a_domicilio = "0";
    }

    public void yesRadioButton(ActionEvent event) {
        no.setSelected(false);
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
}
