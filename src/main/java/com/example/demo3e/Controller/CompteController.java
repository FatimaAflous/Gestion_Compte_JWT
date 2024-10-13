package com.example.demo3e.Controller;

import com.example.demo3e.entity.Compte;
import com.example.demo3e.service.CompteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;
import java.time.temporal.ChronoUnit;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
@RestController
@RequestMapping("/v1/api")
public class CompteController {
    private AuthenticationManager authenticationManager;
    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;
    private UserDetailsService userDetailsService;

    @Autowired
    CompteService compteService;

//constructeur
    public CompteController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userDetailsService = userDetailsService;
    }

    public String generateToken(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        Instant now = Instant.now();
        long expiry = 3600L;
        String scopes = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("MS_sec")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(user.getUsername())
                .claim("role", scopes)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    //methode login
    @PostMapping("/login")
    Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        System.out.println("Tentative d'authentification pour l'utilisateur : " + username);
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            System.out.println("Authentification réussie pour l'utilisateur : " + username);
        } catch (Exception e) {
            System.out.println("Échec de l'authentification : " + e.getMessage());
            throw e;
        }
        // Utilisez la méthode generateToken pour générer l'AccessToken
        String AccessToken = generateToken(authenticate);
        // Refresh Token
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSet_RefreshToken = JwtClaimsSet.builder()
                .issuer("MS_sec")
                .subject(authenticate.getName())
                .issuedAt(instant)
                .expiresAt(instant.plus(15, ChronoUnit.MINUTES))
                .claim("name", authenticate.getName())
                .claim("role", authenticate.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" ")))
                .build();
        String RefreshToken;
        try {
            RefreshToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet_RefreshToken)).getTokenValue();
            System.out.println("RefreshToken généré : " + RefreshToken);
        } catch (Exception e) {
            System.out.println("Échec de la génération du Refresh Token : " + e.getMessage());
            throw e;
        }
        Map<String, String> ID_Token = new HashMap<>();
        ID_Token.put("Access_Token", AccessToken);
        ID_Token.put("Refresh_Token", RefreshToken);
        System.out.println("Tokens générés avec succès.");
        return ID_Token;
    }

//RefreshToken
    @PostMapping("/RefreshToken")
    public Map<String,String>fr_t(String RefreshToken){
        if(RefreshToken==null){
            return Map.of("Message error","Refresh_Token est necessaire");
        }
        // verifier la signature (est ce que token est valide ou non)
        Jwt decoded = jwtDecoder.decode(RefreshToken);
        String username = decoded.getSubject();
        //userDetailsService : donne toute les onfos de l'utilisateur
        UserDetails userDetails=userDetailsService.loadUserByUsername(username);
        //Renouveller l'access token
        Instant instant = Instant.now();
        //recuperer Scopes dans chaine scopes et separe avec des espaces
        String scopes = userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.joining(" "));
        //Payhod (Ensemble de claims)
        //Access token
        JwtClaimsSet jwtClaimsSet_AccessToken = JwtClaimsSet.builder()
                .issuer("MS_sec")
                .subject(userDetails.getUsername())
                .issuedAt(instant)
                .expiresAt(instant.plus(2, ChronoUnit.MINUTES))
                .claim("name", userDetails.getUsername())
                //Roles d'utilisateur
                .claim("role", scopes)
                .build();

        //signe token
        //Recupere Access Token
        String AccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet_AccessToken)).getTokenValue();

        Map<String, String> ID_Token = new HashMap<>();
        ID_Token.put("Access_Token", AccessToken);
        ID_Token.put("Refresh_Token", RefreshToken);
        return ID_Token;
    }


//recuperer tous les comptes :
@GetMapping("/admin/recuperation_comptes")
@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Compte>> recuperer_comptes(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("Détails de l'authentification : " + authentication);
    return ResponseEntity.ok(compteService.consulter_tout_comptes());
    }

  //recuperer un compte specifique
    @GetMapping("/compte/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Compte> recuperer_un_compte(@PathVariable Integer id) {
        Compte compte = compteService.consulter_compte(id);
        if (compte == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(compte);
    }

    //creer compte
    @GetMapping("/admin/create_compte")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Compte> create_compte(@RequestBody Compte compte){
        return ResponseEntity.ok(compteService.creer_compte(compte));
    }

    // modifier un compte
    @PutMapping("/admin/modifier_compte/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Compte> update_compte(@PathVariable Integer id , @RequestBody Compte compte){
       return ResponseEntity.ok(compteService.update_compte(id , compte));
    }

    //supprimer compte
    @DeleteMapping("/admin/delete_compte/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> supprimer_compte(@PathVariable Integer id){
        compteService.delete_compte(id);
        return ResponseEntity.noContent().build();
    }

    //crediter un compte
    @PostMapping("/user/crediter/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Compte> crediter_compte(@PathVariable Integer id , @RequestParam Long montant){
                return ResponseEntity.ok(compteService.crediter_compte(id,montant));
    }

    //debiter un compte
    @PostMapping("/user/debiter/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Compte> debiter_compte(@PathVariable Integer id , @RequestParam Long montant){
        return ResponseEntity.ok(compteService.debiter_compte(id,montant));
    }
}
