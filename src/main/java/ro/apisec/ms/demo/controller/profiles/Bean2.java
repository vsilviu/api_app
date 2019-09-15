package ro.apisec.ms.demo.controller.profiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class Bean2 implements Stuff {

    @Autowired
    public Bean2(@Value("${profile.property.stuff}") String stuff) {
        System.out.println("dev:" + stuff);
    }

    @Override
    public void execute() {
        System.out.println("execute dev");
    }
}
