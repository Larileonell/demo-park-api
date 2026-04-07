package com.allanaoliveira.demo_park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteCreateDto {
    @NotBlank(message = "O nome do cliente é obrigatório")
    @Size(min= 5, max = 100, message = "O nome do cliente deve conter no máximo 250 caracteres")
    private String nome;


    @Size(min=11, max= 11, message = "O CPF deve conter exatamente 11 caracteres")
    @CPF(message = "O CPF deve ser válido")
    private String cpf;

}
