package com.techforb.challenge.Commons;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserCommon {
    @NotNull
    @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b",
            message = "El email debe tener un formato válido")
    private String email;
    @NotNull
    @Pattern(regexp = "^.{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
}
