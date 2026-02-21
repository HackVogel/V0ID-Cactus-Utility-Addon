package dev.hackvogel.v0id.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServerInfoManager {
    private static volatile String currentIp = "Loading...";
    private static volatile String currentReverse = "Loading...";
    private static volatile String currentNameServer = "Loading...";
    private static volatile String currentAsn = "Loading...";
    private static volatile String currentNetName = "Loading...";
    private static volatile String currentNetDesc = "Loading...";
    private static volatile String currentOrg = "Loading...";

    private static volatile boolean fetched = false;
    private static volatile String lastServerIp = "";

    public static void debugInfo(String targetIp) {
        if (targetIp == null || targetIp.isEmpty()) {
            // Fallback: aktuellen Server nehmen
            Minecraft client = Minecraft.getInstance();
            ServerData server = client.getCurrentServer();
            if (server == null) { reset(); return; }
            targetIp = server.ip;
        }

        if (targetIp.equals(lastServerIp) && fetched) return;

        lastServerIp = targetIp;
        fetched = false;
        resetDisplayData();

        final String finalTarget = targetIp;

        CompletableFuture.runAsync(() -> {
            try {
                ServerAddress serverAddress = ServerAddress.parseString(finalTarget);
                String host = serverAddress.getHost();

                resolveNameServers(host);
                String resolvedHost = resolveSRV(host);

                InetAddress address = InetAddress.getByName(resolvedHost);
                String ip = address.getHostAddress();
                currentIp = ip;

                HttpClient httpClient = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://ip-api.com/json/" + ip + "?fields=status,message,isp,org,as,reverse,query"))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

                if (json.has("status") && "success".equals(json.get("status").getAsString())) {
                    currentReverse = json.has("reverse") ? json.get("reverse").getAsString() : "N/A";
                    currentAsn = json.has("as") ? json.get("as").getAsString() : "N/A";
                    currentNetName = json.has("isp") ? json.get("isp").getAsString() : "N/A";
                    if (json.has("org")) {
                        currentOrg = json.get("org").getAsString();
                        currentNetDesc = currentOrg;
                    } else {
                        currentOrg = "N/A";
                        currentNetDesc = "N/A";
                    }
                } else {
                    currentReverse = "Failed";
                    currentAsn = "Failed";
                    currentNetName = "Failed";
                    currentNetDesc = "Failed";
                    currentOrg = "Failed";
                }

                fetched = true;
            } catch (Exception e) {
                e.printStackTrace();
                setErrorState();
            }
        });
    }

    public static void updateInfo() {
        Minecraft client = Minecraft.getInstance();
        ServerData server = client.getCurrentServer();

        if (server == null) {
            reset();
            return;
        }

        String serverAddressStr = server.ip;

        if (serverAddressStr.equals(lastServerIp) && fetched) {
            return;
        }

        lastServerIp = serverAddressStr;
        fetched = false;
        resetDisplayData();

        CompletableFuture.runAsync(() -> {
            try {
                ServerAddress serverAddress = ServerAddress.parseString(serverAddressStr);
                String host = serverAddress.getHost();

                resolveNameServers(host);

                String resolvedHost = resolveSRV(host);

                InetAddress address = InetAddress.getByName(resolvedHost);
                String ip = address.getHostAddress();
                currentIp = ip;

                HttpClient httpClient = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://ip-api.com/json/" + ip + "?fields=status,message,isp,org,as,reverse,query"))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

                if (json.has("status") && "success".equals(json.get("status").getAsString())) {
                    currentReverse = json.has("reverse") ? json.get("reverse").getAsString() : "N/A";
                    currentAsn = json.has("as") ? json.get("as").getAsString() : "N/A";
                    currentNetName = json.has("isp") ? json.get("isp").getAsString() : "N/A";
                    if (json.has("org")) {
                        currentOrg = json.get("org").getAsString();
                        currentNetDesc = currentOrg;
                    } else {
                        currentOrg = "N/A";
                        currentNetDesc = "N/A";
                    }
                } else {
                    currentReverse = "Failed";
                    currentAsn = "Failed";
                    currentNetName = "Failed";
                    currentNetDesc = "Failed";
                    currentOrg = "Failed";
                }

                fetched = true;
            } catch (Exception e) {
                e.printStackTrace();
                setErrorState();
            }
        });
    }

    private static void resolveNameServers(String domain) {
        if (isIpAddress(domain)) {
            currentNameServer = "N/A";
            return;
        }
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            env.put("java.naming.provider.url", "dns:");
            DirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(domain, new String[]{"NS"});
            Attribute ns = attrs.get("NS");
            if (ns != null) {
                List<String> nsList = new ArrayList<>();
                NamingEnumeration<?> en = ns.getAll();
                while (en.hasMore()) {
                    String nsRecord = en.next().toString();
                    if (nsRecord.endsWith(".")) nsRecord = nsRecord.substring(0, nsRecord.length() - 1);
                    nsList.add(nsRecord);
                }
                currentNameServer = String.join(", ", nsList);
            } else {
                currentNameServer = "None found";
            }
        } catch (Exception e) {
            currentNameServer = "Lookup Failed";
        }
    }

    private static String resolveSRV(String domain) {
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            env.put("java.naming.provider.url", "dns:");
            DirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes("_minecraft._tcp." + domain, new String[]{"SRV"});
            if (attrs != null && attrs.get("SRV") != null) {
                String[] parts = attrs.get("SRV").get().toString().split(" ");
                if (parts.length >= 4) {
                    String target = parts[3];
                    if (target.endsWith(".")) target = target.substring(0, target.length() - 1);
                    return target;
                }
            }
        } catch (Exception ignored) {}
        return domain;
    }

    private static boolean isIpAddress(String str) {
        return str.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
    }

    public static void reset() {
        fetched = false;
        lastServerIp = "";
        resetDisplayData();
        currentIp = "N/A";
    }

    private static void resetDisplayData() {
        currentIp = "Resolving...";
        currentReverse = "Loading...";
        currentNameServer = "Loading...";
        currentAsn = "Loading...";
        currentNetName = "Loading...";
        currentNetDesc = "Loading...";
        currentOrg = "Loading...";
    }

    private static void setErrorState() {
        currentIp = "Error";
        currentReverse = "Error";
        currentNameServer = "Error";
        currentAsn = "Error";
        currentNetName = "Error";
        currentNetDesc = "Error";
        currentOrg = "Error";
    }

    public static String getIp() { return currentIp; }
    public static String getReverse() { return currentReverse; }
    public static String getNameServer() { return currentNameServer; }
    public static String getAsn() { return currentAsn; }
    public static String getNetName() { return currentNetName; }
    public static String getNetDesc() { return currentNetDesc; }
    public static String getOrg() { return currentOrg; }
}