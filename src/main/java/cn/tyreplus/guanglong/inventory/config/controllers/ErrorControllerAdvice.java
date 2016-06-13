package cn.tyreplus.guanglong.inventory.config.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    String handleDataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException ex, Model model) {
        model.addAttribute("title" , "违反数据完整性");
        model.addAttribute("msg" , ex.getMessage());
		return "error";
    }

    @ExceptionHandler(Throwable.class)
    String handleDataIntegrityViolationException(HttpServletRequest request, Throwable ex, Model model) {
        model.addAttribute("title" , ex.getMessage());
        model.addAttribute("msg" , ex.getMessage());
		return "error";
    }



}