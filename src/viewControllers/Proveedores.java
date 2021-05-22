package viewControllers;

import Model.Medicamento;
import Model.Medico;
import Model.Proveedor;
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

public class Proveedores implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private VBox itemsLayout;
    @FXML
    private TextField searchInput;
    private List<Proveedor> itemList = new ArrayList<>();
    private List<Proveedor> items = null;
    private List<Proveedor> searchList = new ArrayList<>();
    private Listener listener;
    private String name;

    public Proveedores(){

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
                    motor.showEditProveedor(event);
                }
                @Override
                public void deleteListener(String id, ActionEvent event) throws SQLException{

                    setChosenItem(id);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("¿Está seguro que desea eliminar a: "+findItem(id)+"?");

                    if (alert.showAndWait().get() == ButtonType.OK){
                        System.out.println("proveedor eliminado");
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
    private List<Proveedor> items() throws SQLException {

        ResultSet myRes = null, telRes = null;
        try {
            myRes = database.connectSQL("proveedor");

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*assert telRes != null;
        assert myRes != null;*/
        while (myRes.next()) {

            String id = myRes.getString("id");
            String nombre = myRes.getString("nombre");
            String calle = myRes.getString("calle");
            String numExt = myRes.getString("num_ext");
            String numInt = myRes.getString("num_int");
            String colonia = myRes.getString("colonia");
            String cp = myRes.getString("codigopostal");
            String ciudad = myRes.getString("ciudad");
            String telefono = myRes.getString("telefono");

            Proveedor newMed = defineItem(id, nombre, calle, numExt, numInt, colonia, cp, ciudad, telefono);
            itemList.add(newMed);
        }
        return itemList;
    }

    public Proveedor defineItem(String id, String nombre, String calle, String numExt, String numInt, String colonia, String cp, String ciudad, String telefono) {
        Proveedor proveedor = new Proveedor();
        proveedor.setid(id);
        proveedor.setNombre(nombre);
        proveedor.setCalle(calle);
        proveedor.setNum_ext(numExt);
        proveedor.setNum_int(numInt);
        proveedor.setColonia(colonia);
        proveedor.setCodigoPostal(cp);
        proveedor.setCiudad(ciudad);
        proveedor.setTelefono(telefono);
        return proveedor;
    }

    public void loadItems(List array) throws SQLException {

        items = new ArrayList<>(array);
        for (int i = 0; i < items.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ProveedoresItem.fxml"));

            try {
                HBox hbox = fxmlLoader.load();
                ProveedoresItem mi = fxmlLoader.getController();
                mi.setData(items.get(i),listener);
                itemsLayout.getChildren().add(hbox);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerItem(ActionEvent event) {
        motor.showRegisterProveedor(event);
    }

    public void backHome(ActionEvent event) {
        motor.showInventory(event);
    }

    public void searchItem(ActionEvent event) throws SQLException {
        String search = searchInput.getText();
        if (searchInput.equals("")) {

        } else {
            boolean found = false;
            searchList.clear();
            itemsLayout.getChildren().clear();
            for (int i = 0; i < itemList.size(); i++) {
                if (search.contains(itemList.get(i).getid()) || search.contains(itemList.get(i).getNombre())) {
                    found = true;
                    Proveedor foundItem = itemList.get(i);
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
            myRes = database.connectSQL("proveedor");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (myRes.next()) {
            if (id.equals(myRes.getString("id"))){
                try{
                    String sql = "delete from proveedor where id= ? ";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, id);
                    stmt.executeUpdate();

                    motor.showProveedores(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    public String findItem(String id) throws SQLException {
        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("proveedor");
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
        motor.showProveedores(event);
    }
}
