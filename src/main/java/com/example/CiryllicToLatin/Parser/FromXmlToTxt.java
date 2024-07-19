package com.example.CiryllicToLatin.Parser;

import com.example.CiryllicToLatin.DTO.ClientDto;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FromXmlToTxt {
    public List<ClientDto> convert(String filePath) {
        try{
            List<ClientDto> clients = new ArrayList<>();
            File inputFile = new File(filePath);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("person");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                    StringBuilder fullName = new StringBuilder();
                    Element eElement = (Element) nNode;
                    ClientDto client = new ClientDto();
                    //Получаем полное ФИО
                    fullName.append(eElement.getElementsByTagName("lname").item(0).getTextContent()).append(" ");
                    fullName.append(eElement.getElementsByTagName("fname").item(0).getTextContent()).append(" ");
                    fullName.append(eElement.getElementsByTagName("mname").item(0).getTextContent());
                    client.setFullName(fullName.toString());
                    clients.add(client);
                }
            }
            return clients;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
