package bekhruz.com.cinemora.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
@Component
@RequiredArgsConstructor
public class Translator {
    private final ResourceBundleMessageSource messageSource;

    public String toLocale(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(msgCode, null, locale);
        if (message != null) {
            return message;
        }
        return msgCode;
    }
}
