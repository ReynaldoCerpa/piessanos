package viewControllers;

import Model.Consulta;
import Model.MedicamentoList;
import Model.TratamientoList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Consultas implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    @FXML
    private VBox tratamientosLayout, medicamentosLayout;

    private List<TratamientoList> tratamientosList = new ArrayList<>();
    private List<TratamientoList> tratamientos = null;
    private List<MedicamentoList> medicamentosList = new ArrayList<>();
    private List<MedicamentoList> medicamentos = null;
    private Listener listener;
    private String name;

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;
        Tratamientoitems();
        Medicamentoitems();
        loadItems(tratamientosList, medicamentosList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listener = new Listener(){
            @Override
            public void editListener(String id, ActionEvent event){
            }
            @Override
            public void deleteListener(String id, ActionEvent event) throws SQLException{
            }
            @Override
            public void showListener(String id, ActionEvent event){
            }

            @Override
            public void selectListener(String id, ActionEvent event) {

            }
        };
    }

    public void loadItems(List tratamiento, List medicamento) throws SQLException {

        medicamentos = new ArrayList<>(medicamento);
        for (int i = 0; i < medicamentos.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("MedicamentosListItem.fxml"));

            try {
                HBox hbox = fxmlLoader.load();
                MedicamentosListItem mi = fxmlLoader.getController();
                mi.setData(medicamentos.get(i),listener);
                medicamentosLayout.getChildren().add(hbox);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tratamientos = new ArrayList<>(tratamiento);
        for (int i = 0; i < tratamientos.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("TratamientosListItem.fxml"));

            try {
                HBox hbox = fxmlLoader.load();
                TratamientosListItem mi = fxmlLoader.getController();
                mi.setData(tratamientos.get(i),listener);
                tratamientosLayout.getChildren().add(hbox);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<TratamientoList> Tratamientoitems() throws SQLException {

        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("tratamiento");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (myRes.next()) {
            String id = myRes.getString("clave");
            String nombre = myRes.getString("nombre");
            String precio = myRes.getString("precio");

            TratamientoList newItem = defineTratamiento(id, nombre, precio);
            tratamientosList.add(newItem);
        }
        return tratamientosList;
    }

    public TratamientoList defineTratamiento(String id, String nombre, String precio) {
        TratamientoList item = new TratamientoList();
        item.setId(id);
        item.setNombre(nombre);
        item.setPrecio(precio);
        return item;
    }

    private List<MedicamentoList> Medicamentoitems() throws SQLException {

        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("medicamento");


        } catch (Exception e) {
            e.printStackTrace();
        }

        while (myRes.next()) {
            String id = myRes.getString("codigo");
            String nombre = myRes.getString("nombre");
            String precio = myRes.getString("precio");

            MedicamentoList newItem = defineMedicamento(id, nombre, precio);
            medicamentosList.add(newItem);
        }
        return medicamentosList;
    }

    public MedicamentoList defineMedicamento(String id, String nombre, String precio) {
        MedicamentoList item = new MedicamentoList();
        item.setId(id);
        item.setNombre(nombre);
        item.setPrecio(precio);
        return item;
    }

    private void setChosenItem(String id){
        System.out.println("selected: "+ id);
        motor.setSelectedItem(id);
    }

    public void guardarConsulta(ActionEvent event) {
    }

    public void cancelConsulta(ActionEvent event) {

        String text = "¿Esta seguro que desea cancelar la consulta?";
        if (motor.confirmAction(text)){
            motor.showExpedienteUser(event);
        }
    }
}
