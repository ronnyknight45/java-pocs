package com.github.diegopacheco.java.pocs.xstream.converter;

import com.github.diegopacheco.java.pocs.xstream.pojos.Contact;
import com.github.diegopacheco.java.pocs.xstream.pojos.Contacts;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.path.PathTrackingReader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PojoToListConverter implements Converter {

    private List<String> canConvertClass;

    public PojoToListConverter() {
        this.canConvertClass = Arrays.asList(Contacts.class.getSimpleName());
    }

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext ctx) {
        Contacts c = (Contacts)o;
        for(Contact contact : c.getContacts()){
            writeToXML(contact,"ID",writer,Integer.class);
            writeToXML(contact,"Name",writer,String.class);
            writeToXML(contact,"Active",writer,Boolean.class);
            writeToXML(contact,"Email",writer,String.class);
            writeToXML(contact,"SpokenLanguages",writer,List.class);
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext ctx) {
        reader.moveDown();
        Contacts c = new Contacts();
        List<Contact> contacts = new ArrayList<>();

        String tag = reader.getNodeName();
        while("ID".equals(tag)){
            Contact contact = new Contact();
            writeToPojo(contact,"ID",reader,Integer.class);
            writeToPojo(contact,"Name",reader,String.class);
            writeToPojo(contact,"Active",reader,Boolean.class);
            writeToPojo(contact,"Email",reader,String.class);
            writeToPojo(contact,"SpokenLanguages",reader,List.class);
            contacts.add(contact);
            tag = reader.getNodeName();
        }

        c.setContacts(contacts);
        return c;
    }

    private void writeToXML(Object pojo, String field, HierarchicalStreamWriter writer, Class classType) {
        try {
            Method getter = pojo.getClass().getDeclaredMethod("get" + field);
            Object value = getter.invoke(pojo,null);
            switch (classType.getSimpleName()) {
                case "String":
                    writer.startNode(field);
                    if (null == value) {
                        writer.setValue("");
                    } else {
                        writer.setValue(value.toString());
                    }
                    writer.endNode();
                    break;
                case "Integer":
                    writer.startNode(field);
                    if (null == value) {
                        writer.setValue("0");
                    } else {
                        writer.setValue(value.toString());
                    }
                    writer.endNode();
                    break;
                case "Boolean":
                    writer.startNode(field);
                    if (null == value) {
                        writer.setValue("true");
                    } else {
                        writer.setValue(value.toString());
                    }
                    writer.endNode();
                    break;
                case "List":
                    if (null == value) {
                        writer.startNode(field);
                        writer.setValue(new ArrayList<String>().toString());
                        writer.endNode();
                    } else {
                        List<String> list = (List<String>)value;
                        for(String s: list){
                            writer.startNode(field);
                            writer.setValue(s);
                            writer.endNode();
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToPojo(Object pojo, String field, HierarchicalStreamReader reader, Class classType) {
        try {

            if (!field.equals(reader.getNodeName())){
                return;
            }

            Method setter = pojo.getClass().getDeclaredMethod("set" + field, classType);
            Object value = reader.getValue();
            switch (classType.getSimpleName()) {
                case "String":
                    if (null == value) {
                        setter.invoke(pojo, "");
                    } else {
                        setter.invoke(pojo, value);
                    }
                break;
                case "Integer":
                    if (null == value) {
                        setter.invoke(pojo, 0);
                    } else {
                        setter.invoke(pojo, Integer.parseInt(value.toString()));
                    }
                break;
                case "Boolean":
                    if (null == value) {
                        setter.invoke(pojo, true);
                    } else {
                        setter.invoke(pojo, Boolean.parseBoolean(value.toString()));
                    }
                break;
                case "List":
                    if (null == value) {
                        setter.invoke(pojo, new ArrayList<>());
                    } else {
                        List<String> list = new ArrayList<>();
                        String tag = reader.getNodeName();
                        while(field.equals(tag)){
                            list.add(reader.getValue());
                            reader.moveUp();
                            if (((PathTrackingReader) reader).peekNextChild()==null){
                                setter.invoke(pojo,list);
                                return;
                            }
                            reader.moveDown();
                            tag = reader.getNodeName();
                        }
                        setter.invoke(pojo,list);
                        return;
                    }
                break;
            }
            reader.moveUp();
            reader.moveDown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canConvert(Class aClass) {
        return canConvertClass.contains(aClass.getSimpleName());
    }

}