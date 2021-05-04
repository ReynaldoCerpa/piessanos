package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Motor {
    private User user = new User();
    private Item item = new Item();
    public Motor(){

    }
    public void showEditProveedor(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditProveedor.fxml"));
            Parent root = loader.load();
            EditProveedor controller = loader.<EditProveedor>getController();
            controller.receiveMotorInstance(this);
            Scene EditProveedorScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(EditProveedorScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showEditMedicamento(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditMedicamento.fxml"));
            Parent root = loader.load();
            EditMedicamento controller = loader.<EditMedicamento>getController();
            controller.receiveMotorInstance(this);
            Scene EditMedicamentoScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(EditMedicamentoScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showEditMedico(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditMedico.fxml"));
            Parent root = loader.load();
            EditMedico controller = loader.<EditMedico>getController();
            controller.receiveMotorInstance(this);
            Scene EditMedicoScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(EditMedicoScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showAdministrarMedicos(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdministrarMedicos.fxml"));
            Parent root = loader.load();
            AdministrarMedicos controller = loader.<AdministrarMedicos>getController();
            controller.receiveMotorInstance(this);
            Scene AdministrarMedicosScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(AdministrarMedicosScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showLogin(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Login controller = loader.<Login>getController();
            controller.receiveMotorInstance(this);
            Scene loginScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(loginScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showClient(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Client.fxml"));
            Parent root = loader.load();
            Client controller = loader.<Client>getController();
            controller.receiveMotorInstance(this);
            Scene clientScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(clientScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showPacients(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Pacients.fxml"));
            Parent root = loader.load();
            Pacients controller = loader.<Pacients>getController();
            controller.receiveMotorInstance(this);
            Scene pacientScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(pacientScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showRegisterPacient(ActionEvent event){

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterPacient.fxml"));
            Parent root = loader.load();
            RegisterPacient controller = loader.<RegisterPacient>getController();
            controller.receiveMotorInstance(this);
            Scene registerPacientScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(registerPacientScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void cancelRegisterPacient(ActionEvent event){
        showPacients(event);
    }
    public void showPromo(ActionEvent event){

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Promo.fxml"));
            Parent root = loader.load();
            Promo controller = loader.<Promo>getController();
            controller.receiveMotorInstance(this);
            Scene PromoScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(PromoScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showCreatePromo(ActionEvent event){

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreatePromo.fxml"));
            Parent root = loader.load();
            CreatePromo controller = loader.<CreatePromo>getController();
            controller.receiveMotorInstance(this);
            Scene CreatePromoScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(CreatePromoScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showInventory(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Inventory.fxml"));
            Parent root = loader.load();
            Inventory controller = loader.<Inventory>getController();
            controller.receiveMotorInstance(this);
            Scene InventoryScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(InventoryScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showMedicamentos(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Medicamentos.fxml"));
            Parent root = loader.load();
            Medicamentos controller = loader.<Medicamentos>getController();
            controller.receiveMotorInstance(this);
            Scene MedicamentosScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(MedicamentosScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showRegisterMedicamentos(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterMedicamentos.fxml"));
            Parent root = loader.load();
            RegisterMedicamentos controller = loader.<RegisterMedicamentos>getController();
            controller.receiveMotorInstance(this);
            Scene RegisterMedicamentosScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(RegisterMedicamentosScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showProveedores(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Proveedores.fxml"));
            Parent root = loader.load();
            Proveedores controller = loader.<Proveedores>getController();
            controller.receiveMotorInstance(this);
            Scene ProveedoresScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(ProveedoresScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showRegisterProveedor(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterProveedor.fxml"));
            Parent root = loader.load();
            RegisterProveedor controller = loader.<RegisterProveedor>getController();
            controller.receiveMotorInstance(this);
            Scene RegisterProveedorScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(RegisterProveedorScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showMateriales(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Materiales.fxml"));
            Parent root = loader.load();
            Materiales controller = loader.<Materiales>getController();
            controller.receiveMotorInstance(this);
            Scene MaterialesScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(MaterialesScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showRegisterMateriales(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterMateriales.fxml"));
            Parent root = loader.load();
            RegisterMateriales controller = loader.<RegisterMateriales>getController();
            controller.receiveMotorInstance(this);
            Scene RegisterMaterialesScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(RegisterMaterialesScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showProveedoresMateriales(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProveedoresMateriales.fxml"));
            Parent root = loader.load();
            ProveedoresMateriales controller = loader.<ProveedoresMateriales>getController();
            controller.receiveMotorInstance(this);
            Scene ProveedoresMaterialesScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(ProveedoresMaterialesScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showRegisterProveedorMateriales(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterProveedorMateriales.fxml"));
            Parent root = loader.load();
            RegisterProveedorMateriales controller = loader.<RegisterProveedorMateriales>getController();
            controller.receiveMotorInstance(this);
            Scene RegisterProveedorMaterialesScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(RegisterProveedorMaterialesScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showExpedienteUser(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExpedienteUser.fxml"));
            Parent root = loader.load();
            ExpedienteUser controller = loader.<ExpedienteUser>getController();
            controller.receiveMotorInstance(this);
            Scene ExpedienteUserScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(ExpedienteUserScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showNewConsulta(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newConsulta.fxml"));
            Parent root = loader.load();
            newConsulta controller = loader.<newConsulta>getController();
            controller.receiveMotorInstance(this);
            Scene newConsultaScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(newConsultaScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showCita(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Citas.fxml"));
            Parent root = loader.load();
            Citas controller = loader.<Citas>getController();
            controller.receiveMotorInstance(this);
            Scene CitasScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(CitasScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showNewCita(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewCita.fxml"));
            Parent root = loader.load();
            NewCita controller = loader.<NewCita>getController();
            controller.receiveMotorInstance(this);
            Scene NewCitaScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(NewCitaScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showRegisterMedico(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterMedico.fxml"));
            Parent root = loader.load();
            RegisterMedico controller = loader.<RegisterMedico>getController();
            controller.receiveMotorInstance(this);
            Scene RegisterMedicoScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(RegisterMedicoScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showTratamientos(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Tratamientos.fxml"));
            Parent root = loader.load();
            Tratamientos controller = loader.<Tratamientos>getController();
            controller.receiveMotorInstance(this);
            Scene TratamientosScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(TratamientosScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showTratamiento(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Tratamiento.fxml"));
            Parent root = loader.load();
            Tratamiento controller = loader.<Tratamiento>getController();
            controller.receiveMotorInstance(this);
            Scene TratamientoScene = new Scene(root);

            Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            currentStage.setScene(TratamientoScene);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /* User getters and setters*/
    public void setCedula(String cedula){
        user.setCedula(cedula);
    }
    public String getCedula(){
        return user.getCedula();
    }
    public void setNombre(String nombre){
        user.setNombre(nombre);
    }
    public String getNombre(){
        return user.getNombre();
    }
    public void setApellidop(String apellidop){
        user.setApellidop(apellidop);
    }
    public String getApellidop(){
        return user.getApellidop();
    }
    public void setApellidom(String apellidom){
        user.setApellidom(apellidom);
    }
    public String getApellidom(){
        return user.getApellidom();
    }
    public void setUsername(String username){
        user.setUsername(username);
    }
    public String getUsername(){
        return user.getUsername();
    }
    public void setAdmin(boolean isAdmin){
        user.setAdmin(isAdmin);
    }
    public boolean isAdmin(){
        return user.getisAdmin();
    }

    public void setSelectedItem(String id){
        item.setId(id);
    }
    public String getSelectedItem(){
        return item.getId();
    }



}
