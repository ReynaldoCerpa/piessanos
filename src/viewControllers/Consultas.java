package viewControllers;

import Model.Consulta;
import Model.MedicamentoList;
import Model.TratamientoList;
import com.mysql.cj.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Consultas implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    @FXML
    private Label montoLabel, id, nombre, alert, alert1, alert2, alert3, alertText;
    @FXML
    private VBox tratamientosLayout, medicamentosLayout;
    @FXML
    private Group alertGroup;
    @FXML
    private TextField temperatura;
    @FXML
    private TextArea indicaciones, observaciones;

    private List<TratamientoList> tratamientosList = new ArrayList<>();
    private List<TratamientoList> tratamientos = null;
    private List<MedicamentoList> medicamentosList = new ArrayList<>();
    private List<MedicamentoList> medicamentos = null;
    private Listener listener;
    private String idPaciente, idMedico, numCita;
    private double montoTotal;

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;
        ResultSet myRes = null, ale = null, med = null, enf = null, cons = null;
        try {
            myRes = database.connectSQL("paciente");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(myRes.next()){
            if (motor.getSelectedItem().equals(String.valueOf(myRes.getString("id")))){
                String fullname = myRes.getString("nombre")+" "+myRes.getString("nomPaterno")+" "+myRes.getString("nomMaterno");


                id.setText(myRes.getString("id"));
                nombre.setText(fullname);
                //antecedentes.setText(cons.getString("observaciones"));
                break;
            }
        }
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
            public void selectListener(String id, boolean isSelected, ActionEvent event) {

                montoTotal = (isSelected) ? montoTotal + findPrecio(id) : montoTotal - findPrecio(id);
                montoLabel.setText(String.valueOf(montoTotal));
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
        item.setPrecio("$"+precio);
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
        item.setPrecio("$"+precio);
        return item;
    }

    public double findPrecio(String id){
        double precio = 0;
        for (MedicamentoList medicamentoList : medicamentosList) {
            if (id.equals(medicamentoList.getId())) {
                String num = medicamentoList.getPrecio().substring(1);
                precio = Double.parseDouble(num);
            }
        }
        for (TratamientoList tratamientoList : tratamientosList) {
            if (id.equals(tratamientoList.getId())) {
                String num = tratamientoList.getPrecio().substring(1);
                precio = Double.parseDouble(num);
            }
        }
        return precio;
    }

    private void setChosenItem(String id){
        System.out.println("selected: "+ id);
        motor.setSelectedItem(id);
    }

    public void guardarConsulta(ActionEvent event) throws SQLException {

        alerts(true);
        alertGroup.setVisible(true);
        if (temperatura.getText().isEmpty() || indicaciones.getText().isEmpty() || observaciones.getText().isEmpty()){
            alert.setVisible(true);
        }else {
            ResultSet myRes = null;
            try{
                myRes = database.connectSQL("consulta");
            } catch (Exception e){
                e.printStackTrace();
            }

            boolean notfound = true;
            boolean out = false;

            int size = 1;
            while(myRes.next()){
                size++;

                if (!StringUtils.isStrictlyNumeric(temperatura.getText()) && !out){
                    notfound = false;
                    out = true;
                    alertText.setText(alertText.getText() + "Solo valores numéricos para campo temperatura\n");
                    alertGroup.setVisible(true);
                    System.out.println("invalid data input temperatura");
                }
            }
            if (notfound){
                try{

                    String idExpediente = "";
                    int numCita = 0;
                    String idPaciente = motor.getSelectedItem();

                    String expquery = "select id from expediente where id_paciente = ?";
                    String citaquery = "select numCita from cita where id_paciente = ?";
                    PreparedStatement exp = database.updateData(expquery);
                    PreparedStatement cita = database.updateData(citaquery);
                    exp.setString(1, idPaciente);
                    cita.setString(1, idPaciente);
                    ResultSet idexp = exp.executeQuery();
                    ResultSet numcita = cita.executeQuery();
                    while(idexp.next() && numcita.next()){
                        idExpediente = idexp.getString("id");
                        numCita = Integer.parseInt(numcita.getString("numcita"));
                    }

                    System.out.println(idPaciente);
                    System.out.println(idExpediente);
                    System.out.println(numCita);
                    /*String sql = "insert into consulta "+"(idConsulta, indicaciones, observaciones," +
                            "temperatura_covid, costo_total, id_expediente, id_paciente, numCita)"
                            +" values (?,?,?,?,?," +
                            "(select id from paciente where id=?)," +
                            "(select id from expediente where id=?)," +
                            "(select numcita from cita where numCita=?))";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, "CST"+size);
                    stmt.setString(2, indicaciones.getText());
                    stmt.setString(3, observaciones.getText());
                    stmt.setDouble(4, Double.parseDouble(temperatura.getText()));
                    stmt.setFloat(5, (float) montoTotal);
                    stmt.setString(6, idExpediente);
                    stmt.setString(7, idPaciente);
                    stmt.setInt(8, numCita);
                    stmt.executeUpdate();
                    alerts(false);
                    alertGroup.setVisible(false);*/

                    motor.showExpedienteUser(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelConsulta(ActionEvent event) {

        String text = "¿Esta seguro que desea cancelar la consulta?";
        if (motor.confirmAction(text, "")){
            motor.showExpedienteUser(event);
        }
    }

    public void alerts(boolean isOn){
        alert.setVisible(isOn);
        alert1.setVisible(isOn);
        alert2.setVisible(isOn);
        alert3.setVisible(isOn);
    }
}
