package viewControllers;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Expediente implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private VBox itemsLayout;
    @FXML
    private Label id, nombre, alert;
    @FXML
    private TextArea alergias, medicamentos, enfermedades, antecedentes;
    @FXML
    private Button cancelButton, saveButton;
    @FXML
    private Label alertText;
    @FXML
    private Group alertGroup, requiredGroup, pacientesListGroup;
    @FXML
    private TextField searchInput;
    private String idPaciente = "empty";
    private List<Consulta> itemList = new ArrayList<>();
    private List<Consulta> items = null;
    private List<Consulta> searchList = new ArrayList<>();
    private Listener listener;
    private String name, fullname;

    public Expediente(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;

        ResultSet myRes = null, ale = null, med = null, enf = null, cons = null;
        try {
            myRes = database.connectSQL("paciente");
            ale = database.connectSQL("expediente_alergia");
            med = database.connectSQL("expediente_medicamentoPrescrito");
            enf = database.connectSQL("expediente_enfermedad");
        } catch (Exception e) {
            e.printStackTrace();
        }

         /* && cons.next()*/
        while(myRes.next() && ale.next() && med.next() && enf.next()){
            if (motor.getSelectedItem().equals(String.valueOf(myRes.getString("id")))){
                fullname = myRes.getString("nombre")+" "+myRes.getString("nomPaterno")+" "+myRes.getString("nomMaterno");
                idPaciente = myRes.getString("id");

                id.setText(myRes.getString("id"));
                nombre.setText(fullname);
                alergias.setText(ale.getString("nombre"));
                medicamentos.setText(med.getString("nombre"));
                enfermedades.setText(enf.getString("nombre"));

                String observaciones = "select * from consulta where id_paciente = ? order by observaciones desc limit 1";
                PreparedStatement obs = database.updateData(observaciones);
                obs.setString(1, idPaciente);
                ResultSet obsRes = obs.executeQuery();
                while(obsRes.next()){
                    antecedentes.setText(obsRes.getString("observaciones")+"\n");
                }
                break;
            }
        }

        items();
        loadItems(itemList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listener = new Listener(){
            @Override
            public void editListener(String id, ActionEvent event){

            }
            @Override
            public void deleteListener(String id, ActionEvent event) throws SQLException{
                if (!isAtendido(id)){
                    motor.showConsultas(event);
                } else {
                    String text = "Este paciente ya ha sido atendido";
                    String content = "¿Desea agendar una cita?";
                    if (motor.confirmAction(text, content)){
                        motor.setSelectedPacient(true, idPaciente, fullname);
                        motor.showRegisterCita(event);
                        motor.setGoBack(true);
                    }
                }
            }
            @Override
            public void showListener(String id, ActionEvent event) throws SQLException {

                if (showConsulta(id)){
                    setChosenItem(id);
                    motor.showConsulta(event);
                } else {
                    String text = "Este paciente no ha sido atendido";
                    if (motor.confirmAction(text, "")){
                    }
                }
            }

            @Override
            public void selectListener(String id, boolean isSelected, ActionEvent event) {

            }
        };

    }
    private void setChosenItem(String id){
        System.out.println("selected: "+ id);
        motor.setSelectedItem(id);
    }
    private List<Consulta> items() throws SQLException {

        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("cita");


        } catch (Exception e) {
            e.printStackTrace();
        }

        while (myRes.next()) {
                if (idPaciente.equals(myRes.getString("id_paciente"))) {
                    String id = myRes.getString("idCita");
                    String fecha = myRes.getString("fecha");
                    String atendido = myRes.getString("atendido");

                    Consulta newItem = defineItem(id, fecha, atendido);
                    itemList.add(newItem);
                }
        }
        return itemList;
    }

    public Consulta defineItem(String id, String fecha, String atendido) {
        Consulta item = new Consulta();
        item.setNumCita(id);
        item.setFecha(fecha);
        item.setAtendido(atendido);
        return item;
    }

    public void loadItems(List array) throws SQLException {

        items = new ArrayList<>(array);
        for (int i = 0; i < items.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ConsultasItem.fxml"));

            try {
                HBox hbox = fxmlLoader.load();
                ConsultasItem mi = fxmlLoader.getController();
                mi.setData(items.get(i),listener);
                itemsLayout.getChildren().add(hbox);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerItem(ActionEvent event) {
        motor.showRegisterPacient(event);
    }

    public void backHome(ActionEvent event) {
        motor.showPacientes(event);
    }

    public void searchItem(ActionEvent event) throws SQLException {
        String search = searchInput.getText();
        if (searchInput.equals("")) {

        } else {
            boolean found = false;
            searchList.clear();
            itemsLayout.getChildren().clear();
            for (int i = 0; i < itemList.size(); i++) {
                if (search.contains(itemList.get(i).getNumCita()) || search.contains(itemList.get(i).getFecha())) {
                    found = true;
                    Consulta foundItem = itemList.get(i);
                    searchList.add(foundItem);
                }
            }
            loadItems(searchList);
            if (!found){
                System.out.println("no matches found");
            }
        }
    }

    public void deleteSearch(MouseEvent mouseEvent) throws SQLException {
        searchInput.clear();
        itemsLayout.getChildren().clear();
        loadItems(itemList);
    }

    public void deleteItem(String id, ActionEvent event) throws SQLException {
        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("cita");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (myRes.next()) {
            if (id.equals(myRes.getString("idCita"))){
                try{
                    String sql = "delete from cita where idCita= ? ";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, id);
                    stmt.executeUpdate();

                    motor.setSelectedItem(idPaciente);
                    motor.showExpedienteUser(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    public String findItem(String id) throws SQLException {
        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("cita");
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(myRes.next()){
            if (id.equals(myRes.getString("idCita"))){
                name = myRes.getString("idCita");
                break;
            }
        }
        return name;
    }

    public boolean isAtendido(String id) throws SQLException {
        boolean res = false;
        String atendido = "si";

        String citaquery = "select atendido from cita where idCita = ?";

        PreparedStatement cita = database.updateData(citaquery);

        cita.setString(1, id);

        ResultSet numcita = cita.executeQuery();
        while(numcita.next()){
            atendido = numcita.getString("atendido");
        }
        if (atendido.equals("si")){
            res = true;
        }

        return res;
    }
    public boolean showConsulta(String id) throws SQLException{
        boolean res = false;
        String atendido = "";

        String citaquery = "select atendido from cita where idCita = ?";

        PreparedStatement cita = database.updateData(citaquery);

        cita.setString(1, id);

        ResultSet numcita = cita.executeQuery();
        while(numcita.next()){
            atendido = numcita.getString("atendido");
        }
        if (atendido.equals("si")){
            res = true;
        }

        return res;
    }

    public void cancelChanges(ActionEvent event) {
        String text = "¿Está seguro que desea cancelar el registro?";
        if (motor.confirmAction(text, "")){
            motor.showExpedienteUser(event);
        }
    }

    public void saveChanges(ActionEvent event) throws SQLException {

        alertText.setText("");
        if (alergias.getText().isEmpty() || enfermedades.getText().isEmpty() || medicamentos.getText().isEmpty()){
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
            while(myRes.next() && telRes.next() && enfRes.next() && medRes.next() && aleRes.next()){
                size++;
            }
            if (notfound){
                try{
                    String exp_id = "E"+idPaciente;

                    String ale = "update expediente_alergia set nombre = ? , descripcion = ? "
                            + "where id_expediente = ?";
                    PreparedStatement aleStmt = database.updateData(ale);
                    aleStmt.setString(1, alergias.getText());
                    aleStmt.setString(2, "descripcion");
                    aleStmt.setString(3, exp_id);
                    aleStmt.executeUpdate();

                    String med = "update expediente_medicamentoPrescrito set nombre = ?, descripcion = ? "
                            + "where id_expediente = ?";
                    PreparedStatement medStmt = database.updateData(med);
                    medStmt.setString(1, medicamentos.getText());
                    medStmt.setString(2, "descripcion");
                    medStmt.setString(3, exp_id);
                    medStmt.executeUpdate();

                    String enfermedad = "update expediente_enfermedad set nombre = ?, descripcion = ? "
                            + "where id_expediente = ?";
                    PreparedStatement enfStmt = database.updateData(enfermedad);
                    enfStmt.setString(1, enfermedades.getText());
                    enfStmt.setString(2, "descripcion");
                    enfStmt.setString(3, exp_id);
                    enfStmt.executeUpdate();

                    cancelButton.setVisible(false);
                    saveButton.setVisible(false);
                    alergias.setEditable(false);
                    medicamentos.setEditable(false);
                    antecedentes.setEditable(false);
                    enfermedades.setEditable(false);
                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    motor.showExpedienteUser(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    public void editButton(ActionEvent event) {
        cancelButton.setVisible(true);
        saveButton.setVisible(true);
        alergias.setEditable(true);
        medicamentos.setEditable(true);
        enfermedades.setEditable(true);
    }


    public void registerCita(ActionEvent event) {
        String text = "¿Desea registrar una cita para este paciente?";
        if (motor.confirmAction(text, "")){
            motor.setSelectedPacient(true, idPaciente, fullname);
            motor.showRegisterCita(event);
            motor.setGoBack(true);
        }
    }
}
