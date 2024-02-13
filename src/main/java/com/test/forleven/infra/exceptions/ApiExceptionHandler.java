package com.test.forleven.infra.exceptions;

import com.test.forleven.infra.config.utils.DateTimeUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        //Lista os campos com erro.
        List<ErrorApi.Field> campos = new ArrayList<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()){
            String nome = ((FieldError) error).getField();
            String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());

            campos.add(new ErrorApi.Field(nome, mensagem));
        }

        ErrorApi errorApi = new ErrorApi();
        errorApi.setStatus(status.value());

        String dataHoraAtualFormatada = DateTimeUtil.obterDataHoraAtualFormatada();
        errorApi.setDateTime(DateTimeUtil.converterStringParaLocalDateTime(dataHoraAtualFormatada));

        errorApi.setTitle("Um ou mais campos estão vazios, não pode haver campos em branco.");
        errorApi.setFields(campos);

        return handleExceptionInternal(ex,errorApi,headers,status,request);
    }
    //Erro 400.
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorApi errorApi = new ErrorApi();
        errorApi.setStatus(status.value());

        String dataHoraAtualFormatada = DateTimeUtil.obterDataHoraAtualFormatada();
        errorApi.setDateTime(DateTimeUtil.converterStringParaLocalDateTime(dataHoraAtualFormatada));

        errorApi.setTitle(ex.getMessage());
        return handleExceptionInternal(ex, errorApi, new HttpHeaders(), status,request);
    }
    //Erro 404.
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<Object> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorApi errorApi = new ErrorApi();
        errorApi.setStatus(status.value());

        String dataHoraAtualFormatada = DateTimeUtil.obterDataHoraAtualFormatada();
        errorApi.setDateTime(DateTimeUtil.converterStringParaLocalDateTime(dataHoraAtualFormatada));

        errorApi.setTitle(ex.getMessage());
        return handleExceptionInternal(ex, errorApi, new HttpHeaders(), status,request);
    }


}
