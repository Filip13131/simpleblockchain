package util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class PublicKeyAdapter extends TypeAdapter<PublicKey> {
    public PublicKey read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String KeyAsString = reader.nextString();
        try {
            return StringUtil.getKeyFromString(KeyAsString);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(JsonWriter writer, PublicKey value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(StringUtil.getStringFromKey(value));
    }
}