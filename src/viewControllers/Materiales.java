package viewControllers;

import Model.Material;
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

public class Materiales implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private VBox itemsLayout;
    @FXML
    private TextField searchInput;
    private List<Material> itemList = new ArrayList<>();
    private List<Material> items = null;
    private List<Material> searchList = new ArrayList<>();
    private Listener listener;
    private String name;

    public Materiales(){

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
                    motor.showEditMateriales(event);
                }
                @Override
                public void deleteListener(String id, ActionEvent event) throws SQLException{

                    setChosenItem(id);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("¿Está seguro que desea eliminar a: "+findItem(id)+"?");

                    if (alert.showAndWait().get() == ButtonType.OK){
                        System.out.println("material eliminado");
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
    private List<Material> items() throws SQLException {

        ResultSet myRes = null, telRes = null;
        try {
            myRes = database.connectSQL("materiales_consulta");

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*assert telRes != null;
        assert myRes != null;*/
        while (myRes.next()) {

            int numInventario = myRes.getInt("numInventario");
            System.out.println("numInventario: "+numInventario);
            String nombre = myRes.getString("nombre");
            String cantidadinventario = myRes.getString("cantidadinventario");

            Material newMed = defineItem(numInventario, nombre, cantidadinventario);
            itemList.add(newMed);
        }
        return itemList;
    }

    public Material defineItem(int numInventario, String nombre, String cantidadinventario) {
        Material material = new Material();
        material.setNumInventario(numInventario);
        material.setNombre(nombre);
        material.setCantidadInventario(cantidadinventario);
        return material;
    }

    public void loadItems(List array) throws SQLException {

        items = new ArrayList<>(array);
        for (int i = 0; i < items.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("MaterialesItem.fxml"));

            try {
                HBox hbox = fxmlLoader.load();
                MaterialesItem mi = fxmlLoader.getController();
                mi.setData(items.get(i),listener);
                itemsLayout.getChildren().add(hbox);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerItem(ActionEvent event) {
        motor.showRegisterMateriales(event);
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
                if (search.contains(String.valueOf(itemList.get(i).getNumInventario())) || search.contains(itemList.get(i).getNombre()) || search.contains(itemList.get(i).getCantidadInventario())) {
                    found = true;
                    Material foundItem = itemList.get(i);
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
            myRes = database.connectSQL("materiales_consulta");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (myRes.next()) {
            if (id.equals(myRes.getString("numinventario"))){
                try{
                    String sql = "delete from materiales_consulta where numinventario= ? ";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, id);
                    stmt.executeUpdate();

                    motor.showMateriales(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    public String findItem(String id) throws SQLException {
        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("materiales_consulta");
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(myRes.next()){
            if (id.equals(myRes.getString("numinventario"))){
                name = myRes.getString("nombre");
                break;
            }
        }
        return name;
    }

    public void displayProveedores(ActionEvent event) {

    }
}
