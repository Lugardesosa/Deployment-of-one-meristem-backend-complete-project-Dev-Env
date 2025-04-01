// TODO: TO REWORK AT A LATER TIME

//package org.meristem.oneapp.usersservice.validations.validators;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import org.meristem.oneapp.usersservice.validations.constraints.ExistsById;
//
//
//public class ExistsByIdValidator implements ConstraintValidator<ExistsById, Long> {
//
//    @Override
//    public void initialize(ExistsById constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
//    }
//
//    @Override
//    public boolean isValid(Long value, ConstraintValidatorContext context) {
//        if (value == null) {
//            return true;
//        }
//        return false;
//    }
//}
