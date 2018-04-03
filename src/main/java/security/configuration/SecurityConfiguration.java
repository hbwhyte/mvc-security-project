package security.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import security.handlers.CustomAccessDeniedHandler;
import security.handlers.SimpleAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Autowired
    private SimpleAuthenticationSuccessHandler successHandler;

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }


    // Configuration to connect to the database
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                authorizeRequests()
                // Default (login) page, anyone can access
                .antMatchers("/").permitAll()
                // Login page, anyone can access
                .antMatchers("/login").permitAll()
                // Registration page, anyone can access
                .antMatchers("/registration").permitAll()
                // Any or null directories through admin are only accessible by admin
                // All requests must be authenticated
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                // Any other request besides those must be authenticated
                .anyRequest().authenticated()
                // This block configures login()
                // CSRF (Cross Site Request Forgery) was autoenabled with @EnableWebSecurity
                // Disabled it for the login form at "/login"
                .and().csrf().disable().formLogin().loginPage("/login")
                // Calls the Autowired SimpleAuthenticationSuccessHandler if login is successful
                .successHandler(successHandler)
                // Returns an inline error message if login fails
                .failureUrl("/login?error=true")
                // Uses user email as their unique username
                .usernameParameter("email")
                .passwordParameter("password")
                // This block configures logout()
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // Successful logout redirects to root directory
                .logoutSuccessUrl("/").and().exceptionHandling()
                // Handles redirect if user tries to access a page they don't have access to
                .accessDeniedHandler(accessDeniedHandler());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                // ignores my resources that are not views
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

}
