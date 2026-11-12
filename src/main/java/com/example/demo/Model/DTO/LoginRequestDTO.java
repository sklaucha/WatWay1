package com.example.demo.Model.DTO;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor // Jackson lo necesita para armar el objeto desde el JSON
@AllArgsConstructor
@Builder
public class    LoginRequestDTO {

    private String email;
    private String password;

}