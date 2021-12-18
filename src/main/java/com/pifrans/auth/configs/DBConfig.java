package com.pifrans.auth.configs;

import com.pifrans.auth.constants.SystemProfiles;
import com.pifrans.auth.services.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(SystemProfiles.DEV)
public class DBConfig {

    @Autowired
    private DBService dbService;

    @Bean
    public boolean instantiateDatabase() {
        dbService.init();
        return true;
    }
}
