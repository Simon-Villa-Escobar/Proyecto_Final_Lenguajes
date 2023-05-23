package model;

import controller.Server;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

public class Json {    
    private static final String METODO = "(OPTIONS|GET|HEAD|POST|PUT|DELETE|TRACE|CONNECT)";
    private static final String ESQ = "(http|https)";
    private static final String HOST = "([A-Za-z0-9_+-./:=?&%;]+)+";
    private static final String PROTOCOLO = "([A-Za-z0-9_+-./:=?&%;]+)+";
    private static final String GENERICO = ".+";
    private static final String CABEZA =  "[A-Za-z0-9_+-./=?&%;]+";
    private static final String CAB = "[A-Za-z0-9_+-./:=?&%;]+";
    private static final String JSON = "\\{\\s*\"(\\w+)\":\\s*(\"[^\"]*\"|\\d+|\\{[^{}]*\\}|\\[[^\\[\\]]*\\])\\s*(,\\s*\"(\\w+)\":\\s*(\"[^\"]*\"|\\d+|\\{[^{}]*\\}|\\[[^\\[\\]]*\\])\\s*)*\\}";
    
    private String metodo;
    private String protocolo;
    
    Server request = new Server();
    
    public String getMetodo(){
        return metodo;
    }

    public void setMetodo(){
        this.metodo = metodo;
    }
    
    public String getProtocolo(){
        return protocolo;
    }

    public void setProtocolo(){
        this.protocolo = protocolo;
    }
    
    public static Map<String, Object> parseJSON(String json) {
        // Quita las llaves y comillas del JSON
        json = json.replaceAll("[{}\"]", "");


        //Mapa para almacenar las variables
        Map<String, Object> variables = new HashMap<>();

        // Divide el JSON en pares clave-valor y los almacena en el mapa
        String[] keyValuePairs = json.split(",");
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":");

            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            variables.put(key, value);

        }
        return variables;
    }
    
    public void ApiRest(String linea, String cabecera, String body, int id) {
        String lineaOutput = "";
        String cabeceraOutput = "";
        String bodyOutput = "";
        Boolean parserOk = false;

        Pattern pattern = Pattern.compile(
            String.format("%s %s://%s %s(\\r\\n)?(\\n)?", METODO, ESQ, HOST, PROTOCOLO),Pattern.MULTILINE
        );                   
        Matcher matcher = pattern.matcher(linea);

        if (matcher.matches()) {
            

            metodo = matcher.group(1);
            String esquema = matcher.group(2);
            String servidor = matcher.group(3);
            protocolo = matcher.group(4);
                        
            pattern = Pattern.compile(String.format("(%s): (%s)(\\r\\n)?(\\n)?", CABEZA, CAB));
            matcher = pattern.matcher(cabecera);

            int contador = 0;
            while (matcher.find()) {
                contador = contador + 1;
                String headerName = matcher.group(1);
                String headerValue = matcher.group(2);
            }
            int lineas = cabecera.split("\n").length;

            if (lineas == contador){

                if(!body.isEmpty()){
                    pattern = Pattern.compile(
                    String.format("%s(\\r\\n)?(\\n)?", JSON),Pattern.MULTILINE);
                
                    matcher = pattern.matcher(body);
                    
                    if (matcher.matches()) {
                        parserOk = true;
                        lineaOutput = protocolo + " 200 OK";
                    }
                    else{
                        lineaOutput = "400 BAD REQUEST\n";
                        cabeceraOutput = "PARSER-ERROR: Cuerpo de peticion no valida. Json erróneo" + body;
                    }
                }
                else {
                    parserOk = true;
                    lineaOutput = protocolo + " 200 OK";
                }
            }
            else {
                lineaOutput = "400 BAD REQUEST\n";
                cabeceraOutput = "PARSER-ERROR: Cabecera de peticion no valida. Lineas cabecera incorrectas: " + (lineas-contador);
            }
        } 
        else {
            pattern = Pattern.compile(String.format("%s%s(\\r\\n)?(\\n)?", METODO, GENERICO));
            matcher = pattern.matcher(linea);    
            if (matcher.matches()) {
                pattern = Pattern.compile(String.format("%s %s%s(\\r\\n)?(\\n)?", METODO, ESQ, GENERICO));
                matcher = pattern.matcher(linea);    
                if (matcher.matches()) {
                    pattern = Pattern.compile(String.format("%s %s://%s%s(\\r\\n)?(\\n)?", METODO, ESQ, HOST, GENERICO));
                    matcher = pattern.matcher(linea);    
                    if (matcher.matches()) {
                        pattern = Pattern.compile(String.format("%s %s://%s %s%s(\\r\\n)?(\\n)?", METODO, ESQ, HOST, PROTOCOLO, GENERICO));
                        matcher = pattern.matcher(linea);    
                        if (matcher.matches()) {

                        }
                        else{
                            lineaOutput = "400 BAD REQUEST\n";
                            cabeceraOutput = "PARSER-ERROR: El protocolo no está en la linea de peticion \n";
                        }
                    }
                    else{
                        lineaOutput = "400 BAD REQUEST\n";
                        cabeceraOutput = "PARSER-ERROR: El servidor no está en la linea de peticion \n";
                    }
                }
                else{
                    lineaOutput = "400 BAD REQUEST\n";
                    cabeceraOutput = "PARSER-ERROR: El esquema debe ser http o https \n";
                }
            }
            else{
                lineaOutput = "400 BAD REQUEST\n";
                cabeceraOutput = "PARSER-ERROR: El metodo debe ser una de estas opciones (OPTIONS|GET|HEAD|POST|PUT|DELETE|TRACE|CONNECT) \n";
            }

            lineaOutput = "400 BAD REQUEST\n";
        }
        if (parserOk){
            switch (metodo) {
                case "GET" -> {
                    if (id == 0) {
                        request = new Server("GET", "http://localhost:8080/api/users", "Content-Type: application/json", "");

                    } else {
                        System.out.println(lineaOutput);
                        request = new Server("GET", "http://localhost:8080/api/users", "Content-Type: application/json", "", id);
                    }
                }
                case "POST" -> {
                    lineaOutput = protocolo + " 201 CREATED";
                    System.out.println(lineaOutput);
                    Map<String, Object> variables = parseJSON(body);
                    String usuario = variables.get("nombres") + "," + variables.get("email") + "," + variables.get("phone");
                    request = new Server("POST", "http://localhost:8080/api/users", "Content-Type: application/json", usuario);
                }
                case "PUT" -> {
                    lineaOutput = protocolo + " 201 CREATED";
                    System.out.println(lineaOutput);
                    Map<String, Object> variables1 = parseJSON(body);
                    String usuario1 = variables1.get("nombres") + "," + variables1.get("email") + "," + variables1.get("phone");
                    request = new Server("PUT", "http://localhost:8080/api/users", "Content-Type: application/json", usuario1, id);
                }
                case "DELETE" -> {
                    System.out.println(lineaOutput);
                    request = new Server("DELETE", "http://localhost:8080/api/users", "Content-Type: application/json", "", id);
                }
                default -> System.out.println("Método incorrecto");
            }
        }
        else {
            System.out.println(lineaOutput + cabeceraOutput + bodyOutput);
        }
    }
}
