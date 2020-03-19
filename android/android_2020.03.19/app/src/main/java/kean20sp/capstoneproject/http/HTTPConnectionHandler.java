package kean20sp.capstoneproject.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;


public class HTTPConnectionHandler {

    public HTTPConnectionHandler(){

    }

//    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
//        protected Long doInBackground(URL... urls) {
//            int count = urls.length;
//            long totalSize = 0;
//            for (int i = 0; i < count; i++) {
//                totalSize += Downloader.downloadFile(urls[i]);
//                publishProgress((int) ((i / (float) count) * 100));
//                // Escape early if cancel() is called
//                if (isCancelled()) break;
//            }
//            return totalSize;
//        }
//
//        protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
//        }
//
//        protected void onPostExecute(Long result) {
//            showDialog("Downloaded " + result + " bytes");
//        }
//    }


    public String makeRequest(String host, String path, List<NameValuePair> pairs){
        try{
            URIBuilder uribuilder = new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPath(path);
            URI uri = uribuilder.build();
            HttpPost post = new HttpPost(uri);

            UrlEncodedFormEntity aentity = new UrlEncodedFormEntity(pairs,"UTF-8");
            post.setEntity(aentity);

            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(post); //<-- not working
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                out.close();
                return responseString;
                //..more logic
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                System.err.println(statusLine.getReasonPhrase());
                return null;
            }

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

//    public String makeRequest(String urlString, String POST_params){
//        OutputStream out = null;
//        try{
//            URL url = new URL(urlString);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setReadTimeout(10000);
//            con.setConnectTimeout(15000);
//            con.setRequestMethod("POST");
//            con.setDoInput(true);
//            con.setDoOutput(true);
//
//            out = new BufferedOutputStream(con.getOutputStream());
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
//            writer.write(POST_params);
//            writer.flush();
//            writer.close();
//            out.close();
//
//            con.connect();
//            Scanner input = new Scanner(con.getInputStream());
//            String code = input.next();
//            return code;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
}
