package main.java.view.clueview;

import main.java.model.model.Clue;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.line.LineStyle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class ClueBookletViewPDF extends ClueBookletView {

    public ClueBookletViewPDF(File outputFile){
        this.dest = outputFile;
    }

    private File dest;

    private static final LineStyle BLANK_LINE = new LineStyle(new Color(255, 255, 255, 255), 0);

    private final static int LEFT_COLUMN_WIDTH = 12;

    // Create a new font object selecting one of the PDF base fonts
    private static PDFont fontPlain = PDType1Font.HELVETICA;
    private static PDFont fontBold = PDType1Font.HELVETICA_BOLD;
//    private static PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
//    private static PDFont fontMono = PDType1Font.COURIER;

    private void makeTitleRow(BaseTable table, int playerIndex, String gameSetName){

        // add a spacer row if it is not the first player
        if(playerIndex != 0) {
            Row<PDPage> spacerRow = table.createRow(20);
            Cell<PDPage> spacerCell = spacerRow.createCell(100, "");
            spacerCell.setLeftBorderStyle(BLANK_LINE);
            spacerCell.setRightBorderStyle(BLANK_LINE);
        }


        Row<PDPage> headerRow = table.createRow(20);
        Cell<PDPage> headerCell = headerRow.createCell(20, "Player "+(playerIndex+1));
        headerCell.setFont(fontBold);
        headerCell.setFontSize(20);
        headerCell.setRightBorderStyle(BLANK_LINE);

        Cell<PDPage> gameSetNameCell = headerRow.createCell(80, gameSetName);
        gameSetNameCell.setFont(fontBold);
        gameSetNameCell.setFontSize(20);
        gameSetNameCell.setLeftBorderStyle(BLANK_LINE);
        gameSetNameCell.setAlign(HorizontalAlignment.RIGHT);

    }

    private void makeHeaderRow(BaseTable table, int numGames, int numEasyGames){

        Row<PDPage> headerRow = table.createRow(15);

        headerRow.createCell(LEFT_COLUMN_WIDTH, "");

        for(int i = 0; i < numGames; i++){
            String title = "Board "+(i+1);
            if(i < numEasyGames) title += " (E)";
            Cell<PDPage> headerCell = headerRow.createCell((100.0f - LEFT_COLUMN_WIDTH) / numGames,
                    title);
            headerCell.setFont(fontBold);
            headerCell.setFontSize(12);
        }

    }

    private void makeNumPlayersRow(BaseTable table, int numPlayers, List<Clue> cluesByGame)
    {
        Row<PDPage> row = table.createRow(20);

        Cell<PDPage> labelCell = row.createCell(LEFT_COLUMN_WIDTH, numPlayers+" players:");
        labelCell.setFont(fontPlain);
        labelCell.setFontSize(9);

        float clueCellWidth = (100.0f - LEFT_COLUMN_WIDTH) / cluesByGame.size();

        for (Clue clue : cluesByGame)
        {
            Cell<PDPage> cell = row.createCell(clueCellWidth, ClueView.getClueAsString(clue));
            cell.setFont(fontPlain);
            cell.setFontSize(9);
        }

    }

    @Override
    public void outputClues(String gameSetName, int numEasyGames, List<Map<Integer, List<Clue>>> clueTables) throws IOException
    {
        //truncate game set name so it fits in one line (yeah its a work around)
        BufferedImage bi = new BufferedImage(2, 2, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = bi.createGraphics();
        g.setFont(new Font("Helvetica", Font.BOLD, 20));
        int lettersIncluded = gameSetName.length();
        String gameSetNameMod = gameSetName;//modified title (uppercase, truncated with ellipses)
        int gameSetNameWidth = g.getFontMetrics().stringWidth(gameSetNameMod);
        System.out.println("name width: "+gameSetNameWidth);
        while(gameSetNameWidth > 375)
        {
            lettersIncluded--;
            gameSetNameMod = gameSetName.substring(0, lettersIncluded).toUpperCase() + "...";
            System.out.println("name width: "+gameSetNameWidth);
            gameSetNameWidth = g.getFontMetrics().stringWidth(gameSetNameMod);
        }
        g.dispose();
        gameSetName = gameSetNameMod;

        // clueTable: player index -> number of players -> game number

        System.out.println("\ncreating pdf");

        //Creating PDF document object
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();

        //Dummy Table
        float margin = 50;

        // starting y position is whole page height subtracted by top and bottom margin
        float yStartNewPage = page.getMediaBox().getHeight() - (margin);

        // we want table across whole page width (subtracted by left and right margin of course)
        float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

        float bottomMargin = 50;
        // y position is your coordinate of top left corner of the table

        BaseTable table = new BaseTable(0, yStartNewPage,
                bottomMargin, tableWidth, margin, document, page, true, true);

        //create each players clues
        for(int i = 0; i < clueTables.size(); i++)
        {
            System.out.println("Table for player "+i);

            makeTitleRow(table, i, gameSetName);

            makeHeaderRow(table, clueTables.get(0).get(5).size(), numEasyGames);

            //create the clue rows for each number of games
            for(Map.Entry<Integer, List<Clue>> cluesByGame : clueTables.get(i).entrySet())
            {
                makeNumPlayersRow(table, cluesByGame.getKey(), cluesByGame.getValue());
            }
        }

        table.draw();

        //Setting the font to the Content stream
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

        contentStream.endText();
        contentStream.close();
        document.addPage(page);
        String path = Paths.get(dest.toString(), "Clues.pdf").toString();
        document.save(path);
        document.close();

        //remove the second page
        document = PDDocument.load(new File(path));
        document.removePage(1);
        document.save(path);
        document.close();

        System.out.println("PDF created");
        System.out.println(dest);
    }

}
