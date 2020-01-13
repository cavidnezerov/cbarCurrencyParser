package SaxParser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SaxMain {

    public static void main(String[] args) {

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String link = "https://www.cbar.az/currencies/" + formatter.format(LocalDate.now()) + ".xml";
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            SaxParser saxParser = new SaxParser();
            parser.parse(link, saxParser);
            saxParser.printList();

        } catch (ParserConfigurationException | SAXException | IOException e) {

            e.printStackTrace();
        }
    }
}
