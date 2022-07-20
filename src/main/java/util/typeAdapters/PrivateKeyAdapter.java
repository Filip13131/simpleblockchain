package util.typeAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import util.StringUtil;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

public class PrivateKeyAdapter extends TypeAdapter<PrivateKey> {
    @Override

    public PrivateKey read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String KeyAsString = reader.nextString();
        try {
            return StringUtil.getPrivateKeyFromString(KeyAsString);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(JsonWriter writer, PrivateKey value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(StringUtil.getStringFromKey(value));
    }
}