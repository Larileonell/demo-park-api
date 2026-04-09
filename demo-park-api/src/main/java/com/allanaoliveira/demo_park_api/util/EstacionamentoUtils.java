package com.allanaoliveira.demo_park_api.util;

import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class EstacionamentoUtils {


    public static final String GegarRecibo(){
        LocalDate date = LocalDate.now();
        String recibo = date.toString().substring(0,19);
        return recibo.replace("-", "")
                .replace(":", "")
                .replace(":", "")
                .replace("T", "-");
    }


}
