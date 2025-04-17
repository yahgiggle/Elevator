package elevator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.events.player.PlayerKeyEvent;
import net.risingworld.api.events.player.PlayerMouseButtonEvent;
import net.risingworld.api.events.player.ui.PlayerUIElementClickEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.ui.UIElement;
import net.risingworld.api.ui.UILabel;
import net.risingworld.api.ui.UITextField;
import net.risingworld.api.ui.style.TextAnchor;
import net.risingworld.api.utils.Key;
import net.risingworld.api.utils.Layer;
import net.risingworld.api.utils.MouseButton;
import net.risingworld.api.utils.RaycastResult;
import net.risingworld.api.utils.Vector3f;
import net.risingworld.api.worldelements.Prefab;

/**
 * A plugin for Rising World that adds multi-floor elevators with dynamic floor counts and heights.
 * Players set the floor amount and height via a popup menu with text fields and Confirm/Cancel buttons.
 * Call elevators with the F key and select floors via a UI panel.
 * Features smooth animations, admin restrictions, hardcoded sounds, and a floor list system.
 *
 * @author Yahgiggle
 */
public class Elevator extends Plugin implements Listener {
    // Database for storing elevator and door data
    private Database database;
    private Database database2;

    // Default settings
    private static final float DEFAULT_FLOOR_HEIGHT = 8.0f;
    private static final float MIN_FLOOR_HEIGHT = 4.0f;
    private static final float MAX_FLOOR_HEIGHT = 12.0f;
    private static final String DOOR_SOUND = "Doors.mp3";
    private static final String LOOP_SOUND = "Loop.mp3";
    private static final String BELL_SOUND = "Bell.wav";
    private static final int MIN_FLOORS = 2;
    private static final int MAX_FLOORS = 20;
    private static final float DOOR_ANIMATION_DELAY = 1.0f;
    private static final float WAIT_TIME_SECONDS = 10.0f; // Wait 10 seconds at each floor

    // Lists for elevator and door prefabs
    private final ArrayList<Prefab> elevators = new ArrayList<>();
    private final Map<Integer, Map<Integer, ArrayList<Prefab>>> doorsByElevator = new HashMap<>();
    private final ArrayList<Prefab> previewElevator = new ArrayList<>();

    // Floor list for handling multiple elevator requests
    private final Map<Integer, List<Integer>> floorLists = new HashMap<>();
    private final Map<Integer, String> elevatorStates = new HashMap<>(); // "IDLE", "MOVING_TO", "WAITING"
    private final Map<Integer, Boolean> waitTimerActive = new HashMap<>(); // Track active wait timers

    /**
     * Initializes the plugin, sets up the database, loads elevators and doors,
     * and registers the event listener.
     */
    @Override
    public void onEnable() {
        System.out.println("-- ELEVATOR PLUGIN ENABLED --");

        // Initialize SQLite database
        String worldName = World.getName();
        database = getSQLiteConnection(getPath() + "/" + worldName + "/database.db");
        database2 = getSQLiteConnection(getPath() + "/" + worldName + "/database.db");
        
        // Create tables
        database.execute("CREATE TABLE IF NOT EXISTS `Elevators` (" +
                "`ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "`ElevatorBlockPosition` INTEGER," +
                "`ElevatorX` INTEGER, `ElevatorY` INTEGER, `ElevatorZ` INTEGER," +
                "`ElevatorFloorsAmount` INTEGER," +
                "`ElevatorFloorHeight` FLOAT," +
                "`Blockpos` INTEGER," +
                "`Chunkpos` INTEGER," +
                "`ElevatorID` INTEGER," +
                "`ElevatorRotation` BIGINT," +
                "`MaxFloors` INTEGER," +
                "`FloorHeight` FLOAT);");

        database.execute("CREATE TABLE IF NOT EXISTS `ElevatorOuterDoors` (" +
                "`ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "`ElevatorBlockPosition` INTEGER," +
                "`ElevatorX` INTEGER, `ElevatorY` INTEGER, `ElevatorZ` INTEGER," +
                "`ElevatorFloorsAmount` INTEGER," +
                "`ElevatorFloorHeight` FLOAT," +
                "`Blockpos` INTEGER," +
                "`ElevatorID` INTEGER," +
                "`ElevatorDoorsID` INTEGER," +
                "`ElevatorOuterDoorRotation` BIGINT," +
                "`FloorHeight` FLOAT);");

        database.execute("CREATE TABLE IF NOT EXISTS `ElevatorFloorLists` (" +
                "`ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "`ElevatorID` INTEGER," +
                "`Floor` INTEGER);");

        // Load elevators
        try {
            Map<String, Integer> blockPositionToElevatorID = new HashMap<>();
            Set<String> loadedElevators = new HashSet<>();
            try (ResultSet result = database.executeQuery("SELECT DISTINCT ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorRotation, MaxFloors, FloorHeight " +
                    "FROM `Elevators` WHERE `ElevatorFloorHeight` = 'Elevator'")) {
                while (result.next()) {
                    String blockPosition = result.getString("ElevatorBlockPosition");
                    if (loadedElevators.contains(blockPosition)) {
                        continue;
                    }
                    loadedElevators.add(blockPosition);

                    float x = result.getFloat("ElevatorX");
                    float y = result.getFloat("ElevatorY");
                    float z = result.getFloat("ElevatorZ");
                    float rotation = result.getFloat("ElevatorRotation");
                    int maxFloors = result.getInt("MaxFloors");
                    float floorHeight = result.getFloat("FloorHeight");
                    if (floorHeight == 0.0f) {
                        floorHeight = DEFAULT_FLOOR_HEIGHT;
                    }

                    AssetBundle bundle = AssetBundle.loadFromFile(getPath() + "/assets/elevator.bundle");
                    PrefabAsset asset = PrefabAsset.loadFromAssetBundle(bundle, "Elevator.prefab");
                    Prefab elevator = new Prefab(asset);

                    elevator.setLayer(Layer.OBJECT);
                    elevator.setLocalPosition(x, y, z);
                    elevator.setLocalRotation(0, rotation, 0);

                    int elevatorID = elevator.getID();
                    elevators.add(elevator);
                    synchronized (floorLists) {
                        floorLists.put(elevatorID, new LinkedList<>());
                    }
                    elevatorStates.put(elevatorID, "IDLE");
                    waitTimerActive.put(elevatorID, false);
                    doorsByElevator.put(elevatorID, new HashMap<>());
                    for (int i = 0; i < maxFloors; i++) {
                        doorsByElevator.get(elevatorID).put(i, new ArrayList<>());
                    }

                    blockPositionToElevatorID.put(blockPosition, elevatorID);

                    for (Player p : Server.getAllPlayers()) {
                        p.addGameObject(elevator);
                    }
                }
                System.out.println("Loaded " + loadedElevators.size() + " unique elevators");
            }

            for (Map.Entry<String, Integer> entry : blockPositionToElevatorID.entrySet()) {
                String blockPosition = entry.getKey();
                int newElevatorID = entry.getValue();
                database.executeUpdate("UPDATE Elevators SET `ElevatorID` = '" + newElevatorID + "' WHERE `ElevatorBlockPosition` = '" + blockPosition + "'");
                database.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorID` = '" + newElevatorID + "' WHERE `ElevatorBlockPosition` = '" + blockPosition + "'");
            }

            // Load floor lists
            try (ResultSet result = database.executeQuery("SELECT ElevatorID, Floor FROM `ElevatorFloorLists`")) {
                while (result.next()) {
                    int elevatorID = result.getInt("ElevatorID");
                    int floor = result.getInt("Floor");
                    if (floorLists.containsKey(elevatorID)) {
                        synchronized (floorLists.get(elevatorID)) {
                            floorLists.get(elevatorID).add(floor);
                        }
                        System.out.println("Loaded floor " + floor + " for elevatorID: " + elevatorID);
                    }
                }
            }

            int doorLoadCount = 0;
            try (ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors`")) {
                while (result.next()) {
                    int elevatorID = result.getInt("ElevatorID");
                    int floor = result.getInt("ElevatorFloorsAmount");
                    String blockPos = result.getString("Blockpos");
                    Vector3f pos = new Vector3f().add(Vector3f.ONE).fromString(blockPos);
                    float rotation = result.getFloat("ElevatorOuterDoorRotation");

                    AssetBundle doorBundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
                    PrefabAsset doorAsset = PrefabAsset.loadFromAssetBundle(doorBundle, "ElevatorDoors.prefab");
                    Prefab door = new Prefab(doorAsset);

                    door.setLayer(Layer.OBJECT);
                    door.setLocalPosition(pos.x, pos.y, pos.z);
                    door.setLocalRotation(0, rotation, 0);

                    if (doorsByElevator.containsKey(elevatorID) && doorsByElevator.get(elevatorID).containsKey(floor)) {
                        doorsByElevator.get(elevatorID).get(floor).add(door);
                        int doorID = result.getInt("ElevatorDoorsID");
                        if (doorID >= 0) {
                            doorID = door.getID();
                            database2.executeUpdate("UPDATE ElevatorOuterDoors SET `ElevatorDoorsID` = '" + doorID + "' WHERE `ElevatorID` = '" + elevatorID + "' AND `Blockpos` = '" + blockPos + "'");
                        }
                        for (Player p : Server.getAllPlayers()) {
                            p.addGameObject(door);
                        }
                        doorLoadCount++;
                        System.out.println("Loaded door for elevatorID: " + elevatorID + ", floor: " + floor + ", doorID: " + door.getID());
                    }
                }
                System.out.println("Loaded " + doorLoadCount + " doors");
            }
        } catch (Exception e) {
            System.err.println("Error loading elevators/doors/floor lists: " + e.getMessage());
            e.printStackTrace();
        }

        registerEventListener(this);
    }

    /**
     * Closes the database connections when disabled.
     */
    @Override
    public void onDisable() {
        System.out.println("-- ELEVATOR PLUGIN DISABLED --");
        if (database != null) {
            database.close();
        }
        if (database2 != null) {
            database2.close();
        }
    }

    /**
     * Sets up player attributes and UI on connection.
     */
    @EventMethod
    public void onPlayerConnect(PlayerConnectEvent event) throws SQLException {
        Player player = event.getPlayer();

        player.registerKeys(Key.B, Key.F, Key.K, Key.LeftShift);
        player.setAttribute("ElevatorID", 0);
        player.setAttribute("CallElevator", "NotCalled");
        player.setAttribute("DoorState", "Closed");
        player.setAttribute("PlacingElevator", false);
        player.setAttribute("Tick", 20);
        player.setListenForKeyInput(true);

        player.setAttribute("SoundLoop", SoundAsset.loadFromFile(getPath() + "/assets/" + LOOP_SOUND));
        player.setAttribute("Doorsoundfile", SoundAsset.loadFromFile(getPath() + "/assets/" + DOOR_SOUND));
        player.setAttribute("Bellsoundfile", SoundAsset.loadFromFile(getPath() + "/assets/" + BELL_SOUND));

        for (Prefab elevator : elevators) {
            player.addGameObject(elevator);
        }
        int doorCount = 0;
        for (Map<Integer, ArrayList<Prefab>> doors : doorsByElevator.values()) {
            for (ArrayList<Prefab> doorList : doors.values()) {
                for (Prefab door : doorList) {
                    player.addGameObject(door);
                    doorCount++;
                }
            }
        }

        // Floor selection UI
        UIElement mainPanel = new UIElement();
        player.addUIElement(mainPanel);
        mainPanel.setSize(75, 50, false);
        mainPanel.setPosition(95, 45, true);
        mainPanel.setBorder(5);
        mainPanel.setBorderColor(999);
        mainPanel.setBackgroundColor(12222);
        mainPanel.setVisible(false);
        player.setAttribute("ElevatorMainPanel", mainPanel);
        player.setAttribute("ElevatorMainPanelID", mainPanel.getID());

        UILabel exitButton = new UILabel();
        exitButton.setClickable(true);
        exitButton.setRichTextEnabled(true);
        exitButton.setFontSize(20);
        exitButton.setFontColor(9.0f, 0.0f, 0.0f, 5.0f);
        exitButton.setText(" X ");
        exitButton.setBorder(2);
        exitButton.setBorderColor(999);
        exitButton.setPosition(20.0f, 5.0f, false);
        mainPanel.addChild(exitButton);
        player.setAttribute("ElevatorExitbutton", exitButton);
        player.setAttribute("ElevatorExitbuttonID", exitButton.getID());

        UILabel deleteButton = new UILabel();
        deleteButton.setClickable(true);
        deleteButton.setRichTextEnabled(true);
        deleteButton.setFontSize(14);
        deleteButton.setText(" Delete ");
        deleteButton.setFontColor(9.0f, 0.0f, 0.0f, 5.0f);
        deleteButton.style.textAlign.set(TextAnchor.MiddleCenter);
        deleteButton.setBorder(2);
        deleteButton.setBorderColor(999);
        deleteButton.setPosition(6.0f, 20.0f, false);
        mainPanel.addChild(deleteButton);
        player.setAttribute("ElevatorDeletebutton", deleteButton);
        player.setAttribute("ElevatorDeletebuttonID", deleteButton.getID());

        // Pending floors label
        UILabel pendingFloorsLabel = new UILabel();
        pendingFloorsLabel.setRichTextEnabled(true);
        pendingFloorsLabel.setFontSize(12);
        pendingFloorsLabel.setText("Pending: None");
        pendingFloorsLabel.setPosition(6.0f, 35.0f, false);
        mainPanel.addChild(pendingFloorsLabel);
        player.setAttribute("PendingFloorsLabel", pendingFloorsLabel);
        player.setAttribute("PendingFloorsLabelID", pendingFloorsLabel.getID());

        // Floor input UI
        UIElement floorInputPanel = new UIElement();
        player.addUIElement(floorInputPanel);
        floorInputPanel.setSize(200, 200, false);
        floorInputPanel.setPosition(50, 50, true);
        floorInputPanel.setBorder(5);
        floorInputPanel.setBorderColor(999);
        floorInputPanel.setBackgroundColor(12222);
        floorInputPanel.setVisible(false);
        player.setAttribute("FloorInputPanel", floorInputPanel);
        player.setAttribute("FloorInputPanelID", floorInputPanel.getID());

        UILabel floorLabel = new UILabel();
        floorLabel.setRichTextEnabled(true);
        floorLabel.setFontSize(16);
        floorLabel.setText("Enter Floors (2-20):");
        floorLabel.setPosition(20.0f, 20.0f, false);
        floorInputPanel.addChild(floorLabel);

        UITextField floorTextField = new UITextField();
        floorTextField.setSize(160, 30, false);
        floorTextField.setPosition(20.0f, 50.0f, false);
        floorTextField.setBorder(2);
        floorTextField.setBorderColor(999);
        floorInputPanel.addChild(floorTextField);
        player.setAttribute("FloorTextField", floorTextField);
        player.setAttribute("FloorTextFieldID", floorTextField.getID());

        UILabel heightLabel = new UILabel();
        heightLabel.setRichTextEnabled(true);
        heightLabel.setFontSize(16);
        heightLabel.setText("Enter Floor Height (4-12):");
        heightLabel.setPosition(20.0f, 90.0f, false);
        floorInputPanel.addChild(heightLabel);

        UITextField heightTextField = new UITextField();
        heightTextField.setSize(160, 30, false);
        heightTextField.setPosition(20.0f, 120.0f, false);
        heightTextField.setBorder(2);
        heightTextField.setBorderColor(999);
        floorInputPanel.addChild(heightTextField);
        player.setAttribute("HeightTextField", heightTextField);
        player.setAttribute("HeightTextFieldID", heightTextField.getID());

        UILabel confirmButton = new UILabel();
        confirmButton.setClickable(true);
        confirmButton.setRichTextEnabled(true);
        confirmButton.setFontSize(14);
        confirmButton.setText(" Confirm ");
        confirmButton.setBorder(2);
        confirmButton.setBorderColor(999);
        confirmButton.setPosition(20.0f, 160.0f, false);
        floorInputPanel.addChild(confirmButton);
        player.setAttribute("ConfirmFloorButton", confirmButton);
        player.setAttribute("ConfirmFloorButtonID", confirmButton.getID());

        UILabel cancelButton = new UILabel();
        cancelButton.setClickable(true);
        cancelButton.setRichTextEnabled(true);
        cancelButton.setFontSize(14);
        cancelButton.setText(" Cancel ");
        cancelButton.setBorder(2);
        cancelButton.setBorderColor(999);
        cancelButton.setPosition(100.0f, 160.0f, false);
        floorInputPanel.addChild(cancelButton);
        player.setAttribute("CancelFloorButton", cancelButton);
        player.setAttribute("CancelFloorButtonID", cancelButton.getID());
    }

    /**
     * Handles UI clicks for floor selection, deletion, and floor input confirmation/cancel.
     */
    @EventMethod
    public void onPlayerUIElementClick(PlayerUIElementClickEvent event) throws SQLException {
        if (event.getUIElement() == null) return;

        Player player = event.getPlayer();
        int eventID = event.getUIElement().getID();
        UIElement mainPanel = (UIElement) player.getAttribute("ElevatorMainPanel");
        int elevatorID = (int) player.getAttribute("ElevatorID");

        if (eventID == (int) player.getAttribute("ElevatorExitbuttonID")) {
            player.setMouseCursorVisible(false);
            mainPanel.setVisible(false);
            return;
        }

        if (eventID == (int) player.getAttribute("ElevatorDeletebuttonID")) {
            if (player.isAdmin()) {
                for (Iterator<Prefab> iterator = elevators.iterator(); iterator.hasNext(); ) {
                    Prefab elevator = iterator.next();
                    if (elevator.getID() == elevatorID) {
                        for (Player p : Server.getAllPlayers()) {
                            p.removeGameObject(elevator);
                        }
                        database.executeUpdate("DELETE FROM `Elevators` WHERE `ElevatorID` = '" + elevatorID + "'");
                        database.executeUpdate("DELETE FROM `ElevatorFloorLists` WHERE `ElevatorID` = '" + elevatorID + "'");
                        synchronized (floorLists) {
                            floorLists.remove(elevatorID);
                        }
                        elevatorStates.remove(elevatorID);
                        waitTimerActive.remove(elevatorID);
                        iterator.remove();
                    }
                }

                try (ResultSet result = database.executeQuery("SELECT * FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '" + elevatorID + "'")) {
                    while (result.next()) {
                        int doorID = result.getInt("ElevatorDoorsID");
                        int floor = result.getInt("ElevatorFloorsAmount");
                        if (doorsByElevator.containsKey(elevatorID)) {
                            for (Iterator<Prefab> iterator = doorsByElevator.get(elevatorID).get(floor).iterator(); iterator.hasNext(); ) {
                                Prefab door = iterator.next();
                                if (door.getID() == doorID) {
                                    for (Player p : Server.getAllPlayers()) {
                                        p.removeGameObject(door);
                                    }
                                    database.executeUpdate("DELETE FROM `ElevatorOuterDoors` WHERE `ElevatorID` = '" + elevatorID + "' AND `ElevatorDoorsID` = '" + doorID + "'");
                                    iterator.remove();
                                }
                            }
                        }
                    }
                }
                if (doorsByElevator.containsKey(elevatorID)) {
                    doorsByElevator.remove(elevatorID);
                }

                player.sendYellMessage("Elevator Deleted", 3, false);
                player.setMouseCursorVisible(false);
                mainPanel.setVisible(false);
            } else {
                player.sendTextMessage("Only Admins Can Remove Elevators");
            }
            return;
        }

        UIElement floorInputPanel = (UIElement) player.getAttribute("FloorInputPanel");
        if (eventID == (int) player.getAttribute("ConfirmFloorButtonID")) {
            UITextField floorTextField = (UITextField) player.getAttribute("FloorTextField");
            UITextField heightTextField = (UITextField) player.getAttribute("HeightTextField");
            floorTextField.getCurrentText(player, (String floorText) -> {
                heightTextField.getCurrentText(player, (String heightText) -> {
                    try {
                        if (floorText == null || floorText.trim().isEmpty()) {
                            player.sendTextMessage("Please enter a valid floor count");
                            return;
                        }
                        int floors = Integer.parseInt(floorText.trim());
                        if (floors < MIN_FLOORS || floors > MAX_FLOORS) {
                            player.sendTextMessage("Please enter a number between " + MIN_FLOORS + " and " + MAX_FLOORS);
                            return;
                        }

                        if (heightText == null || heightText.trim().isEmpty()) {
                            player.sendTextMessage("Please enter a valid floor height");
                            return;
                        }
                        float height = Float.parseFloat(heightText.trim());
                        if (height < MIN_FLOOR_HEIGHT || height > MAX_FLOOR_HEIGHT) {
                            player.sendTextMessage("Please enter a height between " + MIN_FLOOR_HEIGHT + " and " + MAX_FLOOR_HEIGHT);
                            return;
                        }

                        player.setAttribute("SelectedFloors", floors);
                        player.setAttribute("SelectedFloorHeight", height);
                        player.setAttribute("PlacingElevator", true);
                        startPlacementPreview(player);
                        floorInputPanel.setVisible(false);
                        player.setMouseCursorVisible(false);
                    } catch (NumberFormatException e) {
                        player.sendTextMessage("Invalid input. Please enter numbers for floors and height");
                    }
                });
            });
            return;
        }

        if (eventID == (int) player.getAttribute("CancelFloorButtonID")) {
            floorInputPanel.setVisible(false);
            player.setMouseCursorVisible(false);
            player.setAttribute("PlacingElevator", false);
            return;
        }

        try (ResultSet result = database.executeQuery("SELECT MaxFloors FROM `Elevators` WHERE `ElevatorID` = '" + elevatorID + "' LIMIT 1")) {
            if (result.next()) {
                int maxFloors = result.getInt("MaxFloors");
                for (int floor = 0; floor < maxFloors; floor++) {
                    if (eventID == (int) player.getAttribute("ElevatorLevel" + floor + "buttonID")) {
                        queueFloorRequest(elevatorID, floor, player, mainPanel, true);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Starts the placement preview after floor count and height are confirmed.
     */
    private void startPlacementPreview(Player player) {
        Timer raycastTimer = new Timer(-1, 0.0f, -1, null);
        Runnable raycastUpdate = () -> {
            int layerMask = Layer.getBitmask(Layer.CONSTRUCTION, Layer.OBJECT, Layer.TERRAIN, Layer.WORLD);
            player.raycast(layerMask, (RaycastResult result) -> {
                if (result != null) {
                    Vector3f collisionPoint = result.getCollisionPoint();
                    player.setAttribute("HardwareID", result.getObjectGlobalID());
                    player.setAttribute("CollisionPoint", collisionPoint);

                    for (Prefab preview : previewElevator) {
                        int prefabID = (int) player.getAttribute("PrefabID");
                        if (preview.getID() == prefabID) {
                            preview.moveToLocalPosition(collisionPoint.x, collisionPoint.y, collisionPoint.z, -1);
                            preview.setLocalRotation(0, player.getRotation().getYaw() + 180, 0);
                            player.setAttribute("PreviewedPrefab", preview);
                        }
                    }
                }
            });
        };
        raycastTimer.setTask(raycastUpdate);
        raycastTimer.start();
        player.setAttribute("raycastTimer", raycastTimer);

        AssetBundle bundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
        PrefabAsset elevatorAsset = PrefabAsset.loadFromAssetBundle(bundle, "ElevatorDoors.prefab");
        Prefab prefab = new Prefab(elevatorAsset);
        prefab.setLayer(Layer.IGNORE_RAYCAST);
        prefab.setLocalPosition(player.getPosition());

        previewElevator.add(prefab);
        player.setAttribute("PrefabID", prefab.getID());

        for (Player p : Server.getAllPlayers()) {
            p.addGameObject(prefab);
        }

        Timer removePreviewTimer = new Timer(1, 120, 0, null);
        Runnable removePreviewUpdate = () -> {
            for (Iterator<Prefab> iterator = previewElevator.iterator(); iterator.hasNext(); ) {
                Prefab preview = iterator.next();
                if (preview.getID() == prefab.getID()) {
                    for (Player p : Server.getAllPlayers()) {
                        p.removeGameObject(preview);
                    }
                    player.setAttribute("PlacingElevator", false);
                    iterator.remove();
                    player.sendYellMessage("Elevator Placing Ended", 2, true);
                }
            }
        };
        removePreviewTimer.setTask(removePreviewUpdate);
        removePreviewTimer.start();
        player.setAttribute("RemovePreviewTimer", removePreviewTimer);
    }

    /**
     * Places elevators with right-click when in placing mode.
     */
    @EventMethod
    public void onPlayerMouseButtonEvent(PlayerMouseButtonEvent event) throws SQLException {
        Player player = event.getPlayer();
        if (!event.isPressed() || event.getButton() != MouseButton.Right || !Boolean.TRUE.equals(player.getAttribute("PlacingElevator"))) {
            return;
        }

        if (!player.isAdmin()) {
            player.sendTextMessage("Only Admins Can Place Elevators");
            return;
        }

        int prefabID = (int) player.getAttribute("PrefabID");
        for (Iterator<Prefab> iterator = previewElevator.iterator(); iterator.hasNext(); ) {
            Prefab preview = iterator.next();
            if (preview.getID() == prefabID) {
                for (Player p : Server.getAllPlayers()) {
                    p.removeGameObject(preview);
                }
                iterator.remove();
            }
        }

        Prefab previewedPrefab = (Prefab) player.getAttribute("PreviewedPrefab");
        Vector3f pos = previewedPrefab.getLocalPosition();
        float x = pos.x;
        float y = pos.y;
        float z = pos.z;
        int chunkPos = 0;
        float rotation = player.getRotation().getYaw() + 180;
        int maxFloors = (Integer) player.getAttribute("SelectedFloors");
        float floorHeight = (Float) player.getAttribute("SelectedFloorHeight");

        AssetBundle bundle = AssetBundle.loadFromFile(getPath() + "/assets/elevator.bundle");
        AssetBundle doorBundle = AssetBundle.loadFromFile(getPath() + "/assets/elevatordoors.bundle");
        PrefabAsset elevatorAsset = PrefabAsset.loadFromAssetBundle(bundle, "Elevator.prefab");
        PrefabAsset doorAsset = PrefabAsset.loadFromAssetBundle(doorBundle, "ElevatorDoors.prefab");

        Prefab elevator = new Prefab(elevatorAsset);
        Prefab[] doors = new Prefab[maxFloors];
        for (int i = 0; i < maxFloors; i++) {
            doors[i] = new Prefab(doorAsset);
        }

        int elevatorID = elevator.getID();
        int[] doorIDs = new int[maxFloors];
        for (int i = 0; i < maxFloors; i++) {
            doorIDs[i] = doors[i].getID();
        }

        elevator.setLayer(Layer.OBJECT);
        for (Prefab door : doors) {
            door.setLayer(Layer.OBJECT);
        }

        for (int i = 0; i < maxFloors; i++) {
            doors[i].setLocalPosition(x, y + (i * floorHeight), z);
            doors[i].setLocalRotation(0, rotation, 0);
        }

        elevator.setLocalPosition(x, y, z);
        elevator.setLocalRotation(0, rotation, 0);

        elevators.add(elevator);
        synchronized (floorLists) {
            floorLists.put(elevatorID, new LinkedList<>());
        }
        elevatorStates.put(elevatorID, "IDLE");
        waitTimerActive.put(elevatorID, false);
        doorsByElevator.put(elevatorID, new HashMap<>());
        for (int i = 0; i < maxFloors; i++) {
            doorsByElevator.get(elevatorID).put(i, new ArrayList<>());
            doorsByElevator.get(elevatorID).get(i).add(doors[i]);
        }

        for (Player p : Server.getAllPlayers()) {
            p.addGameObject(elevator);
            for (Prefab door : doors) {
                p.addGameObject(door);
            }
        }

        Vector3f[] levels = new Vector3f[maxFloors];
        for (int i = 0; i < maxFloors; i++) {
            levels[i] = new Vector3f(x, y + (i * floorHeight), z);
        }

        for (int i = 0; i < maxFloors; i++) {
            database.executeUpdate("INSERT INTO `Elevators` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight, Blockpos, Chunkpos, ElevatorID, ElevatorRotation, MaxFloors, FloorHeight) " +
                    "VALUES ('" + player.getPosition() + "','" + x + "','" + y + "','" + z + "','" + i + "','Elevator','" + levels[i] + "','" + chunkPos + "','" + elevatorID + "','" + rotation + "','" + maxFloors + "','" + floorHeight + "');");
            database.executeUpdate("INSERT INTO `ElevatorOuterDoors` (ElevatorBlockPosition, ElevatorX, ElevatorY, ElevatorZ, ElevatorFloorsAmount, ElevatorFloorHeight, Blockpos, ElevatorID, ElevatorDoorsID, ElevatorOuterDoorRotation, FloorHeight) " +
                    "VALUES ('" + player.getPosition() + "','" + x + "','" + y + "','" + z + "','" + i + "','ElevatorDoor" + i + "','" + levels[i] + "','" + elevatorID + "','" + doorIDs[i] + "','" + rotation + "','" + floorHeight + "');");
        }

        player.setAttribute("PlacingElevator", false);
        player.setAttribute("SelectedFloors", null);
        player.setAttribute("SelectedFloorHeight", null);
        player.sendYellMessage("Elevator Placed with " + maxFloors + " Floors and " + floorHeight + " Height", 2, true);
    }

    /**
     * Handles key inputs for initiating placement and calling elevators.
     */
    @EventMethod
    public void onPlayerKeyInputEvent(PlayerKeyEvent event) {
        Player player = event.getPlayer();

        if (event.isPressed() && event.getKey() == Key.B && player.isKeyPressed(Key.LeftShift)) {
            if (!player.isAdmin()) {
                player.sendTextMessage("Only Admins Can Place Elevators");
                return;
            }

            player.hideCrafting();
            UIElement floorInputPanel = (UIElement) player.getAttribute("FloorInputPanel");
            floorInputPanel.setVisible(true);
            player.setMouseCursorVisible(true);
        }

        if (event.isPressed() && event.getKey() == Key.F) {
            int layerMask = Layer.getBitmask(Layer.OBJECT);
            player.raycast(layerMask, (RaycastResult result) -> {
                if (result == null) return;
                long hitID = result.getObjectGlobalID();

                for (Prefab elevator : elevators) {
                    if (elevator.getID() == hitID) {
                        try (ResultSet rs = database.executeQuery("SELECT MaxFloors FROM `Elevators` WHERE `ElevatorID` = '" + elevator.getID() + "' LIMIT 1")) {
                            if (rs.next()) {
                                int maxFloors = rs.getInt("MaxFloors");
                                player.setAttribute("ElevatorID", elevator.getID());
                                showFloorSelectionUI(player, maxFloors);
                            }
                        } catch (SQLException e) {
                            System.err.println("SQL error querying MaxFloors: " + e.getMessage());
                        }
                        return;
                    }
                }

                try (ResultSet rs = database.executeQuery("SELECT ElevatorID, ElevatorFloorsAmount FROM `ElevatorOuterDoors` WHERE `ElevatorDoorsID` = '" + hitID + "' LIMIT 1")) {
                    if (rs.next()) {
                        int elevatorID = rs.getInt("ElevatorID");
                        int floor = rs.getInt("ElevatorFloorsAmount");
                        queueFloorRequest(elevatorID, floor, player, null, false);
                    }
                } catch (SQLException e) {
                    System.err.println("SQL error querying door: " + e.getMessage());
                }
            });
        }
    }

    /**
     * Shows the floor selection UI with dynamic buttons and pending floors.
     */
    private void showFloorSelectionUI(Player player, int maxFloors) {
        UIElement mainPanel = (UIElement) player.getAttribute("ElevatorMainPanel");

        mainPanel = new UIElement();
        player.addUIElement(mainPanel);
        mainPanel.setSize(75, 50 + (maxFloors * 35), false);
        mainPanel.setPosition(95, 20, true);
        mainPanel.setBorder(5);
        mainPanel.setBorderColor(999);
        mainPanel.setBackgroundColor(12222);
        player.setAttribute("ElevatorMainPanel", mainPanel);
        player.setAttribute("ElevatorMainPanelID", mainPanel.getID());

        UILabel exitButton = new UILabel();
        exitButton.setClickable(true);
        exitButton.setRichTextEnabled(true);
        exitButton.setFontSize(20);
        exitButton.setFontColor(9.0f, 0.0f, 0.0f, 5.0f);
        exitButton.setText(" X ");
        exitButton.setBorder(2);
        exitButton.setBorderColor(999);
        exitButton.setPosition(20.0f, 5.0f, false);
        mainPanel.addChild(exitButton);
        player.setAttribute("ElevatorExitbutton", exitButton);
        player.setAttribute("ElevatorExitbuttonID", exitButton.getID());

        for (int floor = 0; floor < maxFloors; floor++) {
            UILabel floorButton = new UILabel();
            floorButton.setClickable(true);
            floorButton.setRichTextEnabled(true);
            floorButton.setFontSize(20);
            floorButton.setText(" " + floor + " ");
            floorButton.setBorder(2);
            floorButton.setBorderColor(999);
            float yPosition = 40.0f + ((maxFloors - 1 - floor) * 35.0f);
            floorButton.setPosition(20.0f, yPosition, false);
            mainPanel.addChild(floorButton);
            player.setAttribute("ElevatorLevel" + floor + "button", floorButton);
            player.setAttribute("ElevatorLevel" + floor + "buttonID", floorButton.getID());
        }

        UILabel deleteButton = new UILabel();
        deleteButton.setClickable(true);
        deleteButton.setRichTextEnabled(true);
        deleteButton.setFontSize(14);
        deleteButton.setText(" Delete ");
        deleteButton.setFontColor(9.0f, 0.0f, 0.0f, 5.0f);
        deleteButton.style.textAlign.set(TextAnchor.MiddleCenter);
        deleteButton.setBorder(2);
        deleteButton.setBorderColor(999);
        float deleteYPosition = 40.0f + (maxFloors * 35.0f);
        deleteButton.setPosition(6.0f, deleteYPosition, false);
        mainPanel.addChild(deleteButton);
        player.setAttribute("ElevatorDeletebutton", deleteButton);
        player.setAttribute("ElevatorDeletebuttonID", deleteButton.getID());

        // Pending floors label
        UILabel pendingFloorsLabel = new UILabel();
        pendingFloorsLabel.setRichTextEnabled(true);
        pendingFloorsLabel.setFontSize(12);
        int elevatorID = (int) player.getAttribute("ElevatorID");
        String pendingText;
        synchronized (floorLists) {
            pendingText = floorLists.containsKey(elevatorID) && !floorLists.get(elevatorID).isEmpty() ?
                    "Pending: " + String.join(", ", floorLists.get(elevatorID).stream().map(String::valueOf).toArray(String[]::new)) :
                    "Pending: None";
        }
        pendingFloorsLabel.setText(pendingText);
        pendingFloorsLabel.setPosition(6.0f, deleteYPosition + 15.0f, false);
        mainPanel.addChild(pendingFloorsLabel);
        player.setAttribute("PendingFloorsLabel", pendingFloorsLabel);
        player.setAttribute("PendingFloorsLabelID", pendingFloorsLabel.getID());

        mainPanel.setVisible(true);
        player.setMouseCursorVisible(true);
    }

    /**
     * Queues a floor request and processes the list.
     */
    private void queueFloorRequest(int elevatorID, int floor, Player player, UIElement mainPanel, boolean fromInside) {
        List<Integer> floorList;
        synchronized (floorLists) {
            floorList = floorLists.get(elevatorID);
            if (floorList == null) {
                player.sendTextMessage("Invalid elevator ID");
                return;
            }

            // Add floor to list if not already present
            if (!floorList.contains(floor)) {
                floorList.add(floor);
                database.executeUpdate("INSERT INTO `ElevatorFloorLists` (ElevatorID, Floor) VALUES ('" + elevatorID + "', '" + floor + "')"); // Rollback on error
                System.out.println("Queued floor " + floor + " for elevatorID: " + elevatorID);
            }

            // If from inside, prioritize this floor
            if (fromInside && !elevatorStates.getOrDefault(elevatorID, "IDLE").equals("WAITING")) {
                floorList.removeIf(f -> f.equals(floor)); // Remove duplicate
                floorList.add(0, floor); // Move to front
                database.executeUpdate("DELETE FROM `ElevatorFloorLists` WHERE `ElevatorID` = '" + elevatorID + "' AND `Floor` = '" + floor + "'"); // Rollback on error
                database.executeUpdate("INSERT INTO `ElevatorFloorLists` (ElevatorID, Floor) VALUES ('" + elevatorID + "', '" + floor + "')");
            }
        }

        // Update UI for all players in elevator
        for (Player p : Server.getAllPlayers()) {
            if (p.getAttribute("ElevatorID") != null && (int) p.getAttribute("ElevatorID") == elevatorID) {
                UILabel pendingFloorsLabel = (UILabel) p.getAttribute("PendingFloorsLabel");
                if (pendingFloorsLabel != null) {
                    synchronized (floorLists) {
                        String pendingText = !floorList.isEmpty() ?
                                "Pending: " + String.join(", ", floorList.stream().map(String::valueOf).toArray(String[]::new)) :
                                "Pending: None";
                        pendingFloorsLabel.setText(pendingText);
                    }
                }
            }
        }

        // Process only if idle
        String state = elevatorStates.getOrDefault(elevatorID, "IDLE");
        if (state.equals("IDLE")) {
            processNextFloor(elevatorID, player, mainPanel);
        }
    }

    /**
     * Processes the next floor in the list with smooth movement and wait time.
     */
    private void processNextFloor(int elevatorID, Player player, UIElement mainPanel) {
        List<Integer> floorList;
        synchronized (floorLists) {
            floorList = floorLists.get(elevatorID);
            if (floorList == null || floorList.isEmpty()) {
                if (mainPanel != null) {
                    player.setMouseCursorVisible(false);
                    mainPanel.setVisible(false);
                }
                elevatorStates.put(elevatorID, "IDLE");
                waitTimerActive.put(elevatorID, false);
                System.out.println("No floors to process for elevatorID: " + elevatorID);
                return;
            }
        }

        int floor;
        synchronized (floorLists) {
            floor = floorList.get(0);
        }
        elevatorStates.put(elevatorID, "MOVING_TO");
        System.out.println("Processing floor " + floor + " for elevatorID: " + elevatorID);
        try (ResultSet result = database.executeQuery("SELECT Blockpos, FloorHeight FROM `Elevators` WHERE `ElevatorFloorHeight` = 'Elevator' AND `ElevatorFloorsAmount` = '" + floor + "' AND `ElevatorID` = '" + elevatorID + "'")) {
            if (result.next()) {
                Vector3f targetPos = new Vector3f().add(Vector3f.ONE).fromString(result.getString("Blockpos"));
                float floorHeight = result.getFloat("FloorHeight");
                if (floorHeight == 0.0f) {
                    floorHeight = DEFAULT_FLOOR_HEIGHT;
                }

                for (Prefab elevator : elevators) {
                    if (elevator.getID() == elevatorID) {
                        elevator.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                        elevator.setAnimatorParameter("RightDoor", "OpenClose", 1);

                        if (doorsByElevator.containsKey(elevatorID)) {
                            for (Map.Entry<Integer, ArrayList<Prefab>> floorEntry : doorsByElevator.get(elevatorID).entrySet()) {
                                for (Prefab door : floorEntry.getValue()) {
                                    door.setAnimatorParameter("LeftDoor", "OpenClose", 1);
                                    door.setAnimatorParameter("RightDoor", "OpenClose", 1);
                                }
                            }
                        }

                        SoundAsset doorSound = (SoundAsset) player.getAttribute("Doorsoundfile");
                        Vector3f currentPos = elevator.getLocalPosition();
                        for (Player p : Server.getAllPlayers()) {
                            p.playSound(doorSound, false, 1f, 1f, 0f, 15f, currentPos);
                        }

                        Timer delayTimer = new Timer(DOOR_ANIMATION_DELAY, 0.0f, 1, null);
                        delayTimer.setTask(() -> {
                            Timer moveTimer = new Timer(0.05f, 0.0f, -1, null);
                            Vector3f startPos = new Vector3f(currentPos.x, currentPos.y, currentPos.z);
                            float duration = currentPos.distance(targetPos) / 8.0f;
                            float[] elapsed = {0.0f};

                            Runnable moveTask = () -> {
                                elapsed[0] += 0.05f;
                                float t = Math.min(elapsed[0] / duration, 1.0f);
                                float x = startPos.x + (targetPos.x - startPos.x) * t;
                                float y = startPos.y + (targetPos.y - startPos.y) * t;
                                float z = startPos.z + (targetPos.z - startPos.z) * t;
                                Vector3f newPos = new Vector3f(x, y, z);
                                elevator.setLocalPosition(newPos);

                                if (t >= 1.0f) {
                                    moveTimer.kill();
                                    player.sendYellMessage("Arrived at floor " + floor, 2, false);
                                    SoundAsset bellSound = (SoundAsset) player.getAttribute("Bellsoundfile");
                                    for (Player p : Server.getAllPlayers()) {
                                        p.playSound(bellSound, false, 1f, 1f, 0f, 15f, targetPos);
                                    }

                                    elevator.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                                    elevator.setAnimatorParameter("RightDoor", "OpenClose", 2);

                                    for (Player p : Server.getAllPlayers()) {
                                        p.playSound(doorSound, false, 1f, 1f, 0f, 15f, targetPos);
                                    }

                                    if (doorsByElevator.containsKey(elevatorID)) {
                                        for (Prefab door : doorsByElevator.get(elevatorID).getOrDefault(floor, new ArrayList<>())) {
                                            door.setAnimatorParameter("LeftDoor", "OpenClose", 2);
                                            door.setAnimatorParameter("RightDoor", "OpenClose", 2);
                                        }
                                    }

                                    // Remove floor from list and database
                                    synchronized (floorLists) {
                                        if (!floorList.isEmpty()) {
                                            floorList.remove(0);
                                            database.executeUpdate("DELETE FROM `ElevatorFloorLists` WHERE `ElevatorID` = '" + elevatorID + "' AND `Floor` = '" + floor + "'");
                                            System.out.println("Removed floor " + floor + " from elevatorID: " + elevatorID);
                                        }
                                    }

                                    // Update UI for all players
                                    synchronized (floorLists) {
                                        for (Player p : Server.getAllPlayers()) {
                                            if (p.getAttribute("ElevatorID") != null && (int) p.getAttribute("ElevatorID") == elevatorID) {
                                                UILabel pendingFloorsLabel = (UILabel) p.getAttribute("PendingFloorsLabel");
                                                if (pendingFloorsLabel != null) {
                                                    String pendingText = !floorList.isEmpty() ?
                                                            "Pending: " + String.join(", ", floorList.stream().map(String::valueOf).toArray(String[]::new)) :
                                                            "Pending: None";
                                                    pendingFloorsLabel.setText(pendingText);
                                                }
                                            }
                                        }
                                    }

                                    // Wait 10 seconds
                                    elevatorStates.put(elevatorID, "WAITING");
                                    waitTimerActive.put(elevatorID, true);
                                    Timer waitTimer = new Timer(WAIT_TIME_SECONDS, 0.0f, 1, null);
                                    waitTimer.setTask(() -> {
                                        synchronized (floorLists) {
                                            waitTimerActive.put(elevatorID, false);
                                            if (floorList.isEmpty()) {
                                                elevatorStates.put(elevatorID, "IDLE");
                                                if (mainPanel != null) {
                                                    player.setMouseCursorVisible(false);
                                                    mainPanel.setVisible(false);
                                                }
                                                System.out.println("Elevator " + elevatorID + " idle, no floors left");
                                                return;
                                            }
                                            processNextFloor(elevatorID, player, mainPanel);
                                        }
                                    });
                                    waitTimer.start();
                                }
                            };
                            moveTimer.setTask(moveTask);
                            moveTimer.start();
                        });
                        delayTimer.start();
                    }
                }
            } else {
                synchronized (floorLists) {
                    if (!floorList.isEmpty()) {
                        floorList.remove(0);
                        database.executeUpdate("DELETE FROM `ElevatorFloorLists` WHERE `ElevatorID` = '" + elevatorID + "' AND `Floor` = '" + floor + "'");
                        System.out.println("Removed invalid floor " + floor + " from elevatorID: " + elevatorID);
                    }
                }
                // Continue processing next floor
                synchronized (floorLists) {
                    if (!floorList.isEmpty()) {
                        processNextFloor(elevatorID, player, mainPanel);
                    } else {
                        elevatorStates.put(elevatorID, "IDLE");
                        waitTimerActive.put(elevatorID, false);
                        if (mainPanel != null) {
                            player.setMouseCursorVisible(false);
                            mainPanel.setVisible(false);
                        }
                        System.out.println("No floors to process after invalid floor for elevatorID: " + elevatorID);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
            synchronized (floorLists) {
                if (!floorList.isEmpty()) {
                    floorList.remove(0);
                    database.executeUpdate("DELETE FROM `ElevatorFloorLists` WHERE `ElevatorID` = '" + elevatorID + "' AND `Floor` = '" + floor + "'");
                    System.out.println("Removed error floor " + floor + " from elevatorID: " + elevatorID);
                }
                // Continue processing next floor
                if (!floorList.isEmpty()) {
                    processNextFloor(elevatorID, player, mainPanel);
                } else {
                    elevatorStates.put(elevatorID, "IDLE");
                    waitTimerActive.put(elevatorID, false);
                    if (mainPanel != null) {
                        player.setMouseCursorVisible(false);
                        mainPanel.setVisible(false);
                    }
                    System.out.println("No floors to process after SQL error for elevatorID: " + elevatorID);
                }
            }
        }
    }
}