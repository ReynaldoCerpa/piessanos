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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Consultas implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    @FXML
    private Label montoLabel, descuentoLabel, subtotalLabel, id, nombre, alert, alert1, alert2, alert3, alertText;
    @FXML
    private VBox tratamientosLayout, medicamentosLayout;
    @FXML
    private Group alertGroup;
    @FXML
    private TextField temperatura;
    @FXML
    private TextArea indicaciones, observaciones, antecedentes;

    private List<TratamientoList> tratamientosList = new ArrayList<>();
    private List<TratamientoList> tratamientos = null;
    private List<MedicamentoList> medicamentosList = new ArrayList<>();
    private List<MedicamentoList> medicamentos = null;
    private List<String> promoItemsList = new ArrayList<>();
    private List<String> usedMeds = new ArrayList<>();
    private Listener listener;
    private String idPaciente, idMedico, numCita;
    private double montoTotal, descuentoTotal, subtotal;
    private boolean isTRT = true;

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
                break;
            }
        }
        Tratamientoitems();
        Medicamentoitems();
        loadItems(tratamientosList, medicamentosList);
        if (motor.getPromoItems() != null){
            promoItemsList = motor.getPromoItems();
        }
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
            public void selectListener(String id, boolean isSelected, ActionEvent event) throws SQLException {


                if ((isSelected && id.contains("MDT"))) {
                    System.out.println("used "+id);
                    usedMeds.add(id);
                } else {
                    System.out.println("removed from usedList "+id);
                    usedMeds.remove(id);
                }
                if (!id.contains("MDT") && tieneDescuento(id)){
                    if (isSelected) {
                        descuentoTotal = descuentoTotal + findDescuento(id);
                    } else {
                        descuentoTotal = descuentoTotal - findDescuento(id);
                    }
                }
                montoTotal = (isSelected) ? montoTotal + findPrecio(id) : montoTotal - findPrecio(id);
                subtotal = (isSelected && !id.contains("MDT")) ? montoTotal - descuentoTotal : montoTotal;
                descuentoLabel.setText(String.valueOf(descuentoTotal));
                montoLabel.setText(String.valueOf(montoTotal));
                subtotalLabel.setText(String.valueOf(subtotal));
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
            if (myRes.getInt("cantidadinventario") > 0) {
                String id = myRes.getString("codigo");
                String nombre = myRes.getString("nombre");
                String precio = myRes.getString("precio");

                MedicamentoList newItem = defineMedicamento(id, nombre, precio);
                medicamentosList.add(newItem);
            }
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

    public double findDescuento(String id) throws SQLException {
        for (int i=0; i<promoItemsList.size(); i++) {
            if (id.equals(promoItemsList.get(i))) {
                String promoitem = "select * from promocion where clave_tratamiento = ?";
                String tratamiento = "select * from tratamiento where clave = ?";
                PreparedStatement itemStmt = database.updateData(promoitem);
                PreparedStatement trtStmt = database.updateData(tratamiento);
                itemStmt.setString(1, promoItemsList.get(i));
                trtStmt.setString(1, promoItemsList.get(i));
                System.out.println(promoItemsList.get(i));
                ResultSet getItems = itemStmt.executeQuery();
                ResultSet getTRT = trtStmt.executeQuery();
                while (getItems.next() && getTRT.next()) {
                    String desc = getItems.getString("porcentaje_descuento");
                    double porcentaje = Double.parseDouble(desc.substring(0, desc.length() - 1));
                    porcentaje = porcentaje / 100;
                    double precio = getTRT.getFloat("precio");
                    descuentoTotal = precio - (precio * porcentaje);
                }
                break;
            }
        }
        return descuentoTotal;
    }

    public boolean tieneDescuento(String id) throws SQLException {
        boolean hasDiscount = false;
        String promoitem = "select clave_tratamiento from promocion where clave_tratamiento = ? order by clave_tratamiento limit 1";
        PreparedStatement itemStmt = database.updateData(promoitem);
        itemStmt.setString(1, id);
        ResultSet getItems = itemStmt.executeQuery();
        while (getItems.next()) {
            hasDiscount = true;
        }
        return hasDiscount;
    }

    private void setChosenItem(String id){
        System.out.println("selected: "+ id);
        motor.setSelectedItem(id);
    }

    public void guardarConsulta(ActionEvent event) throws SQLException {

        String text = "¿Está seguro que desea guardar el registro?";
        String content = "Ya no podrá ser editado más adelante";
        if (motor.confirmAction(text, content)) {
            alerts(true);
            alertGroup.setVisible(true);
            if (temperatura.getText().isEmpty() || indicaciones.getText().isEmpty() || observaciones.getText().isEmpty()) {
                alert.setVisible(true);
                alertText.setText(alertText.getText() + "Rellene todos los campos obligatorios\n");
                alertGroup.setVisible(true);
                System.out.println("Rellene todos los campos obligatorios");
            } else {
                ResultSet myRes = null;
                try {
                    myRes = database.connectSQL("consulta");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                boolean notfound = true;
                boolean out = false;

                int size = 1;
                while (myRes.next()) {
                    size++;

                    if (!StringUtils.isStrictlyNumeric(temperatura.getText()) && !out) {
                        notfound = false;
                        out = true;
                        alertText.setText(alertText.getText() + "Solo valores numéricos para campo temperatura\n");
                        alertGroup.setVisible(true);
                        System.out.println("invalid data input temperatura");
                    }
                }
                if (notfound) {
                    try {

                        String idExpediente = "";
                        String idCita = "";
                        String idPaciente = motor.getSelectedItem();

                        String expquery = "select id from expediente where id_paciente = ?";
                        String citaquery = "select idCita from cita where id_paciente = ? order by idCita desc limit 1";
                        PreparedStatement exp = database.updateData(expquery);
                        PreparedStatement cita = database.updateData(citaquery);
                        exp.setString(1, idPaciente);
                        cita.setString(1, idPaciente);
                        ResultSet idexp = exp.executeQuery();
                        ResultSet numcita = cita.executeQuery();
                        while (idexp.next() && numcita.next()) {
                            idExpediente = idexp.getString("id");
                            idCita = numcita.getString("idCita");
                        }

                        System.out.println(idPaciente);
                        System.out.println(idExpediente);
                        System.out.println(idCita);

                        String idcons = motor.generateID("CST-", size);
                        String sql = "insert into consulta " + "(idConsulta, indicaciones, observaciones," +
                                "temperatura_covid, costo_total, id_expediente, id_paciente, idCita)"
                                + " values (?,?,?,?,?," +
                                "(select id from expediente where id=?)," +
                                "(select id from paciente where id=?)," +
                                "(select idCita from cita where idCita=?))";
                        PreparedStatement stmt = database.updateData(sql);
                        stmt.setString(1, idcons);
                        stmt.setString(2, indicaciones.getText());
                        stmt.setString(3, observaciones.getText());
                        stmt.setDouble(4, Double.parseDouble(temperatura.getText()));
                        stmt.setFloat(5, (float) subtotal);
                        stmt.setString(6, idExpediente);
                        stmt.setString(7, idPaciente);
                        stmt.setString(8, idCita);
                        stmt.executeUpdate();

                        String atendido = "update cita set atendido='si'"
                                + " where idCita = ?";
                        PreparedStatement atendstmt = database.updateData(atendido);
                        atendstmt.setString(1, idCita);
                        atendstmt.executeUpdate();


                        int cantidadInventario = 0;


                        String sub = "update medicamento set cantidadinventario = cantidadinventario - 1"
                                + " where codigo = ?";
                        PreparedStatement subdstmt = database.updateData(sub);
                        for (int i = 0; i < usedMeds.size(); i++) {
                            String checkQty = "select cantidadinventario from medicamento where codigo = ?";
                            PreparedStatement qty = database.updateData(checkQty);
                            qty.setString(1, usedMeds.get(i));
                            ResultSet qtyRes = qty.executeQuery();
                            while (qtyRes.next()) {
                                cantidadInventario = qtyRes.getInt("cantidadInventario");
                            }
                            if (cantidadInventario > 0) {
                                subdstmt.setString(1, usedMeds.get(i));
                                subdstmt.executeUpdate();
                            }
                        }

                        String medico = "insert into medico_consulta (fecha_edicion, idConsulta, cedula_profesional)"
                                + " values (?,(select idconsulta from consulta where idCita=?)," +
                                "(select cedula_profesional from medico where cedula_profesional=?))";
                        PreparedStatement Medicostmt = database.updateData(medico);
                        Medicostmt.setDate(1, Date.valueOf(motor.formatCurrDate()));
                        Medicostmt.setString(2, String.valueOf(idCita));
                        Medicostmt.setString(3, motor.getCedula());
                        Medicostmt.executeUpdate();

                        alerts(false);
                        alertGroup.setVisible(false);

                        motor.showExpedienteUser(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
