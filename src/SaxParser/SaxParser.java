package SaxParser;

import Valute.Valute;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SaxParser  extends DefaultHandler {

    private boolean isInTag = false;
    private boolean isForeingerValue = false;
    private String qNameS = null;
    private Valute valute;
    private List<Valute> valuteList;

    public void printList(){

        valuteList.forEach(System.out::println);
    }

    @Override
    public void startDocument() throws SAXException {

        System.out.println("Document basladi");
        valuteList = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        qNameS = qName;
        isInTag = true;
        System.out.println(qNameS);
        if (qNameS.equals("ValCurs")){
            System.out.println(attributes.getValue("Description"));
        } else if (qNameS.equals("ValType") && attributes.getValue("Type").equals("Xarici valyutalar")){
            isForeingerValue = true;
        } else if (qNameS.equals("ValType") && attributes.getValue("Type").equals("Bank metalları")) {
            isForeingerValue = false;
        } else if (qNameS.equals("Valute") && isForeingerValue){
            valute = new Valute();
            valute.setCode(attributes.getValue("Code"));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        String data = new String(ch, start, length);
        System.out.println(data);
        if (isInTag && isForeingerValue){
            System.out.println(qNameS);
            switch (qNameS){
                case "Nominal":
                    valute.setNominal(Integer.parseInt(data));
                    break;
                case "Name":
                    valute.setName(data);
                    break;
                case "Value":
                    valute.setValue(new BigDecimal(data));
                    break;
            }
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        qNameS = qName;
        System.out.println(qNameS);
        isInTag = false;
        if (qNameS.equals("Valute") && isForeingerValue) {
            valuteList.add(valute);
            valute = null;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("Document bitti");
    }
}
