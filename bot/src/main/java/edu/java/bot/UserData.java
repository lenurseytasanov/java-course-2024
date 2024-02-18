package edu.java.bot;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class UserData {
    private final Map<String, Set<String>> data;

    public UserData() {
        this.data = new HashMap<>();
    }

    public Map<String, Set<String>> getData() {
        return this.data;
    }
}
