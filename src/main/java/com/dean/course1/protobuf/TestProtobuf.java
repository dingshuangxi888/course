package com.dean.course1.protobuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 测试protobuf
 * Created by dean on 14/12/7.
 */
public class TestProtobuf {

    public static void main(String[] args) throws IOException {
        MyPerson.Person.Builder personBuilder = MyPerson.Person.newBuilder();
        personBuilder.setId(1);
        personBuilder.setName("dean");
        personBuilder.setEmail("xxx@abc.com");

        MyPerson.Person person = personBuilder.build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        person.writeTo(outputStream);
        byte[] bytes = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        MyPerson.Person person2 = MyPerson.Person.parseFrom(inputStream);
        System.out.println("person id: " + person2.getId());
        System.out.println("person name: " + person2.getName());
        System.out.println("person email: " + person2.getEmail());
    }
}
