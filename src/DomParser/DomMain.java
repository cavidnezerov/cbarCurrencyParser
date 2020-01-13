package DomParser;

import Valute.Valute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DomMain {

    public static void main(String[] args) {


        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String link = "https://www.cbar.az/currencies/" + formatter.format(LocalDate.now()) + ".xml";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(link);
            document.normalizeDocument();
            List<Valute> valuteList = new ArrayList<>();
            Element root = document.getDocumentElement();

            System.out.println(root.getAttribute("Description"));

            Node valTypeNode = root.getElementsByTagName("ValType").item(1);
            Element valTypeElement = (Element) valTypeNode;
            NodeList valuteNodeList = (valTypeElement.getElementsByTagName("Valute"));
            for (int j = 0; j < valuteNodeList.getLength(); j++) {

                Node valuteNode = valuteNodeList.item(j);
                if (valuteNode.getNodeType() == Node.ELEMENT_NODE){

                    Element valuteElement  = (Element) valuteNode;
                    Valute valute = new Valute();
                    String data;

                    data = valuteElement.getAttribute("Code");
                    valute.setCode(data);

                    data = valuteElement.getElementsByTagName("Nominal").item(0).getTextContent();
                    valute.setNominal(Integer.parseInt(data));

                    data = valuteElement.getElementsByTagName("Name").item(0).getTextContent();
                    valute.setName(data);

                    data = valuteElement.getElementsByTagName("Value").item(0).getTextContent();
                    valute.setValue(new BigDecimal(data));

                    valuteList.add(valute);

                }
            }
            valuteList.forEach(System.out::println);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }


    }
}
