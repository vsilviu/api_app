package ro.apisec.ms.demo.controller.profiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("production")
public class Bean1 implements Stuff {

    @Autowired
    public Bean1(@Value("${profile.property.stuff}") String stuff) {
        System.out.println("production:" + stuff);
    }

    @Override
    public void execute() {
        System.out.println("execute production");
    }
}
