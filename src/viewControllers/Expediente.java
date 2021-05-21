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
    private Group alertGroup, requiredGroup;
    @FXML
    private TextField searchInput;
    private String idPaciente = "empty";
    private List<Consulta> itemList = new ArrayList<>();
    private List<Consulta> items = null;
    private List<Consulta> searchList = new ArrayList<>();
    private Listener listener;
    private String name;

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
            cons = database.connectSQL("consulta");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(myRes.next() && ale.next() && med.next() && enf.next()){
            if (motor.getSelectedItem().equals(String.valueOf(myRes.getString("id")))){
                String fullname = myRes.getString("nombre")+" "+myRes.getString("nomPaterno")+" "+myRes.getString("nomMaterno");
                idPaciente = myRes.getString("id");

                id.setText(myRes.getString("id"));
                nombre.setText(fullname);
                alergias.setText(ale.getString("nombre"));
                medicamentos.setText(med.getString("nombre"));
                enfermedades.setText(enf.getString("nombre"));
                //antecedentes.setText(cons.getString("observaciones"));
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

                setChosenItem(id);
                String text = "Esta seguro que desea eliminar a: "+findItem(id);
                if (motor.confirmAction(text)){
                    System.out.println(id + " Eliminado");
                    deleteItem(id, event);
                }
            }
            @Override
            public void showListener(String id, ActionEvent event){
                setChosenItem(id);
                motor.showConsulta(event);
            }

            @Override
            public void selectListener(String id, ActionEvent event) {

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
                    String id = myRes.getString("numCita");
                    String fecha = myRes.getString("fecha");

                    Consulta newItem = defineItem(id, fecha);
                    itemList.add(newItem);
                }
        }
        return itemList;
    }

    public Consulta defineItem(String id, String fecha) {
        Consulta item = new Consulta();
        item.setNumCita(id);
        item.setFecha(fecha);
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
            if (id.equals(myRes.getString("numCita"))){
                try{
                    String sql = "delete from cita where numCita= ? ";
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
            if (id.equals(myRes.getString("numCita"))){
                name = myRes.getString("numCita");
                break;
            }
        }
        return name;
    }

    public void nuevaConsulta(ActionEvent event) {
        motor.showConsultas(event);
    }

    public void cancelChanges(ActionEvent event) {
        motor.showExpedienteUser(event);
    }

    public void saveChanges(ActionEvent event) throws SQLException {
        cancelButton.setVisible(false);
        saveButton.setVisible(false);
        alergias.setEditable(false);
        medicamentos.setEditable(false);
        antecedentes.setEditable(false);
        enfermedades.setEditable(false);

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
        antecedentes.setEditable(true);
    }


}
