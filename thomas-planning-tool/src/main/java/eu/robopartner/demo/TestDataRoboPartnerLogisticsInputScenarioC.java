/**
 * 
 */
package eu.robopartner.demo;

/**
 * @author Spyros
 *
 */
public class TestDataRoboPartnerLogisticsInputScenarioC {
//    
//    private Shopfloor shopfloor = null;
//    private RoboPartnerDataModelFactory aRoboPartnerDataModelFactory = null;
//    private Schedule schedule = null;
//    private List<IMAUResource> availableImauResources=null;
//    
//    public Schedule getSchedule() {
//        return schedule;
//    }
//
//    public Shopfloor getShopfloor() {
//        return shopfloor; 
//    }
//    
//    //TODO other inputs
//
//    /**
//     * 
//     */
//    public TestDataRoboPartnerLogisticsInputScenarioC(RoboPartnerDataModelFactory aRoboPartnerDataModelFactory) {
//        super();
//        this.aRoboPartnerDataModelFactory = aRoboPartnerDataModelFactory;
//        this.createDemoData();
//    }
//
//    private void createDemoData() {
//
//        DemoValuesCreator demoValCrtr = new DemoValuesCreator(aRoboPartnerDataModelFactory);
//        Shopfloor theShopfloor = demoValCrtr.createShopfloor("Car Manufacturing Shopfloor", "Demo Car Manufacturing Shopfloor", "Automatically generated to create demo values");
//        this.schedule = demoValCrtr.createSchedule("Demo Schedule", "The Demo Schudule", "A Schedule created for demo purposes");
//        // Create Station
//
//        ConsumableCategory aScrewsConsumableCategory1 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory1.setHasName("Wheel Stud's screw (MCV)");
//        ConsumableCategory aScrewsConsumableCategory2 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory2.setHasName("Clips (LINEA)");
//        ConsumableCategory aScrewsConsumableCategory3 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory3.setHasName("Clips (MCV)");
//        ConsumableCategory aScrewsConsumableCategory4 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory4.setHasName("Wheel stud;s screw (LINEA)");
//        ConsumableCategory aScrewsConsumableCategory5 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory5.setHasName("Turbo flexible Brake Hose");
//        ConsumableCategory aScrewsConsumableCategory6 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory6.setHasName("Turbo Bracket 1");
//        ConsumableCategory aScrewsConsumableCategory7 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory7.setHasName("Turbo Bracket Nut");
//        ConsumableCategory aScrewsConsumableCategory8 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory8.setHasName("Abs sensor");
//        ConsumableCategory aScrewsConsumableCategory9 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory9.setHasName("Turbo Braket 2");
//        ConsumableCategory aScrewsConsumableCategory10 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory10.setHasName("Right Abs sensor");
//        ConsumableCategory aScrewsConsumableCategory11 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory11.setHasName("Left Abs (MCV)");
//        ConsumableCategory aScrewsConsumableCategory12 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory12.setHasName("Turbo Abs sensor(MCV)");
//        ConsumableCategory aScrewsConsumableCategory13 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory13.setHasName("Screw of Abs sensor");
//        ConsumableCategory aScrewsConsumableCategory14 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory14.setHasName("Rigit Brake Hose");
//        ConsumableCategory aScrewsConsumableCategory15 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory15.setHasName("Ring (MCV, LINEA)");
//        ConsumableCategory aScrewsConsumableCategory16 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory16.setHasName("Brake Cable");
//        ConsumableCategory aScrewsConsumableCategory17 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory17.setHasName("Brake plates screw");
//        ConsumableCategory aScrewsConsumableCategory18 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory18.setHasName("Fuso screw");
//        ConsumableCategory aScrewsConsumableCategory19 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory19.setHasName("Heat sheet");
//        ConsumableCategory aScrewsConsumableCategory20 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory20.setHasName("Bottom Hun Liner");
//        ConsumableCategory aScrewsConsumableCategory21 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory21.setHasName("Nut");
//        ConsumableCategory aScrewsConsumableCategory22 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory22.setHasName("Bottom Hunb Spacer");
//        ConsumableCategory aScrewsConsumableCategory23 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory23.setHasName("Wheel hub cap");
//        ConsumableCategory aScrewsConsumableCategory24 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory24.setHasName("Drum Brake screw");
//        ConsumableCategory aScrewsConsumableCategory25 = aRoboPartnerDataModelFactory.createConsumableCategory();
//        aScrewsConsumableCategory25.setHasName("Brake Clip screw");
//
//        RobotResource aRobot1 = demoValCrtr.createRobotResource("Racer Robot 1", "Comau Racer Robot 1", 370.0, 20000.0, Math.ceil((1400 * 2500) / (500.0 * 500.0)), (1000.0 / 500.0), Math.ceil(1400 / 500.0), Math.ceil(2500 / 500.0), (3000 / 500.0), 1, 50., 1.5);
//        RobotResource aRobot2 = demoValCrtr.createRobotResource("Racer Robot 2", "Comau Racer Robot 2", 370.0, 20000.0, Math.ceil((1400 * 2500) / (500.0 * 500.0)), (1000.0 / 500.0), Math.ceil(1400 / 500.0), Math.ceil(2500 / 500.0), (3000 / 500.0), 1, 50., 1.5);
//        RobotResource aRobot3 = demoValCrtr.createRobotResource("Racer Robot 3", "Comau Racer Robot 3", 370.0, 20000.0, Math.ceil((1400 * 2500) / (500.0 * 500.0)), (1000.0 / 500.0), Math.ceil(1400 / 500.0), Math.ceil(2500 / 500.0), (3000 / 500.0), 1, 50., 1.5);
//        RobotResource aRobot4 = demoValCrtr.createRobotResource("Racer Robot 4", "Comau Racer Robot 4", 370.0, 20000.0, Math.ceil((1400 * 2500) / (500.0 * 500.0)), (1000.0 / 500.0), Math.ceil(1400 / 500.0), Math.ceil(2500 / 500.0), (3000 / 500.0), 1, 50., 1.5);
//        RobotResource aRobot5 = demoValCrtr.createRobotResource("Racer Robot 5", "Comau Racer Robot 5", 370.0, 20000.0, Math.ceil((1400 * 2500) / (500.0 * 500.0)), (1000.0 / 500.0), Math.ceil(1400 / 500.0), Math.ceil(2500 / 500.0), (3000 / 500.0), 1, 50., 1.5);
//        RobotResource aRobot6 = demoValCrtr.createRobotResource("Racer Robot 6", "Comau Racer Robot 6", 370.0, 20000.0, Math.ceil((1400 * 2500) / (500.0 * 500.0)), (1000.0 / 500.0), Math.ceil(1400 / 500.0), Math.ceil(2500 / 500.0), (3000 / 500.0), 1, 50., 1.5);
//        RobotResource aRobot7 = demoValCrtr.createRobotResource("Racer Robot 7", "Comau Racer Robot 7", 370.0, 20000.0, Math.ceil((1400 * 2500) / (500.0 * 500.0)), (1000.0 / 500.0), Math.ceil(1400 / 500.0), Math.ceil(2500 / 500.0), (3000 / 500.0), 1, 50., 1.5);
//        RobotResource aRobot8 = demoValCrtr.createRobotResource("Racer Robot 8", "Comau Racer Robot 8", 370.0, 20000.0, Math.ceil((1400 * 2500) / (500.0 * 500.0)), (1000.0 / 500.0), Math.ceil(1400 / 500.0), Math.ceil(2500 / 500.0), (3000 / 500.0), 1, 50., 1.5);
//        
//        // TODO Define the actual coordinates
//        Coordinates3D aRobot1Position = demoValCrtr.createCoordinates3D(8.0, 0.0, 11.5);
//        Coordinates3D aRobot2Position = demoValCrtr.createCoordinates3D(8.0, 0.0, 16.0);
//       	Coordinates3D aRobot3Position = demoValCrtr.createCoordinates3D(17.0, 0.0, 20.0);
//       	Coordinates3D aRobot4Position = demoValCrtr.createCoordinates3D(19.0, 0.0, 24.0);
//        Coordinates3D aRobot5Position = demoValCrtr.createCoordinates3D(22.0, 0.0, 28.0);
//       	Coordinates3D aRobot6Position = demoValCrtr.createCoordinates3D(16.0, 0.0, 19.0);
//       	Coordinates3D aRobot7Position = demoValCrtr.createCoordinates3D(28.0, 0.0, 17.0);
//       	Coordinates3D aRobot8Position = demoValCrtr.createCoordinates3D(32.0, 0.0, 26.0);
//       	
//       	StationPosition aRobot1Station1Position = demoValCrtr.createRobotStationPosition(aRobot1Position, aRobot1);
//       	StationPosition aRobot2Station2Position = demoValCrtr.createRobotStationPosition(aRobot2Position, aRobot2);
//       	StationPosition aRobot3Station3Position = demoValCrtr.createRobotStationPosition(aRobot3Position, aRobot3);
//       	StationPosition aRobot4Station4Position = demoValCrtr.createRobotStationPosition(aRobot4Position, aRobot4);
//       	StationPosition aRobot5Station5Position = demoValCrtr.createRobotStationPosition(aRobot5Position, aRobot5);
//       	StationPosition aRobot6Station6Position = demoValCrtr.createRobotStationPosition(aRobot6Position, aRobot6);
//       	StationPosition aRobot7Station7Position = demoValCrtr.createRobotStationPosition(aRobot7Position, aRobot7);
//       	StationPosition aRobot8Station8Position = demoValCrtr.createRobotStationPosition(aRobot8Position, aRobot8);
//       	
//       	List<StationPosition> aRobot1Station1Positions = new ArrayList<StationPosition>(); 
//        List<StationPosition> aRobot2Station2Positions = new ArrayList<StationPosition>();
//        List<StationPosition> aRobot3Station3Positions = new ArrayList<StationPosition>();
//        List<StationPosition> aRobot4Station4Positions = new ArrayList<StationPosition>();
//        List<StationPosition> aRobot5Station5Positions = new ArrayList<StationPosition>();
//        List<StationPosition> aRobot6Station6Positions = new ArrayList<StationPosition>();
//        List<StationPosition> aRobot7Station7Positions = new ArrayList<StationPosition>();
//        List<StationPosition> aRobot8Station8Positions = new ArrayList<StationPosition>();
//        
//        aRobot1Station1Positions.add(aRobot1Station1Position);
//        aRobot2Station2Positions.add(aRobot2Station2Position);
//        aRobot3Station3Positions.add(aRobot3Station3Position);
//        aRobot4Station4Positions.add(aRobot4Station4Position);
//        aRobot5Station5Positions.add(aRobot5Station5Position);
//        aRobot6Station6Positions.add(aRobot6Station6Position);
//        aRobot7Station7Positions.add(aRobot7Station7Position);
//        aRobot8Station8Positions.add(aRobot8Station8Position);
//        
//        HumanResource operator1 = demoValCrtr.createHumanResource("Operator 1", "Human Operator 1", 11.0, 200.0, Math.ceil((500 * 600) / (5000 * 500.0)), (500.0 / 500.0), Math.ceil(500 / 500.0), Math.ceil(600 / 500.0), Math.ceil(5000 / 500), 1, 75., 1.5);
//        HumanResource operator2 = demoValCrtr.createHumanResource("Operator 2", "Human Operator 2", 11.0, 200.0, Math.ceil((500 * 600) / (5000 * 500.0)), (500.0 / 500.0), Math.ceil(500 / 500.0), Math.ceil(600 / 500.0), Math.ceil(5000 / 500), 1, 75., 1.5);
//        HumanResource operator3 = demoValCrtr.createHumanResource("Operator 3", "Human Operator 3", 11.0, 200.0, Math.ceil((500 * 600) / (5000 * 500.0)), (500.0 / 500.0), Math.ceil(500 / 500.0), Math.ceil(600 / 500.0), Math.ceil(5000 / 500), 1, 75., 1.5);
//        HumanResource operator4 = demoValCrtr.createHumanResource("Operator 4", "Human Operator 4", 11.0, 200.0, Math.ceil((500 * 600) / (5000 * 500.0)), (500.0 / 500.0), Math.ceil(500 / 500.0), Math.ceil(600 / 500.0), Math.ceil(5000 / 500), 1, 75., 1.5);
//        HumanResource operator5 = demoValCrtr.createHumanResource("Operator 5", "Human Operator 5", 11.0, 200.0, Math.ceil((500 * 600) / (5000 * 500.0)), (500.0 / 500.0), Math.ceil(500 / 500.0), Math.ceil(600 / 500.0), Math.ceil(5000 / 500), 1, 75., 1.5);
//        HumanResource operator6 = demoValCrtr.createHumanResource("Operator 6", "Human Operator 6", 11.0, 200.0, Math.ceil((500 * 600) / (5000 * 500.0)), (500.0 / 500.0), Math.ceil(500 / 500.0), Math.ceil(600 / 500.0), Math.ceil(5000 / 500), 1, 75., 1.5);
//        HumanResource operator7 = demoValCrtr.createHumanResource("Operator 7", "Human Operator 7", 11.0, 200.0, Math.ceil((500 * 600) / (5000 * 500.0)), (500.0 / 500.0), Math.ceil(500 / 500.0), Math.ceil(600 / 500.0), Math.ceil(5000 / 500), 1, 75., 1.5);
//        HumanResource operator8 = demoValCrtr.createHumanResource("Operator 8", "Human Operator 8", 11.0, 200.0, Math.ceil((500 * 600) / (5000 * 500.0)), (500.0 / 500.0), Math.ceil(500 / 500.0), Math.ceil(600 / 500.0), Math.ceil(5000 / 500), 1, 75., 1.5);
//        
//        // TODO Define the actual coordinates
//        Coordinates3D aHuman1Position = demoValCrtr.createCoordinates3D(12.0, 0.0, 18.0);
//        Coordinates3D aHuman2Position = demoValCrtr.createCoordinates3D(12.0, 0.0, 20.0);
//       	Coordinates3D aHuman3Position = demoValCrtr.createCoordinates3D(8.0, 0.0, 15.0);
//       	Coordinates3D aHuman4Position = demoValCrtr.createCoordinates3D(16.0, 0.0, 21.0);
//        Coordinates3D aHuman5Position = demoValCrtr.createCoordinates3D(25.0, 0.0, 30.0);
//       	Coordinates3D aHuman6Position = demoValCrtr.createCoordinates3D(12.0, 0.0, 22.0);
//       	Coordinates3D aHuman7Position = demoValCrtr.createCoordinates3D(19.0, 0.0, 26.0);
//       	Coordinates3D aHuman8Position = demoValCrtr.createCoordinates3D(20.0, 0.0, 28.0);
//
//       	StationPosition aHuman1Station1Position = demoValCrtr.createHumanStationPosition(aHuman1Position, operator1);
//        StationPosition aHuman2Station2Position = demoValCrtr.createHumanStationPosition(aHuman2Position, operator2);
//       	StationPosition aHuman3Station3Position = demoValCrtr.createHumanStationPosition(aHuman3Position, operator3);
//       	StationPosition aHuman4Station4Position = demoValCrtr.createHumanStationPosition(aHuman4Position, operator4);
//      	StationPosition aHuman5Station5Position = demoValCrtr.createHumanStationPosition(aHuman5Position, operator5);
//       	StationPosition aHuman6Station6Position = demoValCrtr.createHumanStationPosition(aHuman6Position, operator6);
//       	StationPosition aHuman7Station7Position = demoValCrtr.createHumanStationPosition(aHuman7Position, operator7);
//       	StationPosition aHuman8Station8Position = demoValCrtr.createHumanStationPosition(aHuman8Position, operator8);
//       	
//       	List<StationPosition> aHuman1Station1Positions = new ArrayList<StationPosition>(); 
//        List<StationPosition> aHuman2Station2Positions = new ArrayList<StationPosition>();
//        List<StationPosition> aHuman3Station3Positions = new ArrayList<StationPosition>();
//        List<StationPosition> aHuman4Station4Positions = new ArrayList<StationPosition>();
//        List<StationPosition> aHuman5Station5Positions = new ArrayList<StationPosition>();
//        List<StationPosition> aHuman6Station6Positions = new ArrayList<StationPosition>();
//        List<StationPosition> aHuman7Station7Positions = new ArrayList<StationPosition>();
//        List<StationPosition> aHuman8Station8Positions = new ArrayList<StationPosition>();
//        
//        aHuman1Station1Positions.add(aHuman1Station1Position);
//        aHuman2Station2Positions.add(aHuman2Station2Position);
//        aHuman3Station3Positions.add(aHuman3Station3Position);
//        aHuman4Station4Positions.add(aHuman4Station4Position);
//        aHuman5Station5Positions.add(aHuman5Station5Position);
//        aHuman6Station6Positions.add(aHuman6Station6Position);
//        aHuman7Station7Positions.add(aHuman7Station7Position);
//        aHuman8Station8Positions.add(aHuman8Station8Position);
//       	
//        // Station 1 participates in scheduling only. (In demo)
//        // Length, height, width in cm 
//        // TODO define the actual weight
//        WarehouseBox box1Station1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory1, 15., 20., 30., 10., 300.);
//        WarehouseBox box2Station1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory2, 15., 20., 30., 10., 500.);
//        WarehouseBox box3Station1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory3, 15., 20., 30., 10., 500.);
//        WarehouseBox box4Station1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory4, 15., 20., 30., 10., 250.);
//        WarehouseBox box1Station2 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory5, 15., 20., 30., 10., 200.);
//        WarehouseBox box2Station2 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory6, 15., 20., 30., 10., 200.);
//        WarehouseBox box3Station2 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory7, 15., 20., 30., 10., 500.);
//        WarehouseBox box4Station2 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory8, 15., 20., 30., 10., 150.);
//        WarehouseBox box5Station2 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory9, 15., 20., 30., 10., 200.);
//        WarehouseBox box1Station3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory10, 30., 40., 60., 10., 150.);
//        WarehouseBox box2Station3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory11, 30., 40., 60., 10., 150.);
//        WarehouseBox box3Station3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory12, 30., 40., 60., 10., 150.);
//        WarehouseBox box4Station3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory13, 15., 20., 30., 10., 2400.);
//        WarehouseBox box5Station3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory14, 9., 43., 51., 10., 25.);
//        WarehouseBox box1Station4 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory15, 15., 20., 30., 10., 2200.);
//        WarehouseBox box2Station4 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory16, 15., 30., 40., 10., 200.);
//        WarehouseBox box1Station5 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory17, 15., 20., 30., 10., 500.);
//        WarehouseBox box2Station5 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory18, 15., 20., 30., 10., 150.);
//        WarehouseBox box3Station5 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory19, 15., 30., 40., 10., 60.);
//        WarehouseBox box1Station6 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory20, 15., 20., 30., 10., 240.);
//        WarehouseBox box2Station6 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory21, 15., 20., 30., 10., 300.);
//        WarehouseBox box3Station6 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory22, 15., 30., 40., 10., 144.);
//        WarehouseBox box1Station7 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory23, 15., 30., 40., 10., 400.);
//        WarehouseBox box2Station7 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory24, 15., 20., 30., 10., 1250.);
//        WarehouseBox box1Station8 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory25, 15., 20., 30., 10., 250.);
//        
//        Map<String, Integer> boxesCurrentQuantity = new HashMap<String, Integer>();
//        //TODO Rename the boxes properly
//        // This Current Quantity is the initial (max capacity) of the boxes
//        boxesCurrentQuantity.put("Box 1", 300);
//        boxesCurrentQuantity.put("Box 2", 500);
//        boxesCurrentQuantity.put("Box 3", 500);
//        boxesCurrentQuantity.put("Box 4", 250);
//        boxesCurrentQuantity.put("Box 5", 200);
//        boxesCurrentQuantity.put("Box 6", 200);
//        boxesCurrentQuantity.put("Box 7", 500);
//        boxesCurrentQuantity.put("Box 8", 150);
//        boxesCurrentQuantity.put("Box 9", 200);
//        boxesCurrentQuantity.put("Box 10", 150);
//        boxesCurrentQuantity.put("Box 11", 150);
//        boxesCurrentQuantity.put("Box 12", 150);
//        boxesCurrentQuantity.put("Box 13", 2400);
//        boxesCurrentQuantity.put("Box 14", 25);
//        boxesCurrentQuantity.put("Box 15", 2200);
//        boxesCurrentQuantity.put("Box 16", 200);
//        boxesCurrentQuantity.put("Box 17", 500);
//        boxesCurrentQuantity.put("Box 18", 150);
//        boxesCurrentQuantity.put("Box 19", 60);
//        boxesCurrentQuantity.put("Box 20", 240);
//        boxesCurrentQuantity.put("Box 21", 300);
//        boxesCurrentQuantity.put("Box 22", 144);
//        boxesCurrentQuantity.put("Box 23", 400);
//        boxesCurrentQuantity.put("Box 24", 1250);
//        boxesCurrentQuantity.put("Box 25", 250);
//        
//        Map<String, Integer> boxesLowerLimit = new HashMap<String, Integer>();
//        //TODO Define actual Lower Limit
//        boxesLowerLimit.put("Box 1", 32);
//        boxesLowerLimit.put("Box 2", 10);
//        boxesLowerLimit.put("Box 3", 32);
//        boxesLowerLimit.put("Box 4", 32);
//        boxesLowerLimit.put("Box 5", 10);
//        boxesLowerLimit.put("Box 6", 10);
//        boxesLowerLimit.put("Box 7", 10);
//        boxesLowerLimit.put("Box 8", 10);
//        boxesLowerLimit.put("Box 9", 10);
//        boxesLowerLimit.put("Box 10", 10);
//        boxesLowerLimit.put("Box 11", 10);
//        boxesLowerLimit.put("Box 12", 10);
//        boxesLowerLimit.put("Box 13", 10);
//        boxesLowerLimit.put("Box 14", 4);
//        boxesLowerLimit.put("Box 15", 10);
//        boxesLowerLimit.put("Box 16", 10);
//        boxesLowerLimit.put("Box 17", 10);
//        boxesLowerLimit.put("Box 18", 10);
//        boxesLowerLimit.put("Box 19", 5);
//        boxesLowerLimit.put("Box 20", 10);
//        boxesLowerLimit.put("Box 21", 10);
//        boxesLowerLimit.put("Box 22", 10);
//        boxesLowerLimit.put("Box 23", 10);
//        boxesLowerLimit.put("Box 24", 10);
//        boxesLowerLimit.put("Box 25", 10);
//        
//        //TODO Check if needed
//        Map<String, Integer> boxesUpperLimit = new HashMap<String, Integer>();   
//        
//        double standardStationPositionHeight = 0.0;
//        
//        // TODO Define the actual coordinates
//        Coordinates3D box1PositionCoordinatesStation1 = demoValCrtr.createCoordinates3D(8.0, standardStationPositionHeight, 11.5);
//        Coordinates3D box2PositionCoordinatesStation1 = demoValCrtr.createCoordinates3D(8.0, standardStationPositionHeight, 16.0);
//        Coordinates3D box3PositionCoordinatesStation1 = demoValCrtr.createCoordinates3D(17.0, standardStationPositionHeight, 20.10);
//        Coordinates3D box4PositionCoordinatesStation1 = demoValCrtr.createCoordinates3D(19.0, standardStationPositionHeight, 24.0);
//        Coordinates3D box1PositionCoordinatesStation2 = demoValCrtr.createCoordinates3D(22.0, standardStationPositionHeight, 28.0);
//        Coordinates3D box2PositionCoordinatesStation2 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box3PositionCoordinatesStation2 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box4PositionCoordinatesStation2 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box5PositionCoordinatesStation2 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box1PositionCoordinatesStation3 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box2PositionCoordinatesStation3 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box3PositionCoordinatesStation3 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box4PositionCoordinatesStation3 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box5PositionCoordinatesStation3 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box1PositionCoordinatesStation4 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box2PositionCoordinatesStation4 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box1PositionCoordinatesStation5 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box2PositionCoordinatesStation5 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box3PositionCoordinatesStation5 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box1PositionCoordinatesStation6 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box2PositionCoordinatesStation6 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box3PositionCoordinatesStation6 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box1PositionCoordinatesStation7 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box2PositionCoordinatesStation7 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        Coordinates3D box1PositionCoordinatesStation8 = demoValCrtr.createCoordinates3D(16.0, standardStationPositionHeight, 19.0);
//        
//        List<BoxPosition> boxesPositionStation1 = new ArrayList<BoxPosition>(); 
//        List<BoxPosition> boxesPositionStation2 = new ArrayList<BoxPosition>();
//        List<BoxPosition> boxesPositionStation3 = new ArrayList<BoxPosition>();
//        List<BoxPosition> boxesPositionStation4 = new ArrayList<BoxPosition>();
//        List<BoxPosition> boxesPositionStation5 = new ArrayList<BoxPosition>();
//        List<BoxPosition> boxesPositionStation6 = new ArrayList<BoxPosition>();
//        List<BoxPosition> boxesPositionStation7 = new ArrayList<BoxPosition>();
//        List<BoxPosition> boxesPositionStation8 = new ArrayList<BoxPosition>();
//
//        BoxPosition box1PositionStation1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory1, box1PositionCoordinatesStation1,box1Station1);
//        BoxPosition box2PositionStation1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory2, box2PositionCoordinatesStation1,box2Station1);
//        BoxPosition box3PositionStation1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory3, box3PositionCoordinatesStation1,box3Station1);
//        BoxPosition box4PositionStation1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory4, box4PositionCoordinatesStation1,box4Station1);
//        BoxPosition box1PositionStation2 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory5, box1PositionCoordinatesStation2,box1Station2);
//        BoxPosition box2PositionStation2 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory6, box2PositionCoordinatesStation2,box2Station2);
//        BoxPosition box3PositionStation2 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory7, box3PositionCoordinatesStation2,box3Station2);
//        BoxPosition box4PositionStation2 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory8, box4PositionCoordinatesStation2,box4Station2);
//        BoxPosition box5PositionStation2 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory9, box5PositionCoordinatesStation2,box5Station2);
//        BoxPosition box1PositionStation3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory10,box1PositionCoordinatesStation3,box1Station3);
//        BoxPosition box2PositionStation3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory11,box2PositionCoordinatesStation3,box2Station3);
//        BoxPosition box3PositionStation3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory12,box3PositionCoordinatesStation3,box3Station3);
//        BoxPosition box4PositionStation3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory13,box4PositionCoordinatesStation3,box4Station3);
//        BoxPosition box5PositionStation3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory14,box5PositionCoordinatesStation3,box5Station3);
//        BoxPosition box1PositionStation4 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory15,box1PositionCoordinatesStation4,box1Station4);
//        BoxPosition box2PositionStation4 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory16,box2PositionCoordinatesStation4,box2Station4);
//        BoxPosition box1PositionStation5 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory17,box1PositionCoordinatesStation5,box1Station5);
//        BoxPosition box2PositionStation5 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory18,box2PositionCoordinatesStation5,box2Station5);
//        BoxPosition box3PositionStation5 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory19,box3PositionCoordinatesStation5,box3Station5);
//        BoxPosition box1PositionStation6 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory20,box1PositionCoordinatesStation6,box1Station6);
//        BoxPosition box2PositionStation6 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory21,box2PositionCoordinatesStation6,box2Station6);
//        BoxPosition box3PositionStation6 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory22,box3PositionCoordinatesStation6,box3Station6);
//        BoxPosition box1PositionStation7 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory23,box1PositionCoordinatesStation7,box1Station7);
//        BoxPosition box2PositionStation7 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory24,box2PositionCoordinatesStation7,box2Station7);
//        BoxPosition box1PositionStation8 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory25,box1PositionCoordinatesStation8,box1Station8);
//       
//        // Boxes positions for station 1
//        boxesPositionStation1.add(box1PositionStation1);
//        boxesPositionStation1.add(box2PositionStation1);
//        boxesPositionStation1.add(box3PositionStation1);
//        boxesPositionStation1.add(box4PositionStation1);
//        
//        //Boxes positions for station 2
//        boxesPositionStation2.add(box1PositionStation2);
//        boxesPositionStation2.add(box2PositionStation2);
//        boxesPositionStation2.add(box3PositionStation2);
//        boxesPositionStation2.add(box4PositionStation2);
//        boxesPositionStation2.add(box5PositionStation2);
//        
//        //Boxes positions for station 3
//        boxesPositionStation3.add(box1PositionStation3);
//        boxesPositionStation3.add(box2PositionStation3);
//        boxesPositionStation3.add(box3PositionStation3);
//        boxesPositionStation3.add(box4PositionStation3);
//        boxesPositionStation3.add(box5PositionStation3);
//        
//        //Boxes positions for station 4
//        boxesPositionStation4.add(box1PositionStation4);
//        boxesPositionStation4.add(box2PositionStation4);
//        
//       //Boxes positions for station 5
//        boxesPositionStation5.add(box1PositionStation5);
//        boxesPositionStation5.add(box2PositionStation5);
//        boxesPositionStation5.add(box3PositionStation5);
//        
//       //Boxes positions for station 6
//        boxesPositionStation6.add(box1PositionStation6);
//        boxesPositionStation6.add(box2PositionStation6);
//        boxesPositionStation6.add(box3PositionStation6);
//        
//       //Boxes positions for station 7
//        boxesPositionStation7.add(box1PositionStation7);
//        boxesPositionStation7.add(box2PositionStation7);
//        
//       //Boxes positions for station 8
//        boxesPositionStation8.add(box1PositionStation8);
//        
//        Station theStation1 = demoValCrtr.createStation("RAAL_op30 station", "RAAL_op30", aRobot1Station1Positions, aHuman1Station1Positions, boxesPositionStation1);
//        Station theStation2 = demoValCrtr.createStation("RAAL_op50 station", "RAAL_op50", aRobot2Station2Positions, aHuman2Station2Positions, boxesPositionStation2);
//        Station theStation3 = demoValCrtr.createStation("RAAL_op60 station", "RAAL_op60", aRobot3Station3Positions, aHuman3Station3Positions, boxesPositionStation3);
//        Station theStation4 = demoValCrtr.createStation("RAAL_op70 station", "RAAL_op70", aRobot4Station4Positions, aHuman4Station4Positions, boxesPositionStation4);
//        Station theStation5 = demoValCrtr.createStation("RWAL_op10 station", "RWAL_op10", aRobot5Station5Positions, aHuman5Station5Positions, boxesPositionStation5);
//        Station theStation6 = demoValCrtr.createStation("RWAL_op20 station", "RWAL_op20", aRobot6Station6Positions, aHuman6Station6Positions, boxesPositionStation6);
//        Station theStation7 = demoValCrtr.createStation("RWAL_op30 station", "RWAL_op30", aRobot7Station7Positions, aHuman7Station7Positions, boxesPositionStation7);
//        Station theStation8 = demoValCrtr.createStation("RWAL_op40 station", "RWAL_op40", aRobot8Station8Positions, aHuman8Station8Positions, boxesPositionStation8);
//        	
//        AssemblyLine anAssemblyLine1 = demoValCrtr.createAssemblyLine("Demo Rear Axle Assembly Station", "A Demo Assembly Station", "A Demo Assembly Station created automatically");
//        AssemblyLine anAssemblyLine2 = demoValCrtr.createAssemblyLine("Demo Real Wheel Assembly Station", "A Demo Assembly Station", "A Demo Assembly Station created automatically");
//        anAssemblyLine1.addHasStation(theStation1);
//        anAssemblyLine1.addHasStation(theStation2);
//        anAssemblyLine1.addHasStation(theStation3);
//        anAssemblyLine1.addHasStation(theStation4);
//        anAssemblyLine2.addHasStation(theStation5);
//        anAssemblyLine2.addHasStation(theStation6);
//        anAssemblyLine2.addHasStation(theStation7);
//        anAssemblyLine2.addHasStation(theStation8);
//
//        theShopfloor.addHasAssemblyLine(anAssemblyLine1);
//        theShopfloor.addHasAssemblyLine(anAssemblyLine2);
//
//        // Create Warehouses
//
//        double standardShelfHeight = 1.0;
//
//        //Name box_number of shelf in the market where the box is stored_number of market(the box is stored)
//        // TODO Define the actual weight (same as Warehouse boxes of stations created above)
//        WarehouseBox box1Market1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory1, 15., 20., 30., 10., 300.);
//        WarehouseBox box3Market1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory2, 15., 20., 30., 10., 500.);
//        WarehouseBox box4Market1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory3, 15., 20., 30., 10., 500.);
//        WarehouseBox box2Market1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory4, 15., 20., 30., 10., 250.);
//        WarehouseBox box1Market3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory5, 15., 20., 30., 10., 200.);
//        WarehouseBox box4Market2 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory6, 15., 20., 30., 10., 200.);
//        WarehouseBox box12Market1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory7, 15., 20., 30., 10., 500.);
//        WarehouseBox box2Market3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory8, 15., 20., 30., 10., 150.);
//        WarehouseBox box1Market2 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory9, 15., 20., 30., 10., 200.);
//        WarehouseBox box6Market1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory10, 30., 40., 60., 10., 150.);
//        WarehouseBox box5Market1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory11, 30., 40., 60., 10., 150.);
//        WarehouseBox box3Market3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory12, 30., 40., 60., 10., 150.);
//        WarehouseBox box7Market1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory13, 15., 20., 30., 10., 2400.);
//        WarehouseBox box1Market4 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory14, 9., 43., 51., 10., 25.);
//        WarehouseBox box8Market1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory15, 15., 20., 30., 10., 2200.);
//        WarehouseBox box4Market3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory16, 15., 30., 40., 10., 200.);
//        WarehouseBox box9Market1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory17, 15., 20., 30., 10., 500.);
//        WarehouseBox box8Market3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory18, 15., 20., 30., 10., 150.);
//        WarehouseBox box10Market1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory19, 15., 30., 40., 10., 60.);
//        WarehouseBox box5Market3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory20, 15., 20., 30., 10., 240.);
//        WarehouseBox box6Market3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory21, 15., 20., 30., 10., 300.);
//        WarehouseBox box11Market1 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory22, 15., 30., 40., 10., 144.);
//        WarehouseBox box3Market2= demoValCrtr.createWarehouseBox(aScrewsConsumableCategory23, 15., 30., 40., 10., 400.);
//        WarehouseBox box7Market3 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory24, 15., 20., 30., 10., 1250.);
//        WarehouseBox box2Market2 = demoValCrtr.createWarehouseBox(aScrewsConsumableCategory25, 15., 20., 30., 10., 250.);
//        
//        // Get listis with the WarehouseBox(es) stored in each Warehouse
//        List<WarehouseBox> boxesWarehouse1 = new ArrayList<WarehouseBox>(); 
//        List<WarehouseBox> boxesWarehouse2 = new ArrayList<WarehouseBox>(); 
//        List<WarehouseBox> boxesWarehouse3 = new ArrayList<WarehouseBox>(); 
//        List<WarehouseBox> boxesWarehouse4 = new ArrayList<WarehouseBox>(); 
//        
//        boxesWarehouse1.add(box1Market1);
//        boxesWarehouse1.add(box2Market1);
//        boxesWarehouse1.add(box3Market1);
//        boxesWarehouse1.add(box4Market1);
//        boxesWarehouse1.add(box5Market1);
//        boxesWarehouse1.add(box6Market1);
//        boxesWarehouse1.add(box7Market1);
//        boxesWarehouse1.add(box8Market1);
//        boxesWarehouse1.add(box9Market1);
//        boxesWarehouse1.add(box10Market1);
//        boxesWarehouse1.add(box11Market1);
//        boxesWarehouse1.add(box12Market1);
//        
//        boxesWarehouse2.add(box1Market2);
//        boxesWarehouse2.add(box2Market2);
//        boxesWarehouse2.add(box3Market2);
//        boxesWarehouse2.add(box4Market2);
//        
//        boxesWarehouse3.add(box1Market3);
//        boxesWarehouse3.add(box2Market3);
//        boxesWarehouse3.add(box3Market3);
//        boxesWarehouse3.add(box4Market3);
//        boxesWarehouse3.add(box5Market3);
//        boxesWarehouse3.add(box6Market3);
//        boxesWarehouse3.add(box7Market3);
//        boxesWarehouse3.add(box8Market3);
//        
//        boxesWarehouse4.add(box1Market4);
//        
//        // TODO Define the actual coordinates (based on TOFAS layout)
//        Coordinates3D box1PositionCoordinatesWarehouse1 = demoValCrtr.createCoordinates3D(0.5, standardShelfHeight, 2.0);
//        Coordinates3D box3PositionCoordinatesWarehouse1 = demoValCrtr.createCoordinates3D(3.0, standardShelfHeight, 6.0);
//        Coordinates3D box4PositionCoordinatesWarehouse1 = demoValCrtr.createCoordinates3D(7.0, standardShelfHeight, 10.10);
//        Coordinates3D box2PositionCoordinatesWarehouse1 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box1PositionCoordinatesWarehouse3 = demoValCrtr.createCoordinates3D(13.0, standardShelfHeight, 18.0);
//        Coordinates3D box4PositionCoordinatesWarehouse2 = demoValCrtr.createCoordinates3D(6.0, standardShelfHeight, 12.0);
//        Coordinates3D box12PositionCoordinatesWarehous1 = demoValCrtr.createCoordinates3D(10.0, standardShelfHeight, 15.0);
//        Coordinates3D box2PositionCoordinatesWarehouse3 = demoValCrtr.createCoordinates3D(17.0, standardShelfHeight, 20.0);
//        Coordinates3D box1PositionCoordinatesWarehouse2 = demoValCrtr.createCoordinates3D(14.0, standardShelfHeight, 21.0);
//        Coordinates3D box6PositionCoordinatesWarehouse1 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box5PositionCoordinatesWarehouse1 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box3PositionCoordinatesWarehouse3 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box7PositionCoordinatesWarehouse1 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box1PositionCoordinatesWarehouse4 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box8PositionCoordinatesWarehouse1 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box4PositionCoordinatesWarehouse3= demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box9PositionCoordinatesWarehouse1 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box8PositionCoordinatesWarehouse3 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box10PositionCoordinatesWarehouse1 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box5PositionCoordinatesWarehouse3 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box6PositionCoordinatesWarehouse3 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box11PositionCoordinatesWarehouse1 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box3PositionCoordinatesWarehouse2 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box7PositionCoordinatesWarehouse3 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//        Coordinates3D box2PositionCoordinatesWarehouse2 = demoValCrtr.createCoordinates3D(15.0, standardShelfHeight, 15.0);
//
//        BoxPosition box1PositionWarehouse1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory1, box1PositionCoordinatesWarehouse1,box1Market1 );
//        BoxPosition box3PositionWarehouse1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory2, box3PositionCoordinatesWarehouse1,box3Market1 );
//        BoxPosition box4PositionWarehouse1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory3, box4PositionCoordinatesWarehouse1,box4Market1 );
//        BoxPosition box2PositionWarehouse1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory4, box2PositionCoordinatesWarehouse1,box2Market1 );
//        BoxPosition box1PositionWarehouse3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory5, box1PositionCoordinatesWarehouse3,box1Market3 );
//        BoxPosition box4PositionWarehouse2 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory6, box4PositionCoordinatesWarehouse2,box4Market2 );
//        BoxPosition box12PositionWarehouse1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory7, box12PositionCoordinatesWarehous1,box12Market1 );
//        BoxPosition box2PositionWarehouse3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory8, box2PositionCoordinatesWarehouse3,box2Market3 );
//        BoxPosition box1PositionWarehouse2 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory9, box1PositionCoordinatesWarehouse2,box1Market2 );
//        BoxPosition box6PositionWarehouse1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory10,box6PositionCoordinatesWarehouse1,box6Market1 );
//        BoxPosition box5PositionWarehouse1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory11,box5PositionCoordinatesWarehouse1,box5Market1 );
//        BoxPosition box3PositionWarehouse3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory12,box3PositionCoordinatesWarehouse3,box3Market3 );
//        BoxPosition box7PositionWarehouse1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory13,box7PositionCoordinatesWarehouse1,box7Market1 );
//        BoxPosition box1PositionWarehouse4 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory14,box1PositionCoordinatesWarehouse4,box1Market4 );
//        BoxPosition box8PositionWarehouse1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory15,box8PositionCoordinatesWarehouse1,box8Market1 );
//        BoxPosition box4PositionWarehouse3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory16,box4PositionCoordinatesWarehouse3,box4Market3 );
//        BoxPosition box9PositionWarehouse1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory17,box9PositionCoordinatesWarehouse1,box9Market1 );
//        BoxPosition box8PositionWarehouse3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory18,box8PositionCoordinatesWarehouse3,box8Market3 );
//        BoxPosition box10PositionWarehouse1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory19,box10PositionCoordinatesWarehouse1,box10Market1 );
//        BoxPosition box5PositionWarehouse3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory20,box5PositionCoordinatesWarehouse3,box5Market3 );
//        BoxPosition box6PositionWarehouse3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory21,box6PositionCoordinatesWarehouse3,box6Market3 );
//        BoxPosition box11PositionWarehouse1 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory22,box11PositionCoordinatesWarehouse1,box11Market1 );
//        BoxPosition box3PositionWarehouse2 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory23,box3PositionCoordinatesWarehouse2,box3Market2);
//        BoxPosition box7PositionWarehouse3 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory24,box7PositionCoordinatesWarehouse3,box7Market3 );
//        BoxPosition box2PositionWarehouse2 = demoValCrtr.createBoxPosition(aScrewsConsumableCategory25,box2PositionCoordinatesWarehouse2,box2Market2 );
//       
//        List<BoxPosition> boxesPositionWarehouse1 = new ArrayList<BoxPosition>(); 
//        List<BoxPosition> boxesPositionWarehouse2 = new ArrayList<BoxPosition>();
//        List<BoxPosition> boxesPositionWarehouse3 = new ArrayList<BoxPosition>();
//        List<BoxPosition> boxesPositionWarehouse4 = new ArrayList<BoxPosition>();
//        
//        boxesPositionWarehouse1.add(box1PositionWarehouse1);
//        boxesPositionWarehouse1.add(box2PositionWarehouse1);
//        boxesPositionWarehouse1.add(box3PositionWarehouse1);
//        boxesPositionWarehouse1.add(box4PositionWarehouse1);
//        boxesPositionWarehouse1.add(box5PositionWarehouse1);
//        boxesPositionWarehouse1.add(box6PositionWarehouse1);
//        boxesPositionWarehouse1.add(box7PositionWarehouse1);
//        boxesPositionWarehouse1.add(box8PositionWarehouse1);
//        boxesPositionWarehouse1.add(box9PositionWarehouse1);
//        boxesPositionWarehouse1.add(box10PositionWarehouse1);
//        boxesPositionWarehouse1.add(box11PositionWarehouse1);
//        boxesPositionWarehouse1.add(box12PositionWarehouse1);
//        
//        boxesPositionWarehouse2.add(box1PositionWarehouse2);
//        boxesPositionWarehouse2.add(box2PositionWarehouse2);
//        boxesPositionWarehouse2.add(box3PositionWarehouse2);
//        boxesPositionWarehouse2.add(box4PositionWarehouse2);
//
//        boxesPositionWarehouse3.add(box1PositionWarehouse3);
//        boxesPositionWarehouse3.add(box2PositionWarehouse3);
//        boxesPositionWarehouse3.add(box3PositionWarehouse3);
//        boxesPositionWarehouse3.add(box4PositionWarehouse3);
//        boxesPositionWarehouse3.add(box5PositionWarehouse3);
//        boxesPositionWarehouse3.add(box6PositionWarehouse3);
//        boxesPositionWarehouse3.add(box7PositionWarehouse3);
//        boxesPositionWarehouse3.add(box8PositionWarehouse3);
//        
//        boxesPositionWarehouse4.add(box1PositionWarehouse4);
//        
//        Warehouse theWarehouse1 = demoValCrtr.createWarehouse("The market 1", "Market1", boxesPositionWarehouse1);
//        Warehouse theWarehouse2 = demoValCrtr.createWarehouse("The market 2", "Market2", boxesPositionWarehouse2);
//        Warehouse theWarehouse3 = demoValCrtr.createWarehouse("The market 3", "Market3", boxesPositionWarehouse3);
//        Warehouse theWarehouse4 = demoValCrtr.createWarehouse("The market 4", "Market4", boxesPositionWarehouse4);
//
//        theShopfloor.addHasWarehouse(theWarehouse1);
//        theShopfloor.addHasWarehouse(theWarehouse2);
//        theShopfloor.addHasWarehouse(theWarehouse3);
//        theShopfloor.addHasWarehouse(theWarehouse4);
//
//        this.shopfloor=theShopfloor;
//
//        // Create IMAU Resources
//        // IMAU Initial Locations
//        
//        Coordinates3D theIMAUResource1Coordinates = demoValCrtr.createCoordinates3D(24.0, 0, 17.5);
//        Coordinates3D theIMAUResource2Coordinates = demoValCrtr.createCoordinates3D(6.0, 0, 12.0);
//        Coordinates3D theIMAUResource3Coordinates = demoValCrtr.createCoordinates3D(18.0, 0, 16.10);
//        
//        IMAUResource theImauResource1 = demoValCrtr.createIMAUResource("IMAU1", "The IMAU 1", 1.0, 50.0, 100.0, 100.0, 50.0, 0, 4, 25.0, 1000.0, 50.0, 300.0, 50.0);
//        IMAUResource theImauResource2 = demoValCrtr.createIMAUResource("IMAU2", "The IMAU 2", 1.0, 50.0, 100.0, 100.0, 50.0, 0, 4, 25.0, 1000.0, 50.0, 300.0, 50.0);
//        IMAUResource theImauResource3 = demoValCrtr.createIMAUResource("IMAU3", "The IMAU 3", 1.0, 50.0, 100.0, 100.0, 50.0, 0, 4, 25.0, 1000.0, 50.0, 300.0, 50.0);
//
//        theShopfloor.addHasIMAUResource(theImauResource1);
//        theShopfloor.addHasIMAUResource(theImauResource2);
//        theShopfloor.addHasIMAUResource(theImauResource3);
//
//        // PARTS
//
//        Part drumPart = demoValCrtr.createPart("Drum", Math.ceil(1400 / 500.0), Math.ceil(700 / 500.0), 1, Math.ceil(1400 / 500.0), 11.0, null, 0);
//        Part cablePart = demoValCrtr.createPart("Cable", Math.ceil(100 / 500.0), Math.ceil(100 / 500.0), 1, Math.ceil(100 / 500.0), 1.0, null, 0);
//        Part screwsPart1 = demoValCrtr.createPart("Screws", Math.ceil(100 / 500.0), Math.ceil(100 / 500.0), 1, Math.ceil(100 / 500.0), 1.0, null, 0);
//        Part screwsPart2 = demoValCrtr.createPart("Screws", Math.ceil(100 / 500.0), Math.ceil(100 / 500.0), 1, Math.ceil(100 / 500.0), 1.0, null, 0);
//        BasePart axlePart = demoValCrtr.createBasePart("Axle", Math.ceil(500 / 500.0), Math.ceil(600 / 500.0), 1, Math.ceil(600 / 500.0), 25.0, drumPart, 1);
//        demoValCrtr.addPartRequirement(axlePart, cablePart, 1);
//        demoValCrtr.addPartRequirement(axlePart, screwsPart1, 1);
//        demoValCrtr.addPartRequirement(axlePart, screwsPart2, 1);
//
//
//        GregorianCalendar dueDate = new GregorianCalendar(2018, 1, 1, 1, 0, 0);
//        Job manufacturingJob = demoValCrtr.createJob("Axle Assembly", "Assembly the axle", dueDate.getTimeInMillis());
//
//
//        // Logistic Jobs are not used nor needed
//        Job theLogisticJob = demoValCrtr.createJob("Supply Rigid Brake Hose Job1", "Supply Rigid Brake Hose", dueDate.getTimeInMillis());
//        Job theLogisticJob2 = demoValCrtr.createJob("Supply Brake Plate Screws Job2", "Supply Brake Plates screws", dueDate.getTimeInMillis());
//
//        Order manufacturingOrder = demoValCrtr.createOrder("Linea Turbo order 1", "Fiat model: Linea Turbo", manufacturingJob);
//        Order logisticsOrder = demoValCrtr.createOrder("MCV Diesel order 2", "Fiat model: MCV Diesel", theLogisticJob);
//        logisticsOrder.addHasJob(theLogisticJob2);
//        theShopfloor.addHasOrder(manufacturingOrder);
//        theShopfloor.addHasOrder(logisticsOrder);
//      
//
//    }

}
