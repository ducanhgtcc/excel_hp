package com.example.onekids_project.mapper;

import com.example.onekids_project.response.brandManagement.FindAgentBrandResponse;
import com.example.onekids_project.response.brandManagement.ListAgentBrandResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * mapper for list
 */
public class ListMapper {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * thực hiện mapper giữa 2 List
     * @param source
     * @param targetClass
     * @param <S>
     * @param <T>
     * @return
     */
    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }


}
