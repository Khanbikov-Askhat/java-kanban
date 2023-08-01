package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Long> {

    @Override
    public void write(final JsonWriter jsonWriter, final Long value) throws IOException {
        //jsonWriter.value(duration.toString());
        if (value == null) {
            jsonWriter.nullValue();
            return;
        }
        String text = "\"" + String.valueOf(value) + "\"";
        jsonWriter.value(text);
    }

    @Override
    public Long read(final JsonReader jsonReader) throws IOException {
        String text = jsonReader.nextString();
        text = text.replaceAll("\"", "");
        Long value = Long.parseLong(text);
        return value;
    }
}
