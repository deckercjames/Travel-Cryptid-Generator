package view.gui;

import controller.ControlThread;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;

public class ViewGUI extends JFrame implements ActionListener, ChangeListener {

    public ViewGUI(){
        super("Cryptid Generator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
    }

    private JTextField saveLocationField;
    private JButton selectSaveLocationButton;
    private JButton makeBoardsButton;
    private JButton imHenkButton;
    private JSlider boardCountSlider;
    private JSlider easyCountSlider;
    private JTextField gameNameField;

    private void init(){

        this.setLayout(new BorderLayout());

        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BorderLayout());
        String dir = System.getProperty("user.dir");//+"/generated_games/";
        saveLocationField = new JTextField(dir);
        savePanel.add(saveLocationField, BorderLayout.CENTER);
        selectSaveLocationButton = new JButton("Change...");
        selectSaveLocationButton.addActionListener(this);
        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Name of Board Set:"));
        gameNameField = new JTextField(20);
        namePanel.add(gameNameField);
        savePanel.add(namePanel, BorderLayout.NORTH);
        savePanel.add(selectSaveLocationButton, BorderLayout.EAST);
        this.add(savePanel, BorderLayout.NORTH);

        //im henk button
        imHenkButton = new JButton("Im Colorblind");
        imHenkButton.addActionListener(this);
        this.add(imHenkButton, BorderLayout.EAST);

        boardCountSlider = new JSlider(0, 5, 3);
        boardCountSlider.setMinorTickSpacing(1);
        boardCountSlider.setMajorTickSpacing(1);
        boardCountSlider.setPaintTicks(true);
        boardCountSlider.setPaintLabels(true);
        boardCountSlider.setSnapToTicks(true);
        boardCountSlider.addChangeListener(this);
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
        sliderPanel.add(new JLabel("Number of games to generate:"));
        sliderPanel.add(boardCountSlider);
        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add(sliderPanel, BorderLayout.NORTH);
        this.add(p2, BorderLayout.WEST);

        easyCountSlider = new JSlider(0, 5, 0);
        easyCountSlider.setMinorTickSpacing(1);
        easyCountSlider.setMajorTickSpacing(1);
        easyCountSlider.setPaintTicks(true);
        easyCountSlider.setPaintLabels(true);
        easyCountSlider.setSnapToTicks(true);
        easyCountSlider.addChangeListener(this);
        sliderPanel.add(new JLabel("Easy Games:"));
        sliderPanel.add(easyCountSlider);
        this.add(p2, BorderLayout.WEST);

        JPanel generatePanel = new JPanel();
        makeBoardsButton = new JButton("Generate Games");
        makeBoardsButton.addActionListener(this);
        generatePanel.add(makeBoardsButton, BorderLayout.EAST);
        this.add(generatePanel, BorderLayout.SOUTH);

//        this.add(panel, BorderLayout.WEST);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == makeBoardsButton){

            if(gameNameField.getText().equals("")){
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a name for the game set.",
                        "Invalid Name",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            File outputFolder = new File(Paths.get(saveLocationField.getText(), gameNameField.getText()).toString());

            if(outputFolder.exists()){
                String[] options = {"Cancel", "Overwrite"};
                int result = JOptionPane.showOptionDialog(
                        this,
                        "A game set with this name exists.\nDo you want to overwrite it?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[0]);

                if(result != 1)
                    return;

            }else {
                if (!outputFolder.mkdirs()) {
                    System.out.println("Error in making dirs");
                    return;
                }
            }

            int numBoards = boardCountSlider.getValue();

            //start progress bar
            ProgressMonitor progressMonitor = new ProgressMonitor(
                    this,
                    "Generating Games",
                    "",
                    0, numBoards + 3);
            progressMonitor.setMillisToPopup(0);
            progressMonitor.setMillisToDecideToPopup(0);

            ControlThread ct = new ControlThread(outputFolder, gameNameField.getText(),
                    numBoards, easyCountSlider.getValue(), 6000, progressMonitor);
            Thread thread = new Thread(ct);
            thread.start();

            //no status bar
//            Controller controller = new Controller();
//            controller.makeCompleteGames(outputFolder, gameNameField.getText(), numBoards, 6000, null);

        }



        else if (e.getSource() == selectSaveLocationButton){

            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.dir")));
            chooser.setDialogTitle("Select Output Folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            chooser.setAcceptAllFileFilterUsed(false);

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                System.out.println("getCurrentDirectory(): "
                        +  chooser.getCurrentDirectory());
                System.out.println("getSelectedFile() : "
                        +  chooser.getSelectedFile());

                saveLocationField.setText(chooser.getSelectedFile()+"/");
            }
        }


        else if(e.getSource() == imHenkButton){

            new ViewColorChooserGUI(this);
            this.setVisible(false);

        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {

        //make sure that there are never more easy boards than total boards
        if(e.getSource() == boardCountSlider){
            if(boardCountSlider.getValue() < easyCountSlider.getValue()){
                easyCountSlider.setValue(boardCountSlider.getValue());
            }
        }else if (e.getSource() == easyCountSlider){
            if(easyCountSlider.getValue() > boardCountSlider.getValue()){
                boardCountSlider.setValue(easyCountSlider.getValue());
            }
        }

        //only enable generate if there are more than zero boards
        makeBoardsButton.setEnabled(boardCountSlider.getValue() != 0);

    }
}
