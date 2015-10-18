package karl.codes.minecraft.spyeventbus.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * Created by karl on 10/15/2015.
 */
public class ConfigTest {
    private ObjectMapper json;

    @Before
    public void setUp() throws Exception {
        json = new ObjectMapper();
        json.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
    }

    @After
    public void tearDown() throws Exception {
        json = null;
    }

    @Test
    public void testSetRules() throws Exception {
        String data = "{'rules': [{'test':{'derp':1}}]}";
        StringReader src = new StringReader(data);
        json.readValue(src,Config.class);
    }
}