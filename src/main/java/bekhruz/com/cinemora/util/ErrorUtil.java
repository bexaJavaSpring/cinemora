package bekhruz.com.cinemora.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorUtil {
    public static String getStacktrace(final Throwable error) {
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer, true);
        error.printStackTrace(printWriter);
        return writer.getBuffer().toString();
    }

    public static String parseErrorResponse(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);

            if (root.has("message")) {
                return root.get("message").asText();
            } else if (root.has("error")) {
                return root.get("error").asText();
            } else if (root.has("errorMessage")) {
                return root.get("errorMessage").asText();
            }

            return responseBody;
        } catch (Exception e) {
            return responseBody;
        }
    }
}
