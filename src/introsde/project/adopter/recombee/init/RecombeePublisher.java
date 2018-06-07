package introsde.project.adopter.recombee.init;

import javax.xml.ws.Endpoint;

import introsde.project.adopter.recombee.soap.RecombeeImpl;

public class RecombeePublisher {
    public static String SERVER_URL = "http://localhost";
    public static String PORT = "6908";
    public static String BASE_URL = "/ws/recombee";

    public static String getEndpointURL() {
        return SERVER_URL+":"+PORT+BASE_URL;
    }

    public static void main(String[] args) {
        String endpointUrl = getEndpointURL();
        System.out.println("Starting recombee Service...");
        System.out.println("--> Published at = "+endpointUrl);
        Endpoint.publish(endpointUrl, new RecombeeImpl());
    }
}
