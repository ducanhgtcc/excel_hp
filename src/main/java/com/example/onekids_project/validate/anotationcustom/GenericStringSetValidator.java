package com.example.onekids_project.validate.anotationcustom;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class GenericStringSetValidator implements ConstraintValidator<StringInList, String> {

   private Set<String> validValues;

   public void initialize(StringInList constraint) {
      validValues = Arrays.stream(constraint.values())
              .collect(toSet());
   }

   public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value==null || value=="") return true;
      return validValues.contains(value);
   }
}
