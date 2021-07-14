package com.rcdev.main_api.Config;

import com.rcdev.main_api.Controller.ApiController;
import com.rcdev.main_api.Filters.JwtRequestFilter;
import com.rcdev.main_api.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    UserService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    static Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //Set configuration on the auth object

        /*
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("admin")
                .roles("ADMIN");

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select usr_id, usr_pswrd, usr_enabled from user_details where usr_id = ?")
                .authoritiesByUsernameQuery("select usr_id, usr_role from user_details where usr_id = ?")
                .passwordEncoder(passwordEncoder());

         */

        auth.userDetailsService(userDetailsService);

    }

    /*
    @Bean
    public PasswordEncoder getPasswordEncoder() {return NoOpPasswordEncoder.getInstance(); }
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
        http.authorizeRequests()
                .antMatchers("/swagger-ui/").hasAnyAuthority("ADMIN","USER")
                .antMatchers("/api/*").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/*").permitAll()
                .and()
                .formLogin();

        http.cors().and().csrf().disable();
         */

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/swagger-ui/").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return getMd5(charSequence.toString());
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return getMd5(charSequence.toString()).equals(s);
            }
        };
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(final WebSecurity webSecurity) {
        webSecurity.httpFirewall(allowUrlEncodedSlashHttpFirewall());
        webSecurity.ignoring().antMatchers(
                "/v2/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html");
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    public static String getMd5(String usr_pswrd) {
        logger.info("Password revieved from web: " + usr_pswrd);
        try{
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(usr_pswrd.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            logger.info("Hashed value is equal to: " + hashtext.toString());
            return hashtext.toString();

        }catch (Error | NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }

    }

}
