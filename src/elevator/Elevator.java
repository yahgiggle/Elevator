
package elevator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.risingworld.api.Plugin;
import net.risingworld.api.Server;
import net.risingworld.api.Timer;
import net.risingworld.api.World;
import net.risingworld.api.assets.AssetBundle;
import net.risingworld.api.assets.PrefabAsset;
import net.risingworld.api.assets.SoundAsset;
import net.risingworld.api.database.Database;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerChangeBlockPositionEvent;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.events.player.PlayerGameObjectInteractionEvent;
import net.risingworld.api.events.player.PlayerKeyEvent;
import net.risingworld.api.events.player.PlayerMouseButtonEvent;
import net.risingworld.api.events.player.ui.PlayerUIElementClickEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.ui.UIElement;
import net.risingworld.api.ui.UILabel;
import net.risingworld.api.ui.style.TextAnchor;
import net.risingworld.api.utils.Key;
import net.risingworld.api.utils.Layer;
import net.risingworld.api.utils.MouseButton;
import net.risingworld.api.utils.RaycastResult;
import net.risingworld.api.utils.Vector3f;
import net.risingworld.api.worldelements.GameObject;
import net.risingworld.api.worldelements.Prefab;

/**
 *
 * @author Yahgiggle
 */
public class Elevator extends Plugin implements Listener {
    
  
    
    public Plugin plugin;
    public Timer timer;
    public Database database;
    public Database database0;
    public Database database1;
    public Database database2;
    public Database database3;
    public Database database4;
    public Database database5;
    public Database database6;
    public Database database7;
    public Database database8;
    public Database database9;
    
 
    
    public ArrayList<Prefab> elevators = new ArrayList<>();
    
    public ArrayList<Prefab> elevatorDoors0 = new ArrayList<>();
    public ArrayList<Prefab> elevatorDoors1 = new ArrayList<>();
    public ArrayList<Prefab> elevatorDoors2 = new ArrayList<>();
    public ArrayList<Prefab> elevatorDoors3 = new ArrayList<>();
    public ArrayList<Prefab> elevatorDoors4 = new ArrayList<>();
    public ArrayList<Prefab> elevatorDoors5 = new ArrayList<>();
    public ArrayList<Prefab> elevatorDoors6 = new ArrayList<>();
    public ArrayList<Prefab> elevatorDoors7 = new ArrayList<>();
    public ArrayList<Prefab> elevatorDoors8 = new ArrayList<>();
    public ArrayList<Prefab> elevatorDoors9 = new ArrayList<>();
    public ArrayList<Prefab> PreviewElevator = new ArrayList<>();
    
    
    
    
 @Override
    public void onEnable() {
        
        
        
        
        String WorldName = World.getName();
        System.out.println("-- PORTALS PLUGIN ENABLED --");
        database = getSQLiteConnection(getPath() + "/"+WorldName+"/database.db");
        database0 = getSQLiteConnection(getPath() + "/"+WorldName+"/database.db");
        database1 = getSQLiteConnection(getPath() + "/"+WorldName+"/database.db");
        database2 = getSQLiteConnection(getPath() + "/"+WorldName+"/database.db");
        database3 = getSQLiteConnection(getPath() + "/"+WorldName+"/database.db");
        database4 = getSQLiteConnection(getPath() + "/"+WorldName+"/database.db");
        database5 = getSQLiteConnection(getPath() + "/"+WorldName+"/database.db");
        database6 = getSQLiteConnection(getPath() + "/"+WorldName+"/database.db");
        database7 = getSQLiteConnection(getPath() + "/"+WorldName+"/database.db");
        database8 = getSQLiteConnection(getPath() + "/"+WorldName+"/database.db");
        database9 = getSQLiteConnection(getPath() + "/"+WorldName+"/database.db");
        
        
        
        database.execute("CREATE TABLE IF NOT EXISTS `Elevators` (`ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,`ElevatorBlockPosition` INTEGER,`ElevatorX` INTEGER, `ElevatorY` INTEGER, `ElevatorZ` INTEGER, `ElevatorFloorsAmount` INTEGER,`ElevatorFloorHeight` INTEGER ,`Blockpos` INTEGER,`Chunkpos` INTEGER,`ElevatorID` INTEGER, `ElevatorRotation` BIGINT);");   
         
        database.execute("CREATE TABLE IF NOT EXISTS `ElevatorOuterDoors` (`ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,`ElevatorBlockPosition` INTEGER,`ElevatorX` INTEGER, `ElevatorY` INTEGER, `ElevatorZ` INTEGER, `ElevatorFloorsAmount` INTEGER,`ElevatorFloorHeight` INTEGER ,`Blockpos` INTEGER,`ElevatorID` INTEGER,`ElevatorDoorsID` INTEGER, `ElevatorOuterDoorRotation` BIGINT);");   
        
        
       //  database.execute("ALTER TABLE `Elevators` ADD `ElevatorRotation` BIGINT");
        
       //  database.execute("ALTER TABLE `ElevatorOuterDoors` ADD `ElevatorOuterDoorRotation` BIGINT");
        
        
        
        
        
        
        
        
        
         //Load all elevators from db 
        try(ResultSet result = database.executeQuery("SELECT * FROM `Elevators` WHERE `ElevatorFloorsAmount` = '0' AND `ElevatorFloorHeight` = 'Elevator'")){
        while(result.next()) {
            
            String ElevatorBlockPosition = result.getString("ElevatorBlockPosition"); 
            float ElevatorX = result.getFloat("ElevatorX");
            float ElevatorY = result.getFloat("ElevatorY");
            float ElevatorZ = result.getFloat("ElevatorZ");
            float ElevatorRotation = result.getFloat("ElevatorRotation");
            AssetBundle bundle = AssetBundle.loadFromFile(getPath() + "/assets/elevator.bundle");
          
            
            PrefabAsset asset = PrefabAsset.loadFromAssetBundle(bundle, "Elevator.prefab");
            
            
            Prefab Elevator = new Prefab(asset);
            
            Elevator.setLayer(Layer.OBJECT);
            
            Elevator.setLocalPosition(ElevatorX, ElevatorY, ElevatorZ);
            Elevator.setLocalRotation(0,ElevatorRotation,0);
            
            
            //  Elevator.setLocalPosition(NEWPositionX,NEWPositionY,NEWPositionZ);
            elevators.add(Elevator);
            
            database1.executeUpdate("UPDATE Elevators SET `ElevatorID` = '" + Elevator.getID() + "' WHERE `ElevatorBlockPosition` = '" + ElevatorBlockPosition + "'"); 
            database2.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorID` = '" + Elevator.getID() + "' WHERE `ElevatorBlockPosition` = '" + ElevatorBlockPosition + "'"); 
        }    
    }
    catch(Exception e) {
        e.printStackTrace();
    }
        
        
        
    
        
     //Load all elevators from db 
        try(ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorHeight` = 'ElevatorDoor0'")){
        while(result.next()) {
            
             String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
            float ElevatorOuterDoorRotation = result.getFloat("ElevatorOuterDoorRotation");
            //Load the asset bundle from plugin folder first the elevator doors
            AssetBundle Doorbundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
            
            
            //Load the prefab asset from asset bundle the outer doors
            PrefabAsset ElevatorDoorsAsset = PrefabAsset.loadFromAssetBundle(Doorbundle, "ElevatorDoors.prefab");
            
            
            //Create the outer doors "Prefab" game object
            Prefab ElevatorDoors0 = new Prefab(ElevatorDoorsAsset);
            
            ElevatorDoors0.setLayer(Layer.OBJECT);
            
            ElevatorDoors0.setLocalPosition(GBlockposX, GBlockposY, GBlockposZ);
            
            ElevatorDoors0.setLocalRotation(0,ElevatorOuterDoorRotation,0);
            
     
           
            elevatorDoors0.add(ElevatorDoors0);
            
            database2.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorDoorsID` = '" + ElevatorDoors0.getID() + "' WHERE `ElevatorFloorHeight` = 'ElevatorDoor0' AND `Blockpos` = '"+BlockposGet+"'");
              
        }    
    }
    catch(Exception e) {
        e.printStackTrace();
    }    
        
        
       
        
        
        
      //Load all elevators from db 
        try(ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorHeight` = 'ElevatorDoor1'")){
        while(result.next()) {
            
            String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
            float ElevatorOuterDoorRotation = result.getFloat("ElevatorOuterDoorRotation");
            //Load the asset bundle from plugin folder first the elevator doors
            AssetBundle Doorbundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
            
            
            //Load the prefab asset from asset bundle the outer doors
            PrefabAsset ElevatorDoorsAsset = PrefabAsset.loadFromAssetBundle(Doorbundle, "ElevatorDoors.prefab");
            
            
            //Create the outer doors "Prefab" game object
            Prefab ElevatorDoors1 = new Prefab(ElevatorDoorsAsset);
            
            ElevatorDoors1.setLayer(Layer.OBJECT);
            
            ElevatorDoors1.setLocalPosition(GBlockposX, GBlockposY, GBlockposZ);
            ElevatorDoors1.setLocalRotation(0,ElevatorOuterDoorRotation,0);
     
           
            elevatorDoors1.add(ElevatorDoors1);
            
            database2.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorDoorsID` = '" + ElevatorDoors1.getID() + "' WHERE `ElevatorFloorHeight` = 'ElevatorDoor1' AND `Blockpos` = '"+BlockposGet+"'");
              
        }    
    }
    catch(Exception e) {
        e.printStackTrace();
    }   
        
        
        
    //Load all elevators from db 
        try(ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorHeight` = 'ElevatorDoor2'")){
        while(result.next()) {
            
             String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
            float ElevatorOuterDoorRotation = result.getFloat("ElevatorOuterDoorRotation");
            //Load the asset bundle from plugin folder first the elevator doors
            AssetBundle Doorbundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
            
            
            //Load the prefab asset from asset bundle the outer doors
            PrefabAsset ElevatorDoorsAsset = PrefabAsset.loadFromAssetBundle(Doorbundle, "ElevatorDoors.prefab");
            
            
            //Create the outer doors "Prefab" game object
            Prefab ElevatorDoors2 = new Prefab(ElevatorDoorsAsset);
            
            ElevatorDoors2.setLayer(Layer.OBJECT);
            
            ElevatorDoors2.setLocalPosition(GBlockposX, GBlockposY, GBlockposZ);
            ElevatorDoors2.setLocalRotation(0,ElevatorOuterDoorRotation,0);
     
           
            elevatorDoors2.add(ElevatorDoors2);
            
            database2.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorDoorsID` = '" + ElevatorDoors2.getID() + "' WHERE `ElevatorFloorHeight` = 'ElevatorDoor2' AND `Blockpos` = '"+BlockposGet+"'");
              
        }    
    }
    catch(Exception e) {
        e.printStackTrace();
    }    
        
        
       
        
        
        
      //Load all elevators from db 
        try(ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorHeight` = 'ElevatorDoor3'")){
        while(result.next()) {
            
            String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
            float ElevatorOuterDoorRotation = result.getFloat("ElevatorOuterDoorRotation");
            //Load the asset bundle from plugin folder first the elevator doors
            AssetBundle Doorbundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
            
            
            //Load the prefab asset from asset bundle the outer doors
            PrefabAsset ElevatorDoorsAsset = PrefabAsset.loadFromAssetBundle(Doorbundle, "ElevatorDoors.prefab");
            
            
            //Create the outer doors "Prefab" game object
            Prefab ElevatorDoors3 = new Prefab(ElevatorDoorsAsset);
            
            ElevatorDoors3.setLayer(Layer.OBJECT);
            
            ElevatorDoors3.setLocalPosition(GBlockposX, GBlockposY, GBlockposZ);
            ElevatorDoors3.setLocalRotation(0,ElevatorOuterDoorRotation,0);
     
           
            elevatorDoors3.add(ElevatorDoors3);
            
            database2.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorDoorsID` = '" + ElevatorDoors3.getID() + "' WHERE `ElevatorFloorHeight` = 'ElevatorDoor3' AND `Blockpos` = '"+BlockposGet+"'");
              
        }    
    }
    catch(Exception e) {
        e.printStackTrace();
    }      
        
        
        
        
    //Load all elevators from db 
        try(ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorHeight` = 'ElevatorDoor4'")){
        while(result.next()) {
            
            String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
            float ElevatorOuterDoorRotation = result.getFloat("ElevatorOuterDoorRotation");
            //Load the asset bundle from plugin folder first the elevator doors
            AssetBundle Doorbundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
            
            
            //Load the prefab asset from asset bundle the outer doors
            PrefabAsset ElevatorDoorsAsset = PrefabAsset.loadFromAssetBundle(Doorbundle, "ElevatorDoors.prefab");
            
            
            //Create the outer doors "Prefab" game object
            Prefab ElevatorDoors4 = new Prefab(ElevatorDoorsAsset);
            
            ElevatorDoors4.setLayer(Layer.OBJECT);
            
            ElevatorDoors4.setLocalPosition(GBlockposX, GBlockposY, GBlockposZ);
            ElevatorDoors4.setLocalRotation(0,ElevatorOuterDoorRotation,0);
     
           
            elevatorDoors4.add(ElevatorDoors4);
            
            database2.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorDoorsID` = '" + ElevatorDoors4.getID() + "' WHERE `ElevatorFloorHeight` = 'ElevatorDoor4' AND `Blockpos` = '"+BlockposGet+"'");
              
        }    
    }
    catch(Exception e) {
        e.printStackTrace();
    }    
        
        
       
        
        
        
      //Load all elevators from db 
        try(ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorHeight` = 'ElevatorDoor5'")){
        while(result.next()) {
            
            String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
            float ElevatorOuterDoorRotation = result.getFloat("ElevatorOuterDoorRotation");
            //Load the asset bundle from plugin folder first the elevator doors
            AssetBundle Doorbundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
            
            
            //Load the prefab asset from asset bundle the outer doors
            PrefabAsset ElevatorDoorsAsset = PrefabAsset.loadFromAssetBundle(Doorbundle, "ElevatorDoors.prefab");
            
            
            //Create the outer doors "Prefab" game object
            Prefab ElevatorDoors5 = new Prefab(ElevatorDoorsAsset);
            
            ElevatorDoors5.setLayer(Layer.OBJECT);
            
            ElevatorDoors5.setLocalPosition(GBlockposX, GBlockposY, GBlockposZ);
            ElevatorDoors5.setLocalRotation(0,ElevatorOuterDoorRotation,0);
     
           
            elevatorDoors5.add(ElevatorDoors5);
            
            database2.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorDoorsID` = '" + ElevatorDoors5.getID() + "' WHERE `ElevatorFloorHeight` = 'ElevatorDoor5' AND `Blockpos` = '"+BlockposGet+"'");
              
        }    
    }
    catch(Exception e) {
        e.printStackTrace();
    }  
        
        
        
        
     //Load all elevators from db 
        try(ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorHeight` = 'ElevatorDoor6'")){
        while(result.next()) {
            
            String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
            float ElevatorOuterDoorRotation = result.getFloat("ElevatorOuterDoorRotation");
            //Load the asset bundle from plugin folder first the elevator doors
            AssetBundle Doorbundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
            
            
            //Load the prefab asset from asset bundle the outer doors
            PrefabAsset ElevatorDoorsAsset = PrefabAsset.loadFromAssetBundle(Doorbundle, "ElevatorDoors.prefab");
            
            
            //Create the outer doors "Prefab" game object
            Prefab ElevatorDoors6 = new Prefab(ElevatorDoorsAsset);
            
            ElevatorDoors6.setLayer(Layer.OBJECT);
            
            ElevatorDoors6.setLocalPosition(GBlockposX, GBlockposY, GBlockposZ);
            ElevatorDoors6.setLocalRotation(0,ElevatorOuterDoorRotation,0);
     
           
            elevatorDoors6.add(ElevatorDoors6);
            
            database2.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorDoorsID` = '" + ElevatorDoors6.getID() + "' WHERE `ElevatorFloorHeight` = 'ElevatorDoor6' AND `Blockpos` = '"+BlockposGet+"'");
              
        }    
    }
    catch(Exception e) {
        e.printStackTrace();
    }    
        
        
       
        
        
        
      //Load all elevators from db 
        try(ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorHeight` = 'ElevatorDoor7'")){
        while(result.next()) {
            
            String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
            float ElevatorOuterDoorRotation = result.getFloat("ElevatorOuterDoorRotation");
            //Load the asset bundle from plugin folder first the elevator doors
            AssetBundle Doorbundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
            
            
            //Load the prefab asset from asset bundle the outer doors
            PrefabAsset ElevatorDoorsAsset = PrefabAsset.loadFromAssetBundle(Doorbundle, "ElevatorDoors.prefab");
            
            
            //Create the outer doors "Prefab" game object
            Prefab ElevatorDoors7 = new Prefab(ElevatorDoorsAsset);
            
            ElevatorDoors7.setLayer(Layer.OBJECT);
            
            ElevatorDoors7.setLocalPosition(GBlockposX, GBlockposY, GBlockposZ);
            ElevatorDoors7.setLocalRotation(0,ElevatorOuterDoorRotation,0);
     
           
            elevatorDoors7.add(ElevatorDoors7);
            
            database2.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorDoorsID` = '" + ElevatorDoors7.getID() + "' WHERE `ElevatorFloorHeight` = 'ElevatorDoor7' AND `Blockpos` = '"+BlockposGet+"'");
              
        }    
    }
    catch(Exception e) {
        e.printStackTrace();
    }     
        
        
        
        
    //Load all elevators from db 
        try(ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorHeight` = 'ElevatorDoor8'")){
        while(result.next()) {
            
            String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
            float ElevatorOuterDoorRotation = result.getFloat("ElevatorOuterDoorRotation");
            //Load the asset bundle from plugin folder first the elevator doors
            AssetBundle Doorbundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
            
            
            //Load the prefab asset from asset bundle the outer doors
            PrefabAsset ElevatorDoorsAsset = PrefabAsset.loadFromAssetBundle(Doorbundle, "ElevatorDoors.prefab");
            
            
            //Create the outer doors "Prefab" game object
            Prefab ElevatorDoors8 = new Prefab(ElevatorDoorsAsset);
            
            ElevatorDoors8.setLayer(Layer.OBJECT);
            
            ElevatorDoors8.setLocalPosition(GBlockposX, GBlockposY, GBlockposZ);
            ElevatorDoors8.setLocalRotation(0,ElevatorOuterDoorRotation,0);
     
           
            elevatorDoors8.add(ElevatorDoors8);
            
            database2.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorDoorsID` = '" + ElevatorDoors8.getID() + "' WHERE `ElevatorFloorHeight` = 'ElevatorDoor8' AND `Blockpos` = '"+BlockposGet+"'");
              
        }    
    }
    catch(Exception e) {
        e.printStackTrace();
    }    
        
        
       
        
        
        
      //Load all elevators from db 
        try(ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorHeight` = 'ElevatorDoor9'")){
        while(result.next()) {
            
            String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
            float ElevatorOuterDoorRotation = result.getFloat("ElevatorOuterDoorRotation");
            //Load the asset bundle from plugin folder first the elevator doors
            AssetBundle Doorbundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
            
            
            //Load the prefab asset from asset bundle the outer doors
            PrefabAsset ElevatorDoorsAsset = PrefabAsset.loadFromAssetBundle(Doorbundle, "ElevatorDoors.prefab");
            
            
            //Create the outer doors "Prefab" game object
            Prefab ElevatorDoors9 = new Prefab(ElevatorDoorsAsset);
            
            ElevatorDoors9.setLayer(Layer.OBJECT);
            
            ElevatorDoors9.setLocalPosition(GBlockposX, GBlockposY, GBlockposZ);
            ElevatorDoors9.setLocalRotation(0,ElevatorOuterDoorRotation,0);
     
           
            elevatorDoors9.add(ElevatorDoors9);
            
            database2.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorDoorsID` = '" + ElevatorDoors9.getID() + "' WHERE `ElevatorFloorHeight` = 'ElevatorDoor9' AND `Blockpos` = '"+BlockposGet+"'");
              
        }    
    }
    catch(Exception e) {
        e.printStackTrace();
    }  
    
        
        
        
        
        
        
      
        
        
        
    //Register event listener
    registerEventListener(this);
     
    }
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public void onDisable() {
        System.out.println("-- PLUGIN DISABLED --");
        if(database != null) database.close();
    }
    
    
       @EventMethod
  public void onPlayerConnect(PlayerConnectEvent event) throws SQLException  {
      Player player = event.getPlayer();
      
     //  player.setAttribute("ElevatorFloor", "Ground");
      player.registerKeys(Key.E, Key.F,Key.K, Key.LeftShift); 
      int ElevatorID = 0;
      player.setAttribute("ElevatorID", ElevatorID);
      player.setAttribute("CallElevator","NotCalled");
      player.setAttribute("DoorState","Closed");
      player.setAttribute("PlacingElevator", false);
      
      int Tick = 20;
      player.setAttribute("Tick", Tick);
      
       
      player.setListenForKeyInput(true);
      
      
      SoundAsset SoundLoop = SoundAsset.loadFromFile(getPath() + "/assets/Loop.mp3");
      player.setAttribute("SoundLoop",SoundLoop);
    
      SoundAsset Doorsoundfile = SoundAsset.loadFromFile(getPath() + "/assets/Doors.mp3");
      player.setAttribute("Doorsoundfile",Doorsoundfile);
      
      SoundAsset Bellsoundfile = SoundAsset.loadFromFile(getPath() + "/assets/Bell.wav");
      player.setAttribute("Bellsoundfile",Bellsoundfile);
      
      
      
      
       //Add all known elevators to the player
       for(Prefab Elevator : elevators) {
       player.addGameObject(Elevator);
       } 
       
       
       //Add all known elevators to the player
       for(Prefab ElevatorDoors0 : elevatorDoors0) {
       player.addGameObject(ElevatorDoors0);
       }
       
       
       
        //Add all known elevators to the player
       for(Prefab ElevatorDoors1 : elevatorDoors1) {
       
       player.addGameObject(ElevatorDoors1);
       
       }
       
       //Add all known elevators to the player
       for(Prefab ElevatorDoors2 : elevatorDoors2) {
        
       player.addGameObject(ElevatorDoors2);
       
       }
       
       
       //Add all known elevators to the player
       for(Prefab ElevatorDoors3 : elevatorDoors3) {
       
       player.addGameObject(ElevatorDoors3);
       
       }
       
       
       
        //Add all known elevators to the player
       for(Prefab ElevatorDoors4 : elevatorDoors4) {
        
       player.addGameObject(ElevatorDoors4);
       
       }
       
       //Add all known elevators to the player
       for(Prefab ElevatorDoors5 : elevatorDoors5) {
            
       player.addGameObject(ElevatorDoors5);
       
       }
       
       
        //Add all known elevators to the player
       for(Prefab ElevatorDoors6 : elevatorDoors6) {
       
       player.addGameObject(ElevatorDoors6);
      
       }
       
       
       //Add all known elevators to the player
       for(Prefab ElevatorDoors7 : elevatorDoors7) {
        
       player.addGameObject(ElevatorDoors7);
       
       }
       
       
       
        //Add all known elevators to the player
       for(Prefab ElevatorDoors8 : elevatorDoors8) {
       
       player.addGameObject(ElevatorDoors8);
       
       }
       
       //Add all known elevators to the player
       for(Prefab ElevatorDoors9 : elevatorDoors9) {
       
       player.addGameObject(ElevatorDoors9);
       
       }
       
        
       
     //  player.sendTextMessage("Event=");
       
       
       
       
         // MainMenu
          UIElement ElevatorMainPanel = new UIElement(); 
          player.addUIElement(ElevatorMainPanel);
          ElevatorMainPanel.setSize(75, 450, false);
          ElevatorMainPanel.setPosition(95, 45, true);
          ElevatorMainPanel.setBorder(5);
          ElevatorMainPanel.setBorderColor(999);
          ElevatorMainPanel.setBackgroundColor(12222);
          ElevatorMainPanel.setVisible(false);
          player.setAttribute("ElevatorMainPanel", ElevatorMainPanel);
          player.setAttribute("ElevatorMainPanelID", ElevatorMainPanel.getID());
          

          
           // Exit Button
          UILabel ElevatorExitbutton = new UILabel();                 
          ElevatorExitbutton.setClickable(true);
          ElevatorExitbutton.isClickable();
         // ElevatorLevel9button.setFontColor(50,999,999,50);
          ElevatorExitbutton.setRichTextEnabled(true);
          ElevatorExitbutton.setFontSize(20);
          ElevatorExitbutton.setFontColor(9.0f, 0.0f, 0.0f, 5.0f);
          ElevatorExitbutton.setText(" X ");
          ElevatorExitbutton.setBorder(2);
          ElevatorExitbutton.setBorderColor(999);
          ElevatorExitbutton.setPosition(20.0F, 5.0F, false);
          ElevatorMainPanel.addChild(ElevatorExitbutton);
          player.setAttribute("ElevatorExitbutton", ElevatorExitbutton);
          player.setAttribute("ElevatorExitbuttonID", ElevatorExitbutton.getID());
          
          
          
           // Level 9 Button
          UILabel ElevatorLevel9button = new UILabel();                 
          ElevatorLevel9button.setClickable(true);
          ElevatorLevel9button.isClickable();
         // ElevatorLevel9button.setFontColor(50,999,999,50);
          ElevatorLevel9button.setRichTextEnabled(true);
          ElevatorLevel9button.setFontSize(20);
          ElevatorLevel9button.setText(" 9 ");
          ElevatorLevel9button.setBorder(2);
          ElevatorLevel9button.setBorderColor(999);
          ElevatorLevel9button.setPosition(20.0F, 40.0F, false);
          ElevatorMainPanel.addChild(ElevatorLevel9button);
          player.setAttribute("ElevatorLevel9button", ElevatorLevel9button);
          player.setAttribute("ElevatorLevel9buttonID", ElevatorLevel9button.getID());
          
           // Level 8 Button
          UILabel ElevatorLevel8button = new UILabel();                 
          ElevatorLevel8button.setClickable(true);
          ElevatorLevel8button.isClickable();
        //  ElevatorLevel8button.setFontColor(50,999,999,50);
          ElevatorLevel8button.setRichTextEnabled(true);
          ElevatorLevel8button.setFontSize(20);
          ElevatorLevel8button.setText(" 8 ");
          ElevatorLevel8button.setBorder(2);
          ElevatorLevel8button.setBorderColor(999);
          ElevatorLevel8button.setPosition(20.0F, 75.0F, false);
          ElevatorMainPanel.addChild(ElevatorLevel8button);
          player.setAttribute("ElevatorLevel8button", ElevatorLevel8button);
          player.setAttribute("ElevatorLevel8buttonID", ElevatorLevel8button.getID());
          
          
           // Level 7 Button
          UILabel ElevatorLevel7button = new UILabel();                 
          ElevatorLevel7button.setClickable(true);
          ElevatorLevel7button.isClickable();
        //  ElevatorLevel7button.setFontColor(50,999,999,50);
          ElevatorLevel7button.setRichTextEnabled(true);
          ElevatorLevel7button.setFontSize(20);
          ElevatorLevel7button.setText(" 7 ");
          ElevatorLevel7button.setBorder(2);
          ElevatorLevel7button.setBorderColor(999);
          ElevatorLevel7button.setPosition(20.0F, 110.0F, false);
          ElevatorMainPanel.addChild(ElevatorLevel7button);
          player.setAttribute("ElevatorLevel7button", ElevatorLevel7button);
          player.setAttribute("ElevatorLevel7buttonID", ElevatorLevel7button.getID());
          
          
           // Level 6 Button
          UILabel ElevatorLevel6button = new UILabel();                 
          ElevatorLevel6button.setClickable(true);
          ElevatorLevel6button.isClickable();
       //   ElevatorLevel6button.setFontColor(50,999,999,50);
          ElevatorLevel6button.setRichTextEnabled(true);
          ElevatorLevel6button.setFontSize(20);
          ElevatorLevel6button.setText(" 6 ");
          ElevatorLevel6button.setBorder(2);
          ElevatorLevel6button.setBorderColor(999);
          ElevatorLevel6button.setPosition(20.0F, 145.0F, false);
          ElevatorMainPanel.addChild(ElevatorLevel6button);
          player.setAttribute("ElevatorLevel6button", ElevatorLevel6button);
          player.setAttribute("ElevatorLevel6buttonID", ElevatorLevel6button.getID());
          
          
           // Level 5 Button
          UILabel ElevatorLevel5button = new UILabel();                 
          ElevatorLevel5button.setClickable(true);
          ElevatorLevel5button.isClickable();
       //   ElevatorLevel5button.setFontColor(50,999,999,50);
          ElevatorLevel5button.setRichTextEnabled(true);
          ElevatorLevel5button.setFontSize(20);
          ElevatorLevel5button.setText(" 5 ");
          ElevatorLevel5button.setBorder(2);
          ElevatorLevel5button.setBorderColor(999);
          ElevatorLevel5button.setPosition(20.0F, 180.0F, false);
          ElevatorMainPanel.addChild(ElevatorLevel5button);
          player.setAttribute("ElevatorLevel5button", ElevatorLevel5button);
          player.setAttribute("ElevatorLevel5buttonID", ElevatorLevel5button.getID());
          
           // Level 4 Button
          UILabel ElevatorLevel4button = new UILabel();                 
          ElevatorLevel4button.setClickable(true);
          ElevatorLevel4button.isClickable();
         // ElevatorLevel4button.setFontColor(50,999,999,50);
          ElevatorLevel4button.setRichTextEnabled(true);
          ElevatorLevel4button.setFontSize(20);
          ElevatorLevel4button.setText(" 4 ");
          ElevatorLevel4button.setBorder(2);
          ElevatorLevel4button.setBorderColor(999);
          ElevatorLevel4button.setPosition(20.0F, 215.0F, false);
          ElevatorMainPanel.addChild(ElevatorLevel4button);
          player.setAttribute("ElevatorLevel4button", ElevatorLevel4button);
          player.setAttribute("ElevatorLevel4buttonID", ElevatorLevel4button.getID());
          
           // Level 3 Button
          UILabel ElevatorLevel3button = new UILabel();                 
          ElevatorLevel3button.setClickable(true);
          ElevatorLevel3button.isClickable();
        //  ElevatorLevel3button.setFontColor(50,999,999,50);
          ElevatorLevel3button.setRichTextEnabled(true);
          ElevatorLevel3button.setFontSize(20);
          ElevatorLevel3button.setText(" 3 ");
          ElevatorLevel3button.setBorder(2);
          ElevatorLevel3button.setBorderColor(999);
          ElevatorLevel3button.setPosition(20.0F, 250.0F, false);
          ElevatorMainPanel.addChild(ElevatorLevel3button);
          player.setAttribute("ElevatorLevel3button", ElevatorLevel3button);
          player.setAttribute("ElevatorLevel3buttonID", ElevatorLevel3button.getID());
          
          
           // Level 2 Button
          UILabel ElevatorLevel2button = new UILabel();                 
          ElevatorLevel2button.setClickable(true);
          ElevatorLevel2button.isClickable();
          //ElevatorLevel2button.setFontColor(50,999,999,50);
          ElevatorLevel2button.setRichTextEnabled(true);
          ElevatorLevel2button.setFontSize(20);
          ElevatorLevel2button.setText(" 2 ");
          ElevatorLevel2button.setBorder(2);
          ElevatorLevel2button.setBorderColor(999);
          ElevatorLevel2button.setPosition(20.0F, 285.0F, false);
          ElevatorMainPanel.addChild(ElevatorLevel2button);
          player.setAttribute("ElevatorLevel2button", ElevatorLevel2button);
          player.setAttribute("ElevatorLevel2buttonID", ElevatorLevel2button.getID());
          
          
          
           // Level 1 Button
          UILabel ElevatorLevel1button = new UILabel();                 
          ElevatorLevel1button.setClickable(true);
          ElevatorLevel1button.isClickable();
        //  ElevatorLevel1button.setFontColor(50,999,999,50);
          ElevatorLevel1button.setRichTextEnabled(true);
          ElevatorLevel1button.setFontSize(20);
          ElevatorLevel1button.setText(" 1 ");
          ElevatorLevel1button.setBorder(2);
          ElevatorLevel1button.setBorderColor(999);
          ElevatorLevel1button.setPosition(20.0F, 320.0F, false);
          ElevatorMainPanel.addChild(ElevatorLevel1button);
          player.setAttribute("ElevatorLevel1button", ElevatorLevel1button);
          player.setAttribute("ElevatorLevel1buttonID", ElevatorLevel1button.getID());
          
          // Level 0 Button
          UILabel ElevatorLevel0button = new UILabel();                 
          ElevatorLevel0button.setClickable(true);
          ElevatorLevel0button.isClickable();
         //  ElevatorLevel0button.setFontColor(50,999,999,50);
          ElevatorLevel0button.setRichTextEnabled(true);
          ElevatorLevel0button.setFontSize(20);
          ElevatorLevel0button.setText(" 0 ");
          ElevatorLevel0button.setBorder(2);
          ElevatorLevel0button.setBorderColor(999);
          ElevatorLevel0button.setPosition(20.0F, 355.0F, false);
          ElevatorMainPanel.addChild(ElevatorLevel0button);
          player.setAttribute("ElevatorLevel0button", ElevatorLevel0button);
          player.setAttribute("ElevatorLevel0buttonID", ElevatorLevel0button.getID());
       
          
         // Level 0 Button
          UILabel ElevatorDeletebutton = new UILabel();                 
          ElevatorDeletebutton.setClickable(true);
          ElevatorDeletebutton.isClickable();
         //  ElevatorLevel0button.setFontColor(50,999,999,50);
          ElevatorDeletebutton.setRichTextEnabled(true);
          ElevatorDeletebutton.setFontSize(14);
          ElevatorDeletebutton.setText(" Delete ");
          ElevatorDeletebutton.setFontColor(9.0f, 0.0f, 0.0f, 5.0f);
          ElevatorDeletebutton.style.textAlign.set(TextAnchor.MiddleCenter);
          ElevatorDeletebutton.setBorder(2);
          ElevatorDeletebutton.setBorderColor(999);
          ElevatorDeletebutton.setPosition(6.0F, 410.0F, false);
          ElevatorMainPanel.addChild(ElevatorDeletebutton);
          player.setAttribute("ElevatorDeletebutton", ElevatorDeletebutton);
          player.setAttribute("ElevatorDeletebuttonID", ElevatorDeletebutton.getID());
          
          
          
          
          
          
       
       
  }
    
  
  
  
  
        @EventMethod
  public void onPlayerUIEClick(PlayerUIElementClickEvent event) throws SQLException {
  if (event.getUIElement() == null) return;
  
  Player player = event.getPlayer();
  
       int eventID = 0;
   
       eventID = event.getUIElement().getID();
      
      
      
      
       
      
       // get the button IDs
      int ElevatorLevel9buttonID = (int) player.getAttribute("ElevatorLevel9buttonID"); 
      int ElevatorLevel8buttonID = (int) player.getAttribute("ElevatorLevel8buttonID"); 
      int ElevatorLevel7buttonID = (int) player.getAttribute("ElevatorLevel7buttonID");
      int ElevatorLevel6buttonID = (int) player.getAttribute("ElevatorLevel6buttonID"); 
      int ElevatorLevel5buttonID = (int) player.getAttribute("ElevatorLevel5buttonID"); 
      int ElevatorLevel4buttonID = (int) player.getAttribute("ElevatorLevel4buttonID");
      int ElevatorLevel3buttonID = (int) player.getAttribute("ElevatorLevel3buttonID");
      int ElevatorLevel2buttonID = (int) player.getAttribute("ElevatorLevel2buttonID"); 
      int ElevatorLevel1buttonID = (int) player.getAttribute("ElevatorLevel1buttonID");
      int ElevatorLevel0buttonID = (int) player.getAttribute("ElevatorLevel0buttonID");
      
      
      
      
      int ElevatorMainPanelID = (int) player.getAttribute("ElevatorMainPanelID");
      
      UIElement ElevatorMainPanel = (UIElement) player.getAttribute("ElevatorMainPanel");
      
      
      int ElevatorID = (int) player.getAttribute("ElevatorID");
      
      
      int ElevatorExitbuttonID = (int) player.getAttribute("ElevatorExitbuttonID");
      
      int ElevatorDeletebuttonID = (int) player.getAttribute("ElevatorDeletebuttonID");
      
      
      
        // ElevatorDeletebutton
        if (eventID == ElevatorDeletebuttonID){
        if(player.isAdmin()){
            
            
           for(Iterator<Prefab> RemoveElevators = elevators.iterator();
                                   RemoveElevators.hasNext();){            
                    Prefab Elevators = RemoveElevators.next(); 
                    if(Elevators.getID() == ElevatorID){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(Elevators);
                    }    
                    database.executeUpdate("DELETE FROM `Elevators` WHERE `ElevatorID` = '"+ElevatorID+"'");    
                    RemoveElevators.remove(); 
                    }
                    }  
            
            try(ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"'")){
            while(result.next()) {
                
            int ElevatorDoorsID = result.getInt("ElevatorDoorsID");
            String ElevatorFloorHeight = result.getString("ElevatorFloorHeight");
            
            
        //    player.sendTextMessage("door ids="+ElevatorDoorsID+" height="+ElevatorFloorHeight);
             
            if(ElevatorFloorHeight.equals("ElevatorDoor0")){
            for(Iterator<Prefab> RemoveElevatorDoors = elevatorDoors0.iterator();
                                   RemoveElevatorDoors.hasNext();){            
                    Prefab ElevatorDoors = RemoveElevatorDoors.next(); 
                    if(ElevatorDoors.getID() == ElevatorDoorsID){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(ElevatorDoors);
                    }    
                    database0.executeUpdate("DELETE FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"' AND `ElevatorDoorsID` = '"+ElevatorDoorsID+"'");   
                    RemoveElevatorDoors.remove(); 
                    }
                    }
            }else{   
                
            if(ElevatorFloorHeight.equals("ElevatorDoor1")){
            for(Iterator<Prefab> RemoveElevatorDoors = elevatorDoors1.iterator();
                                   RemoveElevatorDoors.hasNext();){            
                    Prefab ElevatorDoors = RemoveElevatorDoors.next(); 
                    if(ElevatorDoors.getID() == ElevatorDoorsID){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(ElevatorDoors);
                    }    
                    database1.executeUpdate("DELETE FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"' AND `ElevatorDoorsID` = '"+ElevatorDoorsID+"'");   
                    RemoveElevatorDoors.remove(); 
                    }
                    }
            }else{
            
            if(ElevatorFloorHeight.equals("ElevatorDoor2")){
            for(Iterator<Prefab> RemoveElevatorDoors = elevatorDoors2.iterator();
                                   RemoveElevatorDoors.hasNext();){            
                    Prefab ElevatorDoors = RemoveElevatorDoors.next(); 
                    if(ElevatorDoors.getID() == ElevatorDoorsID){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(ElevatorDoors);
                    }    
                    database2.executeUpdate("DELETE FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"' AND `ElevatorDoorsID` = '"+ElevatorDoorsID+"'");   
                    RemoveElevatorDoors.remove(); 
                    }
                    }
            }else{
            
            if(ElevatorFloorHeight.equals("ElevatorDoor3")){
            for(Iterator<Prefab> RemoveElevatorDoors = elevatorDoors3.iterator();
                                   RemoveElevatorDoors.hasNext();){            
                    Prefab ElevatorDoors = RemoveElevatorDoors.next(); 
                    if(ElevatorDoors.getID() == ElevatorDoorsID){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(ElevatorDoors);
                    }    
                    database3.executeUpdate("DELETE FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"' AND `ElevatorDoorsID` = '"+ElevatorDoorsID+"'");   
                    RemoveElevatorDoors.remove(); 
                    }
                    }
            }else{
            
            if(ElevatorFloorHeight.equals("ElevatorDoor4")){
            for(Iterator<Prefab> RemoveElevatorDoors = elevatorDoors4.iterator();
                                   RemoveElevatorDoors.hasNext();){            
                    Prefab ElevatorDoors = RemoveElevatorDoors.next(); 
                    if(ElevatorDoors.getID() == ElevatorDoorsID){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(ElevatorDoors);
                    }    
                    database4.executeUpdate("DELETE FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"' AND `ElevatorDoorsID` = '"+ElevatorDoorsID+"'");   
                    RemoveElevatorDoors.remove(); 
                    }
                    }
            }else{
            
            if(ElevatorFloorHeight.equals("ElevatorDoor5")){
            for(Iterator<Prefab> RemoveElevatorDoors = elevatorDoors5.iterator();
                                   RemoveElevatorDoors.hasNext();){            
                    Prefab ElevatorDoors = RemoveElevatorDoors.next(); 
                    if(ElevatorDoors.getID() == ElevatorDoorsID){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(ElevatorDoors);
                    }    
                    database5.executeUpdate("DELETE FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"' AND `ElevatorDoorsID` = '"+ElevatorDoorsID+"'");   
                    RemoveElevatorDoors.remove(); 
                    }
                    }
            }else{
            
            
            
            if(ElevatorFloorHeight.equals("ElevatorDoor6")){
            for(Iterator<Prefab> RemoveElevatorDoors = elevatorDoors6.iterator();
                                   RemoveElevatorDoors.hasNext();){            
                    Prefab ElevatorDoors = RemoveElevatorDoors.next(); 
                    if(ElevatorDoors.getID() == ElevatorDoorsID){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(ElevatorDoors);
                    }    
                    database6.executeUpdate("DELETE FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"' AND `ElevatorDoorsID` = '"+ElevatorDoorsID+"'");   
                    RemoveElevatorDoors.remove(); 
                    }
                    }
            }else{
            
            
            
            
            if(ElevatorFloorHeight.equals("ElevatorDoor7")){
            for(Iterator<Prefab> RemoveElevatorDoors = elevatorDoors7.iterator();
                                   RemoveElevatorDoors.hasNext();){            
                    Prefab ElevatorDoors = RemoveElevatorDoors.next(); 
                    if(ElevatorDoors.getID() == ElevatorDoorsID){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(ElevatorDoors);
                    }    
                    database7.executeUpdate("DELETE FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"' AND `ElevatorDoorsID` = '"+ElevatorDoorsID+"'");   
                    RemoveElevatorDoors.remove(); 
                    }
                    }
            }else{
            
            
            
            
            if(ElevatorFloorHeight.equals("ElevatorDoor8")){
            for(Iterator<Prefab> RemoveElevatorDoors = elevatorDoors8.iterator();
                                   RemoveElevatorDoors.hasNext();){            
                    Prefab ElevatorDoors = RemoveElevatorDoors.next(); 
                    if(ElevatorDoors.getID() == ElevatorDoorsID){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(ElevatorDoors);
                    }    
                    database8.executeUpdate("DELETE FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"' AND `ElevatorDoorsID` = '"+ElevatorDoorsID+"'");   
                    RemoveElevatorDoors.remove(); 
                    }
                    }
            }else{
            
            
            
            if(ElevatorFloorHeight.equals("ElevatorDoor9")){
            for(Iterator<Prefab> RemoveElevatorDoors = elevatorDoors9.iterator();
                                   RemoveElevatorDoors.hasNext();){            
                    Prefab ElevatorDoors = RemoveElevatorDoors.next(); 
                    if(ElevatorDoors.getID() == ElevatorDoorsID){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(ElevatorDoors);
                    }    
                    database9.executeUpdate("DELETE FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"' AND `ElevatorDoorsID` = '"+ElevatorDoorsID+"'");   
                    RemoveElevatorDoors.remove(); 
                    }
                    }
            }
            
            
            }}}}}}}}}
            
            
            
            
                
            }    
            }
            catch(Exception e) {
            e.printStackTrace();
            } 
            
            
            player.sendYellMessage("Elevator Deleted", 3, false);
            player.setMouseCursorVisible(false);
            ElevatorMainPanel.setVisible(false);
           
           
        }else{player.sendTextMessage("Only Admins Can Remove Elevators");}
        }
      
      
      
      
        // exit menu
        if (eventID == ElevatorExitbuttonID){ 
        player.setMouseCursorVisible(false);
        ElevatorMainPanel.setVisible(false);    
        }
        
        
        
        
        
      // Lift button 0
  if (eventID == ElevatorLevel0buttonID){ 
      
         Vector3f playerPos = player.getPosition();
         
          float ElevatorX = (float)playerPos.x;
          float ElevatorY = (float)playerPos.y;
          float ElevatorZ = (float)playerPos.z;  
          
          Vector3f Blockpos = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
          
       try (ResultSet result = database.executeQuery("SELECT * FROM `Elevators` WHERE `ElevatorFloorHeight` = 'Elevator' AND `ElevatorFloorsAmount` = '0' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
            if (result.next()) {
               
               
               int StoredElevatorID = result.getInt("ElevatorID");
               int Floor = result.getInt("ElevatorFloorsAmount");
               
               String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
               
                  for(Prefab Elevator : elevators) {
                  int getElevatorID = Elevator.getID();
                  
                  
              // close all outer doors     
             if (getElevatorID == StoredElevatorID){ 
                 
                   try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                   int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   }
                 
                 
                   
                   // Close doors
                  Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                  Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                   // play door sound loop   
                   SoundAsset Doorsoundfile = (SoundAsset)player.getAttribute("Doorsoundfile");
                   for(Player AllPlayers : Server.getAllPlayers()){
                     Vector3f CarrentPosition = Elevator.getLocalPosition();   
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     } 
                   
                 
               
                   // wait for doors to close
                   Timer timer10sec = new Timer(0.8F, 0.0F, 0, null);
                   Runnable timerTask10sec = () -> {
                   Elevator.moveToLocalPosition(GBlockposX, GBlockposY, GBlockposZ, 8);         
                   };
                   timer10sec.setTask(timerTask10sec);
                   timer10sec.start();
                   
                   
                   
                   
                   Timer timersec = new Timer(1.0F, 0.0F, 20, null);
                   Runnable timerTasksec = () -> {
                       
                   Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                   
                   
                   
                   
                   
                   Vector3f playPosition = player.getPosition();
                   float playerintPosition = (float) playPosition.y;
                   float playerPosition  = playerintPosition;
                           
                   Vector3f MoveToPosition = new Vector3f(GBlockposX, GBlockposY, GBlockposZ);
               //    player.sendTextMessage("CarrentPosition="+CarrentPosition+" move too="+MoveToPosition);
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                  int ElevatorDoorsID = ElevatorDoors9.getID();    
                  int  StoredElevatorDoorsID = (int)player.getAttribute("StoredElevatorDoorsID");
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                  SoundAsset SoundLoop = (SoundAsset)player.getAttribute("SoundLoop");
                  for(Player AllPlayers : Server.getAllPlayers()){
                  AllPlayers.playSound(SoundLoop, false, 1f, 1f, 0f, 100f, ElevatorDoors9.getLocalPosition());   
                  }
                  
                  }}
                   
                   
                   if(CarrentPosition.equals(MoveToPosition)){
                  //   player.playSound(SoundLoop).stop(true); 
                     
                     
                     player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                     
                     
                     
                     // Open Doors = 2
                     Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                     Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2);
                     
                     // play door sound loop
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                     
                     
                     
                   try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorsAmount` = '0' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   if (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");    
                   
                  for(Prefab ElevatorDoors0 : elevatorDoors0) {
                  int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                //  player.sendTextMessage("level 0 doors close");
                  
                  ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                  ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 2);
       
                  }}  
                       
                       
                       
                   }
                   }   catch (SQLException ex) {
                           Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
                       }
                     
                     
                   
                   
                   timersec.kill();
                   }    
                   };
                   timersec.setTask(timerTasksec);
                   timersec.start();
                   
                    
                  
                   
                   
                   
                   
                   
                   
                   
                   
                 
             
              }
            } 
          }
        }
        
      
            player.setMouseCursorVisible(false);
            ElevatorMainPanel.setVisible(false);
        
  }
      
      
   
  
         
      // Lift button 1
  if (eventID == ElevatorLevel1buttonID){ 
      
         Vector3f playerPos = player.getPosition();
          
          
          float ElevatorX = (float)playerPos.x;
          float ElevatorY = (float)playerPos.y;
          float ElevatorZ = (float)playerPos.z;  
          
          Vector3f Blockpos = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
       
        try (ResultSet result = database.executeQuery("SELECT * FROM `Elevators` WHERE `ElevatorFloorHeight` = 'Elevator' AND `ElevatorFloorsAmount` = '1' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
            if (result.next()) {
               
               
               int StoredElevatorID = result.getInt("ElevatorID");
               int Floor = result.getInt("ElevatorFloorsAmount");
               
               String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
               
                  for(Prefab Elevator : elevators) {
                  int getElevatorID = Elevator.getID();
                   
             if (getElevatorID == StoredElevatorID){ 
                 
                 
                 
                 try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");    
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                   int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   }
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                    
                  // Close doors
                  Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                  Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1);
                 
                   // play door sound loop   
                   SoundAsset Doorsoundfile = (SoundAsset)player.getAttribute("Doorsoundfile");
                   for(Player AllPlayers : Server.getAllPlayers()){
                     Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     } 
                 
               
                   // wait for doors to close
                   Timer timer10sec = new Timer(0.8F, 0.0F, 0, null);
                   Runnable timerTask10sec = () -> {
                   Elevator.moveToLocalPosition(GBlockposX, GBlockposY, GBlockposZ, 8);         
                   };
                   timer10sec.setTask(timerTask10sec);
                   timer10sec.start();
                   
                   
                  
                   
                   
                   Timer timersec = new Timer(1.0F, 0.0F, 20, null);
                   Runnable timerTasksec = () -> {
                       
                   Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                   
                   
                          
                    
                   
                   
                   Vector3f playPosition = player.getPosition();
                   float playerintPosition = (float) playPosition.y;
                   float playerPosition  = playerintPosition;
                           
                   Vector3f MoveToPosition = new Vector3f(GBlockposX, GBlockposY, GBlockposZ);
                   //player.sendTextMessage("CarrentPosition="+CarrentPosition+" move too="+MoveToPosition);
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                  int ElevatorDoorsID = ElevatorDoors9.getID();    
                  int  StoredElevatorDoorsID = (int)player.getAttribute("StoredElevatorDoorsID");
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                  SoundAsset SoundLoop = (SoundAsset)player.getAttribute("SoundLoop");
                  for(Player AllPlayers : Server.getAllPlayers()){
                  AllPlayers.playSound(SoundLoop, false, 1f, 1f, 0f, 100f, ElevatorDoors9.getLocalPosition());   
                  }
                  
                  }}
                   
                   if(CarrentPosition.equals(MoveToPosition)){
                   //    player.playSound(SoundLoop).stop(true);
                       player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                  
                     
                     
                     
                     Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                     Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2);
                     
                     // play door sound loop   
                      for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     } 
                     
                     
                    try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorsAmount` = '1' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   if (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");    
                   
                  for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
               //   player.sendTextMessage("level 1 doors close");
                 
                  
                  
                  ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                  ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 2);
       
                  }}  
                       
                       
                       
                   }
                   }   catch (SQLException ex) {
                           Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
                       }
                     
                     
                     
                     
                   
                   timersec.kill();
                   }    
                   };
                   timersec.setTask(timerTasksec);
                   timersec.start();
                   
                 
                 
                 
              }
            } 
          }
        }
        
      
            player.setMouseCursorVisible(false);
            ElevatorMainPanel.setVisible(false);
      
  }
      
  
  
  
  
  
  
  
  
  
  
     // Lift button 2
  if (eventID == ElevatorLevel2buttonID){ 
      
         Vector3f playerPos = player.getPosition();
          
          
          float ElevatorX = (float)playerPos.x;
          float ElevatorY = (float)playerPos.y;
          float ElevatorZ = (float)playerPos.z;  
          
          Vector3f Blockpos = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
       
        try (ResultSet result = database.executeQuery("SELECT * FROM `Elevators` WHERE `ElevatorFloorHeight` = 'Elevator' AND `ElevatorFloorsAmount` = '2' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
            if (result.next()) {
               
               
               int StoredElevatorID = result.getInt("ElevatorID");
               int Floor = result.getInt("ElevatorFloorsAmount");
               
               String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
               
                  for(Prefab Elevator : elevators) {
                  int getElevatorID = Elevator.getID();
                   
             if (getElevatorID == StoredElevatorID){ 
                 
                  try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");    
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                   int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   }
                 
                   // Close doors
                  Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                  Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1);
                 
                   // play door sound loop   
                   SoundAsset Doorsoundfile = (SoundAsset)player.getAttribute("Doorsoundfile");
                   for(Player AllPlayers : Server.getAllPlayers()){
                     Vector3f CarrentPosition = Elevator.getLocalPosition();   
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                 
               
                   // wait for doors to close
                   Timer timer10sec = new Timer(0.8F, 0.0F, 0, null);
                   Runnable timerTask10sec = () -> {
                   Elevator.moveToLocalPosition(GBlockposX, GBlockposY, GBlockposZ, 8);         
                   };
                   timer10sec.setTask(timerTask10sec);
                   timer10sec.start();
                   
                   
                  
                  
                   
                   Timer timersec = new Timer(1.0F, 0.0F, 20, null);
                   Runnable timerTasksec = () -> {
                       
                   Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                   
                   
                  
                   
                   
                   Vector3f playPosition = player.getPosition();
                   float playerintPosition = (float) playPosition.y;
                   float playerPosition  = playerintPosition;
                           
                   Vector3f MoveToPosition = new Vector3f(GBlockposX, GBlockposY, GBlockposZ);
             //      player.sendTextMessage("CarrentPosition="+CarrentPosition+" move too="+MoveToPosition);
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                  int ElevatorDoorsID = ElevatorDoors9.getID();    
                  int  StoredElevatorDoorsID = (int)player.getAttribute("StoredElevatorDoorsID");
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                  SoundAsset SoundLoop = (SoundAsset)player.getAttribute("SoundLoop");
                  for(Player AllPlayers : Server.getAllPlayers()){
                  AllPlayers.playSound(SoundLoop, false, 1f, 1f, 0f, 100f, ElevatorDoors9.getLocalPosition());   
                  }
                  
                  }}
                   
                   if(CarrentPosition.equals(MoveToPosition)){
                   //    player.playSound(SoundLoop).stop(true);
                      player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                 
                     
                     
                     Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                     Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2);
                     
                     // play door sound loop   
                      for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                      
                     
                      try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorsAmount` = '2' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   if (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");    
                   
                  for(Prefab ElevatorDoors2 : elevatorDoors2) {
                  int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                //  player.sendTextMessage("level 2 doors close");
                 
                  
                  
                  ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                  ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 2);
       
                  }}  
                       
                       
                       
                   }
                   }   catch (SQLException ex) {
                           Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
                       }
                   
                   timersec.kill();
                   }    
                   };
                   timersec.setTask(timerTasksec);
                   timersec.start();
                   
             
              }
            } 
          }
        }
        
      
            player.setMouseCursorVisible(false);
            ElevatorMainPanel.setVisible(false);
      
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
     // Lift button 3
  if (eventID == ElevatorLevel3buttonID){ 
      
         Vector3f playerPos = player.getPosition();
          
          
          float ElevatorX = (float)playerPos.x;
          float ElevatorY = (float)playerPos.y;
          float ElevatorZ = (float)playerPos.z;  
          
          Vector3f Blockpos = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
       
        try (ResultSet result = database.executeQuery("SELECT * FROM `Elevators` WHERE `ElevatorFloorHeight` = 'Elevator' AND `ElevatorFloorsAmount` = '3' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
            if (result.next()) {
               
               
               int StoredElevatorID = result.getInt("ElevatorID");
               int Floor = result.getInt("ElevatorFloorsAmount");
               
               String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
               
                  for(Prefab Elevator : elevators) {
                  int getElevatorID = Elevator.getID();
                   
             if (getElevatorID == StoredElevatorID){ 
                 
                 
                 try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                 //  player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                   int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   }
                 
                 
                  // Close doors
                  Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                  Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1);
                 
                   // play door sound loop   
                   SoundAsset Doorsoundfile = (SoundAsset)player.getAttribute("Doorsoundfile");
                   for(Player AllPlayers : Server.getAllPlayers()){
                     Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     } 
                 
               
                   // wait for doors to close
                   Timer timer10sec = new Timer(0.8F, 0.0F, 0, null);
                   Runnable timerTask10sec = () -> {
                   Elevator.moveToLocalPosition(GBlockposX, GBlockposY, GBlockposZ, 8);         
                   };
                   timer10sec.setTask(timerTask10sec);
                   timer10sec.start();
                   
                   
                  
                  
                   
                   Timer timersec = new Timer(1.0F, 0.0F, 20, null);
                   Runnable timerTasksec = () -> {
                       
                    Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                   
                   
                         
                    
                   
                   
                   Vector3f playPosition = player.getPosition();
                   float playerintPosition = (float) playPosition.y;
                   float playerPosition  = playerintPosition;
                           
                   Vector3f MoveToPosition = new Vector3f(GBlockposX, GBlockposY, GBlockposZ);
            //       player.sendTextMessage("CarrentPosition="+CarrentPosition+" move too="+MoveToPosition);
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                  int ElevatorDoorsID = ElevatorDoors9.getID();    
                  int  StoredElevatorDoorsID = (int)player.getAttribute("StoredElevatorDoorsID");
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                  SoundAsset SoundLoop = (SoundAsset)player.getAttribute("SoundLoop");
                  for(Player AllPlayers : Server.getAllPlayers()){
                  AllPlayers.playSound(SoundLoop, false, 1f, 1f, 0f, 100f, ElevatorDoors9.getLocalPosition());   
                  }
                  
                  }}
                   
                   
                   
                   if(CarrentPosition.equals(MoveToPosition)){
                   //    player.playSound(SoundLoop).stop(true);
                       player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                  
                    
                     Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                     Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2);
                     
                     // play door sound loop   
                      for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                      
                      
                     
                      try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorsAmount` = '3' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   if (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");    
                   
                  for(Prefab ElevatorDoors3 : elevatorDoors3) {
                  int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
               //   player.sendTextMessage("level 3 doors close");
                 
                  
                  
                  ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                  ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 2);
       
                  }}  
                       
                       
                       
                   }
                   }   catch (SQLException ex) {
                           Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
                       }
                     
                     
                   
                   timersec.kill();
                   }    
                   };
                   timersec.setTask(timerTasksec);
                   timersec.start();
                   
             
              }
            } 
          }
        }
        
      
            player.setMouseCursorVisible(false);
            ElevatorMainPanel.setVisible(false);
      
  }
  
  
  
  
     // Lift button 4
  if (eventID == ElevatorLevel4buttonID){ 
      
         Vector3f playerPos = player.getPosition();
          
          
          float ElevatorX = (float)playerPos.x;
          float ElevatorY = (float)playerPos.y;
          float ElevatorZ = (float)playerPos.z;  
          
          Vector3f Blockpos = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
       
        try (ResultSet result = database.executeQuery("SELECT * FROM `Elevators` WHERE `ElevatorFloorHeight` = 'Elevator' AND `ElevatorFloorsAmount` = '4' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
            if (result.next()) {
             
               
               int StoredElevatorID = result.getInt("ElevatorID");
               int Floor = result.getInt("ElevatorFloorsAmount");
               
               String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
               
                  for(Prefab Elevator : elevators) {
                  int getElevatorID = Elevator.getID();
                   
             if (getElevatorID == StoredElevatorID){ 
                 
                 
                 
                 try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                 //  player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                   int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   }
                 
                 
                 
                  // Close doors
                  Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                  Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1);
                 
                   // play door sound loop   
                   SoundAsset Doorsoundfile = (SoundAsset)player.getAttribute("Doorsoundfile");
                   for(Player AllPlayers : Server.getAllPlayers()){
                       Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                 
               
                   // wait for doors to close
                   Timer timer10sec = new Timer(0.8F, 0.0F, 0, null);
                   Runnable timerTask10sec = () -> {
                   Elevator.moveToLocalPosition(GBlockposX, GBlockposY, GBlockposZ, 8);         
                   };
                   timer10sec.setTask(timerTask10sec);
                   timer10sec.start();
                   
                   
                  
                  
                   
                   Timer timersec = new Timer(1.0F, 0.0F, 20, null);
                   Runnable timerTasksec = () -> {
                       
                    Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                   
                   
                 
                   
                   
                   Vector3f playPosition = player.getPosition();
                   float playerintPosition = (float) playPosition.y;
                   float playerPosition  = playerintPosition;
                           
                   Vector3f MoveToPosition = new Vector3f(GBlockposX, GBlockposY, GBlockposZ);
             //      player.sendTextMessage("CarrentPosition="+CarrentPosition+" move too="+MoveToPosition);
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                  int ElevatorDoorsID = ElevatorDoors9.getID();    
                  int  StoredElevatorDoorsID = (int)player.getAttribute("StoredElevatorDoorsID");
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                  SoundAsset SoundLoop = (SoundAsset)player.getAttribute("SoundLoop");
                  for(Player AllPlayers : Server.getAllPlayers()){
                  AllPlayers.playSound(SoundLoop, false, 1f, 1f, 0f, 100f, ElevatorDoors9.getLocalPosition());   
                  }
                  
                  }}
                   
                   
                   
                   if(CarrentPosition.equals(MoveToPosition)){
                    //   player.playSound(SoundLoop).stop(true);
                       player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                   
                     
                     
                     Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                     Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2);
                     
                     // play door sound loop   
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                     
                      try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorsAmount` = '4' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   if (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");    
                   
                  for(Prefab ElevatorDoors4 : elevatorDoors4) {
                  int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
               //   player.sendTextMessage("level 1 doors close");
                 
                  
                  
                  ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                  ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 2);
       
                  }}  
                       
                       
                       
                   }
                   }   catch (SQLException ex) {
                           Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
                       }
                     
                     
                   
                   timersec.kill();
                   }    
                   };
                   timersec.setTask(timerTasksec);
                   timersec.start();
                   
             
              }
            } 
          }
        }
        
      
            player.setMouseCursorVisible(false);
            ElevatorMainPanel.setVisible(false);
      
  }
  
  
  
     // Lift button 5
  if (eventID == ElevatorLevel5buttonID){ 
      
         Vector3f playerPos = player.getPosition();
          
          
          float ElevatorX = (float)playerPos.x;
          float ElevatorY = (float)playerPos.y;
          float ElevatorZ = (float)playerPos.z;  
          
          Vector3f Blockpos = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
       
        try (ResultSet result = database.executeQuery("SELECT * FROM `Elevators` WHERE `ElevatorFloorHeight` = 'Elevator' AND `ElevatorFloorsAmount` = '5' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
            if (result.next()) {
               
               
               int StoredElevatorID = result.getInt("ElevatorID");
               int Floor = result.getInt("ElevatorFloorsAmount");
               
               String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
               
                  for(Prefab Elevator : elevators) {
                  int getElevatorID = Elevator.getID();
                   
             if (getElevatorID == StoredElevatorID){ 
                 
                
                 try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                   int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   }
                 
                 
                   // Close doors
                  Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                  Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1);
                 
                   // play door sound loop   
                   SoundAsset Doorsoundfile = (SoundAsset)player.getAttribute("Doorsoundfile");
                   for(Player AllPlayers : Server.getAllPlayers()){
                       Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     } 
                 
               
                   // wait for doors to close
                   Timer timer10sec = new Timer(0.8F, 0.0F, 0, null);
                   Runnable timerTask10sec = () -> {
                   Elevator.moveToLocalPosition(GBlockposX, GBlockposY, GBlockposZ, 8);         
                   };
                   timer10sec.setTask(timerTask10sec);
                   timer10sec.start();
                   
                   
                  
                   
                   
                   Timer timersec = new Timer(1.0F, 0.0F, 20, null);
                   Runnable timerTasksec = () -> {
                       
                    Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                   
                   
                   
                   
                   Vector3f playPosition = player.getPosition();
                   float playerintPosition = (float) playPosition.y;
                   float playerPosition  = playerintPosition;
                           
                   Vector3f MoveToPosition = new Vector3f(GBlockposX, GBlockposY, GBlockposZ);
              //     player.sendTextMessage("CarrentPosition="+CarrentPosition+" move too="+MoveToPosition);
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                  int ElevatorDoorsID = ElevatorDoors9.getID();    
                  int  StoredElevatorDoorsID = (int)player.getAttribute("StoredElevatorDoorsID");
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                  SoundAsset SoundLoop = (SoundAsset)player.getAttribute("SoundLoop");
                  for(Player AllPlayers : Server.getAllPlayers()){
                  AllPlayers.playSound(SoundLoop, false, 1f, 1f, 0f, 100f, ElevatorDoors9.getLocalPosition());   
                  }
                  
                  }}
                   
                  
                   
                   if(CarrentPosition.equals(MoveToPosition)){
                    //   player.playSound(SoundLoop).stop(true);
                       player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                  
                     
                     
                     Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                     Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2);
                     
                     // play door sound loop
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                     
                      try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorsAmount` = '5' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   if (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");    
                   
                  for(Prefab ElevatorDoors5 : elevatorDoors5) {
                  int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
               //   player.sendTextMessage("level 5 doors close");
                 
                  
                  
                  ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                  ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 2);
       
                  }}  
                       
                       
                       
                   }
                   }   catch (SQLException ex) {
                           Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
                       }
                     
                     
                   
                   timersec.kill();
                   }    
                   };
                   timersec.setTask(timerTasksec);
                   timersec.start();
                   
             
              }
            } 
          }
        }
        
      
            player.setMouseCursorVisible(false);
            ElevatorMainPanel.setVisible(false);
      
  }
  
  
     // Lift button 6
  if (eventID == ElevatorLevel6buttonID){ 
      
         Vector3f playerPos = player.getPosition();
          
          
          float ElevatorX = (float)playerPos.x;
          float ElevatorY = (float)playerPos.y;
          float ElevatorZ = (float)playerPos.z;  
          
         Vector3f Blockpos = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
       
        try (ResultSet result = database.executeQuery("SELECT * FROM `Elevators` WHERE `ElevatorFloorHeight` = 'Elevator' AND `ElevatorFloorsAmount` = '6' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
            if (result.next()) {
               
               
               int StoredElevatorID = result.getInt("ElevatorID");
               int Floor = result.getInt("ElevatorFloorsAmount");
               
               String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
               
                  for(Prefab Elevator : elevators) {
                  int getElevatorID = Elevator.getID();
                   
             if (getElevatorID == StoredElevatorID){ 
                 
                 
                 try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                   int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   }
                 
                 
                 
                   // Close doors
                   Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1);
                 
                   // play door sound loop   
                   SoundAsset Doorsoundfile = (SoundAsset)player.getAttribute("Doorsoundfile");
                   for(Player AllPlayers : Server.getAllPlayers()){
                       Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     } 
                 
               
                   // wait for doors to close
                   Timer timer10sec = new Timer(0.8F, 0.0F, 0, null);
                   Runnable timerTask10sec = () -> {
                   Elevator.moveToLocalPosition(GBlockposX, GBlockposY, GBlockposZ, 8);         
                   };
                   timer10sec.setTask(timerTask10sec);
                   timer10sec.start();
                   
                   
                  
                   
                   
                   Timer timersec = new Timer(1.0F, 0.0F, 20, null);
                   Runnable timerTasksec = () -> {
                       
                   Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                   
                   
                  
                   
                   
                   Vector3f playPosition = player.getPosition();
                   float playerintPosition = (float) playPosition.y;
                   float playerPosition  = playerintPosition;
                           
                   Vector3f MoveToPosition = new Vector3f(GBlockposX, GBlockposY, GBlockposZ);
             //      player.sendTextMessage("CarrentPosition="+CarrentPosition+" move too="+MoveToPosition);
                    for(Prefab ElevatorDoors9 : elevatorDoors9) {
                  int ElevatorDoorsID = ElevatorDoors9.getID();    
                  int  StoredElevatorDoorsID = (int)player.getAttribute("StoredElevatorDoorsID");
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                  SoundAsset SoundLoop = (SoundAsset)player.getAttribute("SoundLoop");
                  for(Player AllPlayers : Server.getAllPlayers()){
                  AllPlayers.playSound(SoundLoop, false, 1f, 1f, 0f, 100f, ElevatorDoors9.getLocalPosition());   
                  }
                  
                  }}
                   
                   
                   if(CarrentPosition.equals(MoveToPosition)){
                    //   player.playSound(SoundLoop).stop(true);
                       player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                     
                     
                     Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                     Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2);
                     
                     // play door sound loop   
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                     
                      try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorsAmount` = '6' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   if (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");    
                   
                  for(Prefab ElevatorDoors6 : elevatorDoors6) {
                  int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                //  player.sendTextMessage("level 6 doors close");
                 
                  
                  
                  ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                  ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 2);
       
                  }}  
                       
                       
                       
                   }
                   }   catch (SQLException ex) {
                           Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
                       }
                     
                   
                   timersec.kill();
                   }    
                   };
                   timersec.setTask(timerTasksec);
                   timersec.start();
                   
             
              }
            } 
          }
        }
        
      
            player.setMouseCursorVisible(false);
            ElevatorMainPanel.setVisible(false);
      
  }
  
  
     // Lift button 7
  if (eventID == ElevatorLevel7buttonID){ 
      
         Vector3f playerPos = player.getPosition();
          
          
          float ElevatorX = (float)playerPos.x;
          float ElevatorY = (float)playerPos.y;
          float ElevatorZ = (float)playerPos.z;  
          
          Vector3f Blockpos = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
       
        try (ResultSet result = database.executeQuery("SELECT * FROM `Elevators` WHERE `ElevatorFloorHeight` = 'Elevator' AND `ElevatorFloorsAmount` = '7' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
            if (result.next()) {
               
               
               int StoredElevatorID = result.getInt("ElevatorID");
               int Floor = result.getInt("ElevatorFloorsAmount");
               
               String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
               
                  for(Prefab Elevator : elevators) {
                  int getElevatorID = Elevator.getID();
                   
             if (getElevatorID == StoredElevatorID){ 
                 
                 
                 try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                 //  player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                   int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   }
                 
                 
                 
                 
                  // Close doors
                  Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                  Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1);
                 
                   // play door sound loop   
                   SoundAsset Doorsoundfile = (SoundAsset)player.getAttribute("Doorsoundfile");
                   for(Player AllPlayers : Server.getAllPlayers()){
                       Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     } 
                 
               
                   // wait for doors to close
                   Timer timer10sec = new Timer(0.8F, 0.0F, 0, null);
                   Runnable timerTask10sec = () -> {
                   Elevator.moveToLocalPosition(GBlockposX, GBlockposY, GBlockposZ, 8);         
                   };
                   timer10sec.setTask(timerTask10sec);
                   timer10sec.start();
                   
                   
                  
                  
                   
                   Timer timersec = new Timer(1.0F, 0.0F, 20, null);
                   Runnable timerTasksec = () -> {
                       
                   Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                   
                   
                   
                   
                   
                   Vector3f playPosition = player.getPosition();
                   float playerintPosition = (float) playPosition.y;
                   float playerPosition  = playerintPosition;
                           
                   Vector3f MoveToPosition = new Vector3f(GBlockposX, GBlockposY, GBlockposZ);
             //      player.sendTextMessage("CarrentPosition="+CarrentPosition+" move too="+MoveToPosition);
                    for(Prefab ElevatorDoors9 : elevatorDoors9) {
                  int ElevatorDoorsID = ElevatorDoors9.getID();    
                  int  StoredElevatorDoorsID = (int)player.getAttribute("StoredElevatorDoorsID");
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                  SoundAsset SoundLoop = (SoundAsset)player.getAttribute("SoundLoop");
                  for(Player AllPlayers : Server.getAllPlayers()){
                  AllPlayers.playSound(SoundLoop, false, 1f, 1f, 0f, 100f, ElevatorDoors9.getLocalPosition());   
                  }
                  
                  }}
                   
                   
                   
                   if(CarrentPosition.equals(MoveToPosition)){
                    //   player.playSound(SoundLoop).stop(true);
                      player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                     
                     
                     Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                     Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2);
                     
                     // play door sound loop   
                      for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                     
                      try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorsAmount` = '7' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   if (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");    
                   
                  for(Prefab ElevatorDoors7 : elevatorDoors7) {
                  int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
               //   player.sendTextMessage("level 7 doors close");
                 
                  
                  
                  ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                  ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 2);
       
                  }}  
                       
                       
                       
                   }
                   }   catch (SQLException ex) {
                           Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
                       }
                   
                   timersec.kill();
                   }    
                   };
                   timersec.setTask(timerTasksec);
                   timersec.start();
                   
             
              }
            } 
          }
        }
        
      
            player.setMouseCursorVisible(false);
            ElevatorMainPanel.setVisible(false);
      
  }
  
  
  
     // Lift button 8
  if (eventID == ElevatorLevel8buttonID){ 
      
         Vector3f playerPos = player.getPosition();
          
          
          float ElevatorX = (float)playerPos.x;
          float ElevatorY = (float)playerPos.y;
          float ElevatorZ = (float)playerPos.z;  
          
          Vector3f Blockpos = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
       
        try (ResultSet result = database.executeQuery("SELECT * FROM `Elevators` WHERE `ElevatorFloorHeight` = 'Elevator' AND `ElevatorFloorsAmount` = '8' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
            if (result.next()) {
               
               
               int StoredElevatorID = result.getInt("ElevatorID");
               int Floor = result.getInt("ElevatorFloorsAmount");
               
               String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
               
                  for(Prefab Elevator : elevators) {
                  int getElevatorID = Elevator.getID();
                   
             if (getElevatorID == StoredElevatorID){ 
                 
                
                 try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                 //  player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                   int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                      
                   }
                   }
                 
                 
                 
                 // Close doors
                  Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                  Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1);
                 
                   // play door sound loop   
                   SoundAsset Doorsoundfile = (SoundAsset)player.getAttribute("Doorsoundfile");
                   for(Player AllPlayers : Server.getAllPlayers()){
                       Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     } 
                 
               
                   // wait for doors to close
                   Timer timer10sec = new Timer(0.8F, 0.0F, 0, null);
                   Runnable timerTask10sec = () -> {
                   Elevator.moveToLocalPosition(GBlockposX, GBlockposY, GBlockposZ, 8);         
                   };
                   timer10sec.setTask(timerTask10sec);
                   timer10sec.start();
                   
                   
                  
                  
                   
                   Timer timersec = new Timer(1.0F, 0.0F, 20, null);
                   Runnable timerTasksec = () -> {
                       
                    Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                   
                   
                   
                   
                   
                   Vector3f playPosition = player.getPosition();
                   float playerintPosition = (float) playPosition.y;
                   float playerPosition  = playerintPosition;
                           
                   Vector3f MoveToPosition = new Vector3f(GBlockposX, GBlockposY, GBlockposZ);
             //      player.sendTextMessage("CarrentPosition="+CarrentPosition+" move too="+MoveToPosition);
             
                  for(Prefab ElevatorDoors9 : elevatorDoors9) {
                  int ElevatorDoorsID = ElevatorDoors9.getID();    
                  int  StoredElevatorDoorsID = (int)player.getAttribute("StoredElevatorDoorsID");
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                  SoundAsset SoundLoop = (SoundAsset)player.getAttribute("SoundLoop");
                  for(Player AllPlayers : Server.getAllPlayers()){
                  AllPlayers.playSound(SoundLoop, false, 1f, 1f, 0f, 100f, ElevatorDoors9.getLocalPosition());   
                  }
                  
                  }}
                  
                   
                   if(CarrentPosition.equals(MoveToPosition)){
                    //   player.playSound(SoundLoop).stop(true);
                       player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                   
                     
                     
                     Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                     Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2);
                     
                     // play door sound loop   
                      for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                     
                      try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorsAmount` = '8' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   if (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");    
                   
                  for(Prefab ElevatorDoors8 : elevatorDoors8) {
                  int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                //  player.sendTextMessage("level 8 doors close");
                 
                  
                  
                  ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                  ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 2);
       
                  }}  
                       
                       
                       
                   }
                   }   catch (SQLException ex) {
                           Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
                       }
                   
                   timersec.kill();
                   }    
                   };
                   timersec.setTask(timerTasksec);
                   timersec.start();
                   
             
              }
            } 
          }
        }
        
      
            player.setMouseCursorVisible(false);
            ElevatorMainPanel.setVisible(false);
      
  }
  
  
  
     // Lift button 9
  if (eventID == ElevatorLevel9buttonID){ 
      
         Vector3f playerPos = player.getPosition();
          
          
          float ElevatorX = (float)playerPos.x;
          float ElevatorY = (float)playerPos.y;
          float ElevatorZ = (float)playerPos.z;  
          
          Vector3f Blockpos = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
       
        try (ResultSet result = database.executeQuery("SELECT * FROM `Elevators` WHERE `ElevatorFloorHeight` = 'Elevator' AND `ElevatorFloorsAmount` = '9' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
            if (result.next()) {
               
               
               int StoredElevatorID = result.getInt("ElevatorID");
               int Floor = result.getInt("ElevatorFloorsAmount");
               
               String BlockposGet = result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
               
                  for(Prefab Elevator : elevators) {
                  int getElevatorID = Elevator.getID();
                   
             if (getElevatorID == StoredElevatorID){ 
                 
                 
                 
                 try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                   int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   }
                 
                 
                  // Close doors
                  Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                  Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1);
                 
                   // play door sound loop   
                   SoundAsset Doorsoundfile = (SoundAsset)player.getAttribute("Doorsoundfile");
                   for(Player AllPlayers : Server.getAllPlayers()){
                       Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     } 
                 
               
                   // wait for doors to close
                   Timer timer10sec = new Timer(0.8F, 0.0F, 0, null);
                   Runnable timerTask10sec = () -> {
                   Elevator.moveToLocalPosition(GBlockposX, GBlockposY, GBlockposZ, 8);         
                   };
                   timer10sec.setTask(timerTask10sec);
                   timer10sec.start();
                   
                   
                  
                   
                   
                   Timer timersec = new Timer(1.0F, 0.0F, 20, null);
                   Runnable timerTasksec = () -> {
                       
                    Vector3f CarrentPosition = Elevator.getLocalPosition(); 
                   
                   
                   
                   
                   
                   Vector3f playPosition = player.getPosition();
                   float playerintPosition = (float) playPosition.y;
                   float playerPosition  = playerintPosition;
                           
                   Vector3f MoveToPosition = new Vector3f(GBlockposX, GBlockposY, GBlockposZ);
            //       player.sendTextMessage("CarrentPosition="+CarrentPosition+" move too="+MoveToPosition);
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                  int ElevatorDoorsID = ElevatorDoors9.getID();    
                  int  StoredElevatorDoorsID = (int)player.getAttribute("StoredElevatorDoorsID");
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                  SoundAsset SoundLoop = (SoundAsset)player.getAttribute("SoundLoop");
                  for(Player AllPlayers : Server.getAllPlayers()){
                  AllPlayers.playSound(SoundLoop, false, 1f, 1f, 0f, 100f, ElevatorDoors9.getLocalPosition());   
                  }
                  
                  }}
                   
                   if(CarrentPosition.equals(MoveToPosition)){
                    //   player.playSound(SoundLoop).stop(true);
                       player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                   
                     
                   
                     Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                     Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2);
                     
                     // play door sound loop   
                      for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Doorsoundfile, false, 1f, 1f, 0f, 15f, CarrentPosition);      
                     }
                     
                      try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorFloorsAmount` = '9' AND `ElevatorID` = '"+ElevatorID+"'")) {
                              
                   if (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = result.getInt("ElevatorDoorsID");    
                   
                  for(Prefab ElevatorDoors9 : elevatorDoors9) {
                  int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                //  player.sendTextMessage("level 9 doors close");
                 
                  
                  
                  ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                  ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 2);
       
                  }}  
                       
                       
                       
                   }
                   }   catch (SQLException ex) {
                           Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
                       }
                   
                   timersec.kill();
                   }    
                   };
                   timersec.setTask(timerTasksec);
                   timersec.start();
                   
                    
                   
                   
                   
                   
                   
             
              }
            } 
          }
        }
        
      
            player.setMouseCursorVisible(false);
            ElevatorMainPanel.setVisible(false);
      
  }
  
  
      
  }
  
  
  
    @EventMethod
    public void onPlayerMouseButtonEvent(PlayerMouseButtonEvent event ) throws SQLException {
    Player player = event.getPlayer();
    String PlayerUID = player.getUID();
    MouseButton clicked = event.getButton();
 
    
                   // Place Elevator
                   if (event.isPressed() && event.getButton() == MouseButton.Right) {
                   if (Boolean.TRUE.equals(player.getAttribute("PlacingElevator"))) {
    
    
                    
                    int PrefabID = (int)player.getAttribute("PrefabID");    
                    
                    for(Iterator<Prefab> RemoveElevator = PreviewElevator.iterator();
                                   RemoveElevator.hasNext();){            
                    Prefab Previewelevator = RemoveElevator.next(); 
                    if(Previewelevator.getID() == PrefabID){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(Previewelevator);
                    }    
                    player.setAttribute("PlacingElevator", false);   
                    RemoveElevator.remove(); 
                    player.sendYellMessage("Elevator Placing Ended", 2, true);
                    
                    }
                    }         
                       
             Prefab PreviewedPrefab = (Prefab) player.getAttribute("PreviewedPrefab");
                    
             Vector3f playerPos = PreviewedPrefab.getLocalPosition();
          
              float ElevatorX = (float)playerPos.x;
              float ElevatorY = (float)playerPos.y;
              float ElevatorZ = (float)playerPos.z;  
              Vector3f Blockpos = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
              int Chunkpos = 0;        
            
              
              
            float rotation = player.getRotation().getYaw()+180;
                        
           //Load the asset bundle from plugin folder first the elevator
            AssetBundle bundle = AssetBundle.loadFromFile(getPath() + "/assets/elevator.bundle");
            
            //Load the asset bundle from plugin folder first the elevator doors
            AssetBundle Doorbundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
            
            
            
            //Load the prefab asset from asset bundle the elevator
            PrefabAsset ElevatorAsset = PrefabAsset.loadFromAssetBundle(bundle, "Elevator.prefab");
            
            //Load the prefab asset from asset bundle the outer doors
            PrefabAsset ElevatorDoorsAsset = PrefabAsset.loadFromAssetBundle(Doorbundle, "ElevatorDoors.prefab");
            
            
            //Create the elevator "Prefab" game object
            Prefab Elevator = new Prefab(ElevatorAsset);
            
            
            
            //Create the outer doors "Prefab" game object
            Prefab ElevatorDoors0 = new Prefab(ElevatorDoorsAsset);
            Prefab ElevatorDoors1 = new Prefab(ElevatorDoorsAsset);
            Prefab ElevatorDoors2 = new Prefab(ElevatorDoorsAsset);
            Prefab ElevatorDoors3 = new Prefab(ElevatorDoorsAsset);
            Prefab ElevatorDoors4 = new Prefab(ElevatorDoorsAsset);
            Prefab ElevatorDoors5 = new Prefab(ElevatorDoorsAsset);
            Prefab ElevatorDoors6 = new Prefab(ElevatorDoorsAsset);
            Prefab ElevatorDoors7 = new Prefab(ElevatorDoorsAsset);
            Prefab ElevatorDoors8 = new Prefab(ElevatorDoorsAsset);
            Prefab ElevatorDoors9 = new Prefab(ElevatorDoorsAsset);
            
            
            // get the placed elevators ID
            int ElevatorID = Elevator.getID();
            
            // get the placed elevator outer doors ID
            int ElevatorDoors0ID = ElevatorDoors0.getID();
            int ElevatorDoors1ID = ElevatorDoors1.getID();
            int ElevatorDoors2ID = ElevatorDoors2.getID();
            int ElevatorDoors3ID = ElevatorDoors3.getID();
            int ElevatorDoors4ID = ElevatorDoors4.getID();
            int ElevatorDoors5ID = ElevatorDoors5.getID();
            int ElevatorDoors6ID = ElevatorDoors6.getID();
            int ElevatorDoors7ID = ElevatorDoors7.getID();
            int ElevatorDoors8ID = ElevatorDoors8.getID();
            int ElevatorDoors9ID = ElevatorDoors9.getID();
            
            
            //Set a layer (determines how the player collides with it)
            Elevator.setLayer(Layer.OBJECT);
            
            //Set a layer (determines how the player collides with it)
            ElevatorDoors0.setLayer(Layer.OBJECT);
            ElevatorDoors1.setLayer(Layer.OBJECT);
            ElevatorDoors2.setLayer(Layer.OBJECT);
            ElevatorDoors3.setLayer(Layer.OBJECT);
            ElevatorDoors4.setLayer(Layer.OBJECT);
            ElevatorDoors5.setLayer(Layer.OBJECT);
            ElevatorDoors6.setLayer(Layer.OBJECT);
            ElevatorDoors7.setLayer(Layer.OBJECT);
            ElevatorDoors8.setLayer(Layer.OBJECT);
            ElevatorDoors9.setLayer(Layer.OBJECT);
     
            
            
            // set outer door Position
           ElevatorDoors0.setLocalPosition(ElevatorX, ElevatorY, ElevatorZ);
           ElevatorDoors1.setLocalPosition(ElevatorX, ElevatorY+8, ElevatorZ);
           ElevatorDoors2.setLocalPosition(ElevatorX, ElevatorY+16, ElevatorZ);
           ElevatorDoors3.setLocalPosition(ElevatorX, ElevatorY+24, ElevatorZ);
           ElevatorDoors4.setLocalPosition(ElevatorX, ElevatorY+32, ElevatorZ);
           ElevatorDoors5.setLocalPosition(ElevatorX, ElevatorY+40, ElevatorZ);
           ElevatorDoors6.setLocalPosition(ElevatorX, ElevatorY+48, ElevatorZ);
           ElevatorDoors7.setLocalPosition(ElevatorX, ElevatorY+56, ElevatorZ);
           ElevatorDoors8.setLocalPosition(ElevatorX, ElevatorY+64, ElevatorZ);
           ElevatorDoors9.setLocalPosition(ElevatorX, ElevatorY+72, ElevatorZ);
           
           
           
           ElevatorDoors0.setLocalRotation(0,player.getRotation().getYaw()+180,0);
           ElevatorDoors1.setLocalRotation(0,player.getRotation().getYaw()+180,0);
           ElevatorDoors2.setLocalRotation(0,player.getRotation().getYaw()+180,0);
           ElevatorDoors3.setLocalRotation(0,player.getRotation().getYaw()+180,0);
           ElevatorDoors4.setLocalRotation(0,player.getRotation().getYaw()+180,0);
           ElevatorDoors5.setLocalRotation(0,player.getRotation().getYaw()+180,0);
           ElevatorDoors6.setLocalRotation(0,player.getRotation().getYaw()+180,0);
           ElevatorDoors7.setLocalRotation(0,player.getRotation().getYaw()+180,0);
           ElevatorDoors8.setLocalRotation(0,player.getRotation().getYaw()+180,0);
           ElevatorDoors9.setLocalRotation(0,player.getRotation().getYaw()+180,0);
           
           
            //Set a position for it - spawn at player position
             //      prefab.setLocalPosition(gotCollisionPoint);
            
             //      prefab.setLocalRotation(0, player.getRotation().getYaw()+180, 0);
           
           
           
           // set elevator Position
          
             Elevator.setLocalPosition(ElevatorX, ElevatorY, ElevatorZ);
             Elevator.setLocalRotation(0, player.getRotation().getYaw()+180, 0);
          
           
           
           
           
           elevatorDoors0.add(ElevatorDoors0);
           elevatorDoors1.add(ElevatorDoors1);
           elevatorDoors2.add(ElevatorDoors2);
           elevatorDoors3.add(ElevatorDoors3);
           elevatorDoors4.add(ElevatorDoors4);
           elevatorDoors5.add(ElevatorDoors5);
           elevatorDoors6.add(ElevatorDoors6);
           elevatorDoors7.add(ElevatorDoors7);
           elevatorDoors8.add(ElevatorDoors8);
           elevatorDoors9.add(ElevatorDoors9);
           elevators.add(Elevator);
           
           
           
            for(Player AllPlayers : Server.getAllPlayers()){        
            //add the elevator
            AllPlayers.addGameObject(ElevatorDoors0); 
            AllPlayers.addGameObject(ElevatorDoors1);
            AllPlayers.addGameObject(ElevatorDoors2);
            AllPlayers.addGameObject(ElevatorDoors3);
            AllPlayers.addGameObject(ElevatorDoors4);
            AllPlayers.addGameObject(ElevatorDoors5);
            AllPlayers.addGameObject(ElevatorDoors6);
            AllPlayers.addGameObject(ElevatorDoors7);
            AllPlayers.addGameObject(ElevatorDoors8);
            AllPlayers.addGameObject(ElevatorDoors9);
            AllPlayers.addGameObject(Elevator);  
            }
           
            
            
               // setup for each floor for the elevators Position, also used for the outer doors
               Vector3f Level0 = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
               Vector3f Level1 = new Vector3f(ElevatorX, ElevatorY +8, ElevatorZ);
               Vector3f Level2 = new Vector3f(ElevatorX, ElevatorY +16, ElevatorZ);
               Vector3f Level3 = new Vector3f(ElevatorX, ElevatorY +24, ElevatorZ);
               Vector3f Level4 = new Vector3f(ElevatorX, ElevatorY +32, ElevatorZ);
               Vector3f Level5 = new Vector3f(ElevatorX, ElevatorY +40, ElevatorZ);
               Vector3f Level6 = new Vector3f(ElevatorX, ElevatorY +48, ElevatorZ);
               Vector3f Level7 = new Vector3f(ElevatorX, ElevatorY +56, ElevatorZ);
               Vector3f Level8 = new Vector3f(ElevatorX, ElevatorY +64, ElevatorZ);
               Vector3f Level9 = new Vector3f(ElevatorX, ElevatorY +72, ElevatorZ);
               
               
     
               
            // save elevator Positions
            database.executeUpdate("INSERT INTO `Elevators` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, Chunkpos, ElevatorID, ElevatorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','0','Elevator','"+Level0+"','"+Chunkpos+"','"+ElevatorID+"','"+rotation+"');");             
            database.executeUpdate("INSERT INTO `Elevators` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, Chunkpos, ElevatorID, ElevatorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','1','Elevator','"+Level1+"','"+Chunkpos+"','"+ElevatorID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `Elevators` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, Chunkpos, ElevatorID, ElevatorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','2','Elevator','"+Level2+"','"+Chunkpos+"','"+ElevatorID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `Elevators` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, Chunkpos, ElevatorID, ElevatorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','3','Elevator','"+Level3+"','"+Chunkpos+"','"+ElevatorID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `Elevators` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, Chunkpos, ElevatorID, ElevatorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','4','Elevator','"+Level4+"','"+Chunkpos+"','"+ElevatorID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `Elevators` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, Chunkpos, ElevatorID, ElevatorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','5','Elevator','"+Level5+"','"+Chunkpos+"','"+ElevatorID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `Elevators` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, Chunkpos, ElevatorID, ElevatorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','6','Elevator','"+Level6+"','"+Chunkpos+"','"+ElevatorID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `Elevators` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, Chunkpos, ElevatorID, ElevatorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','7','Elevator','"+Level7+"','"+Chunkpos+"','"+ElevatorID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `Elevators` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, Chunkpos, ElevatorID, ElevatorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','8','Elevator','"+Level8+"','"+Chunkpos+"','"+ElevatorID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `Elevators` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, Chunkpos, ElevatorID, ElevatorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','9','Elevator','"+Level9+"','"+Chunkpos+"','"+ElevatorID+"','"+rotation+"');");
          
            
            
            // save elevator outer doors Positions
            database.executeUpdate("INSERT INTO `ElevatorOuterDoors` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, ElevatorID, ElevatorDoorsID, ElevatorOuterDoorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','0','ElevatorDoor0','"+Level0+"','"+ElevatorID+"','"+ElevatorDoors0ID+"','"+rotation+"');");              
            database.executeUpdate("INSERT INTO `ElevatorOuterDoors` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, ElevatorID, ElevatorDoorsID, ElevatorOuterDoorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','1','ElevatorDoor1','"+Level1+"','"+ElevatorID+"','"+ElevatorDoors1ID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `ElevatorOuterDoors` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, ElevatorID, ElevatorDoorsID, ElevatorOuterDoorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','2','ElevatorDoor2','"+Level2+"','"+ElevatorID+"','"+ElevatorDoors2ID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `ElevatorOuterDoors` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, ElevatorID, ElevatorDoorsID, ElevatorOuterDoorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','3','ElevatorDoor3','"+Level3+"','"+ElevatorID+"','"+ElevatorDoors3ID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `ElevatorOuterDoors` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, ElevatorID, ElevatorDoorsID, ElevatorOuterDoorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','4','ElevatorDoor4','"+Level4+"','"+ElevatorID+"','"+ElevatorDoors4ID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `ElevatorOuterDoors` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, ElevatorID, ElevatorDoorsID, ElevatorOuterDoorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','5','ElevatorDoor5','"+Level5+"','"+ElevatorID+"','"+ElevatorDoors5ID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `ElevatorOuterDoors` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, ElevatorID, ElevatorDoorsID, ElevatorOuterDoorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','6','ElevatorDoor6','"+Level6+"','"+ElevatorID+"','"+ElevatorDoors6ID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `ElevatorOuterDoors` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, ElevatorID, ElevatorDoorsID, ElevatorOuterDoorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','7','ElevatorDoor7','"+Level7+"','"+ElevatorID+"','"+ElevatorDoors7ID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `ElevatorOuterDoors` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, ElevatorID, ElevatorDoorsID, ElevatorOuterDoorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','8','ElevatorDoor8','"+Level8+"','"+ElevatorID+"','"+ElevatorDoors8ID+"','"+rotation+"');");
            database.executeUpdate("INSERT INTO `ElevatorOuterDoors` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight , Blockpos, ElevatorID, ElevatorDoorsID, ElevatorOuterDoorRotation) VALUES ('"+player.getPosition()+"','"+ElevatorX+"','"+ElevatorY+"','"+ElevatorZ+"','9','ElevatorDoor9','"+Level9+"','"+ElevatorID+"','"+ElevatorDoors9ID+"','"+rotation+"');");
          
            
               
                       
                       
            player.setAttribute("PlacingElevator", false);           
            }
            }
         //   } 
    
    
    
    }
  
  
  
  
 
  
  
    
             @EventMethod
public void onPlayerKeyInputEvent(PlayerKeyEvent event){
    
    
          Player player = event.getPlayer(); 
          String CallElevator = (String) player.getAttribute("CallElevator");
          // int getElevatorID = 0;
          // player.setAttribute("getElevatorID", getElevatorID);
          Object DoorState = player.getAttribute("DoorState");
          
          
          // get the players Position
          Vector3f playerPos = player.getPosition();
          
          float ElevatorX = (float)playerPos.x;
          float ElevatorY = (float)playerPos.y;
          float ElevatorZ = (float)playerPos.z;  
          Vector3f Blockpos = new Vector3f(ElevatorX, ElevatorY, ElevatorZ);
          
          int Chunkpos = 0;
          
          
          // player.setAttribute("CallElevator");
        //  prefab.setLocalRotation(0, player.getRotation().getYaw()+180, 0);
          
          
           
          
           
         
        
        // mouse
        
                 
             // PlaceElevator
             
             if (event.isPressed() && event.getKey() == Key.E && player.isKeyPressed(Key.LeftShift)) {     
             player.hideInventory();
             
            
             Timer raycastTimer = new Timer(-1, 0.0F, -1, null);
             Runnable raycastUpDate = () -> {      
             
             //get collision bitmask: terrain, constructions and objects  Layer.DEFAULT, 
	     int layerMask = Layer.getBitmask( Layer.CONSTRUCTION,    Layer.OBJECT,   Layer.TERRAIN,    Layer.WORLD );                                                                                                
	
	     //perform raycast
	     player.raycast(layerMask, (RaycastResult result) -> {
	    
	     if(result != null){
	        
                long getObjectID = result.getObjectGlobalID();
                boolean Trigger = result.isTrigger();
                int Layer = result.getLayer();
                int InstanceID = result.getInstanceID();
                float Distance = result.getDistance();
                
                player.setAttribute("HardwareID", getObjectID);
                Vector3f CollisionPoint = result.getCollisionPoint();
                player.setAttribute("CollisionPoint", CollisionPoint);
                
                // PreviewBattery
                for(Prefab Preview : PreviewElevator) {
                if(Preview != null){
                int PrefabID = (int)player.getAttribute("PrefabID");     
                if(Preview.getID() == PrefabID){
                Preview.moveToLocalPosition(CollisionPoint.x, CollisionPoint.y, CollisionPoint.z, -1);
                Preview.setLocalRotation(0,player.getRotation().getYaw()+180,0);
                //   player.sendTextMessage("prefabID"+Preview.getID());
                player.setAttribute("PreviewedPrefab", Preview);
                }
                } 
                }
	        }
	        });
    
               };        
               raycastTimer.setTask(raycastUpDate);
               raycastTimer.start();
               player.setAttribute("raycastTimer", raycastTimer);   
            
            
               //Load the asset bundle from plugin folder first
               AssetBundle bundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
 
               //Load the prefab asset from asset bundle
               PrefabAsset ElevatorAsset = PrefabAsset.loadFromAssetBundle(bundle, "ElevatorDoors.prefab");
 
               //Create a "Prefab" game object
               Prefab prefab = new Prefab(ElevatorAsset);
 
               //Set a layer (determines how the player collides with it)
               prefab.setLayer(Layer.IGNORE_RAYCAST);
 
               //Set a position for it - spawn at player position
               prefab.setLocalPosition(player.getPosition());
 
               //Register to player (so it shows up for this player)
            
               PreviewElevator.add(prefab);
               player.setAttribute("PrefabID", prefab.getID());
            
              // player.sendTextMessage("place prefabID"+prefab.getID());
            
               for(Player AllPlayers : Server.getAllPlayers()){
               AllPlayers.addGameObject(prefab);
               }
            
              
               // remove preview prefab if not placed after 2min
               Timer RemovePreviewTimer = new Timer(1, 120, 0, null);
               Runnable RemovePreviewUpDate = () -> {  
            
                   for(Iterator<Prefab> RemoveElevator = PreviewElevator.iterator();
                                   RemoveElevator.hasNext();){            
                    Prefab Previewelevator = RemoveElevator.next(); 
                    if(Previewelevator.getID() == prefab.getID()){
                        
                    for(Player AllPlayers : Server.getAllPlayers()){
                    AllPlayers.removeGameObject(Previewelevator);
                    }    
                    player.setAttribute("PlacingElevator", false);   
                    RemoveElevator.remove(); 
                    player.sendYellMessage("Elevator Placing Ended", 2, true);
                    
                    }
                    }
               
               };        
               RemovePreviewTimer.setTask(RemovePreviewUpDate);
               RemovePreviewTimer.start();
               player.setAttribute("RemovePreviewTimer", RemovePreviewTimer);  
            
               player.setAttribute("PlacingElevator", true);
               }
             
        
        
        
        
            
            
            if (event.isPressed() && event.getKey() == Key.F) {
            
            //get collision bitmask: terrain, constructions and objects  Layer.DEFAULT, 
	    int layerMask = Layer.getBitmask( Layer.OBJECT); 
            
            player.raycast(layerMask, (RaycastResult LayerResult) -> {
	    if(LayerResult != null){
                
            
            
         //   player.sendTextMessage("layer mask id = "+LayerResult.getInstanceID()+" global id ="+LayerResult.getObjectGlobalID());
            
                for(Prefab Elevator : elevators) {
                  int ElevatorID = Elevator.getID();
                   
                  Vector3f LocalPosition = Elevator.getLocalPosition();
                  
                  int LocalPositionX = (int) LocalPosition.x;
                  int LocalPositionY = (int) LocalPosition.y;
                  int LocalPositionZ = (int) LocalPosition.z;
                  float LocalPositionXFloat = LocalPositionX;
                  float LocalPositionYFloat = LocalPositionY;
                  float LocalPositionZFloat = LocalPositionZ;
                  
                  Vector3f LocalPositionfloat = new Vector3f().addLocal(LocalPositionXFloat, LocalPositionYFloat, LocalPositionZFloat);
                  
                  if (ElevatorID == LayerResult.getObjectGlobalID()){ 
                 
                 // let the player use the mouse
                 player.setMouseCursorVisible(true);
                 player.setAttribute("ElevatorID", ElevatorID);
                 UIElement ElevatorMainPanel = (UIElement) player.getAttribute("ElevatorMainPanel");
                 ElevatorMainPanel.setVisible(true);
                 
              }
            } 
            
              
            
       //Add all known elevators to the player
       for(Prefab ElevatorDoors0 : elevatorDoors0) {
       if(LayerResult.getObjectGlobalID() == ElevatorDoors0.getID()){
         player.sendYellMessage("Elevator called", 2, false);    
       try (ResultSet Result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorDoorsID` = '"+ElevatorDoors0.getID()+"' AND `ElevatorFloorHeight` = 'ElevatorDoor0'")) {
       if (Result.next()) {
       
       int getElevatorID = Result.getInt("ElevatorID");
       player.setAttribute("getElevatorID", getElevatorID);
       String getBlockpos = Result.getString("Blockpos");
       player.setAttribute("getBlockpos", getBlockpos);
       
       
             String BlockposGet = Result.getString("Blockpos"); 
              Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
             
             
         //   Vector3i ElevatorPosition = new Vector3i(Utils.ChunkUtils.getGlobalBlockPosition(0, 0, 0, GBlockposX, GBlockposY, GBlockposZ));
            float  ElevatorPositionfloatX = GBlockposX;
            float  ElevatorPositionfloatY = GBlockposY;
            float  ElevatorPositionfloatZ = GBlockposZ;
            Vector3f ElevatorPositionfloat = new Vector3f().addLocal(ElevatorPositionfloatX, ElevatorPositionfloatY, ElevatorPositionfloatZ);
       
       
            for(Prefab Elevator : elevators) {
            if(Elevator.getID() == getElevatorID){
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);    
                
                
                try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
       //            for(Prefab ElevatorDoors0 : elevatorDoors0) {
       //            int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
       //           if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
        //           ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
        //           ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
       //           }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                   int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               } 
            
            
            player.setAttribute("CallElevator","Called");    
            Vector3f EPosition = Elevator.getLocalPosition();
            float distancebetween = ElevatorY - EPosition.y;
            
            
            Vector3f targetPosition = Elevator.getLocalPosition().add(0f, distancebetween, 0f);
                 float distance = Elevator.getLocalPosition().distance(targetPosition);
                 float speed = 8f;
                 
           executeDelayed(distance / speed +2, () -> {
               
             player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, Elevator.getLocalPosition());      
                     }    
              
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2); 
           
           ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 2);
           
           });
           executeDelayed(1, () -> {
           Elevator.moveToLocalPosition(ElevatorPositionfloat, 8);
           });
           executeDelayed(distance / speed +5, () -> {
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
           
           
                try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
       //            for(Prefab ElevatorDoors0 : elevatorDoors0) {
       //            int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
       //           if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
        //           ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
        //           ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
       //           }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                   int ElevatorDoorsID = ElevatorDoors1.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
           
           
           
           
           
           
           
           
           });
           
           
            }
            } 
       
       
       
       
   //    player.sendTextMessage("Match ="+getElevatorID+" and "+getBlockpos);
       }else{player.sendTextMessage("No Match");}
       }catch (SQLException ex) {    
       Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
       }    
       }    
       }
      
       
       
       
       
       
       
       
       
        //Add all known elevators to the player
       for(Prefab ElevatorDoors1 : elevatorDoors1) {
       if(LayerResult.getObjectGlobalID() == ElevatorDoors1.getID()){
       player.sendYellMessage("Elevator called", 2, false);    
       try (ResultSet Result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorDoorsID` = '"+ElevatorDoors1.getID()+"' AND `ElevatorFloorHeight` = 'ElevatorDoor1'")) {
       if (Result.next()) {
       
       int getElevatorID = Result.getInt("ElevatorID");
       player.setAttribute("getElevatorID", getElevatorID);
       String getBlockpos = Result.getString("Blockpos");
       player.setAttribute("getBlockpos", getBlockpos);
       
       
        String BlockposGet = Result.getString("Blockpos"); 
              Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
             
             
         //   Vector3i ElevatorPosition = new Vector3i(Utils.ChunkUtils.getGlobalBlockPosition(0, 0, 0, GBlockposX, GBlockposY, GBlockposZ));
            float  ElevatorPositionfloatX = GBlockposX;
            float  ElevatorPositionfloatY = GBlockposY;
            float  ElevatorPositionfloatZ = GBlockposZ;
            Vector3f ElevatorPositionfloat = new Vector3f().addLocal(ElevatorPositionfloatX, ElevatorPositionfloatY, ElevatorPositionfloatZ);
       
       
            for(Prefab Elevator : elevators) {
            if(Elevator.getID() == getElevatorID){
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);    
                
                
             try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
     //              for(Prefab ElevatorDoors1 : elevatorDoors1) {
     //             int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
      //            if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
      //             ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
      //             ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
      //            }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               } 
            
            
            player.setAttribute("CallElevator","Called");    
            Vector3f EPosition = Elevator.getLocalPosition();
            float distancebetween = ElevatorY - EPosition.y;
            
            
            Vector3f targetPosition = Elevator.getLocalPosition().add(0f, distancebetween, 0f);
                 float distance = Elevator.getLocalPosition().distance(targetPosition);
                 float speed = 8f;
                 
           executeDelayed(distance / speed +2, () -> {
               
           player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, Elevator.getLocalPosition());      
                     }    
                
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2); 
           
           ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 2);
                
           
           });
           executeDelayed(1, () -> {
           Elevator.moveToLocalPosition(ElevatorPositionfloat, 8);
           });
          
           executeDelayed(distance / speed +5, () -> {
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
           
           
            try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
     //              for(Prefab ElevatorDoors1 : elevatorDoors1) {
     //             int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
      //            if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
      //             ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
      //             ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
      //            }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
           
           
           
           
           
           
           });
           
           
           
            }
            } 
       
       
       
       
    //  player.sendTextMessage("Match ="+getElevatorID+" and "+getBlockpos);
       }
       }catch (SQLException ex) {    
       Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
       }    
       } 
      
       
       }
       
       //Add all known elevators to the player
       for(Prefab ElevatorDoors2 : elevatorDoors2) {
       if(LayerResult.getObjectGlobalID() == ElevatorDoors2.getID()){
       player.sendYellMessage("Elevator called", 2, false);    
       try (ResultSet Result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorDoorsID` = '"+ElevatorDoors2.getID()+"' AND `ElevatorFloorHeight` = 'ElevatorDoor2'")) {
       if (Result.next()) {
       
       int getElevatorID = Result.getInt("ElevatorID");
       player.setAttribute("getElevatorID", getElevatorID);
       String getBlockpos = Result.getString("Blockpos");
       player.setAttribute("getBlockpos", getBlockpos);
       
       
        String BlockposGet = Result.getString("Blockpos"); 
              Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
             
             
         //   Vector3i ElevatorPosition = new Vector3i(Utils.ChunkUtils.getGlobalBlockPosition(0, 0, 0, GBlockposX, GBlockposY, GBlockposZ));
            float  ElevatorPositionfloatX = GBlockposX;
            float  ElevatorPositionfloatY = GBlockposY;
            float  ElevatorPositionfloatZ = GBlockposZ;
            Vector3f ElevatorPositionfloat = new Vector3f().addLocal(ElevatorPositionfloatX, ElevatorPositionfloatY, ElevatorPositionfloatZ);
       
       
            for(Prefab Elevator : elevators) {
            if(Elevator.getID() == getElevatorID){
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);    
                
                
             try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
       //            for(Prefab ElevatorDoors2 : elevatorDoors2) {
       //            int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
      //            if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
      //             ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
      //             ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
       //           }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               } 
            
            
            player.setAttribute("CallElevator","Called");    
            Vector3f EPosition = Elevator.getLocalPosition();
            float distancebetween = ElevatorY - EPosition.y;
            
            
            Vector3f targetPosition = Elevator.getLocalPosition().add(0f, distancebetween, 0f);
                 float distance = Elevator.getLocalPosition().distance(targetPosition);
                 float speed = 8f;
                 
           executeDelayed(distance / speed+2, () -> {
               
               player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, Elevator.getLocalPosition());      
                     }
                
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2); 
           
           ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 2);
                
           
           });
           executeDelayed(1, () -> {
           Elevator.moveToLocalPosition(ElevatorPositionfloat, 8);
           });
          
           executeDelayed(distance / speed +5, () -> {
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
           
           
            try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
       //            for(Prefab ElevatorDoors2 : elevatorDoors2) {
       //            int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
      //            if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
      //             ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
      //             ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
       //           }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
           
           
           
           
           
           
           });
           
           
           
            }
            } 
       
       
       
   //    player.sendTextMessage("Match ="+getElevatorID+" and "+getBlockpos);
       }
       }catch (SQLException ex) {    
       Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
       }    
       }  
       
       
       }
       
       
       //Add all known elevators to the player
       for(Prefab ElevatorDoors3 : elevatorDoors3) {
       if(LayerResult.getObjectGlobalID() == ElevatorDoors3.getID()){
       player.sendYellMessage("Elevator called", 2, false);    
       try (ResultSet Result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorDoorsID` = '"+ElevatorDoors3.getID()+"' AND `ElevatorFloorHeight` = 'ElevatorDoor3'")) {
       if (Result.next()) {
       
       int getElevatorID = Result.getInt("ElevatorID");
       player.setAttribute("getElevatorID", getElevatorID);
       String getBlockpos = Result.getString("Blockpos");
       player.setAttribute("getBlockpos", getBlockpos);
       
       
        String BlockposGet = Result.getString("Blockpos"); 
              Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
             
             
         //   Vector3i ElevatorPosition = new Vector3i(Utils.ChunkUtils.getGlobalBlockPosition(0, 0, 0, GBlockposX, GBlockposY, GBlockposZ));
            float  ElevatorPositionfloatX = GBlockposX;
            float  ElevatorPositionfloatY = GBlockposY;
            float  ElevatorPositionfloatZ = GBlockposZ;
            Vector3f ElevatorPositionfloat = new Vector3f().addLocal(ElevatorPositionfloatX, ElevatorPositionfloatY, ElevatorPositionfloatZ);
       
       
           for(Prefab Elevator : elevators) {
            if(Elevator.getID() == getElevatorID){
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);    
                
                
               try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
      //             for(Prefab ElevatorDoors3 : elevatorDoors3) {
      //             int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
       //           if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
       //            ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
       //            ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
       //           }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
            
            
            player.setAttribute("CallElevator","Called");    
            Vector3f EPosition = Elevator.getLocalPosition();
            float distancebetween = ElevatorY - EPosition.y;
            
            
            Vector3f targetPosition = Elevator.getLocalPosition().add(0f, distancebetween, 0f);
                 float distance = Elevator.getLocalPosition().distance(targetPosition);
                 float speed = 8f;
                 
           executeDelayed(distance / speed+2, () -> {
               
           player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, Elevator.getLocalPosition());      
                     }    
               
                
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2); 
           
           ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 2);
                
           
           });
           executeDelayed(1, () -> {
           Elevator.moveToLocalPosition(ElevatorPositionfloat, 8);
           });
          
           executeDelayed(distance / speed +5, () -> {
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
           
           
            try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
      //             for(Prefab ElevatorDoors3 : elevatorDoors3) {
      //             int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
       //           if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
       //            ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
       //            ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
       //           }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
           
           
           
           
           
           
           });
           
           
           
            }
            } 
       
       
       
       
    //   player.sendTextMessage("Match ="+getElevatorID+" and "+getBlockpos);
       }
       }catch (SQLException ex) {    
       Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
       }    
       } 
       
       
       }
       
       
       
        //Add all known elevators to the player
       for(Prefab ElevatorDoors4 : elevatorDoors4) {
       if(LayerResult.getObjectGlobalID() == ElevatorDoors4.getID()){
       player.sendYellMessage("Elevator called", 2, false);    
       try (ResultSet Result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorDoorsID` = '"+ElevatorDoors4.getID()+"' AND `ElevatorFloorHeight` = 'ElevatorDoor4'")) {
       if (Result.next()) {
       
       int getElevatorID = Result.getInt("ElevatorID");
       player.setAttribute("getElevatorID", getElevatorID);
       String getBlockpos = Result.getString("Blockpos");
       player.setAttribute("getBlockpos", getBlockpos);
       
       
        String BlockposGet = Result.getString("Blockpos"); 
              Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
             
             
         //   Vector3i ElevatorPosition = new Vector3i(Utils.ChunkUtils.getGlobalBlockPosition(0, 0, 0, GBlockposX, GBlockposY, GBlockposZ));
            float  ElevatorPositionfloatX = GBlockposX;
            float  ElevatorPositionfloatY = GBlockposY;
            float  ElevatorPositionfloatZ = GBlockposZ;
            Vector3f ElevatorPositionfloat = new Vector3f().addLocal(ElevatorPositionfloatX, ElevatorPositionfloatY, ElevatorPositionfloatZ);
       
       
            for(Prefab Elevator : elevators) {
            if(Elevator.getID() == getElevatorID){
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);    
                
                
             try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
        //           for(Prefab ElevatorDoors4 : elevatorDoors4) {
        //           int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
        //          if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
        //           ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
        //           ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
         //         }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               } 
           
           
            
            
            player.setAttribute("CallElevator","Called");    
            Vector3f EPosition = Elevator.getLocalPosition();
            float distancebetween = ElevatorY - EPosition.y;
            
            
            Vector3f targetPosition = Elevator.getLocalPosition().add(0f, distancebetween, 0f);
                 float distance = Elevator.getLocalPosition().distance(targetPosition);
                 float speed = 8f;
                 
           executeDelayed(distance / speed+2, () -> {
               
               player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, Elevator.getLocalPosition());      
                     }
                
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2); 
           
           ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 2);
                
           
           });
           executeDelayed(1, () -> {
           Elevator.moveToLocalPosition(ElevatorPositionfloat, 8);
           });
          
           executeDelayed(distance / speed +5, () -> {
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
           
           
            try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
        //           for(Prefab ElevatorDoors4 : elevatorDoors4) {
        //           int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
        //          if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
        //           ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
        //           ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
         //         }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
           
           
           
           
           
           
           });
           
           
           
            }
            } 
       
       
       
    //   player.sendTextMessage("Match ="+getElevatorID+" and "+getBlockpos);
       }
       }catch (SQLException ex) {    
       Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
       }    
       }  
       
       
       }
       
       //Add all known elevators to the player
       for(Prefab ElevatorDoors5 : elevatorDoors5) {
       if(LayerResult.getObjectGlobalID() == ElevatorDoors5.getID()){
       player.sendYellMessage("Elevator called", 2, false);   
       try (ResultSet Result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorDoorsID` = '"+ElevatorDoors5.getID()+"' AND `ElevatorFloorHeight` = 'ElevatorDoor5'")) {
       if (Result.next()) {
       
       int getElevatorID = Result.getInt("ElevatorID");
       player.setAttribute("getElevatorID", getElevatorID);
       String getBlockpos = Result.getString("Blockpos");
       player.setAttribute("getBlockpos", getBlockpos);
       
       
        String BlockposGet = Result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
             
             
         //   Vector3i ElevatorPosition = new Vector3i(Utils.ChunkUtils.getGlobalBlockPosition(0, 0, 0, GBlockposX, GBlockposY, GBlockposZ));
            float  ElevatorPositionfloatX = GBlockposX;
            float  ElevatorPositionfloatY = GBlockposY;
            float  ElevatorPositionfloatZ = GBlockposZ;
            Vector3f ElevatorPositionfloat = new Vector3f().addLocal(ElevatorPositionfloatX, ElevatorPositionfloatY, ElevatorPositionfloatZ);
       
       
            for(Prefab Elevator : elevators) {
            if(Elevator.getID() == getElevatorID){
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);    
                
           
           
            try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
       //            for(Prefab ElevatorDoors5 : elevatorDoors5) {
       //            int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
       //           if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
       //            ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
       //            ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
       //           }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
            
            
            
            player.setAttribute("CallElevator","Called");    
            Vector3f EPosition = Elevator.getLocalPosition();
            float distancebetween = ElevatorY - EPosition.y;
            
            
            Vector3f targetPosition = Elevator.getLocalPosition().add(0f, distancebetween, 0f);
                 float distance = Elevator.getLocalPosition().distance(targetPosition);
                 float speed = 8f;
                 
           executeDelayed(distance / speed+2, () -> {
               
               player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, Elevator.getLocalPosition());      
                     }
                
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2); 
           
           ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 2);
                
           
           });
           executeDelayed(1, () -> {
           Elevator.moveToLocalPosition(ElevatorPositionfloat, 8);
           });
          
           executeDelayed(distance / speed +5, () -> {
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
           
           
            try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
       //            for(Prefab ElevatorDoors5 : elevatorDoors5) {
       //            int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
       //           if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
       //            ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
       //            ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
       //           }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
           
           
           
           
           
           
           });
           
           
           
            }
            } 
        
       
       
    //   player.sendTextMessage("Match ="+getElevatorID+" and "+getBlockpos);
       }
       }catch (SQLException ex) {    
       Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
       }    
       }      
       
       }
       
       
        //Add all known elevators to the player
       for(Prefab ElevatorDoors6 : elevatorDoors6) {
       if(LayerResult.getObjectGlobalID() == ElevatorDoors6.getID()){
       player.sendYellMessage("Elevator called", 2, false);    
       try (ResultSet Result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorDoorsID` = '"+ElevatorDoors6.getID()+"' AND `ElevatorFloorHeight` = 'ElevatorDoor6'")) {
       if (Result.next()) {
       
       int getElevatorID = Result.getInt("ElevatorID");
       player.setAttribute("getElevatorID", getElevatorID);
       String getBlockpos = Result.getString("Blockpos");
       player.setAttribute("getBlockpos", getBlockpos);
       
       
        String BlockposGet = Result.getString("Blockpos"); 
              Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
             
             
         //   Vector3i ElevatorPosition = new Vector3i(Utils.ChunkUtils.getGlobalBlockPosition(0, 0, 0, GBlockposX, GBlockposY, GBlockposZ));
            float  ElevatorPositionfloatX = GBlockposX;
            float  ElevatorPositionfloatY = GBlockposY;
            float  ElevatorPositionfloatZ = GBlockposZ;
            Vector3f ElevatorPositionfloat = new Vector3f().addLocal(ElevatorPositionfloatX, ElevatorPositionfloatY, ElevatorPositionfloatZ);
       
       
            for(Prefab Elevator : elevators) {
            if(Elevator.getID() == getElevatorID){
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);    
                
           
             try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
       //            for(Prefab ElevatorDoors6 : elevatorDoors6) {
      //             int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
       //           if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
       //            ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
       //            ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
         //         }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
            
            
            
            player.setAttribute("CallElevator","Called");    
            Vector3f EPosition = Elevator.getLocalPosition();
            float distancebetween = ElevatorY - EPosition.y;
            
            
            Vector3f targetPosition = Elevator.getLocalPosition().add(0f, distancebetween, 0f);
                 float distance = Elevator.getLocalPosition().distance(targetPosition);
                 float speed = 8f;
                 
           executeDelayed(distance / speed+2, () -> {
               
               player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, Elevator.getLocalPosition());      
                     }
                
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2); 
           
           ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 2);
                
           
           });
           executeDelayed(1, () -> {
           Elevator.moveToLocalPosition(ElevatorPositionfloat, 8);
           });
          
           executeDelayed(distance / speed +5, () -> {
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
           
           
            try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
       //            for(Prefab ElevatorDoors6 : elevatorDoors6) {
      //             int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
       //           if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
       //            ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
       //            ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
         //         }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
           
           
           
           
           
           
           });
           
           
           
            }
            } 
        
       
       
    //   player.sendTextMessage("Match ="+getElevatorID+" and "+getBlockpos);
       }
       }catch (SQLException ex) {    
       Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
       }    
       } 
       
      
       }
       
       
       //Add all known elevators to the player
       for(Prefab ElevatorDoors7 : elevatorDoors7) {
       if(LayerResult.getObjectGlobalID() == ElevatorDoors7.getID()){
       player.sendYellMessage("Elevator called", 2, false);    
       try (ResultSet Result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorDoorsID` = '"+ElevatorDoors7.getID()+"' AND `ElevatorFloorHeight` = 'ElevatorDoor7'")) {
       if (Result.next()) {
       
       int getElevatorID = Result.getInt("ElevatorID");
       player.setAttribute("getElevatorID", getElevatorID);
       String getBlockpos = Result.getString("Blockpos");
       player.setAttribute("getBlockpos", getBlockpos);
       
       
        String BlockposGet = Result.getString("Blockpos"); 
              Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
             
             
         //   Vector3i ElevatorPosition = new Vector3i(Utils.ChunkUtils.getGlobalBlockPosition(0, 0, 0, GBlockposX, GBlockposY, GBlockposZ));
            float  ElevatorPositionfloatX = GBlockposX;
            float  ElevatorPositionfloatY = GBlockposY;
            float  ElevatorPositionfloatZ = GBlockposZ;
            Vector3f ElevatorPositionfloat = new Vector3f().addLocal(ElevatorPositionfloatX, ElevatorPositionfloatY, ElevatorPositionfloatZ);
       
       
            for(Prefab Elevator : elevators) {
            if(Elevator.getID() == getElevatorID){
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);    
                
            
             try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
       //            for(Prefab ElevatorDoors7 : elevatorDoors7) {
       //            int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
        //          if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
       //            ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
       //            ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
        //          }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
            
            
            
            player.setAttribute("CallElevator","Called");    
            Vector3f EPosition = Elevator.getLocalPosition();
            float distancebetween = ElevatorY - EPosition.y;
            
            
            Vector3f targetPosition = Elevator.getLocalPosition().add(0f, distancebetween, 0f);
                 float distance = Elevator.getLocalPosition().distance(targetPosition);
                 float speed = 8f;
                 
           executeDelayed(distance / speed+2, () -> {
               
               player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, Elevator.getLocalPosition());      
                     }
                
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2); 
           
           ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 2);
                
           
           });
           executeDelayed(1, () -> {
           Elevator.moveToLocalPosition(ElevatorPositionfloat, 8);
           });
          
           executeDelayed(distance / speed +5, () -> {
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
           
           
            try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
       //            for(Prefab ElevatorDoors7 : elevatorDoors7) {
       //            int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
        //          if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
       //            ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
       //            ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
        //          }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
           
           
           
           
           
           
           });
           
           
           
            }
            } 
        
       
       
    //   player.sendTextMessage("Match ="+getElevatorID+" and "+getBlockpos);
       }
       }catch (SQLException ex) {    
       Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
       }    
       }  
      
       
       }
       
       
       
        //Add all known elevators to the player
       for(Prefab ElevatorDoors8 : elevatorDoors8) {
       if(LayerResult.getObjectGlobalID() == ElevatorDoors8.getID()){
       player.sendYellMessage("Elevator called", 2, false);    
       try (ResultSet Result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorDoorsID` = '"+ElevatorDoors8.getID()+"' AND `ElevatorFloorHeight` = 'ElevatorDoor8'")) {
       if (Result.next()) {
       
       int getElevatorID = Result.getInt("ElevatorID");
       player.setAttribute("getElevatorID", getElevatorID);
       String getBlockpos = Result.getString("Blockpos");
       player.setAttribute("getBlockpos", getBlockpos);
       
       
        String BlockposGet = Result.getString("Blockpos"); 
              Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
             
             
         //   Vector3i ElevatorPosition = new Vector3i(Utils.ChunkUtils.getGlobalBlockPosition(0, 0, 0, GBlockposX, GBlockposY, GBlockposZ));
            float  ElevatorPositionfloatX = GBlockposX;
            float  ElevatorPositionfloatY = GBlockposY;
            float  ElevatorPositionfloatZ = GBlockposZ;
            Vector3f ElevatorPositionfloat = new Vector3f().addLocal(ElevatorPositionfloatX, ElevatorPositionfloatY, ElevatorPositionfloatZ);
       
       
            for(Prefab Elevator : elevators) {
            if(Elevator.getID() == getElevatorID){
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);    
                
             
            try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
         //          for(Prefab ElevatorDoors8 : elevatorDoors8) {
         //          int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
         //         if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
         //          ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
         //          ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
         //         }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
           
           
           
            
            
            
            player.setAttribute("CallElevator","Called");    
            Vector3f EPosition = Elevator.getLocalPosition();
            float distancebetween = ElevatorY - EPosition.y;
            
            
            Vector3f targetPosition = Elevator.getLocalPosition().add(0f, distancebetween, 0f);
                 float distance = Elevator.getLocalPosition().distance(targetPosition);
                 float speed = 8f;
                 
           executeDelayed(distance / speed+2, () -> {
               
               player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, Elevator.getLocalPosition());      
                     }
               
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2); 
           
           ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 2);
              
           
           });
           executeDelayed(1, () -> {
           Elevator.moveToLocalPosition(ElevatorPositionfloat, 8);
           });
          
           executeDelayed(distance / speed +5, () -> {
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
           
           
            try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
         //          for(Prefab ElevatorDoors8 : elevatorDoors8) {
         //          int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
         //         if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
         //          ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
         //          ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
         //         }}
                   for(Prefab ElevatorDoors9 : elevatorDoors9) {
                   int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
           
           
           
           
           
           
           });
           
           
           
            }
            } 
       
       
       
    //   player.sendTextMessage("Match ="+getElevatorID+" and "+getBlockpos);
       }
       }catch (SQLException ex) {    
       Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
       }    
       } 
       
       
       }
       
       //Add all known elevators to the player
       for(Prefab ElevatorDoors9 : elevatorDoors9) {
       if(LayerResult.getObjectGlobalID() == ElevatorDoors9.getID()){
       player.sendYellMessage("Elevator called", 2, false);    
       try (ResultSet Result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorDoorsID` = '"+ElevatorDoors9.getID()+"' AND `ElevatorFloorHeight` = 'ElevatorDoor9'")) {
       if (Result.next()) {
       
       int getElevatorID = Result.getInt("ElevatorID");
       player.setAttribute("getElevatorID", getElevatorID);
       String getBlockpos = Result.getString("Blockpos");
       player.setAttribute("getBlockpos", getBlockpos);
       
       
        String BlockposGet = Result.getString("Blockpos"); 
             Vector3f GBlockpos = new Vector3f().add(Vector3f.ONE).fromString(BlockposGet);
             float GBlockposX = (float) GBlockpos.x;
             float GBlockposY = (float) GBlockpos.y;
             float GBlockposZ = (float) GBlockpos.z;
             
             
         //   Vector3i ElevatorPosition = new Vector3i(Utils.ChunkUtils.getGlobalBlockPosition(0, 0, 0, GBlockposX, GBlockposY, GBlockposZ));
            float  ElevatorPositionfloatX = GBlockposX;
            float  ElevatorPositionfloatY = GBlockposY;
            float  ElevatorPositionfloatZ = GBlockposZ;
            Vector3f ElevatorPositionfloat = new Vector3f().addLocal(ElevatorPositionfloatX, ElevatorPositionfloatY, ElevatorPositionfloatZ);
       
       
            for(Prefab Elevator : elevators) {
            if(Elevator.getID() == getElevatorID){
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);    
           
           
           
             try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
        //           for(Prefab ElevatorDoors9 : elevatorDoors9) {
        //           int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
        //          if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
        //           ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
        //           ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
        //          }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
           
           
           
           
           
           
           
           
           
           
                
                
            
            
            
            player.setAttribute("CallElevator","Called");    
            Vector3f EPosition = Elevator.getLocalPosition();
            float distancebetween = ElevatorY - EPosition.y;
            
            
            Vector3f targetPosition = Elevator.getLocalPosition().add(0f, distancebetween, 0f);
                 float distance = Elevator.getLocalPosition().distance(targetPosition);
                 float speed = 8f;
                 
           executeDelayed(distance / speed+2, () -> {
               
               player.sendYellMessage("Arrived", 2, false);
                     SoundAsset Bellsoundfile = (SoundAsset)player.getAttribute("Bellsoundfile");
                     for(Player AllPlayers : Server.getAllPlayers()){
                     AllPlayers.playSound(Bellsoundfile, false, 1f, 1f, 0f, 15f, Elevator.getLocalPosition());      
                     }
                
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 2); 
           
           ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 2);
           ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 2);
               
           
           });
           executeDelayed(1, () -> {
           Elevator.moveToLocalPosition(ElevatorPositionfloat, 8);
           });
          
           executeDelayed(distance / speed +5, () -> {
           Elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           Elevator.setAnimatorParameter("RightDoor", "OpenClose", 1); 
           
           ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
           ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
           
           
            try (ResultSet OuterDoorresult = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '"+Elevator.getID()+"'")) {
                              
                   while (OuterDoorresult.next()) {
                   int StoredElevatorDoorsID = OuterDoorresult.getInt("ElevatorDoorsID");
                   player.setAttribute("StoredElevatorDoorsID", StoredElevatorDoorsID);
                //   player.sendTextMessage("stored id "+StoredElevatorDoorsID+"");
                   
                   
                   
                   
                   for(Prefab ElevatorDoors0 : elevatorDoors0) {
                   int ElevatorDoorsID = ElevatorDoors0.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors0.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors0.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}  
                       
                   for(Prefab ElevatorDoors1 : elevatorDoors1) {
                  int ElevatorDoorsID = ElevatorDoors1.getID();    
                     
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors1.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors1.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors2 : elevatorDoors2) {
                   int ElevatorDoorsID = ElevatorDoors2.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors2.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors2.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors3 : elevatorDoors3) {
                   int ElevatorDoorsID = ElevatorDoors3.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors3.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors3.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors4 : elevatorDoors4) {
                   int ElevatorDoorsID = ElevatorDoors4.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors4.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors4.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors5 : elevatorDoors5) {
                   int ElevatorDoorsID = ElevatorDoors5.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors5.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors5.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors6 : elevatorDoors6) {
                   int ElevatorDoorsID = ElevatorDoors6.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors6.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors6.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors7 : elevatorDoors7) {
                   int ElevatorDoorsID = ElevatorDoors7.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors7.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors7.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
                   for(Prefab ElevatorDoors8 : elevatorDoors8) {
                   int ElevatorDoorsID = ElevatorDoors8.getID();    
                      
                  if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
                   ElevatorDoors8.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                   ElevatorDoors8.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
                  }}
        //           for(Prefab ElevatorDoors9 : elevatorDoors9) {
        //           int ElevatorDoorsID = ElevatorDoors9.getID();    
                      
        //          if (StoredElevatorDoorsID == ElevatorDoorsID){    
                  
                   // close doors = 1
        //           ElevatorDoors9.setAnimatorParameter("LeftDoor", "OpenClose", 1);
        //           ElevatorDoors9.setAnimatorParameter("RightDoor", "OpenClose", 1);
                  
        //          }}
                       
                       
                   }
                   } catch (SQLException ex) {
                   Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           
           
           
           
           
           
           
           });
           
           
           
            }
            } 
        
       
       
    //   player.sendTextMessage("Match ="+getElevatorID+" and "+getBlockpos);
       }
       }catch (SQLException ex) {    
       Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
       }    
       } 
       
       
       }
       
            
         
            
          
            
            
                
                
             }
             });
             }
            
             
                  
}
    
    


  
}