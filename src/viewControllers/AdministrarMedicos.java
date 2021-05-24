package viewControllers;

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

public class AdministrarMedicos implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private VBox medicosLayout;
    @FXML
    private TextField searchMedicoInput;
    private List<Medico> medList = new ArrayList<>();
    private List<Medico> medicos = null;
    private List<Medico> searchList = new ArrayList<>();
    private Listener listener;
    private String name;

    public AdministrarMedicos(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            medicos();
            listener = new Listener(){
                @Override
                public void editListener(String id, ActionEvent event){
                    setChosenMedico(id);
                    motor.showEditMedico(event);
                }
                @Override
                public void deleteListener(String id, ActionEvent event) throws SQLException{

                    setChosenMedico(id);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("¿Está seguro que desea eliminar a: "+findMedico(id)+"?");

                    if (alert.showAndWait().get() == ButtonType.OK){
                        System.out.println("medico eliminado");
                        deleteMedico(id, event);
                    }

                }

                @Override
                public void showListener(String id, ActionEvent event) {

                }

                @Override
                public void selectListener(String id, boolean isSelected, ActionEvent event) {

                }
            };
            loadMedicos(medList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setChosenMedico(String id){
        System.out.println("selected: "+ id);
        motor.setSelectedItem(id);
    }
    private List<Medico> medicos() throws SQLException {

        ResultSet myRes = null, telRes = null;
        try {
            telRes = database.connectSQL("medico_telefono");
            myRes = database.connectSQL("medico");

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*assert telRes != null;
        assert myRes != null;*/
        while (myRes.next() && telRes.next()) {

            String cedula = myRes.getString("cedula_profesional");
            String nombre = myRes.getString("nombre");
            String telefono = telRes.getString("numTelefono");
            String usuario = myRes.getString("usuario");
            String nomPaterno = myRes.getString("nomPaterno");
            String nomMaterno = myRes.getString("nomMaterno");
            String calle = myRes.getString("calle");
            String num_int = myRes.getString("num_int");
            String num_ext = myRes.getString("num_ext");
            String colonia = myRes.getString("colonia");
            String codigoPostal = myRes.getString("codigoPostal");
            String ciudad = myRes.getString("ciudad");
            String fecha_registro = myRes.getString("fecha_registro");
            int isAdmin = myRes.getInt("isAdmin");

            Medico newMed = defineMedico(cedula, nombre, nomPaterno, nomMaterno, calle, num_int, num_ext, colonia,
                    codigoPostal, ciudad, fecha_registro, telefono, usuario, isAdmin);
            medList.add(newMed);
        }
        return medList;
    }

    public Medico defineMedico(String cedula, String nombre, String nomPaterno, String nomMaterno,
                               String calle, String num_int, String num_ext, String colonia, String codigoPostal,
                               String ciudad, String fecha_registro, String telefono, String usuario, int isAdmin) {
        Medico medico = new Medico();
        medico.setCedula(cedula);
        medico.setNombre(nombre);
        medico.setTelefono(telefono);
        medico.setUsuario(usuario);
        medico.setNomPaterno(nomPaterno);
        medico.setNomMaterno(nomMaterno);
        medico.setCalle(calle);
        medico.setNum_int(num_int);
        medico.setNum_ext(num_ext);
        medico.setColonia(colonia);
        medico.setCodigoPostal(codigoPostal);
        medico.setCiudad(ciudad);
        medico.setFecha_registro(fecha_registro);
        medico.setIsAdmin(isAdmin);
        return medico;
    }

    public void loadMedicos(List array) throws SQLException {

        medicos = new ArrayList<>(array);
        for (int i = 0; i < medicos.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Medico_item.fxml"));

            try {
                HBox hbox = fxmlLoader.load();
                MedicoItem mi = fxmlLoader.getController();
                mi.setData(medicos.get(i),listener);
                medicosLayout.getChildren().add(hbox);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerMedico(ActionEvent event) {
        motor.showRegisterMedico(event);
    }

    public void backHome(ActionEvent event) {
        motor.showClient(event);
    }

    public void searchMedico(ActionEvent event) throws SQLException {
        String searchInput = searchMedicoInput.getText();
        if (searchInput.equals("")) {

        } else {
            boolean found = false;
            searchList.clear();
            medicosLayout.getChildren().clear();
            for (int i = 0; i < medList.size(); i++) {
                if (searchInput.contains(medList.get(i).getCedula()) || searchInput.contains(medList.get(i).getNombre()) || searchInput.contains(medList.get(i).getNomPaterno()) || searchInput.contains(medList.get(i).getNomMaterno()) || searchInput.contains(medList.get(i).getTelefono()) || searchInput.contains(medList.get(i).getUsuario())) {
                    found = true;
                    Medico foundMed = medList.get(i);
                    searchList.add(foundMed);
                }
            }
            loadMedicos(searchList);
            if (!found){
                System.out.println("no matches found");
            }
        }
    }

    public void deleteSearch(MouseEvent mouseEvent) throws SQLException {
        searchMedicoInput.clear();
        medicosLayout.getChildren().clear();
        loadMedicos(medList);
    }

    public void deleteMedico(String id, ActionEvent event) throws SQLException {
        ResultSet myRes = null, telRes = null;
        try {
            telRes = database.connectSQL("medico_telefono");
            myRes = database.connectSQL("medico");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (myRes.next() && telRes.next()) {
            if (id.equals(myRes.getString("cedula_profesional"))){
                try{
                    String sql = "delete from medico where cedula_profesional= ? ";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, id);
                    stmt.executeUpdate();

                    motor.showAdministrarMedicos(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    public String findMedico(String id) throws SQLException {
        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("medico");
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(myRes.next()){
            if (id.equals(myRes.getString("cedula_profesional"))){
                name = myRes.getString("nombre");
                break;
            }
        }
        return name;
    }
}
