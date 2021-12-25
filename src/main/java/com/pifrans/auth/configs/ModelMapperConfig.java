package com.pifrans.auth.configs;

import com.pifrans.auth.dtos.users.UserSimpleDTO;
import com.pifrans.auth.models.Profile;
import com.pifrans.auth.models.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Converters
        Converter<Set<Profile>, Integer> setProfileIntegerConverter = ctx -> ctx.getSource().size();

        // TypeMaps
        TypeMap<User, UserSimpleDTO> userUserSimpleDTOTypeMap = mapper.createTypeMap(User.class, UserSimpleDTO.class);
        userUserSimpleDTOTypeMap.addMappings(m -> m.using(setProfileIntegerConverter).map(User::getProfiles, UserSimpleDTO::setProfiles));

        return mapper;
    }
}
