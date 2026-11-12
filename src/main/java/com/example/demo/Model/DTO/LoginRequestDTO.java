package com.example.demo.Model.DTO;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@Builder
public class    LoginRequestDTO {

    private String email;
    private String password;

}