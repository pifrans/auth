package com.pifrans.auth.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GenericMapper<M, D> {
    private final ModelMapper modelMapper;

    public GenericMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public M toModel(D object, Class<M> classModel) {
        return modelMapper.map(object, classModel);
    }

    public D toDto(M object, Class<D> classDto) {
        return modelMapper.map(object, classDto);
    }
}
