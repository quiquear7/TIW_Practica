package controller;

import java.lang.reflect.Type;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.utils.Base64;

//Esta clase resuelve el problema de javax.ws.rs.ProcessingException: Error deserializing object from entity stream.
//Incluyendo las siguientes anotaciones en el campo array de byte del dominio/modelo (en el proyecto web dinamico)
//@JsonbTypeDeserializer(BytesSerializerDeserializer.class)
//@JsonbTypeSerializer(BytesSerializerDeserializer.class)
//byte [] imagen;
public class BytesSerializerDeserializer implements JsonbDeserializer<byte[]>, JsonbSerializer<byte[]> {

    @Override
    public byte[] deserialize(JsonParser arg0, DeserializationContext arg1, Type arg2) {
        // TODO Auto-generated method stub
        byte []raw=null;
        try {
            raw= Base64.decode(arg0.getString());
        } catch (Base64DecodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return raw;
    }

    @Override
    public void serialize(byte[] arg0, JsonGenerator arg1, SerializationContext arg2) {
        // TODO Auto-generated method stub
        arg1.write(Base64.encode(arg0));
    }
}
