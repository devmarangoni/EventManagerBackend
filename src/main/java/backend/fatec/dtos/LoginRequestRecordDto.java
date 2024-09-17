package backend.fatec.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestRecordDto(
    @NotBlank String email,
    @NotBlank String password
){
    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}