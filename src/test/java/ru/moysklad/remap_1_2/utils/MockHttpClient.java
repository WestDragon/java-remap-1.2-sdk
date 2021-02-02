package ru.moysklad.remap_1_2.utils;

import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class MockHttpClient extends CloseableHttpClient {
    @Getter
    private HttpRequest lastExecutedRequest;

    @Override
    protected CloseableHttpResponse doExecute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws IOException, ClientProtocolException {
        CloseableHttpResponse resp = new CloseableHttpResponse() {
            @Override
            public void close() throws IOException {

            }

            @Override
            public StatusLine getStatusLine() {
                return new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK");
            }

            @Override
            public void setStatusLine(StatusLine statusline) {

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code) {

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code, String reason) {

            }

            @Override
            public void setStatusCode(int code) throws IllegalStateException {

            }

            @Override
            public void setReasonPhrase(String reason) throws IllegalStateException {

            }

            @Override
            public HttpEntity getEntity() {
                HttpEntity se = null;
                try {
                    String uri = httpRequest.getRequestLine().getUri();
                    if (looksLikeMassUpdate(httpRequest)) {
                        se = new StringEntity("[]");
                    } else if (uri.contains("metadata/attributes/")) {
                        se = new StringEntity("{\"type\":\"string\",\"value\":\"STRING\"}");
                    } else if (uri.contains("positions") &&
                            httpRequest.getRequestLine().getMethod().equals("POST") ||
                            uri.endsWith("pricetype/")) {
                        se = new StringEntity("[]");
                    } else if ((uri.endsWith("contactpersons") ||
                                uri.endsWith("notes") ||
                                uri.endsWith("accounts") ||
                                uri.contains("images")
                            ) &&
                            httpRequest.getRequestLine().getMethod().equals("POST")) {
                        se = new StringEntity("[{}]");
                    }
                    else {
                        se = new StringEntity("{}");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return se;
            }

            @Override
            public void setEntity(HttpEntity entity) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public void setLocale(Locale loc) {

            }

            @Override
            public ProtocolVersion getProtocolVersion() {
                return null;
            }

            @Override
            public boolean containsHeader(String name) {
                return false;
            }

            @Override
            public Header[] getHeaders(String name) {
                return new Header[0];
            }

            @Override
            public Header getFirstHeader(String name) {
                return null;
            }

            @Override
            public Header getLastHeader(String name) {
                return null;
            }

            @Override
            public Header[] getAllHeaders() {
                return new Header[0];
            }

            @Override
            public void addHeader(Header header) {

            }

            @Override
            public void addHeader(String name, String value) {

            }

            @Override
            public void setHeader(Header header) {

            }

            @Override
            public void setHeader(String name, String value) {

            }

            @Override
            public void setHeaders(Header[] headers) {

            }

            @Override
            public void removeHeader(Header header) {

            }

            @Override
            public void removeHeaders(String name) {

            }

            @Override
            public HeaderIterator headerIterator() {
                return null;
            }

            @Override
            public HeaderIterator headerIterator(String name) {
                return null;
            }

            @Override
            public HttpParams getParams() {
                return null;
            }

            @Override
            public void setParams(HttpParams params) {

            }
        };

        lastExecutedRequest = httpRequest;
        return resp;
    }

    private static boolean looksLikeMassUpdate(HttpRequest request) throws IOException {
        if (request instanceof HttpPost && ((HttpPost) request).getEntity() != null) {
            String content = IOUtils.toString(((HttpPost) request).getEntity().getContent(), StandardCharsets.UTF_8);
            return "[]".equals(content);
        } else {
            return false;
        }
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public HttpParams getParams() {
        return null;
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        return null;
    }

    public void reset() {
        lastExecutedRequest = null;
    }
}