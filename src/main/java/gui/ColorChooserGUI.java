package main.java.gui;

import main.java.fileio.Settings;
import main.java.model.game.hexs.*;
import main.java.view.boardview.ViewBoard;
import main.java.view.boardview.ViewBoardImageVectors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class ColorChooserGUI extends JFrame implements ActionListener, ChangeListener
{
    ColorChooserGUI(CryptidGUI viewGUI)
    {
        super("Cryptid Color Selector");

        this.viewGUI = viewGUI;

        tagColorMap = Settings.loadColors();

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                colorChooserClosed();
            }
        });

        initBoard();
        init();
    }

    private CryptidGUI viewGUI;

    private Map<HexLocation, Hex> testBoard;

    private JColorChooser colorChooser;

    private JPanel colorOptionPanel;
    private ButtonGroup colorOptionGroup;
    private Map<JRadioButton, HexTag> buttonTagMap;
    private Map<HexTag, Color> tagColorMap;

    private HexTag selectedTag;

    private JButton cancelButton;
    private JButton saveButton;

    private void initBoard()
    {
        testBoard = new HashMap<>();
        Random rd = new Random();
        for(int x = 0; x < 12; x++)
        {
            final int startY = -((x+1)/2);
            final int endY = startY - 8;
            for(int y = startY; y > endY; y--)
            {
                Hex hex = new Hex(Terrain.values()[rd.nextInt(5)]);
                if(Math.random() < 0.4)
                    hex = new Hex(Terrain.values()[rd.nextInt(5)], Animal.values()[rd.nextInt(2)]);
                if(Math.random() < 0.4)
                    hex.addStructure(StructureType.values()[rd.nextInt(2)], StructureColor.values()[rd.nextInt(4)]);
                testBoard.put(new HexLocation(x, y), hex);
            }
        }
    }

    private void makeRadioButton(HexTag tag)
    {
        JRadioButton button = new JRadioButton(tag.toString());
        buttonTagMap.put(button, tag);
        button.addActionListener(this);
        colorOptionGroup.add(button);
        colorOptionPanel.add(button);

        if(tag == Terrain.WATER)
        {
            selectedTag = tag;
            button.setSelected(true);
        }
    }

    private void init()
    {
        this.setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel()
        {
            @Override
            public void paintComponent(Graphics g1)
            {
                super.paintComponent(g1);

                Graphics2D g = (Graphics2D) g1;

                System.out.println("Panel width " + getWidth());

                final int hexSizeX = ViewBoard.getHexWidth(getWidth(), 12);

                System.out.println("Hex width " + hexSizeX);

                ViewBoardImageVectors viewBoardImageVectors = new ViewBoardImageVectors();

                viewBoardImageVectors.drawHexes(g, testBoard.entrySet().iterator(), hexSizeX, new Dimension(), tagColorMap);
            }
        };
        this.add(boardPanel, BorderLayout.CENTER);

        JPanel colorSelectorPanel = new JPanel();
        colorChooser = new JColorChooser(tagColorMap.get(Terrain.WATER));
        colorChooser.getSelectionModel().addChangeListener(this);
        colorChooser.setPreviewPanel(new JPanel());
        colorSelectorPanel.add(colorChooser);
        this.add(colorChooser, BorderLayout.NORTH);

        colorOptionPanel = new JPanel();
        colorOptionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        colorOptionPanel.setLayout(new BoxLayout(colorOptionPanel, BoxLayout.PAGE_AXIS));
        colorOptionGroup = new ButtonGroup();
        colorOptionPanel.add(new JLabel("Terrain"));
        buttonTagMap = new HashMap<>(30);
        for(Terrain terrain : Terrain.values())
            makeRadioButton(terrain);
        colorOptionPanel.add(new JLabel("  "));
        colorOptionPanel.add(new JLabel("Animal Territories"));
        for(Animal animal : Animal.values())
            makeRadioButton(animal);
        colorOptionPanel.add(new JLabel("  "));
        colorOptionPanel.add(new JLabel("Structures"));
        for(StructureColor structColor : StructureColor.values())
            makeRadioButton(structColor);

        this.add(colorOptionPanel, BorderLayout.WEST);


        JPanel saveCancelPanel = new JPanel();
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        saveCancelPanel.add(cancelButton);
        saveButton = new JButton("Save");
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        rootPane.setDefaultButton(saveButton);
        saveButton.addActionListener(this);
        saveCancelPanel.add(saveButton);
        this.add(saveCancelPanel, BorderLayout.SOUTH);


        this.pack();
        this.setMinimumSize(this.getSize());
        this.setSize(this.getWidth(), this.getHeight()+70);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setVisible(true);
    }

    private void colorChooserClosed()
    {
        this.dispose();
        this.setVisible(false);

        viewGUI.setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        System.out.println("Color State Changed");
        Color chosenColor = colorChooser.getColor();
        Color color = new Color(chosenColor.getRGB());
        tagColorMap.put(selectedTag, color);
        System.out.println("Selected tag: "+selectedTag);
        System.out.println("new Color: "+tagColorMap.get(selectedTag));
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        System.out.println("Action performed");

        if(e.getSource() == cancelButton)
        {
            colorChooserClosed();
        }
        else if(e.getSource() == saveButton)
        {
            Settings.saveColors(tagColorMap);

            colorChooserClosed();
        }
        else if(e.getSource() instanceof JRadioButton)
        {
            selectedTag = buttonTagMap.get(e.getSource());
            colorChooser.setColor(tagColorMap.get(selectedTag));
        }
    }
}
