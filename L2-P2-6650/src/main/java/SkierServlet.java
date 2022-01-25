import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "SkierServlet", value = "/SkierServlet")
public class SkierServlet extends HttpServlet {
    /**
     * Reference: https://mkyong.com/java/java-convert-string-to-int/
     */
    private boolean isDigit(String input) {
        // null or length < 0, return false.
        if (input == null || input.length() < 0)
            return false;

        // empty, return false
        input = input.trim();
        if ("".equals(input))
            return false;

        if (input.startsWith("-")) {
            // negative number in string, cut the first char
            return input.substring(1).matches("[0-9]*");
        } else {
            // positive number, good, just check
            return input.matches("[0-9]*");
        }
    }

    /**
     * Validate the request url path according to the API spec
     */
    private boolean isUrlValid(String[] urlParts) {
        // urlPath  = "/1/seasons/2019/days/1/skiers/123"
        // urlParts = [, 1, seasons, 2019, days, 1, skiers, 123]
        int resortID = -1;
        int seasonID = -1;
        int dayID = -1;
        int skierID = -1;

        switch (urlParts.length) {
            case 8:
                if (isDigit(urlParts[1])) {
                    resortID = Integer.parseInt(urlParts[1]);
                }
                if (urlParts[2].equals("seasons")) {
                    if (isDigit(urlParts[3])) {
                        seasonID = Integer.parseInt(urlParts[3]);
                    }
                }
                if (urlParts[4].equals("days")) {
                    if (isDigit(urlParts[5])) {
                        dayID = Integer.parseInt(urlParts[5]);
                    }
                }
                if (urlParts[6].equals("skiers")) {
                    if (isDigit(urlParts[7])) {
                        skierID = Integer.parseInt(urlParts[7]);
                    }
                }

                if (resortID < 0 || seasonID < 0 || dayID < 1 || dayID > 366 || skierID < 0) {
                    return false;
                }
                return true;

            case 3:
                if (isDigit(urlParts[1])) {
                    resortID = Integer.parseInt(urlParts[1]);
                }
                if (!urlParts[2].equals("vertical")) {
                    return false;
                }
                if (skierID < 0) {
                    return false;
                }
                return true;

            default:
                return false;
        }

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing paramterers");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            res.setStatus(HttpServletResponse.SC_OK);

            // do any sophisticated processing with urlParts which contains all the url params
            // TODO: get requested data from DB & write to res

            res.getWriter().write("It works!");
        }
    }

    private String readBody(BufferedReader buffIn) throws IOException {
        String body = "";
        String line;
        while ((line = buffIn.readLine()) != null) {
            body += line;
        }
        return body;
    }

    /**
     * Reference: https://stackoverflow.com/a/10514517/11954837
     */
    private Map<String, Integer> parseBody(String body) {
        Map<String, Integer> bodyMap = new HashMap<String, Integer>();
        String[] pairs = body.split(",");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            bodyMap.put(keyValue[0].replaceAll("^\"|\"$", ""), Integer.valueOf(keyValue[1]));
        }

        return bodyMap;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing paramterers");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            res.setStatus(HttpServletResponse.SC_OK);

            String body = readBody(req.getReader());
            Map<String, Integer> bodyMap = parseBody(body);

            //TODO: Write data into & write to res

            res.getWriter().write("It works!");
        }
    }
}
