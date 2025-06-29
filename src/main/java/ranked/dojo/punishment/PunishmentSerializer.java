package ranked.dojo.punishment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class PunishmentSerializer {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public List<String> serialize(List<Punishment> punishments) {
        if (punishments == null || punishments.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> serialized = new ArrayList<>();
        for (Punishment punishment : punishments) {
            serialized.add(gson.toJson(punishment));
        }
        return serialized;
    }

    public List<Punishment> deserialize(List<String> serialized) {
        if (serialized == null || serialized.isEmpty()) {
            return Collections.emptyList();
        }
        List<Punishment> punishments = new ArrayList<>();
        for (String string : serialized) {
            punishments.add(gson.fromJson(string, Punishment.class));
        }
        return punishments;
    }
}

