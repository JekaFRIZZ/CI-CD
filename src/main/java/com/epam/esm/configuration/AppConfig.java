package com.epam.esm.configuration;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.TagRepository;
import com.epam.esm.dao.UserOrderRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.security.UserDetailsServiceImpl;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserOrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.GiftCertificateValidator;
import com.epam.esm.validator.TagValidator;
import com.epam.esm.validator.UserOrderValidator;
import com.epam.esm.validator.UserValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
@PropertySource("classpath:database/db_init.properties")
@EnableTransactionManagement
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public TagRepository tagDao(EntityManager entityManager) {
        return new TagRepository(entityManager);
    }

    @Bean
    public GiftCertificateRepository giftCertificateDao(EntityManager entityManager) {
        return new GiftCertificateRepository(entityManager);
    }

    @Bean
    public UserOrderRepository userOrderDAO(EntityManager entityManager) {
        return new UserOrderRepository(entityManager);
    }

    @Bean
    public UserRepository userDAO(EntityManager entityManager) {
        return new UserRepository(entityManager);
    }

    @Bean
    public TagService tagService(EntityManager entityManager) {
        return new TagService(tagDao(entityManager),
                tagValidator());
    }

    @Bean
    public GiftCertificateService giftCertificateService(EntityManager entityManager) {
        return new GiftCertificateService(giftCertificateDao(entityManager),
                tagService(entityManager),
                giftCertificateValidator());
    }

    @Bean
    public UserOrderService userOrderService(UserOrderRepository userOrderRepository,
                                             UserRepository userRepository,
                                             GiftCertificateRepository giftCertificateRepository,
                                             UserOrderValidator userOrderValidator) {
        return new UserOrderService(userOrderRepository,
                userRepository,
                giftCertificateRepository,
                userOrderValidator);
    }

    @Bean
    protected BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserService userService(UserRepository userRepository, UserValidator userValidator, BCryptPasswordEncoder passwordEncoder) {
        return new UserService(userRepository, userValidator, passwordEncoder);
    }

    @Bean
    public UserValidator userValidator() {
        return new UserValidator();
    }

    @Bean
    public UserOrderValidator userOrderValidator() {
        return new UserOrderValidator();
    }

    @Bean
    public GiftCertificateValidator giftCertificateValidator() {
        return new GiftCertificateValidator();
    }


    @Bean
    public TagValidator tagValidator() {
        return new TagValidator();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
