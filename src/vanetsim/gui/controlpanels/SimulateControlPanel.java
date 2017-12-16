/*
 * VANETsim open source project - http://www.vanet-simulator.org
 * Copyright (C) 2008 - 2013  Andreas Tomandl, Florian Scheuer, Bernhard Gruber
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package vanetsim.gui.controlpanels;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import vanetsim.ErrorLog;
import vanetsim.Scenario1;
import vanetsim.SystemInitial;
import vanetsim.VanetSimStart;
import vanetsim.VanetSimStarter;
import vanetsim.gui.DrawingArea;
import vanetsim.gui.Renderer;
import vanetsim.gui.helpers.ButtonCreator;
import vanetsim.gui.helpers.ReRenderManager;
import vanetsim.hash.HashChain;
import vanetsim.localization.Messages;
import vanetsim.map.Map;
import vanetsim.scenario.Scenario;
import vanetsim.simulation.SimulationMaster;

/**
 * This class creates all control elements used in the simulation tab.
 */
public final class SimulateControlPanel extends JPanel implements ActionListener, ChangeListener, ItemListener {

	private static long a = 7;
	private static long b = 12;
	private static long p = 103; /* initial curve: E(F_103) : y^2 = x^3 + 7x + 12 */
	private static long Gx = 102; /* initial generator: G = (102, 2) */
	private static long Gy = 2;
	private static long Px = 9; /* initially P = (9, 17) */
	private static long Py = 17;
	private static long Gx2 = 13;  /*initial G2 = (13,5)*/
	private static long Gy2 = 5;
	private static long Qx = 19; /* initially Q = (19, 0) */
	private static long Qy = 0;
	private static long k = 7; /* initially k = 33 */
	private static int n = 100; /* initially n = 100 random curves */
	
	/** The necessary constant for serializing. */
	private static final long serialVersionUID = 7292404190066585320L;

	/** The slider for zooming. */
	private final JSlider zoomSlider_;

	/**
	 * A panel which includes the buttons for starting and stopping the simulation.
	 * Uses a <code>CardLayout</code>.
	 */
	private final JPanel startStopJPanel_;

	/** An area to display text information. */
	private final JTextArea informationTextArea_;

	/** An area to display text information. */
	private JTextArea scenarioInformationTextArea_;

	/** An area to display text information. */
	private JTextField messageTextField_;

	/**
	 * A checkbox to enable/disable the display of circles so that it's possible to
	 * see the max. distances the vehicles may communicate.
	 */
	private final JCheckBox communicationDisplayCheckBox_;

	/**
	 * A checkbox to enable/disable the display of filled circles so that it's
	 * possible to see the mix zones.
	 */
	private final JCheckBox mixZoneDisplayCheckBox_;

	/** A checkbox to enable/disable the display of the vehicle ID. */
	private final JCheckBox vehicleIDDisplayCheckBox_;

	/** A checkbox to enable/disable the display penalty connections. */
	private final JCheckBox penaltyConnectionsCheckBox_;

	/** A checkbox to enable/disable the display known vehicles connections. */
	private final JCheckBox knownVehiclesConnectionsCheckBox_;

	/**
	 * The input field for the target step time. Used to increase or decrease
	 * simulation speed.
	 */
	private final JFormattedTextField targetStepTime_;

	/** A button to apple the target step time. */
	private final JButton targetStepTimeApplyButton_;

	/** The input field for the target time to jump to. */
	private final JFormattedTextField jumpToTargetTime_;

	/** A button to apply the target time. */
	private final JButton jumpToTargetApplyButton_;

	/**
	 * If the zooming slider is set externally, no rerender shall be made within the
	 * listeners here!
	 */
	private boolean dontReRenderZoom_ = false;

	/** A Button to hide the sidebar */
	private final JButton hideBar_;

	/** Mode of gui */
	private int mode_ = 0;

	/**
	 * Constructor for this control panel.
	 */
	public SimulateControlPanel(int scenario) {
		setLayout(new GridBagLayout());

		// global layout settings
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.insets = new Insets(5, 5, 5, 5);

		// language suppport
		JPanel panning = new JPanel();
		panning.setLayout(new FlowLayout(FlowLayout.LEFT));
		JButton changeGUI = new JButton("*");
		changeGUI.setPreferredSize(new Dimension(30, 30));
		changeGUI.setSize(new Dimension(30, 20));
		changeGUI.addActionListener(this);
		changeGUI.setActionCommand("guimode");

		hideBar_ = ButtonCreator.getJButton("hide.png", "hide", "hide", true, this);
		hideBar_.setPreferredSize(new Dimension(15, 31));
		hideBar_.setSize(new Dimension(15, 31));
		hideBar_.setBorder(BorderFactory.createEmptyBorder());
		hideBar_.setActionCommand("toogleBar");
		panning.add(hideBar_, c); // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		//panning.add(ButtonCreator.getJButton("DE_flag.png", "de", "de", true, this), c); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		//panning.add(ButtonCreator.getJButton("EN_flag.png", "en", "en", true, this), c); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		c.gridwidth = 2;
		add(panning, c);
		++c.gridy;
		c.gridx = 0;
		c.gridwidth = 1;
		add(ButtonCreator.getJButton("loadmap.png", "loadmap", Messages.getString("SimulateControlPanel.loadMap"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				this), c);
		c.gridx = 1;

		add(ButtonCreator.getJButton("loadscenario.png", "loadscenario", //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("SimulateControlPanel.loadScenario"), this), c); //$NON-NLS-1$
		c.gridx = 0;
		c.gridwidth = 2;

		// Panel with panning controls
		JLabel jLabel1 = new JLabel(
				"<html><b>" + Messages.getString("SimulateControlPanel.mapControl") + "</b></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		++c.gridy;
		add(jLabel1, c);
		panning = new JPanel();
		panning.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.NONE;
		c1.weightx = 0.5;
		c1.gridx = 1;
		c1.gridy = 0;
		c1.gridheight = 1;
		panning.add(ButtonCreator.getJButton("up.png", "up", Messages.getString("SimulateControlPanel.upButton"), this), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				c1);
		c1.gridx = 0;
		c1.gridy = 1;
		panning.add(ButtonCreator.getJButton("left.png", "left", Messages.getString("SimulateControlPanel.leftButton"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				this), c1);
		c1.gridx = 2;
		c1.gridy = 1;
		panning.add(ButtonCreator.getJButton("right.png", "right", //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("SimulateControlPanel.rightButton"), this), c1); //$NON-NLS-1$
		c1.gridx = 1;
		c1.gridy = 2;
		panning.add(ButtonCreator.getJButton("down.png", "down", Messages.getString("SimulateControlPanel.downButton"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				this), c1);
		++c.gridy;
		add(panning, c);

		// zoom slider
		jLabel1 = new JLabel("<html><b>" + Messages.getString("SimulateControlPanel.zoom") + "</b></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		++c.gridy;
		add(jLabel1, c);

		zoomSlider_ = getZoomSlider();
		++c.gridy;
		add(zoomSlider_, c);

		// Start and stop simulation
		jLabel1 = new JLabel("<html><b>" + Messages.getString("SimulateControlPanel.simulation") + "</b></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		++c.gridy;
		add(jLabel1, c);

		startStopJPanel_ = new JPanel(new CardLayout());

		startStopJPanel_.add(
				ButtonCreator.getJButton("start.png", "start", Messages.getString("SimulateControlPanel.start"), this), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"start"); //$NON-NLS-1$

		startStopJPanel_.add(
				ButtonCreator.getJButton("pause.png", "pause", Messages.getString("SimulateControlPanel.pause"), this), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"pause"); //$NON-NLS-1$
		++c.gridy;
		c.gridwidth = 1;
		add(startStopJPanel_, c);

		c.gridx = 1;
		add(ButtonCreator.getJButton("onestep.png", "onestep", Messages.getString("SimulateControlPanel.onestep"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				this), c); //$NON-NLS-4$

		c.gridx = 0;
		c.gridwidth = 2;
		JPanel tmpPanel = new JPanel();
		jLabel1 = new JLabel(Messages.getString("SimulateControlPanel.jumpToTime")); //$NON-NLS-1$
		tmpPanel.add(jLabel1);
		jumpToTargetTime_ = new JFormattedTextField(NumberFormat.getIntegerInstance());
		jumpToTargetTime_.setPreferredSize(new Dimension(40, 20));
		jumpToTargetTime_.setValue(0);
		tmpPanel.add(jumpToTargetTime_);
		jumpToTargetApplyButton_ = ButtonCreator.getJButton("ok_small.png", "targetTimeApply", //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("SimulateControlPanel.applyTargetTime"), this); //$NON-NLS-1$
		tmpPanel.add(jumpToTargetApplyButton_);
		++c.gridy;
		c.insets = new Insets(0, 0, 0, 0);
		// add(tmpPanel, c);

		tmpPanel = new JPanel();
		jLabel1 = new JLabel(Messages.getString("SimulateControlPanel.targetStepTime")); //$NON-NLS-1$
		tmpPanel.add(jLabel1);
		targetStepTime_ = new JFormattedTextField(NumberFormat.getIntegerInstance());
		targetStepTime_.setPreferredSize(new Dimension(30, 20));
		targetStepTime_.setValue(SimulationMaster.TIME_PER_STEP);
		tmpPanel.add(targetStepTime_);
		targetStepTimeApplyButton_ = ButtonCreator.getJButton("ok_small.png", "targetStepTimeApply", //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("SimulateControlPanel.applyTargetStepTime"), this); //$NON-NLS-1$
		tmpPanel.add(targetStepTimeApplyButton_);
		++c.gridy;
		// add(tmpPanel, c);
		c.insets = new Insets(5, 5, 5, 5);

		// information display checkboxes
		jLabel1 = new JLabel("<html><b>" + Messages.getString("SimulateControlPanel.information") + "</b></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		++c.gridy;
		add(jLabel1, c);

		++c.gridy;
		communicationDisplayCheckBox_ = new JCheckBox(Messages.getString("SimulateControlPanel.displayDistance"), //$NON-NLS-1$
				false);
		communicationDisplayCheckBox_.addItemListener(this);
		add(communicationDisplayCheckBox_, c);

		c.insets = new Insets(0, 5, 5, 5);
		++c.gridy;
		mixZoneDisplayCheckBox_ = new JCheckBox(Messages.getString("SimulateControlPanel.hideMixZones"), false); //$NON-NLS-1$
		mixZoneDisplayCheckBox_.addItemListener(this);
		// add(mixZoneDisplayCheckBox_,c);

		++c.gridy;
		vehicleIDDisplayCheckBox_ = new JCheckBox(Messages.getString("SimulateControlPanel.showVehicleIDs"), false); //$NON-NLS-1$
		vehicleIDDisplayCheckBox_.addItemListener(this);
		add(vehicleIDDisplayCheckBox_, c);
		c.insets = new Insets(5, 5, 5, 5);

		c.insets = new Insets(0, 5, 5, 5);
		++c.gridy;
		penaltyConnectionsCheckBox_ = new JCheckBox(Messages.getString("SimulateControlPanel.showPenaltyConnections"), //$NON-NLS-1$
				false);
		penaltyConnectionsCheckBox_.addItemListener(this);
		// add(penaltyConnectionsCheckBox_,c);

		++c.gridy;
		knownVehiclesConnectionsCheckBox_ = new JCheckBox(
				Messages.getString("SimulateControlPanel.showKnownVehiclesConnections"), false); //$NON-NLS-1$
		knownVehiclesConnectionsCheckBox_.addItemListener(this);
		// add(knownVehiclesConnectionsCheckBox_,c);
		c.insets = new Insets(5, 5, 5, 5);

		// System initialize
		if (scenario == 1) {
			jLabel1 = new JLabel("<html><b>" + Messages.getString("SimulateControlPanel.scenario1") + "</b></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			++c.gridy;
			add(jLabel1, c);
	
			// Step1
			JLabel step1Title = new JLabel("Step 1: System initialization"); //$NON-NLS-1$
			++c.gridy;
			add(step1Title, c);

			JPanel scenario1_step1 = new JPanel(new CardLayout());

			scenario1_step1.add(ButtonCreator.getJButton("start.png", "scenario1_step1",
					Messages.getString("SimulateControlPanel.scenario1_step1"), this), "scenario1_step1");
			++c.gridy;
			c.gridwidth = 2;
			add(scenario1_step1, c);
			
			// Step2
			JLabel step2Title = new JLabel("Setp 2: Generate credential"); //$NON-NLS-1$
			++c.gridy;
			add(step2Title, c);

			JPanel scenario1_step2 = new JPanel(new CardLayout());

			scenario1_step2.add(ButtonCreator.getJButton("start.png", "scenario1_step2",
					Messages.getString("SimulateControlPanel.scenario1_step2"), this), "scenario1_step2");
			++c.gridy;
			c.gridwidth = 2;
			add(scenario1_step2, c);
			
			// Step3
			JLabel step3Title = new JLabel("Step 3: Pseudonym sef-generation"); //$NON-NLS-1$
			++c.gridy;
			add(step3Title, c);

			JPanel scenario1_step3 = new JPanel(new CardLayout());

			scenario1_step3.add(ButtonCreator.getJButton("start.png", "scenario1_step3",
					Messages.getString("SimulateControlPanel.scenario1_step3"), this), "scenario1_step3");
			++c.gridy;
			c.gridwidth = 2;
			add(scenario1_step3, c);
			
			
			// text area for display of information running scenario
			c.gridwidth = 2;
			scenarioInformationTextArea_ = new JTextArea(20, 1);
			scenarioInformationTextArea_.setEditable(false);
			scenarioInformationTextArea_.setLineWrap(true);
			JScrollPane scrolltext1 = new JScrollPane(scenarioInformationTextArea_);
			scrolltext1.setOpaque(false);
			scrolltext1.getViewport().setOpaque(false);
			c.weighty = 1.0;
			++c.gridy;
			add(scrolltext1, c);
			
		} else if (scenario == 2) {
			// Scenario 2
			jLabel1 = new JLabel("<html><b>" + Messages.getString("SimulateControlPanel.scenario2") + "</b></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			++c.gridy;
			add(jLabel1, c);

			JLabel messageTitle = new JLabel("Enter message"); //$NON-NLS-1$
			++c.gridy;
			add(messageTitle, c);

			// TextField Message
			messageTextField_ = new JTextField();
			messageTextField_.setText("");
			messageTextField_.setColumns(20);
			++c.gridy;
			add(messageTextField_, c);

			// Button start
			// Case 1
			JLabel case1Title = new JLabel("Vehicle send message using valid psuedonum"); //$NON-NLS-1$
			++c.gridy;
			add(case1Title, c);

			JPanel scenario2_success = new JPanel(new CardLayout());
			scenario2_success.add(ButtonCreator.getJButton("start.png", "scenario2_success",
					Messages.getString("SimulateControlPanel.scenario2"), this), "scenario2_success");
			++c.gridy;
			c.gridwidth = 2;
			add(scenario2_success, c);
			
			// Case 2
			JLabel case2Title = new JLabel("Vehicle send message using invalid psuedonum"); //$NON-NLS-1$
			++c.gridy;
			add(case2Title, c);
			
			JPanel scenario2_fail = new JPanel(new CardLayout());
			scenario2_fail.add(ButtonCreator.getJButton("start.png", "scenario2_fail",
					Messages.getString("SimulateControlPanel.scenario2"), this), "scenario2_fail");
			++c.gridy;
			c.gridwidth = 2;
			add(scenario2_fail, c);



			// text area for display of information running scenario
			c.gridwidth = 2;
			scenarioInformationTextArea_ = new JTextArea(20, 1);
			scenarioInformationTextArea_.setEditable(false);
			scenarioInformationTextArea_.setLineWrap(true);
			
			SystemInitial systemInitial = new SystemInitial();
			Scenario1 scenario1 = new Scenario1();			
			HashChain h = new HashChain();
			
			String stringSystemInitial = "Prime Number: "+p + "\n" 
			+ "Generator G1: " + systemInitial.generationG1() + "\n" 
			+ "Generator G2: " + systemInitial.generationG2() +  "\n" 
			+ "Generator P: (3,21)" + "\n" 
			+ "Generator secret key: " + systemInitial.s + "\n" 
			+ "W: "+systemInitial.generationW() + "\n" 
			+ "Wi: "+systemInitial.generationWi() + "\n";			

			JScrollPane scrolltext1 = new JScrollPane(scenarioInformationTextArea_);
			scrolltext1.setOpaque(false);
			scrolltext1.getViewport().setOpaque(false);
			c.weighty = 1.0;
			++c.gridy;
			add(scrolltext1, c);

		} else if (scenario == 3) {
			// Scenario 3
			jLabel1 = new JLabel("<html><b>" + Messages.getString("SimulateControlPanel.scenario3") + "</b></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			++c.gridy;
			add(jLabel1, c);

			// TextField Message
			messageTextField_ = new JTextField();
			messageTextField_.setText("");
			messageTextField_.setColumns(20);
			++c.gridy;
			add(messageTextField_, c);

			// Button start 
			JPanel scenario3 = new JPanel(new CardLayout());

			scenario3.add(ButtonCreator.getJButton("start.png", "scenario3",
					Messages.getString("SimulateControlPanel.scenario3"), this), "scenario3");
			++c.gridy;
			c.gridwidth = 1;
			add(scenario3, c);
			
			// text area for display of information running scenario
			c.gridwidth = 2;
			scenarioInformationTextArea_ = new JTextArea(20, 1);
			scenarioInformationTextArea_.setEditable(false);
			scenarioInformationTextArea_.setLineWrap(true);
			JScrollPane scrolltext1 = new JScrollPane(scenarioInformationTextArea_);
			scrolltext1.setOpaque(false);
			scrolltext1.getViewport().setOpaque(false);
			c.weighty = 1.0;
			++c.gridy;
			add(scrolltext1, c);
		}

		// text area for display of information. Consumes all available space
		c.gridwidth = 2;
		informationTextArea_ = new JTextArea(20, 1);
		informationTextArea_.setEditable(false);
		informationTextArea_.setLineWrap(true);
		JScrollPane scrolltext = new JScrollPane(informationTextArea_);
		scrolltext.setOpaque(false);
		scrolltext.getViewport().setOpaque(false);
		c.weighty = 1.0;
		++c.gridy;
		// add(scrolltext, c);
	}

	/**
	 * Shows the "start simulation" button after simulation has been stopped
	 * externally.
	 */
	public void setSimulationStop() {
		CardLayout cl = (CardLayout) (startStopJPanel_.getLayout());
		cl.show(startStopJPanel_, "start"); //$NON-NLS-1$
	}

	/**
	 * Sets the text in the information area.
	 * 
	 * @param newText
	 *            the new information text
	 */
	public void setInformation(String newText) {
		informationTextArea_.setText(newText);
	}

	/**
	 * Sets the value of the zooming slider.
	 * 
	 * @param zoom
	 *            the new zoom value
	 */
	public void setZoomValue(int zoom) {
		dontReRenderZoom_ = true;
		zoomSlider_.setValue(zoom);
	}

	/**
	 * Creates a slider for zooming.
	 * 
	 * @return a ready-to-use <code>JSlider</code>
	 */
	private JSlider getZoomSlider() {
		JSlider slider = new JSlider(-75, 212, 1);
		Hashtable<Integer, JLabel> ht = new Hashtable<Integer, JLabel>();
		// the labels correspond to a exponential scale but internally the slider
		// calculates only in a constant scale => need to convert in stateChanged()!
		ht.put(-75, new JLabel("3km")); //$NON-NLS-1$
		ht.put(-20, new JLabel("1km")); //$NON-NLS-1$
		ht.put(45, new JLabel("200m")); //$NON-NLS-1$
		ht.put(96, new JLabel("100m")); //$NON-NLS-1$
		ht.put(157, new JLabel("30m")); //$NON-NLS-1$
		ht.put(212, new JLabel("10m")); //$NON-NLS-1$
		slider.setLabelTable(ht);
		slider.setPaintLabels(true);
		slider.setMinorTickSpacing(10);
		slider.setMajorTickSpacing(40);
		slider.addChangeListener(this);
		return slider;
	}

	/**
	 * An implemented <code>ActionListener</code> which performs all needed actions
	 * when a <code>JButton</code> is clicked.
	 * 
	 * @param e
	 *            an <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if ("up".equals(command)) { //$NON-NLS-1$
			Renderer.getInstance().pan('u');
			ReRenderManager.getInstance().doReRender();
		} else if ("down".equals(command)) { //$NON-NLS-1$
			Renderer.getInstance().pan('d');
			ReRenderManager.getInstance().doReRender();
		} else if ("left".equals(command)) { //$NON-NLS-1$
			Renderer.getInstance().pan('l');
			ReRenderManager.getInstance().doReRender();
		} else if ("right".equals(command)) { //$NON-NLS-1$
			Renderer.getInstance().pan('r');
			ReRenderManager.getInstance().doReRender();
		} else if ("loadmap".equals(command)) { //$NON-NLS-1$
			VanetSimStart.getMainControlPanel().changeFileChooser(true, true, false);
			int returnVal = VanetSimStart.getMainControlPanel().getFileChooser()
					.showOpenDialog(VanetSimStart.getMainFrame());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				Runnable job = new Runnable() {
					public void run() {
						Map.getInstance().load(VanetSimStart.getMainControlPanel().getFileChooser().getSelectedFile(),
								false);
					}
				};
				new Thread(job).start();
			}
		} else if ("loadscenario".equals(command)) { //$NON-NLS-1$
			VanetSimStart.getMainControlPanel().changeFileChooser(true, true, false);
			int returnVal = VanetSimStart.getMainControlPanel().getFileChooser()
					.showOpenDialog(VanetSimStart.getMainFrame());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				Runnable job = new Runnable() {
					public void run() {
						Scenario.getInstance()
								.load(VanetSimStart.getMainControlPanel().getFileChooser().getSelectedFile(), false);
					}
				};
				new Thread(job).start();
			}
		} else if ("pause".equals(command)) { //$NON-NLS-1$
			CardLayout cl = (CardLayout) (startStopJPanel_.getLayout());
			cl.show(startStopJPanel_, "start"); //$NON-NLS-1$
			Runnable job = new Runnable() {
				public void run() {
					VanetSimStart.getSimulationMaster().stopThread();
				}
			};
			new Thread(job).start();
		} else if ("start".equals(command)) { //$NON-NLS-1$
			if (VanetSimStart.getMainControlPanel().getEditPanel().getEditMode() == true)
				ErrorLog.log(Messages.getString("SimulateControlPanel.simulationNotPossibleInEditMode"), 6, //$NON-NLS-1$
						this.getName(), "startSim", null); //$NON-NLS-1$
			else {
				CardLayout cl = (CardLayout) (startStopJPanel_.getLayout());
				cl.show(startStopJPanel_, "pause"); //$NON-NLS-1$
				VanetSimStart.getSimulationMaster().startThread();
			}
		} else if ("onestep".equals(command)) { //$NON-NLS-1$
			if (VanetSimStart.getMainControlPanel().getEditPanel().getEditMode() == true)
				ErrorLog.log(Messages.getString("SimulateControlPanel.simulationNotPossibleInEditMode"), 6, //$NON-NLS-1$
						this.getName(), "oneStep", null); //$NON-NLS-1$
			else {
				Runnable job = new Runnable() {
					public void run() {
						VanetSimStart.getSimulationMaster().doOneStep();
					}
				};
				new Thread(job).start();
			}
		} else if ("targetTimeApply".equals(command)) { //$NON-NLS-1$
			if (VanetSimStart.getMainControlPanel().getEditPanel().getEditMode() == true)
				ErrorLog.log(Messages.getString("SimulateControlPanel.simulationNotPossibleInEditMode"), 6, //$NON-NLS-1$
						this.getName(), "startSim", null); //$NON-NLS-1$
			else {
				int target = ((Number) jumpToTargetTime_.getValue()).intValue();
				if (target <= Renderer.getInstance().getTimePassed())
					ErrorLog.log(Messages.getString("SimulateControlPanel.jumpingBackwardsNotPossible"), 6, //$NON-NLS-1$
							this.getName(), "jumpToTime", null); //$NON-NLS-1$
				else
					VanetSimStart.getSimulationMaster().jumpToTime(target);
			}
		} else if ("targetStepTimeApply".equals(command)) { //$NON-NLS-1$
			int tmp = ((Number) targetStepTime_.getValue()).intValue();
			if (tmp < 0) {
				ErrorLog.log(Messages.getString("SimulateControlPanel.noNegativeTargetStepTime"), 6, this.getName(), //$NON-NLS-1$
						"targetStepTimeApply", null); //$NON-NLS-1$
			} else {
				VanetSimStart.getSimulationMaster().setTargetStepTime(tmp);
			}
		} else if ("de".equals(command)) {
			VanetSimStarter.restartWithLanguage("de");
		} else if ("en".equals(command)) {
			VanetSimStarter.restartWithLanguage("");
		} else if ("toogleBar".equals(command)) {
			VanetSimStart.getMainControlPanel().tooglePanel();
		} else if ("guimode".equals(command)) {
			repaintGUI();
		} else if ("scenario1_step1".equals(command)) {
			// TODO Auto-generated method stub
			SystemInitial systemInitial = new SystemInitial();
			Scenario1 scenario1 = new Scenario1();			
			HashChain h = new HashChain();
			
			String stringSystemInitial = "Prime Number: "+p + "\n" 
			+ "Generator G1: " + systemInitial.generationG1() + "\n" 
			+ "Generator G2: " + systemInitial.generationG2() +  "\n" 
			+ "Generator P: (3,21)" + "\n" 
			+ "Generator secret key s: " + systemInitial.s + "\n" 
			+ "W: "+systemInitial.generationW() + "\n" 
			+ "Wi: "+systemInitial.generationWi() + "\n";
			
			scenarioInformationTextArea_.setText(stringSystemInitial);
		} else if ("scenario1_step2".equals(command)) {
			// TODO Auto-generated method stub
			SystemInitial systemInitial = new SystemInitial();

			String text = scenarioInformationTextArea_.getText() + "\n"
					+ "-------------TA generation for Scenarios1--------" + "\n"
					+ "Secret key for Vehicle 1: \n " + systemInitial.generationForV() + "\n"
					+ "Secret key for Vehicle 2: \n " + systemInitial.generationForV() + "\n";
			scenarioInformationTextArea_.setText(text);
			Renderer.getInstance().setScenario(12);
		}else if ("scenario1_step3".equals(command)) {
			// TODO Auto-generated method stub			
			Renderer.getInstance().setScenario(13);
		}
		
		else if ("scenario2_success".equals(command)) {
			String vehicle1Pseudonum = "Vehicle with Pseudonum (31,4)" ;
			String vehicle2Pseudonum = "Vehicle with Pseudonum (32,23)";

			String text = scenarioInformationTextArea_.getText() + "\n"
						+ vehicle1Pseudonum + " send message \"" + messageTextField_.getText() + "\" to " 
						+ vehicle2Pseudonum;
			scenarioInformationTextArea_.setText(text);

			Renderer.getInstance().setScenario2(messageTextField_.getText(), true);
		}
		else if ("scenario2_fail".equals(command)) {
			String vehicle1Pseudonum = "Vehicle with Pseudonum (2,4)" ;
			String vehicle2Pseudonum = "Vehicle with Pseudonum (32,23)";

			String text = scenarioInformationTextArea_.getText() + "\n"
						+ vehicle1Pseudonum + " send message \"" + messageTextField_.getText() + "\" to " 
						+ vehicle2Pseudonum;
			scenarioInformationTextArea_.setText(text);

			Renderer.getInstance().setScenario2(messageTextField_.getText(), false);
		}
		else if ("scenario3".equals(command)) {
			Renderer.getInstance().setScenario(3);
		}

	}

	/**
	 * An implemented <code>ChangeListener</code> for the zooming slider which
	 * performs the necessary actions.
	 * 
	 * @param e
	 *            a <code>ChangeEvent</code>
	 */
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) { // only perform action when mouse button is released!
			int value = source.getValue();
			double scale = Math.exp(value / 50.0) / 1000;
			if (dontReRenderZoom_)
				dontReRenderZoom_ = false;
			else {
				Renderer.getInstance().setMapZoom(scale);
				ReRenderManager.getInstance().doReRender();
			}
		}
	}

	/**
	 * Invoked when an item changes. Used for the JCheckBox.
	 * 
	 * @param e
	 *            the change event
	 * 
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		boolean state;
		if (e.getStateChange() == ItemEvent.SELECTED)
			state = true;
		else
			state = false;
		if (e.getSource() == communicationDisplayCheckBox_)
			Renderer.getInstance().setHighlightCommunication(state);
		if (e.getSource() == mixZoneDisplayCheckBox_)
			Renderer.getInstance().setHideMixZones(state);
		if (e.getSource() == vehicleIDDisplayCheckBox_)
			Renderer.getInstance().setDisplayVehicleIDs(state);
		if (e.getSource() == penaltyConnectionsCheckBox_)
			Renderer.getInstance().setShowPenaltyConnections_(state);
		if (e.getSource() == knownVehiclesConnectionsCheckBox_)
			Renderer.getInstance().setShowKnownVehiclesConnections_(state);

		ReRenderManager.getInstance().doReRender();
	}

	/**
	 * starts simulation
	 */
	public void startSimulation() {
		if (VanetSimStart.getMainControlPanel().getEditPanel().getEditMode() == true)
			ErrorLog.log(Messages.getString("SimulateControlPanel.simulationNotPossibleInEditMode"), 6, this.getName(), //$NON-NLS-1$
					"startSim", null); //$NON-NLS-1$
		else {
			CardLayout cl = (CardLayout) (startStopJPanel_.getLayout());
			cl.show(startStopJPanel_, "pause"); //$NON-NLS-1$
			VanetSimStart.getSimulationMaster().startThread();
		}
	}

	/**
	 * stop simulation
	 */
	public void stopSimulation() {
		CardLayout cl = (CardLayout) (startStopJPanel_.getLayout());
		cl.show(startStopJPanel_, "start"); //$NON-NLS-1$
		Runnable job = new Runnable() {
			public void run() {
				VanetSimStart.getSimulationMaster().stopThread();
			}
		};
		new Thread(job).start();

	}

	/**
	 * toggle the simulation status (start/stop)
	 */
	public void toggleSimulationStatus() {
		if (VanetSimStart.getSimulationMaster().isSimulationRunning())
			stopSimulation();
		else
			startSimulation();
	}

	public void toggleMixZones() {
		mixZoneDisplayCheckBox_.setSelected(!mixZoneDisplayCheckBox_.isSelected());
		Renderer.getInstance().setHideMixZones(mixZoneDisplayCheckBox_.isSelected());

	}

	public void toggleCommunicationDistance() {
		communicationDisplayCheckBox_.setSelected(!communicationDisplayCheckBox_.isSelected());
		Renderer.getInstance().setHighlightCommunication(communicationDisplayCheckBox_.isSelected());
	}

	public void toggleVehileIDs() {
		vehicleIDDisplayCheckBox_.setSelected(!vehicleIDDisplayCheckBox_.isSelected());
		Renderer.getInstance().setDisplayVehicleIDs(vehicleIDDisplayCheckBox_.isSelected());

	}

	public void toggleVehicleConnections() {
		knownVehiclesConnectionsCheckBox_.setSelected(!knownVehiclesConnectionsCheckBox_.isSelected());
		Renderer.getInstance().setShowKnownVehiclesConnections_(knownVehiclesConnectionsCheckBox_.isSelected());

	}

	public JButton getHideBar_() {
		return hideBar_;
	}

	public void repaintGUI() {
		String[] styleArray = { "com.jtattoo.plaf.smart.SmartLookAndFeel",
				"com.jtattoo.plaf.aluminium.AluminiumLookAndFeel", "com.jtattoo.plaf.acryl.AcrylLookAndFeel",
				"com.jtattoo.plaf.aero.AeroLookAndFeel", "com.jtattoo.plaf.bernstein.BernsteinLookAndFeel",
				"com.jtattoo.plaf.graphite.GraphiteLookAndFeel", "com.jtattoo.plaf.fast.FastLookAndFeel",
				"com.jtattoo.plaf.hifi.HiFiLookAndFeel", "com.jtattoo.plaf.luna.LunaLookAndFeel",
				"com.jtattoo.plaf.mcwin.McWinLookAndFeel", "com.jtattoo.plaf.mint.MintLookAndFeel",
				"com.jtattoo.plaf.noire.NoireLookAndFeel" };

		try {
			UIManager.setLookAndFeel(styleArray[mode_ % styleArray.length]);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					VanetSimStart.getMainFrame().invalidate();
					VanetSimStart.getMainFrame().repaint();
				}
			});

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mode_++;
	}
}