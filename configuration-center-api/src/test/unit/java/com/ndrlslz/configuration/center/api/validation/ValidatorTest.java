//package com.ndrlslz.configuration.center.api.validation;
//
//import com.ndrlslz.configuration.center.api.exception.InvalidRequestBodyException;
//import com.ndrlslz.configuration.center.api.json.common.Type;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//
//public class ValidatorTest {
//
//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();
//
//    @Test
//    public void shouldThrowExceptionGivenObjectIsNull() {
//        expectedException.expect(InvalidRequestBodyException.class);
//        expectedException.expectMessage("name cannot be null");
//
//        Validator.checkNotNull(null, "name cannot be null");
//    }
//
//    @Test
//    public void shouldPassValidationGivenObjectIsNotNull() {
//        Validator.checkNotNull(new Object(), "object cannot be null");
//    }
//
//    @Test
//    public void shouldThrowExceptionGivenExpressionIsFalse() {
//        expectedException.expect(InvalidRequestBodyException.class);
//        expectedException.expectMessage("expression error");
//
//        Validator.checkState(false, "expression error");
//    }
//
//    @Test
//    public void shouldPassValidationGivenExpressionIsTrue() {
//        Validator.checkState(true, "expression pass");
//    }
//}