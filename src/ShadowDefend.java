import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public class ShadowDefend extends AbstractGame {
    //map and timer related
    private TiledMap map;
    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private static final int ONE = 1;
    private static final int INITIAL_TIMESCALE = 1;
    private static final int ZERO = 0;
    private static int timer;
    private static int timescale;

    //level related
    private static final String LV1 = "res/levels/1.tmx";
    private static final String LV2 = "res/levels/2.tmx";
    private static final String WAVES = "res/levels/waves.txt";
    private String[] levels = {LV1, LV2};

    //enemies and bombs related
    private static ArrayList<Slicer> slicers;
    private ArrayList<Wave> waves;
    private ArrayList<Tower> towers;
    private static ArrayList<Projectile> projectiles;
    private ArrayList<Airplane> airplanes;
    private static ArrayList<Explosive> explosives;
    private int selectProduct; //0: unselected, 1: tank, 2:super tank, 3: airplane
    public static int flyDirection = 0; //0: fly horizontally, 1: fly vertically

    //Loading waves and levels related
    private int currentLevel;
    private int currentWave;
    private int currentEvent;
    private Boolean waveStarted;

    //panel related
    private StatusPanel statusPanel;
    private BuyPanel buyPanel;
    private static Status status;
    private static final int RECOGNIZED_DISTANCE = 35;
    private static final int PLACE_DISTANCE = 20;
    private static final double OVERLAP_BUYPANEL = 1.2;

    //player related
    private static int money;
    private static int lives;

    /**
     * Constructor for ShadowDefend
     * initialising all game related variables, level map and load waves and events
     */
    public ShadowDefend() {
        super(WIDTH, HEIGHT, "ShadowDefend");
        resetGame();
        map = new TiledMap(levels[ZERO]);
        loadWave();
    }


    /**
     * rest all variables in the game, for initialisation
     */
    public void resetGame(){
        waveStarted = false;
        currentLevel = ZERO;
        timer = ZERO;
        timescale = INITIAL_TIMESCALE;
        waveStarted = false;
        status = Status.AWAITING_START;
        statusPanel = new StatusPanel();
        buyPanel = new BuyPanel();
        towers  = new ArrayList<Tower>();
        projectiles = new ArrayList<Projectile>();
        slicers = new ArrayList<Slicer>();
        airplanes = new ArrayList<Airplane>();
        explosives = new ArrayList<Explosive>();
        lives = 25;
        money=500;
    }

    /**
     * load waves and their corresponding events from WAVES file via BufferedReader
     */
    public void loadWave(){
        try(BufferedReader br = new BufferedReader(new FileReader(WAVES))) {
            String one_event;
            ArrayList<Wave> waves = new ArrayList<Wave>();
            ArrayList<Event> events = new ArrayList<Event>();

            while ((one_event = br.readLine()) != null){
                String[] element = one_event.split(",");

                //identify if it is a spawn/delay event, add to events ArrayList
                if(element[1].equals("spawn")){
                    // parse data to assign parameters for a spawn event
                    int waveNumber = Integer.parseInt(element[0]);
                    int numToSpawn = Integer.parseInt(element[2]);
                    String typeToSpawn = element[3];
                    int spawnDelay = Integer.parseInt(element[4]);

                    //add SpawnEvent into "events"ArrayList
                    events.add(new SpawnEvent(waveNumber, typeToSpawn, numToSpawn, spawnDelay));
                }
                else if(element[1].equals("delay")){
                    // parse data to assign parameters for a spawn event
                    int waveNumber = Integer.parseInt(element[0]);
                    int delayTime = Integer.parseInt(element[2]);

                    //add DelayEvent into ArrayList
                    events.add(new DelayEvent(waveNumber, delayTime));
                }
            }
            //add events into waves with same wave number
            int wave_num = ONE;
            Wave wave = new Wave(wave_num);
            for(Event event: events){
                // if current wave is fully stored, create a new wave to store
                if (event.getWaveNum() != wave_num) {
                    waves.add(wave);
                    wave_num++;
                    wave = new Wave(wave_num);
                }
                wave.getWaveEvent().add(event);
            }
            waves.add(wave);
            this.waves = waves;
            this.currentWave = ZERO;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ShadowDefend().run();
    }

    /**
     * Update all game related information in every frame(timer)
     * @param input mouse and keyboard input
     */
    @Override
    protected void update(Input input) {
        // draw the map
        map.draw(ZERO, ZERO, ZERO, ZERO, WIDTH, HEIGHT);
        //update purchase related information
        purchaseProduct(input);

        updateStatus();
        updateLevel();
        updateWave();
        //update every slicer that is on mao and not eliminated
        for(Slicer slicer : slicers){
            if(slicer.onMap()){
                slicer.update();
            }
        }
        //update towers
        for (Tower tower: towers){tower.update(); }
        //update projectiles
        for (Projectile projectile : projectiles){projectile.update(); }
        //update airplanes
        for (Airplane airplane : airplanes){airplane.update(); }
        //update explosives
        for (Explosive explosive : explosives){explosive.update(); }

        // press 'S' key to start a new wave
        if (!waveStarted && input.wasPressed(Keys.S)) {
            if (currentWave < waves.size()) {
                waveStarted = true;
            }
        }

        //timescale Controller
        if (input.wasPressed(Keys.L)) {increaseTimescale();}
        if (input.wasPressed(Keys.K)) {decreaseTimescale();}

        //update statusPanel and buyPanel
        buyPanel.render();
        statusPanel.render(currentWave + ONE, timescale, status, lives);

        //if lives becomes 0, game end
        if (lives <= 0){
            Window.close();
        }
        timer += timescale;
    }

    /**
     * Process valid selection and buy avaiable products(tank, supertank, airplane) from BuyPanel.
     * @param input mouse inputs
     */
    private void purchaseProduct(Input input) {
        try {
            Point mousePosition = new Point(input.getMouseX(), input.getMouseY());
            //if place position is on polyline
            boolean isBlocked = map.getPropertyBoolean((int) input.getMousePosition().x, (int) input.getMousePosition().y, "blocked", false);
            //if overlap buyPanel (keep certain distance)  or statusPanel
            boolean overlapBuyPanel = (mousePosition.y < buyPanel.getImage().getHeight() * OVERLAP_BUYPANEL);
            boolean overlapStatusPanel = (mousePosition.y > HEIGHT - statusPanel.getImage().getHeight());
            //if there is already a tower exist
            boolean isPlaced = false;
            for (Tower tower : towers) {
                if (tower.getTowerLocation().distanceTo(mousePosition) <= PLACE_DISTANCE) {
                    isPlaced = true;
                }
            }

            int tank_price = BuyPanel.getTankPrice();
            int supertank_price = BuyPanel.getSuperTankPrice();
            int airplane_price = BuyPanel.getAirplanePrice();

            //if no product is selected yet
            int UNSELECTED = 0;
            int TANK = 1;
            int SUPERTANK = 2;
            int AIRPLANE = 3;
            if (selectProduct == 0) {
                //process selection if money is sufficient
                if (input.wasPressed(MouseButtons.LEFT)) {
                    //buy tank
                    if (input.getMousePosition().distanceTo(new Point(BuyPanel.TANK_X, BuyPanel.Y)) < RECOGNIZED_DISTANCE && money - tank_price >= 0) {
                        selectProduct = 1;
                    }
                    //buy super tank
                    if (input.getMousePosition().distanceTo(new Point(BuyPanel.SUPERTANK_X, BuyPanel.Y)) < RECOGNIZED_DISTANCE && money - supertank_price >= 0) {
                        selectProduct = 2;
                    }
//                    buy Air Plane
                    if (input.getMousePosition().distanceTo(new Point(BuyPanel.AIRPLANE_X, BuyPanel.Y)) < RECOGNIZED_DISTANCE && money - airplane_price >= 0) {
                        selectProduct = 3;
                    }
                }
            }
            // if a tank is selected, check if it is a valid position.If so, place the tower and deduct the money.
            else if (!isBlocked && input.wasPressed(MouseButtons.LEFT) && !isPlaced && !overlapBuyPanel && !overlapStatusPanel && selectProduct != AIRPLANE) {
                if (selectProduct == TANK) {
                    towers.add(new Tank(mousePosition));
                    selectProduct = UNSELECTED;
                    setMoney(money - tank_price);

                } else if (selectProduct == SUPERTANK) {
                        towers.add(new SuperTank(mousePosition));
                        selectProduct = UNSELECTED;
                        setMoney(money - supertank_price);
                    }
                }
            //if an airplane is selected
            else if (selectProduct == AIRPLANE && input.wasPressed(MouseButtons.LEFT)) {
                airplanes.add(new Airplane(mousePosition));
                selectProduct = UNSELECTED;
                setMoney(money - airplane_price);
            }
            //if any products is selected and placed at valid position, draw corresponding image
            //Otherwise, cancel selection by setting to initial value
            else if (selectProduct != UNSELECTED && !isBlocked && !overlapBuyPanel) {
                if (selectProduct == TANK) {
                    Tank tank  = new Tank(mousePosition);
                    tank.drawTower();
                } else if (selectProduct == SUPERTANK) {
                    SuperTank super_tank  = new SuperTank(mousePosition);
                    super_tank.drawTower();
                }else if (selectProduct == AIRPLANE) {
                    Image image = new Image(BuyPanel.AIR_PLANE_IMAGE);
                    image.draw(mousePosition.x, mousePosition.y);
                }
                //cancel selection
                if (input.wasPressed(MouseButtons.RIGHT)) {
                    selectProduct = UNSELECTED;
                }
            }
        } catch (Exception NullPointerException) {
                //avoid raise exception for initial null pointers
        }
    }

    /**
     * check wave status and select status and update status on status panel
     * let PLACING(select status) has the priority over wave status
     */
    private void updateStatus(){
        //check wave status
        if (waveStarted) {
            status = Status.WAVE_IN_PROGRESS;
        } else {status = Status.AWAITING_START; }

        //check select status
        if (selectProduct != 0) {
            status = Status.PLACING; }
        if (waves.size() <= currentEvent && currentLevel >= levels.length){
            status = Status.WINNER;
        }
    }

    /**
     * update level if all waves are processed
     */
    private void updateLevel(){
        if (currentWave >= waves.size()){
            currentLevel ++;

            //if there is still remaining level
            if(currentLevel < levels.length){
                map = new TiledMap(levels[currentLevel]);
                loadWave();
                resetGame();
                //clean all elements in last game
                slicers.clear();
                towers.clear();
                projectiles.clear();
            }
        }
    }

    /**
     * Update both ended waves(reset related variables) and waves in progress(spawn slicers)
     */
    private void updateWave(){
        if(waveStarted){
            Wave wave = waves.get(currentWave);
            int totalWaveEvent = wave.getWaveEvent().size();

            //wave ended
            if(currentEvent >= totalWaveEvent && noSlicerOnMap()){
                waveStarted = false;
                currentWave ++;
                currentEvent = ZERO;
                slicers.clear();
            }
            //wave in progress
            else if (currentEvent < totalWaveEvent){
                Event event = wave.getWaveEvent().get(currentEvent);

                // Start the event by setting startTime as current timer and endTime as calculated result corresponding to each event
                if(!event.isEventStarted()){
                    event.start();
                }
                //ignore delay event and spawn slicers for spawn event
                else if (event instanceof SpawnEvent){
                    SpawnEvent sp_vent = (SpawnEvent) event;

                    if(sp_vent.getSpawnCount() < (sp_vent.getSpawnNum())){
                        // check if the time gap equals to the spawn delay time
                        if((timer - event.getStartTime())/(sp_vent.getSpawnDelay()*60/1000) == sp_vent.getSpawnCount()){

                            // spawn slicers
                            String spawnType = sp_vent.getSpawnType();
                            spawnSlicers(spawnType);

                            // update spawnCount
                            int currentSpawnCount = sp_vent.getSpawnCount();
                            currentSpawnCount ++;
                            sp_vent.setSpawnCount(currentSpawnCount);
                        }
                    }
                }
                // update currentEvent
                if(timer >= event.getEndTime()){
                    currentEvent ++;
                }
            }
        }
    }

    /**
     * Check if there is no slicer on map
     * @return true if no slicers left
     */
    private boolean noSlicerOnMap() {
        for(Slicer slicer : slicers){
            if(slicer.onMap()){
                return false;
            }
        }
        return true;
    }

    /**
     * Spawn slicers in SpawnEvent
     * @param spawnType type of slicers to spawn
     */
    private void spawnSlicers(String spawnType){
        Slicer SlicerToSpawn = null;
        //System.out.println(spawnType);
        if(spawnType.equals("slicer")){
            SlicerToSpawn = new RegularSlicer(map.getAllPolylines().get(0));
        }
        else if(spawnType.equals("superslicer")){
            SlicerToSpawn = new SuperSlicer(map.getAllPolylines().get(0));
        }
        else if(spawnType.equals("megaslicer")){
            SlicerToSpawn = new MegaSlicer(map.getAllPolylines().get(0));
        }
        else if(spawnType.equals("apexslicer")){
            SlicerToSpawn = new ApexSlicer(map.getAllPolylines().get(0));
        }
        slicers.add(SlicerToSpawn);
    }

    /**
     * Increases the timescale
     */
    private void increaseTimescale() {
        timescale++;
    }

    /**
     * Decreases the timescale but doesn't go below the base timescale
     */
    private void decreaseTimescale() {
        if (timescale > INITIAL_TIMESCALE) {
            timescale--;
        }
    }

    /**
     * Add reward money if one slicer is eliminated
     * @param slicer eliminated slicer
     */
    public static void addReward(Slicer slicer){
        int reward = slicer.getReward();
        money += reward;
    }

    //getters and setter for accessing and modifying variables in other classes
    public static int getHEIGHT() {return HEIGHT; }
    public static int getWIDTH() { return WIDTH; }
    public static int getTimer() { return timer; }
    public static int getTimescale() { return timescale; }
    public static ArrayList<Slicer> getSlicers() {return slicers;}
    public static ArrayList<Projectile> getProjectiles(){return projectiles;}
    public static ArrayList<Explosive> getExplosives() {
        return explosives;
    }
    public static int getMoney() {return money; }
    public static void setMoney(int money) {ShadowDefend.money = money; }
    public static int getLives() {return lives;}
    public static void setLives(int lives) {
        ShadowDefend.lives = lives;
    }

}


