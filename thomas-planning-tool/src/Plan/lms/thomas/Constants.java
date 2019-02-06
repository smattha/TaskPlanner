package lms.thomas;

public class Constants {

	public static String INPUT_SECTION_ATTR = "input";
	public static String OUTPUT_SECTION_ATTR = "output";

	public static String INPUT_ELEMENT_NAME_ATTR = "name";
	public static String INPUT_ELEMENT_VALUE_ATTR = "value";

	//////////////// MOVE//////////////////
	public static String MOVE_POSE_VALUE_ATTR = "pose";
	public static String MOVE_TARGET_VALUE_ATTR = "target_fame";
	public static String MOVE_VEL_VALUE_ATTR = "velocity";
	public static String MOVE_ACC_VALUE_ATTR = "acceleration";

	public static String POSITION_X_VALUE_ATTR = "pos.x";
	public static String POSITION_Y_VALUE_ATTR = "pos.Y";
	public static String POSITION_Z_VALUE_ATTR = "pos.Z";

	public static String POSITION_ORIENT_W_VALUE_ATTR = "orient.w";
	public static String POSITION_ORIENT_X_VALUE_ATTR = "orient.x";
	public static String POSITION_ORIENT_Y_VALUE_ATTR = "orient.y";
	public static String POSITION_ORIENT_Z_VALUE_ATTR = "orient.z";

	//////////////////// Vision/////////////////////////////
	public static String VISION_DISTANCE_VALUE_ATTR = "distance";

	public static String VISION_TARGET_FRAME = "target_frame";
	public static String VISION_SOURCE_FRAME = "source_frame";
	public static String VISION_DETECTED_PART = "detected_part";
	public static String VISION_TYPE = "DETECT_PART";

	//////////////////// navigationGoal/////////////////////////////
	public static String NAVIGATION_ID = "target_frame";

	///////////////////// toolTypes///////////////////////////////
	public static String TOOL_SCREWDRIVER = "screw";
	public static String TOOL_GRIPPER = "gripper";

	///////////////////////// REsources//////////////////////////////
	public static String RESOURCE_ARM1 = "arm1";
	public static String RESOURCE_ARM2 = "arm2";
	public static String RESOURCE_MRP = "mrp";

	//////////////////// Operations////////////////////////
	public static String OPERATIONS_PLACE = "place";
	public static String OPERATIONS_PICK = "pick";

	///////////////// Actions/////////////////////////
	public static String ACTIONS_LOCALIZE = "localize";
	public static String ACTIONS_NAVIGATE = "navigate";
	public static String ACTIONS_APPROACH = "approach";
	public static String ACTIONS_DETECT = "detectPart";
	public static String ACTIONS_ALIGN = "approach";
	public static String ACTIONS_ATTACH = "detectPart";
	public static String ACTIONS_GRASP = "localize";
	public static String ACTIONS_PRE_REACT = "navigate";
	public static String ACTIONS_RETRACT = "approach";
	public static String ACTIONS_MOVE = "move";
	public static String ACTIONS_POST_ATTACH = "approach";
	public static String ACTIONS_RELEASE = "approach";
	public static String ACTIONS_DETECT_BARCODE = "approach";
	public static String ACTIONS_DETECT_SCREWING_POSE = "";
	public static String ACTIONS_SCREW = "approach";
	public static String ACTIONS_READ_BARCODE = "approach";

	/////////////// STORE XML FILES PATH///////////////////
	public static String OUTPUT_FILE_PATH_IMPACT_OUT = "C:\\Users\\smatt\\Desktop\\xml\\program.xml";
	public static String OUTPUT_FILE_PATH_RESOURCE_COMP = "C:\\Users\\smatt\\Desktop\\xml\\resource.xml";
	public static String OUTPUT_FOLDER_OPERATIONS_GENERATED = "C:\\Users\\smatt\\Desktop\\xml\\templates\\";

	/////////////////// Http Server/////////////////////
	public static int HTTP_SERVER_PORT = 8013;
	public static String HTTP_REQUEST_GET_PROCESS_PATH = "/Process";
}
