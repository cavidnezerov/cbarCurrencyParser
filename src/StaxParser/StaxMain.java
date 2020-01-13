package StaxParser;

import Valute.Valute;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StaxMain {

    public static void main(String[] args) {

        boolean isInTag = false;
        boolean isForeingerValute = false;
        Valute valute = null;
        List<Valute> valuteList = null;
        String name =  null;

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String link = "https://www.cbar.az/currencies/" + formatter.format(LocalDate.now()) + ".xml";

            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader( new StreamSource(link));

            while (reader.hasNext()){


                if (reader.getEventType() == XMLEvent.START_DOCUMENT){

                    System.out.println("Document Basladi");
                    valuteList = new ArrayList<>();
                }
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT){

                    isInTag = true;
                    name = reader.getName().toString();
                    System.out.println(name);
                    if (name.equals("ValType") && reader.getAttributeValue(0).equals("Xarici valyutalar")){
                        isForeingerValute = true;
                    } else if (isForeingerValute && name.equals("Valute")){
                        valute = new Valute();
                        valute.setCode(reader.getAttributeValue(0));
                    }
                } else if (event == XMLEvent.CHARACTERS){
                    String data = reader.getText();
                    System.out.println(data);
                    if (isForeingerValute && isInTag){
                        switch (name){
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

                } else if (event == XMLEvent.END_ELEMENT){
                    isInTag = false;
                    name = reader.getName().toString();
                    System.out.println(name);
                    if (isForeingerValute && name.equals("Valute")){
                        valuteList.add(valute);
                        valute = null;
                    }
                } else if (event == XMLEvent.END_DOCUMENT){
                    System.out.println("Document bitti");
                }
            }
            System.out.println();
            valuteList.forEach(System.out::println);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
