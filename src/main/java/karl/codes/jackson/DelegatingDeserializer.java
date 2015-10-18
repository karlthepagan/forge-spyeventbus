package karl.codes.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Created by karl on 10/15/2015.
 */
public class DelegatingDeserializer extends JsonDeserializer<JsonNode> {
    @Override
    public JsonNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.readValueAsTree(); // see JsonNodeDeserializer.deserializeAny
//        node.traverse(p.getCodec()).readValueAs(Event.class); howto use this tree, codec is object mapper
        return node;
    }
}
