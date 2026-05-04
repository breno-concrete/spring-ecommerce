package com.breno.marketplace_test.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.breno.marketplace_test.dtos.LoginRequestDTO;
import com.breno.marketplace_test.dtos.UserRequestDTO;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.repositories.UserRepository;
import com.breno.marketplace_test.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class) //usa o mockito aqui para criar "dublês" automaticamente
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks //implmenta o objeto real que queremos usar injetando os mocks que colocamos anteriormente
    private AuthService authService;


    @Test
    void login_ReturnToken_When_ValidCredentials() {

        //ARRANGE

        //cria DTO fake para simular a entrada do método
        LoginRequestDTO dto = new LoginRequestDTO("user@email.com", "senha123");

        //Cria authentication fake para simular o comportamento do AuthenticationManager
        Authentication authMock = mock(Authentication.class);


        //quando for autenticar qualquer coisa retorne o authMock
        when(authenticationManager.authenticate(any())).thenReturn(authMock);

        //devolver o nome do usuário para o authMock
        when(authMock.getName()).thenReturn("user@email.com");

        //gerar token fake uqnaod usado o email dado anteoriomente
        when(jwtTokenProvider.generateToken("user@email.com")).thenReturn("token-fake-123");


        //ACT


        //CHAMA O MÉTODO QUE QUEREMOS TESTAR
        String token = authService.login(dto);

        //ASSERT

        //VERIFICAR SE O MÉTODO GEROU O TOKEN ESPERADO
        assertThat(token).isEqualTo("token-fake-123");
    }


    @Test
    void login_ThorwsException_When_InvalidCredentials() {

        //ARRANGE

        //DTO d elogin com senha errada
        LoginRequestDTO dto = new LoginRequestDTO("user@email.com", "wrongpassword");

        //Qualquer auth tem que lançar badcredentialsexception
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        //ACT & ASSERT

        //verificamos se dá certo, uso de lambda par ao erro não explodir na minha cara antes de passar pelo teste
        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid credentials");
    }

    @Test
    void register_SavesUser_When_ValidData() {
        //ARRANGE
        UserRequestDTO dto = new UserRequestDTO("john@email.com", "john", "senha123", "61999999999");

        //Simula que o email não existe ainda
        when(userRepository.existsByEmail("john@email.com")).thenReturn(false);


        //Simula senha
        when(passwordEncoder.encode("senha123")).thenReturn("$2a$hash-fake");

        //ACT
        authService.register(dto);

        //ASSERT
        verify(userRepository).save(any(User.class));

    }


    @Test
    void register_ThrowsException_When_EmailAlreadyExists() {
        //ARRANGE
        UserRequestDTO dto = new UserRequestDTO("john@email.com", "john", "senha123", "61999999999");

        //Simula que o email já existe
        when(userRepository.existsByEmail("john@email.com")).thenReturn(true);

        //ACT & ASSERT

        //verifica se se lança uma exception com uma mensagem expecifica
        assertThatThrownBy(() -> authService.register(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already in use");

        //certifica eu o save() nunca foi chamado
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_HasToCodePassword_BeforeSave() {
        //ARRANGE
        UserRequestDTO dto = new UserRequestDTO("breno@email.com", "Breno Silva", "senha-original", "1199999999");

        //Simula que o email não existe ainda
        when(userRepository.existsByEmail(any())).thenReturn(false);
        //Simula o retorno do emtodo encode seja a senha criptografada
        when(passwordEncoder.encode("senha-original")).thenReturn("senha-codificada");

        //ACT
        //registrar
        authService.register(dto);

        //ASSERT
        //se o meteodo encode recebeu determinado argumento
        verify(passwordEncoder).encode("senha-original");


        //Captor para capturar qual objeto User foi passado no argumento
        var userCaptor = org.mockito.ArgumentCaptor.forClass(User.class);

        //Captura qual User foi pro save
        verify(userRepository).save(userCaptor.capture());

        //salva o user passado no save
        User userSalvo = userCaptor.getValue();

        //verificaa se o password é o codificado
        assertThat(userSalvo.getPasswordHash()).isEqualTo("senha-codificada");

        //Verifica se não é igual a senha original passada no DTO
        assertThat(userSalvo.getPasswordHash()).isNotEqualTo("senha-original");

    }

    @Test
    void register_SavesUserWithRoleUser() {
        //ARRANGE
        UserRequestDTO dto = new UserRequestDTO("breno@email.com", "Breno Silva", "11999999999", "senha123");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hash");

        //ACT
        authService.register(dto);

        //ASSERT

        var captor = org.mockito.ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User userSalvo = captor.getValue();


        assertThat(userSalvo.getRole()).isEqualTo(com.breno.marketplace_test.enums.UserRole.USER);
        assertThat(userSalvo.getRole()).isNotEqualTo(com.breno.marketplace_test.enums.UserRole.ADMIN);

        assertThat(userSalvo.getEmail()).isEqualTo("breno@email.com");
        assertThat(userSalvo.getFullName()).isEqualTo("Breno Silva");


    }
}