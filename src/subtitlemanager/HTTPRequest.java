/*
 * Copyright (C) 2017 atulgpt <atlgpt@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package subtitlemanager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.*;

/**
 *
 * @atulgpt <atlgpt@gmail.com>
 */
public class HTTPRequest {

    private static final String USER_AGENT_SUBDB = "SubDB/1.0 (atulgpt/0.1; https://github.com/atulgpt/SubtitleDownloader.git";
    private static final String USER_AGENT_OPENSUB = "atulgpt";
    private static final String API_SUBDB = "http://api.thesubdb.com/";
    private static final String SANDBOX_API_SUBDB = "http://sandbox.thesubdb.com/";
    private static final String API_OPENSUB = "http://api.opensubtitles.org/xml-rpc";
    private static final String URL = API_SUBDB;
    private static final String ANNOYNIMOUS_USER_OPENSUB = "";
    private static final String ANNOYNIMOUS_PASS_OPENSUB = "";
    private static OpenSubToken openSubToken;
    private static SubtitleDownloaderUI subtitleDownloaderUI = null;

    public HTTPRequest() {
    }

    HTTPRequest(SubtitleDownloaderUI callingUI) {
        this();
        subtitleDownloaderUI = callingUI;
    }

    public void sendDownloadRequestsSUBDB(ArrayList<VideoInfo> videoInfoArray, String lang) {
        videoInfoArray.forEach((VideoInfo videoInfo) -> {
            try {
                if (!videoInfo.getMD5hash().equals("") && !videoInfo.isDownloaded()) {
                    String subtitle = sendGetSubtitleFromSUBDB(videoInfo.getMD5hash(), lang);
                    if (subtitle != null && !subtitle.equals("")) {
                        videoInfo.setSubtitle(subtitle);
                        videoInfo.setDownloaded(true);
                    }
                    System.out.println("subtitlemanager.HTTPRequest.sendDownloadRequestsSUBDB() subtile == null = " + (videoInfo.getSubtitle() == null) + " hash = " + videoInfo.getMD5hash() + " fileName = " + videoInfo.getFullFilePath());;
                } else {
                    if (!videoInfo.isDownloaded()) {
                        System.out.println("subtitlemanager.HTTPRequest.sendDownloadRequestsSUBDB() Hash for file = " + videoInfo.getFullFilePath() + " is " + videoInfo.getMD5hash());
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    void sendUploadRequests(ArrayList<String> videoHashArray, ArrayList<String> mSubtitleFilePathArray, String mLangs) {
        for (int i = 0; i < videoHashArray.size(); i++) {
            try {
                sendPostSubtitleSUBDB(videoHashArray.get(i), mSubtitleFilePathArray.get(i));

            } catch (Exception ex) {
                Logger.getLogger(HTTPRequest.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String[] sendRequestForLangsSUBDB() {
        return sendGetLangsSUBDB().split(",");
    }

    // HTTP GET request
    private String sendGetSubtitleFromSUBDB(String videoHash, String lang) throws Exception {
        String url = URL + "?action=download&hash=" + videoHash + "&language=" + lang;
        System.out.println("subtitlemanager.HTTPRequest.sendGet(): url- " + url);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT_SUBDB);
        int responseCode;
        try {
            responseCode = con.getResponseCode();
        } catch (IOException e) {
            System.out.println("subtitlemanager.HTTPRequest.sendGet() Error: " + e);
            SwingUtilities.invokeLater(() -> {
                if (subtitleDownloaderUI != null) {
                    subtitleDownloaderUI.informUI("Network is not working!\n Process interrupted. Try again!", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            return "";
        }
        System.out.println("Response Code : " + responseCode);
        switch (responseCode) {
            case 200:
                StringBuilder response;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                        response.append("\n");
                    }
                    in.close();
                }
                return response.toString();
            case 404:
                System.out.println("subtitlemanager.HTTPRequest.sendGet() Subtile not found, Response Code: " + responseCode);
                SwingUtilities.invokeLater(() -> {
                    if (subtitleDownloaderUI != null) {
                        subtitleDownloaderUI.informUI("Subtitle not found!\n(Response code: " + responseCode + ")", "Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                return "";
            case 402:
                System.out.println("subtitlemanager.HTTPRequest.sendGet() Malformed request!\n(Response code: " + responseCode + ")");
                SwingUtilities.invokeLater(() -> {
                    if (subtitleDownloaderUI != null) {
                        subtitleDownloaderUI.informUI("Malformed request!\n(Response code: " + responseCode + ")", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                return "";
            default:
                break;
        }
        return "";
    }

    // HTTP POST request
    private String sendPostSubtitleSUBDB(String videoHash, String subtitlePath) throws Exception {
        String url = URL + "?action=upload&hash=" + videoHash;
        String boundary = "xYzZY";
        String LINE_FEED = "\r\n";
        File subtitleFile = new File(subtitlePath);
        long dataLength = subtitleFile.length();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT_SUBDB);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        con.setRequestProperty("Content-Length", Long.toString(dataLength));
        // Send post request
        con.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {

            StringBuilder temp = new StringBuilder();
            wr.writeBytes("--" + boundary + LINE_FEED);
            temp.append("--").append(boundary).append(LINE_FEED);
            wr.writeBytes("Content-Disposition: form-data; name=" + "\"hash\"" + LINE_FEED);
            wr.writeBytes(LINE_FEED);
            temp.append("Content-Disposition: form-data; name=\"hash\"").append(LINE_FEED);
            temp.append(LINE_FEED);
            wr.writeBytes(videoHash + LINE_FEED);
            temp.append(videoHash).append(LINE_FEED);
            wr.writeBytes("--" + boundary + LINE_FEED);
            temp.append("--").append(boundary).append(LINE_FEED);
            wr.writeBytes("Content-Disposition: form-data; name=" + "\"file\"; " + "filename=" + "\"subtitle.srt\"" + LINE_FEED);
            temp.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(videoHash).append(".srt\"").append(LINE_FEED);
            wr.writeBytes("Content-Type: application/octet-stream");
            temp.append("Content-Type: application/octet-stream");
            wr.writeBytes(LINE_FEED);
            wr.flush();
            temp.append(LINE_FEED);
            wr.write(Files.readAllBytes(Paths.get(subtitlePath)));
            wr.writeBytes(LINE_FEED);
            temp.append(Arrays.toString(Files.readAllBytes(Paths.get(subtitlePath)))).append("\n len=").append(Files.readAllBytes(Paths.get(subtitlePath)).length);
            //temp.append(LINE_FEED);
            //wr.writeBytes("--"+boundary+LINE_FEED);
            //temp.append("--").append(boundary).append(LINE_FEED);
            wr.flush();
            System.out.println("check: \n" + temp.toString());
        }

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + url);
        System.out.println("Response Code : " + responseCode);
        System.out.println("dataLength: " + Long.toString(dataLength));

        StringBuffer response;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        //print result
        System.out.println(response.toString());
        return response.toString();
    }

    private String sendGetLangsSUBDB() {
        ArrayList<String> langArray = new ArrayList<>();
        String url = URL + "?action=languages";
        System.out.println("subtitlemanager.HTTPRequest.sendGet():get request for url- " + url);
        URL obj = null;
        try {
            obj = new URL(url);

        } catch (MalformedURLException ex) {
            Logger.getLogger(HTTPRequest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        if (obj == null) {
            return "";
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) obj.openConnection();

        } catch (IOException ex) {
            Logger.getLogger(HTTPRequest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        if (con == null) {
            return "";
        }
        try {
            // optional default is GET
            con.setRequestMethod("GET");

        } catch (ProtocolException ex) {
            Logger.getLogger(HTTPRequest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        //add request header
        int responseCode = -1;
        con.setRequestProperty("User-Agent", USER_AGENT_SUBDB);
        try {
            responseCode = con.getResponseCode();

        } catch (IOException ex) {
            Logger.getLogger(HTTPRequest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Response Code : " + responseCode);
        switch (responseCode) {
            case 200:
                StringBuilder response = null;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);

                    }
                } catch (IOException ex) {
                    Logger.getLogger(HTTPRequest.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                if (response == null) {
                    return "";
                }
                return response.toString();
            case 400:
                JOptionPane.showMessageDialog(null, "Malformed request!\n(Response code: " + responseCode + ")", "Error", JOptionPane.ERROR_MESSAGE);
                return "";
            default:
                return "";
        }
    }

    public void sendDownloadRequestsOpenSub(ArrayList<VideoInfo> videoInfoArray, String lang) {
        videoInfoArray.forEach((videoInfo) -> {
            try {
                if (!videoInfo.getChecksumHash().equals("") && !videoInfo.isDownloaded()) {
                    String subtitle = sendGetSubtitleOpenSub(videoInfo.getChecksumHash(), videoInfo.getFileByteLength(), lang);
                    if (subtitle != null && !subtitle.equals("")) {
                        videoInfo.setSubtitle(subtitle);
                        videoInfo.setDownloaded(true);
                    }
                    System.out.println("subtitlemanager.HTTPRequest.sendDownloadRequestsOpenSub() subtile == null = " + (videoInfo.getSubtitle() == null) + " hash = " + videoInfo.getChecksumHash() + " fileName = " + videoInfo.getFullFilePath());;
                } else {
                    if (!videoInfo.isDownloaded()) {
                        System.out.println("subtitlemanager.HTTPRequest.sendDownloadRequestsOpenSub() Hash for file = " + videoInfo.getFullFilePath() + " is " + videoInfo.getChecksumHash());
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    static public String sendGetSubtitleOpenSub(String videoHash, long movieSize, String lang) {
        String token = getTokenOpenSub(ANNOYNIMOUS_USER_OPENSUB, ANNOYNIMOUS_PASS_OPENSUB, lang, USER_AGENT_OPENSUB);
        if (token.equals("")) {
            return "";
        }
        int subFileID = getSubFileIDOpenSub(token, videoHash, movieSize);
        if (subFileID == -1) {
            return "";
        }
        String subtitle = downloadSubtitlesOpenSub(token, subFileID);
        return subtitle;
    }

    @SuppressWarnings("UseOfObsoleteCollectionType")
    static public String getTokenOpenSub(String userName, String password, String lang, String userAgent) {
        System.out.println("expired: " + OpenSubToken.isExpired() + " token: " + OpenSubToken.getToken());
        if (!OpenSubToken.isExpired()) {
            return OpenSubToken.getToken();
        }
        XmlRpcClient server;
        try {
            server = new XmlRpcClient(new URL(API_OPENSUB));
        } catch (MalformedURLException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        Vector params = new Vector();
        params.add(userName);
        params.add(password);
        params.add(lang);
        params.add(userAgent);
        Hashtable<String, String> result = null;
        try {
            result = (Hashtable) server.execute("LogIn", params);
        } catch (IOException | XmlRpcException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (result != null) {
            if (((String) result.get("status")).equals("200 OK")) {
                System.out.println("object: " + result + " token: " + (String) result.get("token"));
                String token = (String) result.get("token");
                OpenSubToken.setToken(token);
                OpenSubToken.setInitialTimeStamp(System.currentTimeMillis());
                System.out.println("token-------" + token);
                return token;
            }
        }
        return "";
    }

    @SuppressWarnings("UseOfObsoleteCollectionType")
    public static int getSubFileIDOpenSub(String tokenOpenSub, String movieHash, long movieSize) {
        XmlRpcClient server;
        try {
            server = new XmlRpcClient(new URL(API_OPENSUB));
        } catch (MalformedURLException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        Hashtable<String, Object> videoProps = new Hashtable<>();
        Hashtable<String, Object> result = new Hashtable<>();
        videoProps.put("moviehash", movieHash);
        videoProps.put("moviebytesize", Long.toString(movieSize));
        Object[] params = new Object[]{videoProps};
        Vector<Object> paramsArray = new Vector<>();
        paramsArray.add(tokenOpenSub);
        paramsArray.add(params);
        try {
            result = (Hashtable<String, Object>) server.execute("SearchSubtitles", paramsArray);
        } catch (XmlRpcException | IOException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (result != null) {
            if (((String) result.get("status")).equals("200 OK")) {
                Vector<Object> subInfoVector = (Vector<Object>) result.get("data");
                for (Object subInfoVector1 : subInfoVector) {
                    Hashtable<String, String> subInfo;
                    subInfo = (Hashtable<String, String>) subInfoVector.get(0);
                    if (subInfo.get("SubLanguageID").equals("eng")) {
                        return Integer.parseInt(subInfo.get("IDSubtitleFile"));
                    }
                }
            }
        }
        return -1;
    }

    @SuppressWarnings("UseOfObsoleteCollectionType")
    public static String downloadSubtitlesOpenSub(String tokenOpenSub, int IDSubtitleFile) {
        XmlRpcClient server;
        try {
            server = new XmlRpcClient(new URL(API_OPENSUB));
        } catch (MalformedURLException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        Object[] IDSubtitleFileArray = new Object[]{IDSubtitleFile};
        Hashtable<String, Object> result = new Hashtable<>();
        Vector<Object> params = new Vector<>();
        params.add(tokenOpenSub);
        params.add(IDSubtitleFileArray);
        try {
            result = (Hashtable<String, Object>) server.execute("DownloadSubtitles", params);
        } catch (XmlRpcException | IOException ex) {
            Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (result != null) {
            if (((String) result.get("status")).equals("200 OK")) {
                Vector<Object> subDataArray = (Vector<Object>) result.get("data");
                for (int i = 0; i < subDataArray.size(); i++) {

                    Hashtable<String, String> subContent;
                    subContent = (Hashtable<String, String>) subDataArray.get(i);
                    String subtitleBase64Encoded = subContent.get("data");
                    byte[] GZippedSubtitleByteArray = Base64.getDecoder().decode(subtitleBase64Encoded);
                    try {
                        ByteArrayInputStream bytein = new ByteArrayInputStream(GZippedSubtitleByteArray);
                        GZIPInputStream gzin = new GZIPInputStream(bytein);
                        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
                        int res = 0;
                        byte buf[] = new byte[1024 * 1024];
                        while (res >= 0) {
                            res = gzin.read(buf, 0, buf.length);
                            if (res > 0) {
                                byteout.write(buf, 0, res);
                            }
                        }
                        byte uncompressedSubByteArray[] = byteout.toByteArray();
                        return new String(uncompressedSubByteArray);
                    } catch (IOException ex) {
                        Logger.getLogger(HTTPRequest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return "";
    }

    private static class OpenSubToken {

        private static final long TOKEN_EXPIRE_TIME_MILLIS = 15 * 60 * 1000;
        private static final long THRESHOLD_TIME_MILLIS = 4 * 60 * 1000;
        private static String token;
        private static long initialTimeStamp = -1;

        private static String getToken() {
            return token;
        }

        private static void setToken(String token) {
            OpenSubToken.token = token;
        }

        private static void setInitialTimeStamp(long initialTimeStamp) {
            OpenSubToken.initialTimeStamp = initialTimeStamp;
        }

        private static boolean isExpired() {
            return ((System.currentTimeMillis() - initialTimeStamp) > (TOKEN_EXPIRE_TIME_MILLIS - THRESHOLD_TIME_MILLIS) || initialTimeStamp < 0);
        }
    }
}
