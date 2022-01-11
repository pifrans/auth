package com.pifrans.auth.configs;

import com.pifrans.auth.constants.SystemProfiles;
import com.pifrans.auth.services.DBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.UnexpectedRollbackException;

@Configuration
@Profile(SystemProfiles.DEV)
public class DBConfig {
    private static final Logger LOG = LoggerFactory.getLogger(DBConfig.class);
    private final DBService dbService;

    @Autowired
    public DBConfig(DBService dbService) {
        this.dbService = dbService;
    }

    @Bean
    public boolean instantiateDatabase() {
        try {
            dbService.init();
            return true;
        } catch (UnexpectedRollbackException e) {
            LOG.error(e.getStackTrace()[9].toString() + " --> " + e.getMessage());
            return false;
        }
    }
}
