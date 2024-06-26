package com.github.diegopacheco.java.pocs.xstream.test;

import com.github.diegopacheco.java.pocs.xstream.pojos.ContactItem;
import com.github.diegopacheco.java.pocs.xstream.pojos.ContactRoot;
import com.github.diegopacheco.java.pocs.xstream.pojos.Contacts;
import com.github.diegopacheco.java.pocs.xstream.serialization.SerializationService;
import com.github.diegopacheco.java.pocs.xstream.serialization.XStreamSerializationService;
import com.github.diegopacheco.java.pocs.xstream.serializationv2.XStreamAdvancedSerializationService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XstreamSerializationServiceTest {

    @Test
    public void testMethod1() {
        String xml = "<contacts>" +
                "<ID>1</ID>" +
                "<Name>Diego</Name>" +
                "<Active>True</Active>" +
                "<Email>diego@diego.com</Email>" +
                "<SpokenLanguages>Portuguese</SpokenLanguages>" +
                "<SpokenLanguages>English</SpokenLanguages>" +
                "<ID>2</ID>" +
                "<Name>Gandalf</Name>" +
                "<Active>True</Active>" +
                "<Email>gan@white.com</Email>" +
                "<SpokenLanguages>Catness</SpokenLanguages>" +
                "<SpokenLanguages>Portuguese</SpokenLanguages>" +
                "<ID>3</ID>" +
                "<Name>Melina</Name>" +
                "<Email>mel@mel.com</Email>" +
                "<SpokenLanguages>Catness</SpokenLanguages>" +
                "<SpokenLanguages>Portuguese</SpokenLanguages>" +
                "</contacts>";
        SerializationService serialization = new XStreamSerializationService();
        Contacts contacts = serialization.deserialize(xml);
        assertNotNull(contacts);
        System.out.println(contacts);

        String xmlAgain = serialization.serialize(contacts);
        assertNotNull(xmlAgain);
        System.out.println(xmlAgain);
    }

    @Test
    public void testMethod2FromPojo() {

        ContactRoot tempRoot = new ContactRoot();

        List<ContactItem> ContactItems = new ArrayList<>();
        ContactItem tempItem = new ContactItem();
        tempItem.setID(1);
        tempItem.setActive(true);
        tempItem.setEmail("e@e.com");
        tempItem.setName("e");
        tempItem.setSpokenLanguages(Arrays.asList("a", "b"));
        ContactItems.add(tempItem);

        tempItem = new ContactItem();
        tempItem.setID(2);
        tempItem.setActive(true);
        tempItem.setEmail("f@f.com");
        tempItem.setName("f");
        tempItem.setSpokenLanguages(Arrays.asList("a", "b"));
        ContactItems.add(tempItem);

        tempItem = new ContactItem();
        tempItem.setID(3);
        tempItem.setActive(true);
        tempItem.setEmail("g@g.com");
        tempItem.setName("g");
        tempItem.setSpokenLanguages(Arrays.asList("a", "b"));
        ContactItems.add(tempItem);

        tempRoot.setContactItems(ContactItems);

        SerializationService ser = new XStreamSerializationService();
        String tempXml = ser.serialize(tempRoot);
        String prepDeserialize = ser.serializePreProcess(tempRoot);

        System.out.println("XML no pre-process: ");
        System.out.println(tempXml);

        System.out.println("XMl with pre-process");
        System.out.println(prepDeserialize);

        System.out.println("original XML and deserialize now... ");
        String pre = ser.preProcess(prepDeserialize);
        System.out.println(pre);
        ContactRoot r2 = ser.deserialize(pre);
        System.out.println(r2);

    }

    @Test
    public void testMethod2FromXml() {
        String xml = "<ContactRoot>\n" +
                "    <ID>1</ID>\n" +
                "    <Name>e</Name>\n" +
                "    <Email>e@e.com</Email>\n" +
                "    <Active>true</Active>\n" +
                "    <SpokenLanguages>a</SpokenLanguages>\n" +
                "    <SpokenLanguages>b</SpokenLanguages>\n" +
                "    <ID>2</ID>\n" +
                "    <Name>f</Name>\n" +
                "    <Email>f@f.com</Email>\n" +
                "    <Active>true</Active>\n" +
                "    <SpokenLanguages>a</SpokenLanguages>\n" +
                "    <SpokenLanguages>b</SpokenLanguages>\n" +
                "    <ID>3</ID>\n" +
                "    <Name>g</Name>\n" +
                "    <Email>g@g.com</Email>\n" +
                "    <Active>true</Active>\n" +
                "    <SpokenLanguages>a</SpokenLanguages>\n" +
                "    <SpokenLanguages>b</SpokenLanguages>\n" +
                "</ContactRoot>";
        SerializationService serialization = new XStreamSerializationService();
        String preProcessedXML = serialization.preProcess(xml);
        System.out.println(preProcessedXML);
        ContactRoot c = serialization.deserialize(preProcessedXML);
        System.out.println(c);
    }

    @Test
    public void testMethod2Generic() {
        ContactRoot tempRoot = new ContactRoot();

        List<ContactItem> ContactItems = new ArrayList<>();
        ContactItem tempItem = new ContactItem();
        tempItem.setID(1);
        tempItem.setActive(true);
        tempItem.setEmail("e@e.com");
        tempItem.setName("e");
        tempItem.setSpokenLanguages(Arrays.asList("a", "b"));
        ContactItems.add(tempItem);

        tempItem = new ContactItem();
        tempItem.setID(2);
        tempItem.setActive(true);
        tempItem.setEmail("f@f.com");
        tempItem.setName("f");
        tempItem.setSpokenLanguages(Arrays.asList("a", "b"));
        ContactItems.add(tempItem);

        tempItem = new ContactItem();
        tempItem.setID(3);
        tempItem.setActive(true);
        tempItem.setEmail("g@g.com");
        tempItem.setName("g");
        tempItem.setSpokenLanguages(Arrays.asList("a", "b"));
        ContactItems.add(tempItem);

        tempRoot.setContactItems(ContactItems);

        XStreamAdvancedSerializationService ser = new XStreamAdvancedSerializationService();
        String tempXml = ser.serialize(tempRoot);
        System.out.println(tempXml);

        ContactRoot rootBack =  ser.deserialize(tempXml,ContactRoot.class);
        System.out.println(rootBack);
    }

}
