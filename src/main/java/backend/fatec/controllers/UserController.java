package backend.fatec.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.fatec.dtos.ErrorResponseRecordDto;
import backend.fatec.dtos.LoginRequestRecordDto;
import backend.fatec.dtos.LoginResponseRecordDto;
import backend.fatec.dtos.SuccessResponseRecordDto;
import backend.fatec.dtos.UserRecordDto;
import backend.fatec.dtos.ValidateTokenRecordDto;
import backend.fatec.helpers.JwtHelper;
import backend.fatec.models.User;
import backend.fatec.repositories.UserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtHelper jwtHelper;
    
    @GetMapping("/admin/user")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> setUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        try {
            var user = new User();
            BeanUtils.copyProperties(userRecordDto, user);

            List<Optional<User>> existingUser = userRepository.checkIfEmailIsUsed(user.getEmail());
            if(existingUser.size() > 0  && existingUser.get(0).isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseRecordDto("Email cadastrado"));
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao realizar cadastro"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestRecordDto request) {
        System.out.println(request);
        try{
            Optional<User> userExists = userRepository.findByEmail(request.getEmail());
            if(userExists.isPresent()) {
                User user = userExists.get();
                Boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
                if(passwordMatches) {
                    String token = jwtHelper.generateToken(request.getEmail());
                    return ResponseEntity.ok(new LoginResponseRecordDto(request.getEmail(), user, token, "Login realizado"));
                }

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseRecordDto("Senha incorreta"));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseRecordDto("Usuário não encontrado"));
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao realizar login"));
        }
    }

    @PostMapping("/validate_token")
    public ResponseEntity<?> validateToken(@Valid @RequestBody ValidateTokenRecordDto request) {
        try{
            Boolean isValidToken = jwtHelper.validateToken(request.token());
            System.out.println("isValidToken");
            System.out.println(isValidToken);
            if(isValidToken){
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseRecordDto("Token válido"));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseRecordDto("Token inválido ou expirado"));
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao validar o token"));
        }
    }

    public Boolean checkIfUserIsAdmin(String email) {
        System.out.println(email);
        try{
            Optional<User> user = userRepository.findByEmail(email);
            if(user.isPresent()){
                return user.get().getAdmin();
            }

            return false;
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        }
    }
}