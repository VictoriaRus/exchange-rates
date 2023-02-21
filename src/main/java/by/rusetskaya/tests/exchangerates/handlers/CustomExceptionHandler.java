package by.rusetskaya.tests.exchangerates.handlers;

import by.rusetskaya.tests.exchangerates.errors.CustomRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class, Exception.class })
    public ModelAndView exceptRuntimeException(
            RuntimeException exception
    ) {
        String message = exception.getMessage();

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("message", message);
        modelAndView.setStatus(HttpStatus.valueOf(500));

        return modelAndView;
    }

    @ExceptionHandler(value = {CustomRuntimeException.class })
    public ModelAndView exceptCustomRuntimeException(
            CustomRuntimeException exception
    ) {
        int statusCode = exception.getStatusCode();
        String message = exception.getMessage();

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", message);
        modelAndView.setStatus(HttpStatus.valueOf(statusCode));

        return modelAndView;
    }
}
