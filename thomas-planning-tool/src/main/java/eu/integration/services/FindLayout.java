package eu.integration.services;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

//import lms.robopartner.datamodel.map.utilities.JsonPropertiesHelper;
//import lms.robopartner.mfg_planning_tool.ActivePassiveResourceScheduler;
import lms.robopartner.mfg_planning_tool.LayoutScheduler3D;

//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.json.JSONObject;
import org.slf4j.LoggerFactory;

public class FindLayout extends JFrame {

	/**
	 * \\
	 * 
	 * @author user
	 * 
	 *         this class extends jframe in order the user knows when the impact is
	 *         ready to run, running or finished running.
	 */
	private static final long serialVersionUID = 1714087859799248594L;
	public static boolean flagForForLoop = false;
	public static double sizeOfExistingAss = 0;
	public static int numOfWorkcenters = 0;
	public static int count, countTotal;
	org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FindLayout.class);

	public static void main(String[] args) {

		FindLayout find = new FindLayout();
		find.setVisible(true);
		find.findLayoutClient();

		/*
		 * org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FindLayout.class);
		 * LayoutScheduler3D layoutScheduler3D = new LayoutScheduler3D(); Vector<String>
		 * resultVector = null; try { resultVector =
		 * layoutScheduler3D.getLayoutSchedule(); } catch ( Exception e ) {
		 * LOGGER.error(ExceptionUtils.getStackTrace(e)); } if ( resultVector != null )
		 * { for ( int i = 0; i < resultVector.size(); i++ )
		 * LOGGER.trace(resultVector.get(i)); } else LOGGER.debug("null"); LOGGER.
		 * debug("flag: {} and assignments: {} and workcenters: {} and count: {} and total count: {}"
		 * , FindLayout.flagForForLoop, FindLayout.sizeOfExistingAss,
		 * FindLayout.numOfWorkcenters, FindLayout.count, FindLayout.countTotal);
		 * 
		 * ActivePassiveResourceScheduler activePassiveResourcesScheduler = new
		 * ActivePassiveResourceScheduler();
		 * activePassiveResourcesScheduler.setPlanningInput(layoutScheduler3D.
		 * getPlanningInput());
		 * activePassiveResourcesScheduler.setResourcesAndPartsMapingForSR(
		 * layoutScheduler3D.getResourcesAndPartsMapingForSR());
		 * activePassiveResourcesScheduler.taskScheduler(); Vector<String> scheduledTask
		 * = activePassiveResourcesScheduler.getTaskSchedulerResults(); for ( int i = 0;
		 * i < scheduledTask.size(); i++ ) LOGGER.info(scheduledTask.get(i));
		 */
	}

	/**
	 * Constructor
	 */
	public FindLayout() {
		setFont(new Font("Dialog", Font.BOLD, 12));
		setTitle("HRC Task Planner");
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(FindLayout.class.getResource("/Images/ROBO-PARTNER_Logo.ICO")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		setResizable(false);

		JLabel labelPicture = new JLabel();
		labelPicture.setHorizontalAlignment(SwingConstants.CENTER);
		labelPicture.setIcon(new ImageIcon(FindLayout.class.getResource("/Images/ROBO-PARTNER_Logo.png")));
		setSize(500, 320);
		this.getContentPane().add(labelPicture, BorderLayout.NORTH);

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(2, 1));
		JLabel labelTitle = new JLabel("HRC Task Planner");
		labelTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
		labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		southPanel.add(labelTitle);
		JButton buttonStart = new JButton("START");
		buttonStart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				wait = false;
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3, 3));
		buttonPanel.add(new JLabel());
		buttonPanel.add(buttonStart);
		buttonPanel.add(new JLabel());
		buttonPanel.add(new JLabel());
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		buttonPanel.add(statusLabel);
		for (int i = 0; i < 4; i++)
			buttonPanel.add(new JLabel());
		southPanel.add(buttonPanel);
		getContentPane().add(southPanel);

		// pack();
	}

	private JLabel statusLabel = new JLabel("Status: Ready to start");
	private boolean wait = true;
	// private List<JSONObject> tasksWithProperties;
	// private List<JSONObject> tasksWithTables;
	// private List<JSONObject> cellDimensions;

	/**
	 * from this function is initialized the tcp/ip connection with the PS tool. It
	 * calls IMPACT, generates a layout and sends it back to PS. when the generation
	 * is finished the connection does not close and awaits a new call in order to
	 * generate another alternative.
	 * 
	 * @param args
	 */
	public void findLayoutClient() {
		// cellDimensions = new ArrayList<JSONObject>();
		// tasksWithProperties = new ArrayList<JSONObject>();
		// tasksWithTables = new ArrayList<JSONObject>();
		while (wait) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
			// LOGGER.trace("is waiting to connect");
		}
		String ip = "127.0.0.1";
		int port = 50108;
		DataOutputStream outputStream = null;
		InputStream inputStream = null;
		Socket socket = null;
		byte[] msgReceived = new byte[1024];
		boolean waitForResponce = true;
		String msgReceivedString = "";

		try {
			socket = new Socket(ip, port);
			LOGGER.info("connection established");
			outputStream = new DataOutputStream(socket.getOutputStream());
			inputStream = socket.getInputStream();
		} catch (IOException e) {
			// LOGGER.error("IO exception: {}", ExceptionUtils.getStackTrace(e));
		}

		Vector<String> resultVector = new Vector<String>();
		LayoutScheduler3D layoutScheduler3D = new LayoutScheduler3D();
		try {
			outputStream.write("getData\n".getBytes());
		} catch (IOException e) {
			// LOGGER.error("IO exception for input stream: {}",
			// ExceptionUtils.getStackTrace(e));
		}

		boolean isFinished = false;
		while (waitForResponce) {
			try {
				int len = inputStream.read(msgReceived);
				LOGGER.debug("byte length: {}", len);
				msgReceivedString = new String(msgReceived, StandardCharsets.UTF_8);
				if (msgReceivedString.startsWith("{")) {
					// JSONObject jsonObject = new JSONObject(msgReceivedString);
					// if (
					// jsonObject.getString(JsonPropertiesHelper.TYPE_PROPERTY).startsWith(JsonPropertiesHelper.TYPE_PROPERTY_VALUE_PROPERTIES)
					// ) tasksWithProperties.add(jsonObject);
					// else if (
					// jsonObject.getString(JsonPropertiesHelper.TYPE_PROPERTY).startsWith(JsonPropertiesHelper.TYPE_PROPERTY_VALUE_TABLES)
					// ) tasksWithTables.add(jsonObject);
					// else if (
					// jsonObject.getString(JsonPropertiesHelper.TYPE_PROPERTY).startsWith(JsonPropertiesHelper.TYPE_PROPERTY_VALUE_CELL)
					// )
					// cellDimensions.add(jsonObject);
					outputStream.write("ok\n".getBytes());
				} else if (msgReceivedString.startsWith("dataEnd"))
					outputStream.write("start the procedure\n".getBytes());
				else if (!msgReceivedString.startsWith("wait")) {
					if (msgReceivedString.startsWith("again"))
						outputStream.write("start the procedure\n".getBytes());
					else if (msgReceivedString.startsWith("start impact")) {
						statusLabel.setText("Status: Procedure Started");
						// add here calls of setters
						// layoutScheduler3D.setTasksWithProperties(tasksWithProperties);
						// layoutScheduler3D.setCellDimensions(cellDimensions);
						// layoutScheduler3D.setTasksWithTables(tasksWithTables);
						layoutScheduler3D.setInputStream(inputStream);
						layoutScheduler3D.setOutputStream(outputStream);
						resultVector = layoutScheduler3D.getLayoutSchedule();
						if (resultVector.size() > 0) {
							LOGGER.info("message sent to PS = impact Started");
							outputStream.write("impact started\n".getBytes());
							LOGGER.info("debug1");
							int i = 0;
							while (i < resultVector.size()) {
								LOGGER.info("debug2");
								inputStream.read(msgReceived);
								LOGGER.info("debug3");
								msgReceivedString = new String(msgReceived, StandardCharsets.UTF_8);
								if (msgReceivedString.startsWith("ok")) {
									LOGGER.info("message sent to PS = {}", resultVector.get(i));
									outputStream.write(resultVector.get(i).getBytes());
									i++;
								}
							}

							LOGGER.info("the vector was sent");
							inputStream.read(msgReceived);
							msgReceivedString = new String(msgReceived, StandardCharsets.UTF_8);
							isFinished = true;
							if (msgReceivedString.startsWith("ok"))
								outputStream.write("end\n".getBytes());
						}
					}
				}
			} catch (IOException e) {
				statusLabel.setText("Status: Connection Lost");
				// LOGGER.error("IO exception for input stream: {}",
				// ExceptionUtils.getStackTrace(e));
				waitForResponce = false;
			} catch (Exception e) {
				// LOGGER.error("Excpetion while retrieving vector: {}",
				// ExceptionUtils.getStackTrace(e));
			}

			statusLabel.setText("Status: Ready to run again");
			for (int i = 0; i < resultVector.size(); i++) {
				LOGGER.trace(resultVector.get(i));
			}
			if (isFinished) {
				// ActivePassiveResourceScheduler activePassiveResourcesScheduler = new
				// ActivePassiveResourceScheduler();
				// activePassiveResourcesScheduler.setPlanningInput(layoutScheduler3D.getPlanningInput());
				// activePassiveResourcesScheduler.setResourcesAndPartsMapingForSR(layoutScheduler3D.getResourcesAndPartsMapingForSR());
				// activePassiveResourcesScheduler.taskScheduler();
				// Vector<String> scheduledTask =
				// activePassiveResourcesScheduler.getTaskSchedulerResults();
				// for ( int i = 0; i < scheduledTask.size(); i++ )
				// LOGGER.info(scheduledTask.get(i));
			}
		}
	}
}
