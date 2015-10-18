package karl.codes.minecraft.spyeventbus.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import karl.codes.minecraft.spyeventbus.action.EventRule;

import java.util.List;

/**
 * Created by karl on 10/15/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    private List<EventRule> rules;

    public Config() {
    }

    public void setRules(List<EventRule> rules) {
        this.rules.addAll(rules);
    }

    public List<EventRule> getRules() {
        return rules;
    }
}
