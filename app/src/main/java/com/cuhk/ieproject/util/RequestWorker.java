package com.cuhk.ieproject.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.cuhk.ieproject.R;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anson on 1/9/2016.
 */
public class RequestWorker {
    public static final String baseUrl ="http://cantoneseopera.uthinktech.com";
    public static final String apiUrl = baseUrl;

    private static RequestWorker mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private RequestWorker(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized RequestWorker getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestWorker(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public static abstract class MyJsonRequest<T> extends JsonRequest<T>{
        protected Context context;

        public MyJsonRequest(Context context, int method, String url, String requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener) {
            super(method, url, requestBody, listener, errorListener);
            this.context = context;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<>();

            return headers;
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            try {
                String json = new String(
                        response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                JSONObject jsonObj = new JSONObject(json);
                Log.i("RequestWorker", "Response Body: " + jsonObj.toString());
                T t = convertResponse(jsonObj);

                return Response.success(
                        t,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException e) {
                return Response.error(new ParseError(e));
            }
        }

        public abstract T convertResponse(JSONObject jsonObject);
    }

    public static abstract class PostRequest<T> extends MyJsonRequest<T>{
        protected Map<String, String> requestBody;

        public PostRequest(Context context, String path, Map<String, String> requestBody, Response.Listener<T> listener, final RequestWorker.ErrorListener errorListener){
            super(context, Method.POST, RequestWorker.apiUrl + path, null, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Error myError;
                    try {
                        JSONObject json = new JSONObject(new String(
                                error.networkResponse.data,
                                HttpHeaderParser.parseCharset(error.networkResponse.headers)));
                        myError = new Error();


                    } catch (UnsupportedEncodingException | JSONException | NullPointerException e) {
                        e.printStackTrace();
                        myError = new Error();
                    }
                    errorListener.onErrorResponse(myError);
                }
            });
            this.requestBody = requestBody;
            Log.i("RequestWorker", "Request URL:" + path);
            Log.i("RequestWorker", "Request Method:" + "POST");
            Log.i("RequestWorker", "Request Body: " + Arrays.toString(requestBody.entrySet().toArray()));
        }

        @Override
        public String getBodyContentType() {
            return "application/x-www-form-urlencoded; charset=UTF-8";
        }

        @Override
        public byte[] getBody() {
            try {
                String query = "";
                Uri uri = Uri.parse(query);
                Uri.Builder builder = uri.buildUpon();
                for (HashMap.Entry<String, String> entry : requestBody.entrySet()) {
                    builder.appendQueryParameter(entry.getKey(), entry.getValue());
                }
                query = builder.build().getEncodedQuery();

                return query.getBytes(PROTOCOL_CHARSET);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static abstract class GetRequest<T> extends MyJsonRequest<T>{
        public GetRequest(Context context, String path, Map<String, String> requestBody, Response.Listener<T> listener, final RequestWorker.ErrorListener errorListener){
            super(context, Method.GET, computeGetUri(RequestWorker.apiUrl + path, requestBody), null, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Error myError;
                    try {
                        JSONObject json = new JSONObject(new String(
                                error.networkResponse.data,
                                HttpHeaderParser.parseCharset(error.networkResponse.headers)));
                        myError = new Error();

                    } catch (UnsupportedEncodingException | JSONException | NullPointerException e) {
                        e.printStackTrace();
                        myError = new Error();
                    }
                    errorListener.onErrorResponse(myError);
                }
            });
            Log.i("RequestWorker", "Request URL:" + path);
            Log.i("RequestWorker", "Request Method:" + "GET");
            Log.i("RequestWorker", "Request Body: " + Arrays.toString(requestBody.entrySet().toArray()));
        }

        private static String computeGetUri(String uri, Map<String, String> requestBody){
            Uri.Builder builder = Uri.parse(uri)
                    .buildUpon();

            for (Map.Entry<String, String> entry : requestBody.entrySet()){
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }

            return builder.build().toString();
        }
    }

    public static abstract class UploadRequest<T> extends MyJsonRequest<T> {
        private MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

        public UploadRequest(Context context, String path, String fileFieldName, File file, Response.Listener<T> listener, final RequestWorker.ErrorListener errorListener) {
            super(context, Method.POST, RequestWorker.apiUrl + path, null, listener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Error myError;
                    try {
                        JSONObject json = new JSONObject(new String(
                                error.networkResponse.data,
                                HttpHeaderParser.parseCharset(error.networkResponse.headers)));
                        myError = new Error();

                    } catch (UnsupportedEncodingException | JSONException | NullPointerException e) {
                        e.printStackTrace();
                        myError = new Error();
                    }
                    errorListener.onErrorResponse(myError);
                }
            });
        }

        @Override
        public byte[] getBody(){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            return bos.toByteArray();
        }

        @Override
        public String getBodyContentType() {
            return "";
        }
    }

    public static class InputStreamVolleyRequest extends Request<byte[]> {
        private final Response.Listener<byte[]> mListener;
        //create a static map for directly accessing headers
        public Map<String, String> responseHeaders ;

        public InputStreamVolleyRequest(String mUrl,Response.Listener<byte[]> listener,
                final ErrorListener errorListener) {

            super(Method.GET, mUrl, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Error myError;
                    try {
                        JSONObject json = new JSONObject(new String(
                                error.networkResponse.data,
                                HttpHeaderParser.parseCharset(error.networkResponse.headers)));
                        myError = new Error();

                    } catch (UnsupportedEncodingException | JSONException | NullPointerException e) {
                        e.printStackTrace();
                        myError = new Error();
                    }
                    errorListener.onErrorResponse(myError);
                }
            });
            // this request would never use cache since you are fetching the file content from server
            setShouldCache(false);
            mListener = listener;
        }

        @Override
        protected void deliverResponse(byte[] response) {
            mListener.onResponse(response);
        }

        @Override
        protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

            //Initialise local responseHeaders map with response headers received
            responseHeaders = response.headers;

            //Pass the response data here
            return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

    public static abstract class ErrorListener {
        public abstract void onErrorResponse(Error error);
    }
}
