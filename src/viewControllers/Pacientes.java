package viewControllers;

import Model.Medicamento;
import Model.Medico;
import Model.Paciente;
import Model.Tratamiento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
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

public class Pacientes implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private VBox itemsLayout;
    @FXML
    private TextField searchInput;
    private List<Paciente> itemList = new ArrayList<>();
    private List<Paciente> items = null;
    private List<Paciente> searchList = new ArrayList<>();
    private Listener listener;
    private String name;

    public Pacientes(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            items();
            listener = new Listener(){
                @Override
                public void editListener(String id, ActionEvent event){
                    setChosenItem(id);
                    motor.showEditPacientes(event);
                }
                @Override
                public void deleteListener(String id, ActionEvent event) throws SQLException{

                    setChosenItem(id);
                    String text = "Está seguro que desea eliminar a: "+findItem(id);
                    if (motor.confirmAction(text)){
                        deleteItem(id, event);
                    }
                }
                @Override
                public void showListener(String id, ActionEvent event){
                    setChosenItem(id);
                    motor.showExpedienteUser(event);
                }

                @Override
                public void selectListener(String id, ActionEvent event) {

                }
            };
            loadItems(itemList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setChosenItem(String id){
        System.out.println("selected: "+ id);
        motor.setSelectedItem(id);
    }
    private List<Paciente> items() throws SQLException {

        ResultSet myRes = null, telRes = null;
        try {
            myRes = database.connectSQL("paciente");
            telRes = database.connectSQL("paciente_telefono");

        } catch (Exception e) {
            e.printStackTrace();
        }

        while (myRes.next() && telRes.next()) {

            String id = myRes.getString("id");
            String nombre = myRes.getString("nombre");
            String nomPaterno = myRes.getString("nomPaterno");
            String nomMaterno = myRes.getString("nomMaterno");
            String telefono = telRes.getString("numTelefono");

            Paciente newItem = defineItem(id, nombre, nomPaterno, nomMaterno, telefono);
            itemList.add(newItem);
        }
        return itemList;
    }

    public Paciente defineItem(String id, String nombre, String nomPaterno, String nomMaterno, String telefono) {
        Paciente paciente = new Paciente();
        paciente.setId(id);
        paciente.setNombre(nombre);
        paciente.setNomPaterno(nomPaterno);
        paciente.setNomMaterno(nomMaterno);
        paciente.setTelefono(telefono);
        return paciente;
    }

    public void loadItems(List array) throws SQLException {

        items = new ArrayList<>(array);
        for (int i = 0; i < items.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("PacientesItem.fxml"));

            try {
                HBox hbox = fxmlLoader.load();
                PacientesItem mi = fxmlLoader.getController();
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
        motor.showClient(event);
    }

    public void searchItem(ActionEvent event) throws SQLException {
        String search = searchInput.getText();
        if (searchInput.equals("")) {

        } else {
            boolean found = false;
            searchList.clear();
            itemsLayout.getChildren().clear();
            for (int i = 0; i < itemList.size(); i++) {
                if (search.contains(itemList.get(i).getId()) || search.contains(itemList.get(i).getNombre()) || search.contains(itemList.get(i).getNomPaterno()) || search.contains(itemList.get(i).getNomMaterno())) {
                    found = true;
                    Paciente foundItem = itemList.get(i);
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
            myRes = database.connectSQL("paciente");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (myRes.next()) {
            if (id.equals(myRes.getString("id"))){
                try{
                    String sql = "delete from paciente where id= ? ";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, id);
                    stmt.executeUpdate();

                    String cita = "delete from cita where id_paciente= ? ";
                    PreparedStatement Citastmt = database.updateData(cita);
                    Citastmt.setString(1, id);
                    Citastmt.executeUpdate();

                    motor.showPacientes(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    public String findItem(String id) throws SQLException {
        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("paciente");
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(myRes.next()){
            if (id.equals(myRes.getString("id"))){
                name = myRes.getString("nombre");
                break;
            }
        }
        return name;
    }

    public void displayProveedores(ActionEvent event) {

    }
}
