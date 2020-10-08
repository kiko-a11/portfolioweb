package jp.summit_tech.conf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * WebSecurityConfig is for security (login, authority, etc)
 *
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/resources/**");
    }

    /**
     * Control login information.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
        .authoritiesByUsernameQuery(
            "SELECT"
            + "      id as username"
            + "    , authority as authority "
            + "FROM"
            + "    t_users "
            + "WHERE"
            + "    id = ? "
         )
        .usersByUsernameQuery(
            "SELECT"
            + "      id as username"
            + "    , password as password "
            + "    , true as enabled "
            + "FROM"
            + "    t_users "
            + "WHERE"
            + "    id = ? "
        );
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/login", "/regist", "/publish/{id}")
            .permitAll().anyRequest().authenticated();

//        http.csrf().disable().formLogin().loginProcessingUrl("/loginprocessing")
        http.formLogin().loginProcessingUrl("/loginprocessing")
            .loginPage("/login")
            .failureUrl("/login?error=failed")
            .defaultSuccessUrl("/home", true)
            .usernameParameter("user").passwordParameter("pass");

        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login?warn=logout");
    }
}