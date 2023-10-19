package dev.marcoreitano.master.amundsen;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class AmundsenApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void writeDocumentationSnippets() {

        var modules = ApplicationModules.of(AmundsenApplication.class).verify();

//        modules.forEach(System.out::println);
        
        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }

    @Test
    void verifyApplicationModuleStructure() {
        ApplicationModules.of(AmundsenApplication.class).verify();
    }

}
