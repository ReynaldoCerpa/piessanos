package viewControllers;

import Model.Cita;
import Model.Medicamento;
import Model.Medico;
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

public class Citas implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private VBox itemsLayout;
    @FXML
    private TextField searchInput;
    private List<Cita> itemList = new ArrayList<>();
    private List<Cita> items = null;
    private List<Cita> searchList = new ArrayList<>();
    private Listener listener;
    private String name;

    public Citas(){

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
                    motor.showEditCita(event);
                }
                @Override
                public void deleteListener(String id, ActionEvent event) throws SQLException{

                    setChosenItem(id);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("¿Está seguro que desea eliminar a: "+findItem(id)+"?");

                    if (alert.showAndWait().get() == ButtonType.OK){
                        System.out.println("cita eliminado");
                        deleteItem(id, event);
                    }

                }

                @Override
                public void showListener(String id, ActionEvent event) {

                }

                @Override
                public void selectListener(String id,  boolean isSelected, ActionEvent event) {

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
    private List<Cita> items() throws SQLException {

        ResultSet myRes = null, telRes = null;
        try {
            myRes = database.connectSQL("cita");

        } catch (Exception e) {
            e.printStackTrace();
        }

        while (myRes.next()) {

            String idCita = myRes.getString("idCita");
            String hora = myRes.getString("hora");
            String fecha = myRes.getString("fecha");
            String domicilio = myRes.getString("domicilio");
            String paciente = myRes.getString("id_paciente");

            Cita newItem = defineItem(idCita, paciente, hora, fecha, domicilio);
            itemList.add(newItem);
        }
        return itemList;
    }

    public Cita defineItem(String idCita, String paciente, String hora, String fecha, String lugar) {
        Cita cita = new Cita();
        cita.setidCita(idCita);
        cita.setId_paciente(paciente);
        cita.setHora(hora);
        cita.setFecha(fecha);
        cita.setDomicilio(lugar);
        return cita;
    }

    public void loadItems(List array) throws SQLException {

        items = new ArrayList<>(array);
        for (int i = 0; i < items.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CitaItem.fxml"));

            try {
                HBox hbox = fxmlLoader.load();
                CitaItem mi = fxmlLoader.getController();
                mi.setData(items.get(i),listener);
                itemsLayout.getChildren().add(hbox);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerItem(ActionEvent event) {
        motor.showRegisterCita(event);
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
                if (search.contains(itemList.get(i).getId_paciente()) || search.contains(itemList.get(i).getFecha()) || search.contains(itemList.get(i).getHora())) {
                    found = true;
                    Cita foundItem = itemList.get(i);
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

                    String consQuery = "delete from consulta where idCita= ? ";
                    PreparedStatement conStmt = database.updateData(consQuery);
                    conStmt.setString(1, id);
                    conStmt.executeUpdate();

                    motor.showCita(event);
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

    public void displayProveedores(ActionEvent event) {

    }
}
