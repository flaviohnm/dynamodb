package br.com.dynamodb.config;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConstantsTest {

    @Test
    void construtorDeveSerPrivadoELancarExcecaoAoSerInvocadoPorReflection() throws Exception {
        Constructor<Constants> construtor = Constants.class.getDeclaredConstructor();

        assertThat(Modifier.isPrivate(construtor.getModifiers())).isTrue();

        construtor.setAccessible(true);

        assertThatThrownBy(construtor::newInstance)
                .isInstanceOf(InvocationTargetException.class)
                .cause()
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Classe utilitária não pode ser instanciada");
    }

    @Test
    void timezoneDeveSerMenosTres() {
        assertThat(Constants.TIMEZONE).isEqualTo(-3);
    }

    @Test
    void plusMonthDeveSerTres() {
        assertThat(Constants.PLUS_MONTH).isEqualTo(3);
    }

    @Test
    void timezoneRecifeDeveSerAmericaRecife() {
        assertThat(Constants.TIMEZONE_RECIFE).isEqualTo("America/Recife");
    }

    @Test
    void formatterDeveFormatarDataNoPadraoDiaMesAnoHoraMinutoSegundo() {
        LocalDateTime data = LocalDateTime.of(2024, 4, 15, 10, 30, 45);

        String resultado = data.format(Constants.FORMATTER);

        assertThat(resultado).isEqualTo("15/04/2024 10:30:45");
    }

    @Test
    void formatterDeveUsarRelogioDeDozeHorasParaMeiaNoite() {
        LocalDateTime meiaNoite = LocalDateTime.of(2024, 1, 1, 0, 0, 0);

        String resultado = meiaNoite.format(Constants.FORMATTER);

        assertThat(resultado).isEqualTo("01/01/2024 12:00:00");
    }
}
